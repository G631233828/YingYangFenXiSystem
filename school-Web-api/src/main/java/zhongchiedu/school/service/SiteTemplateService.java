package zhongchiedu.school.service;

import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.SiteTemplate;


/**
 * 
* <p>Title: WebMenuService</p>  
* <p>Description:网站配置 </p>  
* @author 郭建波  
 */
public interface SiteTemplateService extends GeneralService<SiteTemplate>{
	
	void saveOrUpdate(SiteTemplate siteTemplate);

}
