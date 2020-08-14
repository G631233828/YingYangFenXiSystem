package zhongchiedu.wechat.compent;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import zhongchiedu.commons.utils.Common;
import zhongchiedu.commons.utils.Contents;
import zhongchiedu.school.pojo.SiteTemplate;
import zhongchiedu.school.pojo.WebMenu;
import zhongchiedu.school.service.SiteTemplateService;
import zhongchiedu.school.service.WebMenuService;
import zhongchiedu.wechat.pojo.WeiWeb;
import zhongchiedu.wechat.service.WeiWebService;

/**
 * 前台拦截器， 可以添加黑名单
 * @author fliay
 *
 */
@Component
public class WeiWebHandlerInterceptor implements HandlerInterceptor {

	private static final Logger log = LoggerFactory.getLogger(WeiWebHandlerInterceptor.class);
	

	@Autowired
	private WebMenuService webMenuService;
	
	@Autowired
	private SiteTemplateService siteTemplateService;
	
	@Autowired
	private WeiWebService weiWebService;
	
	
	@SuppressWarnings("unchecked")
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		log.info("请求的uri："+request.getRequestURI()+",请求的IP："+Common.getHostIp());
		List<WebMenu> webMenus =  (List<WebMenu>) session.getAttribute(Contents.WEIWEBMENU);
		if(Common.isEmpty(webMenus)||webMenus.size()<=0) {
			//从数据库查询菜单
			SiteTemplate site = this.siteTemplateService.findOneByQuery(new Query(), SiteTemplate.class);
			if (Common.isNotEmpty(site) && Common.isNotEmpty(site.getWebMenu())) {
				WebMenu menu = site.getWebMenu();
				// 通过webmenu获取所有的菜单
				 webMenus = this.webMenuService.findWebMenu(menu.getId(),true, null);
				 session.setAttribute(Contents.WEIWEBMENU, webMenus);
			}
		}
		//加载微网站配置
		WeiWeb weiweb = this.weiWebService.findOneByQuery(new Query(), WeiWeb.class);
		session.setAttribute(Contents.WEIWEB, weiweb);
		
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
