/**
 * 
 */
package zhongchiedu.system.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.commons.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.system.pojo.SysOperationAuthority;
import zhongchiedu.system.service.SysOperationAuthorityService;

/**  
* <p>Title: SysOperationAuthorityServiceImpl</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2019年12月27日  
*/
@Slf4j
@Service
public class SysOperationAuthorityServiceImpl  extends GeneralServiceImpl<SysOperationAuthority> implements SysOperationAuthorityService{

	
	/* (non-Javadoc)  
	 * <p>Title: findSysOperationAuthorityByName</p>  
	 * <p>Description: 通过按钮名称查找</p>  
	 * @param name
	 * @return  
	 * @see zhongchiedu.system.service.SysOperationAuthorityService#findSysOperationAuthorityByName(java.lang.String)  
	 */
	@Override
	public SysOperationAuthority findSysOperationAuthorityByName(String name) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.findOneByQuery(query, SysOperationAuthority.class);

	}

	/* (non-Javadoc)  
	 * <p>Title: findAllSysOperationAuthorityByIsDisable</p>  
	 * <p>Description: 查询所有非禁用的按钮</p>  
	 * @return  
	 * @see zhongchiedu.system.service.SysOperationAuthorityService#findAllSysOperationAuthorityByIsDisable()  
	 */
	@Override
	public List<SysOperationAuthority> findAllSysOperationAuthorityByIsDisable() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.find(query, SysOperationAuthority.class);
	}

	/* (non-Javadoc)  
	 * <p>Title: findAllSysOperationAuthority</p>  
	 * <p>Description:获取所有的按钮 </p>  
	 * @return  
	 * @see zhongchiedu.system.service.SysOperationAuthorityService#findAllSysOperationAuthority()  
	 */
	@Override
	public List<SysOperationAuthority> findAllSysOperationAuthority() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.find(query, SysOperationAuthority.class);
	}

	/* (non-Javadoc)  
	 * <p>Title: saveOrUpdate</p>  
	 * <p>Description: </p>  
	 * @param sysOperationAuthority  
	 * @see zhongchiedu.system.service.SysOperationAuthorityService#saveOrUpdate(zhongchiedu.system.pojo.SysOperationAuthority)  
	 */
	@Override
	public void saveOrUpdate(SysOperationAuthority sysOperationAuthority) {
		if(Common.isNotEmpty(sysOperationAuthority)) {
			if(Common.isNotEmpty(sysOperationAuthority.getId())) {
				//update
				SysOperationAuthority ed = this.findOneById(sysOperationAuthority.getId(), SysOperationAuthority.class);
				BeanUtils.copyProperties(sysOperationAuthority, ed);
				this.save(sysOperationAuthority);
				log.info("按钮修改成功{}",sysOperationAuthority.getId());
			}else {
				//insert
				this.insert(sysOperationAuthority);
				log.info("添加按钮{}",sysOperationAuthority);
			}
		}
	}

	/* (non-Javadoc)  
	 * <p>Title: toDisable</p>  
	 * <p>Description: </p>  
	 * @param id
	 * @return  
	 * @see zhongchiedu.system.service.SysOperationAuthorityService#toDisable(java.lang.String)  
	 */
	@Override
	public BasicDataResult toDisable(String id) {
		if(Common.isEmpty(id)) {
			return BasicDataResult.build(400, "禁用失败，请求出现问题，请刷新后重试", null);
		}
		SysOperationAuthority sys = this.findOneById(id, SysOperationAuthority.class);
		if(Common.isEmpty(sys)) {
			return BasicDataResult.build(400, "无法获取到资源信息，可能已经被删除", null);
		}
		sys.setIsDisable(sys.getIsDisable().equals(true)?false:true);
		this.save(sys);
		return BasicDataResult.build(200, sys.getIsDisable().equals(true)?"禁用成功":"启用成功",sys.getIsDisable());
	}

	/* (non-Javadoc)  
	 * <p>Title: ajaxgetRepletes</p>  
	 * <p>Description: </p>  
	 * @param name
	 * @return  
	 * @see zhongchiedu.system.service.SysOperationAuthorityService#ajaxgetRepletes(java.lang.String)  
	 */
	@Override
	public BasicDataResult ajaxgetRepletes(String name) {
		if(Common.isNotEmpty(name)) {
			SysOperationAuthority sys = this.findSysOperationAuthorityByName(name);
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
	 * @see zhongchiedu.system.service.SysOperationAuthorityService#delete(java.lang.String)  
	 */
	private Lock lock = new ReentrantLock();
	@Override
	public String delete(String id) {
		try {
			lock.lock();
			List<String> ids = Arrays.asList(id.split(","));
			for (String edid : ids) {
				SysOperationAuthority de = this.findOneById(edid, SysOperationAuthority.class);
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
	 * <p>Title: findPagination</p>  
	 * <p>Description: </p>  
	 * @param pageNo
	 * @param pageSize
	 * @return  
	 * @see zhongchiedu.system.service.SysOperationAuthorityService#findPagination(java.lang.Integer, java.lang.Integer)  
	 */
	@Override
	public Pagination<SysOperationAuthority> findPagination(Integer pageNo, Integer pageSize) {
		
		Pagination<SysOperationAuthority> pagination = null;
		Query query = new Query();
		 query.addCriteria(Criteria.where("isDelete").is(false));
		try {
		pagination = this.findPaginationByQuery(query, pageNo, pageSize,
				SysOperationAuthority.class);
		
		} catch (Exception e) {
			log.info("查询所有信息失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return Common.isNotEmpty(pagination)?pagination:new Pagination<SysOperationAuthority>();
	}

	
	
}
