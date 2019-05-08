package com.xiaoqingxin.customClothing;

/** 全局常量 */

public class C {
	
	public static final String APP_NAME = "xiaoqingxin";

	/** 用户key前缀 */
	public static final String USER_KEY = "QUSER-";
	/** 用户field前缀 */
	public static final String FIELD_KEY = "QFIELD-";
	/** temp前缀 */
	public static final String TEMP_KEY = "QTEMP-";
	
	/** 普通用户权限 */
	public static final String QUSER = "ROLE_QUSER";
	/** 匿名用户权限 */
	public static final String ANONYMOUS = "ROLE_ANONYMOUS";
	/** 管理员权限 */
	public static final String ADMIN = "ROLE_ADMIN";
	/** DBA权限 */
	public static final String DBA = "ROLE_DBA";
	
	/** 普通用户角色 */
	public static final String ROLE_QUSER = "QUSER";
	/** 匿名用户角色 */
	public static final String ROLE_ANONYMOUS = "ANONYMOUS";
	/** 管理员角色 */
	public static final String ROLE_ADMIN = "ADMIN";
	/** DBA角色 */
	public static final String ROLE_DBA = "DBA";
	
	/** 手机号正则表达式 */
	public static final String MPN_PATTERN = "^1[0-9]{10}$";
	/** 电子邮箱正则表达式 */
	public static final String EMAIL_PATTERN = "^([\\w\\-\\.])+@\\w+([\\-\\.]\\w+)*\\.\\w+([\\-\\.]\\w+)*$";
	
	

}
