///**
// * 
// */
//package zhongchiedu.system.pojo;
//
//import org.springframework.data.mongodb.core.mapping.DBRef;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.NoArgsConstructor;
//import zhongchiedu.framework.pojo.GeneralBean;
//
///**  
//* <p>Title: SysUser</p>  
//* <p>Description: </p>  
//* @author 郭建波  
//* @date 2020年1月9日  
//*/
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@EqualsAndHashCode(callSuper=true)
//public class SysSchoolAccount extends GeneralBean<SysSchoolAccount>{
//	
//	/** serialVersionUID*/  
//	private static final long serialVersionUID = -5119664612233628187L;
//	private String name;//用户名
//	private String accountName; 	//登录账户
//	private String passWord;		//登录密码
//	@DBRef
//	private SysSchool sysSchool;//绑定学校
//	
//	private String lastLoginTime;   //登录时间
//	private String lastLoginIp;		//上次登录Ip
//	@DBRef
//	private MultiMedia photograph;  //用户头像
//	private String salt;//加密密码的盐
//	@DBRef
//	private SysRole role;
//}
