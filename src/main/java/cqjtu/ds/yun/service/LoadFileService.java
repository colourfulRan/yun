package cqjtu.ds.yun.service;

import javax.servlet.http.HttpSession;
import java.io.File;

public interface LoadFileService {

    //上传文件
    public void uploadFile(File file,String fileType, HttpSession session);

    //计算元数据的伪随机函数
    public String PRF(String partHash,int i);

    //获取文件类型
    public int getFileType(String fileType);

    //下载文件
    public File downloadFile(int fileHash);


}
