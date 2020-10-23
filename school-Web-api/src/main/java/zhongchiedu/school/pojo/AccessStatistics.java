package zhongchiedu.school.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zhongchiedu.framework.pojo.GeneralBean;

/***
 * 网站访问统计
 * @author fliay
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class AccessStatistics extends GeneralBean<AccessStatistics>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7641203828547710963L;
	
	private long visitor;//网站每日访问量统计
	
	private long newsNum;//每日新增文章

}
