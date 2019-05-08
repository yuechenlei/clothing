package com.xiaoqingxin.customClothing.web.bean;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class UploadedFile implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private MultipartFile multipartFile;

	public MultipartFile getMultipartFile() {
		return multipartFile;
	}

	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UploadedFile [multipartFile=");
		builder.append(multipartFile);
		builder.append("]");
		return builder.toString();
	}
	
	

}
