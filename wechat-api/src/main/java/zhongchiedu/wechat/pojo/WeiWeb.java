package zhongchiedu.wechat.pojo;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zhongchiedu.framework.pojo.GeneralBean;
import zhongchiedu.system.pojo.MultiMedia;

/**
 * 微网站配置
 * @author fliay
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class WeiWeb extends GeneralBean<WeiWeb>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2042375986487716519L;

	@DBRef
	private MultiMedia  logo; //学校logo
	
	@DBRef
	private List<MultiMedia> listBanana;//轮播图

	private String name;//学校名称
	
}
