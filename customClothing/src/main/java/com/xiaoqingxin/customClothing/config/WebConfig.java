package com.xiaoqingxin.customClothing.config;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import com.xiaoqingxin.customClothing.formatter.DateFormatter;
import com.xiaoqingxin.customClothing.formatter.VarietyFormatter;

import org.thymeleaf.context.Context;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;


@Configuration
@EnableWebMvc
@EnableAsync // 支持异步调用
@ComponentScan(basePackages = "com.xiaoqingxin.customClothing.web", useDefaultFilters = false, includeFilters = @Filter(Controller.class))
public class WebConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware, AsyncConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

	@Autowired
	private ApplicationContext applicationContext;

	public WebConfig() {
		super();
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

//	/** jsp视图解析器  */
//	@Bean("internalResourceViewResolver")
//	public ViewResolver viewResolverForJsp() {
//		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//		resolver.setPrefix("/WEB-INF/views/");
//		resolver.setSuffix(".jsp");
//		resolver.setExposeContextBeansAsAttributes(true);
//		// 将视图解析为JstlView，而不是InternalResourceView
////		resolver.setViewClass(JstlView.class);
//		return resolver;
//	}

//	/** 拦截器 */
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
////		registry.addInterceptor(new ExceptionLogInterceptor());
//		// registry.addInterceptor(localeChangeInterceptor());
//		super.addInterceptors(registry);
//	}

	/** 把静态资源的处理请求交给默认Servlet */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

//	/** spring静态资源处理器 */
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		super.addResourceHandlers(registry);
//		registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");
//		registry.addResourceHandler("/images/**").addResourceLocations("/images/");
//        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
//        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
//	}

	/** 国际化信息 */
	@Bean
	public MessageSource messageSource() {
		// 能够重新加载信息属性 不必重新编译或重启应用
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		// 可设置为类路径 classpath: 或者文件路径 file:
		messageSource.setBasenames("Messages", "ValidationMessages");
		messageSource.setCacheSeconds(600);
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Override
	public void addFormatters(final FormatterRegistry registry) {
		super.addFormatters(registry);
		registry.addFormatter(varietyFormatter());
		registry.addFormatter(dateFormatter());
	}

	@Bean
	public VarietyFormatter varietyFormatter() {
		return new VarietyFormatter();
	}

	@Bean
	public DateFormatter dateFormatter() {
		return new DateFormatter();
	}

	/** Thymeleaf 模板解析器 */
	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(this.applicationContext);
		templateResolver.setPrefix("/WEB-INF/views/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCacheable(true);
		templateResolver.setCacheTTLMs(10000L);
		templateResolver.setCharacterEncoding("UTF-8");
		// 允许解耦
		templateResolver.setUseDecoupledLogic(true);
		return templateResolver;
	}

	
	/** Thymeleaf 模板引擎 */
	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setEnableSpringELCompiler(true);
		templateEngine.addDialect(new SpringSecurityDialect());  // 添加Spring-Security标签支持
		return templateEngine;
	}

	/** Thymeleaf 视图解析器 */
	@Bean
	public ThymeleafViewResolver viewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setOrder(1);
		viewResolver.setCharacterEncoding("UTF-8");
//		viewResolver.setViewNames(new String[] { ".html", ".xhtml" });
		return viewResolver;
	}
	
	@Bean("thymeleafContext")
	public Context thymeleafContext() {
		return new Context();
	}
	
	/** 文件上传 */
	@Bean
	public MultipartResolver multipartResolver() throws IOException {
		return new StandardServletMultipartResolver();
	}

	
	@Override
	public Executor getAsyncExecutor() {
		// new DelegatingSecurityContextExecutorService(Executors.newFixedThreadPool(5));
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(5);
		taskExecutor.setMaxPoolSize(10);
		taskExecutor.setQueueCapacity(200);
		taskExecutor.initialize();
		return taskExecutor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new SpringAsyncExceptionHandler();
	}

	class SpringAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
		@Override
		public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
			logger.error("Exception occurs in async method,error={}", throwable);
		}
	}
	
	

}
