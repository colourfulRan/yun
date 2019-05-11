package cqjtu.ds.yun.web;

import cqjtu.ds.yun.result.ExecuteResult;
import cqjtu.ds.yun.service.domain.Admin;
import cqjtu.ds.yun.service.impl.AdminInfo;
import cqjtu.ds.yun.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import javax.management.ValueExp;
import javax.servlet.http.HttpSession;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.Map;


@Controller
public class AdLoginController {
    private Logger logger = LoggerFactory.getLogger(AdLoginController.class);
    @Autowired
    private AdminService adminService;


    @RequestMapping ("/adminlogin")
    public String login(@RequestParam(value="adminname",required = false)String adminname,
                        @RequestParam(value="password",required = false)String password,
                        Map<String,Object> map, HttpSession session) {


        logger.info("adminname:{}", adminname);
        logger.info("password:{}", password);
        //调用远程服务
        ExecuteResult <AdminInfo> loginResult = adminService.login(adminname,password);
        if(StringUtils.isEmpty((adminname))||StringUtils.isEmpty((password))){
            map.put("msg","请输入用户名和密码 ");
            return "login";
        }
       else if(loginResult.isExecuted()&&loginResult.isSuccess()){
            //map.put("msg","登录成功 ");
            session.setAttribute("loginAdmin",adminname);
            return "redirect:/adindex.html";
            /*重定位*/


        }else {
            map.put("msg","用户名或密码错误");
            return "login";
        }

    }

}
