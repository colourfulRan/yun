package cqjtu.ds.yun.web;

import cqjtu.ds.yun.service.domain.Progress;
import cqjtu.ds.yun.utils.AliyunOSSUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;

@Controller
public class UpLoadController {
    private final org.slf4j.Logger logger= LoggerFactory.getLogger(getClass());
    private static final String TO_PATH="upLoad";

    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;

    @RequestMapping("/test")
    public String test(){
        return "test";
    }

    @RequestMapping("/toUpLoadFile")
    public String toUpLoadFile(){
        return TO_PATH;
    }

    /**文件上传**/
    @RequestMapping("/uploadFile")
    public String uploadBlog(@RequestParam("file")MultipartFile file){//MultipartFile是spring类型，代表HTML中form data方式上传的文件，包含二进制数据+文件名称。
        logger.info("文件上传");
        String filename=file.getOriginalFilename();
        String fileType=file.getContentType();
        String[] type=fileType.split("/");
       /* switch (type[0]){
            case "image":fileType="图片";
            case "text":fileType="文档";
            case "application":fileType="文档";
            case "vedio":fileType="视频";
            case "music":fileType="音乐";
            default:fileType="其他";
        }*/
        System.out.println(filename+"  "+fileType);
        try {
            if(file!=null){
                if (!"".equals(filename.trim())){
                    //MultipartFile转File
                    File newFile=new File(filename);
                    FileOutputStream os=new FileOutputStream(newFile);
                    os.write(file.getBytes());
                    os.close();
                    file.transferTo(newFile);//将上传文件写入目标文件
                    //上传到OSS
                    String uploadUrl=aliyunOSSUtil.upLoad(newFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败："+e.getMessage();
        }
        return "upload_success";
    }

    /**获取上传进度**/
    @GetMapping("/size")
    public @ResponseBody Progress getProgress(HttpServletRequest request){
        HttpSession session=request.getSession();
        Progress progress= (Progress) session.getAttribute("status");
        System.out.println(progress+"controller");
        return progress;
    }

    @RequestMapping("upload")
    public String upLoad(){
        return "upLoad";
    }
}
