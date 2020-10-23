package zhongchiedu.app.controller.school;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.school.pojo.IndexSetting;
import zhongchiedu.school.pojo.SiteTemplate;
import zhongchiedu.school.pojo.WebMenu;
import zhongchiedu.school.service.IndexSettingService;
import zhongchiedu.school.service.SiteTemplateService;
import zhongchiedu.school.service.WebMenuService;
import zhongchiedu.system.log.annotation.SystemControllerLog;

@Controller
@RequestMapping("/school")
@Slf4j
public class IndexSettingController {

	private @Autowired IndexSettingService indexSettingService;

	private @Autowired WebMenuService webMenuService;

	private @Autowired SiteTemplateService siteTemplateService;

	@GetMapping("indexSettings")
	@RequiresPermissions(value = "indexSettings:list")
	@SystemControllerLog(description = "查询所有首页配置")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
		// 分页查询数据
		log.info("查询所有首页配置");
		Pagination<IndexSetting> pagination = this.indexSettingService.findPagination(pageNo, pageSize);
		model.addAttribute("pageList", pagination);

		return "school/indexSetting/list";
	}

	/**
	 * 跳转到添加页面
	 */
	@GetMapping("/indexSetting")
	 @RequiresPermissions(value = "indexSettings:add")
	public String addIndexSettingPage(Model model) {

		List<WebMenu> webMenus = this.webMenuService.findWebMenu("0",false, 0);
		model.addAttribute("webMenus", webMenus);

		return "school/indexSetting/add";
	}

	/**
	 * 
	 * <p>
	 * Title: toeditPage
	 * </p>
	 * <p>
	 * Description:跳转到编辑页面
	 * </p>
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/indexSetting/{id}")
	@RequiresPermissions(value = "indexSettings:edit")
	@SystemControllerLog(description = "编辑首页配置")
	public String toeditPage(@PathVariable String id, Model model) {

		List<WebMenu> webMenus = this.webMenuService.findWebMenu("0",false, 0);
		model.addAttribute("webMenus", webMenus);
		
		
		IndexSetting indexSetting = this.indexSettingService.findOneById(id, IndexSetting.class);
		model.addAttribute("indexSetting", indexSetting);
		model.addAttribute("parentId", indexSetting.getWebMenu().get(0).getParentId());
		
		List<WebMenu> firstLevel = this.webMenuService.findWebMenu(indexSetting.getWebMenu().get(0).getParentId(), false,1);
		model.addAttribute("firstLevel", firstLevel);

		// 获取当前所有的一级菜单根据indexSetting.firstLevel
		List<WebMenu> secondLevel = this.webMenuService.findWebMenuByFirstLevel(indexSetting.getWebMenu().get(0).getFirstLevel());
		model.addAttribute("secondLevel", secondLevel);

		return "school/indexSetting/add";

	}

	@DeleteMapping("/indexSetting/{id}")
	@RequiresPermissions(value = "indexSettings:delete")
	@SystemControllerLog(description = "删除首页配置")
	public String delete(@PathVariable String id) {
		log.info("删除" + id);
		this.indexSettingService.delete(id);
		log.info("删除" + id + "成功");
		return "redirect:/school/indexSettings";
	}

	@PostMapping("/indexSetting")
	@RequiresPermissions(value = "indexSettings:add")
	@SystemControllerLog(description = "添加首页配置")
	public String addIndexSetting(@ModelAttribute("indexSetting") IndexSetting indexSetting) {
		this.indexSettingService.saveOrUpdate(indexSetting);
		return "redirect:/school/indexSettings";
	}

	@PutMapping("/indexSetting")
	@RequiresPermissions(value = "indexSettings:edit")
	@SystemControllerLog(description = "修改首页配置")
	public String editIndexSetting(@ModelAttribute("indexSetting") IndexSetting indexSetting) {
		this.indexSettingService.saveOrUpdate(indexSetting);
		return "redirect:/school/indexSettings";
	}

	@RequestMapping(value = "/indexSetting/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult toDisable(@RequestParam(value = "id", defaultValue = "") String id) {
		return this.indexSettingService.toDisable(id);
	}

	@RequestMapping(value = "/indexSetting/edit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult edit(@RequestParam(value = "id", defaultValue = "") String id, String sort) {

		return this.indexSettingService.findAndEditIndexSetting(id, sort);
	}

}
