package com.example.postscategory.testcontroller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.example.postscategory.common.Roles;
import com.example.postscategory.controller.UserController;
import com.example.postscategory.model.Role;
import com.example.postscategory.model.User;
import com.example.postscategory.service.IRoleService;
import com.example.postscategory.service.IUserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class TestLogin {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IUserService userService;
	@MockBean
	private IRoleService roleService;

	// Test Login case: Tài khoản tồn tại và chưa bị vô hiệu hóa mật khẩu đúng
	@Test
	public void TestLogin_user_exists_status_enable_password_true() throws Exception {

		List<Role> listRole = Arrays.asList(new Role(1, "administrator"));

		User user = new User(3, "Tanthan", "Võ Tấn Thân", "a5e9681b51c61ec237a2d440257ba560a687f140", null,
				"tanthan2000@gmail.com");
		when(userService.findByUserName(anyString())).thenReturn(user);
		when(roleService.listRoleByUser(anyString())).thenReturn(listRole);

		MvcResult mvcResult = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/login").param("username", "Tanthan").param("password", "than123"))
				.andDo(MockMvcResultHandlers.print()).andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();
		
		assertEquals("Võ Tấn Thân", ((User) mvcResult.getRequest().getSession().getAttribute("user")).getFullName());
		assertEquals(Roles.ADMIN, mvcResult.getRequest().getSession().getAttribute("role"));
		assertEquals("/home", response.getRedirectedUrl());

	}

	// Test Login case: Tài khoản tồn tại và chưa bị vô hiệu hóa mật khẩu sai
	@Test
	public void TestLogin_user_exists_status_enable_password_false() throws Exception {

		User user = new User(3, "Tanthan", "Võ Tấn Thân", "a5e9681b51c61ec237a2d440257ba560a687f140",
				null, "tanthan2000@gmail.com");
		when(userService.findByUserName(anyString())).thenReturn(user);

		MvcResult mvcResult = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/login").param("username", "Tanthan").param("password", "than12"))
				.andDo(MockMvcResultHandlers.print()).andReturn();

		assertEquals("Mật khẩu không đúng!", mvcResult.getModelAndView().getModel().get("error"));
		assertEquals("user/Login", mvcResult.getModelAndView().getViewName());

	}

	// Test Login case: Tài khoản tồn tại và bị vô hiệu hóa
	@Test
	public void TestLogin_user_exists_status_disable() throws Exception {

		SimpleDateFormat ff = new SimpleDateFormat("dd-MM-yyyy");
		User user = new User(3, "Tanthan", "Võ Tấn Thân", "a5e9681b51c61ec237a2d440257ba560a687f140",
				ff.parse("2022-08-23"), "tanthan2000@gmail.com");
		when(userService.findByUserName(anyString())).thenReturn(user);

		MvcResult mvcResult = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/login").param("username", "Tanthan").param("password", "than123"))
				.andDo(MockMvcResultHandlers.print()).andReturn();

		assertEquals("Tài khoản của bạn đã bị vô hiệu hóa!", mvcResult.getModelAndView().getModel().get("error"));
		assertEquals("user/Login", mvcResult.getModelAndView().getViewName());

	}
	// Test Login case: Tài khoản không tồn tại
	@Test
	public void TestLogin_user_not_exists() throws Exception {
	
		when(userService.findByUserName(anyString())).thenReturn(null);

		MvcResult mvcResult = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/login").param("username", "Tanthan").param("password", "than123"))
				.andDo(MockMvcResultHandlers.print()).andReturn();

		assertEquals("Tài khoản không tồn tại.", mvcResult.getModelAndView().getModel().get("error"));
		assertEquals("user/Login", mvcResult.getModelAndView().getViewName());

	}
}
