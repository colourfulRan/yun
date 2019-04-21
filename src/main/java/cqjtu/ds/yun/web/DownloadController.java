package cqjtu.ds.yun.web;

import com.aliyun.oss.model.OSSObject;
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
import java.io.*;
import java.net.URLEncoder;

@Controller
public class DownloadController {
    private final org.slf4j.Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;

    /**以附件形式流式下载**/
    @ResponseBody
    @RequestMapping(value = "/downloadFile",method = RequestMethod.GET)
    public void downloadFile(HttpServletRequest request, HttpServletResponse response){
        try{
            //String key=request.getParameter("key");
            String key="time1128";
            //String filename=request.getParameter("filename");
           // int i=filename.lastIndexOf("\\");
           // filename=filename.substring(i+1);
            String filename="测试.doc";
            OSSObject ossObject=aliyunOSSUtil.downloadFile(key);
            //以缓冲的方式从字符输入流中读取文本，缓冲各个字符，从而提供字符、数组和行的高效读取
            BufferedReader reader=new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
            InputStream inputStream=ossObject.getObjectContent();
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
            byte[] car=new byte[1024];
            int L;
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**断点续传下载**/
    @ResponseBody
    @RequestMapping(value = "/download",method = RequestMethod.GET)
    public void download(){
        aliyunOSSUtil.downloadFile("time1128","测试.doc");
    }
}
