package com.xiaoqingxin.customClothing.config;


import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Controller;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
// @Import({DataSourceConfig.class})
@ImportResource("classpath:/spring-dataSource.xml")
@ComponentScan(basePackages = { "com.xiaoqingxin.customClothing" }, excludeFilters = { @Filter(Controller.class)})
public class RootConfig {

	/** 让spring能够解析properties文件  */
	@Bean(name = "propertySourcesPlaceholderConfigurer")
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
		pspc.setFileEncoding("UTF-8");
		return pspc;
	}

	@Bean(name = "validator")
	public Validator validator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		return factory.getValidator();
	}
}
