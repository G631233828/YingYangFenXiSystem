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
import zhongchiedu.system.pojo.IpConfigs;
import zhongchiedu.system.pojo.SysSchool;
import zhongchiedu.system.pojo.SysUnit;
import zhongchiedu.system.pojo.SysUser;
import zhongchiedu.system.service.IpConfigsService;

@Service
@Slf4j
public class IpConfigsServiceImpl extends GeneralServiceImpl<IpConfigs> implements IpConfigsService {

	@Override
	public Pagination<IpConfigs> findPagination(Integer pageNo, Integer pageSize) {
		Pagination<IpConfigs> pagination = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		try {
			pagination = this.findPaginationByQuery(query, pageNo, pageSize, IpConfigs.class);

		} catch (Exception e) {
			log.info("查询所有信息失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return Common.isNotEmpty(pagination) ? pagination : new Pagination<IpConfigs>();
	}

	@Override
	public List<IpConfigs> findWhiteOrBlackList(boolean whiteOrBlack) {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("isDelete").is(false));
		query.addCriteria(Criteria.where("whiteOrBlack").is(whiteOrBlack));
		return this.find(query, IpConfigs.class);
	}

	@Override
	public BasicDataResult ajaxgetRepletes(String ip) {
		if (Common.isNotEmpty(ip)) {
			IpConfigs ipConfigs = this.findByIp(ip);
			if (Common.isNotEmpty(ipConfigs)) {
				return BasicDataResult.build(206, "数据已存在，请勿重复提交", null);
			}
			return BasicDataResult.ok();
		}
		return BasicDataResult.build(400, "未能获取到请求的信息", null);
	}

	private Lock lock = new ReentrantLock();

	@Override
	public String delete(String id) {
		try {
			lock.lock();
			List<String> ids = Arrays.asList(id.split(","));
			for (String edid : ids) {
				IpConfigs de = this.findOneById(edid, IpConfigs.class);
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
	public IpConfigs findByIp(String ip) {
		Query query = new Query();
		query.addCriteria(Criteria.where("ip").is(ip));
		query.addCriteria(Criteria.where("isDelete").is(false));

		return this.findOneByQuery(query, IpConfigs.class);
	}

	@Override
	public void saveOrUpdate(IpConfigs ipConfigs) {
		if (Common.isNotEmpty(ipConfigs)) {
			if (Common.isNotEmpty(ipConfigs.getId())) {
				// update
				IpConfigs  ed = this.findOneById(ipConfigs.getId(), IpConfigs.class);
				BeanUtils.copyProperties(ipConfigs, ed);
				this.save(ipConfigs);
				log.info("修改ip成功{}", ipConfigs.getId());
			} else {
				// insert
				this.insert(ipConfigs);
				log.info("添加ip{}", ipConfigs);
			}
		}
		
	}

	@Override
	public BasicDataResult toDisable(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "禁用失败，请求出现问题，请刷新后重试", null);
		}
		IpConfigs sys = this.findOneById(id, IpConfigs.class);
		if (Common.isEmpty(sys)) {
			return BasicDataResult.build(400, "无法获取到资源信息，可能已经被删除", null);
		}
		sys.setIsDisable(sys.getIsDisable().equals(true) ? false : true);
		this.save(sys);
		return BasicDataResult.build(200, sys.getIsDisable().equals(true) ? "禁用成功" : "启用成功", sys.getIsDisable());
	
	}

}
