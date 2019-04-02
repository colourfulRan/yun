package cqjtu.ds.yun.commons;

import lombok.Setter;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class CustomMultipartResolver extends CommonsMultipartResolver{
    @Autowired
    @Setter
    private FileUploadProgressListener progressListener;


    public MultipartParsingResult parsingResult(HttpServletRequest request){
        String encoding=determineEncoding(request);
        System.out.println(encoding);
        FileUpload fileUpload=prepareFileUpload(encoding);
        //向文件上传进度监视器设置session用于存储上传进度
        progressListener.setSession(request.getSession());
        //将文件上传进度监视器加入到fileUpload中
        fileUpload.setProgressListener(progressListener);
        try {
            List<FileItem> fileItems=((ServletFileUpload)fileUpload).parseRequest(request);
            return parseFileItems(fileItems,encoding);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        return null;
    }

}
