package zhongchiedu.system.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zhongchiedu.framework.pojo.GeneralBean;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IpConfigs  extends GeneralBean<IpConfigs>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8555537937196925186L;

	private String ip;
	
	/**
	 * false 白名单
	 * true 黑名单
	 */
	private boolean whiteOrBlack;//黑名单或白名单 白名单访问后台 黑名单禁止访问前台
	
	

}
