package cqjtu.ds.yun.dal;

import cqjtu.ds.yun.service.domain.Detail;
import cqjtu.ds.yun.service.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface UserRepo extends JpaRepository<User,Integer>
{

 Page<User>findByUsernameLike(String username, Pageable pageable);//模糊查询，分页展示；
 User findByUsername(String username);
 User findByUsernameAndPassword(String username,String password);//

 User findByUserId(Integer userId);

 ;



 //User findByPassword(String password);


}
