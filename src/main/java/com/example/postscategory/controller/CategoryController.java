package com.example.postscategory.controller;

import java.util.Date;

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

import com.example.postscategory.form.CategoryInput;
import com.example.postscategory.model.Category;
import com.example.postscategory.service.ICategoryService;

@Controller
public class CategoryController {

	@Autowired
	private ICategoryService categoryService;

	// trang index
	@GetMapping("/category")
	public String index(Model model, HttpServletRequest rq) throws Exception {
		model.addAttribute("listcategory", categoryService.findAll());
		model.addAttribute("user", rq.getSession().getAttribute("user"));
		return "category/Index";
	}

	// thêm mới thể loại
	@GetMapping("/category/create")
	public String create(Model model, HttpServletRequest rq) {

		CategoryInput categoryInput = new CategoryInput();
		categoryInput.setId(0);
		model.addAttribute("categoryInput", categoryInput);
		model.addAttribute("title", "Thêm mới thể loại");
		model.addAttribute("user", rq.getSession().getAttribute("user"));
		return "category/Form";
	}

	// Sửa thể loại
	@GetMapping("category/{id}/edit")
	public String edit(@PathVariable int id, Model model, HttpServletRequest rq) {

		CategoryInput categoryInput = new CategoryInput();
		Category category = categoryService.getCategoryById(id);
		// trả dữ liệu về form
		categoryInput.setId(category.getId());
		categoryInput.setName(category.getName());

		model.addAttribute("categoryInput", categoryInput);
		model.addAttribute("title", "Sửa thể loại");
		model.addAttribute("user", rq.getSession().getAttribute("user"));
		return "category/Form";
	}

	// xóa thể loại
	@GetMapping("category/{id}/delete")
	public String delete(@PathVariable int id, RedirectAttributes redirect) {
		categoryService.delete(id);
		redirect.addFlashAttribute("success", "Deleted employee successfully!");
		return "redirect:/category";
	}

	// thêm mới thể loại hoặc sửa thể loại
	@PostMapping("/category/save")
	public String save(@Valid CategoryInput categoryInput, BindingResult result, RedirectAttributes redirect) {
		try {
			int id = categoryInput.getId();
			System.out.println("id:" + id);
			// thêm mới
			if (id == 0) {
				Category category = new Category();
				category.setName(categoryInput.getName());
				category.setCreatedAt(new Date());
				categoryService.save(category);
				return "redirect:/category";
				// sửa
			} else {
				Category category = new Category();
				category.setId(categoryInput.getId());
				category.setName(categoryInput.getName());
				category.setUpdatedAt(new Date());
				categoryService.save(category);
				return "redirect:/category";
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Lỗi:" + e.getMessage());
			return "Lỗi:" + e.getMessage();
		}
	}
}
