package cqjtu.ds.yun.service.impl;

import cqjtu.ds.yun.dal.FileRepo;
import cqjtu.ds.yun.service.LoadFileService;
import cqjtu.ds.yun.service.domain.DomainFile;
import cqjtu.ds.yun.utils.AESUtils;
import cqjtu.ds.yun.utils.AliyunOSSUtil;
import cqjtu.ds.yun.utils.MD5Utils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


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
        File encrypfile=new File("encrypfile");
        encrypfile=AESUtils.encryptFile(file,encrypfile,key);
        String fileId=MD5Utils.getMD5ByFile(encrypfile);
        aliyunOSSUtil.upLoad(encrypfile,session,fileId);
        DomainFile domainFile=new DomainFile();
        domainFile.setFileId(fileId);
        domainFile.setFileName(file.getName());
        domainFile.setSecretKey(key);
        domainFile.setTypeId(getFileType(fileType));
        System.out.println(file.length()+"  "+file.length()/1024+" "+Math.ceil(file.length()/1024.0)+" "+(int) Math.ceil(file.length()/1024.0));
        domainFile.setFileSize((int) Math.ceil(file.length()/1024.0));
       // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
       // Date date=new Date();
        domainFile.setUpdateDate(new Date());
        domainFile.setUserId(1);
       // domainFile.setDel(false);
        fileRepo.save(domainFile);
        encrypfile.delete();
    }

    private int getFileType(String fileType){
        String[] types=fileType.split("/");
        int type;
        switch (types[0]){
            case "image":type=1;break;
            case "video":type=3;break;
            case "audio":type=4;break;
            default:type=5;
        }
        if(fileType.equals("application/msword") || fileType.equals("application/pdf") || fileType.equals("application/vnd.ms-excel")
                || fileType.equals("application/vnd.ms-powerpoint") || fileType.equals("text/plain") || fileType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                || fileType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")){
            type=2;
        }
        return type;
    }
}
