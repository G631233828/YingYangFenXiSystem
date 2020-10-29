package zhongchiedu.system.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("audit")
public class SysAudit {
	
	private boolean newsAudit;

}
