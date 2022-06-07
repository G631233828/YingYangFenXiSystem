package zhongchiedu.school.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpFreePublishService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.freepublish.WxMpFreePublishItem;
import me.chanjar.weixin.mp.bean.freepublish.WxMpFreePublishList;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNews;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem;
import me.chanjar.weixin.mp.bean.material.WxMpNewsArticle;
import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.commons.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.WebMenu;
import zhongchiedu.school.pojo.WxMpNews;
import zhongchiedu.school.service.WxMpNewsService;



@Service
@Slf4j
public class WxMpNewsServiceImpl extends GeneralServiceImpl<WxMpNews> implements WxMpNewsService {
//	@Autowired
//	private WxMpMaterialNewsGetService wxMpMaterialNewsService;
	
	@Autowired
	private WxMpService wxMpService;
	

	/**
	 * 将获取到的微信信息转换成一条条的新闻列表
	 */
	@Override
	public void insertNews(boolean flag) {
		
		
		try {

		Query query = new Query();
//		WxMpMaterialNewsGet re = this.wxMpMaterialNewsService.findOneByQuery(query, WxMpMaterialNewsGet.class);
//		
//		List<WxMaterialNewsBatchGetNewsItem> items = re.getWxMpMaterialNewsBatchGetResult().getItems();
		
		System.out.println(this.wxMpService.getAccessToken());
		
		
		
		
			WxMpFreePublishList publicationRecords = this.wxMpService.getFreePublishService().getPublicationRecords(0, 1);
			System.out.println(this.wxMpService.getFreePublishService());
			
			List<WxMpFreePublishItem> items2 = publicationRecords.getItems();
			System.out.println(items2);
		//获取微信公众号上面的新闻数量
		int count = this.wxMpService.getMaterialService().materialCount().getNewsCount();
		int img = this.wxMpService.getMaterialService().materialCount().getImageCount();
		int video = this.wxMpService.getMaterialService().materialCount().getVideoCount();
		int voice = this.wxMpService.getMaterialService().materialCount().getVoiceCount();
		
		System.out.println("当前新闻数量："+count);
		System.out.println("当前新闻数量："+img);
		System.out.println("当前新闻数量："+ video);
		System.out.println("当前新闻数量："+voice);
		
		List<WxMpNews> findWxNews = findWxNews();
		if(437>findWxNews.size()) {
			System.out.println("更新微信新闻");
			WxMpMaterialNewsBatchGetResult getWxMpMaterialNewsBatchGetResult = new WxMpMaterialNewsBatchGetResult();
				System.out.println("通过微信api获取永久素材");
				getWxMpMaterialNewsBatchGetResult.setTotalCount(437);
				List<WxMaterialNewsBatchGetNewsItem> items = new ArrayList<WxMaterialNewsBatchGetNewsItem>();
				int pageNum = this.getpageNum(20, count);
				int nowpage = 0;
				int nums = 20;
				for (int i = 0; i < pageNum; i++) {
					WxMpMaterialNewsBatchGetResult re = this.wxMpService.getMaterialService() .materialNewsBatchGet(nowpage, nums);
					items.addAll(re.getItems());
					nowpage = nowpage + 20;
					nums = nums + 20;
				}
				getWxMpMaterialNewsBatchGetResult.setItems(items);
			
			for(WxMaterialNewsBatchGetNewsItem it:getWxMpMaterialNewsBatchGetResult.getItems()) {
				WxMpMaterialNews content = it.getContent();
				List<WxMpNewsArticle> articles = content.getArticles();
				for(WxMpNewsArticle art:articles) {
					System.out.println(it.getMediaId()+":"+it.getUpdateTime()+":"+art.getTitle()+":"+art.getUrl()+":"+art.getThumbUrl());
					//根据条件获取新闻信息
					WxMpNews getwxMpNews = this.findByMediaIdAndTitle(it.getMediaId(), art.getUrl());
					if(Common.isEmpty(getwxMpNews)) {
						//插入数据
						WxMpNews wxMpNews = new WxMpNews();
						wxMpNews.setAuthor(art.getAuthor());
						wxMpNews.setCreateTime(content.getCreateTime());
						wxMpNews.setDigest(art.getDigest());
						wxMpNews.setMediaId(it.getMediaId());
						wxMpNews.setThumbMediaId(art.getThumbMediaId());
						wxMpNews.setThumbUrl(art.getThumbUrl());
						wxMpNews.setTitle(art.getTitle());
						wxMpNews.setUpdateTime(it.getUpdateTime());
						wxMpNews.setUrl(art.getUrl());
						System.out.println("创建永久素材数据");
						log.info("创建永久素材数据");
						this.insert(wxMpNews);
					}else {
						//根据updateTime来判断是否是新数据
						if(!getwxMpNews.getCreateTime().equals(it.getUpdateTime())){
							//执行更新
							getwxMpNews.setAuthor(art.getAuthor());
							getwxMpNews.setCreateTime(content.getCreateTime());
							getwxMpNews.setDigest(art.getDigest());
							getwxMpNews.setMediaId(it.getMediaId());
							getwxMpNews.setThumbMediaId(art.getThumbMediaId());
							getwxMpNews.setThumbUrl(art.getThumbUrl());
							getwxMpNews.setTitle(art.getTitle());
							getwxMpNews.setUpdateTime(it.getUpdateTime());
							getwxMpNews.setUrl(art.getUrl());
							log.info("修改永久素材数据");
							System.out.println("修改永久素材数据");
							this.save(getwxMpNews);
						}
						
					}
					
				}
			}
		}
		
		
	
	} catch (WxErrorException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}

	@Override
	public WxMpNews findByMediaIdAndTitle(String mediaId, String url) {
		Query query = new Query();
		query.addCriteria(Criteria.where("mediaId").is(mediaId));
		query.addCriteria(Criteria.where("url").is(url));
		WxMpNews wxMpNews=this.findOneByQuery(query, WxMpNews.class);
		return wxMpNews;
	}

	@Override
	public Pagination<WxMpNews> findPagination(Integer pageNo, Integer pageSize) {
		Pagination<WxMpNews> pagination = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.with(new Sort(new Order(Direction.DESC, "updateTime")));
		try {
			pagination = this.findPaginationByQuery(query, pageNo, pageSize, WxMpNews.class);

		} catch (Exception e) {
			log.info("查询所有信息失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return Common.isNotEmpty(pagination) ? pagination : new Pagination<WxMpNews>();
	}

	@Override
	public BasicDataResult toDisable(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "禁用失败，请求出现问题，请刷新后重试", null);
		}
		WxMpNews wxMpNews = this.findOneById(id, WxMpNews.class);
		if (Common.isEmpty(wxMpNews)) {
			return BasicDataResult.build(400, "无法获取到资源信息，可能已经被删除", null);
		}
		wxMpNews.setIsDisable(wxMpNews.getIsDisable().equals(true) ? false : true);
		this.save(wxMpNews);
		return BasicDataResult.build(200, wxMpNews.getIsDisable().equals(true) ? "禁用成功" : "启用成功", wxMpNews.getIsDisable());
	}
	private Lock lock = new ReentrantLock();

	@Override
	public String delete(String id) {
		try {
			lock.lock();
			List<String> ids = Arrays.asList(id.split(","));
			for (String edid : ids) {
				WxMpNews de = this.findOneById(edid, WxMpNews.class);
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

	public int getpageNum(int count, int allnum) {

		return allnum / count + 1;
	}

	@Override
	public List<WxMpNews> findWxNews() {
		return this.find(new Query(), WxMpNews.class);
	}

}
