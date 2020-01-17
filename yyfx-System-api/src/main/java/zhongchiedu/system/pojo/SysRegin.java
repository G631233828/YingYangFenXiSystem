/**
 * 
 */
package zhongchiedu.system.pojo;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zhongchiedu.framework.pojo.GeneralBean;

/**  
* <p>Title: SysRegin</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月14日  
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysRegin  extends GeneralBean<SysRegin>{

	/** serialVersionUID*/  
	private static final long serialVersionUID = 1435928612308015017L;
	private String name;
	@DBRef
	private SysRegin sysRegin;//归属
	private String level;//1.省、自治区、直辖市 2.地级市、地区、自治区 3.市辖区、县级市、县
}
