package zhongchiedu.school.pojo;


import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zhongchiedu.framework.pojo.GeneralBean;

//网站模板
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SiteTemplate  extends GeneralBean<SiteTemplate>{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3300624490754729209L;
	
	
	private String templateName;//模板名称
	private String templatePath;//模板路径
	private String templateStyle;//模板风格
	@DBRef
	private WebMenu webMenu;//站点菜单

}
