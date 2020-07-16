/**
 * 
 */
package zhongchiedu.system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.commons.utils.UserType;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.system.pojo.SysMenuAuthority;
import zhongchiedu.system.pojo.SysOperationAuthority;
import zhongchiedu.system.pojo.SysResource;
import zhongchiedu.system.pojo.SysRole;
import zhongchiedu.system.pojo.SysUser;

/**  
* <p>Title: SysRoleService</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月8日  
*/
public interface SysRoleService  extends GeneralService<SysRole>{

	Pagination<SysRole> findPagination(SysUser user ,Integer pageNo,Integer pageSize);

	SysRole findSysRoleByName(String name);
	
	List<SysRole> findAllSysRoleByIsDisable(SysUser user);
	
	List<SysRole> findAllSysRole();
	
	void saveOrUpdate(SysRole sysRole,SysUser user);
	
	BasicDataResult toDisable(String id);
	
	BasicDataResult ajaxgetRepletes(String name);
	
	String delete(String id);
	
	BasicDataResult author(String id,String checkallPermission);

	BasicDataResult getAuthor(String id);
	
	BasicDataResult createSysOperationAuthority(String param);
	
	
	
	
}
