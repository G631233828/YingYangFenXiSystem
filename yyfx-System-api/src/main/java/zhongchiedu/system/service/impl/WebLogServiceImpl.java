package zhongchiedu.system.service.impl;


import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import zhongchiedu.commons.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.system.pojo.WebLog;
import zhongchiedu.system.service.WebLogService;

@Service
public class WebLogServiceImpl extends GeneralServiceImpl<WebLog> implements WebLogService {

	@Override
	public Pagination<WebLog> findAllWebLog(int pageNo,int pageSize,String type) {
		Query query = this.findWeLogByQuery(type);
		return this.findPaginationByQuery(query, pageNo, pageSize, WebLog.class);
	}


	public Query findWeLogByQuery(String type){
		Query query = new Query();
		query.addCriteria(Criteria.where("type").is(type));
		query.with(new Sort(new Order(Direction.DESC, "createDate")));  
		return query;
	}


	@Override
	public List<WebLog> findWebLogByDate(String date) {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("createTime").gte(new Date(date)).lt(Common.dateplus(1)));
		List<WebLog> weblog = this.find(query, WebLog.class);
		return weblog;
	}
	

}
