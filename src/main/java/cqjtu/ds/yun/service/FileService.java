package cqjtu.ds.yun.service;

import cqjtu.ds.yun.result.ExecuteResult;
import cqjtu.ds.yun.service.domain.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;


public interface FileService
{
 //  ExecuteResult<List<File>> findAllFiles(Integer userid,Integer typeid);
//图片分页展示
Page<File>findAllFiles(Integer userid, Integer typeid,Integer isdel,Pageable pageable);

//根据图片名的分页模糊查询
 Page<File>findAllbyimagename(Integer userid, Integer typeid,Integer isdel,String filename,Pageable pageable);
    /**
     * 删除文件
     */
    void RemoveFile(String fileid);

    /**
     * 更新文件
     */
    File UpdateFile(File file);

    /**
     * 添加文件
     */
    File SaveFile(File file);

 /**
  *根据类型和用户获取数据量
  */
   Integer ImageCount(Integer userid,Integer typeid,Integer isdel);

   /*
   根据文件模糊名获取数据量
    */
   Integer Imagename_Count(Integer userid,Integer typeid,Integer isdel,String filename);
}
