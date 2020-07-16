/**
 * 
 */
package zhongchiedu.system.service;

import java.util.List;

import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.system.pojo.SysUnit;

/**  
* <p>Title: SysUnitService</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月9日  
*/
public interface SysUnitService extends GeneralService<SysUnit>{
	
	Pagination<SysUnit> findPagination(Integer pageNo,Integer pageSize);

	SysUnit findSysUnitByName(String name);
	
	List<SysUnit> findAllSysUnitByIsDisable();
	
	List<SysUnit> findAllSysUnit();
	
	void saveOrUpdate(SysUnit sysUnit);
	
	BasicDataResult toDisable(String id);
	
	BasicDataResult ajaxgetRepletes(String name);
	
	String delete(String id);


}
