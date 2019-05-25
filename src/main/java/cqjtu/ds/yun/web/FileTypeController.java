package cqjtu.ds.yun.web;

import cqjtu.ds.yun.dal.FileTypeRepo;
import cqjtu.ds.yun.service.domain.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
public class FileTypeController {
    @Autowired
    FileTypeRepo fileTypeRepo;

    @GetMapping("/types")
    public  String type(Model model){
        Collection<Type> types= fileTypeRepo.findAll();
        model.addAttribute("types",types);
        return"typetable";
    }

    @GetMapping("/type")
    public  String toAddTypePage(){

        return"typeforms";
    }

    //类别添加
    //springMVC自动将请求参数和入参对象进行一一绑定，要求请求参数的名字和javabean入参对象属性名相同
    @PostMapping("/type")
    public String addType(Type type){
        //来到类别列表页面
        fileTypeRepo.save(type);
        return "redirect:/types";
    }

    //来到修改页面，查出当前类别，在页面回显
    @GetMapping("/type/{typeId}")
    public String toEditTypePage(@PathVariable("typeId")  Integer typeId, Model model){
        Type type =fileTypeRepo.getOne(typeId);
        model.addAttribute("type",type);
        //回到修改页面 add是修改添加二合一的页面
        return  "typeforms";
    }

    //文件类别修改
    @PutMapping("/type")
    public String updateType(Type type){
        System.out.println("修改的文件类别数据"+type);
        fileTypeRepo.save(type);
        return "redirect:/types";
    }

    //类别删除
    @DeleteMapping("/type/{typeId}")
    public String deleteType(@PathVariable("typeId")int typeId){
        fileTypeRepo.deleteById(typeId);
        return"redirect:/types";
    }



}
