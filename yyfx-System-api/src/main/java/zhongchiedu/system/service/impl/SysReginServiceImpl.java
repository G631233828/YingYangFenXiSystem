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
import zhongchiedu.system.pojo.SysRegin;
import zhongchiedu.system.pojo.SysRole;
import zhongchiedu.system.service.SysReginService;

/**  
* <p>Title: SysReginServiceImpl</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月14日  
*/
@Service
@Slf4j
public class SysReginServiceImpl extends GeneralServiceImpl<SysRegin> implements SysReginService{

	/* 
	 * <p>Title: findPagination</p>  
	 * <p>Description: </p>  
	 * @param pageNo
	 * @param pageSize
	 * @return  
	 * @see zhongchiedu.system.service.SysReginService#findPagination(java.lang.Integer, java.lang.Integer)  
	 */
	@Override
	public Pagination<SysRegin> findPagination(Integer pageNo, Integer pageSize) {
		Pagination<SysRegin> pagination = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		try {
			pagination = this.findPaginationByQuery(query, pageNo, pageSize, SysRegin.class);

		} catch (Exception e) {
			log.info("查询所有信息失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return Common.isNotEmpty(pagination) ? pagination : new Pagination<SysRegin>();
	}

	/* 
	 * <p>Title: findSysReginByName</p>  
	 * <p>Description: </p>  
	 * @param name
	 * @return  
	 * @see zhongchiedu.system.service.SysReginService#findSysReginByName(java.lang.String)  
	 */
	@Override
	public SysRegin findSysReginByName(String name) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.findOneByQuery(query, SysRegin.class);
	}

	/* 
	 * <p>Title: findAllSysReginByIsDisable</p>  
	 * <p>Description: </p>  
	 * @return  
	 * @see zhongchiedu.system.service.SysReginService#findAllSysReginByIsDisable()  
	 */
	@Override
	public List<SysRegin> findAllSysReginByIsDisable() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.find(query, SysRegin.class);

	}

	/* 
	 * <p>Title: findAllSysRegin</p>  
	 * <p>Description: </p>  
	 * @return  
	 * @see zhongchiedu.system.service.SysReginService#findAllSysRegin()  
	 */
	@Override
	public List<SysRegin> findAllSysRegin() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.find(query, SysRegin.class);

	}

	/* 
	 * <p>Title: saveOrUpdate</p>  
	 * <p>Description: </p>  
	 * @param sSysRegin  
	 * @see zhongchiedu.system.service.SysReginService#saveOrUpdate(zhongchiedu.system.pojo.SysRegin)  
	 */
	@Override
	public void saveOrUpdate(SysRegin sysRegin) {
		if (Common.isNotEmpty(sysRegin)) {
			if (Common.isNotEmpty(sysRegin.getId())) {
				// update
				SysRegin  ed = this.findOneById(sysRegin.getId(), SysRegin.class);
				BeanUtils.copyProperties(sysRegin, ed);
				this.save(sysRegin);
				log.info("修改地区成功{}", sysRegin.getId());
			} else {
				// insert
				this.insert(sysRegin);
				log.info("添加地区{}", sysRegin);
			}
		}
	}

	/* 
	 * <p>Title: toDisable</p>  
	 * <p>Description: </p>  
	 * @param id
	 * @return  
	 * @see zhongchiedu.system.service.SysReginService#toDisable(java.lang.String)  
	 */
	@Override
	public BasicDataResult toDisable(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "禁用失败，请求出现问题，请刷新后重试", null);
		}
		SysRegin sys = this.findOneById(id, SysRegin.class);
		if (Common.isEmpty(sys)) {
			return BasicDataResult.build(400, "无法获取到资源信息，可能已经被删除", null);
		}
		sys.setIsDisable(sys.getIsDisable().equals(true) ? false : true);
		this.save(sys);
		return BasicDataResult.build(200, sys.getIsDisable().equals(true) ? "禁用成功" : "启用成功", sys.getIsDisable());
	
	}

	/* 
	 * <p>Title: ajaxgetRepletes</p>  
	 * <p>Description: </p>  
	 * @param name
	 * @return  
	 * @see zhongchiedu.system.service.SysReginService#ajaxgetRepletes(java.lang.String)  
	 */
	@Override
	public BasicDataResult ajaxgetRepletes(String name) {
		if (Common.isNotEmpty(name)) {
			SysRegin sys = this.findSysReginByName(name);
			if (Common.isNotEmpty(sys)) {
				return BasicDataResult.build(206, "数据已存在，请勿重复提交", null);
			}
			return BasicDataResult.ok();
		}
		return BasicDataResult.build(400, "未能获取到请求的信息", null);
	}

	/* 
	 * <p>Title: delete</p>  
	 * <p>Description: </p>  
	 * @param id
	 * @return  
	 * @see zhongchiedu.system.service.SysReginService#delete(java.lang.String)  
	 */
	private Lock lock = new ReentrantLock();

	@Override
	public String delete(String id) {
		try {
			lock.lock();
			List<String> ids = Arrays.asList(id.split(","));
			for (String edid : ids) {
				SysRegin de = this.findOneById(edid, SysRegin.class);
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

}
