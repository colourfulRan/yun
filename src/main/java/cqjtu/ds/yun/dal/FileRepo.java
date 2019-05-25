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

    //展示十天内查看过的文件
    @Query(value="SELECT * from file where recent_date is not null and is_del=0 and user_id=:user_id and (UNIX_TIMESTAMP(current_timestamp())-UNIX_TIMESTAMP(recent_date))/60/60/24<=10 limit :start,:size",nativeQuery=true)
    List<DomainFile>findAllRecent(@Param("user_id")Integer user_id, @Param("start")Integer start, @Param("size")Integer size);
    @Query(value = "select count(*) from file where user_id=?1 and is_del=0 and recent_date is not null and (UNIX_TIMESTAMP(current_timestamp())-UNIX_TIMESTAMP(recent_date))/60/60/24<=10", nativeQuery = true)
    Integer countRecent(Integer userId);

    //文档列表（5、6、7、8、10）
    @Query(value = "select * from file where is_del=0 and user_id=:user_id  and (type_id=5 or type_id=6 or type_id=7 or type_id=8 or type_id=10) limit :start,:size",nativeQuery = true)
    List<DomainFile>findAllFile(@Param("user_id")Integer user_id, @Param("start")Integer start, @Param("size")Integer size);
    @Query(value = "select count(*) from file where is_del=0 and user_id=:user_id  and (type_id=5 or type_id=6 or type_id=7 or type_id=8 or type_id=10) ",nativeQuery = true)
    Integer countFile(@Param("user_id")Integer user_id);

  //文档列表之迷糊查询
    @Query(value = "select * from file where is_del=0 and file_name Like :file_name and user_id=:user_id and (type_id=5 or type_id=6 or type_id=7 or type_id=8 or type_id=10)  limit :start,:size",nativeQuery = true)
    List<DomainFile>findAllFilenameLike(@Param("file_name")String file_name,@Param("user_id")Integer user_id, @Param("start")Integer start, @Param("size")Integer size);
    @Query(value = "select count(*) from file where is_del=0 and file_name Like :file_name and user_id=:user_id  and (type_id=5 or type_id=6 or type_id=7 or type_id=8 or type_id=10)",nativeQuery = true)
    Integer countAllnameLike(@Param("file_name")String file_name,@Param("user_id")Integer user_id);

    //清空回收站
    @Query(value = "select * from file where user_id=:user_id  and (is_del=1 or is_del=2 )",nativeQuery = true)
    List<DomainFile> findAllRecy(@Param("user_id")Integer user_id);

    //定时删除类型为1,2的数据
    @Query(value = "select * from file where is_del=1 or is_del=2",nativeQuery = true)
    List<DomainFile> findData();

    //用于递归找文件夹的子文件时
   @Query(value = "select * from file where parent_id=1?",nativeQuery = true)
    List<DomainFile>findAllByP(Integer parentId);

    //打开所在目录时需要
    Page<DomainFile>findAllByParentId(Integer parentId,Pageable pageable);
    Integer countAllByParentId(Integer parentId);

   //ztree中加载文件夹
    List<DomainFile> findAllByUserIdAndTypeId(Integer userId,Integer typeId);

   //各文件列表
    Page<DomainFile> findAllByUserIdAndTypeIdAndIsDel(Integer userId,Integer typeId,Integer isDel,Pageable pageable);
    Integer countAllByUserIdAndTypeIdAndIsDel(Integer userId, Integer typeId,Integer isDel);

    //用于分类中文件名的模糊查询
    Page<DomainFile>findAllByUserIdAndTypeIdAndIsDelAndFileNameLike(Integer userId, Integer typeId,Integer isDel,String fileName,Pageable pageable);
    Integer countAllByUserIdAndTypeIdAndIsDelAndFileNameLike(Integer userId, Integer typeId,Integer isDel,String fileName);

    //回收站列表（某用户且被放回回收站的文件）
    Page<DomainFile>findAllByUserIdAndIsDel(Integer userId,Integer isDel,Pageable pageable);
    Integer countAllByUserIdAndIsDel(Integer userId,Integer isDel);

    //全部文件的列表
    Page<DomainFile>findAllByUserIdAndParentIdAndIsDel(Integer userId,Integer parentId,Integer isDel,Pageable pageable);
    Integer countAllByUserIdAndParentIdAndIsDel(Integer userId,Integer parentId,Integer isDel);

    //在全部文件中进行模糊查找
    Page<DomainFile>findAllByUserIdAndIsDelAndFileNameLike(Integer userId,Integer isDel,String fileName,Pageable pageable);
    Integer countAllByUserIdAndIsDelAndFileNameLike(Integer userId,Integer isDel,String fileName);


    //数据库中文件fileid唯一！！！
    DomainFile findByFileId(Integer fileId);
    //删除文件
    void deleteByFileId(Integer fileId);


    //同级文件夹新建重名问题
    DomainFile findByUserIdAndFileNameAndIsDelAndParentId(Integer userId,String fileName,Integer isDel,Integer parentId);
}
