/**
 * 
 */
package zhongchiedu.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
import zhongchiedu.system.pojo.SysUser;
import zhongchiedu.system.service.SysMenuAuthorityService;
import zhongchiedu.system.service.SysOperationAuthorityService;
import zhongchiedu.system.service.SysResourceService;
import zhongchiedu.system.service.SysRoleService;
import zhongchiedu.system.service.SysUserService;

/**  
* <p>Title: SysResourceServiceImpl</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2019年12月30日  
*/
@Service
@Slf4j
public class SysResourceServiceImpl extends GeneralServiceImpl<SysResource> implements SysResourceService  {

	@Autowired
	private SysOperationAuthorityService sysOperationAuthorityService;
	@Autowired
	private SysMenuAuthorityService sysMenuAuthorityService;
//	@Autowired
//	private SysRoleService sysRoleService;
	


	/* <p>Title: findPagination</p>  
	 * <p>Description: </p>  
	 * @param pageNo
	 * @param pageSize
	 * @return  
	 * @see zhongchiedu.system.service.SysResourceService#findPagination(java.lang.Integer, java.lang.Integer)  
	 */
	@Override
	public Pagination<SysResource> findPagination(Integer pageNo, Integer pageSize) {
		Pagination<SysResource> pagination = null;
		Query query = new Query();
		 query.addCriteria(Criteria.where("isDelete").is(false));
		try {
		pagination = this.findPaginationByQuery(query, pageNo, pageSize,
				SysResource.class);
		
		} catch (Exception e) {
			log.info("查询所有信息失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return Common.isNotEmpty(pagination)?pagination:new Pagination<SysResource>();
	}



	/* 
	 * <p>Title: findResourceMenu</p>  
	 * <p>Description: </p>  
	 * @param parentId
	 * @return  
	 * @see zhongchiedu.system.service.SysResourceService#findResourceMenu(java.lang.String)  
	 */
	@Override
	public List<SysResource> findSysResourceMenu(String parentId) {
		Query query= new Query();
		query.addCriteria(Criteria.where("parentId").is(parentId));
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("isDelete").is(false));
		List<SysResource> list = this.find(query,SysResource.class);
		return list;
	}

	/* 
	 * <p>Title: findSysResourceByName</p>  
	 * <p>Description: </p>  
	 * @param name
	 * @return  
	 * @see zhongchiedu.system.service.SysResourceService#findSysResourceByName(java.lang.String)  
	 */
	@Override
	public SysResource findSysResourceByName(String name) {
		Query  query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.findOneByQuery(query, SysResource.class);
		
		
	}



	/* 
	 * <p>Title: saveOrUpdate</p>  
	 * <p>Description: </p>  
	 * @param sysResource  
	 * @see zhongchiedu.system.service.SysResourceService#saveOrUpdate(zhongchiedu.system.pojo.SysResource)  
	 */
	@Override
	public void saveOrUpdate(SysResource sysResource) {
		
		if(Common.isNotEmpty(sysResource)) {
			SysResource parent = null;
			if(sysResource.getType() == 0) {
				sysResource.setParentId("0");
			} /*
				 * else { //type =1 添加的是菜单 parent = this.findOneById(sysResource.getParentId(),
				 * SysResource.class); if(Common.isNotEmpty(parent)) {
				 * sysResource.setResKey(parent.getResKey()+":"+ sysResource.getResKey()); } }
				 */
			if(Common.isNotEmpty(sysResource.getId())) {
				//update
				SysResource ed = this.findOneById(sysResource.getId(), SysResource.class);
				sysResource.setSysMenuAuthority(ed.getSysMenuAuthority());
				BeanUtils.copyProperties(sysResource, ed);
				this.save(sysResource);
				log.info("资源修改成功{}",sysResource.getId());
			}else {
				//insert
				sysResource.setSysMenuAuthority(new ArrayList<SysMenuAuthority>());
				this.insert(sysResource);
				log.info("添加资源按钮{}",sysResource);
			}
		}
			
	
	}

	/* 
	 * <p>Title: toDisable</p>  
	 * <p>Description: </p>  
	 * @param id
	 * @return  
	 * @see zhongchiedu.system.service.SysResourceService#toDisable(java.lang.String)  
	 */
	@Override
	public BasicDataResult toDisable(String id) {
		if(Common.isEmpty(id)) {
			return BasicDataResult.build(400, "禁用失败，请求出现问题，请刷新后重试", null);
		}
		SysResource sys = this.findOneById(id, SysResource.class);
		if(Common.isEmpty(sys)) {
			return BasicDataResult.build(400, "无法获取到资源信息，可能已经被删除", null);
		}
		sys.setIsDisable(sys.getIsDisable().equals(true)?false:true);
		this.save(sys);
		return BasicDataResult.build(200, sys.getIsDisable().equals(true)?"禁用成功":"启用成功",sys.getIsDisable());
	}

	/* 
	 * <p>Title: ajaxgetRepletes</p>  
	 * <p>Description: </p>  
	 * @param name
	 * @return  
	 * @see zhongchiedu.system.service.SysResourceService#ajaxgetRepletes(java.lang.String)  
	 */
	@Override
	public BasicDataResult ajaxgetRepletes(String name) {
		if(Common.isNotEmpty(name)) {
			SysResource sys = this.findSysResourceByName(name);
			if(Common.isNotEmpty(sys)) {
				return BasicDataResult.build(206,"数据已存在，请勿重复提交", null); 
			}
			return BasicDataResult.ok();
		}
		return BasicDataResult.build(400,"未能获取到请求的信息", null);
	}

	/* 
	 * <p>Title: delete</p>  
	 * <p>Description: </p>  
	 * @param id
	 * @return  
	 * @see zhongchiedu.system.service.SysResourceService#delete(java.lang.String)  
	 */
	private Lock lock = new ReentrantLock();
	@Override
	public String delete(String id) {
		try {
			lock.lock();
			List<String> ids = Arrays.asList(id.split(","));
			for (String edid : ids) {
				SysResource de = this.findOneById(edid, SysResource.class);
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
	 * <p>Title: findAllSysResources</p>  
	 * <p>Description: </p>  
	 * @return  
	 * @see zhongchiedu.system.service.SysResourceService#findAllSysResources()  
	 */
	@Override
	public List<SysResource> findAllSysResources() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.find(query, SysResource.class);
		
	}



	/* 
	 * <p>Title: createSysOperationAuthority</p>  
	 * <p>Description: </p>  
	 * @param param
	 * @return  
	 * @see zhongchiedu.system.service.SysResourceService#createSysOperationAuthority(java.lang.String)  
	 */
//	@Override
//	public BasicDataResult createSysOperationAuthority(String param) {
//		String id = Common.subStringBeforeOf("_", param);//当前编辑id
//		String operId = Common.subStringEndOf("_", param);
//		SysOperationAuthority so = this.sysOperationAuthorityService.findOneById(operId, SysOperationAuthority.class);
//		
//  		SysResource sr = this.findOneById(id, SysResource.class);
//  		
//		SysMenuAuthority sm = this.sysMenuAuthorityService.findSysMenuAuthority(operId, id);
//		
//		//SysResource parent = this.findOneById(sr.getParentId(), SysResource.class);
//		
//		String reskey = sr.getPermissionKey()+":"+so.getKey();
//		if(Common.isEmpty(sm)) {
//			//创建
//			 sm= new SysMenuAuthority();
//			 sm.setParentResourceId(sr.getId());
//			 sm.setResKey(reskey);
//			 sm.setSysOperationAuthority(so);
//			this.sysMenuAuthorityService.insert(sm);
//		}else {
//			sm.setResKey(reskey);
//			this.sysMenuAuthorityService.save(sm);
//		}
//		//更新操作权限
//		List<SysMenuAuthority> listsm = sr.getSysMenuAuthority()==null?new ArrayList<SysMenuAuthority>():sr.getSysMenuAuthority();
//		List<SysMenuAuthority> newsm = new ArrayList<>();
//		
//		if(listsm.size() == 0) {
//			listsm.add(sm);
//			sr.setSysMenuAuthority(listsm);
//			this.save(sr);
//			return BasicDataResult.build(200, "操作成功",null);
//		}else {
//			//判断是否存在，存在则去除
//			boolean flag = this.contain(listsm, sm.getId());
//			if(flag) {
//				//删除
//				for(SysMenuAuthority sys:listsm) {
//					if(!sys.getId().equals(sm.getId())) {
//						newsm.add(sys);
//					}
//				}
//				sr.setSysMenuAuthority(newsm);
//				this.save(sr);
////				//删除的时候需要把角色表中的权限也去掉
////				//获取所有的非删除状态下的角色
////				List<SysRole> listRoles = this.sysRoleService.findAllSysRole();
////				for(SysRole sysRole : listRoles) {
////					List<SysMenuAuthority> newRoleAuth = new ArrayList<>();
////					for(SysMenuAuthority sys:sysRole.getSysMenuAuthority()) {
////						if(!sys.getId().equals(sm.getId())) {
////							newRoleAuth.add(sys);
////						}
////					}
////					sysRole.setSysMenuAuthority(newRoleAuth);
////					this.sysRoleService.save(sysRole);
////				}
////				
//				
//				
//				return BasicDataResult.build(200, "操作成功",null);
//			}else {
//				//添加
//				listsm.add(sm);
//				sr.setSysMenuAuthority(listsm);
//				this.save(sr);
//				return BasicDataResult.build(200, "操作成功",null);
//			}
//		}
//		
//	}


//	public boolean contain(List<SysMenuAuthority> listsm,String containId) {
//		for(SysMenuAuthority sys:listsm) {
//			if(sys.getId().equals(containId)) {
//				return true;
//			}
//		}
//		return false;
//	}
//	
	
	
	
	
	
}
