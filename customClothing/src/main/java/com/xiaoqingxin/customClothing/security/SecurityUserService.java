package com.xiaoqingxin.customClothing.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.xiaoqingxin.customClothing.model.User;
import com.xiaoqingxin.customClothing.service.UserToRedisService;

@Service
public class SecurityUserService implements UserDetailsService{
	
	@Resource
	private UserToRedisService userToRedisService;
	
	public SecurityUserService() {
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if(StringUtils.isEmpty(username)) return null;
		
		User quser = userToRedisService.findByMPN(username);
		if(quser==null || StringUtils.isEmpty(quser.getId())) {
			quser = userToRedisService.findByEmail(username);
			if(quser==null || StringUtils.isEmpty(quser.getId())) return null;
		}
		// 权限列表
		List<SimpleGrantedAuthority> authorities = createAuthorities(quser.getRole());
		
		return new org.springframework.security.core.userdetails.User(username, quser.getPassword(), authorities);
	}
	
	private List<SimpleGrantedAuthority> createAuthorities(String roleStr){
        String[] roles = roleStr.split(",");
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        for (String role : roles) {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        return simpleGrantedAuthorities;
    }

}
