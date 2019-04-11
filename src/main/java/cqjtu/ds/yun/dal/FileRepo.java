package cqjtu.ds.yun.dal;

import cqjtu.ds.yun.service.domain.DomainFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepo extends JpaRepository<DomainFile,Integer> {
}
