package zhongchiedu.system.pojo;


import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import zhongchiedu.framework.pojo.GeneralBean;


@Document
@Data
public class WebLog extends GeneralBean<WebLog>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -574653567328003244L;
	
	private String method;
	private String type;
	private String requestIp;
	private String exceptionCode;
	private String exceptionDetail;
	private String params;
	private String createby;
	private String uri;//请求uri

	
	
	
	

}
