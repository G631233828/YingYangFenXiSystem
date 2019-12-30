package zhongchiedu.system.service.impl;


import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.system.pojo.Log;
import zhongchiedu.system.service.LogService;

@Service
public class LogServiceImpl extends GeneralServiceImpl<Log> implements LogService {

	@Override
	public Pagination<Log> findAllLog(int pageNo,int pageSize,String type) {
		Query query = this.findLogByQuery(type);
		return this.findPaginationByQuery(query, pageNo, pageSize, Log.class);
	}


	public Query findLogByQuery(String type){
		Query query = new Query();
		query.addCriteria(Criteria.where("type").is(type));
		query.with(new Sort(new Order(Direction.DESC, "createDate")));  
		return query;
	}
	

}
