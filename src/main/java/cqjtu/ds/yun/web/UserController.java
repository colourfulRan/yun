package cqjtu.ds.yun.web;

import cqjtu.ds.yun.dal.UserRepo;
import cqjtu.ds.yun.service.domain.User;
import javafx.print.Collation;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
