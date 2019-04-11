package cqjtu.ds.yun.web;

import cqjtu.ds.yun.service.LoadFileService;
import cqjtu.ds.yun.service.domain.Progress;
import cqjtu.ds.yun.service.impl.LoadFileServiceImpl;
import cqjtu.ds.yun.utils.AliyunOSSUtil;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UpLoadController {
    private final org.slf4j.Logger logger= LoggerFactory.getLogger(getClass());
    private static final String TO_PATH="upLoad";


    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;
    @Autowired
    private LoadFileService loadFileService;

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
    @ResponseBody
    public String uploadBlog(@RequestParam("file")MultipartFile file,HttpServletRequest request){//MultipartFile是spring类型，代表HTML中form data方式上传的文件，包含二进制数据+文件名称。       logger.info("文件上传");
        String filename=file.getOriginalFilename();
        String fileType=file.getContentType();

        System.out.println(filename+"  "+fileType);
        HttpSession session=request.getSession();
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
                    String key="time1128";
                 //   String url=aliyunOSSUtil.upLoad(newFile,session,key);
                    loadFileService.uploadFile(newFile,fileType,session);
                    //删除临时文件
                    newFile.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //return "上传失败："+e.getMessage();
        }
        int percent=session.getAttribute("uploadPercent")==null?0: (int) session.getAttribute("uploadPercent");
        long uploadSize= session.getAttribute("uploadSize")==null?0: (long) session.getAttribute("uploadSize");
        String fileSize=session.getAttribute("fileSize")==null?null: (String) session.getAttribute("fileSize");
        System.out.println(percent+" "+uploadSize+" "+fileSize);
        return "upload_success";
    }

    /**获取实时上传进度**/
    @RequestMapping("/percent")
    @ResponseBody
    public String getUploadPercent(HttpServletRequest request){
        HttpSession session=request.getSession();
        int percent=session.getAttribute("uploadPercent")==null?0: (int) session.getAttribute("uploadPercent");
        long uploadSize= session.getAttribute("uploadSize")==null?0: (long) session.getAttribute("uploadSize");
        String fileSize=session.getAttribute("fileSize")==null?null: (String) session.getAttribute("fileSize");
        System.out.println(session.getAttribute("uploadPercent"));
        JSONObject result = new JSONObject();
        try {
            result.put("percent",percent);
            result.put("uploadSize",uploadSize);
            result.put("fileSize",fileSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**重置上传进度**/
    @RequestMapping("/resetPercent")
    public void resetPercent(HttpServletRequest request){
        HttpSession session=request.getSession();
        session.setAttribute("upload_percent",0);
    }

    /**取消上传**/
    @RequestMapping("/cancelUpload")
    public void cancelUpload(String uploadId){
       // HttpSession session=request.getSession();
        //String uploadId= (String) session.getAttribute("uploadId");
        System.out.println(uploadId);
        aliyunOSSUtil.cancelUpload(uploadId,"time1409");
    }

    @RequestMapping("upload")
    public String upLoad(){
        return "upLoad";
    }
}
