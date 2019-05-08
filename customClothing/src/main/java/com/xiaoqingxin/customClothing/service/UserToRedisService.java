package com.xiaoqingxin.customClothing.service;

import com.xiaoqingxin.customClothing.model.User;

public interface UserToRedisService {
	
	   // 为适应缓存而返回 User
		User insert(User user);

		void delete(String id);

		// 为适应缓存而返回 User
		User update(User user);

		/** 通过id查询 */
		User get(String id);

//		/** 根据名字模糊查询 */
//		User find(String name);
		
		User findByUuid(String uuid);

		/** 异步方式，插入user */
		User insertForAsync(User user);

		User updateForAsync(User user);
		
		User findByEmail(String email);
		
		/** 通过手机号查询 */
		User findByMPN(String mpn);

		String getActivationKey(String key);

		void setActivationKey(String key, String activationKey);

}
