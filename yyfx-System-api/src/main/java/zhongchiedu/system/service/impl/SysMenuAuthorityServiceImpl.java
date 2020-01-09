package zhongchiedu.system.service.impl;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.Common;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.system.pojo.SysMenuAuthority;
import zhongchiedu.system.pojo.SysOperationAuthority;
import zhongchiedu.system.pojo.SysResource;
import zhongchiedu.system.service.SysMenuAuthorityService;

/**
 * <p>
 * Title: SysMenuAuthorityServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author 郭建波
 * @date 2020年1月2日
 */
@Slf4j
@Service
public class SysMenuAuthorityServiceImpl extends GeneralServiceImpl<SysMenuAuthority>
		implements SysMenuAuthorityService {
	/*
	 * <p>Title: saveOrUpdate</p> <p>Description: </p>
	 * @param sysMenuAuthority
	 * @see
	 * zhongchiedu.system.service.SysMenuAuthorityService#saveOrUpdate(zhongchiedu.
	 * system.pojo.SysMenuAuthority)
	 */
	@Override
	public void saveOrUpdate(SysMenuAuthority sysMenuAuthority) {
		if (Common.isNotEmpty(sysMenuAuthority)) {
			SysMenuAuthority sysmenu = this.findSysMenuAuthority(sysMenuAuthority.getSysOperationAuthority(),
					sysMenuAuthority.getParentResource());
			if(Common.isEmpty(sysmenu)) {
				this.save(sysMenuAuthority);
			}
		}
	}

	/*
	 * <p>Title: findSysMenuAuthority</p> <p>Description: </p>
	 * @param sop
	 * @param sr
	 * @return
	 * @see zhongchiedu.system.service.SysMenuAuthorityService#findSysMenuAuthority(
	 * zhongchiedu.system.pojo.SysOperationAuthority,
	 * zhongchiedu.system.pojo.SysResource)
	 */
	@Override
	public SysMenuAuthority findSysMenuAuthority(SysOperationAuthority sop, SysResource sr) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sysOperationAuthority.$id").is(new ObjectId(sop.getId())));
		query.addCriteria(Criteria.where("parentResource.$id").is(new ObjectId(sr.getId())));
		return this.findOneByQuery(query, SysMenuAuthority.class);

	}

	/* 
	 * <p>Title: findSysMenuAuthority</p>  
	 * <p>Description: </p>  
	 * @param sysOperationAuthorityId
	 * @param sysResourceId
	 * @return  
	 * @see zhongchiedu.system.service.SysMenuAuthorityService#findSysMenuAuthority(java.lang.String, java.lang.String)  
	 */
	@Override
	public SysMenuAuthority findSysMenuAuthority(String sysOperationAuthorityId, String sysResourceId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sysOperationAuthority.$id").is(new ObjectId(sysOperationAuthorityId)));
		query.addCriteria(Criteria.where("parentResource.$id").is(new ObjectId(sysResourceId)));
		return this.findOneByQuery(query, SysMenuAuthority.class);
	}

}