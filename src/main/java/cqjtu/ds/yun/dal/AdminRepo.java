package cqjtu.ds.yun.dal;

import cqjtu.ds.yun.service.domain.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepo  extends JpaRepository<Admin,Integer>{



//    public Admin findByUsernameAndPassword(String adminname, String password);//
        Admin findByAdminnameAndPassword(String adminname, String password);
}

