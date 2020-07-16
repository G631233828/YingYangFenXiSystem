/**
 * 
 */
package zhongchiedu.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.commons.utils.Common;
import zhongchiedu.commons.utils.UserType;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.system.pojo.SysMenuAuthority;
import zhongchiedu.system.pojo.SysOperationAuthority;
import zhongchiedu.system.pojo.SysResource;
import zhongchiedu.system.pojo.SysRole;
import zhongchiedu.system.pojo.SysSchool;
import zhongchiedu.system.pojo.SysUser;
import zhongchiedu.system.service.SysMenuAuthorityService;
import zhongchiedu.system.service.SysOperationAuthorityService;
import zhongchiedu.system.service.SysResourceService;
import zhongchiedu.system.service.SysRoleService;
import zhongchiedu.system.service.SysSchoolService;
import zhongchiedu.system.service.SysUserService;

/**
 * <p>
 * Title: SysRoleServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author 郭建波
 * @date 2020年1月8日
 */
@Service
@Slf4j
public class SysRoleServiceImpl extends GeneralServiceImpl<SysRole> implements SysRoleService {

	@Autowired
	private SysMenuAuthorityService sysMenuAuthorityService;

	@Autowired
	private SysResourceService sysResourceService;

	@Autowired
	private SysSchoolService sysSchoolService;
	
	@Autowired
	private SysOperationAuthorityService sysOperationAuthorityService;
	
	
//	@Autowired
//	private SysUserService SysUserService;

	/*
	 * <p>Title: findPagination</p> <p>Description: </p>
	 * 
	 * @param pageNo
	 * 
	 * @param pageSize
	 * 
	 * @return
	 * 
	 * @see
	 * zhongchiedu.system.service.SysRoleService#findPagination(java.lang.Integer,
	 * java.lang.Integer)
	 */
	@Override
	public Pagination<SysRole> findPagination(SysUser user, Integer pageNo, Integer pageSize) {
		Pagination<SysRole> pagination = null;
		Query query = new Query();
		String userType_ = user.getUserType();

		if (userType_.equals(UserType.SCHOOL_ADMIN) || userType_.equals(UserType.SYSTEM)) {

			if (userType_.equals(UserType.SCHOOL_ADMIN)) {
				query.addCriteria(Criteria.where("sysSchool.$id").is(new ObjectId(user.getSysSchool().getId())));
			}

			// 系统用户获取系统角色
			query.addCriteria(Criteria.where("isDelete").is(false));
			try {
				pagination = this.findPaginationByQuery(query, pageNo, pageSize, SysRole.class);

			} catch (Exception e) {
				log.info("查询所有信息失败——————————》" + e.toString());
				e.printStackTrace();
			}
		}
		return Common.isNotEmpty(pagination) ? pagination : new Pagination<SysRole>();
	}

