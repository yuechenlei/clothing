package com.xiaoqingxin.customClothing.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.xiaoqingxin.customClothing.C;
import com.xiaoqingxin.customClothing.enums.Sex;
import com.xiaoqingxin.customClothing.model.User;
import com.xiaoqingxin.customClothing.service.UserToRedisService;

import redis.clients.jedis.exceptions.JedisConnectionException;

@Service("userToRedisService")
public class UserToRedisServiceImpl implements UserToRedisService{
	private static final Logger logger = LoggerFactory.getLogger(UserToRedisServiceImpl.class);
	
	@SuppressWarnings("rawtypes")
	@Resource
	private RedisTemplate redisTemplate;
	
//	private static final String[] FIELDS = new String[] { "id", "mpNumber", "password","email", "userName","sex",
//			"registerDate","lastModifyDate","lastLoginDate","version","state","uuid","treaty"};

	@SuppressWarnings("unchecked")
	@Override
	public User insert(User user) {
		
		Map<String, String> map = createUserMap(user);
		
		try {
			@SuppressWarnings("rawtypes")
			SessionCallback callBack = new SessionCallback() {
				@Override
				public Object execute(RedisOperations ops) throws DataAccessException {
					ops.multi();
					ops.opsForHash().putAll(C.USER_KEY+user.getId(),map);
					ops.opsForValue().set(C.USER_KEY+user.getUuid(), user.getId());
					if (!StringUtils.isEmpty(user.getEmail())) {
						ops.opsForValue().set(C.USER_KEY + user.getEmail(), user.getId());
					}
					if(!StringUtils.isEmpty(user.getMpNumber())) {
					ops.opsForValue().set(C.USER_KEY+user.getMpNumber(), user.getId());
					}
					ops.opsForValue().set(C.TEMP_KEY+user.getUuid()+"-"+"ActivationKey", user.getId());
					ops.expire(C.TEMP_KEY+user.getUuid()+"-"+"ActivationKey", 2, TimeUnit.MINUTES);
					return ops.exec();
				}
			};
			redisTemplate.executePipelined(callBack); 
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		}catch (Exception e) {
			logger.error("", e);
			throw e;
		} 
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void delete(String id) {
		try {
			User user = get(id);
			if(user==null) return;
			@SuppressWarnings("rawtypes")
			SessionCallback callBack = new SessionCallback() {
				@Override
				public Object execute(RedisOperations ops) throws DataAccessException {
					ops.multi();
					ops.delete(C.USER_KEY+id);
					ops.delete(C.USER_KEY+user.getUuid());
					ops.delete(C.USER_KEY+user.getEmail());
					ops.delete(C.USER_KEY+user.getMpNumber());
					ops.delete(C.TEMP_KEY+user.getUuid()+"-"+"ActivationKey");
					return ops.exec();
				}
			};
			redisTemplate.executePipelined(callBack); 
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		}catch (Exception e) {
			logger.error("", e);
			throw e;
		} 
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public User update(User user) {
		
		delete(Long.toString(user.getId()));
		
		Map<String, String> map = createUserMap(user);
		
		try {
			@SuppressWarnings("rawtypes")
			SessionCallback callBack = new SessionCallback() {
				@Override
				public Object execute(RedisOperations ops) throws DataAccessException {
					ops.multi();
					ops.opsForHash().putAll(C.USER_KEY+user.getId(),map);
					ops.opsForValue().set(C.USER_KEY+user.getUuid(), user.getId());
					if (!StringUtils.isEmpty(user.getEmail())) {
						ops.opsForValue().set(C.USER_KEY + user.getEmail(), user.getId());
					}
					if(!StringUtils.isEmpty(user.getMpNumber())) {
					ops.opsForValue().set(C.USER_KEY+user.getMpNumber(), user.getId());
					}
					return ops.exec();
				}
			};
			redisTemplate.executePipelined(callBack); 
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		}catch (Exception e) {
			logger.error("", e);
			throw e;
		} 
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public User get(String id) {
		if(StringUtils.isEmpty(id)) return null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, String> map = redisTemplate.opsForHash().entries(C.USER_KEY+id);
		if(map==null) return null;
		User user = new User();
		user.setId(Long.parseLong(map.get("id")));
		user.setMpNumber(map.get("mpNumber")==null?null:Long.parseLong(map.get("mpNumber")));
		user.setPassword(map.get("password"));
		user.setEmail(map.get("email"));
		user.setNickname(map.get("nickname"));
		user.setRole(map.get("role"));
		user.setAvatar(map.get("avatar"));
		user.setSex(Sex.valueOf(map.get("sex")));
		try {
			user.setRegisterDate(map.get("registerDate")==null?null:sdf.parse(map.get("registerDate")));
			user.setLastModifyDate(map.get("lastModifyDate")==null?null:sdf.parse(map.get("lastModifyDate")));
			user.setLastLoginDate(map.get("lastLoginDate")==null?null:sdf.parse(map.get("lastLoginDate")));
		} catch (ParseException e) {
			logger.error("", e);
		}
		user.setLastLoginIp(map.get("lastLoginIp"));
		user.setVersion(Integer.parseInt(map.get("version")));
		user.setState(Boolean.parseBoolean(map.get("state")));
		user.setUuid(map.get("uuid"));
		user.setTreaty(Boolean.parseBoolean(map.get("treaty")));
		
		return user;
	}

	@Override
	public User findByUuid(String uuid) {
		Object value = redisTemplate.opsForValue().get(C.USER_KEY+uuid);
		if(value==null) return null;
		String id = value.toString();
		return get(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Async
	public User insertForAsync(User user) {
		
		Map<String, String> map = createUserMap(user);
		
		try {
			@SuppressWarnings("rawtypes")
			SessionCallback callBack = new SessionCallback() {
				@Override
				public Object execute(RedisOperations ops) throws DataAccessException {
					ops.multi();
					ops.opsForHash().putAll(C.USER_KEY+user.getId(),map);
					ops.opsForValue().set(C.USER_KEY+user.getUuid(), user.getId());
					if (!StringUtils.isEmpty(user.getEmail())) {
						ops.opsForValue().set(C.USER_KEY + user.getEmail(), user.getId());
					}
					if(!StringUtils.isEmpty(user.getMpNumber())) {
					ops.opsForValue().set(C.USER_KEY+user.getMpNumber(), user.getId());
					}
					ops.opsForValue().set(C.TEMP_KEY+user.getUuid()+"-"+"ActivationKey",user.getActivationKey());
					ops.expire(C.TEMP_KEY+user.getUuid()+"-"+"ActivationKey",2,TimeUnit.MINUTES);
					return ops.exec();
				}
			};
			redisTemplate.executePipelined(callBack); 
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		}catch (Exception e) {
			logger.error("", e);
			throw e;
		} 
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Async
	public User updateForAsync(User user) {
		
        delete(Long.toString(user.getId()));
		
		Map<String, String> map = createUserMap(user);
		
		try {
			@SuppressWarnings("rawtypes")
			SessionCallback callBack = new SessionCallback() {
				@Override
				public Object execute(RedisOperations ops) throws DataAccessException {
					ops.multi();
					ops.opsForHash().putAll(C.USER_KEY+user.getId(),map);
					ops.opsForValue().set(C.USER_KEY+user.getUuid(), user.getId());
					if (!StringUtils.isEmpty(user.getEmail())) {
						ops.opsForValue().set(C.USER_KEY + user.getEmail(), user.getId());
					}
					if(!StringUtils.isEmpty(user.getMpNumber())) {
					ops.opsForValue().set(C.USER_KEY+user.getMpNumber(), user.getId());
					}
					return ops.exec();
				}
			};
			redisTemplate.executePipelined(callBack); 
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		}catch (Exception e) {
			logger.error("", e);
			throw e;
		} 
		return user;
	}

	@Override
	public User findByEmail(String email) {
		Object value = redisTemplate.opsForValue().get(C.USER_KEY+email);
		if(value==null) return null;
		String id = value.toString();
		return get(id);
	}

	@Override
	public User findByMPN(String mpn) {
		Object value = redisTemplate.opsForValue().get(C.USER_KEY+mpn);
		if(value==null) return null;
		String id = value.toString();
		return get(id);
	}

	@Override
	public String getActivationKey(String key) {
		Object value = redisTemplate.opsForValue().get(C.TEMP_KEY+key);
		if(value==null) return null;
		return value.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setActivationKey(String key, String code) {
		redisTemplate.opsForValue().set(C.TEMP_KEY+key,code);
		redisTemplate.expire(C.TEMP_KEY+key,2,TimeUnit.MINUTES);
	}
	
	private Map<String, String> createUserMap(User user) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, String> map = new HashMap<>();
		map.put("id", Long.toString(user.getId()));
		map.put("mpNumber", user.getMpNumber()==null?null:Long.toString(user.getMpNumber()));
		map.put("password", user.getPassword());
		map.put("email", user.getEmail()==null?null:user.getEmail());
		map.put("nickname", user.getNickname()==null?null:user.getNickname());
		map.put("role", user.getRole()==null?null:user.getRole());
		map.put("avatar", user.getAvatar()==null?null:user.getAvatar());
		map.put("sex", user.getSex()==null?Sex.SECRET.toString():user.getSex().toString());
		map.put("registerDate", user.getRegisterDate()==null?null:sdf.format(user.getRegisterDate()));
		map.put("lastModifyDate", user.getLastModifyDate()==null?null:sdf.format(user.getLastModifyDate()));
		map.put("lastLoginDate", user.getLastLoginDate()==null?null:sdf.format(user.getLastLoginDate()));
		map.put("lastLoginIp", user.getLastLoginIp()==null?null:user.getLastLoginIp());
		map.put("version", Integer.toString(user.getVersion()));
		map.put("state", Boolean.toString(user.isState()));
		map.put("uuid", user.getUuid());
		map.put("treaty", Boolean.toString(user.isTreaty()));
		
		return map;
	}
	

}
