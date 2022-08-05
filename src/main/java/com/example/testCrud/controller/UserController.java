package com.example.testCrud.controller;

import java.time.LocalTime;
import java.time.temporal.ChronoField;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.testCrud.form.UserCode;
import com.example.testCrud.model.User;
import com.example.testCrud.service.IUserService;

import utils.DataUtils;

@Controller
public class UserController {
	
	@Autowired
	private IUserService userService;
	@Autowired
	private JavaMailSender javaMailSender;
	
	
	//trang đăng nhập
	@GetMapping("/login")
	public String login(Model model, @RequestParam(name = "username", required = false,defaultValue = "" ) String username, 
						 @RequestParam(name = "password" ,required = false ,defaultValue = "") String password,
						 HttpServletRequest request, RedirectAttributes redirect) {
		
		HttpSession  session = request.getSession();//tạo biến session
		String uri = null;//dể lấy url lưu trong session trước khi đăng nhập
		User  user = null;
	
		//kiểm tra xem người dùng có yêu cầu đường dẫn trước khi đăng nhập
		if(session.getAttribute("uri") != null) {
			uri = session.getAttribute("uri").toString();
		}
		//gọi trang login lần dầu tiên 
		if(username.equals("") && password.equals("") && session.getAttribute("user") == null ) {
			return "user/Login";
		}
		//tìm kiếm user theo userName
		user = userService.findByUserName(username); 
		
		//if tài khoản tồn tại thì return profile
		if(session.getAttribute("user") != null) {
			return "redirect:/user/profile";
		}
		
		//nếu user == null thì thông báo lỗi
		if(user == null ) {
			model.addAttribute("error", "Tài khoản không tồn tại.");
			return "user/Login";
		
		//ngược lại check thông tin tài khoản mật khẩu
		}else if(user.getPassword().equals(DigestUtils.sha1Hex(password).toString())){
			if(user.getUpdatedAt() != null) {//trường updated_at khác null
				model.addAttribute("error", "Tài khoản của bạn đã bị vô hiệu hóa!");
				return "user/Login";
			}
			session.setAttribute("user", user);//tạo sssion lưu thông tin đăng nhập
			session.removeAttribute("url");//xoa session url đi
			model.addAttribute("user", user);
			
			//đăng nhập thành công nếu uri khác null thì trả về trang trước đó or trả về trang home
			return (uri != null) ? "redirect:" + uri : "redirect:/home";
		}
		model.addAttribute("error", "Mật khẩu không đúng!.");
		return "user/Login";
	}
	
	//trang hiển thị thông tin người dùng
	@GetMapping("/profile")
	public String profile(	Model model, HttpServletRequest rq ,
			 				RedirectAttributes redirect) {	
		
		HttpSession session = rq.getSession();
		if(rq.getSession().getAttribute("user") == null)
			return "redirect:/login";
		model.addAttribute("user",session.getAttribute("user"));
		return "user/Profile";	
	}
	
	//đăng xuất tài khoản
	@GetMapping("/logout")
	public String logout( HttpServletRequest rq ) {
		
		HttpSession session = rq.getSession();
		session.removeAttribute("user");//xóa session user
		return "user/Login";
	}

	//quên mật khẩu tài khoản
	@GetMapping("user/forgotPassword")
	public String forgotPassword(@RequestParam(name = "username", required = true, defaultValue = "") String username ,
								 HttpServletRequest rq,
								 RedirectAttributes redirect,
								 Model model) {
		
		HttpSession session = rq.getSession();
		UserCode userCode = null;
		User user = null;
		if(session.getAttribute("userCode") != null)
			userCode = (UserCode)session.getAttribute("userCode");
		
		//if username tồn tại thì gưi mã xác nhận về email của người dùng
		if(username.equals("") && userCode == null) {
			return "user/ForgotPassword";
		}
		
		// trả về User theo userName
		if(userCode != null) {
			user = userService.findByUserName(userCode.getUserName());//đã gửi mã xác thực mà hết hiệu lực yêu cầu gửi lại
		}else {
			user = userService.findByUserName(username);//yêu cầu gửi mã xác thực lần đầu
		}
		// userName tồn tại
		if( user != null || userCode != null) {
			//if tài không bị vô  hiệu hóa mới cho đổi mật khẩu
			if( user.getUpdatedAt() == null ) {
				String code = DataUtils.genarateCode(6);//tạo mã xác thực gồm 6 số
				SimpleMailMessage msg = new SimpleMailMessage();
				msg.setTo( (userCode != null) ? userCode.getEmail() : user.getEmail());//set email gửi mã
				msg.setSubject("Đặt lại mật khẩu cho tài khoản của bạn!!");// set title email
				msg.setText("xin chào "+ user.getFullName()+"\n" +
							"mã xác thực cho tài khoản của bạn là: " +code +"\n" +
							"bạn không được cung cấp mã này cho bất kì ai để tránh mất tài khoản");// set nội dung
				javaMailSender.send(msg);//gửi mail
				
				//lưu thông tin email
				LocalTime time = java.time.LocalTime.now();//lấy thời gian vừa gửi mail
				session.setAttribute("userCode", new UserCode( user.getUserName(), code, user.getEmail(), time));
				model.addAttribute("email", user.getEmail());
				return "redirect:/user/verifyCode";
			
			}else {
				model.addAttribute("error","Tài khoản này đã bị vô hiệu hóa!");
			}
		}else {
			model.addAttribute("error","Tên đăng nhập này không đúng!");
		}
		return "user/ForgotPassword";
	}

