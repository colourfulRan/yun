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
        return fileRepo.findByFileId( fileid);
    }

    //update、delete操作需要使用事务
    @Transactional
    @Override
    public void RemoveFile(Integer fileid) {
        fileRepo.deleteByFileId(fileid);
    }


    @Override
    public List<DomainFile>findAllRecy(Integer userid) {
      return  fileRepo.findAllRecy(userid);
    }


    @Transactional
    @Override
    public DomainFile SaveFile(DomainFile file) {
        return fileRepo.save(file);
    }

    @Override
    public List<DomainFile> findData()
    {
      return fileRepo.findData();
    }

    @Override
    public Integer FCount(Integer userid, Integer typeid, Integer isdel) {
        return fileRepo.countAllByUserIdAndTypeIdAndIsDel(userid, typeid, isdel);
    }

    @Override
    public Integer Fname_Count(Integer userid, Integer typeid, Integer isdel, String filename) {
        return fileRepo.countAllByUserIdAndTypeIdAndIsDelAndFileNameLike(userid, typeid, isdel, filename);
    }

    @Override
    public Page<DomainFile> findAllFiles(Integer userid, Integer typeid, Integer isdel, Pageable pageable) {

        return fileRepo.findAllByUserIdAndTypeIdAndIsDel(userid, typeid, isdel, pageable);
    }

    @Override
    public Page<DomainFile> findAllbyFname(Integer userid, Integer typeid, Integer isdel, String filename, Pageable pageable) {
        return fileRepo.findAllByUserIdAndTypeIdAndIsDelAndFileNameLike(userid, typeid, isdel, filename, pageable);
    }

    @Override
    public Integer RecycleCount(Integer userid, Integer isdel) {
        return fileRepo.countAllByUserIdAndIsDel(userid, isdel);
    }

    @Override
    public Page<DomainFile> findAllRecycle(Integer userid, Integer isdel, Pageable pageable) {
        return fileRepo.findAllByUserIdAndIsDel(userid, isdel, pageable);
    }

    @Override
    public Integer Sec_Count(Integer userid) {
        return fileRepo.countFile(userid);
    }

    @Override
    public List<DomainFile> findAllSec(Integer userid, Integer start, Integer size)
    {
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
    public List<DomainFile> findallRec(Integer userid,Integer start, Integer size)
    {
        return fileRepo.findAllRecent(userid,start,size);
    }

    @Override
    public Integer CountRec(Integer userid) {
        return fileRepo.countRecent(userid);
    }


    @Override
    public Page<DomainFile> findAll_all(Integer userid, Integer parentid,Integer isDel, Pageable pageable) {
        return fileRepo.findAllByUserIdAndParentIdAndIsDel(userid, parentid, isDel, pageable);
    }

    @Override
    public Integer countAll_all(Integer userid, Integer parentid,Integer isDel) {
        return fileRepo.countAllByUserIdAndParentIdAndIsDel(userid, parentid, isDel);
    }

    @Override
    public List<DomainFile> findAll_abyP(Integer parentid)
    {
        return fileRepo.findAllByP(parentid);
    }

   @Override
    public Integer countAll_abyP(Integer parentid)
    {
        return fileRepo.countAllByParentId(parentid);
    }

   @Override
    public Page<DomainFile> findAll_abyP1(Integer parentid,Pageable pageable) {
        return fileRepo.findAllByParentId(parentid,pageable);
    }

    @Override
    public Page<DomainFile> findAll_namelike(Integer userid,Integer isdel, String filename, Pageable pageable) {
        return fileRepo.findAllByUserIdAndIsDelAndFileNameLike(userid, isdel, filename, pageable);
    }

    @Override
    public Integer countAll_namelike(Integer userid, Integer isdel,String filename) {
       return fileRepo.countAllByUserIdAndIsDelAndFileNameLike(userid, isdel, filename);
    }

   @Override
    public List<DomainFile> findAlluserbyt(Integer userid, Integer typeid) {
        return fileRepo.findAllByUserIdAndTypeId(userid, typeid);
    }


    @Override
    public DomainFile findbyname(Integer userid, String filename,Integer isdel,Integer parentid) {
        return fileRepo.findByUserIdAndFileNameAndIsDelAndParentId(userid, filename,isdel, parentid);
    }


}



