package com.xiaoqingxin.customClothing.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.xiaoqingxin.customClothing.model.User;

@Repository
public interface UserMapper {

	void insert(User user);

	void delete(String id);

	void update(User user);

	User get(String id);
	
	User findByUuid(String uuid);

	User findByEmail(String email);

	User findByMPN(String mpn);

	User findByEmailAndPwd(@Param("email")String email, @Param("pwd")String pwd);

	User findByMPNAndPwd(@Param("mpn")String mpn, @Param("pwd")String pwd);

}
