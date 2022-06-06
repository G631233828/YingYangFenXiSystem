/**
 * 
 */
package zhongchiedu.system.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.system.pojo.ProcessInfo;
import zhongchiedu.system.pojo.SysResource;
import zhongchiedu.system.pojo.SysUser;

/**  
* <p>Title: SysUserService</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月9日  
*/
public interface SysUserService extends GeneralService<SysUser>{
	
	Pagination<SysUser> findPagination(SysUser user,Integer pageNo,Integer pageSize);

	SysUser findSysUserByAccountName(String accountName,String userType);
	
	List<SysUser> findAllSysUserByIsDisable();
	
	List<SysUser> findAllSysUser();
	
	void saveOrUpdate(SysUser sysUser,String roleId ,MultipartFile[] file ,String imgPath,String dir,String oldheadImg,SysUser sessionUser);
	
	BasicDataResult toDisable(String id);
	
	BasicDataResult ajaxgetRepletes(String name);
	
	String delete(String id,SysUser suser);
	
	BasicDataResult checkPassword(String id,String password);

	BasicDataResult editPassword(String id,String password);

	List<SysResource> findAllSysResources(SysUser user);
	
	SysUser findUserByUserNamePassword(String userName,String passWord);
	
	 ProcessInfo findproInfo(HttpServletRequest request);
	 
	  String upload(HttpServletRequest request, HttpSession session);
}
