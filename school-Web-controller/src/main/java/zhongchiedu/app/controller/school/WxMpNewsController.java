package zhongchiedu.app.controller.school;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.school.pojo.WxMpNews;
import zhongchiedu.school.service.WxMpNewsService;
import zhongchiedu.system.log.annotation.SystemControllerLog;

@Controller
@RequestMapping("/school")
@Slf4j
public class WxMpNewsController {
	
	@Autowired
	private WxMpNewsService wxMpNewsService;
//	@Autowired
//	private WxMpMaterialNewsGetService wxMpMaterialNewsGetService;
	
	
	@GetMapping("wxMpNews")
	@RequiresPermissions(value = "wxMpNews:list")
	@SystemControllerLog(description = "查询所有菜单")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
		// 分页查询数据
		log.info("查询所有微信新闻");
		Pagination<WxMpNews> pagination = this.wxMpNewsService.findPagination(pageNo, pageSize);
		model.addAttribute("pageList", pagination);

		return "school/wxMpNews/list";
	}

	@DeleteMapping("/wxMpNews/{id}")
	@RequiresPermissions(value = "wxMpNews:delete")
	@SystemControllerLog(description = "删除")
	public String delete(@PathVariable String id) {
		log.info("删除" + id);
		this.wxMpNewsService.delete(id);
		log.info("删除" + id + "成功");
		return "redirect:/school/wxMpNews";
	}
	
	
	@RequestMapping(value = "/wxMpNews/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult toDisable(@RequestParam(value = "id", defaultValue = "") String id) {
		return this.wxMpNewsService.toDisable(id);
	}
	
	
	
	
	@RequestMapping(value = "/wxMpNews/tongbu", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult tongbu() {
		try {
			//this.wxMpMaterialNewsGetService.getWxMpMaterialNews();
			this.wxMpNewsService.insertNews(false);	
		}catch (Exception e) {
			e.printStackTrace();
			return BasicDataResult.build(400, "同步失败", null);
		}
		return BasicDataResult.build(200, "同步成功", null);
	}
	
	
	
	
	

}
