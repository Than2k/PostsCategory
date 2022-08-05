package com.example.testCrud.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.testCrud.common.Roles;
import com.example.testCrud.model.Role;
import com.example.testCrud.model.User;
import com.example.testCrud.service.IRoleService;

@Component
public class AuthencationInterceptor implements HandlerInterceptor{

	@Autowired
	private IRoleService roleService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//tạo các biến để kiểm tra role
		boolean Role_Admin = false;
		boolean Role_Editor = false;
		boolean Role_User = false;
		
		HttpSession session = request.getSession();
		String uri = request.getRequestURI();//lấy uri trước đăng nhập
		session.setAttribute("uri", uri);// lưu lại uri sau khi đăng nhập trả về trang đó
		
		//if mà user chưa tồn tại thì bắt đăng nhập đăng nhập thành công kiểm tra quyền
		if(session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			List<Role> listRole = roleService.listRoleByUser(user.getUserName());
			for (Role role : listRole) {
				if(role.getName().equals(Roles.ADMIN)) {
					Role_Admin = true;
					break;
				}else if(role.getName().equals(Roles.EDITOR)) {
					Role_Editor = true;
					break;
				}else if(role.getName().equals(Roles.USER)) {
					Role_User = true;
					break;
				}			
			}
			session.setAttribute( "role", (Role_Admin) ? Roles.ADMIN : (Role_Editor) ? Roles.EDITOR : Roles.USER);
			// kiểm tra xem đường dẫn yêu cầu có hợp lệ  với người dùng đó không
			if( Role_Admin == true ) {
				return true;
				
			}else if( Role_Editor == true ){
				if( uri.contains("delete") ) {
					response.sendRedirect("/alert");
					return false;
				}
				else
					return true;
				
			}else if( Role_User == true ) {
				if( uri.contains("create") || uri.contains("delete") || uri.contains("edit") ) {
					response.sendRedirect("/alert");
					return false;
				}				
				else
					return true;
			}		
		}
		response.sendRedirect("/login");
		return true;
	}
}
