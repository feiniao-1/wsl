package com.uploaddown;

/**
 * 注意： DbUtils组件，属性要与数据库字段一致！
 * 
 * @author AdminTH
 * 
 */
public class User {
	 private String id;// INT PRIMARY KEY AUTO_INCREMENT,
	 private String userName;// VARCHAR(50), -- 上传者
	 private String fileName;// VARCHAR(50), -- 文件名称(显示的名称)
	 private String fullName;// VARCHAR(100) -- 全名 (uuid + 文件名称)
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	

}