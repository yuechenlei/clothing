package com.xiaoqingxin.customClothing.web.bean;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class EmailRegisterBean implements Serializable{

	private static final long serialVersionUID = -7422830198997297995L;
	
	private Long id;
//	@NotNull(message="邮件不能为空")
//	@Pattern(regexp="^([\\w\\-\\.])+@\\w+([\\-\\.]\\w+)*\\.\\w+([\\-\\.]\\w+)*$",message="非法邮件格式")
//	private String email;
	@NotNull(message="密码不能为空")
	@Size(min=6,max=20,message="密码长度错误")
	@Pattern(regexp="^([\\w\\-\\.]){6,20}$",message="密码包含非法字符")
	private String password;
	@NotNull(message="密码不能为空")
	@Size(min=6,max=20,message="密码长度错误")
	@Pattern(regexp="^([\\w\\-\\.]){6,20}$",message="密码包含非法字符")
	private String password2;
	@AssertTrue(message="没有同意用户协议")
	private boolean reCheck;
	private Date registerDate;
	
	private String username;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public boolean isReCheck() {
		return reCheck;
	}

	public void setReCheck(boolean reCheck) {
		this.reCheck = reCheck;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
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
		builder.append("EmailRegisterBean [id=");
		builder.append(id);
		builder.append(", password=");
		builder.append(password);
		builder.append(", password2=");
		builder.append(password2);
		builder.append(", reCheck=");
		builder.append(reCheck);
		builder.append(", registerDate=");
		builder.append(registerDate);
		builder.append(", username=");
		builder.append(username);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
	

}
