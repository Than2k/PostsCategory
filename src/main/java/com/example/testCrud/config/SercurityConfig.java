//package com.example.testCrud.config;
//package com.example.testCrud.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//
//
//
//@Configuration//khi chúng ta chạy cái project thì sẽ chạy sercurity này trước
//@EnableWebSecurity
//
//public class sercurityConfig extends WebSecurityConfigurerAdapter {
//
//	@Override
//	public void configure(AuthenticationManagerBuilder auth)  {
//		// TODO Auto-generated method stub
//		 try {
//
//			 auth.inMemoryAuthentication()
//			 .withUser("user").password("{noop}123").roles("USER");//noop mk sẽ để ở dạng mặc định
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.out.println("lỗi:"+e.getMessage());
//		}
//
//	}
//	@Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//	            .antMatchers("/", "/home","/login").permitAll() // Cho phép tất cả mọi người truy cập vào 2 địa chỉ này
//	            .anyRequest().authenticated() // Tất cả các request khác đều cần phải xác thực mới được truy cập
//	            .and()
//            .formLogin() // Cho phép người dùng xác thực bằng form login
//            	.loginPage("/login")
//            	.loginProcessingUrl("/login")// cái url trả về cái Authentication chứa cái username, passworrd
//            	.defaultSuccessUrl("/posts",true)
//            	.failureUrl("/login?success=false")
////            	.usernameParameter("username")// mặc định mà username ,password if đổi trên form thì phải đổi ở đây
////            	.passwordParameter("password")//
//            	
//                .and()
//            .logout() // Cho phép logout
//                .permitAll();
//    }
//	
//}
