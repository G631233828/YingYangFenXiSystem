package zhongchiedu.school.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zhongchiedu.framework.pojo.GeneralBean;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class WebMenu  extends GeneralBean<WebMenu>{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2741901713977529633L;
	private String name;//主题
	private String parentId;
	private String resUrl;//网页链接
	private int type;// 0 主题 1菜单 2子菜单
	private String firstLevel;//一级菜单Id 二级菜单需要绑定一级菜单
	private boolean isArticle = false;//是否是文章

}
