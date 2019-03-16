package cqjtu.ds.yun.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("home")
    public String home(){
        return "home";
    }


    @RequestMapping("my")
    public String my(){
        return "my";
    }
}
