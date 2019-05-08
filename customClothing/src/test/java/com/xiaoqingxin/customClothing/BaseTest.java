package com.xiaoqingxin.customClothing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xiaoqingxin.customClothing.config.RootConfig;
import com.xiaoqingxin.customClothing.config.WebConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootConfig.class,WebConfig.class })
@Rollback(false)
public class BaseTest {
	
	@Test
	public void testConfig() {

	}

}
