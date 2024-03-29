package zhongchiedu.school.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.IndexSetting;
import zhongchiedu.school.pojo.News;

public interface NewsService extends GeneralService<News> {

	Pagination<News> findPagination(String webMenuId, Integer pageNo, Integer pageSize,HttpSession session);
	
	void SaveOrUpdateNews(News news, MultipartFile[] filenews, String oldnewsImg, String path, String dir,
			String editorValue,HttpSession session);


	BasicDataResult toDisable(String id);

	String delete(String id);

	void updateNewsVisit(String id);
	
	News findNewsById(String id);
	
	Pagination<News> findNewsBySuperMenuId(String id,Integer pageNo,Integer pageSize);     
	
	Pagination<News> findNewsByWebMenuId(String id,Integer pageNo,Integer pageSize);     
	
	List<News> findNewsByNewsIds(List<IndexSetting> indexs);
	
	List<News> findNewsByDate(String date);
	
	BasicDataResult toRelease(String id);
	
	public Pagination<News> findPaginationAudit( Integer pageNo, Integer pageSize);

	void ToAudit(String id,String type);
	
	List<News> findNewsByWebMenuId(String id); 
	
	
	
	
}













