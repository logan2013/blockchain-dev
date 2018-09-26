package org.pl.blockchain.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.pl.blockchain.web.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping("/hello")
    public String hello(Model model){
	   model.addAttribute("username",messageSource.getMessage("username", null, null));
       return"/user/hello";
    }
	
    @RequestMapping("/{id}") 
    public String  getUser(@PathVariable Integer id,Model model) {
        
        model.addAttribute("user",new User(id,"张三",20,"中国广州"));
        return "/user/detail";
    }
    
    @RequestMapping("/list")
    public String  listUser(Model model) {
        List<User> userList = new ArrayList<User>();
        for (int i = 0; i <10; i++) {
            userList.add(new User(i,"张三"+i,20+i,"中国广州"));
        }
        
        model.addAttribute("users", userList);
        return "/user/list";
    }
}
