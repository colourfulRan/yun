package cqjtu.ds.yun.service;

import cqjtu.ds.yun.result.ExecuteResult;
import cqjtu.ds.yun.service.domain.DomainFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;


public interface FileService
{

   List<DomainFile>findallRec(Integer userid,Integer start, Integer size);
   Integer CountRec(Integer userid);

    List<DomainFile>findAll(Integer userid);
    Integer CountAll(Integer userid);
    
//图片分页展示
Page<DomainFile>findAllFiles(Integer userid, Integer typeid, Boolean isdel, Pageable pageable);

//根据图片名的分页模糊查询
 Page<DomainFile>findAllbyFname(Integer userid, Integer typeid,Boolean isdel,String filename,Pageable pageable);

    /**
     * 根据id查找文件
      */
    DomainFile findbyid(Integer fileid);
    List<DomainFile>findAllbyisdel(Boolean isdel);

    /**
     * 根据id删除文件
     */
    void RemoveFile(Integer fileid);

    /**
     * 清空回收站文件
     */
    void RemoveAll(Boolean isDel);

    /**
     * 更新文件
     */
    DomainFile  UpdateFile( DomainFile file);

    /**
     * 添加文件
     */
    DomainFile  SaveFile( DomainFile  file);

 /**
  *根据类型和用户获取数据量
  */
   Integer FCount(Integer userid,Integer typeid,Boolean isdel);

   /*
   根据文件模糊名获取数据量
    */
   Integer Fname_Count(Integer userid,Integer typeid,Boolean isdel,String filename);

   /*
   回收站文件数量
    */
   Integer RecycleCount(Integer userid,Boolean isdel);

   Page<DomainFile>findAllRecycle(Integer userid,Boolean isdel,Pageable pageable);


  Integer Sec_Count(Integer userid);
  List<DomainFile> findAllSec(Integer userid,Integer start,Integer size);

 Integer FnameSec_Count(String name,Integer userid);
 List<DomainFile>findAllFnameSec(String name,Integer userid,Integer start,Integer size);

 Page<DomainFile>findAll_all(Integer userid,Integer parentid,Pageable pageable);
 Integer countAll_all(Integer userid, Integer parentid);



 List<DomainFile>findAll_abyP(Integer parentid);
 Integer countAll_abyP(Integer parentid);

 //
    Page<DomainFile>findAll_namelike(Integer userid,String filename,Pageable pageable);
    Integer countAll_namelike(Integer userid,String filename);

}
