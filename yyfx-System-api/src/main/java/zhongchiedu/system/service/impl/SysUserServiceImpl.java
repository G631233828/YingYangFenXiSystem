/**
 * 
 */
package zhongchiedu.system.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.commons.utils.Common;
import zhongchiedu.commons.utils.ExcelReadUtil;
import zhongchiedu.commons.utils.FileOperateUtil;
import zhongchiedu.commons.utils.UserType;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.system.pojo.MultiMedia;
import zhongchiedu.system.pojo.ProcessInfo;
import zhongchiedu.system.pojo.SysMenuAuthority;
import zhongchiedu.system.pojo.SysResource;
import zhongchiedu.system.pojo.SysRole;
import zhongchiedu.system.pojo.SysSchool;
import zhongchiedu.system.pojo.SysUser;
import zhongchiedu.system.service.SysSchoolService;
import zhongchiedu.system.service.SysUserService;

/**
 * <p>
 * Title: SysUserServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author 郭建波
 * @date 2020年1月9日
 */
@Service
@Slf4j
public class SysUserServiceImpl extends GeneralServiceImpl<SysUser> implements SysUserService {

	@Autowired
	private SysRoleServiceImpl sysRoleService;

	@Autowired
	private MultiMediaServiceImpl multiMediaSerice;
	
	@Autowired
	private SysResourceServiceImpl sysResourceService;

 	/*
	 * <p>Title: findPagination</p> <p>Description: </p>
	 * 
	 * @param pageNo
	 * 
	 * @param pageSize
	 * 
	 * @return 
	 * //获取当前用户的用户类型
	 *  //1.超级管理员 查看所有学校的管理员 
	 *  //2.学校管理员 查看自己学校的老师 
	 *  //3.老师用户
	 * 不能查看信息
	 * 
	 * @see
	 * zhongchiedu.system.service.SysUserService#findPagination(java.lang.Integer,
	 * java.lang.Integer)
	 */
	@Override
	public Pagination<SysUser> findPagination(SysUser user, Integer pageNo, Integer pageSize) {
		Pagination<SysUser> pagination = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		String userType_ = user.getUserType();
		if(Common.isEmpty(userType_)) {
			return new Pagination<SysUser>();
		}else if(userType_.equals(UserType.SCHOOL_USER)) {
			//老师登陆
			return new Pagination<SysUser>();
		}else if(userType_.equals(UserType.SCHOOL_ADMIN)){
			query.addCriteria(Criteria.where("sysSchool.$id").is(new ObjectId(user.getSysSchool().getId())));
		}else if(userType_.equals(UserType.SYSTEM)) {
			//获取所有超级管理员用户，学校管理员
//			Criteria ca = new Criteria();
//			Criteria ca2 = new Criteria();
//			ca.andOperator(Criteria.where("userType").is(UserType.SCHOOL_ADMIN));
//			ca2.andOperator(Criteria.where("userType").is(UserType.SYSTEM));
//			query.addCriteria(ca.orOperator(ca2));
			List<String> type = new ArrayList<String>();
			type.add(UserType.SCHOOL_ADMIN);
			type.add(UserType.SYSTEM);
			query.addCriteria(Criteria.where("userType").in(type));
		}else {
			return new Pagination<SysUser>();
		}
		
		
//		if(Common.isNotEmpty(userType)) {
//			query.addCriteria(Criteria.where("userType").is(userType));
//		}
		try {
			pagination = this.findPaginationByQuery(query, pageNo, pageSize, SysUser.class);

		} catch (Exception e) {
			log.info("查询所有信息失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return Common.isNotEmpty(pagination) ? pagination : new Pagination<SysUser>();
	}

	/*
	 * <p>Title: findSysUserByAccountName</p> <p>Description: </p>
	 * 
	 * @param account
	 * 
	 * @return
	 * 
	 * @see
	 * zhongchiedu.system.service.SysUserService#findSysUserByAccountName(java.lang.
	 * String)
	 */
	@Override
	public SysUser findSysUserByAccountName(String accountName, String userType) {
		Query query = new Query();
		query.addCriteria(Criteria.where("accountName").is(accountName));
		if (Common.isNotEmpty(userType)) {
			query.addCriteria(Criteria.where("userType").is(userType));
		}
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.findOneByQuery(query, SysUser.class);
	}

	/*
	 * <p>Title: findAllSysUserByIsDisable</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see zhongchiedu.system.service.SysUserService#findAllSysUserByIsDisable()
	 */
	@Override
	public List<SysUser> findAllSysUserByIsDisable() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.find(query, SysUser.class);
	}

	/*
	 * <p>Title: findAllSysUser</p> <p>Description: </p>
	 *  
	 * @return
	 * 
	 * @see zhongchiedu.system.service.SysUserService#findAllSysUser()
	 */
	@Override
	public List<SysUser> findAllSysUser() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.find(query, SysUser.class);
	}

