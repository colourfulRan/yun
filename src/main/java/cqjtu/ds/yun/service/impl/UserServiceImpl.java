package cqjtu.ds.yun.service.impl;

import cqjtu.ds.yun.dal.UserRepo;
import cqjtu.ds.yun.result.ExecuteResult;
import cqjtu.ds.yun.service.UserService;
import cqjtu.ds.yun.service.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 用户服务接口实现
 */
@Service
public class UserServiceImpl implements UserService
{
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);//日志打印

    @Autowired
    private UserRepo userRepo;

    @Transactional
    @Override
    public User SaveorupdateUser(User user) {
        return userRepo.save(user);
    }

    @Transactional
    @Override
    public void RemoveUser(Integer userId)
    {
       userRepo.deleteById(userId);
    }

    @Override
    public User GetbyuserId(Integer userId) {
        return userRepo.getOne(userId);
    }



    @Override
    public Page<User> listByuserNameLike(String username, Pageable pageable) {
      username="%"+username+"%";
      Page<User> users=userRepo.findByUsernameLike(username,pageable);
      return users;
    }

    @Override
    public ExecuteResult<User> login(String username, String password)
    {

        ExecuteResult<User> result=new ExecuteResult<>();
        try {
            User user = userRepo.findByUsernameAndPassword(username,password);
           // logger.info("user {}",user);
            result.setExecuted(true);
            if(user!=null)
            {
                result.setSuccess(true);
                result.setCode( "0000");
                result.setData(user);
            }
            else{
                result.setSuccess(false);
                result.setCode( "1000");
                result.setMessage("用户名或密码不正确");
            }

            logger.info("result {}",result);
        }
        catch (Exception e)
        {
            result.setExecuted(false);
            result.setCode("9999");
            result.setMessage("系统异常");
            logger.warn("登录出错",e);
        }

        return result;
    }

    @Override
    public ExecuteResult<Boolean> register(String username, String password)
    {
        ExecuteResult<Boolean> result=new ExecuteResult<>();

        try
        {
            User user=userRepo.findByUsername(username);//查找出用户名

            result.setExecuted(true);//执行成功
            if(user==null)
            {
                user=new User();
                user.setUsername(username);
                user.setPassword(password);
                userRepo.save(user);//插入数据
                result.setSuccess(true);
                result.setCode( "0000");
                result.setMessage("注册成功");
            }

            else
            {
                result.setSuccess(false);
                result.setCode("1000");
                result.setMessage("注册失败");
            }
        }
        catch (Exception e)
        {
            result.setExecuted(false);
            result.setMessage("系统异常");
            result.setCode("9999");
            logger.warn("登录出错",e);
        }
        return result;
     }
    }
