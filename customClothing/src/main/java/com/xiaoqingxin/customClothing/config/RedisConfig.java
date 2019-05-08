package com.xiaoqingxin.customClothing.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
@PropertySource("classpath:/redis.properties")
public class RedisConfig {
	
	@Value("${redis.host}")
	private String hostName;
	@Value("${redis.port}")
	private int port;
	@Value("${redis.master.name}")
	private String master;
	@Value("${redis.password}")
	private String password;
	@Value("${redis.dbIndex}")
	private int dbIndex;
	@Value("${redis.connection.timeout}")
	private int connTimeout;
	@Value("${redis.pool.maxTotal}")
	private int maxTotal;
	@Value("${redis.pool.minIdle}")
	private int minIdle;
	@Value("${redis.pool.maxIdle}")
	private int maxIdle;
	@Value("${redis.pool.maxWaitMillis}")
	private long maxWaitMillis;
	@Value("${redis.pool.blockWhenExhausted}")
	private boolean blockWhenExhausted;
	@Value("${redis.pool.testOnBorrow}")
	private boolean testOnBorrow;
	@Value("${redis.pool.testOnReturn}")
	private boolean testOnReturn;
	@Value("${redis.pool.testWhileIdle}")
	private boolean testWhileIdle;
	@Value("${redis.pool.minEvictableIdleTimeMillis}")
	private long minEvictableIdleTimeMillis;
	@Value("${redis.pool.timeBetweenEvictionRunsMillis}")
	private long timeBetweenEvictionRunsMillis;
	@Value("${redis.pool.numTestsPerEvictionRun}")
	private int numTestsPerEvictionRun;
	@Value("${redis.pool.softMinEvictableIdleTimeMillis}")
	private long softMinEvictableIdleTimeMillis;
	@Value("${redis.pool.lifo}")
	private boolean lifo;
//	private static JedisPool pool=null;
	

	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMinIdle(minIdle);
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);
		jedisPoolConfig.setTestOnBorrow(testOnBorrow);
		jedisPoolConfig.setTestOnReturn(testOnReturn);
		jedisPoolConfig.setTestWhileIdle(testWhileIdle);
		jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
		jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTimeMillis);
		jedisPoolConfig.setLifo(lifo);
		return jedisPoolConfig;
	}

//	/**
//	 * 哨兵模式
//	 * @Title: jedisPool @Description: 哨兵模式
//	 * @param @return 参数 
//	 * @return JedisSentinelPool 返回类型 
//	 * @throws
//	 */
//	@Bean
//	public JedisSentinelPool jedisPool() {
//		Set<String> sentinels = new HashSet<String>();
//		sentinels.addAll(Arrays.asList(hostName.split(";")));
//
//		JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(master, sentinels, jedisPoolConfig(), connTimeout,
//				password, dbIndex);
//		return jedisSentinelPool;
//	}
	
//	/** 连接池方式 */
//	@Bean
//	public JedisPool pool(){
//		pool=new JedisPool(jedisPoolConfig(),hostName,port,connTimeout,password,dbIndex);
//		return pool;
//	}
	
	/** springTemplate工厂方式 */
	@Bean
	public JedisConnectionFactory jedisConnectionFactory(){
		JedisConnectionFactory jcf = new JedisConnectionFactory();
		jcf.setPoolConfig(jedisPoolConfig());
		jcf.setHostName(hostName);
		jcf.setPort(port);
		jcf.setTimeout(connTimeout);
		jcf.setPassword(password);
		jcf.setDatabase(dbIndex);
		// 因为配置在了ioc容器中 ，所以不需要自行调用了,spring会自动调用
//		jcf.afterPropertiesSet();
		return jcf;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public RedisTemplate redisTemplate(){
	
		RedisTemplate rt = new RedisTemplate();
		// 为了测试暂时取消JdkSerializationRedisSerializer  不测试了需要改过来
		RedisSerializer jsrs = new JdkSerializationRedisSerializer();
		RedisSerializer srs = new StringRedisSerializer(Charset.forName("UTF-8"));
		rt.setDefaultSerializer(srs);
		rt.setKeySerializer(srs);
//		rt.setValueSerializer(srs);
		rt.setValueSerializer(jsrs);
		rt.setHashKeySerializer(srs);
//		rt.setHashValueSerializer(srs);
		rt.setHashValueSerializer(jsrs);
		rt.setConnectionFactory(jedisConnectionFactory());
		rt.setEnableTransactionSupport(true);
		return rt;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean("redisTemplateForCache")
	public RedisTemplate redisTemplateForCache(){
	
		RedisTemplate rt = new RedisTemplate();
		// 为了测试暂时取消JdkSerializationRedisSerializer  不测试了需要改过来
		RedisSerializer jsrs = new JdkSerializationRedisSerializer();
		RedisSerializer srs = new StringRedisSerializer(Charset.forName("UTF-8"));
		rt.setDefaultSerializer(srs);
		rt.setKeySerializer(srs);
//		rt.setValueSerializer(srs);
		rt.setValueSerializer(jsrs);
		rt.setHashKeySerializer(srs);
//		rt.setHashValueSerializer(srs);
		rt.setHashValueSerializer(jsrs);
		rt.setConnectionFactory(jedisConnectionFactory());
//		rt.setEnableTransactionSupport(true);
		return rt;
	}
	
	/** 缓存管理器 */
	@Bean(name = "redisCacheManager")
	public CacheManager redisCacheManager() {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplateForCache());
		// 设置超时时间为10 分钟 单位为秒
		cacheManager.setDefaultExpiration(600);
		// 设置缓存名称
		List<String> cacheNames = new ArrayList<String>();
		cacheNames.add("redisCacheManager");
		cacheManager.setCacheNames(cacheNames);
		cacheManager.setTransactionAware(true);
		cacheManager.afterPropertiesSet();
		return cacheManager;
	}
	

}
