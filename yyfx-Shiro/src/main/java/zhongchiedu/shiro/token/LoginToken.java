/**
 * 
 */
package zhongchiedu.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zhongchiedu.system.pojo.MultiMedia;
import zhongchiedu.system.pojo.SysRole;
import zhongchiedu.system.pojo.SysUser;

/**  
* <p>Title: LoginToken</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月15日  
*/
public class LoginToken extends  UsernamePasswordToken {

	/** serialVersionUID*/  
	private static final long serialVersionUID = -1308128021206225440L;
	
	private String loginType;

	
	/**
	 * @return the loginType
	 */
	public String getLoginType() {
		return loginType;
	}



	/**
	 * @param loginType the loginType to set
	 */
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}



	/**  
	* <p>Title: </p>  
	* <p>Description: </p>  
	* @param loginType  
	*/  
	public LoginToken(final String username,final String password,String loginType) {
		super(username,password);
		System.out.println("loginToken ---"+loginType);
		this.loginType = loginType;
	}
	
	
	

}
