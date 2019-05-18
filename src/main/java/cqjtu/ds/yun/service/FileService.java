package cqjtu.ds.yun.service;

import cqjtu.ds.yun.service.domain.DomainFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;


public interface FileService
{

    //最近文件列表
   List<DomainFile>findallRec(Integer userid,Integer start, Integer size);
   Integer CountRec(Integer userid);

//文件分页展示
Page<DomainFile>findAllFiles(Integer userid, Integer typeid, Integer isdel, Pageable pageable);

//根据图片名的分页模糊查询
 Page<DomainFile>findAllbyFname(Integer userid, Integer typeid,Integer isdel,String filename,Pageable pageable);

    //  根据id查找文件
    DomainFile findbyid(Integer fileid);

     //根据id删除文件
    void RemoveFile(Integer fileid);

     // 清空回收站文件,
    void RemoveAllRecy(Integer userid);

    //定时器
   List<DomainFile> findData();

     //更新文件
    DomainFile  SaveFile( DomainFile  file);

 //根据类型和用户获取数据量
   Integer FCount(Integer userid,Integer typeid,Integer isdel);

   //根据文件模糊名获取数据量
   Integer Fname_Count(Integer userid,Integer typeid,Integer isdel,String filename);

   // 回收站文件列表
   Page<DomainFile>findAllRecycle(Integer userid,Integer isdel,Pageable pageable);
   Integer RecycleCount(Integer userid,Integer isdel);

   //文档列表
   List<DomainFile> findAllSec(Integer userid,Integer start,Integer size);
   Integer Sec_Count(Integer userid);

   //文档模糊查询
   Integer FnameSec_Count(String name,Integer userid);
   List<DomainFile>findAllFnameSec(String name,Integer userid,Integer start,Integer size);

   //全部文件列表
 Page<DomainFile>findAll_all(Integer userid,Integer parentid,Integer isDel,Pageable pageable);
 Integer countAll_all(Integer userid, Integer parentid,Integer isDel);

 //递归时查询文件夹文件
 List<DomainFile>findAll_abyP(Integer parentid);

 //加载文件夹文件
 Integer countAll_abyP(Integer parentid);
 Page<DomainFile>findAll_abyP1(Integer parentid,Pageable pageable);

 //文件夹中模糊查询
 Page<DomainFile>findAll_namelike(Integer userid,Integer isdel,String filename,Pageable pageable);
 Integer countAll_namelike(Integer userid,Integer isdel,String filename);

 //zTree加载文件夹
    List<DomainFile>findAlluserbyt(Integer userid,Integer typeid);

//同级文件名称问题
    DomainFile findbyname(Integer userid,String filename,Integer parentid);
}
