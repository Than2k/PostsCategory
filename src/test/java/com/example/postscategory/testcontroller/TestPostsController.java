package com.example.postscategory.testcontroller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import com.example.postscategory.common.Pagination;
import com.example.postscategory.common.Roles;
import com.example.postscategory.controller.PostsController;
import com.example.postscategory.form.PostsInput;
import com.example.postscategory.form.PostsOutput;
import com.example.postscategory.model.Category;
import com.example.postscategory.model.Posts;
import com.example.postscategory.model.Role;
import com.example.postscategory.model.User;
import com.example.postscategory.service.ICategoryPostsService;
import com.example.postscategory.service.ICategoryService;
import com.example.postscategory.service.IPostsService;
import com.example.postscategory.service.IRoleService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(PostsController.class)
@WithMockUser
public class TestPostsController {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private IPostsService postsService;
	@MockBean
	private ICategoryService categoryService;
	@MockBean
	private ICategoryPostsService CPService;
	@MockBean
	private IRoleService roleService;

	public List<Category> createListCategoryTest() throws Exception {
		SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd");
		List<Category> listCategory = new ArrayList<Category>();
		listCategory.add(new Category(1, "Tin tức", ff.parse("2022-11-10"), null));
		listCategory.add(new Category(2, "Thời tiết", ff.parse("2022-11-10"), null));
		listCategory.add(new Category(3, "Tin tưc  7h", ff.parse("2022-11-10"), null));
		listCategory.add(new Category(4, "Tin tức 10h", ff.parse("2022-11-10"), null));
		listCategory.add(new Category(5, "thể thao", ff.parse("2022-11-10"), null));
		return listCategory;
	}

	public List<Posts> createListPostsTest() throws Exception {
		SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd");
		List<Posts> listPosts = new ArrayList<Posts>();
		listPosts.add(new Posts(1, "Tin tức", "mọi người vui vẻ", "ip13promax.jpg", ff.parse("2022-11-11"), null));
		listPosts.add(new Posts(2, "Tin tức 7h", "mọi người vui vẻ", "ip13promax.jpg", ff.parse("2022-11-11"), null));
		listPosts.add(new Posts(3, "Tin tức 8h", "mọi người vui vẻ", "ip13promax.jpg", ff.parse("2022-11-11"), null));
		return listPosts;
	}

	User user = new User(2, "Tanthan", "Võ Tấn Thân", "12345", null, "tanthan2000@gmail.com");
	List<Role> listRole = Arrays.asList(new Role(1, "administrator"));

	// Test inddexx case: trả về it nhất 1 bài viết
	@Test
	public void testIndex_listPost_exists() throws Exception {

		// giả lập Service trả về dữ liệu mong muốn
		when(roleService.listRoleByUser(anyString())).thenReturn(listRole);
		when(categoryService.findAll()).thenReturn(createListCategoryTest());
		when(postsService.count(anyInt(), anyString())).thenReturn(3);
		when(postsService.listPosts(anyInt(), anyInt(), anyInt(), anyString())).thenReturn(createListPostsTest());
		//
		MvcResult mvcResult = this.mockMvc
				.perform(
						MockMvcRequestBuilders.get("/posts").sessionAttr("user", user).sessionAttr("role", Roles.ADMIN))
				.andDo(MockMvcResultHandlers.print()).andReturn();

		Pagination paginationResult = (Pagination) mvcResult.getModelAndView().getModel().get("paginationIp");
		// kiểm tra số trang
		assertEquals(1, paginationResult.getPageCount());
		// kiểm tra pageSize
		assertEquals(10, paginationResult.getPageSize());
		// Kiểm tra số lượng post
		assertEquals(3, paginationResult.getRowCount());
		// kiểm tra viewName
		assertEquals("posts/Index", mvcResult.getModelAndView().getViewName());
		// kiểm tra role
		assertEquals(Roles.ADMIN, mvcResult.getModelAndView().getModel().get("role"));
		// kt fullname
		assertEquals("Võ Tấn Thân", ((User) mvcResult.getModelAndView().getModel().get("user")).getFullName());

	}

