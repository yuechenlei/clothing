package com.xiaoqingxin.customClothing.web;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.xiaoqingxin.customClothing.model.User;
import com.xiaoqingxin.customClothing.service.UserService;
import com.xiaoqingxin.customClothing.service.UserToRedisService;
import com.xiaoqingxin.customClothing.utils.StringRandomUtil;
import com.xiaoqingxin.customClothing.web.bean.UploadedFile;

@Controller
@RequestMapping({"/personal"})
@SessionAttributes(names= {"id","email","mpNumber"})
public class PersonalController {
	private static final Logger logger = LoggerFactory.getLogger(PersonalController.class);
	
	@Resource
	private UserService userService;
	@Resource
	private UserToRedisService userToRedisService;
	
	@RequestMapping({"/index"})
	public String toUserCenter(@AuthenticationPrincipal Principal principal, Model model) {
		logger.info("method=toUserCenter...");
		// model.addAttribute("username", principal.getName());
		return "userCenter/index";
	}
	
	@RequestMapping({"/myOrder"})
	public String toMyOrder(@AuthenticationPrincipal Principal principal, Model model) {
		logger.info("method=toMyOrder...");
		// model.addAttribute("username", principal.getName());
		return "userCenter/myOrder";
	}
	
	@RequestMapping({"/myInterest"})
	public String toMyInterest(@AuthenticationPrincipal Principal principal, Model model) {
		logger.info("method=toMyInterest...");
		// model.addAttribute("username", principal.getName());
		return "userCenter/myInterest";
	}
	
	@RequestMapping({"/myBodyInfo"})
	public String toMyBody(@AuthenticationPrincipal Principal principal, Model model) {
		logger.info("method=toMyBodyInfo...");
		// model.addAttribute("username", principal.getName());
		return "userCenter/myBodyInfo";
	}
	
	@RequestMapping({"/myAccount"})
	public String toMyAccount(Model model,@CookieValue(value="id",required=true)String id) {
		logger.info("method=toMyAccount...");
		User user = userService.get(id);
		model.addAttribute("generalAvatar", "/customClothing/resources/images/meinPhoto.jpg");
		if(!StringUtils.isEmpty(user.getAvatar()))
		model.addAttribute("avatar", "http://"+user.getAvatar());
		return "userCenter/myAccount";
	}
	
	@RequestMapping({"/addBodyInfo"})
	public String toAddBodyInfo(@AuthenticationPrincipal Principal principal, Model model) {
		logger.info("method=toAddBodyInfo...");
		// model.addAttribute("username", principal.getName());
		return "userCenter/addBodyInfo";
	}
	
	@RequestMapping({"/uploadAvatar"})
	@ResponseBody
	public Map<String,String> uploadAvatar(UploadedFile uploadedFile, @AuthenticationPrincipal Principal principal,Model model,
			         @CookieValue(value="id",required=true)String id) {
		logger.info("method=uploadAvatar...");
		Map<String, String> map = new HashMap<>();
		
		MultipartFile multipartFile = uploadedFile.getMultipartFile();
		String fileNameTemp = multipartFile.getOriginalFilename();
		String userName = principal.getName();
		String fileName = StringRandomUtil.getStringRandom(6)+fileNameTemp.substring(fileNameTemp.lastIndexOf("."));
		
		try {
			User user = userToRedisService.get(id);
			if(!StringUtils.isEmpty(user.getAvatar())) {
				String avatar = user.getAvatar();
				File file = new File("F:"+avatar.substring(avatar.indexOf(".com")+4));
				file.delete();
			}
			File file = new File("F:/apacheResources/Avatar/"+userName);
			file.mkdirs();
			String path = "www.xiaoqingxin.com/apacheResources/Avatar/"+userName+"/"+fileName;
			user.setAvatar(path);
			userService.updateForAsync(user);
			userToRedisService.updateForAsync(user);
			file = new File("F:/apacheResources/Avatar/"+userName,fileName);
			multipartFile.transferTo(file);
			map.put("path", path);
		} catch (IllegalStateException e) {
			logger.error("",e);
			map.put("fail", "true");
		} catch (IOException e) {
			logger.error("",e);
			map.put("fail", "true");
		}catch (Exception e) {
			logger.error("",e);
			map.put("fail", "true");
		}
		
		map.put("success", "true");
		
		return map;
		// return "userCenter/addBodyInfo";
	}

}
