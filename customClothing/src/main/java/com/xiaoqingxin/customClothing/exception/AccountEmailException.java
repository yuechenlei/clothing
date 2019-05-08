package com.xiaoqingxin.customClothing.exception;

public class AccountEmailException extends Exception {

	private static final long serialVersionUID = -4102795148963716825L;

	public AccountEmailException(String message) {
		super(message);
	}

	public AccountEmailException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
