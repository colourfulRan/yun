package cqjtu.ds.yun.web;

import cqjtu.ds.yun.dal.AdminRepo;
import cqjtu.ds.yun.dal.UserRepo;
import cqjtu.ds.yun.service.domain.Admin;
import cqjtu.ds.yun.service.domain.User;
import javafx.print.Collation;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collection;

@Controller
public class UserController {
    @Autowired
    UserRepo userRepo;

    @GetMapping("/users")
    public  String list(Model model){
        Collection<User> users = userRepo.findAll();
        model.addAttribute("users",users);
        return "tables";
    }

    @GetMapping("/user")
    public  String toAddPage(){

        return"forms";
    }

//用户添加
    //springMVC自动将请求参数和入参对象进行一一绑定，要求请求参数的名字和javabean入参对象属性名相同
    @PostMapping("/user")
    public String addUser(User user){
        //来到员工列表页面
        System.out.println("保存的用户信息"+user);
        userRepo.save(user);
        return "redirect:/users";
    }

    //来到修改页面，查出当前用户，在页面回显
    @GetMapping("/user/{userId}")
    public String toEditPage(@PathVariable("userId")  Integer userId,Model model){
        User user =userRepo.getOne(userId);
        model.addAttribute("user",user);
        //回到修改页面 add是修改添加二合一的页面
        return  "forms";
    }

    //用户修改
    @PutMapping("/user")
    public String updateUser(User user){
        System.out.println("修改的用户数据"+user);
        userRepo.save(user);
        return "redirect:/users";
    }

    //用户删除
    @DeleteMapping("/user/{userId}")
    public String deleteUser(@PathVariable("userId")Integer userId){
        userRepo.deleteById(userId);
        return"redirect:/users";
    }



}