	/*
	 * <p>Title: saveOrUpdate</p> <p>Description: </p>
	 * 
	 * @param sysUser
	 * 
	 * @see
	 * zhongchiedu.system.service.SysUserService#saveOrUpdate(zhongchiedu.system.
	 * pojo.SysUser)
	 */
	@Override
	public void saveOrUpdate(SysUser user, String roleId, MultipartFile[] file, String imgPath, String dir,
			String oldheadImg,SysUser sessionUser) {
		String userType_ = sessionUser.getUserType();
		//1.系统管理员添加 其他管理员
		//2.系统管理员添加学校管理员
		//3.学校管理员添加其他账户
		if(userType_.equals(UserType.SYSTEM)) {
			
			
			if(Common.isEmpty(user.getSysSchool().getId())) {
				user.setSysSchool(null);
				user.setUserType(UserType.SYSTEM);
				//创建管理员
			}else {
				user.setUserType(UserType.SCHOOL_ADMIN);
			}
			
			
		}else if(userType_.equals(UserType.SCHOOL_ADMIN)||userType_.equals(UserType.SCHOOL_USER)) {
			if(Common.isEmpty(sessionUser.getSysSchool())) {
				return ;
			}
			user.setUserType(UserType.SCHOOL_USER);
			user.setSysSchool(sessionUser.getSysSchool());
		}
		
		SysUser getUser = null;
		List<MultiMedia> userHead = null;
		userHead = this.multiMediaSerice.uploadPictures(file, dir, imgPath, "USER", 105, 105);

		if (Common.isNotEmpty(user.getId())) {
			getUser = this.findOneById(user.getId(), SysUser.class);
			if (Common.isNotEmpty(getUser)) {
				user.setPhotograph(Common.isNotEmpty(oldheadImg) ? getUser.getPhotograph() : null);
				user.setAccountName(getUser.getAccountName());
			}
		}
		if (userHead.size() > 0) {
			user.setPhotograph(userHead.get(0));
		}
		SysRole role = this.sysRoleService.findOneById(roleId, SysRole.class);
		user.setRole(role != null ? role : null);
		if (Common.isNotEmpty(getUser)) {
			BeanUtils.copyProperties(user, getUser);
			this.save(user);
		} else {
			this.insert(user);
		}

	}

