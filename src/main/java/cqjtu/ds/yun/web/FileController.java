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
import java.sql.Timestamp;
import java.util.*;

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


    //
    @RequestMapping("test2")
    public String test2()
    {
        return "test2";
    }


    @RequestMapping("image_list")
    @ResponseBody
    public  Map<String, Object> image_list(HttpSession session,Integer page,Integer limit)
    {
        logger.info("图片列表！！！");
        Integer userid= (Integer) session.getAttribute("uid");//通过session获取userid

        Integer count=fileService.FCount(userid,1,false);//获取数据条数

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
    @RequestMapping("search_imagename")
    @ResponseBody
    public Map<String, Object> search_imagename(HttpSession session,String filename,Integer page,Integer limit) {
        System.out.println("模糊查询");
        logger.info("filename:{}",filename);
        Integer userid= (Integer) session.getAttribute("uid");//通过session获取userid
        String filena="%" + filename + "%";
        Map<String, Object> map = null;
        Integer count = fileService.Fname_Count(userid,1,false,filena);//获取数据条数
        Pageable pageRequest = PageRequest.of(page - 1, limit);
        Page<DomainFile> filename_page = fileService.findAllbyFname(userid,1,false,filena,pageRequest);
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
            for (int i = 0; i < Infos.length; i++)
            {
                System.out.println(Infos[i]);
                DomainFile oldfile=fileService.findbyid(Integer.valueOf(Infos[i]));
                oldfile.setDel(true);
                oldfile.setFileValid(30);
                oldfile.setDelDate(new Timestamp(System.currentTimeMillis()));
                fileService.SaveFile(oldfile);
            }
                Map<String, Object> map = null;
                map = new LinkedHashMap<String, Object>();
                map.put("data",true);
                return map;
        }



    //对回收站的操作
      @RequestMapping("recycle_list")
      @ResponseBody
     public Map<String, Object> recycle_list(HttpSession session,Integer page,Integer limit)
      {
          logger.info("回收站列表！！！");
          Integer userid= (Integer) session.getAttribute("uid");//通过session获取userid
          Integer count=fileService.RecycleCount(userid,true);//获取数据条数
          Pageable pageRequest = PageRequest.of(page-1,limit);
          Page<DomainFile> pageResult = fileService.findAllRecycle(userid,true,pageRequest);
          Map<String, Object> map = null;
          map = new LinkedHashMap<String, Object>();
          map.put("code",0);
          map.put("msg","");
          map.put("count",count);
          map.put("data",pageResult.getContent());
          logger.info("map:{}",map);
          return map;
      }

       @RequestMapping("recycle_reduction")
       @ResponseBody
       public Map<String, Object> recycle_reduction(String fileid)
       {
           logger.info("fileid:{}",fileid);
           System.out.println("调用还原函数!");
           String[] Infos = fileid.split(",");
           for (int i = 0; i < Infos.length; i++)
           {
               System.out.println(Infos[i]);
               DomainFile oldfile=fileService.findbyid(Integer.valueOf(Infos[i]));
               oldfile.setDel(false);
               oldfile.setUpdateDate(new Timestamp(System.currentTimeMillis()));
               fileService.SaveFile(oldfile);
           }
           Map<String, Object> map = null;
           map = new LinkedHashMap<String, Object>();
           map.put("data",true);
           return map;
       }


    @RequestMapping("recycle_del")
    @ResponseBody
    public Map<String, Object> recycle_del(String fileid)
    {
        logger.info("fileid:{}",fileid);
        System.out.println("调用回收站删除函数!");
        String[] Infos = fileid.split(",");
        for (int i = 0; i < Infos.length; i++)
        {
            System.out.println(Infos[i]);
            fileService.RemoveFile(Integer.valueOf(Infos[i]));
        }
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("data",true);
        return map;
    }

    @RequestMapping("recycle_delAll")
    @ResponseBody
    public Map<String, Object> recycle_delAll()
    {
        System.out.println("调用回收站清空函数!");
       fileService.RemoveAll(true);
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("data",true);
        return map;
    }




    //对音乐的操作
    @RequestMapping("music_list")
    @ResponseBody
    public  Map<String, Object> music_list(HttpSession session,Integer page,Integer limit)
    {
        logger.info("音乐列表！！！");
        Integer userid= (Integer) session.getAttribute("uid");//通过session获取userid
        Integer count=fileService.FCount(userid,4,false);//获取数据条数
        Pageable pageRequest = PageRequest.of(page-1,limit);
        Page<DomainFile> pageResult = fileService.findAllFiles(userid,4,false,pageRequest);
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",count);
        map.put("data",pageResult.getContent());
        logger.info("map:{}",map);
        return map;
    }


    @RequestMapping("search_musicname")
    @ResponseBody
    public Map<String, Object> search_musicname(HttpSession session,String filename,Integer page,Integer limit) {
        System.out.println("模糊查询");
        logger.info("filename:{}",filename);
        Integer userid= (Integer) session.getAttribute("uid");//通过session获取userid
        String filena="%" + filename + "%";
        Map<String, Object> map = null;
        Integer count = fileService.Fname_Count(userid,4,false,filena);//获取数据条数
        Pageable pageRequest = PageRequest.of(page - 1, limit);
        Page<DomainFile> filename_page = fileService.findAllbyFname(userid,4,false,filena,pageRequest);
        map = new LinkedHashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", filename_page.getContent());
        logger.info("map:{}", map);
        return map;
    }

    //@RequestMapping后，返回值通常解析为跳转路径，加上@responsebody后返回结果不会被解析为跳转路径，而是直接写入HTTP response body中。
    // 比如异步获取json数据，加上@responsebody后，会直接返回json数据。
    @RequestMapping("music_del")
    //区别 方法的返回值字符串 和 视图解析器解析的页面名字字符串 的冲突
    @ResponseBody
    public Map<String, Object> music_del(String fileid)
    {
        logger.info("fileid:{}",fileid);
        //System.out.println("调用音乐删除函数!");
        String[] Infos = fileid.split(",");
        for (int i = 0; i < Infos.length; i++)
        {
            System.out.println(Infos[i]);
            DomainFile oldfile=fileService.findbyid(Integer.valueOf(Infos[i]));
            oldfile.setDel(true);
            oldfile.setFileValid(30);
            oldfile.setDelDate(new Timestamp(System.currentTimeMillis()));
            System.out.println("调用删除更新函数！！");
            fileService.SaveFile(oldfile);
            System.out.println("更新函数调用结束！！");
        }
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("data",true);
        return map;
    }



    //对视频的操作
    @RequestMapping("vedio_list")
    @ResponseBody
    public  Map<String, Object> vedio_list(HttpSession session,Integer page,Integer limit)
    {
        logger.info("视频列表！！！");
        Integer userid= (Integer) session.getAttribute("uid");//通过session获取userid
        Integer count=fileService.FCount(userid,3,false);//获取数据条数
        Pageable pageRequest = PageRequest.of(page-1,limit);
        Page<DomainFile> pageResult = fileService.findAllFiles(userid,3,false,pageRequest);
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",count);
        map.put("data",pageResult.getContent());
        logger.info("map:{}",map);
        return map;
    }


    @RequestMapping("search_vedioname")
    @ResponseBody
    public Map<String, Object> search_vedioname(HttpSession session,String filename,Integer page,Integer limit) {
        System.out.println("模糊查询");
        logger.info("filename:{}",filename);
        Integer userid= (Integer) session.getAttribute("uid");//通过session获取userid
        String filena="%" + filename + "%";
        Map<String, Object> map = null;
        Integer count = fileService.Fname_Count(userid,3,false,filena);//获取数据条数
        Pageable pageRequest = PageRequest.of(page - 1, limit);
        Page<DomainFile> filename_page = fileService.findAllbyFname(userid,3,false,filena,pageRequest);
        map = new LinkedHashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", filename_page.getContent());
        logger.info("map:{}", map);
        return map;
    }

    //@RequestMapping后，返回值通常解析为跳转路径，加上@responsebody后返回结果不会被解析为跳转路径，而是直接写入HTTP response body中。
    // 比如异步获取json数据，加上@responsebody后，会直接返回json数据。
    @RequestMapping("vedio_del")
    //区别 方法的返回值字符串 和 视图解析器解析的页面名字字符串 的冲突
    @ResponseBody
    public Map<String, Object> vedio_del(String fileid)
    {
        logger.info("fileid:{}",fileid);
        System.out.println("调用图片删除函数!");
        String[] Infos = fileid.split(",");
        for (int i = 0; i < Infos.length; i++)
        {
            System.out.println(Infos[i]);
            DomainFile oldfile=fileService.findbyid(Integer.valueOf(Infos[i]));
            oldfile.setDel(true);
            oldfile.setFileValid(30);
            oldfile.setDelDate(new Timestamp(System.currentTimeMillis()));
            fileService.SaveFile(oldfile);
        }
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("data",true);
        return map;
    }



    //对文件的操作
    @RequestMapping("file_list")
    @ResponseBody
    public  Map<String, Object> file_list(HttpSession session,Integer page,Integer limit)
    {
        logger.info("文件列表！！！");
        Integer userid= (Integer) session.getAttribute("uid");//通过session获取userid
        Integer count=fileService.Sec_Count(userid);//获取数据条数
        int start=(page-1)*limit;
        List<DomainFile> listall = fileService.findAllSec(userid,start,limit);
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",count);
        map.put("data",listall);
        logger.info("map:{}",map);
        return map;
    }


  @RequestMapping("search_filename")
    @ResponseBody
    public Map<String, Object> search_filename(HttpSession session,String filename,Integer page,Integer limit) {
        System.out.println("模糊查询");
        logger.info("filename:{}",filename);
        Integer userid= (Integer) session.getAttribute("uid");//通过session获取userid
        String filena="%" + filename + "%";
        Map<String, Object> map = null;
        Integer count = fileService.FnameSec_Count(filena,userid);
        int start=(page-1)*limit;
        List<DomainFile> filename_page = fileService.findAllFnameSec(filena,userid,start,limit);
        map = new LinkedHashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", filename_page);
        logger.info("map:{}", map);
        return map;
    }

    //@RequestMapping后，返回值通常解析为跳转路径，加上@responsebody后返回结果不会被解析为跳转路径，而是直接写入HTTP response body中。
    // 比如异步获取json数据，加上@responsebody后，会直接返回json数据。
    @RequestMapping("file_del")
    //区别 方法的返回值字符串 和 视图解析器解析的页面名字字符串 的冲突
    @ResponseBody
    public Map<String, Object> file_del(String fileid)
    {
        logger.info("fileid:{}",fileid);
        System.out.println("调用文件删除函数!");
        String[] Infos = fileid.split(",");
        for (int i = 0; i < Infos.length; i++)
        {
            System.out.println(Infos[i]);
            DomainFile oldfile=fileService.findbyid(Integer.valueOf(Infos[i]));
            oldfile.setDel(true);
            oldfile.setFileValid(30);
            oldfile.setDelDate(new Timestamp(System.currentTimeMillis()));
            fileService.SaveFile(oldfile);
        }
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("data",true);
        return map;
    }


    //最近使用文件
    @RequestMapping("recent_list")
    @ResponseBody
    public  Map<String, Object> recent_list(HttpSession session,Integer page,Integer limit)
    {
        logger.info("最近列表！！！");
        Integer userid= (Integer) session.getAttribute("uid");//通过session获取userid
        Integer start=(page-1)*limit;
        List<DomainFile> listall=fileService.findallRec(userid,start,limit);
        Integer count=fileService.CountRec(userid);
        logger.info("count:{}",count);
        logger.info("listall:{}",listall);
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",count);
        map.put("data",listall);
        //需要解决分页问题！！！
        return map;
    }


    @RequestMapping("search_recentname")
    @ResponseBody
    public Map<String, Object> search_recentname(HttpSession session,String filename,Integer page,Integer limit) {
        System.out.println("模糊查询");
        Integer userid= (Integer) session.getAttribute("uid");//通过session获取userid
        String filena="%" + filename + "%";
        Map<String, Object> map = null;
        Integer count = fileService.FnameSec_Count(filena,userid);
        Integer start=(page-1)*limit;
       List<DomainFile> filename_page = fileService.findAllFnameSec(filena,userid,start,limit);
        map = new LinkedHashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", filename_page);
        logger.info("map:{}", map);
        return map;
    }


    //@RequestMapping后，返回值通常解析为跳转路径，加上@responsebody后返回结果不会被解析为跳转路径，而是直接写入HTTP response body中。
    // 比如异步获取json数据，加上@responsebody后，会直接返回json数据。
    @RequestMapping("recent_del")
    //区别 方法的返回值字符串 和 视图解析器解析的页面名字字符串 的冲突
    @ResponseBody
    public Map<String, Object> recent_del(String fileid)
    {
        logger.info("fileid:{}",fileid);
        String[] Infos = fileid.split(",");
        for (int i = 0; i < Infos.length; i++)
        {
            DomainFile oldfile=fileService.findbyid(Integer.valueOf(Infos[i]));
            oldfile.setRecentDate(null);
            oldfile.setDel(true);
            oldfile.setFileValid(30);
            oldfile.setDelDate(new Timestamp(System.currentTimeMillis()));
            fileService.SaveFile(oldfile);
        }
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("data",true);
        return map;
    }


    @RequestMapping("recent_clear")
    @ResponseBody
    public Map<String, Object> recent_clear(String fileid)
    {
        logger.info("fileid:{}",fileid);
        String[] Infos = fileid.split(",");
        for (int i = 0; i < Infos.length; i++)
        {
            System.out.println(Infos[i]);
            DomainFile oldfile=fileService.findbyid(Integer.valueOf(Infos[i]));
            oldfile.setRecentDate(null);
            fileService.SaveFile(oldfile);
        }
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("data",true);
        return map;
    }



    //全部文件
    @RequestMapping("all_list")
    @ResponseBody
    public  Map<String, Object> all_list(HttpSession session,Integer parentid,Integer page,Integer limit) {
        logger.info("全部文件列表！！！");
        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        Pageable pageRequest = PageRequest.of(page - 1, limit);
        Integer count = 0;
        Page<DomainFile> listall = null;
        if (parentid == null)
        {
            parentid = -1;
            }
        listall = fileService.findAll_all(userid, parentid, pageRequest);
        count = fileService.countAll_all(userid, parentid);

        logger.info("count:{}", count);
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", listall.getContent());
        logger.info("listall:{}", map);
        return map;
    }


        //新建文件夹
        @RequestMapping("all_add")
        @ResponseBody
        public Map<String, Object>all_list(HttpSession session,Integer parentid,String filename)
        {
            if(parentid==null)
            {
                parentid=-1;
            }
            logger.info("新建文件夹！！！");
            Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
            DomainFile nfile=new DomainFile();
            nfile.setUserId(userid);
            nfile.setFileValid(0);
            nfile.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            nfile.setDel(false);
            nfile.setTypeId(0);
            nfile.setParentId(parentid);
            nfile.setFileName(filename);
            logger.info("nfile:{}",nfile);
            fileService.SaveFile(nfile);
            Map<String, Object> map = null;
            map = new LinkedHashMap<String, Object>();
            map.put("data",true);
            return map;
        }



    @RequestMapping("all_del")
    @ResponseBody
    public Map<String, Object> all_del(String fileid)
    {
        System.out.println("执行文件夹的删除函数！！！！");
        logger.info("fileid:{}",fileid);
        String[] Infos = fileid.split(",");
        for (int i = 0; i < Infos.length; i++)
        {
            //调用递归删除文件函数
            deleteF(Integer.valueOf(Infos[i]));
        }
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("data",true);
        return map;
    }



    //封装一个关于递归删除文件夹的方法！！！
    void deleteF(Integer id)
    {
        Integer count = -1;

        DomainFile fi = fileService.findbyid(id);//找到该文件

        //如果文件为空则结束
        if (fi == null)//!!!
        {
            return ;//递归出口
        }

        else
        {
           // List<DomainFile> listall = fileService.findAll_abyP(id);
            //for (DomainFile dfile : listall)
            //
              if (fi.getTypeId()!= 0)//如果是文件 则直接删除删除
               {
                  fileService.RemoveFile(id);
               }
              else
              {
                 count = fileService.countAll_abyP(id);//则找到以其id作为父id的所有文件
                // deleteF(file.getFileId());
                  if (count == 0)//空文件夹
                   {
                       fileService.RemoveFile(id);
                       return;
                    }
                  else
                  {
                     List<DomainFile> listall = fileService.findAll_abyP(id);//寻找出以其id作为父id的文件
                     for (DomainFile dfile : listall)
                     {
                        //遍历删除文件
                         deleteF(dfile.getFileId());
                     }
                   }
                System.out.println("删除文件夹自身!!!");
                fileService.RemoveFile(id);
            }
        }
    }


    @RequestMapping("search_allname")
    @ResponseBody
    public Map<String, Object> search_allname(HttpSession session,String filename,Integer page,Integer limit) {
        System.out.println("模糊查询");
        logger.info("filename:{}",filename);
        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        Pageable pageRequest = PageRequest.of(page - 1, limit);
        Integer count = 0;
        Page<DomainFile> filename_page=null;
        if(filename=="")
        {
            System.out.println("filename为空");
            filename_page = fileService.findAll_all(userid,-1, pageRequest);
            count = fileService.countAll_all(userid, -1);
        }
        else
        {
             // Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
             String filena = "%" + filename + "%";
             count = fileService.countAll_namelike(userid, filena);
             filename_page = fileService.findAll_namelike(userid, filena, pageRequest);
        }
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", filename_page.getContent());
        logger.info("map:{}", map);
        return map;
    }

    @RequestMapping("all_rename")
    @ResponseBody
    public Map<String, Object>all_rename(HttpSession session,Integer fileid,String filename)
    {
        logger.info("重命名！！！");
        System.out.println(filename);
        //Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        DomainFile file1=fileService.findbyid(fileid);
        file1.setFileName(filename);
        fileService.SaveFile(file1);
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("data",true);
        logger.info("data:{}",map);
        return map;
    }



    @RequestMapping("show_Tree")
    @ResponseBody
    public Map<String, Object>show_Tree(HttpSession session)
    {
        logger.info("zTree展示");
        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        //调用保存函数！！
        List<DomainFile>listall=fileService.findAll(userid);
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("state",0);
        map.put("tree",listall);
        logger.info("map:{}",map);
        return map;
    }



}






