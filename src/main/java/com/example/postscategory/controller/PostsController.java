package com.example.postscategory.controller;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import com.example.postscategory.form.CategoryNameId;
import com.example.postscategory.form.PaginationInput;
import com.example.postscategory.form.PostsInput;
import com.example.postscategory.form.PostsOutput;
import com.example.postscategory.model.Posts;

import com.example.postscategory.service.ICategoryService;
import com.example.postscategory.service.ICategoryPostsService;
import com.example.postscategory.service.IPostsService;

@Controller
public class PostsController {
	private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
	@Autowired
	private IPostsService postsService;
	@Autowired
	private ICategoryService categoryservice;
	@Autowired
	private ICategoryPostsService CPService;

	// Trả về trang chủ bài viết
	@GetMapping("/posts")
	public String index( Model model,
						 @RequestParam(name = "searchValues", required = false, defaultValue = "") String searchValues,
						 @RequestParam(name = "page", required = false, defaultValue = "0") int page,
						 @RequestParam(name = "category_id", required = false, defaultValue = "0") int category_id,
						 RedirectAttributes redirect, HttpServletRequest rq) {

		HttpSession session = rq.getSession();// tạo session
		List<Posts> listPosts = null;// tạo list post trả về view
		int rowCount;
		int index;
		String role = null;
		rowCount = postsService.count(category_id, searchValues); // số lượng posts
		//Tạo input search
		PaginationInput pInput = new PaginationInput((page <= 0) ? 1 : page, 10, rowCount, searchValues, category_id);
																														
																														
		// xử lý phân trang hiển thị 6 trang
		List<Integer> listPage = new ArrayList<Integer>();
		index = pInput.getPage();
		if (page <= pInput.getPageCount()) {// if page nhập vào <= pageCount
			if (page > 3) {
				if (index > 4 && pInput.getPageCount() > 2) {
					listPage.add(1);
					listPage.add(-1);
				}
				for (int i = index - 3; i <= index && i <= pInput.getPageCount(); i++)
					listPage.add(i);

				for (int i = index + 1; i <= index + 2 && i <= pInput.getPageCount(); i++)
					listPage.add(i);

				if (index + 2 < pInput.getPageCount())
					listPage.add(-1);
			} else {
				for (int i = 1; i <= 6 && i <= pInput.getPageCount(); i++) {
					listPage.add(i);
				}
				if (pInput.getPageCount() > 6)
					listPage.add(-1);
			}

		} else {// xuất ra lỗi
			model.addAttribute("errorPage", "Không tìm thấy kết quả nào!");
		}
		// lấy dữ liêu theo page và pageSize
		listPosts = postsService.listPosts(pInput.getCategory_id(), pInput.getPage(), pInput.getPageSize(),
				pInput.getSearchValues());

		// kiểm tra quyền trả về view hiển hiện chức năng theo role
		if (session.getAttribute("role") != null) {
			role = (String) session.getAttribute("role");
		}

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
		postsInput.setId(0);

		model.addAttribute("postsInput", postsInput);
		model.addAttribute("title", "Thêm mới bài viết");
		model.addAttribute("listCategory", categoryservice.findAll());
		model.addAttribute("user", rq.getSession().getAttribute("user"));
		return "posts/Form";
	}

	// edit posts
	@GetMapping("/posts/{id}/edit")
	public String edit( @PathVariable int id, Model model, RedirectAttributes redirect, HttpServletRequest rq) {
		// bài viêt đó phải tồn tại thì mới cho vô sửa
		if (postsService.getPostdByID(id) != null) {

			PostsInput postsInput = new PostsInput();
			Posts posts = postsService.getPostdByID(id);

			// tạo postsInput trả về form
			postsInput.setId(posts.getId());
			postsInput.setTitle(posts.getTitle());
			postsInput.setContent(posts.getContent());
			postsInput.setCategoryIDs(CPService.getListCategoryID(id));

			// tempPost.setImage((MultipartFile)Posts.getImage());
			//List<Integer> listCId = CPService.getListCategoryID(id);// get list categoryID theo posts_id
			model.addAttribute("title", "Sửa bài viết");
			model.addAttribute("image", posts.getImage());
			model.addAttribute("postsInput", postsInput);
			model.addAttribute("listCategory", categoryservice.findAll());
			//model.addAttribute("listCategoryId", listCId);
			model.addAttribute("user", rq.getSession().getAttribute("user"));

			return "/posts/Form";
			// bài viết không tồn tại trả về trang index và thông báo lỗi
		} else {
			redirect.addFlashAttribute("message", "không tìn thấy bài viết với id:" + id);
			return "redirect:/posts";
		}
	}

	// lưu lại post khi thay đổi or thêm mới
	@PostMapping("/posts/save")
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
		}
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
			if (rq.getMethod().equals("POST")) {//
				redirect.addFlashAttribute("message", "Xóa thành công bài viết có tiêu đề là:" + postsByID.getTitle());
				CPService.deleteByPostsID(id);
				postsService.delete(id);
				return "redirect:/Posts";

				// ngược lại lấy post đó theo posts_id trả về trang delete
			} else {
				// tạo postsOutput
				PostsOutput temp = new PostsOutput();

				temp.setTitle(postsByID.getTitle());
				temp.setContent(postsByID.getContent());
				temp.setImage(postsByID.getImage());
				List<Integer> listCId = CPService.getListCategoryID(id);// get list categoryID theo posts_id
				// nếu bài viết không có thể loại nào thì gán thể loại bằng null
				if (listCId.isEmpty()) {
					temp.setCategories(null);
					// ngược lại gán list thể loại theo posts
				} else {
					List<CategoryNameId> listNId = new ArrayList<CategoryNameId>();
					for (int items : listCId) {
						listNId.add(new CategoryNameId(categoryservice.getNameByID(items), items));
					}
					temp.setCategories(listNId);
				}
				model.addAttribute("listCPOutput", temp);
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
	public String postsCategoryDetail( @PathVariable("id") int id, Model model, HttpServletRequest rq,
			RedirectAttributes redirect) {

		PostsOutput temp = new PostsOutput();
		Posts postsByID = postsService.getPostdByID(id);// get posts by id
		if (postsByID != null) {

			temp.setTitle(postsByID.getTitle());
			temp.setContent(postsByID.getContent());
			temp.setImage(postsByID.getImage());
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