	/*
	 * <p>Title: toDisable</p> <p>Description: </p>
	 * 
	 * @param id
	 * 
	 * @return
	 * 
	 * @see zhongchiedu.system.service.SysUserService#toDisable(java.lang.String)
	 */
	@Override
	public BasicDataResult toDisable(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "禁用失败，请求出现问题，请刷新后重试", null);
		}
		SysUser sys = this.findOneById(id, SysUser.class);
		if (Common.isEmpty(sys)) {
			return BasicDataResult.build(400, "无法获取到资源信息，可能已经被删除", null);
		}
		sys.setIsDisable(sys.getIsDisable().equals(true) ? false : true);
		this.save(sys);
		return BasicDataResult.build(200, sys.getIsDisable().equals(true) ? "禁用成功" : "启用成功", sys.getIsDisable());
	}

	/*
	 * <p>Title: ajaxgetRepletes</p> <p>Description: </p>
	 * 
	 * @param name
	 * 
	 * @return
	 * 
	 * @see
	 * zhongchiedu.system.service.SysUserService#ajaxgetRepletes(java.lang.String)
	 */
	@Override
	public BasicDataResult ajaxgetRepletes(String name) {
		if (Common.isNotEmpty(name)) {
			SysUser sys = this.findSysUserByAccountName(name, "");
			if (Common.isNotEmpty(sys)) {
				return BasicDataResult.build(206, "数据已存在，请勿重复提交", null);
			}
			return BasicDataResult.ok();
		}
		return BasicDataResult.build(400, "未能获取到请求的信息", null);
	}

	/*
	 * <p>Title: delete</p> <p>Description: </p>
	 * 
	 * @param id
	 * 
	 * @return
	 * 
	 * @see zhongchiedu.system.service.SysUserService#delete(java.lang.String)
	 */
	private Lock lock = new ReentrantLock();

	@Override
	public String delete(String id, SysUser suser) {
		try {
			lock.lock();
			List<String> ids = Arrays.asList(id.split(","));
			String loginid = Common.isNotEmpty(suser) ? suser.getId() : "";
			for (String edid : ids) {
				// 不能删除自己的账号
				if (edid != loginid) {
					SysUser de = this.findOneById(edid, SysUser.class);
					de.setIsDelete(true);
					this.save(de);
				}
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return "error";
	}

	/*
	 * <p>Title: checkPassword</p> <p>Description: </p>
	 * 
	 * @param id
	 * 
	 * @param password
	 * 
	 * @return
	 * 
	 * @see
	 * zhongchiedu.system.service.SysUserService#checkPassword(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public BasicDataResult checkPassword(String id, String password) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "发生未知异常，请联系管理员！", null);
		}
		if (Common.isEmpty(password)) {
			return BasicDataResult.build(400, "请输入旧密码", null);
		}
		SysUser user = this.findOneById(id, SysUser.class);
		if (Common.isEmpty(user)) {
			return BasicDataResult.build(400, "未能获取到数据，请刷新后再试", null);
		}
		if (user.getPassWord().equals(password)) {
			return BasicDataResult.build(200, "密码正确", null);
		}
		return BasicDataResult.build(400, "发生未知异常，请联系管理员！", null);
	}

	/*
	 * <p>Title: editPassword</p> <p>Description: </p>
	 * 
	 * @param id
	 * 
	 * @param password
	 * 
	 * @return
	 * 
	 * @see zhongchiedu.system.service.SysUserService#editPassword(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public BasicDataResult editPassword(String id, String password) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "发生未知异常，请联系管理员！", null);
		}
		if (Common.isEmpty(password)) {
			return BasicDataResult.build(400, "新密码不能为空", null);
		}
		SysUser user = this.findOneById(id, SysUser.class);
		if (Common.isEmpty(user)) {
			return BasicDataResult.build(400, "未能获取到数据，请刷新后再试", null);
		}
		user.setPassWord(password);
		this.save(user);
		return BasicDataResult.build(200, "密码修改成功", null);
	}

	
	
	

	//超级管理员登陆可以查看所有的权限
		//学校管理员登陆只能看到当前用户登陆之后获取到的角色中的所有权限
		//学校管理员创建角色只能创建通过↑的角色，不能获取到更多的角色信息
		//
		
		

		@Override
		public List<SysResource> findAllSysResources(SysUser user) {
			Query query = new Query();
			String userType_ = user.getUserType();
			List<SysResource> findAllSysResources = new ArrayList<SysResource>();
			if (userType_.equals(UserType.SCHOOL_ADMIN) || userType_.equals(UserType.SYSTEM)||userType_.equals(UserType.SCHOOL_USER)) {
				if (userType_.equals(UserType.SCHOOL_ADMIN)||userType_.equals(UserType.SCHOOL_USER)) {
					//登陆的是学校管理员,或者是学校管理员创建的管理员
				    //1.通过session中用户的id获取用户信息，并且获取到用户的角色id
					SysUser suser = this.findOneById(user.getId(), SysUser.class);
					//2.通过角色id获取对应的角色
					SysRole srole = suser.getRole();
					List<SysResource> ls = srole.getSysresource();
					List<SysMenuAuthority> sm = srole.getSysMenuAuthority();
					//3.将角色中的菜单以及按钮分别放入SysResource中
					for(SysResource res:ls) {
						List<SysMenuAuthority> sysmenu = new ArrayList<SysMenuAuthority>();
						for(SysMenuAuthority au:sm) {
							if(res.getId().equals(au.getParentResourceId())) {
								sysmenu.add(au);
							}
						}
						res.setSysMenuAuthority(sysmenu);
						findAllSysResources.add(res);
					}
					
					
				}else {
					//系统管理员
					query.addCriteria(Criteria.where("isDisable").is(false));
					query.addCriteria(Criteria.where("isDelete").is(false));
					return this.sysResourceService.find(query, SysResource.class);
					
					
				}
			
			}
			
			
			return findAllSysResources;
		}

		@Override
		public SysUser findUserByUserNamePassword(String userName, String passWord) {
			// TODO Auto-generated method stub
			Query query = new Query();
//			query.addCriteria(Criteria.where("isDisable").is(false));
//			query.addCriteria(Criteria.where("isDelete").is(false));
			query.addCriteria(Criteria.where("accountName").is(userName));
			query.addCriteria(Criteria.where("passWord").is(passWord));
			
			return this.findOneByQuery(query, SysUser.class);
			
			
		}
	
	
	
		/**
		 * 上传进度
		 */
		@Override
		public ProcessInfo findproInfo(HttpServletRequest request) {

			return (ProcessInfo) request.getSession().getAttribute("proInfo");

		}

		@Override
		public String upload(HttpServletRequest request, HttpSession session) {
			String error = "";
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				// 别名
				String upname = File.separator + "FileUpload" + File.separator + "sysuser";

				// 可以上传的文件格式
				log.info("准备上传类目数据");
				String filetype[] = { "xls,xlsx" };
				List<Map<String, Object>> result = FileOperateUtil.upload(request, upname, filetype);
				log.info("上传文件成功");
				boolean has = (Boolean) result.get(0).get("hassuffix");

				if (has != false) {
					// 获得上传的xls文件路径
					String path = (String) result.get(0).get("savepath");
					File file = new File(path);
					// 知道导入返回导入结果
					error = this.BatchImport(file, 1, session);
				}
			} catch (Exception e) {
				return e.toString();
			}
			return error;

		}
	
	
		
		public String BatchImport(File file, int row, HttpSession session) {
			String error = "";
			String[][] resultexcel = null;
			try {
				resultexcel = ExcelReadUtil.readExcel(file, 0);
			} catch (IOException e) {
				e.printStackTrace();
			}
			int rowLength = resultexcel.length;
			ProcessInfo pri = new ProcessInfo();
			pri.allnum = rowLength;
			
			
			for (int i = 1; i < rowLength; i++) {
				Query query = new Query();
				SysUser importUser = new SysUser();

				pri.nownum = i;
				pri.lastnum = rowLength - i;
				session.setAttribute("proInfo", pri);
				int j = 0;
				try {

					String userName = resultexcel[i][j].trim();//姓名
					if (Common.isEmpty(userName)) {
						error += "<span class='entypo-attention'></span>导入文件过程中出现姓名为空，第<b>&nbsp&nbsp" + (i + 1)
								+ "行&nbsp&nbsp</b>请手动去修改该条信息！&nbsp&nbsp</br>";
						continue;
					}
					importUser.setUserName(userName);
					
					String accountName = resultexcel[i][j+1].trim();// 手机号
					if (Common.isEmpty(accountName)) {
						error += "<span class='entypo-attention'></span>导入文件过程中出现手机号为空，第<b>&nbsp&nbsp" + (i + 1)
								+ "行&nbsp&nbsp</b>请手动去修改该条信息！&nbsp&nbsp</br>";
						continue;
					}
					importUser.setAccountName(accountName);
					
					String roleName = resultexcel[i][j + 2].trim();
					if (Common.isEmpty(roleName)) {
						error += "<span class='entypo-attention'></span>导入文件过程中出现角色名称为空，第<b>&nbsp&nbsp" + (i + 1)
								+ "行</b>请手动去修改该条信息！&nbsp&nbsp</br>";
						continue;
					}
					
					//通过手机号查询用户是否存在不存在则添加，存在则提示
					SysUser sysuer = this.findSysUserByAccountName(accountName, UserType.SCHOOL_USER);
					if(Common.isNotEmpty(sysuer)) {
						error += "<span class='entypo-attention'></span>导入文件过程中出现该账号已经存在！第<b>&nbsp&nbsp" + (i + 1)
								+ "行&nbsp&nbsp</b>请手动去修改该条信息！&nbsp&nbsp</br>";
						continue;
					}else {
						//通过角色名称获取角色信息
						SysRole sysrole = this.sysRoleService.findSysRoleByName(roleName);
						if(Common.isEmpty(sysrole)) {
							error += "<span class='entypo-attention'></span>导入文件过程中没有找到角色["+roleName+"]！，第<b>&nbsp&nbsp" + (i + 1)
									+ "行&nbsp&nbsp</b>请先创建该角色！&nbsp&nbsp</br>";
							continue;
						}
						importUser.setRole(sysrole);
						importUser.setSysSchool(sysrole.getSysSchool());
						importUser.setUserType(UserType.SCHOOL_USER);
						importUser.setCardType("身份证");
						importUser.setCardId("000");
						importUser.setPassWord("changeme");
						this.insert(importUser);
						
					}
					
					
				

					// 捕捉批量导入过程中遇到的错误，记录错误行数继续执行下去
				} catch (Exception e) {
					log.debug("导入文件过程中出现错误第" + (i + 1) + "行出现错误" + e);
					String aa = e.getLocalizedMessage();
					String b = aa.substring(aa.indexOf(":") + 1, aa.length()).replaceAll("\"", "");
					error += "<span class='entypo-attention'></span>导入文件过程中出现错误第<b>&nbsp&nbsp" + (i + 1)
							+ "&nbsp&nbsp</b>行出现错误内容为<b>&nbsp&nbsp" + b + "&nbsp&nbsp</b></br>";
					if ((i + 1) < rowLength) {
						continue;
					}

				}
			}
			log.info(error);
			return error;
		}	
		
		
		
		
		
		
		
	
}
