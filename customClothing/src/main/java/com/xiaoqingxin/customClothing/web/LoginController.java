package com.xiaoqingxin.customClothing.web;


import java.security.Principal;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.xiaoqingxin.customClothing.C;
import com.xiaoqingxin.customClothing.model.User;
import com.xiaoqingxin.customClothing.service.UserService;
import com.xiaoqingxin.customClothing.service.UserToRedisService;
import com.xiaoqingxin.customClothing.utils.SendEmailUtil;
import com.xiaoqingxin.customClothing.utils.StringRandomUtil;
import com.xiaoqingxin.customClothing.web.bean.EmailRegisterBean;
import com.xiaoqingxin.customClothing.web.bean.LoginMitEmailBean;

@Controller
@RequestMapping({"/loginRequest"})
@SessionAttributes(names= {"id","email","mpNumber"})
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Resource
	private SendEmailUtil sendEmailUtil;
	@Resource
	private UserService userService;
	@Resource
	private UserToRedisService userToRedisService;
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	@RequestMapping("/toRegister")
	public String toRegister() {
		logger.info("LoginController,toRegister...");
		return "register/register";
	}
	
	@RequestMapping("/toLogin")
	public String toLogin(@RequestParam(value="re",required=false)boolean reLogin,Model model,HttpServletRequest request,@AuthenticationPrincipal Principal principal) {
		logger.info("LoginController,toLogin...");
		// 重复登录提醒
		if(reLogin) {
//			String username = principal==null?"":principal.getName();
//			User user = null;
//			if(Pattern.matches(C.MPN_PATTERN, username)) {
//				user = userToRedisService.findByMPN(username);
//			}
//            if(Pattern.matches(C.EMAIL_PATTERN, username)) {
//            	user = userToRedisService.findByEmail(username);
//			}
			model.addAttribute("reLogin", reLogin);
			//if(user!=null) model.addAttribute("ip", user.getLastLoginIp());
		}
		return "login/login";
	}
	
	// 邮箱验证跳转
	@RequestMapping("/emailVerify")
	public String toEmailVerify(@RequestParam(value="e",required=false)String email,Model model) {
		logger.info("method=toEmailVerify(),email={}",email);
		
		if(StringUtils.isEmpty(email)) return "home/home";
		
		User user = userToRedisService.findByEmail(email);
		if (user==null) return "home/home";
		
		model.addAttribute("remail", email);
		model.addAttribute("uuid", user.getUuid());
		
		return "register/emailVerify";
	}
	
	
	/** 邮箱注册 */
	@RequestMapping("/newEmailRegister")
	@ResponseBody
	public Map<String,Object> newEmailRegister(@Valid EmailRegisterBean er,Errors errors,Locale locale,Model model) {
		logger.info("method=newEmailRegister(),er={},locale={}",er,locale);
		
		Map<String,Object> result = new HashMap<>();
		
		if(errors.hasErrors()) {
			List<FieldError> fieldErrors = errors.getFieldErrors();
			for(FieldError error :fieldErrors) {
				logger.info("method=newEmailRegister(),error={}",error);
			}
			result.put("error", true);
			result.put("msg", "数据异常");
			return result;
		}
		if(!er.getPassword().equals(er.getPassword2())) {
			result.put("error", true);
			result.put("msg", "两次密码不一致");
			return result;
		}
		
		User tuser = userToRedisService.findByEmail(er.getUsername());
		if(tuser!=null) {
			result.put("existence", true);
			result.put("msg", "账号已存在");
			return result;
		}
		
		
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String activationKey = StringRandomUtil.getStringRandom(6);
		String url = "http://192.168.0.109:8090/customClothing/loginRequest/emailNotarizeForUrl?a="+uuid+"&b="+er.getUsername();
		
		try {
			sendEmailUtil.sendMail(er.getUsername(), "", "",locale,activationKey,url);
		} catch (Exception e) {
			result.put("error", true);
			result.put("msg", "系统异常");
			return result;
		}
		
		User user = new User();
		user.setEmail(er.getUsername());
		user.setPassword(bcryptPasswordEncoder.encode(er.getPassword()));
		user.setTreaty(true);
		user.setRole(C.QUSER);
		Date date = new Date();
		user.setRegisterDate(date);
		user.setLastModifyDate(date);
		user.setUuid(uuid);
		user.setActivationKey(activationKey);
		user.setState(false);
		
		userService.insert(user);
		user = userService.findByEmail(user.getEmail());
		user.setActivationKey(activationKey);
		userToRedisService.insertForAsync(user);
		
		model.addAttribute("remail", er.getUsername());
		model.addAttribute("uuid", uuid);
//		model.addAttribute("activationKey", activationKey);
//		model.addAttribute("url", url);
//		model.addAttribute("subscriptionDate", new Date());

		result.put("success", true);
		result.put("msg", "注册成功");
		
		return result;
//		return "register/confirmation";
		
	}
	
	
	/** 邮箱点击链接  验证 */
	@RequestMapping("/emailNotarizeForUrl")
	public String emailNotarizeForUrl(@RequestParam(value="a",required=false) String uuid,
			                    @RequestParam(value="b",required=false) String email,Model model) {
		logger.info("method=emailNotarize(),uuid={},email={}",uuid,email);
		if(StringUtils.isEmpty(uuid) || StringUtils.isEmpty(email) || uuid.length()<32) return "home/home";
		
		
		User user = userToRedisService.findByUuid(uuid);
		if(user==null) return "home/home";
		if(user.isState()) return "home/home";
		
		String remail = user.getEmail();
		if(!email.equals(remail)) {
			return "register/emailVerifyFail";
		}
		
		user.setState(true);
		user.setLastModifyDate(new Date());
		user.setVersion(user.getVersion()+1);
		// 异步调用
		userToRedisService.updateForAsync(user);
		userService.updateForAsync(user);
		
		model.addAttribute("id", user.getId());
		model.addAttribute("email", user.getEmail());
		
		return "register/emailVerifySuccess";
	}
	
	/** 邮箱验证码  验证 */
	@RequestMapping("/emailNotarizeForCode")
	@ResponseBody
	public String emailNotarizeForCode(@RequestParam(value="a",required=false) String uuid,
			                           @RequestParam(value="b",required=false) String email,
			                           @RequestParam(value="c",required=false) String activationKey) {
		logger.info("method=emailNotarize(),uuid={},email={},activationKey={}",uuid,email,activationKey);
		
		if(StringUtils.isEmpty(uuid) || StringUtils.isEmpty(email) || StringUtils.isEmpty(activationKey)
				|| uuid.length()<32) return "home/home";
		
		
		User user = userToRedisService.findByUuid(uuid);
		if(null==user) return "home/home";
		if(user.isState()) return "home/home";
		
		String remail = user.getEmail();
		String code = user.getActivationKey();
		if((!email.equals(remail)) || (!code.equals(activationKey))) {
			return "register/emailVerifyFail";
		}
		user.setState(true);
		user.setLastModifyDate(new Date());
		user.setVersion(user.getVersion()+1);
		
		userToRedisService.updateForAsync(user);
		userService.updateForAsync(user);
		
		return "register/emailVerifySuccess";
	}
	
	/** 邮箱验证码验证  ajax */
	@RequestMapping("/emailNotarizeForAjax")
	@ResponseBody
	public Map<String,Object> emailNotarizeForAjax(@RequestParam(value="a",required=false) String uuid,
			                           @RequestParam(value="b",required=false) String email,
			                           @RequestParam(value="c",required=false) String activationKey) {
		logger.info("method=emailNotarize(),uuid={},email={},activationKey={}",uuid,email,activationKey);
		
		Map<String,Object> result = new HashMap<>();
		
		if(StringUtils.isEmpty(uuid) || StringUtils.isEmpty(email) || StringUtils.isEmpty(activationKey)
				|| uuid.length()<32) {
			result.put("fail", true);
			result.put("msg", "数据异常");
			return result;
		}
		
		User user = userToRedisService.findByUuid(uuid);
		if(null==user) {
			result.put("inexistence", true);
			result.put("msg", "数据异常");
			return result;
		}
		if(user.isState()) {
			result.put("active", true);
			result.put("msg", "您的账号已经处于激活状态");
			return result;
		}
		
		String code = userToRedisService.getActivationKey(uuid+"-ActivationKey");
		if(StringUtils.isEmpty(code)){
			result.put("overdue", true);
			result.put("msg", "激活码已过期。");
			return result;
		}
		if(!code.equals(activationKey)) {
			result.put("fail", true);
			result.put("msg", "激活码错误");
			return result;
		}
		
		String remail = user.getEmail();
		if (!email.equals(remail)) {
			result.put("fail", true);
			result.put("msg", "数据异常");
			return result;
		}
		
		user.setState(true);
		user.setLastModifyDate(new Date());
		user.setVersion(user.getVersion()+1);
		
		userToRedisService.updateForAsync(user);
		userService.updateForAsync(user);
		
		result.put("success", true);
		result.put("msg", "您成功激活了账号");
		result.put("id", user.getId());
		result.put("email", email);
		return result;
	}
	
	
	
	/** 邮箱登录 ajax */
	@RequestMapping("/loginMitEmaiForAjax")
	@ResponseBody
	public Map<String, Object> loginMitEmaiForAjax(@Valid LoginMitEmailBean lmeb,Errors errors,Model model) {
		logger.info("method=loginMitEmaiForAjax(),lmeb={}",lmeb);
		Map<String, Object> result = new HashMap<>();
		
		if(errors.hasErrors()) {
			List<FieldError> fieldErrors = errors.getFieldErrors();
			for(FieldError error :fieldErrors) {
				logger.error("method=loginMitEmaiForAjax(),error={}",error);
			}
			result.put("error", true);
			result.put("msg", "数据异常");
			return result;
		}
		
		User user = null;
		try {
			user = userToRedisService.findByEmail(lmeb.getUsername());
		} catch (Exception e) {
			logger.error("",e);
			result.put("error", true);
			result.put("msg", "系统异常");
			return result;
		}
		if(user==null) {
			result.put("inexistence", true);
			result.put("msg", "此账号不存在");
			return result;
		}
		
		String rawPassword = lmeb.getPassword();
		String encodedPassword = bcryptPasswordEncoder.encode(lmeb.getPassword());
		if (!bcryptPasswordEncoder.matches(rawPassword, encodedPassword)) {
			result.put("pwdError", true);
			result.put("msg", "密码错误");
			return result;
		}
		
		if(!user.isState()) {
			result.put("unable", true);
			result.put("msg", "您的账号还没激活。");
			return result;
		}
		
		// 加入session
//		model.addAttribute("id", user.getId());
//		model.addAttribute("email", user.getEmail());
		
		result.put("success", true);
		result.put("msg", "登录成功");
		result.put("id", user.getId());
		result.put("email", user.getEmail());
		
		return result;
	}
	
	
	/** 邮箱登录 */
	@RequestMapping("/loginMitEmail")
	public String loginMitEmail(@Valid LoginMitEmailBean lmeb,Errors errors,Model model) {
		logger.info("method=loginMitEmail(),lmeb={}",lmeb);
		
		if(errors.hasErrors()) {
			List<FieldError> fieldErrors = errors.getFieldErrors();
			for(FieldError error :fieldErrors) {
				logger.error("method=loginMitEmail(),error={}",error);
			}
			return "login/loginFail";
		}
		
		User user = null;
		try {
			user = userToRedisService.findByEmail(lmeb.getUsername());
		} catch (Exception e) {
			logger.error("",e);
			return "login/loginFail";
		}
		if(user==null) {
			return "login/loginFail";
		}
		
		String rawPassword = lmeb.getPassword();
		String encodedPassword = bcryptPasswordEncoder.encode(lmeb.getPassword());
		if (!bcryptPasswordEncoder.matches(rawPassword, encodedPassword)) {
			return "login/loginFail";
		}
		
		if(!user.isState()) {
			return "login/loginFail";
		}
		
		model.addAttribute("id", user.getId());
		model.addAttribute("email", user.getEmail());
		
		return "redirect:/homepage";
//		return "home/home";
	}
	
	/** 退出登录 */
	@RequestMapping("/loginOut")
	public String loginOut(HttpSession session,SessionStatus sessionStatus) {
		logger.info("进入loginOut()......");
		Enumeration<String> es = session.getAttributeNames();
		while (es.hasMoreElements()) {
			String name = (String) es.nextElement();
			logger.info("method=loginOut(),session:{}","{"+name+"="+session.getAttribute(name)+"}");
		}
		sessionStatus.setComplete();
		// session.invalidate();
		return "redirect:/homepage";
//		return "home/home";
	}
	
	
	/** 重新获取激活码 */
	@RequestMapping("/getActivationKey")
	public String getActivationKey(@RequestParam(value="e",required=false)String email,Model model,Locale locale) {
        logger.info("method=getActivationKey(),email={}",email);
		
		if(StringUtils.isEmpty(email)) return "home/home";
		
		User user = userToRedisService.findByEmail(email);
		if (user==null) return "home/home";
		
		String activationKey = StringRandomUtil.getStringRandom(6);
		userToRedisService.setActivationKey(user.getUuid()+"-ActivationKey",activationKey);
		String url = "http://192.168.0.109:8090/customClothing/loginRequest/emailNotarizeForUrl?a="+user.getUuid()+"&b="+email;
		
		try {
			sendEmailUtil.sendMail(email, "", "",locale,activationKey,url);
		} catch (Exception e) {
			return "home/home";
		}
		
		model.addAttribute("remail", email);
		model.addAttribute("uuid", user.getUuid());
		
		return "register/emailVerify";
	}
	
	
}
