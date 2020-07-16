/**
 * 
 */
package zhongchiedu.system.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zhongchiedu.framework.pojo.GeneralBean;

/**  
* <p>Title: SysUnit</p>  
* <p>Description:计量单位 </p>  
* @author 郭建波  
* @date 2020年1月23日  
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class SysUnit extends GeneralBean<SysUnit>{
	
	/** serialVersionUID*/  
	private static final long serialVersionUID = -334117674383549711L;
	
	private String name;
}
