/**
 * 
 */
package zhongchiedu.system.service;

import java.util.List;

import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.system.pojo.SysOperationAuthority;
import zhongchiedu.system.pojo.SysResource;
import zhongchiedu.system.pojo.SysRole;

/**  
* <p>Title: SysRoleService</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月8日  
*/
public interface SysRoleService  extends GeneralService<SysRole>{

	Pagination<SysRole> findPagination(Integer pageNo,Integer pageSize);

	SysRole findSysRoleByName(String name);
	
	List<SysRole> findAllSysRoleByIsDisable();
	
	List<SysRole> findAllSysRole();
	
	void saveOrUpdate(SysRole sysRole);
	
	BasicDataResult toDisable(String id);
	
	BasicDataResult ajaxgetRepletes(String name);
	
	String delete(String id);
	
	BasicDataResult author(String id,String checkallPermission);

	BasicDataResult getAuthor(String id);
	
	
	
	
	
	
	
	
	
}
