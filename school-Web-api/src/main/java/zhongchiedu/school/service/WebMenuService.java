package zhongchiedu.school.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.WebMenu;



/**
 * 
* <p>Title: WebMenuService</p>  
* <p>Description:网站菜单 </p>  
* @author 郭建波  
* @date 2019年12月30日
 */

public interface WebMenuService  extends GeneralService<WebMenu>{

	Pagination<WebMenu> findPagination(Integer pageNo,Integer pageSize);
	
	List<WebMenu> findWebMenu(String parentId,boolean weiWeb,Integer type);
	
	void saveOrUpdate(WebMenu webMenu,MultipartFile[] img,String oldImg,String imgPath,String dir);
	
	BasicDataResult toDisable(String id);
	
	String delete(String id);
	
	BasicDataResult ajaxgetRepletes(String name);
	
	WebMenu findWebMenuByName(String name);
	
	WebMenu findWebMenuById(String id);
	
	List<WebMenu> findWebMenuByFirstLevel(String id);
	
	
	
}
