/**
 * 
 */
package zhongchiedu.system.service;

import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.system.pojo.SysMenuAuthority;
import zhongchiedu.system.pojo.SysOperationAuthority;
import zhongchiedu.system.pojo.SysResource;

/**  
* <p>Title: SysMenuAuthorityService</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2019年12月31日  
*/
public interface SysMenuAuthorityService extends GeneralService<SysMenuAuthority> {

	
	void saveOrUpdate(SysMenuAuthority sysMenuAuthority);
	
	
	SysMenuAuthority findSysMenuAuthority(SysOperationAuthority sop,String sysResourceId);	
	
	SysMenuAuthority findSysMenuAuthority(String sysOperationAuthorityId,String sysResourceId);	
}
