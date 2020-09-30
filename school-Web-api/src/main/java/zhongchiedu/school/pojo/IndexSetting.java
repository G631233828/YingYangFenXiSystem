package zhongchiedu.school.pojo;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zhongchiedu.framework.pojo.GeneralBean;

/**
 *  
 * @author fliay
 * 网站首页配置
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class IndexSetting  extends GeneralBean<IndexSetting>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7344187255395749815L;
	
	@DBRef
	private List<WebMenu> webMenu;
	
	private String menuIds;
	
	private int num;//加载数量
	
	private boolean random;//随机加载或加载最新的
	@DBRef
	private WebMenu firstLevel;
	
	@DBRef
	private WebMenu secondLevel;
	
	
	
	
}
  