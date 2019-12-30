/**
 * 
 */
package zhongchiedu.system.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zhongchiedu.framework.pojo.GeneralBean;

/**  
* <p>Title: SysOperationAuthority</p>  
* <p>Description: 权限精确到按钮级别 </p>  
* @author 郭建波  
* @date 2019年12月27日  
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysOperationAuthority extends GeneralBean<SysOperationAuthority> {

	/** serialVersionUID*/  
	private static final long serialVersionUID = -7240987958137039054L;
	private String name;
	private String key;//按钮中关键字key :xxx   xxx:add xxx:del  
	
	
	
}
