package cqjtu.ds.yun.dal;

import cqjtu.ds.yun.service.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepo extends JpaRepository<User,Integer>
{

 Page<User>findByUsernameLike(String username, Pageable pageable);//模糊查询，分页展示；
 User findByUsername(String username);
 User findByUsernameAndPassword(String username,String password);//

 //User findByPassword(String password);


}
