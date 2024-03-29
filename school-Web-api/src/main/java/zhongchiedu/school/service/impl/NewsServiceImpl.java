package zhongchiedu.school.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import zhongchiedu.commons.utils.Contents;
import zhongchiedu.commons.utils.UserType;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.IndexSetting;
import zhongchiedu.school.pojo.News;
import zhongchiedu.school.pojo.WebMenu;
import zhongchiedu.school.service.NewsService;
import zhongchiedu.school.service.WebMenuService;
import zhongchiedu.system.log.annotation.SystemServiceLog;
import zhongchiedu.system.pojo.MultiMedia;
import zhongchiedu.system.pojo.SysAudit;
import zhongchiedu.system.pojo.SysUser;
import zhongchiedu.system.service.MultiMediaService;

@Service
@Slf4j
public class NewsServiceImpl extends GeneralServiceImpl<News> implements NewsService {

	@Autowired
	private MultiMediaService multiMediaService;
	@Autowired
	private WebMenuService webMenuService;
	@Autowired
	private SysAudit sysAudit;

	@Override
	public Pagination<News> findPagination(String webMenuId, Integer pageNo, Integer pageSize,HttpSession session) {
		Pagination<News> pagination = null;
		Query query = new Query();
		SysUser sysUser = (SysUser) session.getAttribute(Contents.SYSUSER_SESSION);
		if(sysUser.getUserType().equals(UserType.SCHOOL_USER)) {
			query.addCriteria(Criteria.where("sysUser.$id").is(new ObjectId(sysUser.getId())));
		}
		
		query.addCriteria(Criteria.where("isDelete").is(false));
		query.addCriteria(Criteria.where("webMenu.$id").is(new ObjectId(webMenuId)));
		try {
			pagination = this.findPaginationByQuery(query, pageNo, pageSize, News.class);

		} catch (Exception e) {
			log.info("查询所有信息失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return Common.isNotEmpty(pagination) ? pagination : new Pagination<News>();
	}

	@Override
	@SystemServiceLog(description = "创建新闻出现错误")
	public void SaveOrUpdateNews(News news, MultipartFile[] filenews, String oldnewsImg, String path, String dir,
			String editorValue,HttpSession session) {
		SysUser sysUser = (SysUser) session.getAttribute(Contents.SYSUSER_SESSION);
		if(Common.isEmpty(sysUser)) {
			return;
		}
		news.setSysUser(sysUser);
		if (Common.isNotEmpty(news)) {
			WebMenu webMenu = this.webMenuService.findOneById(news.getWebMenu().getId(), WebMenu.class);
			if (Common.isNotEmpty(webMenu)) {
				// 根据当前的menuid获取根menu
				WebMenu supMenu = this.webMenuService.findWebMenuById(webMenu.getFirstLevel());
				if (Common.isNotEmpty(supMenu)) {
					news.setSupMenu(supMenu);
				}
			}

			News ed = null;
			// 上传图片
			List<MultiMedia> newsImg = this.multiMediaService.uploadPictures(filenews, dir, path, "NEWS");

			if (Common.isNotEmpty(news.getId())) {
				ed = this.findOneById(news.getId(), News.class);
				news.setViews(ed.getViews());
				if (Common.isNotEmpty(ed)) {
					news.setNewsImg(Common.isNotEmpty(oldnewsImg) ? ed.getNewsImg() : null);
				}
			}
			if (newsImg.size() > 0) {
				news.setNewsImg(newsImg.get(0));
			}
			if (Common.isNotEmpty(editorValue)) {
				news.setContent(editorValue);
			}
			if (Common.isNotEmpty(ed)) {
				ed.setStatus(0);
				BeanUtils.copyProperties(news, ed);
				this.save(ed);
			} else {
				this.insert(news);
			}
		}

	}

	@Override
	public BasicDataResult toDisable(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "禁用失败，请求出现问题，请刷新后重试", null);
		}
		News news = this.findOneById(id, News.class);
		if (Common.isEmpty(news)) {
			return BasicDataResult.build(400, "无法获取到资源信息，可能已经被删除", null);
		}
		news.setIsDisable(news.getIsDisable().equals(true) ? false : true);
		this.save(news);
		return BasicDataResult.build(200, news.getIsDisable().equals(true) ? "禁用成功" : "启用成功", news.getIsDisable());

	}

	private Lock lock = new ReentrantLock();

