package zhongchiedu.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/error")
public class ErrorController {
	
	@RequestMapping("/nopage")
	public String Eroor403() {
		return "403";
	}

}
