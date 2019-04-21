package cqjtu.ds.yun.service;

import cqjtu.ds.yun.result.ExecuteResult;
import cqjtu.ds.yun.service.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService
{
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

    /**
     *
     * @param username
     * @param password
     * @return
     * 登录
     */
    ExecuteResult<User> login(String username, String password);
    ExecuteResult<Boolean>register(String username,String password);

}
