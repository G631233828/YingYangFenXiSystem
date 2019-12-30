/**
 * 
 */
package zhongchiedu.system.service;

import java.util.List;

import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.system.pojo.SysOperationAuthority;

/**  
* <p>Title: SysOperationAuthorityService</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2019年12月27日  
*/
public interface SysOperationAuthorityService extends GeneralService<SysOperationAuthority>{
	
	Pagination<SysOperationAuthority> findPagination(Integer pageNo,Integer pageSize);

	SysOperationAuthority findSysOperationAuthorityByName(String name);
	
	List<SysOperationAuthority> findAllSysOperationAuthorityByIsDisable();
	
	List<SysOperationAuthority> findAllSysOperationAuthority();
	
	void saveOrUpdate(SysOperationAuthority sysOperationAuthority);
	
	BasicDataResult toDisable(String id);
	
	BasicDataResult ajaxgetRepletes(String name);
	
	String delete(String id);
	
}