	//xác thực mã code
	@GetMapping("/user/verifyCode")
	public String verifyCode(  Model model, HttpServletRequest rq ,
							   @RequestParam(value = "code" , required = true, defaultValue = "" ) String code,
							   RedirectAttributes redirect) {
		//tạo session
		HttpSession session = rq.getSession();
		UserCode userCode = null;
		
		//kiểm tra xem session với name = userCode có giá trị không
		if(session.getAttribute("userCode") != null) {
			userCode = (UserCode)session.getAttribute("userCode");
		}else {
			return "redirect:/user/forgotPassword";
		}
		//xử lý thời gian gửi mã xác nhận
		LocalTime timeNow = java.time.LocalTime.now();//lấy thời gian hiện tại
		long timeMilis = timeNow.getLong(ChronoField.MILLI_OF_DAY) - userCode.getSendTime().getLong(ChronoField.MILLI_OF_DAY);
		model.addAttribute("giay",60 - timeMilis/1000);//trẳ về thời gian xác thực
		model.addAttribute("email", userCode.getEmail());//trả về email form xác thực
		
		//chạy lần đầu
		if(code.equals("")) {
			return "user/VerifyCode";
		}
		
		//if thời gian xác thực mã >60 s thì thông báo lỗi
		if((timeMilis/1000)>60) {
			model.addAttribute("error","Mã xác nhận đã hết hiệu lực.");
			model.addAttribute("giay",0);
			return "user/VerifyCode";
			
		//if mã code nhập vào đúng thì return về form đổi mật khẩu
		}else if(code.equals(userCode.getCode())){ 
			session.setAttribute("verify", true);
			return "user/ResetPassword";
			
		//trả về trang xác thực mã xác nhận thông báo lỗi
		}else { 
			model.addAttribute("error", "Mã không chính xác.");
			return "user/VerifyCode";
		}
	}
	
	//thay đổi mật khẩu
	@GetMapping("/user/resetPassword")
	public String resetPassword(  Model model, @RequestParam(value = "newPassword", required = true, defaultValue = "") String newPassword,
								               @RequestParam(value = "enterPassword", required = true, defaultValue = "") String enterPassword,
								               HttpServletRequest rq ,
								               RedirectAttributes redirect) {
		HttpSession session = rq.getSession();
		UserCode userCode = null;
		
		//if chưa được xác thực thì không cho đổi mật khẩu
		if(session.getAttribute("verify") == null) {
			return "redirect:/user/verifyCode";
		}
		if( session.getAttribute("userCode") != null ) {
			userCode = (UserCode)session.getAttribute("userCode");
		}
		
		//if mk khớp cập nhật database return thông báo thay đổi mk thành công
		if( newPassword.equals(enterPassword) ) {
			
			User user = userService.findByUserName(userCode.getUserName());
			//lưu lại mật khẩu thay đổi
			userService.save(new User(user.getId(), user.getUserName(), user.getFullName(),
								 DigestUtils.sha1Hex(enterPassword.toString()) , user.getUpdatedAt(), user.getEmail()));
			model.addAttribute("error","bạn đã thay đổi mật khẩu thành công.");
			return "user/Alert";
		}else {//trả về trang resetPassword thông báo lỗi			
			model.addAttribute("error", "Mật khẩu không giống nhau");
			return "user/ResetPassword";
		}		
	}
	//thông báo lỗi
	@GetMapping("/alert")
	public String alert(Model model , HttpServletRequest rq) {
		model.addAttribute("error", "bạn không có quyền sử dụng chức năng này");
		return "user/Alert";
	}
	
}
