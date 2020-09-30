package zhongchiedu.school.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.commons.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.IndexSetting;
import zhongchiedu.school.pojo.WebMenu;
import zhongchiedu.school.service.IndexSettingService;
import zhongchiedu.school.service.WebMenuService;

/**
 * <p>
 * Title: SiteTemplateServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author 郭建波
 * @date 2019年12月30日
 */
@Service
@Slf4j
public class IndexSettingServiceImpl extends GeneralServiceImpl<IndexSetting> implements IndexSettingService {
	
	
	@Autowired
	private WebMenuService webMenuService;
	
	
	
	
	
	public Pagination<IndexSetting> findPagination(Integer pageNo, Integer pageSize) {
	Pagination<IndexSetting> pagination = null;
	Query query = new Query();
	 query.addCriteria(Criteria.where("isDelete").is(false));
		query.with(new Sort(new Order(Direction.ASC, "sort")));
	try {
	pagination = this.findPaginationByQuery(query, pageNo, pageSize,
			IndexSetting.class);
	
	} catch (Exception e) {
		log.info("查询所有信息失败——————————》" + e.toString());
		e.printStackTrace();
	}
	return Common.isNotEmpty(pagination)?pagination:new Pagination<IndexSetting>();
	}

	@Override
	public List<IndexSetting> findIndexSetting() {
		
		Query query  = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("isDelete").is(false));
		query.with(new Sort(new Order(Direction.ASC, "sort")));
		return this.find(query, IndexSetting.class);
	}

	@Override
	public void saveOrUpdate(IndexSetting indexSetting) {
		
		if(Common.isNotEmpty(indexSetting)) {
			
			List<WebMenu> webMenus = this.webMenuService.findWebMenuInIds(indexSetting.getMenuIds());
			if(Common.isNotEmpty(webMenus)) {
				indexSetting.setWebMenu(webMenus);
			}
		
			if(Common.isNotEmpty(indexSetting.getId())) {
				//update
				IndexSetting ed = this.findOneById(indexSetting.getId(), IndexSetting.class);
				//webMenu.setFirstLevel(Common.isNotEmpty(ed.getFirstLevel())?ed.getFirstLevel():null);
				BeanUtils.copyProperties(indexSetting, ed);
				this.save(indexSetting);
				log.info("修改成功{}",indexSetting.getId());
			}else {
				//insert
				this.insert(indexSetting);
				log.info("添加{}",indexSetting);
			}
		}
		
	}

	@Override
	public BasicDataResult toDisable(String id) {
		if(Common.isEmpty(id)) {
			return BasicDataResult.build(400, "禁用失败，请求出现问题，请刷新后重试", null);
		}
		IndexSetting sys = this.findOneById(id, IndexSetting.class);
		if(Common.isEmpty(sys)) {
			return BasicDataResult.build(400, "无法获取到资源信息，可能已经被删除", null);
		}
		sys.setIsDisable(sys.getIsDisable().equals(true)?false:true);
		this.save(sys);
		return BasicDataResult.build(200, sys.getIsDisable().equals(true)?"禁用成功":"启用成功",sys.getIsDisable());
	}

	private Lock lock = new ReentrantLock();
	@Override
	public String delete(String id) {
		try {
			lock.lock();
			List<String> ids = Arrays.asList(id.split(","));
			for (String edid : ids) {
				IndexSetting de = this.findOneById(edid, IndexSetting.class);
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
	public BasicDataResult findAndEditIndexSetting(String id, String sort) {

		IndexSetting index = this.findOneById(id, IndexSetting.class);
		
		if(Common.isNotEmpty(index)) {
			if(Common.isNotEmpty(sort)) {
				index.setSort(sort);
			}
			this.save(index);
			return BasicDataResult.build(200, "", null);
		}
		return BasicDataResult.build(400, "未能获取到数据", null);
	}
	
	




}
