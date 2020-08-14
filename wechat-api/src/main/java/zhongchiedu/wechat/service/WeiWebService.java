package zhongchiedu.wechat.service;

import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.wechat.pojo.WeiWeb;

public interface WeiWebService extends GeneralService<WeiWeb>{

	public void saveOrUpdate(WeiWeb weiWeb,
			MultipartFile[] filelogo, MultipartFile[] filebananas, String imgPath, 
			 String oldLogo,String dir);
	
	
}
