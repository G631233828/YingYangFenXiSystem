/**
 * 
 */
package zhongchiedu.shiro.config;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

/**  
* <p>Title: SysUserFormAuthenticationFilter</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月16日  
*/
public class SysUserFormAuthenticationFilter  extends FormAuthenticationFilter{

	/* 
	 * <p>Title: onLoginSuccess</p>  
	 * <p>Description: </p>  
	 * @param token
	 * @param subject
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception  
	 * @see org.apache.shiro.web.filter.authc.FormAuthenticationFilter#onLoginSuccess(org.apache.shiro.authc.AuthenticationToken, org.apache.shiro.subject.Subject, javax.servlet.ServletRequest, javax.servlet.ServletResponse)  
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		System.out.println("登陆成功");
		
		
		return super.onLoginSuccess(token, subject, request, response);
	}

	
	
}
