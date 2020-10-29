package zhongchiedu.framework.pojo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zhongchiedu.commons.utils.Common;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralBean<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private Boolean isDelete=false;//是否删除
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date createTime = new Date();
//	private String createTime = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date());
	private String createDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());//创建时间
	private Boolean isDisable=false;//禁用
	private String sort= "0";//排序
	private String description;//描述
	
	
}
