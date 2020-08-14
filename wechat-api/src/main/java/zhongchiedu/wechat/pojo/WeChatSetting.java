package zhongchiedu.wechat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zhongchiedu.framework.pojo.GeneralBean;

/**
 * 微信公众号配置
 * @author fliay
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class WeChatSetting extends GeneralBean<WeChatSetting>{/**
	 * 
	 */
	private static final long serialVersionUID = -5844322198034309336L;

	
	private String welcome;//关注提示
	
	
	
	
	
	
	
	
	
	
	
	
}
