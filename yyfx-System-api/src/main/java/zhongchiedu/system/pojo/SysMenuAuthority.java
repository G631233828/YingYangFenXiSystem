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
* <p>Title: SysMenuAuthority</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2019年12月31日  
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class SysMenuAuthority extends GeneralBean<SysMenuAuthority> {
	
	
	/** serialVersionUID*/  
	private static final long serialVersionUID = 4937077588858590945L;
	
	
	@DBRef
	private SysOperationAuthority sysOperationAuthority;
	private String parentResourceId;
//	private String resKey;//目录key+":"+菜单key+":"+按钮key
//	private String resUrl;

}
