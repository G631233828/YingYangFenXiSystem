///**
// * 
// */
//package zhongchiedu.system.service;
//
//import java.util.List;
//
//import org.springframework.web.multipart.MultipartFile;
//
//import zhongchiedu.commons.utils.BasicDataResult;
//import zhongchiedu.framework.pagination.Pagination;
//import zhongchiedu.framework.service.GeneralService;
//import zhongchiedu.system.pojo.SysSchoolAccount;
//import zhongchiedu.system.pojo.SysUser;
//
///**  
//* <p>Title: SysSchoolAccountService</p>  
//* <p>Description: </p>  
//* @author 郭建波  
//* @date 2020年1月17日  
//*/
//public interface SysSchoolAccountService extends GeneralService<SysUser>{
//	
//	Pagination<SysUser> findPagination(Integer pageNo,Integer pageSize);
//
//	SysUser findSysSchoolAccountByAccountName(String accountName);
//	
//	List<SysUser> findAllSysSchoolAccountByIsDisable();
//	
//	List<SysUser> findAllSysSchoolAccount();
//	
//	void saveOrUpdate(SysUser sysUser,String roleId ,MultipartFile[] file ,String imgPath,String dir,String oldheadImg);
//	
//	BasicDataResult toDisable(String id);
//	
//	BasicDataResult ajaxgetRepletes(String name);
//	
//	String delete(String id,SysUser suser); 
//	
//	BasicDataResult checkPassword(String id,String password);
//
//	BasicDataResult editPassword(String id,String password);
//
//}
