package zhongchiedu.app.compent;





import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import zhongchiedu.commons.utils.Common;
import zhongchiedu.commons.utils.Contents;
import zhongchiedu.system.pojo.Role;
import zhongchiedu.system.pojo.SysRole;
import zhongchiedu.system.pojo.SysUser;
import zhongchiedu.system.pojo.User;
import zhongchiedu.system.service.RoleService;
import zhongchiedu.system.service.SysRoleService;

@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {

	private static final Logger log = LoggerFactory.getLogger(LoginHandlerInterceptor.class);
	@Autowired
	private SysRoleService sysRoleService;
	
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
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
