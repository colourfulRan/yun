package cqjtu.ds.yun.service.impl;

import com.aliyun.oss.model.OSSObject;
import cqjtu.ds.yun.dal.FileRepo;
import cqjtu.ds.yun.service.LoadFileService;
import cqjtu.ds.yun.service.domain.DomainFile;
import cqjtu.ds.yun.utils.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Timestamp;
import java.util.List;
import java.util.TimeZone;

import static cqjtu.ds.yun.utils.BlindSignature.blindSignature;


@Component
public class LoadFileServiceImpl implements LoadFileService {
    private final org.slf4j.Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;
    @Autowired
    private FileRepo fileRepo;

    @Override
    public void uploadFile(File file,String fileType, HttpSession session) {
        String key=MD5Utils.getMD5ByFile(file);
        BigInteger blindMsg=addBlindFactor(key);
        BigInteger blindSig=getBlindSignatureSerectKey(blindMsg);
        String serectKey=reductionSignature(blindSig);
        File encrypfile=new File("encrypfile");
        encrypfile=AESUtils.encryptFile(file,encrypfile,serectKey);
        String fileHash=MD5Utils.getMD5ByFile(encrypfile);
        //云端不存在相同文件
        if(fileHash!=null){
            BloomFilter bloomFilter=new BloomFilter(50);
            try {
                List<String> partHashs=FileUtil.splitBySize(file,50);
                int i=1;
                for (String str:partHashs){
                    bloomFilter.addIfNotExist(PRF(str,i));
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            bloomFilter.saveFilterToFile("src/bloomFilter.obj");
         //   File filterFile=new File("src/bloomFilter.obj");
            aliyunOSSUtil.upLoad(encrypfile,session,fileHash);
         //   aliyunOSSUtil.upLoad(filterFile,)
        }else {
            //云端存在相同文件
        }

        DomainFile domainFile=new DomainFile();
        domainFile.setFileHash(fileHash);
        domainFile.setFileName(file.getName());
        System.out.println("文件名s："+file.getName());
        domainFile.setSecretKey(serectKey);
        domainFile.setTypeId(getFileType(fileType));
      //  System.out.println(file.length()+"  "+file.length()/1024+" "+Math.ceil(file.length()/1024.0)+" "+(int) Math.ceil(file.length()/1024.0));
        domainFile.setFileSize((int) Math.ceil(file.length()/1024.0));
       // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
       // Date date=new Date();
        //TimeZone pst = TimeZone.getTimeZone("Etc/GMT-8");
        //Timestamp time=new Timestamp(System.currentTimeMillis());

        domainFile.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        domainFile.setUserId(1);
        domainFile.setFileValid(0);
       // domainFile.setDel(false);
        fileRepo.save(domainFile);

       // encrypfile.delete();
    }

    @Override
    public String PRF(String partHash, int i) {
        partHash=partHash+i;
      //  System.out.println(partHash);
        String e= DigestUtils.md5DigestAsHex(partHash.getBytes());
        return e;
    }

    public int getFileType(String fileType){
        String[] types=fileType.split("/");
        int type;
        switch (types[0]){
            case "image":type=1;break;
            case "video":type=3;break;
            case "audio":type=4;break;
            case "plain":type=5;break;
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
            blindMsg=BlindSignature.blindHideMsg(m,factor,e,n);
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

    /**
     * 由密钥服务器签名的消息
     */
    public BigInteger getBlindSignatureSerectKey(BigInteger blindMsg){
        BigInteger blindSig=BigInteger.ZERO;
        try {
            ObjectInputStream ois2=new ObjectInputStream(new FileInputStream("src/private.key"));
            RSAPrivateKey priKey= (RSAPrivateKey) ois2.readObject();
            ois2.close();
            BigInteger d=priKey.getPrivateExponent();
            BigInteger n=priKey.getModulus();
            blindSig=blindSignature(blindMsg,d,n);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return blindSig;
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
}
