package zhongchiedu.app.compent;





import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.Common;
import zhongchiedu.commons.utils.Contents;
import zhongchiedu.school.service.AccessStatisticsService;
import zhongchiedu.system.config.IpFilter;
import zhongchiedu.system.config.WhiteList;
import zhongchiedu.system.pojo.IpConfigs;
import zhongchiedu.system.pojo.SysRole;
import zhongchiedu.system.pojo.SysUser;
import zhongchiedu.system.service.IpConfigsService;
import zhongchiedu.system.service.SysRoleService;

@Component
@Slf4j
public class LoginHandlerInterceptor implements HandlerInterceptor {

	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private WhiteList whitelist;
	@Autowired
	private IpFilter ipFilter;
	@Autowired
	private IpConfigsService ipConfigsService;
	
	@Autowired
	private AccessStatisticsService ac;
	
	
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(ipFilter.isSystem()) {//是否开启后台ip过滤
			String ip = Common.toIpAddr(request);
			log.info("当前请求ip"+ip);
			log.info("配置列表白名单ip"+whitelist.getIps());
			Set<String> ips = new HashSet<String>();
			Set<String> getips = new HashSet<String>();
			List<IpConfigs> ipconfigs = this.ipConfigsService.findWhiteOrBlackList(false);
			if(ipconfigs.size()>0) {
				for(IpConfigs i:ipconfigs) {
					getips.add(i.getIp());
				}
			}
			ips.addAll(whitelist.getIps());
			log.info("数据库配置白名单ip"+getips);
			if(getips.size()>0)ips.addAll(getips);
			if(!ips.contains(ip)) {
				log.info("ip被禁止，请求的IP地址为"+ip+"请求的路径"+request.getRequestURI().toString());
				response.setHeader("Content-Type", "text/html;charset=utf-8");  //设置响应头的编码
				response.setCharacterEncoding("UTF-8");
				response.getWriter().append("<h1 style=\"text-align:center;\">您当前请求的ip为："+ip+"不在授权范围之内，请联系管理员！</h1>");
				return false;
				//throw new UnauthorizedException();
			}
		}
		
		HttpSession session = request.getSession();
		String urlid = request.getParameter("urlid");
		if(Common.isNotEmpty(urlid)){
			session.setAttribute(Contents.MENU_ID, urlid);
		}
		
		//修改权限之后需要去刷新用户的权限
		SysUser user = (SysUser) session.getAttribute(Contents.SYSUSER_SESSION);
		if(Common.isNotEmpty(user)){
			//获取session中所属角色的id
			SysRole sessionRole = user.getRole();
			//通过roleID 查找数据库中的role
			SysRole role = this.sysRoleService.findOneById(sessionRole.getId(), SysRole.class);
			if(sessionRole.getVersion() == role.getVersion()){
				return true;
			}else{
				SecurityUtils.getSubject().logout();
			}
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
