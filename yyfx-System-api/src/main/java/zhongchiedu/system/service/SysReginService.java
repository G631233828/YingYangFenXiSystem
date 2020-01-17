/**
 * 
 */
package zhongchiedu.system.service;

import java.util.List;

import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.system.pojo.SysRegin;
import zhongchiedu.system.pojo.SysRole;

/**  
* <p>Title: SysReginService</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月14日  
*/
public interface SysReginService  extends GeneralService<SysRegin>{

	Pagination<SysRegin> findPagination(Integer pageNo,Integer pageSize);

	SysRegin findSysReginByName(String name);
	
	List<SysRegin> findAllSysReginByIsDisable();
	
	List<SysRegin> findAllSysRegin();
	
	void saveOrUpdate(SysRegin sSysRegin);
	
	BasicDataResult toDisable(String id);
	
	BasicDataResult ajaxgetRepletes(String name);
	
	String delete(String id);
	
}
