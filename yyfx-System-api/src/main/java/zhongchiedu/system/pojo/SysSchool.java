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
* <p>Title: SysSchool</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月15日  
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysSchool extends GeneralBean<SysSchool> {

	/** serialVersionUID*/  
	private static final long serialVersionUID = -3369675206766839424L;
	private String name;
	private String address;
	private String province;//省
	private String city;    //城市
	private String district;//区
	private String personLiable;//学校负责人
	private String landine;//座机
	private String contactNumber;//联系电话
	
}
