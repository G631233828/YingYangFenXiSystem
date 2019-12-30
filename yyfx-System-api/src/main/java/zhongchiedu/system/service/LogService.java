package zhongchiedu.system.service;



import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.system.pojo.Log;

public interface LogService  extends GeneralService<Log>{

	public Pagination<Log> findAllLog(int pageNo,int pageSize,String type);
	
	
}
