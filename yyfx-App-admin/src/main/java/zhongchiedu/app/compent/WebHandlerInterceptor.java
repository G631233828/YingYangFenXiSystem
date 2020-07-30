package zhongchiedu.app.compent;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import zhongchiedu.commons.utils.Common;
import zhongchiedu.commons.utils.Contents;
import zhongchiedu.school.pojo.Settings;
import zhongchiedu.school.pojo.SiteTemplate;
import zhongchiedu.school.pojo.WebMenu;
import zhongchiedu.school.service.SettingsService;
import zhongchiedu.school.service.SiteTemplateService;
import zhongchiedu.school.service.WebMenuService;
import zhongchiedu.system.pojo.Role;
import zhongchiedu.system.pojo.SysRole;
import zhongchiedu.system.pojo.SysUser;
import zhongchiedu.system.pojo.User;
import zhongchiedu.system.service.RoleService;
import zhongchiedu.system.service.SysRoleService;

/**
 * 前台拦截器， 可以添加黑名单
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
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		log.info("请求的uri："+request.getRequestURI()+",请求的IP："+Common.getHostIp());
		
		List<WebMenu> webMenus = (List<WebMenu>) session.getAttribute(Contents.WEBMENU);
		
		if(Common.isEmpty(webMenus)||webMenus.size()<=0) {
			//从数据库查询菜单
			SiteTemplate site = this.siteTemplateServie.findOneByQuery(new Query(), SiteTemplate.class);
			if (Common.isNotEmpty(site) && Common.isNotEmpty(site.getWebMenu())) {
				WebMenu menu = site.getWebMenu();
				// 通过webmenu获取所有的菜单
				 webMenus = this.webMenuService.findWebMenu(menu.getId(), null);
				 session.setAttribute(Contents.WEBMENU, webMenus);
			}
		}
		  
		Settings settings = this.settingsService.findOneByQuery(new Query(), Settings.class);
		session.setAttribute(Contents.SETTINGS, settings);
		
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
