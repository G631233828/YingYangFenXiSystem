package zhongchiedu.system.service;

import java.util.List;

import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.system.pojo.IpConfigs;



public interface IpConfigsService  extends GeneralService<IpConfigs>{

	Pagination<IpConfigs> findPagination(Integer pageNo,Integer pageSize);

	List<IpConfigs> findWhiteOrBlackList(boolean whiteOrBlack);
	
	BasicDataResult ajaxgetRepletes(String ip);
	
	String delete(String id );
	
	IpConfigs findByIp(String ip);
	
	void saveOrUpdate(IpConfigs ipConfigs);
	
	BasicDataResult toDisable(String id);
}
