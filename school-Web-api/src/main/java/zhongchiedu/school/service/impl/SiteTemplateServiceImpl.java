package zhongchiedu.school.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.Common;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.SiteTemplate;
import zhongchiedu.school.service.SiteTemplateService;
/**  
* <p>Title: SiteTemplateServiceImpl</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2019年12月30日  
*/
@Service
@Slf4j
public class SiteTemplateServiceImpl  extends GeneralServiceImpl<SiteTemplate> implements SiteTemplateService{@Override
	public void saveOrUpdate(SiteTemplate siteTemplate) {
	
	SiteTemplate getSiteTemplate = null;
	if(Common.isNotEmpty(siteTemplate.getId())) {
		getSiteTemplate = this.findOneById(siteTemplate.getId(), SiteTemplate.class);
	}
	if(Common.isNotEmpty(getSiteTemplate)) {
		BeanUtils.copyProperties(siteTemplate, getSiteTemplate);
		this.save(getSiteTemplate);
	}else {
		this.insert(siteTemplate);
	}
	
}

}
