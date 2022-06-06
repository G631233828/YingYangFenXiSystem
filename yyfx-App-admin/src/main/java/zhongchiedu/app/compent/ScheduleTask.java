package zhongchiedu.app.compent;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.school.service.AccessStatisticsService;
import zhongchiedu.school.service.WxMpNewsService;

@Slf4j
@Component
public class ScheduleTask {

	@Autowired
	private AccessStatisticsService accessStatisticsService;
	
	@Autowired
	private WxMpNewsService wxMpNewsService;
//	@Autowired
//	private WxMpMaterialNewsGetService wxMpMaterialNewsGetService;


	
	
	//@Scheduled(cron = "0/20 * * * * ?")
	@Scheduled(cron = "0 55 23 * * ?")
	public void todoSchedule() {
		log.info("开始统计数据" +LocalDate.now().toString());
		this.accessStatisticsService.insertAccessStatistics();
		log.info("统计完成");
	}
	
//	@Scheduled(cron = "0 0 23 * * ?")
//	public void getWxMaterial() {
//		log.info("开始获取微信永久素材" +LocalDate.now().toString());
//		this.wxMpMaterialNewsGetService.getWxMpMaterialNews();
//		log.info("获取微信永久素材完成");
//	}
	
	

	@Scheduled(cron = "0 20 23 * * ?")
	///@Scheduled(cron = "0/20 * * * * ?")
	public void accessWxNews() {
		log.info("开始统计新闻信息" +LocalDate.now().toString());
		this.wxMpNewsService.insertNews(false);
		log.info("统计新闻信息完成");
	}
	
	
	
	
}
