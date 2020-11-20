package zhongchiedu.school.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.commons.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.News;
import zhongchiedu.school.pojo.WebMenu;
import zhongchiedu.school.service.NewsService;
import zhongchiedu.school.service.WebMenuService;
import zhongchiedu.system.pojo.MultiMedia;
import zhongchiedu.system.service.impl.MultiMediaServiceImpl;

/**
 * <p>
 * Title: WebMenuServiceImpl
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
public class WebMenuServiceImpl extends GeneralServiceImpl<WebMenu> implements WebMenuService {

	@Autowired
	private MultiMediaServiceImpl multiMediaSerice;
	
	@Autowired
	@Lazy(true)
	private NewsService newsService;

	@Override
	public Pagination<WebMenu> findPagination(Integer pageNo, Integer pageSize) {
		Pagination<WebMenu> pagination = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		query.with(new Sort(new Order(Direction.ASC, "sort")));

		try {
			pagination = this.findPaginationByQuery(query, pageNo, pageSize, WebMenu.class);

		} catch (Exception e) {
			log.info("查询所有信息失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return Common.isNotEmpty(pagination) ? pagination : new Pagination<WebMenu>();
	}

	@Override
	public List<WebMenu> findWebMenu(String parentId, boolean weiWeb, Integer type) {
		Query query = new Query();
		if (Common.isNotEmpty(type)) {
			query.addCriteria(Criteria.where("type").is(type));
		}
		if (weiWeb) {
			query.addCriteria(Criteria.where("weiWeb").is(weiWeb));
		}

		query.addCriteria(Criteria.where("parentId").is(parentId));
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("isDelete").is(false));
		List<WebMenu> list = this.find(query, WebMenu.class);
		return list;
	}

	@Override
	public void saveOrUpdate(WebMenu webMenu, MultipartFile[] img, String oldImg, String imgPath, String dir) {
		WebMenu ed = null;
		if (Common.isNotEmpty(webMenu)) {
			if (webMenu.getType() == 0) {
				webMenu.setParentId("0");
			}
			if (img.length > 0) {
				List<MultiMedia> pic = this.multiMediaSerice.uploadPictures(img, dir, imgPath, "WEBMENUIMG", 300, 300);

				if (Common.isNotEmpty(webMenu.getId())) {
					ed = this.findOneById(webMenu.getId(), WebMenu.class);
					
					if (Common.isNotEmpty(ed)) {
						webMenu.setImg(Common.isNotEmpty(oldImg) ? ed.getImg() : null);
					}
				}
				if (pic.size() > 0) {
					webMenu.setImg(pic.get(0));
				}
			}
			if (Common.isNotEmpty(webMenu.getId())) {
				//判断firstLevel是否发生改变，如果发生改变了需要修改news 中所有WebMenu跟supMenu
				if(webMenu.getFirstLevel()==null||!webMenu.getFirstLevel().equals(ed.getFirstLevel())) {
					System.out.println("菜单目录发生了变化");
					this.updateNewsByWebMenu(webMenu, ed);
				}
				// update
				// WebMenu ed = this.findOneById(webMenu.getId(), WebMenu.class);
				// webMenu.setFirstLevel(Common.isNotEmpty(ed.getFirstLevel())?ed.getFirstLevel():null);
				BeanUtils.copyProperties(webMenu, ed);
				uploadWebMenuVersion(webMenu.getId());
				this.save(webMenu);
				
				
				
				
				
				log.info("资源修改成功{}", webMenu.getId());
			} else {
				// insert
				uploadWebMenuVersion(webMenu.getId());
				this.insert(webMenu);
				log.info("添加资源按钮{}", webMenu);
			}
		}

	}

	@Override
	public BasicDataResult toDisable(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "禁用失败，请求出现问题，请刷新后重试", null);
		}
		WebMenu sys = this.findOneById(id, WebMenu.class);
		if (Common.isEmpty(sys)) {
			return BasicDataResult.build(400, "无法获取到资源信息，可能已经被删除", null);
		}
		sys.setIsDisable(sys.getIsDisable().equals(true) ? false : true);
		this.save(sys);
		uploadWebMenuVersion(id);
		return BasicDataResult.build(200, sys.getIsDisable().equals(true) ? "禁用成功" : "启用成功", sys.getIsDisable());

	}

	private Lock lock = new ReentrantLock();

	@Override
	public String delete(String id) {
		try {
			lock.lock();
			List<String> ids = Arrays.asList(id.split(","));
			for (String edid : ids) {
				WebMenu de = this.findOneById(edid, WebMenu.class);
				de.setIsDelete(true);
				this.save(de);
			}
			uploadWebMenuVersion(id);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return "error";
	}

	@Override
	public BasicDataResult ajaxgetRepletes(String name) {
		if (Common.isNotEmpty(name)) {
			WebMenu sys = this.findWebMenuByName(name);
			if (Common.isNotEmpty(sys)) {
				return BasicDataResult.build(206, "数据已存在，请勿重复提交", null);
			}
			return BasicDataResult.ok();
		}
		return BasicDataResult.build(400, "未能获取到请求的信息", null);
	}

	@Override
	public WebMenu findWebMenuByName(String name) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.findOneByQuery(query, WebMenu.class);
	}

	@Override
	public WebMenu findWebMenuById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.findOneByQuery(query, WebMenu.class);

	}

	@Override
	public List<WebMenu> findWebMenuByFirstLevel(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("firstLevel").is(id));
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("isDelete").is(false));
		return this.find(query, WebMenu.class);
	}

	@Override
	public List<WebMenu> findWebMenuInIds(String menuIds) {
		Query query = new Query();
		if (Common.isNotEmpty(menuIds)) {
			 List<String> ids = Arrays.asList(menuIds.split(","));
			query.addCriteria(Criteria.where("isDisable").is(false));
			query.addCriteria(Criteria.where("isDelete").is(false));
			query.addCriteria(Criteria.where("_id").in(ids));
		}
		return this.find(query, WebMenu.class);

	}

	
	
	

	/**
	 * 更新网站菜单版本
	 */
	public void uploadWebMenuVersion(String id) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				uploadWebMenuVersionByThread(id);
			}
		}).start();
	}

	/**
	 * 菜单版本+1
	 * 
	 * @param id
	 */
	public synchronized void uploadWebMenuVersionByThread(String id) {
		WebMenu menu = this.findOneById(id, WebMenu.class);
		WebMenu supMenu = null;
		if(menu.getParentId()!="0") {
			supMenu = this.findOneById(menu.getParentId(), WebMenu.class);
		}
		if(Common.isNotEmpty(supMenu)) {
			menu = supMenu;
		}
		 menu.setVersion( menu.getVersion() + 1);
		this.save(menu);
	}

	//更新新闻的 webMenu 跟supMenu
	@Override
	public void updateNewsByWebMenu(WebMenu newwebmenu, WebMenu oldwebMenu) {
		WebMenu supMenu = null;
		String id = "";
		if(Common.isEmpty(newwebmenu.getFirstLevel())) {
			id= newwebmenu.getParentId();
		}else {
			id = newwebmenu.getFirstLevel();
		}
		 supMenu= this.findOneById(id, WebMenu.class);
		List<News> getnews = this.newsService.findNewsByWebMenuId(oldwebMenu.getId());
		for(News news:getnews) {
			news.setWebMenu(newwebmenu);
			news.setSupMenu(supMenu);
			this.newsService.save(news);
		}
		
	}
	
	
	
	
	
	
	
	
	
}
