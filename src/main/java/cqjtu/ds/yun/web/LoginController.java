package cqjtu.ds.yun.web;

import cqjtu.ds.yun.result.ExecuteResult;
import cqjtu.ds.yun.service.UserService;
import javax.servlet.http.HttpServletRequest;
import cqjtu.ds.yun.service.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;

import java.util.Map;
/**
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
            //user.setUserId(loginResult.getData().getUserId());
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
*/
@Controller
class LoginController {

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 去注册页面
     *
     * @return
     */
    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    /**
     * 执行注册 成功后登录页面 否则调回注册页面
     *
     * @param request
     * @param user
     * @return
     */
    @RequestMapping("/doregister")
    public String register(HttpServletRequest request, User user) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        if (password.equals(password2)) {
            if (registerUser(username) == true) {
                User user1 = new User();
                user1.setUsername(username);
                user1.setPassword(password);
                userService.save(user1);
                return "index";
            } else {
                return "register";
            }
        } else {
            return "register";
        }
    }

    public Boolean registerUser(String username) {
        Boolean a = true;
        if (userService.findByName(username)== null) {
            return a;
        } else {
            return false;
        }
    }

    /**
     * 去登录页面
     *
     * @return

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 执行登录
     *
     * @param request
     * @return
     */
    @RequestMapping("/dologin")
    public String login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = userService.FindNameAndPsw(username, password);
        String str = "";
        if (user != null) {
            //登陆成功
            str = "main";
        } else {
            //登陆失败

            str = "index";

        }
        return str;
    }

}

