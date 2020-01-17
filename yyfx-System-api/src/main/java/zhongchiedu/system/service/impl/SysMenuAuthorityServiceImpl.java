package zhongchiedu.system.service.impl;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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
import zhongchiedu.system.service.SysResourceService;

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
			//通过当前添加按钮所属的菜单查找根的key 
			String parentId = sysMenuAuthority.getParentResourceId();
			//通过parentId获取根id
			//SysResource sysResource = this.sysResourceService.findOneById(parentId, SysResource.class);
			
			//sysMenuAuthority.setResKey(sysResource.getResKey()+":"+sysMenuAuthority.getResKey());
			SysMenuAuthority sysmenu = this.findSysMenuAuthority(sysMenuAuthority.getSysOperationAuthority(),
					sysMenuAuthority.getParentResourceId());
			
			
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
	public SysMenuAuthority findSysMenuAuthority(SysOperationAuthority sop, String sysResourceId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sysOperationAuthority.$id").is(new ObjectId(sop.getId())));
		query.addCriteria(Criteria.where("parentResourceId").is(sysResourceId));
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
		query.addCriteria(Criteria.where("parentResourceId").is(sysResourceId));
		return this.findOneByQuery(query, SysMenuAuthority.class);
	}

}
