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
import zhongchiedu.commons.utils.Contents;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.school.pojo.News;
import zhongchiedu.school.pojo.SiteTemplate;
import zhongchiedu.school.pojo.WebMenu;
import zhongchiedu.school.service.NewsService;
import zhongchiedu.school.service.SiteTemplateService;
import zhongchiedu.school.service.WebMenuService;
import zhongchiedu.system.log.annotation.SystemControllerLog;
import zhongchiedu.system.pojo.SysUser;

@Controller
@RequestMapping("/school")
@Slf4j
public class NewsController {

	private @Autowired SiteTemplateService siteTemplateService;

	private @Autowired WebMenuService webMenuService;

	private @Autowired NewsService newsService;

	@GetMapping("newses")
    @RequiresPermissions(value = "sysnewss:list")
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
	@RequiresPermissions(value = "sysnewss:list")
	@SystemControllerLog(description = "跳转文章界面")
	public String findNews(String webMenuId, @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
			Model model, @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,HttpSession session) {
		
		WebMenu webMenu = this.webMenuService.findOneById(webMenuId, WebMenu.class);

		Pagination<News> pagination = this.newsService.findPagination(webMenuId, pageNo, pageSize,session);
		if (pagination == null)
			pagination = new Pagination<News>();

		model.addAttribute("pageList", pagination);
		model.addAttribute("webMenuId", webMenuId);
		model.addAttribute("webMenu", webMenu);

		return "school/mainWeb/list";
	}
	
	
	
	
	@GetMapping("/addnews/{webMenuId}")
	@RequiresPermissions(value = "sysnewss:add")
	@SystemControllerLog(description = "跳转添加文章页面")
	public String toaddNews(Model model,@PathVariable (name = "webMenuId")String webMenuId) {
		
		WebMenu webMenu = this.webMenuService.findOneById(webMenuId, WebMenu.class);

		model.addAttribute("webMenuId", webMenuId);
		model.addAttribute("webMenu", webMenu);

		return "school/mainWeb/add";
	}
	
	@GetMapping("/editnews/{id}")
	@RequiresPermissions(value = "sysnewss:edit")
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
	@RequiresPermissions(value = "sysnewss:add")
	@SystemControllerLog(description = "添加文章")
	public String addNews(HttpServletRequest request, @ModelAttribute("news") News news,
			@RequestParam("filenews")MultipartFile[] filenews,String editorValue,
			@RequestParam(defaultValue="",value="oldnewsImg")String oldnewsImg,HttpSession session
			) {
		this.newsService.SaveOrUpdateNews(news,filenews,oldnewsImg,imgpath,dir,editorValue,session);
		return "redirect:/school/findNews?webMenuId="+news.getWebMenu().getId();
	}
	
	@PutMapping("/news")
	@RequiresPermissions(value = "sysnewss:edit")
	@SystemControllerLog(description = "编辑文章")
	public String editNews(HttpServletRequest request, @ModelAttribute("news") News news,
			@RequestParam("filenews")MultipartFile[] filenews,String editorValue,
			@RequestParam(defaultValue="",value="oldnewsImg")String oldnewsImg,HttpSession session
			) {
		this.newsService.SaveOrUpdateNews(news,filenews,oldnewsImg,imgpath,dir,editorValue,session);
		return "redirect:/school/findNews?webMenuId="+news.getWebMenu().getId();
	}
	
	
	
	
	@DeleteMapping("/news/{id}")
	@RequiresPermissions(value = "sysnewss:delete")
	@SystemControllerLog(description = "删除")
	public String delete(@PathVariable String id) {
		News news = this.newsService.findOneById(id, News.class);
		log.info("删除" + id);
		this.newsService.delete(id);
		log.info("删除" + id + "成功");
		return "redirect:/school/findNews?webMenuId="+news.getWebMenu().getId();
	}
	
	
	@GetMapping("/preview/{id}")
	public String NewsPreview(Model model,@PathVariable (name = "id")String id) {
		News news = this.newsService.findOneById(id, News.class);
		model.addAttribute("webMenu", news.getWebMenu());
		model.addAttribute("news", news);
		return "school/mainWeb/preview";
	}
	
	
	
	@RequestMapping(value = "news/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult toDisable(@RequestParam(value = "id", defaultValue = "") String id) {
		return this.newsService.toDisable(id);
	}
	
	
	/**
	 * 发布审核
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "news/release", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult toRelease(@RequestParam(value = "id", defaultValue = "") String id) {
		
		return this.newsService.toRelease(id);
	}
	
	
	
	
	
	
	@GetMapping("newsAudit")
	@RequiresPermissions(value = "audit:list")
	@SystemControllerLog(description = "查询所有待审核新闻")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {

		Pagination<News> pagination = this.newsService.findPaginationAudit(pageNo, pageSize);
		if (pagination == null)
			pagination = new Pagination<News>();

		model.addAttribute("pageList", pagination);

		return "school/audit/list";
	}
	
	

	@GetMapping("/audit")
	@RequiresPermissions(value = "audit:edit")
	@SystemControllerLog(description = "新闻审核")
	public String toaudit(String id,String type) {
		this.newsService.ToAudit(id, type);
		return "redirect:/school/newsAudit";
	}
	
	
	
	
	

}
