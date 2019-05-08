package com.xiaoqingxin.customClothing.web.bean;


import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginMitEmailBean implements Serializable{

	private static final long serialVersionUID = -6270268430317436984L;
	
//	@NotNull(message="邮件不能为空")
//	@Pattern(regexp="^([\\w\\-\\.])+@\\w+([\\-\\.]\\w+)*\\.\\w+([\\-\\.]\\w+)*$",message="非法邮件格式")
//	private String email;
	@NotNull(message="密码不能为空")
	@Size(min=6,max=20,message="密码长度错误")
	@Pattern(regexp="^([\\w\\-\\.]){6,20}$",message="密码包含非法字符")
	private String password;
	
	private String username;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoginMitEmailBean [password=");
		builder.append(password);
		builder.append(", username=");
		builder.append(username);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
}
