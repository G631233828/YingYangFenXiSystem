/**
 * 
 */
package zhongchiedu.shiro.realm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import zhongchiedu.commons.utils.Common;
import zhongchiedu.commons.utils.Contents;
import zhongchiedu.commons.utils.UserType;
import zhongchiedu.shiro.token.LoginToken;
import zhongchiedu.system.pojo.SysMenuAuthority;
import zhongchiedu.system.pojo.SysResource;
import zhongchiedu.system.pojo.SysUser;
import zhongchiedu.system.service.SysUserService;

/**
 * <p>
 * Title: SystemUserReam
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author 郭建波
 * @date 2020年1月15日
 */
public class SystemUserRealm extends AuthorizingRealm {

	@Autowired
	private SysUserService SysUserService;

	/*
	 * <p>Title: getName</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see org.apache.shiro.realm.CachingRealm#getName()
	 */
//	@Override
//	public String getName() {
//		// TODO Auto-generated method stub
//		return UserType.SYSTEM;
//	}

	/*
	 * <p>Title: doGetAuthorizationInfo</p> <p>Description: </p>
	 * 
	 * @param principals
	 * 
	 * @return
	 * 
	 * @see
	 * org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.
	 * shiro.subject.PrincipalCollection)
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
		String username = (String) principals.getPrimaryPrincipal(); // 一定是String类型，在SimpleAuthenticationInfo
		System.out.println("sys"+username);
		 if(username == null) {
			 throw new UnknownAccountException();
		 }
		
		
		
		
		if (!principals.getRealmNames().contains(getName())) return null;
		
		// 获取登录的用户名
		String accountName = SecurityUtils.getSubject().getPrincipal().toString();
		// 获取 shiro session
		if (Common.isNotEmpty(accountName)) {
			Session session = SecurityUtils.getSubject().getSession();
			SysUser suser = (SysUser) session.getAttribute(Contents.SYSUSER_SESSION);
			if (Common.isNotEmpty(suser)) {
				SysUser u = this.SysUserService.findOneById(suser.getId(), SysUser.class);
				// 权限信息对象info 用来存放查出的所有用户role以及权限permission
				SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
				//判断是否是删除或禁用的角色
				if(!u.getRole().getIsDelete()&&!u.getRole().getIsDisable()) {
					// 获取角色拥有的所有菜单权限
					List<SysResource> rs = u.getRole().getSysresource();
					List<SysMenuAuthority> sm = u.getRole().getSysMenuAuthority();
					// 设置权限名称
					info.addRole(u.getRole().getRoleName());
					
					Set<String> permissionkeys = new HashSet<String>();

					for (SysMenuAuthority s : sm) {
						permissionkeys.add(s.getResKey());
					}
					for (SysResource r : rs) {
						permissionkeys.add(r.getResKey());
					}

					info.addStringPermissions(permissionkeys);
				}
				
			

				return info;
			}

		}
		return null;

	}

	/*
	 * <p>Title: doGetAuthenticationInfo</p> <p>Description: </p>
	 * 
	 * @param token
	 * 
	 * @return
	 * 
	 * @throws AuthenticationException
	 * 
	 * @see
	 * org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache
	 * .shiro.authc.AuthenticationToken)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 获取用户的名称
		//UsernamePasswordToken usertoken = (UsernamePasswordToken) token;
		
		LoginToken usertoken = (LoginToken) token;
		SysUser suser = null;
		if(usertoken.getLoginType().equals(UserType.SYSTEM)) {
			suser = this.SysUserService.findSysUserByAccountName(usertoken.getUsername(),UserType.SYSTEM);
		}else if(usertoken.getLoginType().equals(UserType.SCHOOL_ADMIN)) {
			suser = this.SysUserService.findSysUserByAccountName(usertoken.getUsername(),UserType.SCHOOL_ADMIN);
		}else if(usertoken.getLoginType().equals(UserType.SCHOOL_USER)){
			suser = this.SysUserService.findSysUserByAccountName(usertoken.getUsername(),UserType.SCHOOL_USER);
		}else {
			throw new UnknownAccountException();
		}
		if(Common.isNotEmpty(suser)) {
			Session session = SecurityUtils.getSubject().getSession();
			SysUser u = this.SysUserService.findOneById(suser.getId(), SysUser.class);
			if (u.getIsDisable()) {
				throw new DisabledAccountException();
			}
		
			if (Common.isNotEmpty(u)) {
				session.setAttribute(Contents.SYSUSER_SESSION, suser);
				if(!u.getRole().getIsDelete()&&!u.getRole().getIsDisable()) {
					session.setAttribute(Contents.SYSRESOURCE_SESSION, Common.isNotEmpty(u.getRole())?u.getRole().getSysresource():"");
				}
				
				return new SimpleAuthenticationInfo(suser.getAccountName(), suser.getPassWord(), getName());
			}
		}
		return null;

	}

}
