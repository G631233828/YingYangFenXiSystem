package zhongchiedu.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import zhongchiedu.commons.utils.Common;
import zhongchiedu.commons.utils.Contents;
import zhongchiedu.system.pojo.SysUser;
import zhongchiedu.system.service.SysUserService;

@RequestMapping("/")
@Controller
public class WebLoginController {

	@Autowired
	private SysUserService sysUserService;

	@PostMapping("/user/login")
	public String tologin(@RequestParam(value = "userName", defaultValue = "") String userName,
			@RequestParam(value = "passWord", defaultValue = "") String passWord,
			@RequestParam(value = "menuId", defaultValue = "") String menuId,
			@RequestParam(value = "newsId", defaultValue = "") String newsId,
			Model model,HttpSession session) {

		SysUser user = this.sysUserService.findUserByUserNamePassword(userName, passWord);
		if(Common.isNotEmpty(user)) {
			if(user.getIsDelete()||user.getIsDisable()) {
				model.addAttribute("error", "当前登录账号已经被锁定，请联系管理员");
				return "websites/fushanweb/login";
			}
			session.setAttribute(Contents.WEBSYSUSER, user);
			if(Common.isNotEmpty(menuId)) {
				//跳转新闻列表
				return "redirect:/web/list/"+menuId;
				
			}else if(Common.isNotEmpty(newsId)) {
				//详细新闻
				return "redirect:/web/news/"+newsId;
			}
		}
		return "websites/fushanweb/login";
	}

}
