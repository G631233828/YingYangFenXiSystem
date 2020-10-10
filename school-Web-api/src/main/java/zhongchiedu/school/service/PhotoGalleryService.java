package zhongchiedu.school.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.PhotoGallery;
import zhongchiedu.system.pojo.MultiMedia;

public interface PhotoGalleryService extends GeneralService<PhotoGallery> {

	
	Pagination<PhotoGallery> findPagination(Integer pageNo, Integer pageSize);
	
	void SaveOrUpdate(PhotoGallery photoGallery);
	
	String delete(String id,String imgids);

	void updateNewsVisit(String id);
	
	PhotoGallery findPhotoGalleryById(String id);
	
	BasicDataResult ajaxFindPhotoGalleryById(String id);
	
	void photoGalleryImgUpload(String id,MultipartFile[] file);
	
	List<PhotoGallery> findImgs(Integer num);
	
	void toRecommend(String id,String imgids);
	
	
	
	
}
