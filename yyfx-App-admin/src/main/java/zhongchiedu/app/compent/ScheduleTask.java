package zhongchiedu.app.compent;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.school.service.AccessStatisticsService;

@Slf4j
@Component
public class ScheduleTask {

	@Autowired
	private AccessStatisticsService accessStatisticsService;
	
	//@Scheduled(cron = "0/20 * * * * ?")
	@Scheduled(cron = "0 55 23 * * ?")
	public void todoSchedule() {
		log.info("开始统计数据" +LocalDate.now().toString());
		this.accessStatisticsService.insertAccessStatistics();
		log.info("统计完成");
	}
	
	
}
