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

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class FileController {
    private Logger logger = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private FileService fileService;

    @RequestMapping("my")
    public String main() {
        return "my";
    }

    //最近
    @RequestMapping("recent")
    public String recent() {
        return "recent";
    }

    //所有文件
    @RequestMapping("all")
    public String all() {
        return "all";
    }

    //页面助力转跳
    @RequestMapping("all1")
    public  String all1(Model model, Integer parentid,Integer fileid)
    {
        System.out.println("助力打开所在文件夹");
        logger.info("parentid:{}",parentid);

        model.addAttribute("pa",parentid);
        model.addAttribute("fi",fileid);
        return "all";
    }

    //图像
    @RequestMapping("image")
    public String image() {
        return "image";
    }

    //音乐
    @RequestMapping("music")
    public String music() {
        return "music";
    }

    //文件
    @RequestMapping("file")
    public String file() {
        return "file";
    }

    //视频
    @RequestMapping("vedio")
    public String vedio() {
        return "vedio";
    }

    //其他
    @RequestMapping("other")
    public String other() {
        return "other";
    }

    //回收站
    @RequestMapping("recycle")
    public String recycle() {
        return "recycle";
    }

    //数插件
    @RequestMapping("ztree")
    public String ztree() {
        return "ztree";
    }


    /**
     *最近文件操作
     */


    //最近列表（正确）
    @RequestMapping("recent_list")
    //@RequestMapping后，返回值通常解析为跳转路径，加上@responsebody后返回结果不会被解析为跳转路径，而是直接写入HTTP response body中。
    // 比如异步获取json数据，加上@responsebody后，会直接返回json数据。

    @ResponseBody
    public Map<String, Object> recent_list(HttpSession session, Integer page, Integer limit) {
        logger.info("最近列表！！！");
        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        Integer start = (page - 1) * limit;
        List<DomainFile> listall = fileService.findallRec(userid, start, limit);
        Integer count = fileService.CountRec(userid);
        logger.info("count:{}", count);
        logger.info("listall:{}", listall);
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", listall);
        return map;
    }


    //清除查看记录（正确）
    @RequestMapping("recent_clear")
    @ResponseBody
    public Map<String, Object> recent_clear(String fileid) {
        System.out.println("清除记录！！！！！");
        String[] Infos = fileid.split(",");
        for (int i = 0; i < Infos.length; i++) {
            System.out.println(Infos[i]);
            DomainFile oldfile = fileService.findbyid(Integer.valueOf(Infos[i]));
            oldfile.setRecentDate(null);
            fileService.SaveFile(oldfile);
        }
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("data", true);
        return map;
    }

    //fileid parentid均是唯一
    //打开选中文件所在目录。应跳回文件夹出然后查询列出相应的目录（正确）
    @RequestMapping("recent_location")
    @ResponseBody
    public Map<String, Object> recent_location(Integer parentid, Integer page, Integer limit) {
        System.out.println("打开文件所在目录！！");
        Integer count = fileService.countAll_abyP(parentid);
        Pageable pageRequest = PageRequest.of(page - 1, limit);
        Page<DomainFile> pageResult = fileService.findAll_abyP1(parentid, pageRequest);
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", pageResult.getContent());
        return map;
    }


    /**
     *   文件操作
     */

    //查看详情(正确)
    @RequestMapping("detail")
    @ResponseBody
    public Map<String, Object> detail(Integer fileid) {
        //需进行判断，如果已经下载则可预览，否只看得到相应的图标

        System.out.println("显示详情");
        DomainFile fi = fileService.findbyid(fileid);
        Integer typeid=fi.getTypeId();

        //保存最近查看日期
        fi.setRecentDate(new Timestamp(System.currentTimeMillis()));
        fileService.SaveFile(fi);
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("data", true);
        map.put("title", fi.getFileName()+"(更多详情需下载到本地)");
        if(typeid==1)
        {
            //图片
            map.put("src", "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1922740179,1840515713&fm=27&gp=0.jpg");
        }
        else if(typeid==2)
        {
            //其他
            map.put("src", "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1393385878,1171283051&fm=27&gp=0.jpg");
        }
        else if(typeid==3)
        {
            //视频
            map.put("src", "http://img2.imgtn.bdimg.com/it/u=3353719390,3327074354&fm=26&gp=0.jpg");
        }
        else if(typeid==4)
        {
            //音乐
            map.put("src", "http://img4.imgtn.bdimg.com/it/u=3747951984,847416224&fm=26&gp=0.jpg");
        }
        else
        {
            //文档
            map.put("src", "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2304701565,2703988045&fm=27&gp=0.jpg");
        }
        return map;
    }


    //列表（正确）
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> list(HttpSession session, Integer page, Integer limit,Integer typeid) {
        logger.info("列表！！！");
        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        Integer count = fileService.FCount(userid, typeid, 0);//获取数据条数
        Pageable pageRequest = PageRequest.of(page - 1, limit);
        Page<DomainFile> pageResult = fileService.findAllFiles(userid, typeid, 0, pageRequest);
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", pageResult.getContent());
        return map;
    }


    //模糊查询(正确)
    @RequestMapping("search_name")
    @ResponseBody
    public Map<String, Object> search_name(HttpSession session, String filename, Integer page, Integer limit,Integer typeid) {
        System.out.println("模糊查询");
        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        String filena = "%" + filename + "%";
        Map<String, Object> map = null;
        Integer count = fileService.Fname_Count(userid, typeid, 0, filena);//获取数据条数
        Pageable pageRequest = PageRequest.of(page - 1, limit);
        Page<DomainFile> filename_page = fileService.findAllbyFname(userid, typeid, 0, filena, pageRequest);
        map = new LinkedHashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", filename_page.getContent());
        return map;
    }


    //文件批量删除（正确）
    @RequestMapping("del")
    @ResponseBody
    public Map<String, Object> del(String fileid)
    {
        logger.info("fileid:{}", fileid);
        System.out.println("调用删除函数!");
        String[] Infos = fileid.split(",");
        for (int i = 0; i < Infos.length; i++)
        {
            System.out.println(Infos[i]);
            DomainFile oldfile = fileService.findbyid(Integer.valueOf(Infos[i]));
            int parentid = oldfile.getParentId();
            int filesize = oldfile.getFileSize();
            while (parentid != -1)
            {
                DomainFile pf = fileService.findbyid(parentid);
                pf.setFileSize(pf.getFileSize() - filesize);
                fileService.SaveFile(pf);
                parentid = pf.getParentId();
            }

            oldfile.setIsDel(1);
            oldfile.setFileValid(10);
            oldfile.setDelDate(new Timestamp(System.currentTimeMillis()));
            fileService.SaveFile(oldfile);
        }
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("data", true);
        return map;
    }


    //重命名(正确)
    @RequestMapping("rename")
    @ResponseBody
    public Map<String, Object> rename(Integer fileid, String filename)
    {
        logger.info("重命名！！！");
        System.out.println(filename);
        DomainFile file = fileService.findbyid(fileid);
        file.setFileName(filename);
        fileService.SaveFile(file);
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("data", true);
        return map;
    }


    /**
     *  对文档的额外操作
     */

    //文档列表(正确)
    @RequestMapping("file_list")
    @ResponseBody
    public Map<String, Object> file_list(HttpSession session, Integer page, Integer limit) {
        logger.info("文档列表！！！");
        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        Integer count = fileService.Sec_Count(userid);//获取数据条数
        int start = (page - 1) * limit;
        List<DomainFile> listall = fileService.findAllSec(userid, start, limit);
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", listall);
        return map;
    }


    //文档模糊查询
    @RequestMapping("search_filename")
    @ResponseBody
    public Map<String, Object> search_filename(HttpSession session, String filename, Integer page, Integer limit) {
        System.out.println("模糊查询");
        logger.info("filename:{}", filename);
        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        String filena = "%" + filename + "%";
        Map<String, Object> map = null;
        Integer count = fileService.FnameSec_Count(filena, userid);
        int start = (page - 1) * limit;
        List<DomainFile> filename_page = fileService.findAllFnameSec(filena, userid, start, limit);
        map = new LinkedHashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", filename_page);
        return map;
    }


    /**
     *  对回收站的操作
     */

    //回收站列表（正确）
    @RequestMapping("recycle_list")
    @ResponseBody
    public Map<String, Object> recycle_list(HttpSession session, Integer page, Integer limit) {
        logger.info("回收站列表！！！");
        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        Integer count = fileService.RecycleCount(userid, 1);//获取数据条数
        Pageable pageRequest = PageRequest.of(page - 1, limit);
        Page<DomainFile> pageResult = fileService.findAllRecycle(userid, 1, pageRequest);
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", pageResult.getContent());
        return map;
    }

    //回收站之还原函数（批量还原）
    @RequestMapping("recycle_reduction")
    @ResponseBody
    public Map<String, Object> recycle_reduction(HttpSession session, String fileid) {
        System.out.println("调用还原函数!!");
        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        String[] Infos = fileid.split(",");
        for (int i = 0; i < Infos.length; i++)
        {
            System.out.println(Infos[i]);
            Integer id = Integer.valueOf(Infos[i]);
            DomainFile oldfile = fileService.findbyid(id);

            // /获取父文件夹
            int parentid = oldfile.getParentId();
            int filesize =oldfile.getFileSize();
            DomainFile pfile = fileService.findbyid(parentid);
            //父文件夹不为空且其父文件夹未进入回收站  或者本身就是一级文件夹
            //则恢复到原来的位置
            if ((pfile != null && pfile.getIsDel() == 0) )
            {
                //size 的恢复
                //直到遍历完根目录!!!
                while (parentid != -1)
                {
                    //找到父文件
                    DomainFile pf = fileService.findbyid(parentid);
                    System.out.println(pf.getFileId() + " " + pf.getFileSize());
                    pf.setFileSize(pf.getFileSize()+filesize);
                    fileService.SaveFile(pf);
                    System.out.println("恢复后大小为：" + pf.getFileSize());
                    parentid = pf.getParentId();
                }

                //恢复的是文件夹且不是
                if (oldfile.getTypeId() == 0)
                {
                    reductionF(id);
                }
                else
                {
                    oldfile.setIsDel(0);
                    oldfile.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                    fileService.SaveFile(oldfile);
                 }

            }

            //自己便是根
            else if( oldfile.getParentId() == -1)
            {
                //根文件夹
               if(oldfile.getTypeId()==0)
               {
                   reductionF(id);
               }
               //根下的文件
                else
                    {
                        oldfile.setIsDel(0);
                        oldfile.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                        fileService.SaveFile(oldfile);
                    }
            }

            //父文件夹为空,统一放置在同一文件夹下
            else
                {
                DomainFile hsfile = fileService.findbyid(-2);
                oldfile.setParentId(-2);
                fileService.SaveFile(oldfile);
                if (hsfile == null)
                {
                    //新建一个id=-2的文件夹用于存储回收站恢复且其父文件夹已经删除的文件
                    DomainFile nfile = new DomainFile();
                    nfile.setUserId(userid);
                    nfile.setFileValid(0);
                    nfile.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                    nfile.setIsDel(0);
                    nfile.setTypeId(0);
                    nfile.setParentId(-1);
                    nfile.setFileName("回收站还原");
                    nfile.setFileId(-2);
                    nfile.setFileSize(oldfile.getFileSize());//
                    fileService.SaveFile(nfile);


                    //判断恢复文件夹类型
                    if (oldfile.getTypeId() == 0)
                    {
                        reductionF(id);
                    }
                    else
                        {
                        oldfile.setIsDel(0);
                        oldfile.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                        fileService.SaveFile(oldfile);
                    }
                }
                else
                    {
                        hsfile.setFileSize(hsfile.getFileSize()+oldfile.getFileSize());
                        fileService.SaveFile(hsfile);
                    //判断恢复文件类型
                    if (oldfile.getTypeId() == 0)
                    {
                        reductionF1(id);
                    }
                    else
                    {
                        oldfile.setIsDel(0);
                        oldfile.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                        fileService.SaveFile(oldfile);
                    }
                }
            }


        }
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("data", true);
        return map;
    }


    //封装一个递归恢复文件夹的函数！！

    void reductionF(Integer id) {
        Integer count = -1;
        DomainFile fi = fileService.findbyid(id);//找到该文件

        //如果没有文件则结束
        if (fi == null)//!!!
        {
            return;//递归出口
        }

        //如果给定父文件夹下的单独被放回回收站的文件夹则返回递归

        else if (fi.getTypeId() == 0 && fi.getIsDel() == 1 && fi.getFileId() != id) {
            return;
        } else {
            if (fi.getTypeId() != 0)//如果是文件 则直接删除
            {
                fi.setIsDel(0);
                fi.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                fileService.SaveFile(fi);
            } else//如果是文件夹
            {
                count = fileService.countAll_abyP(id);//则找到以其id作为父id的所有文件
                if (count == 0)//空文件夹
                {
                    // fileService.RemoveFile(id);
                    fi.setIsDel(0);
                    fi.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                    fileService.SaveFile(fi);
                    return;
                } else {
                    List<DomainFile> listall = fileService.findAll_abyP(id);//寻找出以其id作为父id的文件
                    for (DomainFile dfile : listall) {
                        //遍历删除文件
                        reductionF(dfile.getFileId());
                    }
                }
                fi.setIsDel(0);
                fi.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                fileService.SaveFile(fi);
            }
        }
    }



    //(-2作为回收站还原的fileid！！！！！)
    void reductionF1(Integer id)
    {
        Integer count = -1;
        DomainFile fi = fileService.findbyid(id);//找到该文件

        //如果没有文件则结束
        if (fi == null)//!!!
        {
            return;//递归出口
        }

        //如果给定父文件夹下的单独被放回回收站的文件夹则返回递归

        else if (fi.getTypeId() == 0 && fi.getIsDel() == 1 && fi.getFileId() != id) {
            return;
        } else {
            if (fi.getTypeId() != 0)//如果是文件 则直接删除
            {
                fi.setIsDel(0);
                fi.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                fi.setParentId(-2);
                fileService.SaveFile(fi);
            } else//如果是文件夹
            {
                count = fileService.countAll_abyP(id);//则找到以其id作为父id的所有文件
                if (count == 0)//空文件夹
                {
                    // fileService.RemoveFile(id);
                    fi.setIsDel(0);
                    fi.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                    fi.setParentId(-2);
                    fileService.SaveFile(fi);
                    return;
                } else {
                    List<DomainFile> listall = fileService.findAll_abyP(id);//寻找出以其id作为父id的文件
                    for (DomainFile dfile : listall) {
                        //遍历删除文件
                        reductionF1(dfile.getFileId());
                    }
                }
                fi.setIsDel(0);
                fi.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                fi.setParentId(-2);
                fileService.SaveFile(fi);
            }
        }
    }


    //回收站删除！！（正确）
    @RequestMapping("recycle_del")
    @ResponseBody
    public Map<String, Object> recycle_del(String fileid) {

        logger.info("fileid:{}", fileid);
        System.out.println("调用回收站删除函数!");
        String[] Infos = fileid.split(",");
        for (int i = 0; i < Infos.length; i++) {
            Integer id = Integer.valueOf(Infos[i]);
            DomainFile file = fileService.findbyid(id);
            //如果是文件夹型，递归删除
            if (file.getTypeId() == 0)
            {
                deleteF1(id);
            }
            //否直接删除
            else
            {
                fileService.RemoveFile(id);
            }
        }
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("data", true);
        return map;
    }


    //封装一个关于递归删除文件夹的方法！！！
    void deleteF1(Integer id) {
        Integer count = -1;
        DomainFile fi = fileService.findbyid(id);//找到该文件
        //如果没有文件则结束
        if (fi == null)//!!!
        {
            return;//递归出口
        }

        //如果给定父文件夹下的单独被放回回收站的文件夹则返回递归
        else if (fi.getTypeId() == 0 && fi.getIsDel() == 1 && fi.getFileId() != id)
        {
            return;
        }
        else {
            if (fi.getTypeId() != 0)//如果是文件 则直接删除
            {
                fileService.RemoveFile(id);
            } else//如果是文件夹
            {
                count = fileService.countAll_abyP(id);//则找到以其id作为父id的所有文件
                // deleteF(file.getFileId());
                if (count == 0)//空文件夹
                {
                    fileService.RemoveFile(id);
                    return;
                } else {
                    List<DomainFile> listall = fileService.findAll_abyP(id);//寻找出以其id作为父id的文件
                    for (DomainFile dfile : listall) {
                        //遍历删除文件
                        deleteF(dfile.getFileId());
                    }
                }
                //System.out.println("删除首文件夹壳!!!");
                fileService.RemoveFile(id);
            }
        }
    }

    //清空回收站（正确）
    @RequestMapping("recycle_delAll")
    @ResponseBody
    public Map<String, Object> recycle_delAll(HttpSession session) {
        System.out.println("调用回收站清空函数!");
        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        List<DomainFile> list =fileService.findAllRecy(userid);
        for(DomainFile fi:list)
        {
            fileService.RemoveFile(fi.getFileId());
        }
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("data", true);
        return map;
    }


    /**
     * 对文件夹的操作
     * @param fileid
     * @return
     */

    //判断类型（正确）
    @RequestMapping("all_judge")
    @ResponseBody
    public Map<String, Object> all_judge(Integer fileid) {

        System.out.println("先判断文件类型是否为文件夹！！");
        DomainFile fi = fileService.findbyid(fileid);
        Integer typeid=fi.getTypeId();
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        if (typeid== 0) {
            map.put("data", true);
        } else
            {
            fi.setRecentDate(new Timestamp(System.currentTimeMillis()));
            fileService.SaveFile(fi);
            map.put("data", false);
            map.put("title", fi.getFileName()+"(更多详情需下载到本地)");
            if(typeid==1)
                {
                    //图片
                    map.put("src", "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1922740179,1840515713&fm=27&gp=0.jpg");
                }
                else if(typeid==2)
                {
                    //其他
                    map.put("src", "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1393385878,1171283051&fm=27&gp=0.jpg");
                }
                else if(typeid==3)
                {
                    //视频
                    map.put("src", "http://img2.imgtn.bdimg.com/it/u=3353719390,3327074354&fm=26&gp=0.jpg");
                }
                else if(typeid==4)
                {
                    //音乐
                    map.put("src", "http://img4.imgtn.bdimg.com/it/u=3747951984,847416224&fm=26&gp=0.jpg");
                }
                else
                {
                    //文档
                    map.put("src", "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2304701565,2703988045&fm=27&gp=0.jpg");
                }
        }
        return map;
    }


    //全部文件列表（正确）
    @RequestMapping("all_list")
    @ResponseBody
    public Map<String, Object> all_list(HttpSession session, Integer parentid, Integer page, Integer limit)
    {
        logger.info("全部文件列表！！！");
        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        Pageable pageRequest = PageRequest.of(page - 1, limit);
        Integer count = 0;
        Page<DomainFile> listall = null;
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        if (parentid == null)
        {
            parentid = -1;
        }
        count = fileService.countAll_all(userid, parentid, 0);
        listall = fileService.findAll_all(userid, parentid, 0, pageRequest);
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", listall.getContent());
        return map;
    }


    //新建文件夹（正确）
    @RequestMapping("all_add")
    @ResponseBody
    public Map<String, Object> all_list(HttpSession session, Integer parentid, String filename) {
        logger.info("parentid:{}", parentid);
        logger.info("filename:{}", filename);
        if (parentid == null) {
            parentid = -1;
        }
        Map<String, Object> map = null;
        String message = null;
        logger.info("新建文件夹！！！");
        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        if (filename == "") {
            message = "文件夹名不能为空，请重命名";
        } else {
            DomainFile fi = fileService.findbyname(userid, filename,0, parentid);
            if (fi != null)

            {
                message = "文件夹重名，请重命名";//自动(+1)
            }
            else
                {
                DomainFile nfile = new DomainFile();
                nfile.setUserId(userid);
                nfile.setFileValid(0);
                nfile.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                nfile.setIsDel(0);
                nfile.setTypeId(0);
                nfile.setParentId(parentid);
                nfile.setFileName(filename);
                logger.info("nfile:{}", nfile);
                fileService.SaveFile(nfile);
                message = "新建成功";
            }
        }
        map = new LinkedHashMap<String, Object>();
        map.put("data", true);
        map.put("message", message);
        return map;
    }


    //将文件夹放入回收站（正确）
    @RequestMapping("all_del")
    @ResponseBody
    public Map<String, Object> all_del(String fileid)
    {
        System.out.println("执行文件夹的删除函数！！！！");
        logger.info("fileid:{}", fileid);
        String[] Infos = fileid.split(",");
        for (int i = 0; i < Infos.length; i++)
        {
            Integer id = Integer.valueOf(Infos[i]);
            //调用递归删除文件函数

            DomainFile file = fileService.findbyid(id);

            //先对要涉及到的父文件夹进行减
            int parentid = file.getParentId();
            int filesize = file.getFileSize();
            //直到遍历完根目录!!!
            while (parentid != -1) {
                //找到父文件
                DomainFile pf = fileService.findbyid(parentid);
                System.out.println(pf.getFileId() + " " + pf.getFileSize());
                pf.setFileSize(pf.getFileSize() - filesize);
                fileService.SaveFile(pf);
                System.out.println("删除后大小为：" + pf.getFileSize());
                parentid = pf.getParentId();
            }

            if (file.getTypeId() == 0)//如果放到回收站的是一个文件夹,对表面上的文件夹置1
            {
                deleteF(id);
                file.setIsDel(1);
                fileService.SaveFile(file);

            }

            else//如果放回回收站的是文件
            {

                //执行放入回收站操作
               file.setIsDel(1);
               file.setFileValid(10);
               file.setDelDate(new Timestamp(System.currentTimeMillis()));
               fileService.SaveFile(file);
            }
        }

        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("data", true);
        return map;
    }

    //放到回收站，(均置2 缓存起来)
    void deleteF(Integer id)
    {
        Integer count = -1;
        DomainFile fi = fileService.findbyid(id);//找到该文件

        //如果文件为空则结束
        if (fi == null)//!!!
        {
            return;//递归出口
        }
        else
            {
              if (fi.getIsDel() == 0)
              {
                if (fi.getTypeId() != 0)//如果是文件且其状态为0   则将isDel置2
                {
                    fi.setIsDel(2);
                    fi.setFileValid(10);
                    fi.setDelDate(new Timestamp(System.currentTimeMillis()));
                    fileService.SaveFile(fi);
                }
                else//文件夹
                    {
                    count = fileService.countAll_abyP(id);//则找到以其id作为父id的所有文件
                    // deleteF(file.getFileId());
                    if (count == 0)//空文件夹
                    {
                        //fileService.RemoveFile(id);
                        fi.setIsDel(2);
                        fi.setFileValid(10);
                        fi.setDelDate(new Timestamp(System.currentTimeMillis()));
                        fileService.SaveFile(fi);
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
                    fi.setIsDel(2);
                    fi.setFileValid(10);
                    fi.setDelDate(new Timestamp(System.currentTimeMillis()));
                    fileService.SaveFile(fi);
                }
            }
        }
    }

    //正所有文件中模糊查询（正确）
    @RequestMapping("search_allname")
    @ResponseBody
    public Map<String, Object> search_allname(HttpSession session, String filename, Integer page, Integer limit) {
        System.out.println("模糊查询");
        logger.info("filename:{}", filename);
        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        Pageable pageRequest = PageRequest.of(page - 1, limit);
        Integer count = 0;
        Page<DomainFile> filename_page = null;
        if (filename == "") {
            System.out.println("filename为空");
            filename_page = fileService.findAll_all(userid, -1, 0, pageRequest);
            count = fileService.countAll_all(userid, -1, 0);
        } else {
            String filena = "%" + filename + "%";
            count = fileService.countAll_namelike(userid, 0,filena);
            filename_page = fileService.findAll_namelike(userid,0,filena, pageRequest);
        }
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", filename_page.getContent());
        return map;
    }

    //ztree展示（正确）
    @RequestMapping("show_Tree")
    @ResponseBody
    public Map<String, Object> show_Tree(HttpSession session) {
        logger.info("zTree展示");
        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        //调用保存函数！！
        List<DomainFile> listall = fileService.findAlluserbyt(userid, 0);
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        map.put("data", listall);
        return map;
    }

    //文件的移动(正确！！)
    @RequestMapping("move_file")
    @ResponseBody
    public Map<String, Object> move_file(String oldfid, Integer newfid) {
        logger.info("移动文件位置！");
        if (newfid == null) {
            newfid = -1;
        }
        //拆分需移动的id;并将新文件夹的id设置为文件的新父id;批量移动
        String[] Infos = oldfid.split(",");
        Integer filesize = 0;
        //oid ;
        //一次对需移动的文件进行处理
        for (int i = 0; i < Infos.length; i++)
        {
            //filesize=0;
            Integer oid = Integer.valueOf(Infos[i]);

            //找到要移动的文件
            DomainFile ofi = fileService.findbyid(oid);

            filesize += ofi.getFileSize();

            //对之前的文件夹进行减

            //首先计算父文件ID
            DomainFile pf = null;
            Integer oparentid = ofi.getParentId();
            while (oparentid != -1) {
                //找到父文件
                pf = fileService.findbyid(oparentid);

                pf.setFileSize(pf.getFileSize() - ofi.getFileSize());
                fileService.SaveFile(pf);
                oparentid = pf.getParentId();
            }
            //移动文件
            ofi.setParentId(newfid);
            fileService.SaveFile(ofi);
        }

            //将移动到的文件size进行更正,对新的文件夹进行加。
            DomainFile nfi = fileService.findbyid(newfid);
            Integer parentid1 = nfi.getFileId();
            DomainFile pf1 = null;
            if (parentid1 != -1) {
                while (parentid1 != -1) {
                    //找到父文件
                    pf1 = fileService.findbyid(parentid1);
                    pf1.setFileSize(pf1.getFileSize() + filesize);
                    fileService.SaveFile(pf1);
                    parentid1 = pf1.getParentId();
                }
            } else {
                pf1 = fileService.findbyid(parentid1);
                pf1.setFileSize(pf1.getFileSize() + filesize);
                fileService.SaveFile(pf1);
            }
            //根文件夹
            Map<String, Object> map = null;
            map = new LinkedHashMap<String, Object>();
            map.put("data", true);
            return map;
    }
}
