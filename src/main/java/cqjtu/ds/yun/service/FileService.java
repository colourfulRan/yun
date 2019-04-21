package cqjtu.ds.yun.service;

import cqjtu.ds.yun.result.ExecuteResult;
import cqjtu.ds.yun.service.domain.DomainFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;


public interface FileService
{
 //  ExecuteResult<List<File>> findAllFiles(Integer userid,Integer typeid);
//图片分页展示
Page<DomainFile>findAllFiles(Integer userid, Integer typeid, Boolean isdel, Pageable pageable);

//根据图片名的分页模糊查询
 Page<DomainFile>findAllbyimagename(Integer userid, Integer typeid,Boolean isdel,String filename,Pageable pageable);
    /**
     * 删除文件
     */
    void RemoveFile(String fileid);

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
   Integer ImageCount(Integer userid,Integer typeid,Boolean isdel);

   /*
   根据文件模糊名获取数据量
    */
   Integer Imagename_Count(Integer userid,Integer typeid,Boolean isdel,String filename);
}
