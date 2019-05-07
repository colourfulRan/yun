package cqjtu.ds.yun.service;

import cqjtu.ds.yun.result.ExecuteResult;
import cqjtu.ds.yun.service.domain.Admin;
import cqjtu.ds.yun.service.domain.User;
import cqjtu.ds.yun.service.impl.AdminInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AdminService {
    /**
     * 登录操作
     * @param adminname
     * @param password
     * @return 执行结果及用户信息
     */
    ExecuteResult <AdminInfo>login(String adminname, String password);
    /**
     * 新增编辑保存用户
     * @param user
     * @return
     */
    User SaveorupdateUser(User user);

    /**
     * 删除用户
     */
    void RemoveUser(Integer userId);

    /**
     *根据id获取用户
     */
    User GetbyuserId(Integer userId);

    /**
     *根据用户名分页模糊查询
     */
    Page<User> listByuserNameLike(String userName, Pageable pageable);

    List<User> findAll();
}
