package zhongchiedu.school.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import zhongchiedu.school.pojo.PhotoGallery;
import zhongchiedu.school.service.PhotoGalleryService;
import zhongchiedu.system.pojo.MultiMedia;
import zhongchiedu.system.service.MultiMediaService;

@Service
@Slf4j
public class PhotoGalleryServiceImpl extends GeneralServiceImpl<PhotoGallery> implements PhotoGalleryService {

	@Autowired
	private MultiMediaService multiMediaService;

	@Override
	public Pagination<PhotoGallery> findPagination(Integer pageNo, Integer pageSize) {
		Pagination<PhotoGallery> pagination = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("isDelete").is(false));
		query.addCriteria(Criteria.where("showInIndex").is(true));
		try {
			pagination = this.findPaginationByQuery(query, pageNo, pageSize, PhotoGallery.class);

		} catch (Exception e) {
			log.info("查询所有信息失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return Common.isNotEmpty(pagination) ? pagination : new Pagination<PhotoGallery>();
	}

	@Override
	public void SaveOrUpdate(PhotoGallery photoGallery) {
		PhotoGallery ed = null;
		if (Common.isNotEmpty(photoGallery.getId())) {
			ed = this.findOneById(photoGallery.getId(), PhotoGallery.class);
		}
		if (Common.isNotEmpty(ed)) {
			BeanUtils.copyProperties(photoGallery, ed);
			this.save(ed);
		} else {
			this.insert(photoGallery);
		}

	}

	private Lock lock = new ReentrantLock();

	@Override
	public String delete(String id, String imgids) {
		try {
			lock.lock();
			PhotoGallery de = this.findOneById(id, PhotoGallery.class);
			List<MultiMedia> media = de.getImgs();
			List<String> ids = Arrays.asList(imgids.split(","));
			List<MultiMedia> delimg = new ArrayList<MultiMedia>();
			for (MultiMedia imgs : media) {
				for (String imgid : ids) {
					if (imgs.getId().equals(imgid)) {
						delimg.add(imgs);
					}
				}
			}
			media.removeAll(delimg);
			de.setImgs(media);
			this.save(de);

//			for (String edid : ids) {
//				PhotoGallery de = this.findOneById(edid,PhotoGallery.class);
//				de.setIsDelete(true);
//				this.save(de);
//			}
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
		PhotoGallery photoGallery = this.findOneById(id, PhotoGallery.class);
		if (Common.isNotEmpty(photoGallery)) {
			photoGallery.setViews(photoGallery.getViews() + 1);
			this.save(photoGallery);
		}
	}

	@Override
	public PhotoGallery findPhotoGalleryById(String id) {
		Query query = new Query();
		if (Common.isNotEmpty(id)) {
			query.addCriteria(Criteria.where("isDelete").is(false));
			query.addCriteria(Criteria.where("isDisable").is(false));
			query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));

		}

		return this.findOneByQuery(query, PhotoGallery.class);
	}

	@Override
	public BasicDataResult ajaxFindPhotoGalleryById(String id) {
		Query query = new Query();
		if (Common.isNotEmpty(id)) {
			query.addCriteria(Criteria.where("isDelete").is(false));
			query.addCriteria(Criteria.where("isDisable").is(false));
			query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));

		}
		PhotoGallery photo = this.findOneByQuery(query, PhotoGallery.class);
		if (Common.isNotEmpty(photo)) {
			return BasicDataResult.build(200, "success", photo);
		}
		return BasicDataResult.build(400, "未能获取到数据", null);
	}

	@Value("${upload.imgpath}")
	private String imgpath;
	@Value("${upload.savedir}")
	private String dir;

	@Override
	public void photoGalleryImgUpload(String id, MultipartFile[] file) {
		if (Common.isNotEmpty(id)) {
			PhotoGallery photo = this.findOneById(id, PhotoGallery.class);
			List<MultiMedia> multi = this.multiMediaService.uploadPictures(file, dir,
					imgpath + Common.generatedate() + File.separator, "PHOTOGALLERY");
			if (Common.isEmpty(photo)) {
				return;
			}
			if (Common.isEmpty(photo.getImgs())) {
				photo.setImgs(multi);
			} else {
				List<MultiMedia> list = photo.getImgs();
				list.addAll(multi);
			}
			this.save(photo);

		}

	}

	@Override
	public List<PhotoGallery> findImgs(Integer num) {
		//获取最新的20个相册
		Query query = new Query();
		query.limit(20);// 从多少条开始,取多少条记录
		query.addCriteria(Criteria.where("showInIndex").is(true));
		query.with(new Sort(new Order(Direction.DESC, "createTime")));
		List<PhotoGallery> photos = this.find(query, PhotoGallery.class);
		return photos;
	}

	@Override
	public void toRecommend(String id, String imgids) {

		if (Common.isNotEmpty(id)) {
//			List<MultiMedia> media = null;
//			if (Common.isNotEmpty(imgids)) {
//				List<String> ids = Arrays.asList(imgids.split(","));
//				Query query = new Query();
//				query.addCriteria(Criteria.where("_id").in(ids));
//				media = this.multiMediaService.find(query, MultiMedia.class);
//			}
			MultiMedia media = this.multiMediaService.findOneById(imgids, MultiMedia.class);
			
			PhotoGallery photo = this.findOneById(id, PhotoGallery.class);
			if (Common.isNotEmpty(photo)) {
				photo.setRecommend(media);
				this.save(photo);
			}

		}

	}

}
