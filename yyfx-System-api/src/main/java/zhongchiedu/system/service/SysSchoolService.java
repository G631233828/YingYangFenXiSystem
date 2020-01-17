/**
 * 
 */
package zhongchiedu.system.service;

import java.util.List;

import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.system.pojo.SysSchool;
import zhongchiedu.system.pojo.SysSchool;

/**  
* <p>Title: SysSchoolService</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月15日  
*/
public interface SysSchoolService extends GeneralService<SysSchool>{

	Pagination<SysSchool> findPagination(Integer pageNo,Integer pageSize);

	SysSchool findSysSchoolByName(String name);
	
	List<SysSchool> findAllSysSchoolByIsDisable();
	
	List<SysSchool> findAllSysSchool();
	
	void saveOrUpdate(SysSchool sysSchool);
	
	BasicDataResult toDisable(String id);
	
	BasicDataResult ajaxgetRepletes(String name);
	
	String delete(String id);
	
}
