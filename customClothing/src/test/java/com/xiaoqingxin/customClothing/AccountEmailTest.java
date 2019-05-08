package com.xiaoqingxin.customClothing;

import static org.junit.Assert.assertEquals;

import javax.mail.Message;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;

public class AccountEmailTest extends BaseTest {

	private GreenMail greenMail;
	
//	@Autowired
//	private AccountEmailService accountEmailService;

	@Before
	public void startMailServer() throws Exception {
		greenMail = new GreenMail(ServerSetup.SMTP);
		greenMail.setUser("zhuxiaowei7277@gmail.com", "Wosou_2018");
		greenMail.start();
	}

	@Test
	public void testSendMail() throws Exception {
		String subject = "Test Subject";
		String htmlText = "<h3>Test</h3>";
//		accountEmailService.sendMail("1242061335@qq.com", subject, htmlText);

		greenMail.waitForIncomingEmail(2000, 1);

		Message[] msgs = greenMail.getReceivedMessages();
		assertEquals(1, msgs.length);
		assertEquals(subject, msgs[0].getSubject());
		assertEquals(htmlText, GreenMailUtil.getBody(msgs[0]).trim());
	}

	@After
	public void stopMailServer() throws Exception {
		greenMail.stop();
	}

}
