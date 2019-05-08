package com.xiaoqingxin.customClothing.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;


/** 为应用程序中的每个URL注册springSecurityFilterChain过滤器  */
@Order(2)
public class SecurityWebInitializer extends AbstractSecurityWebApplicationInitializer {
	
	public SecurityWebInitializer() {
        super();
    }
}
