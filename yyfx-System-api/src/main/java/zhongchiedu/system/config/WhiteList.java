package zhongchiedu.system.config;

import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("white")
public class WhiteList {

	private Set<String> ips;
}
