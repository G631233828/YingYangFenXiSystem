package zhongchiedu.app.controller.school;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.commons.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.school.pojo.News;
import zhongchiedu.school.pojo.SiteTemplate;
import zhongchiedu.school.pojo.WebMenu;
import zhongchiedu.school.service.NewsService;
import zhongchiedu.school.service.SiteTemplateService;
import zhongchiedu.school.service.WebMenuService;
import zhongchiedu.system.log.annotation.SystemControllerLog;

@Controller
@RequestMapping("/school")
@Slf4j
public class NewsController {

	private @Autowired SiteTemplateService siteTemplateService;

	private @Autowired WebMenuService webMenuService;

	private @Autowired NewsService newsService;

	@GetMapping("newses")
    @RequiresPermissions(value = "news:list")
	@SystemControllerLog(description = "跳转文章界面")
	public String list(HttpSession session) {
		// 获取站点模板
		SiteTemplate siteTemplate = this.siteTemplateService.findOneByQuery(new Query(), SiteTemplate.class);
		WebMenu webMenu = siteTemplate.getWebMenu() != null ? siteTemplate.getWebMenu() : null;
		if (Common.isNotEmpty(webMenu)) {
			List<WebMenu> webMenus = this.webMenuService.findWebMenu(webMenu.getId(),false, null);
			if (webMenus.size() > 0) {
				session.setAttribute("webMenus", webMenus);
			}

		}
		return "school/mainWeb/list_index";
	}

	@GetMapping("findNews")
	@RequiresPermissions(value = "news:list")
	@SystemControllerLog(description = "跳转文章界面")
	public String findNews(String webMenuId, @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
			Model model, @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
		
		WebMenu webMenu = this.webMenuService.findOneById(webMenuId, WebMenu.class);

		Pagination<News> pagination = this.newsService.findPagination(webMenuId, pageNo, pageSize);
		if (pagination == null)
			pagination = new Pagination<News>();

		model.addAttribute("pageList", pagination);
		model.addAttribute("webMenuId", webMenuId);
		model.addAttribute("webMenu", webMenu);

		return "school/mainWeb/list";
	}
	
	
	
	
	@GetMapping("/addnews/{webMenuId}")
	@RequiresPermissions(value = "news:add")
	@SystemControllerLog(description = "跳转添加文章页面")
	public String toaddNews(Model model,@PathVariable (name = "webMenuId")String webMenuId) {
		
		WebMenu webMenu = this.webMenuService.findOneById(webMenuId, WebMenu.class);

		model.addAttribute("webMenuId", webMenuId);
		model.addAttribute("webMenu", webMenu);

		return "school/mainWeb/add";
	}
	
	@GetMapping("/editnews/{id}")
	@RequiresPermissions(value = "news:edit")
	@SystemControllerLog(description = "跳转编辑文章页面")
	public String toeditNews(Model model,@PathVariable (name = "id")String id) {
		
		News news = this.newsService.findOneById(id, News.class);
		
		model.addAttribute("webMenu", news.getWebMenu());
		model.addAttribute("news", news);
		return "school/mainWeb/add";
	}
	
	
	
	@Value("${upload.imgpath}")
	private String imgpath;
	@Value("${upload.savedir}")
	private String dir;
	
	@PostMapping("/news")
	@RequiresPermissions(value = "news:add")
	@SystemControllerLog(description = "添加文章")
	public String addNews(HttpServletRequest request, @ModelAttribute("news") News news,
			@RequestParam("filenews")MultipartFile[] filenews,String editorValue,
			@RequestParam(defaultValue="",value="oldnewsImg")String oldnewsImg
			) {
		this.newsService.SaveOrUpdateNews(news,filenews,oldnewsImg,imgpath,dir,editorValue);
		return "redirect:/school/findNews?webMenuId="+news.getWebMenu().getId();
	}
	
	@PutMapping("/news")
	@RequiresPermissions(value = "news:edit")
	@SystemControllerLog(description = "编辑文章")
	public String editNews(HttpServletRequest request, @ModelAttribute("news") News news,
			@RequestParam("filenews")MultipartFile[] filenews,String editorValue,
			@RequestParam(defaultValue="",value="oldnewsImg")String oldnewsImg
			) {
		this.newsService.SaveOrUpdateNews(news,filenews,oldnewsImg,imgpath,dir,editorValue);
		return "redirect:/school/findNews?webMenuId="+news.getWebMenu().getId();
	}
	
	
	
	
	@DeleteMapping("/news/{id}")
	@RequiresPermissions(value = "news:delete")
	@SystemControllerLog(description = "删除")
	public String delete(@PathVariable String id) {
		News news = this.newsService.findOneById(id, News.class);
		log.info("删除" + id);
		this.newsService.delete(id);
		log.info("删除" + id + "成功");
		return "redirect:/school/findNews?webMenuId="+news.getWebMenu().getId();
	}
	
	
	
	
	
	@RequestMapping(value = "news/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult toDisable(@RequestParam(value = "id", defaultValue = "") String id) {
		return this.newsService.toDisable(id);
	}
	
	
	

}
