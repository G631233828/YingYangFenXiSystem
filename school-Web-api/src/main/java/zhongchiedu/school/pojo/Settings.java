package zhongchiedu.school.pojo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zhongchiedu.framework.pojo.GeneralBean;
import zhongchiedu.system.pojo.MultiMedia;

/**
 * 校园网站配置
 * @author fliay
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class Settings  extends GeneralBean<Settings>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6167698514532160275L;
	
	private MultiMedia  logo; //学校logo
	
	private List<MultiMedia> listBanana;//轮播图
	
	private MultiMedia icon;//网站icon
	
	private String title;//网站titie
	
	
	private MultiMedia qRcode;//二维码
	
	private String address;//地址
	
	private String connectPhone;//联系电话
	
	private String recordNumber; //沪备案号
	
	private String gxbRecordNumber;//工信部备案号
	
	private String coryright;//版权
	
	private String email;//电子邮箱
	
	private String postCode;//邮编
	
	
	

}
  