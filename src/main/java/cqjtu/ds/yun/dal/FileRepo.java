package cqjtu.ds.yun.dal;

import cqjtu.ds.yun.service.domain.File;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface FileRepo extends JpaRepository<File,Integer>
{
    List<File> findAll();
    List<File> findAllByUseridAndTypeid(Integer userid,Integer typeid);

    Page<File> findAllByUseridAndTypeidAndIsdel(Integer userid,Integer typeid,Integer isdel,Pageable pageable);


    Page<File>findAllByUseridAndTypeidAndIsdelAndFilenameLike(Integer userid, Integer typeid,Integer isdel,String filename,Pageable pageable);
    //FilenameLike();

    void deleteByFileid(String fileid);

    Integer countAllByUseridAndTypeidAndIsdel(Integer userid, Integer typeid,Integer isdel);

    Integer countAllByUseridAndTypeidAndIsdelAndFilenameLike(Integer userid, Integer typeid,Integer isdel,String filename);
   // FilenameLike

    //List<File>findAllByFilename(String filename) ;
    //List<File>findAllByUser_idAndType_id(int user_id,int type_id);

}
