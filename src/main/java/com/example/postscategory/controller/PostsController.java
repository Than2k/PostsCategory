package com.example.postscategory.controller;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.postscategory.common.Pagination;
import com.example.postscategory.form.CategoryNameId;
import com.example.postscategory.form.PostsInput;
import com.example.postscategory.form.PostsOutput;
import com.example.postscategory.model.Posts;

import com.example.postscategory.service.ICategoryService;
import com.example.postscategory.service.ICategoryPostsService;
import com.example.postscategory.service.IPostsService;

@Controller
public class PostsController {
	
	@Autowired
	private IPostsService postsService;
	@Autowired
	private ICategoryService categoryservice;
	@Autowired
	private ICategoryPostsService CPService;

	// Trả về trang chủ bài viết
	@GetMapping("/posts")
<<<<<<< HEAD
	public String index( Model model,
						 @RequestParam(name = "searchValues", required = false, defaultValue = "") String searchValues,
						 @RequestParam(name = "page", required = false, defaultValue = "0") int pageInput,
						 @RequestParam(name = "category_id", required = false, defaultValue = "0") int category_id,
						 RedirectAttributes redirect, HttpServletRequest rq) {
=======
	public String index( 	Model model,
			 	@RequestParam(name = "searchValues", required = false, defaultValue = "") String searchValues,
			 	@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			 	@RequestParam(name = "category_id", required = false, defaultValue = "0") int category_id,
			 	RedirectAttributes redirect, HttpServletRequest rq) {
>>>>>>> b0ecc6f3ada62ee3c77ac4533b7e3e8205054d62

		HttpSession session = rq.getSession();// tạo session
		List<Posts> listPosts = null;// tạo list post trả về view
		int rowCount;
		String role = null;
		rowCount = postsService.count(category_id, searchValues); // số lượng posts
		//Tạo input search
		Pagination pInput = new Pagination((pageInput <= 0) ? 1 : pageInput, 10, rowCount, searchValues, category_id);																																																								
		// xử lý phân trang hiển thị 6 trang
		List<Integer> listPage = pInput.helper(pageInput);
		if(listPage.size() == 0) {
			model.addAttribute("errorPage", "Không tìm thấy kết quả nào!");
		}
		// lấy dữ liêu theo page và pageSize
		listPosts = postsService.listPosts( pInput.getCategory_id(), pInput.getPage(), pInput.getPageSize(),
											pInput.getSearchValues());
		// kiểm tra quyền trả về view hiển hiện chức năng theo role
		if (session.getAttribute("role") != null) {
			role = (String) session.getAttribute("role");
		}
		//đẩy dữ liệu qua view
		model.addAttribute("paginationIp", pInput);
		model.addAttribute("listCategory", categoryservice.findAll());
		model.addAttribute("listPosts", listPosts);// trả về list post theo trang
		model.addAttribute("listPage", listPage);// trả về list page
		model.addAttribute("user", rq.getSession().getAttribute("user"));
		model.addAttribute("role", role);
		return "posts/Index";
	}

	// create posts
	@GetMapping("/posts/create")
	public String create(Model model, HttpServletRequest rq) {
		
		PostsInput postsInput = new PostsInput();
		model.addAttribute("postsInput", postsInput); 
		model.addAttribute("title", "Thêm mới bài viết");
		model.addAttribute("listCategory", categoryservice.findAll());
		model.addAttribute("user", rq.getSession().getAttribute("user"));
		return "posts/Create";
	}

	// edit posts
	@GetMapping("/posts/{id}/edit")
	public String edit( @PathVariable int id, Model model, RedirectAttributes redirect, HttpServletRequest rq) {
		// bài viêt đó phải tồn tại thì mới cho vô sửa
		if (postsService.getPostdByID(id) != null) {
			PostsInput postsInput = new PostsInput();
			Posts posts = postsService.getPostdByID(id);
			
			// tạo postsInput trả về form
			postsInput.setId(posts.getId()); postsInput.setTitle(posts.getTitle());
			postsInput.setContent(posts.getContent()); postsInput.setCategoryIDs(CPService.getListCategoryID(id));

<<<<<<< HEAD
=======
			// tempPost.setImage((MultipartFile)Posts.getImage());

>>>>>>> b0ecc6f3ada62ee3c77ac4533b7e3e8205054d62
			model.addAttribute("title", "Sửa bài viết");
			model.addAttribute("image", posts.getImage());
			model.addAttribute("postsInput", postsInput);
			model.addAttribute("listCategory", categoryservice.findAll());
			model.addAttribute("user", rq.getSession().getAttribute("user"));

			return "/posts/Update";
			// bài viết không tồn tại trả về trang index và thông báo lỗi
		} else {
			redirect.addFlashAttribute("message","Không tìm thấy bài viết có id là:"+id);
			return "redirect:/posts";
		}
	}

	//lưu mới bài viết
	@PostMapping("/posts/save")
<<<<<<< HEAD
	public String create(@Valid PostsInput postsInput, BindingResult result, RedirectAttributes redirect) throws Exception{
		if (result.hasErrors()) {
			return "posts/Create";
=======
	public String save( @Valid PostsInput postsIp, BindingResult result, RedirectAttributes redirect) {
		try {

			int postsId = postsIp.getId();
			String image = null;
			// if ảnh được chọn mới xử lý thêm vào server
			if (!postsIp.getImage().isEmpty()) {

				Path staticPath = Paths.get("src/main/resources/static/images");// đường dẫn lưu ảnh
				Path imagePath = Paths.get("");// đường dẫn
				// kiểm tra xem ảnh đó tồn tại không
				if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
																							

					Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
				}
				Path file = CURRENT_FOLDER.resolve(staticPath).resolve(imagePath)
						.resolve(postsIp.getImage().getOriginalFilename());
				image = imagePath.resolve(postsIp.getImage().getOriginalFilename()).toString();// lấy link tên file ảnh
				try (OutputStream os = Files.newOutputStream(file)) {
					os.write(postsIp.getImage().getBytes());
				}
			}
			// kiểm tra xem bài viết có phải thêm mới không
			if (postsId == 0) {// thêm mới
				if (result.hasErrors()) {
					return "posts/Form";
				}
				int postsID = 0;
				Posts posts = new Posts();// tạo mới post
				posts.setTitle(postsIp.getTitle());
				posts.setContent(postsIp.getContent());
				Date date = new Date();
				posts.setCreatedAt(date);
				if (image != null)
					posts.setImage(image);
				postsService.save(posts);// thêm bài viết csdl
				postsID = posts.getId();

				// thêm thể loại vô csdl
				for (int items : postsIp.getCategoryIDs()) {
					CPService.Add(items, postsID);
				}
				// trả về thông báo
				redirect.addFlashAttribute("message", "Thêm thành công bài viết có tiêu đề là:" + posts.getTitle());
				// ngược lại thì edit bài viết
			} else { // edit

				Posts posts = postsService.getPostdByID(postsId);
				posts.setTitle(postsIp.getTitle());
				posts.setContent(postsIp.getContent());
				Date date = new Date();
				posts.setUpdatedAt(date);
				// nếu edit mà thay đổi anh thì mới set lại ảnh
				if (image != null)
					posts.setImage(image);
				postsService.save(posts);

				// xóa category_posts
				CPService.deleteByPostsID(postsId);
				for (int items : postsIp.getCategoryIDs()) {
					CPService.Add(items, postsId);
				}
				redirect.addFlashAttribute("message", "Cập nhật thành công bài viết có tiêu đề là:" + posts.getTitle());
			}
			return "redirect:/posts";
		} catch (Exception e) {
			System.out.println("Lỗi:" + e.getMessage());
			return "Lỗi:" + e.getMessage();
>>>>>>> b0ecc6f3ada62ee3c77ac4533b7e3e8205054d62
		}
		int postsID = 0; Date date = new Date();
		String image = postsService.upLoadImage(postsInput.getImage());
		Posts posts = new Posts();// tạo mới post
		posts.setTitle(postsInput.getTitle()); posts.setContent(postsInput.getContent());		
		posts.setCreatedAt(date);posts.setImage(image);
		postsService.save(posts);// thêm bài viết csdl
		postsID = posts.getId();

		// thêm thể loại vô csdl
		for (int items : postsInput.getCategoryIDs()) {
			CPService.Add(items, postsID);
		}
		// trả về thông báo
		redirect.addFlashAttribute("message", "Thêm thành công bài viết có tiêu đề là:" + posts.getTitle());
		return "redirect:/posts";
	}
	// update bài viết
	@PostMapping("/posts/update")
	public String save( @Valid PostsInput postsInput, BindingResult result, RedirectAttributes redirect) throws Exception{
		if (result.hasErrors()) {
			return "posts/Update";
		}
		int postsId = postsInput.getId(); Date date = new Date();
		Posts posts = postsService.getPostdByID(postsId);
		posts.setTitle(postsInput.getTitle()); posts.setContent(postsInput.getContent());
		posts.setUpdatedAt(date);
		String image = postsService.upLoadImage(postsInput.getImage());
		// nếu edit mà thay đổi anh thì mới set lại ảnh
		if (image != null)
			posts.setImage(image);
		postsService.save(posts);

		// xóa category_posts
		CPService.deleteByPostsID(postsId);
		for (int items : postsInput.getCategoryIDs()) {
			CPService.Add(items, postsId);
		}
		redirect.addFlashAttribute("message", "Cập nhật thành công bài viết có tiêu đề là:" + posts.getTitle());
		return "redirect:/posts";
	}

	@RequestMapping("/posts/{id}/delete")
	public String delete( @PathVariable int id, RedirectAttributes redirect, Model model, HttpServletRequest rq) {
		/*
		 * kiểm tra bài viết này tồn tại không nếu tồn tài thì xóa ngược lại trả về
		 * trang index và thông báo lỗi
		 */
		Posts postsByID = postsService.getPostdByID(id);
		if (postsByID != null) {
			/*
			 * kiểm tra xem method gửi view to controller là get hay post if là post thì xóa
			 * bài viết đó và trả về trang index
			 */
			if (rq.getMethod().equals("POST")) {
				CPService.deleteByPostsID(id); postsService.delete(id);
				redirect.addFlashAttribute("message", "Xóa thành công bài viết có tiêu đề là:" + postsByID.getTitle());
				return "redirect:/Posts";
			// ngược lại lấy post đó theo posts_id trả về trang delete
			} else {
				// tạo postsOutput
				PostsOutput postsOutput = new PostsOutput(); postsOutput.setTitle(postsByID.getTitle());
				postsOutput.setContent(postsByID.getContent()); postsOutput.setImage(postsByID.getImage());
				List<Integer> listCId = CPService.getListCategoryID(id);// get list categoryID theo posts_id
				// nếu bài viết không có thể loại nào thì gán thể loại bằng null
				if (listCId.isEmpty()) {
					postsOutput.setCategories(null);
					// ngược lại gán list thể loại theo posts
				} else {
					List<CategoryNameId> listNId = new ArrayList<CategoryNameId>();
					for (int items : listCId) 
						listNId.add(new CategoryNameId(categoryservice.getNameByID(items), items));
					postsOutput.setCategories(listNId);
				}
				model.addAttribute("postsOutput", postsOutput);
				model.addAttribute("posts_id", id);
				model.addAttribute("user", rq.getSession().getAttribute("user"));
				return "/posts/Delete";
			}
		} else {// nếu posts_id ko tồn tại thì trả về trang index và thông báo lỗi
			redirect.addFlashAttribute("message", "Không tìn tháy bài viết với id:" + id);
			return "redirect:/posts";
		}
		
	}

	// hiển thị chi tiết bài viết
	@GetMapping("/posts/{id}/detail")
	public String postsCategoryDetail(  @PathVariable("id") int id, Model model, HttpServletRequest rq,
										RedirectAttributes redirect) {

		Posts postsByID = postsService.getPostdByID(id);// get posts by id
		if (postsByID != null) {
			PostsOutput temp = new PostsOutput(); temp.setTitle(postsByID.getTitle());
			temp.setContent(postsByID.getContent()); temp.setImage(postsByID.getImage());
			List<Integer> listCId = CPService.getListCategoryID(id);// get list categoryID theo posts_id
			if (listCId.isEmpty()) {
				temp.setCategories(null);
			} else {
				List<CategoryNameId> listNId = new ArrayList<CategoryNameId>();
				for (int items : listCId) {
					listNId.add(new CategoryNameId(categoryservice.getNameByID(items), items));
				}
				temp.setCategories(listNId);
			}
			model.addAttribute("user", rq.getSession().getAttribute("user"));
			model.addAttribute("postsDetail", temp);
			return "posts/postsDetail";

		} else {
			redirect.addFlashAttribute("message", "Không tìn tháy bài viết với id:" + id);
			return "redirect:/posts";
		}

	}
}
