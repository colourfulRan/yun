package cqjtu.ds.yun.web;

import cqjtu.ds.yun.result.ExecuteResult;
import cqjtu.ds.yun.service.UserService;

import cqjtu.ds.yun.service.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes(value={"uid","uname","uphoto"})
public class LoginController
{
    private Logger logger = LoggerFactory.getLogger(LoginController.class);
   // User user=new User();

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(String username, String password, Model model)//
    {
        logger.info("username:{}",username);
        logger.info("password:{}",password);
        //调用远程服务
        ExecuteResult<User> loginResult=userService.login(username,password);
        //User u1=
        if(loginResult.isExecuted()&&loginResult.isSuccess())
        {
            model.addAttribute("uid",loginResult.getData().getUserId());
            model.addAttribute("uname",loginResult.getData().getUsername());
            model.addAttribute("uphoto",loginResult.getData().getPhoto());
            return "main" ;
        }

        else
        {
            return "index";

        }
    }

}
