package com.xiaoqingxin.customClothing.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoqingxin.customClothing.mapper.UserMapper;
import com.xiaoqingxin.customClothing.model.User;
import com.xiaoqingxin.customClothing.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Resource
	private UserMapper userMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	@CachePut(value = "redisCacheManager", key = "'MARIADB_USER:'+#result.id") // 缓存的数据类型为字符串
	public User insert(User user) {
		logger.info("method=insert(),user={}",user);
		userMapper.insert(user);
		user = userMapper.findByUuid(user.getUuid());
		return user;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	@CacheEvict(value = "redisCacheManager", key = "'MARIADB_USER:'+#id")
	public void delete(String id) {
		userMapper.delete(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	@CachePut(value = "redisCacheManager", key = "'MARIADB_USER:'+#user.id")
	public User update(User user) {
		userMapper.update(user);
		return user;
	}

	@Override
	@Cacheable(value = "redisCacheManager", key = "'MARIADB_USER:'+#id")
	public User get(String id) {
		return userMapper.get(id);
	}

	@Override
//	@Cacheable(value = "redisCacheManager", key = "'MARIADB_USER:'+#uuid")
	public User findByUuid(String uuid) {
		return userMapper.findByUuid(uuid);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	@CachePut(value = "redisCacheManager", key = "'MARIADB_USER:'+#result.id") // 缓存的数据类型为字符串
	@Async
	public User insertForAsync(User user) {
		userMapper.insert(user);
		user = userMapper.findByUuid(user.getUuid());
		return user;
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	@CachePut(value = "redisCacheManager", key = "'MARIADB_USER:'+#user.id")
	@Async
	public User updateForAsync(User user) {
		userMapper.update(user);
		return user;
	}

	@Override
	@Cacheable(value = "redisCacheManager", key = "'MARIADB_USER:'+#email")
	public User findByEmail(String email) {
		return userMapper.findByEmail(email);
	}

	@Override
	@Cacheable(value = "redisCacheManager", key = "'MARIADB_USER:'+#mpn")
	public User findByMPN(String mpn) {
		return userMapper.findByMPN(mpn);
	}

	@Override
	public User findByEmailAndPwd(String email, String pwd) {
		return userMapper.findByEmailAndPwd(email,pwd);
	}

	@Override
	public User findByMPNAndPwd(String mpn, String pwd) {
		return userMapper.findByMPNAndPwd(mpn,pwd);
	}

}
