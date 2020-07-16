/**
 * 
 */
package zhongchiedu.system.service.impl;

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
import zhongchiedu.system.pojo.SysSchool;
import zhongchiedu.system.pojo.SysUnit;
import zhongchiedu.system.service.SysUnitService;

/**  
* <p>Title: SysUnitServiceImpl</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月9日  
*/
@Service
@Slf4j
public class SysUnitServiceImpl extends GeneralServiceImpl<SysUnit> implements SysUnitService{

	
	
	
	/* 
	 * <p>Title: findPagination</p>  
	 * <p>Description: </p>  
	 * @param pageNo
	 * @param pageSize
	 * @return  
	 * @see zhongchiedu.system.service.SysUnitService#findPagination(java.lang.Integer, java.lang.Integer)  
	 */
	@Override
	public Pagination<SysUnit> findPagination(Integer pageNo, Integer pageSize) {
		Pagination<SysUnit> pagination = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));

		try {
			pagination = this.findPaginationByQuery(query, pageNo, pageSize, SysUnit.class);

		} catch (Exception e) {
			log.info("查询所有信息失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return Common.isNotEmpty(pagination) ? pagination : new Pagination<SysUnit>();
	}

	/* 
	 * <p>Title: findSysUnitByAccountName</p>  
	 * <p>Description: </p>  
	 * @param account
	 * @return  
	 * @see zhongchiedu.system.service.SysUnitService#findSysUnitByAccountName(java.lang.String)  
	 */
	@Override
	public SysUnit findSysUnitByName(String name) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.findOneByQuery(query, SysUnit.class);
	}

	/* 
	 * <p>Title: findAllSysUnitByIsDisable</p>  
	 * <p>Description: </p>  
	 * @return  
	 * @see zhongchiedu.system.service.SysUnitService#findAllSysUnitByIsDisable()  
	 */
	@Override
	public List<SysUnit> findAllSysUnitByIsDisable() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.find(query, SysUnit.class);
	}

	/* 
	 * <p>Title: findAllSysUnit</p>  
	 * <p>Description: </p>  
	 * @return  
	 * @see zhongchiedu.system.service.SysUnitService#findAllSysUnit()  
	 */
	@Override
	public List<SysUnit> findAllSysUnit() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.find(query, SysUnit.class);
	}

	/* 
	 * <p>Title: saveOrUpdate</p>  
	 * <p>Description: </p>  
	 * @param sysUser  
	 * @see zhongchiedu.system.service.SysUnitService#saveOrUpdate(zhongchiedu.system.pojo.SysUnit)  
	 */
	@Override
	public void saveOrUpdate(SysUnit unit) {
		if (Common.isNotEmpty(unit)) {
			if (Common.isNotEmpty(unit.getId())) {
				// update
				SysUnit  ed = this.findOneById(unit.getId(), SysUnit.class);
				BeanUtils.copyProperties(unit, ed);
				this.save(unit);
				log.info("修改计量单位成功{}", unit.getId());
			} else {
				// insert
				this.insert(unit);
				log.info("添加计量单位{}", unit);
			}
		}
	}

	/* 
	 * <p>Title: toDisable</p>  
	 * <p>Description: </p>  
	 * @param id
	 * @return  
	 * @see zhongchiedu.system.service.SysUnitService#toDisable(java.lang.String)  
	 */
	@Override
	public BasicDataResult toDisable(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "禁用失败，请求出现问题，请刷新后重试", null);
		}
		SysUnit sys = this.findOneById(id, SysUnit.class);
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
	 * @see zhongchiedu.system.service.SysUnitService#ajaxgetRepletes(java.lang.String)  
	 */
	@Override
	public BasicDataResult ajaxgetRepletes(String name) {
		if (Common.isNotEmpty(name)) {
			SysUnit sys = this.findSysUnitByName(name);
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
	 * @see zhongchiedu.system.service.SysUnitService#delete(java.lang.String)  
	 */
	private Lock lock = new ReentrantLock();
	
	@Override
	public String delete(String id) {
		try {
			lock.lock();
			List<String> ids = Arrays.asList(id.split(","));
			for (String edid : ids) {
				SysUnit de = this.findOneById(edid, SysUnit.class);
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
