package cqjtu.ds.yun.dal;


import cqjtu.ds.yun.service.domain.DomainFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface FileRepo extends JpaRepository<DomainFile,Integer>
{

    @Query(value="SELECT * from file where recent_date is not null and user_id=:user_id and (UNIX_TIMESTAMP(current_timestamp())-UNIX_TIMESTAMP(recent_date))/60/60/24<=10 limit :start,:size",nativeQuery=true)
    List<DomainFile>findAllRecent(@Param("user_id")Integer user_id, @Param("start")Integer start, @Param("size")Integer size);
    @Query(value = "select count(*) from file where user_id=?1 and recent_date is not null and (UNIX_TIMESTAMP(current_timestamp())-UNIX_TIMESTAMP(recent_date))/60/60/24<=10", nativeQuery = true)
    Integer countRecent(Integer userId);

    @Query(value = "select * from file where user_id=:user_id  and (type_id=5 or type_id=6 or type_id=7 or type_id=8) limit :start,:size",nativeQuery = true)
    List<DomainFile>findAllFile(@Param("user_id")Integer user_id, @Param("start")Integer start, @Param("size")Integer size);
    @Query(value = "select count(*) from file where user_id=:user_id  and (type_id=5 or type_id=6 or type_id=7 or type_id=8) ",nativeQuery = true)
    Integer countFile(@Param("user_id")Integer user_id);

    @Query(value = "select * from file where file_name Like 1? and user_id=2? and (type_id=5 or type_id=6 or type_id=7 or type_id=8) limit ?,?",nativeQuery = true)
    List<DomainFile>findAllFilenameLike(String name, Integer userId, Integer start,Integer size);
    @Query(value = "select count(*) from file where file_name Like 1? and user_id=2?  and (type_id=5 or type_id=6 or type_id=7 or type_id=8)",nativeQuery = true)
    Integer countAllnameLike(String name,Integer userId);





    List<DomainFile> findAllByUserId(Integer userId);

    List<DomainFile> findAllByUserIdAndTypeId(Integer userId,Integer typeId);
    List<DomainFile>findAllByIsDel(Boolean isDel);

    List<DomainFile>findAllByParentId(Integer parentId);
    Integer countAllByParentId(Integer parentId);


    Page<DomainFile> findAllByUserIdAndTypeIdAndIsDel(Integer userId,Integer typeId,Boolean isDel,Pageable pageable);
    Page<DomainFile>findAllByUserIdAndTypeIdAndIsDelAndFileNameLike(Integer userId, Integer typeId,Boolean isDel,String fileName,Pageable pageable);
    Page<DomainFile>findAllByUserIdAndIsDel(Integer userId,Boolean isDel,Pageable pageable);


    Integer countAllByUserIdAndTypeIdAndIsDel(Integer userId, Integer typeId,Boolean isDel);

    Integer countAllByUserIdAndTypeIdAndIsDelAndFileNameLike(Integer userId, Integer typeId,Boolean isDel,String fileName);
    Integer countAllByUserIdAndIsDel(Integer userId,Boolean isDel);
    Integer countAllByUserId(Integer userId);


    Page<DomainFile>findAllByUserIdAndParentId(Integer userId,Integer parentId,Pageable pageable);
    Integer countAllByUserIdAndParentId(Integer userId,Integer parentId);


    //在所有文件中进行模糊查找
    Page<DomainFile>findAllByUserIdAndFileNameLike(Integer userId,String fileName,Pageable pageable);
    Integer countAllByUserIdAndFileNameLike(Integer userId,String fileName);


    //
    DomainFile findByFileId(Integer fileId);
    void deleteByFileId(Integer fileId);
    void deleteAllByIsDel(Boolean isDel);
}
