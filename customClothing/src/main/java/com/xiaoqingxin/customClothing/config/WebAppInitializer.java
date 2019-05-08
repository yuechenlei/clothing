package com.xiaoqingxin.customClothing.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

@Order(1)
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	private static final Logger logger = LoggerFactory.getLogger(WebAppInitializer.class);

	public static final String CHARACTER_ENCODING = "UTF-8";
	public static final String UPLOADS_PATH = "F:/uploads";
//	public static final String ACTIVE_PROFILES = "development";

	public WebAppInitializer() {
		super();
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { RootConfig.class,SecurityConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	protected void customizeRegistration(Dynamic registration) {
		registration.setMultipartConfig(new MultipartConfigElement(UPLOADS_PATH, 2*1024*1024, 10*1024*1024, 2*1024*1024));
	}

	@Override
	protected Filter[] getServletFilters() {
		final CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding(CHARACTER_ENCODING);
		encodingFilter.setForceEncoding(true);
		return new Filter[] { encodingFilter };
	}

	@Override
	protected WebApplicationContext createRootApplicationContext() {

		WebApplicationContext context = super.createRootApplicationContext();
		ConfigurableEnvironment environment = (ConfigurableEnvironment) context.getEnvironment();
//		environment.setActiveProfiles(ACTIVE_PROFILES);
		try {
			environment.getPropertySources().addFirst(new ResourcePropertySource("classpath:/mariadb.properties"));
		} catch (IOException e) {
			logger.error("",e);
		}

//		environment.setActiveProfiles(new String[]{"live", "testdb"});

		return context;

	}
	
//	@Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//        super.onStartup(servletContext);
//        servletContext.setInitParameter("spring.profiles.active", "live");
//
//        //Set multiple active profile
//        //servletContext.setInitParameter("spring.profiles.active", "dev, testdb");
//    }

}
