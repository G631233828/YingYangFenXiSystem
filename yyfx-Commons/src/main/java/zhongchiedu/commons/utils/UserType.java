/**
 * 
 */
package zhongchiedu.commons.utils;

import java.io.Serializable;

/**  
* <p>Title: UserType</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月15日  
*/
public class UserType implements Serializable{
	
	/** serialVersionUID*/  
	private static final long serialVersionUID = 8857456797452285570L;

	/**
	 * 系统用户
	 */
	public static final String SYSTEM="system";
	
	/**
	 * 学校管理员
	 */
	public static final String SCHOOL_ADMIN="school_admin";
	
	/**
	 * 学校用户
	 */
	public static final String SCHOOL_USER="school_user";

}
