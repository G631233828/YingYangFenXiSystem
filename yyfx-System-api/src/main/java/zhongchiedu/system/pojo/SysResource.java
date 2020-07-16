/**
 * 
 */
package zhongchiedu.system.pojo;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zhongchiedu.framework.pojo.GeneralBean;

/**  
* <p>Title: SysResouce</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2019年12月30日  
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class SysResource extends GeneralBean<Resource>{
	

	/** serialVersionUID*/  
	private static final long serialVersionUID = -909465184765170526L;
	private String name; // 资源名称
	private String parentId; // 父目录id 没有父目录则为0
	private String resKey; // 资源key
	private int type; // type = 0  根菜单   type = 1 子菜单 type =2 按钮
	private String resUrl; // 资源链接
	private String icon; // 资源图标 
	private String permissionKey;//按钮权限key
	
	 @DBRef
	 private List<SysMenuAuthority> sysMenuAuthority;//如果type=1可以设置拥有的按钮权限
	 
	
	
}
