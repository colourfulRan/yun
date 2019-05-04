package cqjtu.ds.yun.dal;


import cqjtu.ds.yun.service.domain.DomainFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface FileRepo extends JpaRepository<DomainFile,Integer>
{
    List<DomainFile> findAll();
    List<DomainFile> findAllByUserIdAndTypeId(Integer userId,Integer typeId);

    Page<DomainFile> findAllByUserIdAndTypeIdAndIsDel(Integer userId,Integer typeId,Boolean isDel,Pageable pageable);
    Page<DomainFile>findAllByUserIdAndTypeIdAndIsDelAndFileNameLike(Integer userId, Integer typeId,Boolean isDel,String fileName,Pageable pageable);
    //FilenameLike();

    void deleteByFileId(String fileId);

    Integer countAllByUserIdAndTypeIdAndIsDel(Integer userId, Integer typeId,Boolean isDel);

    Integer countAllByUserIdAndTypeIdAndIsDelAndFileNameLike(Integer userId, Integer typeId,Boolean isDel,String fileName);

    DomainFile findByFileId(int fileId);

}
