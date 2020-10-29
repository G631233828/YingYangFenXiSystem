package zhongchiedu.school.pojo;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zhongchiedu.framework.pojo.GeneralBean;
import zhongchiedu.system.pojo.MultiMedia;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class News extends GeneralBean<News>{/**
	 * 
	 */
	private static final long serialVersionUID = -9185192206564697473L;
	
	private String title;  //标题
	//private boolean isbanana; // 是否能展示到bananer
	private String content;//内容 富文本
	private String author;//发布人
	@DBRef
	private MultiMedia newsImg;//图片地址
	private int views;//浏览次数
	@DBRef
	private WebMenu webMenu;//栏目Id
	private int top;
	private String releaseDate;//发布日期
	@DBRef
	private WebMenu supMenu;//根栏目Id
	
	private int status = 0;//发布状态   0草稿 1审核中   2发布成功  
	
	
	

}