	// Test inddexx case: không tìm thấy bài viết nào
	@Test
	public void testIndex_listPosts_exitsts() throws Exception {

		// giả lập Service trả về dữ liệu mong muốn
		when(roleService.listRoleByUser(anyString())).thenReturn(listRole);
		when(categoryService.findAll()).thenReturn(createListCategoryTest());
		when(postsService.count(anyInt(), anyString())).thenReturn(0);
		when(postsService.listPosts(anyInt(), anyInt(), anyInt(), anyString())).thenReturn(null);
		//
		MvcResult mvcResult = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/posts").sessionAttr("role", Roles.ADMIN))
				.andDo(MockMvcResultHandlers.print()).andReturn();

		Pagination paginationResult = (Pagination) mvcResult.getModelAndView().getModel().get("paginationIp");
		// kiểm tra số trang
		assertEquals(0, paginationResult.getPageCount());
		// kiểm tra viewName
		assertEquals("posts/Index", mvcResult.getModelAndView().getViewName());
		// kiểm tra role
		assertEquals(Roles.ADMIN, mvcResult.getModelAndView().getModel().get("role"));
		// kt fullname
		assertEquals("Không tìm thấy kết quả nào!", mvcResult.getModelAndView().getModel().get("errorPage"));

	}

	// Test thêm mới 1 bài viết
	@Test
	public void testSave() throws Exception {
		SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd");
		List<Integer> listCId = Arrays.asList(1, 2, 3);
		PostsInput postsInput = new PostsInput(0, "Tin tức tối nay", "Tin tức tối nay nhiều vụ cướp xảy ra", null,
				listCId);
		Posts postResult = new Posts(10, "Tin tức tối nay", "Tin tức tối nay nhiều vụ cướp xảy ra", null,
				ff.parse("2022-11-11"), null);

		// giả lập Service trả về dữ liệu mong muốn
		when(roleService.listRoleByUser(anyString())).thenReturn(listRole);
		when(postsService.upLoadImage(any(MultipartFile.class))).thenReturn(null);
		when(postsService.save(any(Posts.class))).thenReturn(postResult);

		MvcResult mvcResult = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/posts/save").sessionAttr("user", user)
						.sessionAttr("role", Roles.ADMIN).accept(MediaType.APPLICATION_JSON)
						.contentType("application/json").content(objectMapper.writeValueAsString(postsInput)))
				.andDo(MockMvcResultHandlers.print()).andReturn();
		MockHttpServletResponse reponse = mvcResult.getResponse();
		assertEquals("Thêm thành công bài viết có tiêu đề là:Tin tức tối nay", mvcResult.getFlashMap().get("message"));
		// kiểm tra trang được điều hướng tới
		assertEquals("/posts", reponse.getRedirectedUrl());

	}

	// Test thêm mới 1 bài viết case: role user
	@Test
	public void testCreate_role_user() throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(
				MockMvcRequestBuilders.get("/posts/create").sessionAttr("user", user).sessionAttr("role", Roles.USER))
				.andDo(MockMvcResultHandlers.print()).andReturn();
		MockHttpServletResponse reponse = mvcResult.getResponse();

		// kiểm tra trang được điều hướng tới
		assertEquals("/alert", reponse.getRedirectedUrl());

	}

	// Test sửa bài viết case: role user
	@Test
	public void testEdit_role_user() throws Exception {

		MvcResult mvcResult = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/posts/edit").sessionAttr("role", Roles.USER))
				.andDo(MockMvcResultHandlers.print()).andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		// kiểm tra trang được điều hướng
		assertEquals("/alert", response.getRedirectedUrl());

	}

	// Test sửa bài viết case: Bài viết đó không tồn tại
	@Test
	public void testEdit_posts_not_exists() throws Exception {

		// giả lập Service trả về dữ liệu mong muốn
		when(roleService.listRoleByUser(anyString())).thenReturn(listRole);
		// thêm ảnh

		when(postsService.getPostdByID(anyInt())).thenReturn(null);
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/posts/{id}/edit", 10)
				.sessionAttr("user", user).sessionAttr("role", Roles.ADMIN)).andDo(MockMvcResultHandlers.print())
				.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals("Không tìm thấy bài viết có id:10", mvcResult.getFlashMap().get("message"));

		// kiểm tra trang được điều hướng
		assertEquals("/posts", response.getRedirectedUrl());

	}

	// test sửa bài viết case: bài viết đó được tìm thấy
	@Test
	public void testEdit_posts_exists() throws Exception {
		SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd");
		Posts post = new Posts(10, "Tin tức tối nay", "Tin tức tối nay nhiều vụ cướp xảy ra", null,
				ff.parse("2022-11-11"), null);

		// giả lập Service trả về dữ liệu mong muốn
		// trả về list role
		when(roleService.listRoleByUser(anyString())).thenReturn(listRole);
		// tim kiếm post theo id
		when(postsService.getPostdByID(anyInt())).thenReturn(post);// return post or null
		// return categoryName by id
		when(categoryService.getNameByID(anyInt())).thenReturn(createListCategoryTest().get(0).getName())
				.thenReturn(createListCategoryTest().get(1).getName())
				.thenReturn(createListCategoryTest().get(4).getName());
		// trả về list categoryId by postsId
		when(CPService.getListCategoryID(anyInt())).thenReturn(Arrays.asList(1, 2, 5));
		when(categoryService.findAll()).thenReturn((Iterable<Category>) createListCategoryTest());

		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/posts/{id}/edit", 10)
				.sessionAttr("user", user).sessionAttr("role", Roles.EDITOR)).andDo(MockMvcResultHandlers.print())
				.andReturn();

		// kiểm tra
		assertEquals("Tin tức tối nay",
				((PostsInput) mvcResult.getModelAndView().getModel().get("postsInput")).getTitle());
		// viewName trả về
		assertEquals("/posts/Edit", mvcResult.getModelAndView().getViewName());

	}


	// Test lưu bài viết đã được sửa
	@Test
	public void testUpdate() throws Exception {
		SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd");
		List<Integer> listCId = Arrays.asList(1, 2, 3);
		PostsInput postsInput = new PostsInput(10, "Tin tức tối nay", "Tin tức tối nay nhiều vụ cướp xảy ra", null,
				listCId);
		Posts postResult = new Posts(10, "Tin tức tối nay", "Tin tức tối nay nhiều vụ cướp xảy ra", null,
				ff.parse("2022-11-11"), null);
		Posts postResultUpdate = new Posts(10, "Tin tức tối nay", "Tin tức tối nay nhiều vụ cướp xảy ra", null,
				ff.parse("2022-11-11"), ff.parse("2022-8-18"));

		// giả lập Service trả về dữ liệu mong muốn
		when(roleService.listRoleByUser(anyString())).thenReturn(listRole);
		// thêm ảnh
		when(postsService.upLoadImage(any(MultipartFile.class))).thenReturn(null);
		when(postsService.save(any(Posts.class))).thenReturn(postResultUpdate);
		when(postsService.getPostdByID(anyInt())).thenReturn(postResult);
		MvcResult mvcResult = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/posts/update").sessionAttr("role", Roles.ADMIN)
						.accept(MediaType.APPLICATION_JSON).contentType("application/json")
						.content(objectMapper.writeValueAsString(postsInput)))
				.andDo(MockMvcResultHandlers.print()).andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		assertEquals("Cập nhật thành công bài viết có id là:10", mvcResult.getFlashMap().get("message"));
		// kiểm tra trang được điều hướng
		assertEquals("/posts", response.getRedirectedUrl());

	}

	// Test Xóa bài viết case: bài viết đó không tồn tại
	@Test
	public void testDelete_posts_not_exists() throws Exception {
		when(roleService.listRoleByUser(anyString())).thenReturn(listRole);
		// tim kiếm post theo id
		when(postsService.getPostdByID(anyInt())).thenReturn(null);// return post or null
		// trả về list categoryId by postsId

		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/posts/{id}/delete", 10)
				.sessionAttr("user", user).sessionAttr("role", Roles.ADMIN)).andDo(MockMvcResultHandlers.print())
				.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		assertEquals("Không tìm thấy bài viết với id:10", mvcResult.getFlashMap().get("message"));
		// kiểm tra trang được điều hướng tới
		assertEquals("/posts", response.getRedirectedUrl());

	}

	// Test Xóa bài viết case: role User
	@Test
	public void testDelete_role_user() throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/posts/{id}/delete", 10)
				.sessionAttr("user", user).sessionAttr("role", Roles.USER)).andDo(MockMvcResultHandlers.print())
				.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		// điểu hướng qua trang thông báo lỗi
		assertEquals("/alert", response.getRedirectedUrl());
	}

	// Test Xóa bài viết case: role editor
	@Test
	public void testDelete_role_editor() throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/posts/{id}/delete", 10)
				.sessionAttr("user", user).sessionAttr("role", Roles.EDITOR)).andDo(MockMvcResultHandlers.print())
				.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		// điểu hướng qua trang thông báo lỗi
		assertEquals("/alert", response.getRedirectedUrl());
	}

	/*
	 * Test Xóa bài viết case: bài viết đó tồn tại và method get trả vài viết về
	 * trang xác nhận xóa
	 */
	@Test
	public void testDelete_posts_exists_method_get() throws Exception {
		SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd");
		Posts post = new Posts(10, "Tin tức tối nay", "Tin tức tối nay nhiều vụ cướp xảy ra", null,
				ff.parse("2022-11-11"), null);

		// giả lập Service trả về dữ liệu mong muốn
		// trả về list role
		when(roleService.listRoleByUser(anyString())).thenReturn(listRole);
		// tim kiếm post theo id
		when(postsService.getPostdByID(anyInt())).thenReturn(post);// return post or null
		// return categoryName by id
		when(categoryService.getNameByID(anyInt())).thenReturn(createListCategoryTest().get(0).getName())
				.thenReturn(createListCategoryTest().get(1).getName())
				.thenReturn(createListCategoryTest().get(4).getName());
		// trả về list categoryId by postsId
		when(CPService.getListCategoryID(anyInt())).thenReturn(Arrays.asList(1, 2, 5));

		MvcResult mvcResult = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/posts/{id}/delete", 10).sessionAttr("user", user))
				.andDo(MockMvcResultHandlers.print()).andReturn();

		// kiểm tra
		assertEquals("Tin tức tối nay",
				((PostsOutput) mvcResult.getModelAndView().getModel().get("postsOutput")).getTitle());
		// viewName trả về
		assertEquals("/posts/Delete", mvcResult.getModelAndView().getViewName());

	}

	// Test Xóa bài viết case: bài viết đó tồn tại và method post thực hiện xóa bài
	// viết
	@Test
	public void testDelete_posts_exists_method_post() throws Exception {
		SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd");
		Posts post = new Posts(10, "Tin tức tối nay", "Tin tức tối nay nhiều vụ cướp xảy ra", null,
				ff.parse("2022-11-11"), null);

		// giả lập Service trả về dữ liệu mong muốn
		// trả về list role
		when(roleService.listRoleByUser(anyString())).thenReturn(listRole);
		// tim kiếm post theo id
		when(postsService.getPostdByID(anyInt())).thenReturn(post);

		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/posts/{id}/delete", 10)
				.sessionAttr("user", user).sessionAttr("role", Roles.ADMIN)).andDo(MockMvcResultHandlers.print())
				.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();
		// kiểm tra
		assertEquals("Xóa thành công bài viết có tiêu đề là:Tin tức tối nay", mvcResult.getFlashMap().get("message"));
		// kiểm tra trang được điều hướng tới
		assertEquals("/posts", response.getRedirectedUrl());

	}

	// Test chi tiết bài viết case: bài viết đó không tồn tại
	@Test
	public void testPostsDetail_posts_not_exists() throws Exception {
		// giả lập Service trả về dữ liệu mong muốn
		when(postsService.getPostdByID(anyInt())).thenReturn(null);
		when(roleService.listRoleByUser(anyString())).thenReturn(listRole);

		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/posts/{id}/detail", 10)
				.sessionAttr("user", user).sessionAttr("role", Roles.ADMIN)).andDo(MockMvcResultHandlers.print())
				.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		// kiểm tra
		assertEquals("Không tìm thấy bài viết với id:10", mvcResult.getFlashMap().get("message"));
		// kiểm tra trang được điều hướng tới
		assertEquals("/posts", response.getRedirectedUrl());

	}

	// Test chi tiết bài viết case: bài viết đó tồn tại
	@Test
	public void testPostsDetail_posts_exists() throws Exception {
		SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd");
		Posts post = new Posts(10, "Tin tức tối nay", "Tin tức tối nay nhiều vụ cướp xảy ra", null,
				ff.parse("2022-11-11"), null);

		// giả lập Service trả về dữ liệu mong muốn
		when(roleService.listRoleByUser(anyString())).thenReturn(listRole);
		when(CPService.getListCategoryID(anyInt())).thenReturn(Arrays.asList(2, 5));
		// return categoryName by id
		when(categoryService.getNameByID(anyInt())).thenReturn(createListCategoryTest().get(0).getName())
				.thenReturn(createListCategoryTest().get(1).getName())
				.thenReturn(createListCategoryTest().get(4).getName());
		when(postsService.getPostdByID(anyInt())).thenReturn(post);// return null or posts

		MvcResult mvcResult = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/posts/{id}/detail", 10).sessionAttr("user", user))
				.andDo(MockMvcResultHandlers.print()).andReturn();

		PostsOutput postsOutput = (PostsOutput) mvcResult.getModelAndView().getModel().get("postsDetail");

		// kiểm tra dữ liệu
		assertEquals("Tin tức tối nay", postsOutput.getTitle());
		assertEquals("Tin tức", postsOutput.getCategories().get(0).getCategoryName());
		assertEquals(2, postsOutput.getCategories().size());
		// kiểm tra viewName
		assertEquals("posts/postsDetail", mvcResult.getModelAndView().getViewName());

	}

}
