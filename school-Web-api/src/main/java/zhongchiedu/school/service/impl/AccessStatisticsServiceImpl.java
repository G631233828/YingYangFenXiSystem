package zhongchiedu.school.service.impl;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.Common;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.AccessStatistics;
import zhongchiedu.school.pojo.News;
import zhongchiedu.school.service.AccessStatisticsService;
import zhongchiedu.school.service.NewsService;
import zhongchiedu.system.pojo.WebLog;
import zhongchiedu.system.service.WebLogService;

@Service
@Slf4j
public class AccessStatisticsServiceImpl extends GeneralServiceImpl<AccessStatistics>
		implements AccessStatisticsService {
	
	@Autowired
	private NewsService newsService;
	@Autowired
	private WebLogService webLogService;
	
	
	@Override
	public void insertAccessStatistics() {
		log.info("系统开始统计今日数据");
		String date = Common.getDateN("/",0);
		//获取今日新增新闻
		List<News> list = this.newsService.findNewsByDate(date);	
		//获取今日访问
		List<WebLog> logs = this.webLogService.findWebLogByDate(date);
		long newsNums = list.size();//昨日新增新闻数量
		Set<String> ips = new HashSet<String>();
		logs.forEach(weblog->{
			ips.add(weblog.getRequestIp());
		});
		AccessStatistics acc = new AccessStatistics();
		acc.setNewsNum(newsNums);
		acc.setVisitor(ips.size());
		this.insert(acc);
		
		log.info("数据统计完成");
		
	}
	
	
	
	
	
	
	
	
	

}
