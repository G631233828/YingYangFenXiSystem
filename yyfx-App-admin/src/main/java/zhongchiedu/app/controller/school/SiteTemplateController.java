package zhongchiedu.app.controller.school;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.Common;
import zhongchiedu.school.pojo.SiteTemplate;
import zhongchiedu.school.pojo.WebMenu;
import zhongchiedu.school.service.SiteTemplateService;
import zhongchiedu.school.service.WebMenuService;
import zhongchiedu.system.log.annotation.SystemControllerLog;

@Controller
@RequestMapping("/school")
@Slf4j
public class SiteTemplateController {

	private @Autowired SiteTemplateService siteTemplateService;
	private @Autowired WebMenuService webMenuService;
	
	
	@RequestMapping("/findSiteTemplate")
	@RequiresPermissions(value = "siteTemplate:edit")
	@SystemControllerLog(description="查询网站模板配置")
	public String findSiteTemplate(Model model) {
		
		SiteTemplate siteTemplate = this.siteTemplateService.findOneByQuery(new Query(), SiteTemplate.class);
		
		List<WebMenu> webMenus = this.webMenuService.findWebMenu("0", null);
		
		model.addAttribute("siteTemplate", siteTemplate);
		model.addAttribute("webMenus", webMenus);
		return "school/siteTemplate/add";
	}
	
	
	
	@RequestMapping("editSiteTemplate")
	@RequiresPermissions(value = "siteTemplate:edit")
	@SystemControllerLog(description="编辑网站模板配置")
	public String editSiteTemplate(SiteTemplate siteTemplate){
		if(Common.isEmpty(siteTemplate.getId())){
			siteTemplate.setId(null);
		}
		this.siteTemplateService.saveOrUpdate(siteTemplate);
		return "redirect:findSiteTemplate";
	}
	
	
	
	
	
}
