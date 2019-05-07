package cqjtu.ds.yun.dal;

import cqjtu.ds.yun.service.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public interface UserRepo extends JpaRepository<User,Integer>
{
// public static Map<Integer, User> users =  new HashMap<Integer,User>();

 Page<User>findByUsernameLike(String username, Pageable pageable);//模糊查询，分页展示；
 User findByUsername(String username);
 User findByUsernameAndPassword(String username,String password);//
 List<User> findAll();
// List<User> listUser();
// User deleteUserByUserId(String userid);
// User saveOrUpdataUser(User user);
// User findByUserId(String userid);

 //User findByPassword(String password);


}
