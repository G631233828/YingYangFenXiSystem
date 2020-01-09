package zhongchiedu.system.service;

import java.util.List;

import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.system.pojo.SysResource;

/**
 * 
* <p>Title: SysResourceService</p>  
* <p>Description:功能菜单资源类 </p>  
* @author 郭建波  
* @date 2019年12月30日
 */
public interface SysResourceService  extends GeneralService<SysResource>{

	Pagination<SysResource> findPagination(Integer pageNo,Integer pageSize);
	
	/**
	 * 
	 * <p>Title: findResourceMenu</p>  
	 * <p>Description: 根据父菜单Id查询所有的二级菜单</p>  
	 * @param parentId
	 * @return
	 */
	List<SysResource> findSysResourceMenu(String parentId);
	
	List<SysResource> findAllSysResources();
	
	SysResource findSysResourceByName(String name);
	
	void saveOrUpdate(SysResource sysResource,String[] operation);
	
	BasicDataResult toDisable(String id);
	
	BasicDataResult ajaxgetRepletes(String name);
	
	String delete(String id);
	
	
}
