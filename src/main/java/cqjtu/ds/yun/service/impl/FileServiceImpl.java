package cqjtu.ds.yun.service.impl;


import cqjtu.ds.yun.dal.FileRepo;
import cqjtu.ds.yun.service.FileService;

import cqjtu.ds.yun.service.domain.DomainFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.List;


@Service
public class FileServiceImpl implements FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);//日志打印

    @Autowired
    private FileRepo fileRepo;

    @Override
    public DomainFile findbyid(Integer fileid) {
        return fileRepo.findByFileId(fileid);
    }

    //update、delete操作需要使用事务
    @Transactional
    @Override
    public void RemoveFile(Integer fileid) {
        fileRepo.deleteByFileId(fileid);
    }

    @Transactional
    @Override
    public void RemoveAll(Boolean isDel) {
        fileRepo.deleteAllByIsDel(isDel);
    }

    @Transactional
    @Override
    public DomainFile UpdateFile(DomainFile file) {
        return fileRepo.save(file);
    }

    @Transactional
    @Override
    public DomainFile SaveFile(DomainFile file) {
        return fileRepo.save(file);
    }

    @Override
    public Integer FCount(Integer userid, Integer typeid, Boolean isdel) {
        return fileRepo.countAllByUserIdAndTypeIdAndIsDel(userid, typeid, isdel);
    }

    @Override
    public Integer Fname_Count(Integer userid, Integer typeid, Boolean isdel, String filename) {
        return fileRepo.countAllByUserIdAndTypeIdAndIsDelAndFileNameLike(userid, typeid, isdel, filename);
    }

    @Override
    public Page<DomainFile> findAllFiles(Integer userid, Integer typeid, Boolean isdel, Pageable pageable) {

        return fileRepo.findAllByUserIdAndTypeIdAndIsDel(userid, typeid, isdel, pageable);
    }

    @Override
    public Page<DomainFile> findAllbyFname(Integer userid, Integer typeid, Boolean isdel, String filename, Pageable pageable) {
        return fileRepo.findAllByUserIdAndTypeIdAndIsDelAndFileNameLike(userid, typeid, isdel, filename, pageable);
    }

    @Override
    public Integer RecycleCount(Integer userid, Boolean isdel) {
        return fileRepo.countAllByUserIdAndIsDel(userid, isdel);
    }

    @Override
    public Page<DomainFile> findAllRecycle(Integer userid, Boolean isdel, Pageable pageable) {
        return fileRepo.findAllByUserIdAndIsDel(userid, isdel, pageable);
    }

    @Override
    public List<DomainFile> findAllbyisdel(Boolean isdel) {
        return fileRepo.findAllByIsDel(isdel);
    }

    @Override
    public Integer Sec_Count(Integer userid) {
        return fileRepo.countFile(userid);
    }

    @Override
    public List<DomainFile> findAllSec(Integer userid, Integer start, Integer size) {
        return fileRepo.findAllFile(userid, start, size);
    }

    @Override
    public Integer FnameSec_Count(String name, Integer userid) {
        return fileRepo.countAllnameLike(name, userid);
    }

    @Override
    public List<DomainFile> findAllFnameSec(String name, Integer userid, Integer start, Integer size) {
        return fileRepo.findAllFilenameLike(name, userid, start, size);
    }


    @Override
    public List<DomainFile> findAll(Integer userid)
    {
        return fileRepo.findAllByUserId(userid);
    }

    @Override
    public Integer CountAll(Integer userid) {
        return fileRepo.countAllByUserId(userid);
    }

    @Override
    public List<DomainFile> findallRec(Integer userid,Integer start, Integer size)
    {
        return fileRepo.findAllRecent(userid,start,size);
    }

    @Override
    public Integer CountRec(Integer userid) {
        return fileRepo.countRecent(userid);
    }


    @Override
    public Page<DomainFile> findAll_all(Integer userid, Integer parentid, Pageable pageable) {
        return fileRepo.findAllByUserIdAndParentId(userid, parentid, pageable);
    }

    @Override
    public Integer countAll_all(Integer userid, Integer parentid) {
        return fileRepo.countAllByUserIdAndParentId(userid, parentid);
    }





    @Override
    public List<DomainFile> findAll_abyP(Integer parentid)
    {
        return fileRepo.findAllByParentId(parentid);
    }

   @Override
    public Integer countAll_abyP(Integer parentid)
    {
        return fileRepo.countAllByParentId(parentid);
    }


    @Override
    public Page<DomainFile> findAll_namelike(Integer userid, String filename, Pageable pageable) {
        return fileRepo.findAllByUserIdAndFileNameLike(userid, filename, pageable);
    }

    @Override
    public Integer countAll_namelike(Integer userid, String filename) {
       return fileRepo.countAllByUserIdAndFileNameLike(userid, filename);
    }
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
