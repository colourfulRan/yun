package cqjtu.ds.yun.web;

import com.aliyun.oss.model.OSSObject;
import cqjtu.ds.yun.service.LoadFileService;
import cqjtu.ds.yun.utils.AESUtils;
import cqjtu.ds.yun.utils.AliyunOSSUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
public class DownloadController {
    private final org.slf4j.Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    private LoadFileService fileService;

    /**以附件形式流式下载**/
    @ResponseBody
    @RequestMapping(value = "/downloadFile")
    public Map<String,Object> downloadFile(String fileId,HttpServletRequest request, HttpServletResponse response){
        Map<String ,Object> map=new HashMap<>();
        int file_id=Integer.parseInt(fileId);
        HttpSession session=request.getSession();
        int userId= (int) session.getAttribute("uid");
        if(fileService.checkFileUser(userId,file_id)){
            try{
                File deFile=fileService.downloadFile(file_id);
                InputStream inputStream=new FileInputStream(deFile);
                String filename=deFile.getName();
                //以缓冲的方式从字符输入流中读取文本，缓冲各个字符，从而提供字符、数组和行的高效读取
                BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));

                //缓冲文件输出流
                BufferedOutputStream outputStream=new BufferedOutputStream(response.getOutputStream());
                //通知浏览器以附件形式下载
                // response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(filename,"UTF-8"));
                //为防止文件名出现乱码
                //response.setContentType("application/doc");
                final String userAgent=request.getHeader("USER-AGENT");
                if(StringUtils.contains(userAgent,"MSIE")) {//IE浏览器
                    filename=URLEncoder.encode(filename,"UTF-8");
                }else if (StringUtils.contains(userAgent,"Mozilla")){//google,火狐浏览器
                    filename=new String(filename.getBytes(),"ISO8859-1");
                }else {
                    filename=URLEncoder.encode(filename,"UTF-8");//其他浏览器
                }
                //设置让浏览器弹出下载提示框，而不是直接在浏览器中打开
                response.addHeader("Content-Disposition","attachment;filename="+filename);
                System.out.println(filename);
                int L;
                byte[] car=new byte[1024];
                while ((L=inputStream.read(car))!=-1){
                    if(car.length!=0){
                        outputStream.write(car,0,L);
                    }
                }
                if(outputStream!=null){
                    outputStream.flush();
                    outputStream.close();
                }
                if(inputStream!=null){
                    inputStream.close();
                }
                map.put("code",0);
                map.put("msg","下载成功！");
                map.put("data","");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            map.put("code",1);
            map.put("msg","下载失败！");
            map.put("data","");
        }
        return map;
    }

    /**断点续传下载**/
    @ResponseBody
    @RequestMapping(value = "/download",method = RequestMethod.GET)
    public void download(){
  //      aliyunOSSUtil.downloadFile("time1128","测试.doc");
    }
}
