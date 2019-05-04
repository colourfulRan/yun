package cqjtu.ds.yun.web;
import cqjtu.ds.yun.service.FileService;
import cqjtu.ds.yun.service.domain.DomainFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;

import java.util.Map;

@Controller
public class FileController
{
    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @RequestMapping("my")
    public String main()
    {
        return "my";
    }
    //最近
    @RequestMapping("recent")
    public String recent()
    {
        return "recent";
    }
    //所有文件
    @RequestMapping("all")
    public String all()
    {
        return "all";
    }
    //图像
    @RequestMapping("image")
    public String image()
    {
        return "image";
    }
   //音乐
    @RequestMapping("music")
    public String music()
    {
        return "music";
    }
   //文件
    @RequestMapping("file")
    public String file()
    {
        return "file";
    }
   //视频
    @RequestMapping("vedio")
    public String vedio()
    {
        return "vedio";
    }

    //其他
    @RequestMapping("other")
    public String other()
    {
        return "other";
    }

    //回收站
    @RequestMapping("recycle")
    public  String recycle()
    {
        return "recycle";
    }



    @RequestMapping("translist")
    public String translist()
    {
        return "translist";
    }
    //正在上传
    @RequestMapping("isupload")
    public String isupload()
    {
        return "isupload";
    }
    //正在下载
    @RequestMapping("isdownload")
    public String isdownload()
    {
        return "isdownload";
    }
    //传输完成
    @RequestMapping("trans")
    public String trans()
    {
        return "trans";
    }





    @RequestMapping("image_list")
    @ResponseBody
    public  Map<String, Object> image_list(HttpSession session,Integer page,Integer limit)
    {
        logger.info("图片列表！！！");
        Integer userid= (Integer) session.getAttribute("uid");//通过session获取userid
        Integer count=fileService.ImageCount(userid,1,false);//获取数据条数
       // int pageNum=count % limit == 0 ? count / limit : count/limit+1;//页数
        //pageNum总是从0开始，表示查询页，limit指每页的期望行数
        Pageable pageRequest = PageRequest.of(page-1,limit);
        Page<DomainFile> pageResult = fileService.findAllFiles(userid,1,false,pageRequest);
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",count);
        map.put("data",pageResult.getContent());
        logger.info("map:{}",map);
        return map;
    }

    //图片处理之模糊查询图片
    @RequestMapping("search_filename")
    @ResponseBody
    public Map<String, Object> search_filename(HttpSession session,String filename,Integer page,Integer limit) {
        System.out.println("模糊查询");
        logger.info("filename:{}",filename);
        Integer userid= (Integer) session.getAttribute("uid");//通过session获取userid
        String filena="%" + filename + "%";
        Map<String, Object> map = null;
        Integer count = fileService.Imagename_Count(userid,2,false,filena);//获取数据条数
        Pageable pageRequest = PageRequest.of(page - 1, limit);
        Page<DomainFile> filename_page = fileService.findAllbyimagename(userid,2,false,filena,pageRequest);
        map = new LinkedHashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", filename_page.getContent());
        logger.info("map:{}", map);
        return map;
    }




        //图片处理之删除图片
       //@RequestMapping后，返回值通常解析为跳转路径，加上@responsebody后返回结果不会被解析为跳转路径，而是直接写入HTTP response body中。
       // 比如异步获取json数据，加上@responsebody后，会直接返回json数据。
        @RequestMapping("image_del")
        //区别 方法的返回值字符串 和 视图解析器解析的页面名字字符串 的冲突
        @ResponseBody
        public Map<String, Object> image_del(String fileid)
        {
            logger.info("fileid:{}",fileid);
            System.out.println("调用图片删除函数!");
            String[] Infos = fileid.split(",");
            Map<String, Object> map = null;
            for (int i = 0; i < Infos.length; i++) {
                fileService.RemoveFile(Infos[i]);
            }
                map = new LinkedHashMap<String, Object>();
                map.put("data",true);
                return map;
        }




}

