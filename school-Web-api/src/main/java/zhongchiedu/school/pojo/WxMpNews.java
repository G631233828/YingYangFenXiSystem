package zhongchiedu.school.pojo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult;
import zhongchiedu.framework.pojo.GeneralBean;
import zhongchiedu.system.pojo.MultiMedia;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class WxMpNews extends GeneralBean<WxMpNews> {

	/**
		 * 
		 */
	private static final long serialVersionUID = -2621613053356354009L;
	//private WxMpMaterialNewsBatchGetResult wxMpMaterialNewsBatchGetResult;

	private String mediaId;//素材id
	private String title;//标题
	private Date updateTime;//更新时间
	private String thumbMediaId;//图文消息的封面图片素材id
	private String thumbUrl;//图片地址
	private String url;//文章地址
	private String digest;//描述
	private String author;//作者
	
	
	
	
	
	
	
	
}
