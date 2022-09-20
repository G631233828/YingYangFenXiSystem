package zhongchiedu.web.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.Common;
import zhongchiedu.commons.utils.Contents;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.school.pojo.IndexSetting;
import zhongchiedu.school.pojo.News;
import zhongchiedu.school.pojo.PhotoGallery;
import zhongchiedu.school.pojo.WebMenu;
import zhongchiedu.school.pojo.WxMpNews;
import zhongchiedu.school.service.IndexSettingService;
import zhongchiedu.school.service.NewsService;
import zhongchiedu.school.service.PhotoGalleryService;
import zhongchiedu.school.service.WebMenuService;
import zhongchiedu.school.service.WxMpNewsService;
import zhongchiedu.system.log.annotation.WebControllerLog;
import zhongchiedu.system.pojo.SysUser;
import zhongchiedu.web.config.Limit;

@RequestMapping("/")
@Controller
@Slf4j
public class WebController {

	@Autowired
	private WebMenuService webMenuService;
	@Autowired
	private NewsService newsService;

	@Autowired
	private IndexSettingService indexSettingService;

	@Autowired
	private PhotoGalleryService photoGalleryService;

	@Autowired
	private WxMpNewsService wxMpNewsService;
//	@Autowired
//	private WxMpMaterialNewsGetService wxMpMaterialNewsGetService;

	@GetMapping(value = "/")
	@WebControllerLog(description = "请求网站首页")
    @Limit(key = "limit", permitsPerSecond = 1, timeout = 500, timeunit = TimeUnit.MILLISECONDS,msg = "当前访问人数较多请等待...")
	public String index(Model model) {
		log.info("请问求网站首页");
		// this.wxMpNewsService.getWpNews();
//		this.wxMpMaterialNewsGetService.getWxMpMaterialNews();
////		WxMpNews wx = this.wxMpNewsService.findBymediaId("");
////		System.out.println(wx);
//		

		return "redirect:/web/index";
	}

//	@RequestMapping(value = "/index")
	@GetMapping(value = "web/index")
	@WebControllerLog(description = "请求网站首页")
	@Limit(key = "limit2", permitsPerSecond = 1, timeout = 500, timeunit = TimeUnit.MILLISECONDS,msg = "当前访问人数较多请等待...")
	public String toindex(Model model) {
		List<IndexSetting> indexs = this.indexSettingService.findIndexSetting();
		model.addAttribute("indexs", indexs);
		List<News> newslist = this.newsService.findNewsByNewsIds(indexs);
		model.addAttribute("newslist", newslist);
		List<PhotoGallery> imgs = this.photoGalleryService.findImgs(20);
		model.addAttribute("imgs", imgs);
		Pagination<WxMpNews> findPagination = this.wxMpNewsService.findPagination(1, 5);
		model.addAttribute("wxnews", findPagination.getDatas());

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

//	@RequestMapping("web/login")
//	public  String tologin() {
//		return
//	}

	@RequestMapping(value = "web/list/{menuId}")
	@WebControllerLog(description = "查看新闻列表")
	@Limit(key = "limit3", permitsPerSecond = 1, timeout = 500, timeunit = TimeUnit.MILLISECONDS,msg = "当前访问人数较多请等待...")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@PathVariable(value = "menuId", required = true) String menuId, HttpSession session) {
		// 1.获取相同目录的菜单
		WebMenu webMenu = this.webMenuService.findWebMenuById(menuId);
		if (webMenu.isLogin()) {
			SysUser sysUser = (SysUser) session.getAttribute(Contents.WEBSYSUSER);
			if (Common.isEmpty(sysUser)) {
				model.addAttribute("menuId", menuId);
				// 前台Session为空，跳转登录界面
				return "websites/fushanweb/login";
			}
		}
		// type = 1
		if (webMenu.getType() == 1) {
			List<WebMenu> webMenusleft = this.webMenuService.findWebMenuByFirstLevel(webMenu.getId());
			model.addAttribute("webMenusleft", webMenusleft);
			// 网页当前位置导航
			model.addAttribute("one", webMenu);
			// 获取当前模块下的所有新闻
			Pagination<News> pageList = this.newsService.findNewsBySuperMenuId(webMenu.getId(), pageNo, pageSize);
			model.addAttribute("pageList", pageList);
		} else if (webMenu.getType() == 2) {
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

	@RequestMapping(value = "web/news/{newsId}")
	@WebControllerLog(description = "查看新闻")
	@Limit(key = "limit4", permitsPerSecond = 1, timeout = 500, timeunit = TimeUnit.MILLISECONDS,msg = "当前访问人数较多请等待...")
	public String findNews(HttpSession session, HttpServletRequest request, Model model,
			@PathVariable(value = "newsId", required = true) String newsId) {

		News news = this.newsService.findNewsById(newsId);
		if (Common.isNotEmpty(news)) {
			if (news.getWebMenu().isLogin()) {
				SysUser sysUser = (SysUser) session.getAttribute(Contents.WEBSYSUSER);
				if (Common.isEmpty(sysUser)) {
					model.addAttribute("newsId", newsId);
					// 前台Session为空，跳转登录界面
					return "websites/fushanweb/login";
				}
			}

			String ip = request.getRemoteAddr();
			String getIp = (String) session.getAttribute(ip + "_" + newsId);
			if (Common.isEmpty(getIp)) {
				// 刷新浏览量
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

	// 获取图片列表
	@RequestMapping(value = "web/listImgs")
	@WebControllerLog(description = "查看图片列表")
	@Limit(key = "limit5", permitsPerSecond = 1, timeout = 500, timeunit = TimeUnit.MILLISECONDS,msg = "当前访问人数较多请等待...")
	public String listImgs(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "12") Integer pageSize) {

		// 获取所有相册
		Pagination<PhotoGallery> pageList = this.photoGalleryService.findPagination(2,pageNo, pageSize);
		model.addAttribute("pageList", pageList);

		return "websites/fushanweb/listImgs";
	}

	@RequestMapping(value = "web/imgs/{photoId}")
	@WebControllerLog(description = "查看图片")
	@Limit(key = "limit7", permitsPerSecond = 1, timeout = 500, timeunit = TimeUnit.MILLISECONDS,msg = "当前访问人数较多请等待...")
	public String findImgs(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@PathVariable(value = "photoId", required = true) String photoId) {
		// 根据相册获取所有照片
		PhotoGallery photo = this.photoGalleryService.findOneById(photoId, PhotoGallery.class);

		model.addAttribute("photo", photo);

		return "websites/fushanweb/imgs";
	}
	
	
	
	
	@RequestMapping(value = "web/wx")
	@WebControllerLog(description = "查看微信文章")
	public String findWx(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

		Pagination<WxMpNews> pageList = this.wxMpNewsService.findPagination(pageNo, pageSize);
		model.addAttribute("pageList", pageList);
		return "websites/fushanweb/wxNewsList";
	}
	
	
	
	

}
