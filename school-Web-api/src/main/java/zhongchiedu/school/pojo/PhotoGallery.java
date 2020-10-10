package zhongchiedu.school.pojo;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zhongchiedu.framework.pojo.GeneralBean;
import zhongchiedu.system.pojo.MultiMedia;

/**
 * 照片库
 * @author fliay
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class PhotoGallery extends GeneralBean<PhotoGallery> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8611021155839504625L;
	private String name;//活动名称
	private String author;//作者
	private int views;//浏览次数
	@DBRef
	private List<MultiMedia> imgs;//上传的图片
	@DBRef
	private MultiMedia recommend;
	private boolean showInIndex;//是否首页显示
	
	
}
