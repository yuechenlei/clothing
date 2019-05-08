package com.xiaoqingxin.customClothing.model;

import java.io.Serializable;
import java.util.Date;

import com.xiaoqingxin.customClothing.enums.Sex;


public class User implements Serializable{
	private static final long serialVersionUID = 3169932651726600298L;
	
	private Long id;
	private Long mpNumber;
	private String password;
	private String email;
	private String nickname;
	private String role;
	private Sex sex;
	private Date registerDate;
	private Date lastModifyDate;
	private Date lastLoginDate;
	private String lastLoginIp;
	private int version;
	private boolean state;
	private String activationKey;
	private String uuid;
	private boolean treaty;
	private String avatar;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMpNumber() {
		return mpNumber;
	}
	public void setMpNumber(Long mpNumber) {
		this.mpNumber = mpNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Sex getSex() {
		return sex;
	}
	public void setSex(Sex sex) {
		this.sex = sex;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public Date getLastModifyDate() {
		return lastModifyDate;
	}
	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public String getLastLoginIp() {
		return lastLoginIp;
	}
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public String getActivationKey() {
		return activationKey;
	}
	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public boolean isTreaty() {
		return treaty;
	}
	public void setTreaty(boolean treaty) {
		this.treaty = treaty;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [id=");
		builder.append(id);
		builder.append(", mpNumber=");
		builder.append(mpNumber);
		builder.append(", password=");
		builder.append(password);
		builder.append(", email=");
		builder.append(email);
		builder.append(", nickname=");
		builder.append(nickname);
		builder.append(", role=");
		builder.append(role);
		builder.append(", sex=");
		builder.append(sex);
		builder.append(", registerDate=");
		builder.append(registerDate);
		builder.append(", lastModifyDate=");
		builder.append(lastModifyDate);
		builder.append(", lastLoginDate=");
		builder.append(lastLoginDate);
		builder.append(", lastLoginIp=");
		builder.append(lastLoginIp);
		builder.append(", version=");
		builder.append(version);
		builder.append(", state=");
		builder.append(state);
		builder.append(", activationKey=");
		builder.append(activationKey);
		builder.append(", uuid=");
		builder.append(uuid);
		builder.append(", treaty=");
		builder.append(treaty);
		builder.append(", avatar=");
		builder.append(avatar);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
	

}
