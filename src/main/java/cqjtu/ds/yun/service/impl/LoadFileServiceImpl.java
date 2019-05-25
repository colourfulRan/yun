package cqjtu.ds.yun.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyun.oss.model.OSSObject;
import cqjtu.ds.keycspfacade.SignatureService;
import cqjtu.ds.yun.dal.FileRepo;
import cqjtu.ds.yun.service.LoadFileService;
import cqjtu.ds.yun.service.domain.DomainFile;
import cqjtu.ds.yun.utils.*;
import cqjtu.ds.yunserverfacade.CheckService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

import java.io.*;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


@Component
public class LoadFileServiceImpl implements LoadFileService {
    private final org.slf4j.Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;
    @Autowired
    private FileRepo fileRepo;
    @Reference
    private SignatureService signatureService;
    @Reference
    private CheckService checkService;

    @Override
    public boolean uploadFile(File file,String fileType, HttpSession session) {
        long start = System.currentTimeMillis();
        String key= MD5Utils.getMD5ByFile(file);
        BigInteger blindMsg=addBlindFactor(key);
        BigInteger blindSig=signatureService.getBlindSignatureSerectKey(blindMsg);
        String serectKey=reductionSignature(blindSig);
        File encrypfile=new File("encrypfile");
        encrypfile= AESUtils.encryptFile(file,encrypfile,serectKey);
        String fileHash=MD5Utils.getMD5ByFile(encrypfile);
        long end = System.currentTimeMillis();
        System.out.print("盲签名时间：");
        System.out.println(end-start);
        int userId= (int) session.getAttribute("uid");
        Map<String,Object> map=checkService.checkHash(fileHash,userId);
        boolean flag=true;
        long start1 = System.currentTimeMillis();
        if(!(Boolean) map.get("result")){
            //云端不存在相同文件,文件首次上传
            List<String> partHashs = null;
            try {
                partHashs= FileUtil.splitBySize(file,50);
            } catch (IOException e) {
                e.printStackTrace();
            }
         //   hashFilter.saveFilterToFile("src/bloomFilter.obj");
         //   File filterFile=new File("src/bloomFilter.obj");

            checkService.save(fileHash,partHashs,userId);
            long end1 = System.currentTimeMillis();
            System.out.print("签名文件时间：");
            System.out.println(end1-start1);
            aliyunOSSUtil.upLoad(encrypfile,session,fileHash);
        }else {
            //云端存在相同文件
            long start2= System.currentTimeMillis();
            List<Integer> challenges= (List<Integer>) map.get("challenges");
            int challId=challenges.get(challenges.size()-1);
            List<String> challengeHash=FileUtil.splitByChallenges(file,50,challenges);
            flag=checkService.checkChallenge(challengeHash,challId);
            long end2 = System.currentTimeMillis();
            System.out.print("挑战认证时间：");
            System.out.println(end2-start2);
        }
        if (flag){
            DomainFile domainFile=new DomainFile();
            domainFile.setFileHash(fileHash);
            domainFile.setFileName(file.getName());
            domainFile.setSecretKey(serectKey);
            domainFile.setTypeId(getFileType(fileType));
            domainFile.setFileSize((int) Math.ceil(file.length()/1024.0));
            domainFile.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            domainFile.setUserId(userId);
            domainFile.setFileValid(0);
            fileRepo.save(domainFile);
            session.setAttribute("uploadPercent",100);
        }
        encrypfile.delete();
        file.deleteOnExit();
        return flag;
    }




    public int getFileType(String fileType){
        String[] types=fileType.split("/");
        int type;
        switch (types[0]){
            case "image":type=1;break;
            case "video":type=3;break;
            case "audio":type=4;break;
            case "text":type=5;break;
            case "pdf":type=6;break;
            case "vnd.openxmlformats-officedocument.wordprocessingml.document":type=7;break;
            case "msword":type=7;break;
            case "vnd.ms-powerpoint":type=8;break;
            case "vnd.openxmlformats-officedocument.presentationml.presentation":type=8;break;
            case "vnd.ms-excel":type=9;break;
            case "vnd.openxmlformats-officedocument.spreadsheetml.sheet":type=9;break;
            default:type=2;
        }
        return type;
    }

    /**
     * 将原消息盲化
     */
    public BigInteger addBlindFactor(String key){
        BigInteger blindMsg=BigInteger.ZERO;
        //将十六进制表示的值转化为BigInteger
        BigInteger m=new BigInteger(key,16);
        //盲因子
        BigInteger factor=new BigInteger("317127853611181057365928393691525761187227861954");
        try {
            ObjectInputStream ois1=new ObjectInputStream(new FileInputStream("src/public.key"));
            RSAPublicKey pubKey= (RSAPublicKey) ois1.readObject();
            ois1.close();
            BigInteger e=pubKey.getPublicExponent();
            BigInteger n=pubKey.getModulus();
            blindMsg= BlindSignature.blindHideMsg(m,factor,e,n);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return blindMsg;
    }

    /**
     * 解盲得到签名的密钥
     */
    public String reductionSignature(BigInteger blindSig){
        String serectKey=null;
        try {
            ObjectInputStream ois1=new ObjectInputStream(new FileInputStream("src/public.key"));
            RSAPublicKey pubKey= (RSAPublicKey) ois1.readObject();
            ois1.close();
            BigInteger n=pubKey.getModulus();
            //盲因子
            BigInteger factor=new BigInteger("317127853611181057365928393691525761187227861954");
            BigInteger sig=BlindSignature.blindRetriveSig(blindSig,factor,n);
            serectKey=sig.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return serectKey;
    }


    //下载文件
    public File downloadFile(int fileId){
        DomainFile file=fileRepo.findByFileId(fileId);
        OSSObject ossObject=aliyunOSSUtil.downloadFile(file.getFileHash());
        InputStream inputStream=ossObject.getObjectContent();
        File deFile=new File(file.getFileName());
        //解密后的文件
        deFile= AESUtils.decryptFile(inputStream,deFile,file.getSecretKey());
        return deFile;
    }

    @Override
    public boolean checkFileUser(int userId, int fileId) {
        DomainFile file=fileRepo.findByFileId(fileId);
        return checkService.checkFileUser(file.getFileHash(),userId);
    }
}
