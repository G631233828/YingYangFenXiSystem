package zhongchiedu.app.controller.wechat;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.system.log.annotation.SystemControllerLog;
import zhongchiedu.wechat.pojo.WeChatSetting;
import zhongchiedu.wechat.service.WeChatSettingService;

@Controller
@RequestMapping("/wechat")
@Slf4j
public class WeChatSettingController {

	@Autowired
	private WeChatSettingService weChatSettingService;
	
	@RequestMapping("/findWeChatSettings")
	//@RequiresPermissions(value = "wechatSetting:edit")
	@SystemControllerLog(description = "查询微信配置")
	public String findWeChatSetting(Model model) {
		WeChatSetting wechatSetting = this.weChatSettingService.findOneByQuery(new Query(),WeChatSetting.class );	
		model.addAttribute("wechatSetting", wechatSetting);
		
		return "wechat/wechatSetting/add";
	}
	
	@RequestMapping("/weChatSetting")
	//@RequiresPermissions(value = "settings:edit")
	@SystemControllerLog(description = "编辑微信设置")
	public String addOrUpdateWeChatSetting(@ModelAttribute("weChatSetting")WeChatSetting weChatSetting) {
		
		
		this.weChatSettingService.saveOrUpdate(weChatSetting);;
		
		return "redirect:/wechat/findWeChatSettings";
		
	}
	
	
}
