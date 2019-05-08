package com.xiaoqingxin.customClothing.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.xiaoqingxin.customClothing.C;
import com.xiaoqingxin.customClothing.security.SecurityUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;
	@Autowired
	SecurityUserService securityUserService;
	
	public SecurityConfig() {
		super();
	}
	
	/** BCrypt加密机  */
	@Bean
	public BCryptPasswordEncoder bcryptPasswordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
	
	/** “记住我”功能保存Token */
	@Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
	
    
	/** 拦截请求  进行权限控制  */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	   
	     http
		       .formLogin()
		       .loginPage("/loginRequest/toLogin")  // 指定登录页面的位置
		       .defaultSuccessUrl("/homepage")
		       .usernameParameter("username")
		       .passwordParameter("password")
		       .permitAll() // 授予所有用户（即未经身份验证的用户）访问我们的登录页面的权限
		     .and()
		       .rememberMe()
		       .rememberMeParameter("remember-me")
		       .tokenRepository(persistentTokenRepository())//设置操作表的Repository
	           .tokenValiditySeconds(2*60)//设置记住我的时间
	           //.userDetailsService(userDetailsService)//设置userDetailsService
//		       .key("unique-and-secret")
               .rememberMeCookieName("remember-me-cookie")
	           //.tokenValiditySeconds(10*24*60*60)
		     .and()
		       .logout()   // 提供注销支持。使用时会自动应用WebSecurityConfigurerAdapter。                                                             
		       .logoutUrl("/loginRequest/loginOut")   // 触发注销的URL（默认为/logout）。如果启用了CSRF保护（默认），则该请求也必须是POST 
		       // .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))  // 由.logoutUrl替换
		       .logoutSuccessUrl("/homepage")       // 注销后重定向到的URL。默认是 /login?logout。                                    
		       // .logoutSuccessHandler(logoutSuccessHandler)     // 指定一个自定义LogoutSuccessHandler。如果指定了，logoutSuccessUrl()则忽略。                         
		       .invalidateHttpSession(true)    // 指定HttpSession在注销时是否使其无效。默认为true。 
		       .clearAuthentication(true)  // 注销时清除身份验证信息
		       // .addLogoutHandler(logoutHandler)                                         
		       .deleteCookies("JSESSIONID","remember-me-cookie")  // 允许指定在注销成功时删除的cookie的名称。这是CookieClearingLogoutHandler显式添加的快捷方式。
	         .and()
	           .authorizeRequests()   // 有多个子节点，每个匹配器按其声明的顺序进行考虑。                                                             
	           .antMatchers("/resources/**", "/homepage", "/loginRequest/**" ,"/about").permitAll()   // 任何用户都可以访问的多种URL模式
	           .antMatchers("/personal/**").hasRole(C.ROLE_QUSER)
	           .antMatchers("/admin/**").hasRole(C.ROLE_ADMIN)   // 任何以  " / admin/" 开头的URL都必须是具有"ROLE_ADMIN"角色的用户。由于调用hasRole方法，所以不需要指定"ROLE_"前缀。                                   
	           .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")  // 任何以 / db/ 开头的URL都要求用户同时拥有 ROLE_ADMIN 和 ROLE_DBA 权限          
	           .anyRequest().authenticated() // 任何尚未匹配的URL只需要对用户进行身份验证
	         .and()
               .exceptionHandling()
               .accessDeniedPage("/403")
//           .and()
//               .requiresChannel()  // 自动将请求重定向到Https上
//               .antMatchers("")
//               .requiresSecure();
             .and()
               .sessionManagement()
               .sessionFixation()
               // .migrateSession()  创建新会话并保留会话属性
               .changeSessionId()  // 更改sessionId并保留会话属性
               .maximumSessions(1)  // 控制用户的最大会话数
               .expiredUrl("/loginRequest/toLogin?re=true")  // 会话过期后跳转的url
               .maxSessionsPreventsLogin(false);  // 允许进行身份验证的用户访问，并且现有用户的会话过期
	          
	   
	}
	
	// 基于JDBC认证  配置用户存储
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(securityUserService)
			.passwordEncoder(bcryptPasswordEncoder());
			// .and()
			// .authenticationProvider(authenticationProvider);
		
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	}
	
	
	
//	// 基于LDAP认证--示例
//	@Autowired
//	public void configureGlobal2(AuthenticationManagerBuilder auth) throws Exception {
//		auth
//			.ldapAuthentication()
//			.userDnPatterns("uid={0},ou=people")
//			.groupSearchBase("ou=groups");
//	}
	
	
//public static void main(String[] args) {
//	String string = "57c559d02eea4a279751e8baa92b402e";
//	System.out.println(string.length());
//}	
	
	
	UsernamePasswordAuthenticationFilter u;
	AbstractAuthenticationProcessingFilter a;
	FilterSecurityInterceptor fs;
	DaoAuthenticationProvider da;
	HttpSessionSecurityContextRepository hc;
	PersistentTokenRepository pt;

	
	
	
	
}
