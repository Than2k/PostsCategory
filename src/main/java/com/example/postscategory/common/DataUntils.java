package com.example.postscategory.common;

import java.util.Random;

	public class DataUntils {
		public static String genarateCode(int length) {
			String number = "012345678";
			char otp[] = new char[length];
			Random getOtpNum = new Random();
			for(int i = 0; i < length; i++ ) {
				otp[i] = number.charAt(getOtpNum.nextInt(number.length()));
			}
			String otpCode = "";
			for(int i=0 ; i < otp.length ; i++) {
				otpCode += otp[i];
			}
			return otpCode;
		}
}
