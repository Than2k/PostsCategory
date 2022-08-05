package com.example.testCrud.form;

import org.apache.commons.codec.digest.DigestUtils;

import utils.DataUtils;



public class testSha1 {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(DataUtils.genarateCode(6));
		System.out.println(DigestUtils.sha1Hex("than123").toString());
		if("d820aa5f8750fdde33200083b0702a32578a6c95".equals(DigestUtils.sha1Hex("than").toString())) {
			System.out.println("okoko");
		}
	}

}
