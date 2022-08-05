package com.example.testCrud.form;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCode {
	private String userName;
	private String code;
	private String email;
	private LocalTime sendTime;
}
