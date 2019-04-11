package cqjtu.ds.yun.service;

import javax.servlet.http.HttpSession;
import java.io.File;

public interface LoadFileService {

    //上传文件
    public void uploadFile(File file,String fileType, HttpSession session);
}
