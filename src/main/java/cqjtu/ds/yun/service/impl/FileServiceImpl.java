package cqjtu.ds.yun.service.impl;


import cqjtu.ds.yun.dal.FileRepo;
import cqjtu.ds.yun.result.ExecuteResult;
import cqjtu.ds.yun.service.FileService;

import cqjtu.ds.yun.service.domain.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
public class FileServiceImpl implements FileService
{
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);//日志打印

     @Autowired
     private FileRepo fileRepo;

    @Transactional
    @Override
    public void RemoveFile(String fileid)
    {
        fileRepo.deleteByFileid(fileid);
    }

    @Transactional
    @Override
    public File UpdateFile(File file) {
        return fileRepo.save(file);
    }

    @Transactional
    @Override
    public File SaveFile(File file) {
        return fileRepo.save(file);
    }


    @Override
    public Integer ImageCount(Integer userid,Integer typeid,Integer isdel) {
        return fileRepo.countAllByUseridAndTypeidAndIsdel(userid, typeid, isdel);
    }

    @Override
    public Integer Imagename_Count(Integer userid,Integer typeid,Integer isdel,String filename) {
        return fileRepo.countAllByUseridAndTypeidAndIsdelAndFilenameLike(userid, typeid, isdel, filename);
    }

    /* @Override
    public ExecuteResult<List<File>> findAllFiles(Integer userid,Integer typeid)
    {

        ExecuteResult<List<File>> filesExecuteResult = new ExecuteResult<>();
        try {
            List<File> listAll = fileRepo.findAllByUseridAndTypeid(userid, typeid);
            logger.info("listAll:{}",listAll);

            List<File> filesList = new ArrayList<>();
           filesExecuteResult.setExecuted(true);
            if (listAll != null)
            {

                for (File file: listAll) {
                    logger.info("file:{}",file);

                    filesList.add(file);
                }
                //
                logger.info("files:{}",filesList);
                filesExecuteResult.setSuccess(true);
                filesExecuteResult.setCode("0000");
                filesExecuteResult.setMessage("查询成功！");
                filesExecuteResult.setData(filesList);
            }else {
              filesExecuteResult.setSuccess(false);
              filesExecuteResult.setCode("1000");
              filesExecuteResult.setMessage("暂停营业");
            }

        }catch (Exception e) {
            filesExecuteResult.setExecuted(false);
            filesExecuteResult.setSuccess(false);
            filesExecuteResult.setCode("1111");
            filesExecuteResult.setMessage("系统异常");
            logger.warn("查询失败！",e);
        }
        return filesExecuteResult;
    }
*/

   @Override
    public Page<File> findAllFiles(Integer userid, Integer typeid,Integer isdel, Pageable pageable)
   {

        return fileRepo.findAllByUseridAndTypeidAndIsdel(userid, typeid, isdel, pageable);
    }

    @Override
    public Page<File> findAllbyimagename(Integer userid, Integer typeid,Integer isdel,String filename,Pageable pageable)
    {
        return fileRepo.findAllByUseridAndTypeidAndIsdelAndFilenameLike(userid, typeid, isdel, filename, pageable);
    }
}
