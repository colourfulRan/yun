package cqjtu.ds.yun.dal;

import cqjtu.ds.yun.service.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer>{
}
