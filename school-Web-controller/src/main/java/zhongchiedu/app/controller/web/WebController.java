package zhongchiedu.app.controller.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import zhongchiedu.commons.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.school.pojo.IndexSetting;
import zhongchiedu.school.pojo.News;
import zhongchiedu.school.pojo.WebMenu;
import zhongchiedu.school.service.IndexSettingService;
import zhongchiedu.school.service.NewsService;
import zhongchiedu.school.service.PhotoGalleryService;
import zhongchiedu.school.service.WebMenuService;
import zhongchiedu.system.pojo.MultiMedia;

@RequestMapping("/web")
@Controller
public class WebController {
	
	@Autowired
	private WebMenuService webMenuService;
	@Autowired
	private NewsService newsService;

	@Autowired
	private IndexSettingService indexSettingService;
	
	@Autowired
	private PhotoGalleryService photoGalleryService;

	@RequestMapping(value = "/index")
	public String toindex(Model model) {
		List<IndexSetting> indexs = this.indexSettingService.findIndexSetting();
		model.addAttribute("indexs", indexs);
		List<News> newslist = this.newsService.findNewsByNewsIds(indexs);
		model.addAttribute("newslist", newslist);
		List<MultiMedia> imgs = this.photoGalleryService.findImgs(20);
		
		
		return "websites/fushanweb/index";
	}
	
//	@RequestMapping(value = "/index2")
//	public String toindex2(Model model) {
//		List<IndexSetting> indexs = this.indexSettingService.findIndexSetting();
//		model.addAttribute("indexs", indexs);
//		List<News> newslist = this.newsService.findNewsByNewsIds(indexs);
//		model.addAttribute("newslist", newslist);
//		return "websites/fushanweb/index";
//	}
	
//	@RequestMapping("aaa/{name}/{value}")
//	//@RequiresPermissions(value = "webmenu:edit")
//	@SystemControllerLog(description = "修改资源")
//	public String test(@PathVariable("name")String name,@PathVariable("value")String value) {
//		System.out.println(name);
//		System.out.println(value);
//		System.out.println(name);  
//		System.out.println(value);
//		System.out.println(name);
//		System.out.println(value);
//		return "redirect:/school/webMenus";
//	}
//	
	
	
	@RequestMapping(value = "/list/{menuId}")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@PathVariable(value = "menuId",required = true)String menuId) {
		//1.获取相同目录的菜单
		WebMenu webMenu = this.webMenuService.findWebMenuById(menuId);
		// type = 1 
		if(webMenu.getType()==1) {
			List<WebMenu> webMenusleft = this.webMenuService.findWebMenuByFirstLevel(webMenu.getId());
			model.addAttribute("webMenusleft", webMenusleft);
			//网页当前位置导航 
			model.addAttribute("one", webMenu);
			//获取当前模块下的所有新闻
			Pagination<News> pageList = this.newsService.findNewsBySuperMenuId(webMenu.getId(), pageNo, pageSize);
			model.addAttribute("pageList", pageList);
		}else if(webMenu.getType()== 2) {
			List<WebMenu> webMenusleft = this.webMenuService.findWebMenuByFirstLevel(webMenu.getFirstLevel());
			model.addAttribute("webMenusleft", webMenusleft);
			WebMenu one = this.webMenuService.findWebMenuById(webMenu.getFirstLevel());
			model.addAttribute("one", one);
			model.addAttribute("two", webMenu);
			Pagination<News> pageList = this.newsService.findNewsByWebMenuId(webMenu.getId(), pageNo, pageSize);
			model.addAttribute("pageList", pageList);
		}
		model.addAttribute("webMenu", webMenu);
		
	
		return "websites/fushanweb/list";
	}
	
	
	@RequestMapping(value = "/news/{newsId}")
	public String findNews(HttpSession session,HttpServletRequest request,Model model,@PathVariable(value="newsId",required = true)String newsId) {
		
		News news = this.newsService.findNewsById(newsId);
		if(Common.isNotEmpty(news)) {
			
			String ip = request.getRemoteAddr();
			String getIp = (String) session.getAttribute(ip + "_" + newsId);
			if(Common.isEmpty(getIp)) {
				//刷新浏览量
				this.newsService.updateNewsVisit(newsId);
				session.setAttribute(ip + "_" + newsId, ip + "_" + newsId);
			}
			List<WebMenu> webMenusleft = this.webMenuService.findWebMenuByFirstLevel(news.getWebMenu().getFirstLevel());
			model.addAttribute("webMenusleft", webMenusleft);
			model.addAttribute("news", news);
			model.addAttribute("two", news.getWebMenu());
			model.addAttribute("webMenu", news.getWebMenu());
			model.addAttribute("one", news.getSupMenu());
		}
		
		return "websites/fushanweb/article";
	}
	
	
	
	
	
	
	
	
	
	
	
//	@RequestMapping(value = "/list/{menuid}/{newsid}")
//	public String list(Model model,@PathVariable(value = "menuid",required = true)String menuid,@PathVariable(value ="newsid",required = false)String newsid) {
//		
//		
//		
//		//menuid 菜单 id  type =1 的菜单id  跳转到list界面
//		//newsid新闻id				        跳转到article界面
//		if(Common.isNotEmpty(newsid)) {
//			//查询新闻
//			News news = this.newsService.findNewsById(newsid);
//			model.addAttribute("news", news);
//			return "web/article";
//		}
//		else {
//			
//			
//		}
//		
//		
//		
//		return "web/index";
//	}
//	
	
	
	
//	
//	public static void main(String[] args) {
//		
//		for(int i=1;i<=10;i++) {
//			System.out.println(i%3);
//		}
//	
//	}
//	
	
	
	
	
	
	
	
	
	

}
