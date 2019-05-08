package com.xiaoqingxin.customClothing.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.unbescape.html.HtmlEscape;

@Controller
@RequestMapping("/")
public class HomeController {
//	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value= {"/homepage"}, method=RequestMethod.GET)
	public String home(Model model) {
		return "home/home.html";
	}
	
	@RequestMapping(value="/exception", method=RequestMethod.GET)
	public String exception(Model model) {
		return "home/exception.html";
	}
	
	/** Error page. */
    @RequestMapping("/403")
    public String forbidden() {
        return "home/403";
    }
    
    /** Error page. */
    @RequestMapping("/exception")
    public String error(HttpServletRequest request, Model model) {
        model.addAttribute("errorCode", "Error " + request.getAttribute("javax.servlet.error.status_code"));
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("<ul>");
        while (throwable != null) {
            errorMessage.append("<li>").append(HtmlEscape.escapeHtml5(throwable.getMessage())).append("</li>");
            throwable = throwable.getCause();
        }
        errorMessage.append("</ul>");
        model.addAttribute("errorMessage", errorMessage.toString());
        return "home/exception";
    }
	
}
