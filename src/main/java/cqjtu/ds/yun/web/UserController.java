package cqjtu.ds.yun.web;


import cqjtu.ds.yun.service.DetailService;
import cqjtu.ds.yun.service.UserService;
import cqjtu.ds.yun.service.domain.Detail;
import cqjtu.ds.yun.dal.AdminRepo;
import cqjtu.ds.yun.dal.UserRepo;
import cqjtu.ds.yun.service.domain.Admin;
import cqjtu.ds.yun.service.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collection;

@Controller
public class UserController
{
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired

    private UserService userService;

    @Autowired
    private  DetailService detailService;
    //个人中心
    @RequestMapping("personal")
    public String personal()
    {
        System.out.println("个人中心");
        return "personal";
    }
   //基本资料
    @RequestMapping("basic_info")
    public String basic_info()
    {
        System.out.println("基本资料");
        return "basic_info";

    }
    //详细资料
    @RequestMapping("detail_info")
    public String detail_info()
    {
        return "detail_info";
    }
    //头像设置
    @RequestMapping("photo_set")
    public  String photo_set()
    {
        return "photo_set";
    }



    @RequestMapping("basic_update")
    @ResponseBody
    public Map<String, Object>basic_update(HttpSession session,String sex,String bloodtype,String liveplace,String birthplace,String birthday) throws ParseException {
        logger.info("基本资料更新");
        System.out.println(sex+" "+bloodtype+" "+liveplace+" "+birthplace+" "+birthday);

        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();

        SimpleDateFormat  format = new    SimpleDateFormat("yyyy-MM-dd");//定义日期格式
        Date da = null;//将字符串转换为日期
        try {
            da = format.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        User user=userService.findbyid(userid);

        user.setBloodType(bloodtype);
        user.setBrithday(da);
        user.setBrithPlace(birthplace);
        user.setLivePlace(liveplace);
        user.setSex(sex);

        userService.SaveUser(user);
        map.put("state",true);
        return map;
    }




    @RequestMapping("basic_search")
    @ResponseBody
    public Map<String, Object> basic_search(HttpSession session)
    {
        System.out.println("初始化基本资料");
        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        User user=userService.findbyid(userid);

        map.put("data",user);

        map.put("state",true);

        //logger.info("map:{}",map);
        return map;
    }



    @RequestMapping("detail_update")
    @ResponseBody
    public Map<String, Object>detail_update(HttpSession session,String shape,String marriage,String smokehabit,String drinkhabit,String sleephabit,String educat,String profession,String telephone)
    {
       System.out.println(shape+" "+marriage+" "+smokehabit);
        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        Detail detail= detailService.findbyid1(userid);
        if(detail==null)
        {
            detailService.InsertDetail(shape,marriage, smokehabit, drinkhabit, sleephabit, educat,profession, telephone,userid);
        }
        else
            {
            detailService.UpdateDetail(shape, marriage, smokehabit, drinkhabit, sleephabit, educat,profession, telephone,userid);
        }
        map.put("state",true);
        logger.info("map详细:{}",map);
        return map;
    }



    @RequestMapping("detail_search")
    @ResponseBody
    public Map<String, Object> detail_search(HttpSession session)
    {
        logger.info("详细资料初始化");
        Integer userid = (Integer) session.getAttribute("uid");//通过session获取userid
        Map<String, Object> map = null;
        map = new LinkedHashMap<String, Object>();
        Detail detail=detailService.findbyid1(userid);
        map.put("data",detail);
        map.put("state",true);
        logger.info("map:{}",map);
        return map;
    }

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