	@Override
	public String delete(String id) {
		try {
			lock.lock();
			List<String> ids = Arrays.asList(id.split(","));
			for (String edid : ids) {
				News de = this.findOneById(edid, News.class);
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

	/**
	 * 访问量+1
	 */
	@Override
	public void updateNewsVisit(String id) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				updateVisitByThread(id);
			}
		}).start();

	}

	public synchronized void updateVisitByThread(String id) {
		News news = this.findOneById(id, News.class);
		if (Common.isNotEmpty(news)) {
			news.setViews(news.getViews() + 1);
			this.save(news);
		}
	}

	@Override
	public News findNewsById(String id) {
		Query query = new Query();
		if (Common.isNotEmpty(id)) {
			query.addCriteria(Criteria.where("isDelete").is(false));
			query.addCriteria(Criteria.where("isDisable").is(false));
			query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));

		}

		return this.findOneByQuery(query, News.class);

	}

	@Override
	public Pagination<News> findNewsBySuperMenuId(String id, Integer pageNo, Integer pageSize) {
		Pagination<News> pagination = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		query.addCriteria(Criteria.where("status").is(2));
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("supMenu.$id").is(new ObjectId(id)));
		query.with(new Sort(new Order(Direction.DESC, "createDate")));
		try {
			pagination = this.findPaginationByQuery(query, pageNo, pageSize, News.class);

		} catch (Exception e) {
			log.info("查询所有信息失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return Common.isNotEmpty(pagination) ? pagination : new Pagination<News>();
	}

	@Override
	public Pagination<News> findNewsByWebMenuId(String id, Integer pageNo, Integer pageSize) {
		Pagination<News> pagination = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("status").is(2));
		query.addCriteria(Criteria.where("isDelete").is(false));
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("webMenu.$id").is(new ObjectId(id)));
		query.with(new Sort(new Order(Direction.DESC, "createDate")));
		try {
			pagination = this.findPaginationByQuery(query, pageNo, pageSize, News.class);

		} catch (Exception e) {
			log.info("查询所有信息失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return Common.isNotEmpty(pagination) ? pagination : new Pagination<News>();
	}

	@Override
	public List<News> findNewsByNewsIds(List<IndexSetting> indexs) {
		Set<WebMenu> list = new HashSet();
		List<News> news = new ArrayList<News>();

		for (IndexSetting index : indexs) {
			for (WebMenu menu : index.getWebMenu()) {
				list.add(menu);
			}
		}
		List<News> getNews = null;

		for (WebMenu menu : list) {
			Query query = new Query();
			query.limit(10);
			query.addCriteria(Criteria.where("status").is(2));
			query.with(new Sort(new Order(Direction.DESC, "createTime")));
			query.addCriteria(Criteria.where("isDelete").is(false));
			query.addCriteria(Criteria.where("isDisable").is(false));
			query.addCriteria(Criteria.where("webMenu.$id").is(new ObjectId(menu.getId())));
			getNews = this.find(query, News.class);
			if (getNews.size() > 0) {
				news.addAll(getNews);
			}
		}
		return news;
	}

	@Override
	public List<News> findNewsByDate(String date) {

		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("createTime").gte(new Date(date)).lt(Common.dateplus(1)));
		List<News> news = this.find(query, News.class);
		return news;
	}

	@Override
	public BasicDataResult toRelease(String id) {

		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "未找到需要发布的内容，请刷新后重试", null);
		}
		News news = this.findOneById(id, News.class);
		if (Common.isEmpty(news)) {
			return BasicDataResult.build(400, "无法获取到资源信息，可能已经被删除", null);
		}
		int status = news.getStatus();
		if (status == 1) {
			return BasicDataResult.build(400, "发布内容已经在审核了，无需重新申请！", null);
		} else if (status == 2) {
			return BasicDataResult.build(400, "该条信息已经成功发布了！", null);
		}
		String msg="";
		boolean flag= false;
		if(sysAudit.isNewsAudit()) {
			//是否开启审核
			news.setStatus(1);
			msg = "发布成功，请等待管理员审核！";
		}else {
			news.setStatus(2);
			msg = "发布成功";
			flag = true;
		}
		this.save(news);
		return BasicDataResult.build(200,  msg, flag);
		
	}

	@Override
	public Pagination<News> findPaginationAudit(Integer pageNo, Integer pageSize) {
		Pagination<News> pagination = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		query.addCriteria(Criteria.where("status").is(1));
		try {
			pagination = this.findPaginationByQuery(query, pageNo, pageSize, News.class);
		} catch (Exception e) {
			log.info("查询所有信息失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return Common.isNotEmpty(pagination) ? pagination : new Pagination<News>();
	}

	@Override
	public void ToAudit(String id, String type) {
		// type =1 通过
		// type =0驳回
		if (type.equals("1") || type.equals("0")) {
			News news = this.findOneById(id, News.class);
			if (type.equals("1")) {
				news.setStatus(2);
			} else {
				news.setStatus(3);
			}
			this.save(news);
		}

	}

	@Override
	public List<News> findNewsByWebMenuId(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("webMenu.$id").is(new ObjectId(id)));
		return this.find(query, News.class);
	}

//	public static void main(String[] args) {
//		System.out.println(Common.fromStringToDate(Common.getDateNow()));
//	}

}
