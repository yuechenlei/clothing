package com.xiaoqingxin.customClothing.utils;

import java.util.Date;
import java.util.Locale;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.xiaoqingxin.customClothing.exception.AccountEmailException;

@Component
/** 新注册邮箱验证-->发送邮件 */
public class SendEmailUtil {
	private static final Logger logger = LoggerFactory.getLogger(SendEmailUtil.class);
	@Resource
	private JavaMailSender javaMailSender;
	@Resource
	private Environment env;
	@Resource
    private TemplateEngine emailTemplateEngine;
	@Resource
	Context thymeleafContext;

	/**
	 * @param to  收信人地址
	 * @param subject  主题
	 * @param htmlText  内容
	 * @param locale  
	 * @param activationKey 6位激活码
	 * @param url 
	 * @throws AccountEmailException
	 */
	public void sendMail(String to, String subject, String htmlText, final Locale locale,String activationKey,
			String url) throws AccountEmailException {
		logger.info("method=sendMail(),to={},subject={},htmlText={}",toString(),subject,htmlText);
		
		
		try {
			MimeMessage msg = javaMailSender.createMimeMessage();
			MimeMessageHelper msgHelper = new MimeMessageHelper(msg,"UTF-8");

			msgHelper.setFrom(env.getProperty("mailserver.username"));
			msgHelper.setTo(to);
			if (StringUtils.isEmpty(subject)) {
				msgHelper.setSubject(env.getProperty("mail.subject"));
			}else {
				msgHelper.setSubject(subject);
			}
			
	        if (StringUtils.isEmpty(htmlText)) {
	        	thymeleafContext.setLocale(locale);
	        	thymeleafContext.setVariable("activationKey", activationKey);
	        	thymeleafContext.setVariable("subscriptionDate", new Date());
	        	thymeleafContext.setVariable("url", url);
	            
		        final String htmlContent = this.emailTemplateEngine.process("html/confirmation", thymeleafContext);
	        	msgHelper.setText(htmlContent, true);
			}else {
				msgHelper.setText(htmlText, true);
			}
			
			javaMailSender.send(msg);
		} catch (MessagingException e) {
			throw new AccountEmailException("Faild to send mail.", e);
		}
	}

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public Environment getEnv() {
		return env;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}

	public TemplateEngine getEmailTemplateEngine() {
		return emailTemplateEngine;
	}

	public void setEmailTemplateEngine(TemplateEngine emailTemplateEngine) {
		this.emailTemplateEngine = emailTemplateEngine;
	}

}
