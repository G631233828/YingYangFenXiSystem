package zhongchiedu.system.service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.system.pojo.MultiMedia;

public interface MultiMediaService extends GeneralService<MultiMedia> {

	
	public List<MultiMedia> uploadPictures(MultipartFile[] file,String dir,String path,String belong,int width,int height);
	
	 List<MultiMedia> uploadPictures(MultipartFile[] file,String dir,String path,String belong);
}
