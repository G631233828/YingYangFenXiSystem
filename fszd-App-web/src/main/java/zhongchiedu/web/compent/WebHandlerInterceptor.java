package zhongchiedu.web.compent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import zhongchiedu.commons.utils.Common;
import zhongchiedu.commons.utils.Contents;
import zhongchiedu.school.pojo.News;
import zhongchiedu.school.pojo.Settings;
import zhongchiedu.school.pojo.SiteTemplate;
import zhongchiedu.school.pojo.WebMenu;
import zhongchiedu.school.service.NewsService;
import zhongchiedu.school.service.SettingsService;
import zhongchiedu.school.service.SiteTemplateService;
import zhongchiedu.school.service.WebMenuService;
import zhongchiedu.system.config.IpFilter;
import zhongchiedu.system.pojo.IpConfigs;
import zhongchiedu.system.service.IpConfigsService;

/**
 * 前台拦截器， 可以添加黑名单
 * 
 * @author fliay
 *
 */
@Component
public class WebHandlerInterceptor implements HandlerInterceptor {

	private static final Logger log = LoggerFactory.getLogger(WebHandlerInterceptor.class);

	@Autowired
	private SiteTemplateService siteTemplateServie;

	@Autowired
	private SettingsService settingsService;

	@Autowired
	private WebMenuService webMenuService;
	@Autowired
	private IpFilter ipFilter;
	@Autowired
	private NewsService newsService;
	@Autowired
	private IpConfigsService ipConfigsService;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (ipFilter.isWeb()) {
			String ip = Common.toIpAddr(request);
			log.info("当前请求ip" + ip);
			System.out.println(request.getRequestURI().toString());
			System.out.println("当前请求ip" + ip);
			List<IpConfigs> ipconfigs = this.ipConfigsService.findWhiteOrBlackList(true);
			Set<String> ips = new HashSet<String>();

			if (ipconfigs.size() > 0) {
				for (IpConfigs i : ipconfigs) {
					ips.add(i.getIp());
				}
			}
			System.out.println("黑名单列表"+ips);
			if (ips.contains(ip)) {
				System.out.println("非法ip被禁止");
				response.setHeader("Content-Type", "text/html;charset=utf-8");  //设置响应头的编码
				response.setCharacterEncoding("UTF-8");
				response.getWriter().append("<h1 style=\"text-align:center;\">您涉嫌非法访问，ip已经被禁止请于管理员联系!</h1>");
				return false;
			}

		}

		HttpSession session = request.getSession();
		log.info("请求的uri：" + request.getRequestURI() + ",请求的IP：" + Common.getHostIp());
		List<WebMenu> webMenus = (List<WebMenu>) session.getAttribute(Contents.WEBMENU);
		WebMenu version =  (WebMenu) session.getAttribute(Contents.WEBMENUVERSION);
		SiteTemplate site = this.siteTemplateServie.findOneByQuery(new Query(), SiteTemplate.class);
		if(!site.getWebMenu().getIsDisable()&&!site.getWebMenu().getIsDelete()) {

			if(Common.isNotEmpty(version)) {
				if(version.getVersion()!=site.getWebMenu().getVersion()) {
					webMenus = null;
				}
			}
			if (Common.isEmpty(webMenus) || webMenus.size() <= 0) {
				// 从数据库查询菜单
				//SiteTemplate site = this.siteTemplateServie.findOneByQuery(new Query(), SiteTemplate.class);
				if (Common.isNotEmpty(site) && Common.isNotEmpty(site.getWebMenu())) {
					WebMenu menu = site.getWebMenu();
					session.setAttribute(Contents.WEBMENUVERSION, menu);
					// 通过webmenu获取所有的菜单
					webMenus = this.webMenuService.findWebMenu(menu.getId(), false, null);
					session.setAttribute(Contents.WEBMENU, webMenus);
				}
			}

			// 热门新闻
			List<News> news = (List<News>) session.getAttribute(Contents.REMEN);

			if (Common.isEmpty(news) || news.size() <= 0) {
				Query query = new Query();
				query.addCriteria(Criteria.where("isDelete").is(false));
				query.addCriteria(Criteria.where("isDisable").is(false));
				query.with(new Sort(new Order(Direction.DESC, "views")));
				news = this.newsService.find(query, News.class);
				session.setAttribute(Contents.REMEN, news);
			}

			Settings settings = this.settingsService.findOneByQuery(new Query(), Settings.class);
			session.setAttribute(Contents.SETTINGS, settings);
		}else {
			session.setAttribute(Contents.WEBMENUVERSION, null);
		}

		return true;

	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 通过urlid查询父目录以及urlid所在的资源

	}

}
