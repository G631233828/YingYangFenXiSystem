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
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.system.pojo.SysMenuAuthority;
import zhongchiedu.system.pojo.SysOperationAuthority;
import zhongchiedu.system.pojo.SysResource;
import zhongchiedu.system.service.SysMenuAuthorityService;
import zhongchiedu.system.service.SysOperationAuthorityService;
import zhongchiedu.system.service.SysResourceService;

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
	public void saveOrUpdate(SysResource sysResource,String[] operation) {
		
		if(Common.isNotEmpty(sysResource)) {
			if(sysResource.getType() == 0) {
				sysResource.setParentId("0");
			}
			if(sysResource.getType() == 1) {
				//拥有按钮权限
				String parentId = sysResource.getParentId();
				//获取当前添加菜单的父菜单
				SysResource sr = this.findOneById(parentId, SysResource.class);
				
				List<SysMenuAuthority> list = new  ArrayList<SysMenuAuthority>();
				for (String op : operation) {
					SysOperationAuthority sysop = 	this.sysOperationAuthorityService.findOneById(op, SysOperationAuthority.class);
					SysMenuAuthority sm = new SysMenuAuthority();
					sm.setSysOperationAuthority(sysop);
					sm.setParentResource(sr);
					this.sysMenuAuthorityService.saveOrUpdate(sm);
//					//sm.setResKey(sr.getResKey()+":"+sysResource.getResKey()+":"+sysop.getKey());
					SysMenuAuthority sys = this.sysMenuAuthorityService.findSysMenuAuthority(sysop, sr);
					list.add(sys);
				}
				sysResource.setSysMenuAuthority(list);
			}
			   
			if(Common.isNotEmpty(sysResource.getId())) {
				//update
				SysResource ed = this.findOneById(sysResource.getId(), SysResource.class);
				BeanUtils.copyProperties(sysResource, ed);
				this.save(sysResource);
				log.info("资源修改成功{}",sysResource.getId());
			}else {
				//insert
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

}