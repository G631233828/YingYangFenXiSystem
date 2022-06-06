package zhongchiedu.school.service;

import java.util.List;

import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.WxMpNews;

public interface WxMpNewsService extends GeneralService<WxMpNews> {

	Pagination<WxMpNews> findPagination(Integer pageNo, Integer pageSize);

	public void insertNews(boolean flag);

	public WxMpNews findByMediaIdAndTitle(String mediaId, String url);

	BasicDataResult toDisable(String id);

	String delete(String id);
	
	List<WxMpNews> findWxNews();

}
