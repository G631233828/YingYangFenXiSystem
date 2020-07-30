package zhongchiedu.app.controller.wechat;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.commons.utils.Common;
import zhongchiedu.system.log.annotation.SystemControllerLog;
import zhongchiedu.wechat.pojo.WeChatMenu;
import zhongchiedu.wechat.service.WeChatMenuService;


@Controller
@RequestMapping("/wechat")
public class WeChatMenuController {

	private static final Logger log = LoggerFactory.getLogger(WeChatMenuController.class);

	@Autowired
	private WeChatMenuService weChatMenuService;

	@GetMapping("/wechatmenus")
	@RequiresPermissions(value = "wechatmenu:list")
	@SystemControllerLog(description = "查询微信菜单")
	public String weChatmenus(Model model, HttpSession session) {
		List<WeChatMenu> list = this.weChatMenuService.findWeChatMenus();
		model.addAttribute("weChatMenu", list);
		if (Common.isNotEmpty(list)) {
			int size = 0;
			for (WeChatMenu m : list) {
				if (m.getParentId().equals("0")) {
					size++;
				}
			}
			model.addAttribute("supMenuSize", size);
		}

		return "wechat/wechatMenu/list";
	}

	@PostMapping("/wechatmenu")
	@RequiresPermissions(value = "wechatmenu:add")
	@SystemControllerLog(description = "添加微信菜单")
	public String addWeChatMenu(@ModelAttribute("weChatmenu") WeChatMenu weChatmenu) {

		this.weChatMenuService.saveOrUpdate(weChatmenu);

		return "redirect:/wechat/wechatmenus";
	}

	@RequestMapping("/wechatmenu/editMenu")
	@RequiresPermissions(value = "wechatmenu:edit")
	@SystemControllerLog(description = "微信子菜单编辑")
	@ResponseBody
	public BasicDataResult editMenu(@RequestParam(value = "id", defaultValue = "") String id) {

		return this.weChatMenuService.findWeChatMenuByid(id);

	}

	@DeleteMapping("/wechatmenu/{id}")
	@RequiresPermissions(value = "weChatmenu:delete")
	@SystemControllerLog(description = "删除菜单")
	public String delete(@PathVariable String id) {

		this.weChatMenuService.deleteWeChatMenuById(id);

		return "redirect:/wechat/wechatmenus";
	}

	@RequestMapping("/wechatmenu/release")
	@RequiresPermissions(value = "wechatmenu:release")
	@SystemControllerLog(description = "菜单发布")
	@ResponseBody
	public BasicDataResult release(HttpSession session) {
		
		BasicDataResult result = this.weChatMenuService.release();
		
		return result;
	}
	
	
	

}
