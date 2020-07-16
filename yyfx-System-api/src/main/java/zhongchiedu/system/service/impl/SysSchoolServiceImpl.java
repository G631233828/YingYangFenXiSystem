/**
 * 
 */
package zhongchiedu.system.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.types.ObjectId;
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
import zhongchiedu.system.pojo.SysSchool;
import zhongchiedu.system.service.SysSchoolService;

/**  
* <p>Title: SysSchoolServiceImpl</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月15日  
*/
@Service
@Slf4j
public class SysSchoolServiceImpl extends GeneralServiceImpl<SysSchool> implements SysSchoolService {

	/* 
	 * <p>Title: findPagination</p>  
	 * <p>Description: </p>  
	 * @param pageNo
	 * @param pageSize
	 * @return  
	 * @see zhongchiedu.system.service.SysSchoolService#findPagination(java.lang.Integer, java.lang.Integer)  
	 */
	@Override
	public Pagination<SysSchool> findPagination(Integer pageNo, Integer pageSize) {
		Pagination<SysSchool> pagination = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		try {
			pagination = this.findPaginationByQuery(query, pageNo, pageSize, SysSchool.class);

		} catch (Exception e) {
			log.info("查询所有信息失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return Common.isNotEmpty(pagination) ? pagination : new Pagination<SysSchool>();
	}

	/* 
	 * <p>Title: findSysSchoolByName</p>  
	 * <p>Description: </p>  
	 * @param name
	 * @return  
	 * @see zhongchiedu.system.service.SysSchoolService#findSysSchoolByName(java.lang.String)  
	 */
	@Override
	public SysSchool findSysSchoolByName(String name) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.findOneByQuery(query, SysSchool.class);
	}

	/* 
	 * <p>Title: findAllSysSchoolByIsDisable</p>  
	 * <p>Description: </p>  
	 * @return  
	 * @see zhongchiedu.system.service.SysSchoolService#findAllSysSchoolByIsDisable()  
	 */
	@Override
	public List<SysSchool> findAllSysSchoolByIsDisable() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.find(query, SysSchool.class);
	}

	/* 
	 * <p>Title: findAllSysSchool</p>  
	 * <p>Description: </p>  
	 * @return  
	 * @see zhongchiedu.system.service.SysSchoolService#findAllSysSchool()  
	 */
	@Override
	public List<SysSchool> findAllSysSchool() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.find(query, SysSchool.class);
	}

	/* 
	 * <p>Title: saveOrUpdate</p>  
	 * <p>Description: </p>  
	 * @param sysSchool  
	 * @see zhongchiedu.system.service.SysSchoolService#saveOrUpdate(zhongchiedu.system.pojo.SysSchool)  
	 */
	@Override
	public void saveOrUpdate(SysSchool sysSchool) {
		if (Common.isNotEmpty(sysSchool)) {
			if (Common.isNotEmpty(sysSchool.getId())) {
				// update
				SysSchool  ed = this.findOneById(sysSchool.getId(), SysSchool.class);
				BeanUtils.copyProperties(sysSchool, ed);
				this.save(sysSchool);
				log.info("修改学校成功{}", sysSchool.getId());
			} else {
				// insert
				this.insert(sysSchool);
				log.info("添加学校{}", sysSchool);
			}
		}
	}

	/* 
	 * <p>Title: toDisable</p>  
	 * <p>Description: </p>  
	 * @param id
	 * @return  
	 * @see zhongchiedu.system.service.SysSchoolService#toDisable(java.lang.String)  
	 */
	@Override
	public BasicDataResult toDisable(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "禁用失败，请求出现问题，请刷新后重试", null);
		}
		SysSchool sys = this.findOneById(id, SysSchool.class);
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
	 * @see zhongchiedu.system.service.SysSchoolService#ajaxgetRepletes(java.lang.String)  
	 */
	@Override
	public BasicDataResult ajaxgetRepletes(String name) {
		if (Common.isNotEmpty(name)) {
			SysSchool sys = this.findSysSchoolByName(name);
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
	 * @see zhongchiedu.system.service.SysSchoolService#delete(java.lang.String)  
	 */
	private Lock lock = new ReentrantLock();

	@Override
	public String delete(String id) {
		try {
			lock.lock();
			List<String> ids = Arrays.asList(id.split(","));
			for (String edid : ids) {
				SysSchool de = this.findOneById(edid, SysSchool.class);
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

	@Override
	public SysSchool findSysSchoolById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.findOneByQuery(query, SysSchool.class);
	}

}
