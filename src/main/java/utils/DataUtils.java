package utils;

import java.util.Random;

public class DataUtils {
	public static String genarateCode(int length) {
		String number = "012345678";
		char otp[] = new char[length];
		Random getOtpNum = new Random();
		for(int i = 0; i < length; i++ ) {
			otp[i] = number.charAt(getOtpNum.nextInt(number.length()));
		}
		String optCode = "";
		for(int i=0 ; i < otp.length ; i++) {
			optCode += otp[i];
		}
		return optCode;
	}
}
