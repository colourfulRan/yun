package cqjtu.ds.yun.dal;

import cqjtu.ds.yun.service.domain.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileTypeRepo extends JpaRepository<Type,Integer>{
   List<Type> findAll();

}
