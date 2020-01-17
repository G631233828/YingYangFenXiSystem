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
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.commons.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.system.pojo.MultiMedia;
import zhongchiedu.system.pojo.SysRole;
import zhongchiedu.system.pojo.SysUser;
import zhongchiedu.system.service.SysUserService;

/**  
* <p>Title: SysUserServiceImpl</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月9日  
*/
@Service
@Slf4j
public class SysUserServiceImpl extends GeneralServiceImpl<SysUser> implements SysUserService{

	
	
	@Autowired
	private SysRoleServiceImpl sysRoleService;
	
	@Autowired
	private MultiMediaServiceImpl multiMediaSerice;
	
	
	
	/* 
	 * <p>Title: findPagination</p>  
	 * <p>Description: </p>  
	 * @param pageNo
	 * @param pageSize
	 * @return  
	 * @see zhongchiedu.system.service.SysUserService#findPagination(java.lang.Integer, java.lang.Integer)  
	 */
	@Override
	public Pagination<SysUser> findPagination(Integer pageNo, Integer pageSize) {
		Pagination<SysUser> pagination = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		try {
			pagination = this.findPaginationByQuery(query, pageNo, pageSize, SysUser.class);

		} catch (Exception e) {
			log.info("查询所有信息失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return Common.isNotEmpty(pagination) ? pagination : new Pagination<SysUser>();
	}

	/* 
	 * <p>Title: findSysUserByAccountName</p>  
	 * <p>Description: </p>  
	 * @param account
	 * @return  
	 * @see zhongchiedu.system.service.SysUserService#findSysUserByAccountName(java.lang.String)  
	 */
	@Override
	public SysUser findSysUserByAccountName(String accountName) {
		Query query = new Query();
		query.addCriteria(Criteria.where("accountName").is(accountName));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.findOneByQuery(query, SysUser.class);
	}

	/* 
	 * <p>Title: findAllSysUserByIsDisable</p>  
	 * <p>Description: </p>  
	 * @return  
	 * @see zhongchiedu.system.service.SysUserService#findAllSysUserByIsDisable()  
	 */
	@Override
	public List<SysUser> findAllSysUserByIsDisable() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.find(query, SysUser.class);
	}

	/* 
	 * <p>Title: findAllSysUser</p>  
	 * <p>Description: </p>  
	 * @return  
	 * @see zhongchiedu.system.service.SysUserService#findAllSysUser()  
	 */
	@Override
	public List<SysUser> findAllSysUser() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.find(query, SysUser.class);
	}

	/* 
	 * <p>Title: saveOrUpdate</p>  
	 * <p>Description: </p>  
	 * @param sysUser  
	 * @see zhongchiedu.system.service.SysUserService#saveOrUpdate(zhongchiedu.system.pojo.SysUser)  
	 */
	@Override
	public void saveOrUpdate(SysUser user,String roleId ,MultipartFile[] file ,String imgPath,String dir,String oldheadImg) {
		SysUser getUser = null;
		List<MultiMedia> userHead = null; 
			userHead = this.multiMediaSerice.uploadPictures(file, dir, imgPath, "USER",105,105);
	
		if(Common.isNotEmpty(user.getId())){
			getUser = this.findOneById(user.getId(), SysUser.class);
			if(Common.isNotEmpty(getUser)){
				user.setPhotograph(Common.isNotEmpty(oldheadImg)?getUser.getPhotograph():null);
				user.setAccountName(getUser.getAccountName());
			}
		}
		if(userHead.size()>0){
			user.setPhotograph(userHead.get(0));
		}
		SysRole role = this.sysRoleService.findOneById(roleId, SysRole.class);
		user.setRole(role != null ? role : null);
		if(Common.isNotEmpty(getUser)){
			BeanUtils.copyProperties(user, getUser);
			this.save(user);
		}else{
			this.insert(user);
		}
		
	}

	/* 
	 * <p>Title: toDisable</p>  
	 * <p>Description: </p>  
	 * @param id
	 * @return  
	 * @see zhongchiedu.system.service.SysUserService#toDisable(java.lang.String)  
	 */
	@Override
	public BasicDataResult toDisable(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "禁用失败，请求出现问题，请刷新后重试", null);
		}
		SysUser sys = this.findOneById(id, SysUser.class);
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
	 * @see zhongchiedu.system.service.SysUserService#ajaxgetRepletes(java.lang.String)  
	 */
	@Override
	public BasicDataResult ajaxgetRepletes(String name) {
		if (Common.isNotEmpty(name)) {
			SysUser sys = this.findSysUserByAccountName(name);
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
	 * @see zhongchiedu.system.service.SysUserService#delete(java.lang.String)  
	 */
	private Lock lock = new ReentrantLock();
	
	@Override
	public String delete(String id,SysUser suser) {
		try {
			lock.lock();
			List<String> ids = Arrays.asList(id.split(","));
			String loginid = Common.isNotEmpty(suser)?suser.getId():"";
			for (String edid : ids) {
				//不能删除自己的账号
				if(edid!=loginid) {
					SysUser de = this.findOneById(edid, SysUser.class);
					de.setIsDelete(true);
					this.save(de);
				}
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
	 * <p>Title: checkPassword</p>  
	 * <p>Description: </p>  
	 * @param id
	 * @param password
	 * @return  
	 * @see zhongchiedu.system.service.SysUserService#checkPassword(java.lang.String, java.lang.String)  
	 */
	@Override
	public BasicDataResult checkPassword(String id, String password) {
		if(Common.isEmpty(id)){
			return BasicDataResult.build(400, "发生未知异常，请联系管理员！", null);
		}
		if(Common.isEmpty(password)){
			return BasicDataResult.build(400, "请输入旧密码", null);
		}
		SysUser user = this.findOneById(id, SysUser.class);
		if(Common.isEmpty(user)){
			return BasicDataResult.build(400, "未能获取到数据，请刷新后再试", null);
		}
		if(user.getPassWord().equals(password)){
			return BasicDataResult.build(200, "密码正确", null);
		}
		return BasicDataResult.build(400, "发生未知异常，请联系管理员！", null);
	}

	/* 
	 * <p>Title: editPassword</p>  
	 * <p>Description: </p>  
	 * @param id
	 * @param password
	 * @return  
	 * @see zhongchiedu.system.service.SysUserService#editPassword(java.lang.String, java.lang.String)  
	 */
	@Override
	public BasicDataResult editPassword(String id, String password) {
		if(Common.isEmpty(id)){
			return BasicDataResult.build(400, "发生未知异常，请联系管理员！", null);
		}
		if(Common.isEmpty(password)){
			return BasicDataResult.build(400, "新密码不能为空", null);
		}
		SysUser user = this.findOneById(id, SysUser.class);
		if(Common.isEmpty(user)){
			return BasicDataResult.build(400, "未能获取到数据，请刷新后再试", null);
		}
		user.setPassWord(password);
		this.save(user);
		return BasicDataResult.build(200, "密码修改成功", null);
	}

}
