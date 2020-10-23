package zhongchiedu.system.service;



import java.util.List;

import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.system.pojo.WebLog;

public interface WebLogService  extends GeneralService<WebLog>{

	public Pagination<WebLog> findAllWebLog(int pageNo,int pageSize,String type);
	
	List<WebLog> findWebLogByDate(String date);
	
	
	
	
}