	/*
	 * <p>Title: findSSysRoleByName</p> <p>Description: </p>
	 * 
	 * @param name
	 * 
	 * @return
	 * 
	 * @see zhongchiedu.system.service.SysRoleService#findSSysRoleByName(java.lang.
	 * String)
	 */
	@Override
	public SysRole findSysRoleByName(String name) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.findOneByQuery(query, SysRole.class);
	}

	/*
	 * <p>Title: findAllSysRoleByIsDisable</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see zhongchiedu.system.service.SysRoleService#findAllSysRoleByIsDisable()
	 */
	@Override
	public List<SysRole> findAllSysRoleByIsDisable(SysUser user) {
		
		Query query = new Query();
		//1.系统管理员   只查看  system 学校管理员的角色
		//2.学校管理员或有权限的老师  只能看到user的权限
		String userType_ = user.getUserType();
		if(userType_.equals(UserType.SYSTEM)) {
			Criteria ca = new Criteria();
			ca.orOperator(Criteria.where("userType").is(UserType.SYSTEM),Criteria.where("userType").is(UserType.SCHOOL_ADMIN));
			query.addCriteria(ca);
		}else if(userType_.equals(UserType.SCHOOL_ADMIN)||userType_.equals(UserType.SCHOOL_USER)) {
			String schoolId = user.getSysSchool().getId();
			query.addCriteria(Criteria.where("sysSchool.$id").is(new ObjectId(schoolId)));
			query.addCriteria(Criteria.where("userType").is(UserType.SCHOOL_USER));
		}else {
			return null;
		}
		
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.find(query, SysRole.class);
	}

	/*
	 * <p>Title: findAllSysRole</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see zhongchiedu.system.service.SysRoleService#findAllSysRole()
	 */
	@Override
	public List<SysRole> findAllSysRole() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.find(query, SysRole.class);
	}

	/*
	 * <p>Title: saveOrUpdate</p> <p>Description: </p>
	 * 
	 * @param sysRole
	 * 
	 * @see
	 * zhongchiedu.system.service.SysRoleService#saveOrUpdate(zhongchiedu.system.
	 * pojo.SysRole)
	 */
	@Override
	public void saveOrUpdate(SysRole sysRole, SysUser user) {
		if (Common.isNotEmpty(sysRole) && Common.isNotEmpty(user)) {
			sysRole.setUserType(UserType.SCHOOL_ADMIN);
			String schoolId = user.getSysSchool() == null ? "" : user.getSysSchool().getId();
			// 学校老师，管理员登陆
			if (Common.isNotEmpty(schoolId) && (user.getUserType().equals(UserType.SCHOOL_ADMIN)
					|| user.getUserType().equals(UserType.SCHOOL_USER))) {
				// 添加学校id
				SysSchool school = this.sysSchoolService.findSysSchoolById(schoolId);
				if (Common.isEmpty(school)) {
					return;
				}
				sysRole.setSysSchool(school);
				sysRole.setUserType(UserType.SCHOOL_USER);
			}
			// 超级管理员登陆
			if (Common.isNotEmpty(sysRole.getId())) {
				// update
				SysRole ed = this.findOneById(sysRole.getId(), SysRole.class);
				BeanUtils.copyProperties(sysRole, ed);
				this.save(sysRole);
				log.info("角色修改成功{}", sysRole.getId());
			} else {
				// insert
				this.insert(sysRole);
				log.info("添加角色{}", sysRole);
			}
		}
	}

	/*
	 * <p>Title: toDisable</p> <p>Description: </p>
	 * 
	 * @param id
	 * 
	 * @return
	 * 
	 * @see zhongchiedu.system.service.SysRoleService#toDisable(java.lang.String)
	 */
	@Override
	public BasicDataResult toDisable(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "禁用失败，请求出现问题，请刷新后重试", null);
		}
		SysRole sys = this.findOneById(id, SysRole.class);
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
	 * zhongchiedu.system.service.SysRoleService#ajaxgetRepletes(java.lang.String)
	 */
	@Override
	public BasicDataResult ajaxgetRepletes(String name) {
		if (Common.isNotEmpty(name)) {
			SysRole sys = this.findSysRoleByName(name);
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
	 * @see zhongchiedu.system.service.SysRoleService#delete(java.lang.String)
	 */
	private Lock lock = new ReentrantLock();

	@Override
	public String delete(String id) {
		try {
			lock.lock();
			List<String> ids = Arrays.asList(id.split(","));
			for (String edid : ids) {
				SysRole de = this.findOneById(edid, SysRole.class);
				de.setIsDelete(true);
				this.save(de);
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
	 * <p>Title: author</p> <p>Description: </p>
	 * 
	 * @param id
	 * 
	 * @param checkallPermission
	 * 
	 * @return
	 * 
	 * @see zhongchiedu.system.service.SysRoleService#author(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public BasicDataResult author(String id, String checkallPermission) {
		try {
			List<SysResource> listres = new ArrayList<SysResource>();
			List<SysMenuAuthority> listmenu = new ArrayList<SysMenuAuthority>();
			String st = "_";
			SysRole role = this.findOneById(id, SysRole.class);
			String[] strids = checkallPermission.split(",");
			TreeSet<Object> t = Common.toRepeat(strids);
			Iterator<Object> i = t.iterator();
			while (i.hasNext()) {
				String getid = i.next().toString();
				if (getid.contains(st)) {
					String bid = Common.subStringEndOf(st, getid);
					// String pid = Common.subStringBeforeOf(st, getid);
					SysMenuAuthority menu = this.sysMenuAuthorityService.findOneById(bid, SysMenuAuthority.class);
					listmenu.add(menu);
				} else {
					SysResource res = this.sysResourceService.findOneById(getid, SysResource.class);
					listres.add(res);
				}
			}
			role.setSysresource(new ArrayList<SysResource>());
			role.setSysMenuAuthority(new ArrayList<SysMenuAuthority>());
			this.save(role);
			// 更新
			role.setSysMenuAuthority(listmenu);
			role.setSysresource(listres);
			this.save(role);
			uploadRoleVersion(id);
			return BasicDataResult.build(200, "权限分配成功", null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return BasicDataResult.build(400, "权限分配失败", null);
		}

	}

	/**
	 * 更新权限版本
	 */
	public void uploadRoleVersion(String id) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				uploadRoleVersionByThread(id);
			}
		}).start();
	}

	/**
	 * 权限版本+1
	 * 
	 * @param id
	 */
	public synchronized void uploadRoleVersionByThread(String id) {
		SysRole role = this.findOneById(id, SysRole.class);
		role.setVersion(role.getVersion() + 1);
		this.save(role);
	}

	/*
	 * <p>Title: getAuthor</p> <p>Description: </p>
	 * 
	 * @param id
	 * 
	 * @return
	 * 
	 * @see zhongchiedu.system.service.SysRoleService#getAuthor(java.lang.String)
	 */
	@Override
	public BasicDataResult getAuthor(String id) {
		if (Common.isEmpty(id)) {

			return BasicDataResult.build(400, "未能找到角色的相关信息", null);
		}
		SysRole role = this.findOneById(id, SysRole.class);

		if (role != null) {

//			List<SysResource> list = role.getSysresource();
			return BasicDataResult.build(200, "获取权限成功", role);

		}
		return BasicDataResult.build(200, "未设置权限", null);
	}

	@Override
	public BasicDataResult createSysOperationAuthority(String param) {
		String id = Common.subStringBeforeOf("_", param);//当前编辑id
		String operId = Common.subStringEndOf("_", param);
		SysOperationAuthority so = this.sysOperationAuthorityService.findOneById(operId, SysOperationAuthority.class);
		
  		SysResource sr = this.sysResourceService.findOneById(id, SysResource.class);
  		
		SysMenuAuthority sm = this.sysMenuAuthorityService.findSysMenuAuthority(operId, id);
		
		//SysResource parent = this.findOneById(sr.getParentId(), SysResource.class);
		
		String reskey = sr.getPermissionKey()+":"+so.getKey();
		if(Common.isEmpty(sm)) {
			//创建
			 sm= new SysMenuAuthority();
			 sm.setParentResourceId(sr.getId());
			 sm.setResKey(reskey);
			 sm.setSysOperationAuthority(so);
			this.sysMenuAuthorityService.insert(sm);
		}else {
			sm.setResKey(reskey);
			this.sysMenuAuthorityService.save(sm);
		}
		//更新操作权限
		List<SysMenuAuthority> listsm = sr.getSysMenuAuthority()==null?new ArrayList<SysMenuAuthority>():sr.getSysMenuAuthority();
		List<SysMenuAuthority> newsm = new ArrayList<>();
		
		if(listsm.size() == 0) {
			listsm.add(sm);
			sr.setSysMenuAuthority(listsm);
			this.sysResourceService.save(sr);
			return BasicDataResult.build(200, "操作成功",null);
		}else {
			//判断是否存在，存在则去除
			boolean flag = this.contain(listsm, sm.getId());
			if(flag) {
				//删除
				for(SysMenuAuthority sys:listsm) {
					if(!sys.getId().equals(sm.getId())) {
						newsm.add(sys);
					}
				}
				sr.setSysMenuAuthority(newsm);
				this.sysResourceService.save(sr);
				//删除的时候需要把角色表中的权限也去掉
				//获取所有的非删除状态下的角色
				List<SysRole> listRoles = this.findAllSysRole();
				for(SysRole sysRole : listRoles) {
					List<SysMenuAuthority> newRoleAuth = new ArrayList<>();
					for(SysMenuAuthority sys:sysRole.getSysMenuAuthority()) {
						if(!sys.getId().equals(sm.getId())) {
							newRoleAuth.add(sys);
						}
					}
					sysRole.setSysMenuAuthority(newRoleAuth);
					this.save(sysRole);
				}
				
				
				
				return BasicDataResult.build(200, "操作成功",null);
			}else {
				//添加
				listsm.add(sm);
				sr.setSysMenuAuthority(listsm);
				this.sysResourceService.save(sr);
				return BasicDataResult.build(200, "操作成功",null);
			}
		}
	}
	
	
	
	
	
	public boolean contain(List<SysMenuAuthority> listsm,String containId) {
		for(SysMenuAuthority sys:listsm) {
			if(sys.getId().equals(containId)) {
				return true;
			}
		}
		return false;
	}
	
	
	
	



}
