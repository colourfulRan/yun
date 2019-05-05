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
import java.util.HashMap;
import java.util.Map;

@Controller
public class UpLoadController {
    private final org.slf4j.Logger logger= LoggerFactory.getLogger(getClass());


    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;
    @Autowired
    private LoadFileService loadFileService;
   /* @Reference
    private FileService fileService;*/
    @RequestMapping("test")
    @ResponseBody
    public Map<String,Object> test(String fileId){
        System.out.println("dddd");
        Map<String ,Object> map=new HashMap<>();
        map.put("msg",fileId+"哈哈");
        System.out.println(fileId);
        return map;
    }


    /**文件上传**/
    @ResponseBody
    @RequestMapping("uploadFile")
    public Map<String,Object> uploadBlog(@RequestParam("file")MultipartFile file, HttpServletRequest request){//MultipartFile是spring类型，代表HTML中form data方式上传的文件，包含二进制数据+文件名称。       logger.info("文件上传");
        HttpSession session=request.getSession();
        Map<String,Object> map=new HashMap<>();
        String filename=file.getOriginalFilename();
        String fileType=file.getContentType();
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
                  //  String url=aliyunOSSUtil.upLoad(newFile,session,key);
                    map.put("code",0);
                    map.put("msg","上传成功！");
                    map.put("data","");
                    loadFileService.uploadFile(newFile,fileType,session);
                    //删除临时文件
                    newFile.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code",1);
            map.put("msg","上传失败！");
            //src.put("src","G://月度考核表模板.doc");
            map.put("data","");
        }
        return map;
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
        session.setAttribute("uploadPercent",0);
        session.setAttribute("uploadSize","0KB");
        session.setAttribute("fileSize","0KB");
        session.setAttribute("fileName",null);
        return "重置进度";
    }


    @RequestMapping("upload")
    public String upLoad(HttpSession session){
        session.getAttribute("uploadPercent");
        System.out.println(session.getId());
        return "upLoad";
    }
}
