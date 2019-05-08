package com.xiaoqingxin.customClothing.config;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;


//@Configuration
//@ImportResource("classpath:/spring-dataSource.xml")
//@MapperScan("com.xiaoqingxin.customClothing.mapper")
//@EnableTransactionManagement(order = 0)
public class MyBatisConfig implements TransactionManagementConfigurer{
//	private static final Logger logger = LoggerFactory.getLogger(MyBatisConfig.class);
	
	@javax.annotation.Resource
	DataSource dataSource;
	
	/***
	 * 配置SqlSessionFactoryBean
	 * @return SqlSessionFactoryBean
	 */
	@Bean(name="sqlSessionFactory")
	public SqlSessionFactoryBean initSqlSessionFactory() {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);
		// MyBatis配置文件
		Resource resource = new ClassPathResource("mybatis-config.xml");
		sqlSessionFactory.setConfigLocation(resource);
		return sqlSessionFactory;
	}
	
	/***
	 *    通过自动扫描，发现MyBatis Mapper接口
	 * @return Mapper扫描器
	 */
	@Bean 
	public MapperScannerConfigurer initMapperScannerConfigurer() {
		MapperScannerConfigurer msc = new MapperScannerConfigurer();
		msc.setBasePackage("com.xiaoqingxin.customClothing.mapper");
		msc.setSqlSessionFactoryBeanName("sqlSessionFactory");
		// 必须使用@Repository
		msc.setAnnotationClass(Repository.class);
		return msc;
	}

	/** 事务管理器 */
	@Override
	@Bean("transactionManager")
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		DataSourceTransactionManager tm = new DataSourceTransactionManager();
		tm.setDataSource(dataSource);

		return tm;
	}

	
	

}
