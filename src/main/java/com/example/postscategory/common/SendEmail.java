package com.example.postscategory.common;

import java.time.LocalTime;
import java.time.temporal.ChronoField;


import org.springframework.mail.SimpleMailMessage;

import com.example.postscategory.model.User;


public  class SendEmail {
	
	public static SimpleMailMessage sendVerifyCode(User user , String code) {
		
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(user.getEmail());// set email gửi mã
		msg.setSubject("Đặt lại mật khẩu cho tài khoản của bạn!!");// set title email
		msg.setText("xin chào " + user.getFullName() + "\n" + "mã xác thực cho tài khoản của bạn là: " + code
				+ "\n" + "bạn không được cung cấp mã này cho bất kì ai để tránh mất tài khoản");// set nội dung
		
		return msg;
	}
	public static long timeVerifyCode(LocalTime timeSend) {
		LocalTime timeNow = java.time.LocalTime.now();// lấy thời gian hiện tại
		long timeMilis = timeNow.getLong(ChronoField.MILLI_OF_DAY)
				- timeSend.getLong(ChronoField.MILLI_OF_DAY);
		return timeMilis/1000;
	}
}
