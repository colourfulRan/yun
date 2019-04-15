package cqjtu.ds.yun.web;

import cqjtu.ds.yun.service.LoadFileService;
import cqjtu.ds.yun.utils.AliyunOSSUtil;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

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
    @RequestMapping("uploadFile")
    public String uploadBlog(@RequestParam("file")MultipartFile file,HttpServletRequest request){//MultipartFile是spring类型，代表HTML中form data方式上传的文件，包含二进制数据+文件名称。       logger.info("文件上传");
        HttpSession session=request.getSession();
        String filename=file.getOriginalFilename();
        session.setAttribute("fileName",filename);
        System.out.println(filename);
        long fileLength=file.getSize();
        DecimalFormat df=new DecimalFormat(".00");
        if(fileLength<1024){
            session.setAttribute("fileSize","1KB");
        }else if(fileLength>=1024 && fileLength<1024*1024){
            session.setAttribute("fileSize",df.format(fileLength/1024.0)+"KB");
        }else {
            session.setAttribute("fileSize",df.format(fileLength/(1024.0*1024.0))+"MB");
        }
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
                    String url=aliyunOSSUtil.upLoad(newFile,session,key);
                 //   loadFileService.uploadFile(newFile,fileType,session);
                    //删除临时文件
                    newFile.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //return "上传失败："+e.getMessage();
        }
        return "upload_success";//上传成功页面
    }

    /**获取实时上传进度**/
    @RequestMapping(value = "percent",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getUploadPercent(HttpServletRequest request){
        HttpSession session=request.getSession();
        int percent=session.getAttribute("uploadPercent")==null?0: (int) session.getAttribute("uploadPercent");
        String uploadSize= session.getAttribute("uploadSize")==null?null: (String) session.getAttribute("uploadSize");
        String fileSize=session.getAttribute("fileSize")==null?null: (String) session.getAttribute("fileSize");
        System.out.println("上传进度"+percent+"已上传大小"+uploadSize);

        JSONObject data=new JSONObject();
        try {
            data.put("fileName",session.getAttribute("fileName"));
            data.put("uploadPercent",percent);
            data.put("fileSize",fileSize);
            data.put("uploadSize",uploadSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data.toString();
    }


    /**重置上传进度**/
    @RequestMapping("resetPercent")
    @ResponseBody
    public String resetPercent(HttpServletRequest request){
        HttpSession session=request.getSession();
        session.setAttribute("upload_percent",0);
        return "重置进度";
    }


    @RequestMapping("upload")
    public String upLoad(HttpSession session){
        session.getAttribute("uploadPercent");
        System.out.println(session.getId());
        return "upLoad";
    }
}
