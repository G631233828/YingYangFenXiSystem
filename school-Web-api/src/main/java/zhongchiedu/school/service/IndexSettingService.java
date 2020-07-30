package zhongchiedu.school.service;

import java.util.List;

import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.IndexSetting;

/**
 * 网站设置
 * 
 * @author fliay
 *
 */

public interface IndexSettingService extends GeneralService<IndexSetting> {

	Pagination<IndexSetting> findPagination(Integer pageNo, Integer pageSize);

	List<IndexSetting> findIndexSetting();

	void saveOrUpdate(IndexSetting indexSetting);

	BasicDataResult toDisable(String id);

	String delete(String id);
	

	BasicDataResult findAndEditIndexSetting(String id, String sort);
	

}
