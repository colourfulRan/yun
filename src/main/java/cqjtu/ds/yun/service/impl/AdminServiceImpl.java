package cqjtu.ds.yun.service.impl;

import cqjtu.ds.yun.dal.AdminRepo;
import cqjtu.ds.yun.dal.UserRepo;
import cqjtu.ds.yun.result.ExecuteResult;
import cqjtu.ds.yun.service.AdminService;
import cqjtu.ds.yun.service.domain.Admin;
import cqjtu.ds.yun.service.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service("AdminService")
public class AdminServiceImpl implements AdminService {
    private static  final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
//    private static AtomicLong counter = new AtomicLong();
//    private final ConcurrentMap<Long, User> userMap = new ConcurrentHashMap<>();

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public ExecuteResult<AdminInfo> login(String adminname, String password) {
        ExecuteResult<AdminInfo> result = new ExecuteResult<>();
        try {
            Admin admin = adminRepo.findByAdminnameAndPassword(adminname, password);
            result.setExecuted(true);
            result.setSuccess(admin != null);
            result.setCode(admin != null ? "0000" : "1000");
            result.setMessage(admin != null ? "登录成功" : "用户名或密码不正确");

        }catch(Exception e) {
            result.setExecuted(false);
            result.setCode("9999");
            result.setMessage("系统异常，请联系应用负责人");
            logger.warn("登录出错",e);
        }
        return result;
    }
    @Transactional
    @Override
    public User SaveorupdateUser(User user) {
        return userRepo.save(user);
    }
    @Transactional
    @Override
    public void RemoveUser(Integer userId) {
        userRepo.deleteById(userId);
    }

    @Override
    public User GetbyuserId(Integer userId) {
        return userRepo.getOne(userId);
    }

    @Override
    public Page<User> listByuserNameLike(String username, Pageable pageable) {
        username="%"+username+"%";
        Page<User> user=userRepo.findByUsernameLike(username,pageable);
        return user;
    }

    @Override
    public List<User> findAll() {
        Page<User> users= (Page<User>) userRepo.findAll();
        return (List<User>) users;

    }





}
