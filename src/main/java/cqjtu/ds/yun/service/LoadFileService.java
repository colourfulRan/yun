package cqjtu.ds.yun.service;

import javax.servlet.http.HttpSession;
import java.io.File;

public interface LoadFileService {

    //上传文件
    public boolean uploadFile(File file,String fileType, HttpSession session);

    //计算元数据的伪随机函数
   // public String PRF(String partHash,int i);

    //获取文件类型
    public int getFileType(String fileType);

    //下载文件
    public File downloadFile(int fileHash);

    //检查该用户是否拥有文件所有权
    public boolean checkFileUser(int userId,int fileId);



}
