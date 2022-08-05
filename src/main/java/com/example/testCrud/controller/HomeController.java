package com.example.testCrud.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	//Trang chủ
	@GetMapping("/home")
	public String home(Model model,HttpServletRequest rq) {
		model.addAttribute("user",rq.getSession().getAttribute("user"));
		return "/Home/index";
	}
}
