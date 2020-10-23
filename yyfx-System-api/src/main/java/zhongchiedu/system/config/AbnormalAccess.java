package zhongchiedu.system.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("abnormal")
public class AbnormalAccess {

	private List<String> name;//定义攻击字符
}
