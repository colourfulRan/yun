package cqjtu.ds.yun.utils;

import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import cqjtu.ds.yun.service.domain.ConstantConfig;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class AliyunOSSUtil {
   // @Autowired
    //private static ConstantConfig constantConfig=new ConstantConfig();
    private static final org.slf4j.Logger logger= LoggerFactory.getLogger(AliyunOSSUtil.class);
    /*private static String endpoint=constantConfig.getLXIMAGE_END_POINT();
    private static String accessKeyId=constantConfig.getLXIMAGE_ACCESS_KEY_ID();
    private static String accessKeySecret=constantConfig.getLXIMAGE_ACCESS_KEY_SECRET();
    private static String bucketName=constantConfig.getLXIMAGE_BUCKET_NAME1();
    private static String fileHost=constantConfig.getLXIMAGE_FILE_HOST();*/
    private static String endpoint="oss-cn-shanghai.aliyuncs.com";
    private static String accessKeyId="LTAIjkWdA4qPnOJ9";
    private static String accessKeySecret="tMCv7Ql44yeYVVMYo6cB4Fk74inIJW";
    private static String bucketName="osswtc";
    private static String fileHost="root";
    // 创建一个要存放的Object的名称，使用文件名怕重复，直接改名+存入数据库
    private static String key;

    //创建一个可重用固定线程数的线程池
    private static ExecutorService executorService= Executors.newFixedThreadPool(10);

    private  static OSS ossClient=null;

    //partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
    //线程安全，对象加锁
    private static List<PartETag> partETags=Collections.synchronizedList(new ArrayList<PartETag>());

    /**上传文件**/
    public String upLoad(File file){

        //判断文件
        if (file==null){
            return null;
        }
        System.out.println(endpoint+accessKeyId);

        key="time1854";
        ClientBuilderConfiguration conf=new ClientBuilderConfiguration();
        //连接空闲超时时间，超时则关闭
        conf.setIdleConnectionTime(1000);
        //创建OSSClient实例
        ossClient=new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret,conf);
        //判断容器是否存在，不存在就创建
        /*if (!ossClient.doesBucketExist(bucketName)){
            //创建存储空间
            ossClient.createBucket(bucketName);
            CreateBucketRequest createBucketRequest=new CreateBucketRequest(bucketName);
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            ossClient.createBucket(createBucketRequest);
        }*/
        /**分片上传**/
        /**1.初始化一个分片上传事件**/
        InitiateMultipartUploadRequest request=new InitiateMultipartUploadRequest(bucketName,key);
        InitiateMultipartUploadResult result=ossClient.initiateMultipartUpload(request);
        try{
            //返回uploadId，它是分片上传事件的唯一标识，可以根据这个ID来发起相关操作，如取消分片上传、查询分片上传等
            String uploadId=result.getUploadId();
            System.out.print(uploadId+"   ");
            /**2.上传分片**/

            //计算文件有多少个分片
            long partSize=5*1024*1024L;
            long fileLength=file.length();
            int partCount= (int) (fileLength/partSize);
            if(fileLength % partSize!=0){
                partCount++;
            }
            if(partCount>10000){
                throw new RuntimeException("Total parts count should not exceed 10000");
            }else {
                System.out.println("总块数："+partCount);
            }
            //遍历分片上传
            logger.info("---OSS文件上传开始-------"+file.getName());
            for (int i=0;i<partCount;i++){
                long startPos=i*partSize;
                //是否为最后一块分片
                long curPartSize=(i+1==partCount)?(fileLength-startPos):partSize;
                executorService.execute(new PartUploader(file,startPos,curPartSize,i+1,uploadId));
            }
            //等待所有的分片完成
            executorService.shutdown();
            while (!executorService.isTerminated()){
                try {
                    executorService.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            }
            //验证是否所有的分片都完成
            if(partETags.size()!=partCount){
                throw new IllegalStateException("文件的某些部分上传失败！");
            }else {
                System.out.println("成功上传文件"+file.getName());
            }
            /**3.完成分片上传**/
            //排序。partETags必须按分片号升序排列
            Collections.sort(partETags, new Comparator<PartETag>() {
                @Override
                public int compare(PartETag o1, PartETag o2) {
                    return o1.getPartNumber()-o2.getPartNumber();
                }
            });
            System.out.println("Completing to upload multiparts\n");
            //在执行该操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
            System.out.println(uploadId);
            CompleteMultipartUploadRequest completeMultipartUploadRequest=new CompleteMultipartUploadRequest(bucketName,key,uploadId,partETags);
            ossClient.completeMultipartUpload(completeMultipartUploadRequest);
            //设置文件路径和名称
            // String fileUrl=fileHost+"/"+file.getName();
            //上传文件
            //PutObjectResult result=ossClient.putObject(new PutObjectRequest(bucketName,fileUrl,file));
            //设置权限（公开读）
            ossClient.setBucketAcl(bucketName,CannedAccessControlList.PublicRead);
            if (result!=null){
                logger.info("---OSS文件上传成功---");
            }
        }catch (OSSException oe){
            logger.error(oe.getMessage());
        }catch (ClientException ce){
            logger.error(ce.getErrorMessage());
        } finally {
            if (ossClient!=null)
                //关闭OSSClient
                ossClient.shutdown();
        }
        return null;
    }

    private static class PartUploader implements Runnable {

        private File localFile;
        private long startPos;
        private long partSize;
        private int partNumber;
        private String uploadId;

        public PartUploader(File localFile,long startPos,long partSize,int partNumber,String uploadId){
            this.localFile=localFile;
            this.startPos=startPos;
            this.partNumber=partNumber;
            this.uploadId=uploadId;
            this.partSize=partSize;
        }

        @Override
        public void run() {
            InputStream inputStream=null;
            try{
                inputStream=new FileInputStream(this.localFile);
                //跳过已经上传的分片
                inputStream.skip(startPos);
                UploadPartRequest uploadPartRequest=new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setKey(key);
                uploadPartRequest.setUploadId(this.uploadId);
                System.out.println(uploadId);
                uploadPartRequest.setInputStream(inputStream);
                //设置分片大小。除了最后一个分片没有大小限制，其他分片最小为100KB
                uploadPartRequest.setPartSize(this.partSize);
                //设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围,OSS将返回InvalidArgum的错误码
                uploadPartRequest.setPartNumber(this.partNumber);
                //每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会根据分片号排序组成完整的文件。
                UploadPartResult uploadPartResult=ossClient.uploadPart(uploadPartRequest);
                System.out.println("Part#"+this.partNumber+"done\n");
                //每次上传分片之后，OSS的返回结果会包含一个PartETag。PartETag将被保存到PartETags中。
                synchronized (partETags) {
                    partETags.add(uploadPartResult.getPartETag());
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                if(inputStream!=null){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        }
    }


}

