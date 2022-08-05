package com.example.testCrud.controller;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.testCrud.model.Category;
import com.example.testCrud.service.ICategoryService;


@Controller
public class CategoryController {

	@Autowired 
	private ICategoryService categoryService;
	
	//trang index 
	@GetMapping("/category")
	public String index(Model model, HttpServletRequest rq) throws Exception{
		model.addAttribute("listcategory",categoryService.findAll());
		model.addAttribute("user",rq.getSession().getAttribute("user"));
		return "category/Index";
	}
	
	//thêm mới thể loại
	@GetMapping("/category/create")
	public String create(Model model,HttpServletRequest rq) {
		model.addAttribute("category",new Category());
		model.addAttribute("title","Thêm mới thể loại");
		model.addAttribute("user",rq.getSession().getAttribute("user"));
		return "category/Form";	
	}
	
	//Sửa thể loại
	@GetMapping("category/{id}/edit")
	public String edit(@PathVariable int id, Model model, HttpServletRequest rq) {
		model.addAttribute("category", categoryService.findOne(id));
		model.addAttribute("title","Sửa thể loại");
		model.addAttribute("user",rq.getSession().getAttribute("user"));
		return "category/Form";
	}
	
	//xóa thể loại
	@GetMapping("category/{id}/delete")
	public String delete(@PathVariable int id, RedirectAttributes redirect) {
		categoryService.delete(id);
		redirect.addFlashAttribute("success", "Deleted employee successfully!");
		return "redirect:/Category";
	}
	
	//thêm mới thể loại hoặc sửa thể loại
	@PostMapping("/category/save")
	public String save(@Valid Category category, BindingResult result, RedirectAttributes redirect) {
		try {
			if (result.hasErrors()) {
				System.out.println("lỗi:"+result.getAllErrors());
				return "category/Form";
			}
			categoryService.save(category);;
			redirect.addFlashAttribute("success", "Saved category successfully!");
			return "redirect:/Category";
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Lỗi:"+e.getMessage());
			return "Lỗi:"+e.getMessage();
		}
	}
}
