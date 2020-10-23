package zhongchiedu.system.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("ipfilter")
public class IpFilter {
	
	private boolean web;
	
	private boolean system;

}
