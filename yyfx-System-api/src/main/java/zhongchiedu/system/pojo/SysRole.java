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
import zhongchiedu.commons.utils.UserType;
import zhongchiedu.framework.pojo.GeneralBean;

/**
 * <p>
 * Title: SysRole
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author 郭建波
 * @date 2019年12月31日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysRole extends GeneralBean<SysRole> {
	/** serialVersionUID */
	private static final long serialVersionUID = 4931119425223903208L;

	private String roleName; // 角色名称
	private String roleKey; // 角色key
	@DBRef
	private List<SysResource> sysresource;

	@DBRef
	private List<SysMenuAuthority> sysMenuAuthority;
	
	@DBRef
	private SysSchool sysSchool;
	
	private String userType;
	
	private int version; // 当前版本

}
