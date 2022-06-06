//package zhongchiedu.school.service.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.stereotype.Service;
//
//import lombok.extern.slf4j.Slf4j;
//import me.chanjar.weixin.common.error.WxErrorException;
//import me.chanjar.weixin.mp.api.WxMpService;
//import me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult;
//import me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem;
//import zhongchiedu.commons.utils.Common;
//import zhongchiedu.framework.service.GeneralServiceImpl;
//import zhongchiedu.school.pojo.WxMpMaterialNewsGet;
//import zhongchiedu.school.service.WxMpMaterialNewsGetService;
//
//@Service
//@Slf4j
//public class WxMpMaterialNewsGetServiceImplback extends GeneralServiceImpl<WxMpMaterialNewsGet>
//		implements WxMpMaterialNewsGetService {
//
//	@Autowired
//	private WxMpService wxMpService;
//
//	@Override
//	public void getWxMpMaterialNews() {
//
//		try {
//
//			WxMpMaterialNewsGet wx = this.findWxMpMaterialNews();
//			// 判断数量是否有变化，有变化则更新
//			int count = this.wxMpService.getMaterialService().materialCount().getNewsCount();
//			WxMpMaterialNewsBatchGetResult getWxMpMaterialNewsBatchGetResult = new WxMpMaterialNewsBatchGetResult();
//				System.out.println("通过微信api获取永久素材");
//				getWxMpMaterialNewsBatchGetResult.setTotalCount(count);
//				List<WxMaterialNewsBatchGetNewsItem> items = new ArrayList<WxMaterialNewsBatchGetNewsItem>();
//				int pageNum = this.getpageNum(20, count);
//				int nowpage = 0;
//				int nums = 20;
//				for (int i = 0; i < pageNum; i++) {
//					WxMpMaterialNewsBatchGetResult re = this.wxMpService.getMaterialService()
//							.materialNewsBatchGet(nowpage, nums);
//					items.addAll(re.getItems());
//					nowpage = nowpage + 20;
//					nums = nums + 20;
//				}
//				getWxMpMaterialNewsBatchGetResult.setItems(items);
//			
//			if (Common.isEmpty(wx)) {
//				WxMpMaterialNewsGet news = new WxMpMaterialNewsGet();
//				news.setWxMpMaterialNewsBatchGetResult(getWxMpMaterialNewsBatchGetResult);
//				log.info("获取永久素材");
//				System.out.println("获取永久素材");
//				this.save(news);
//			} else {
//					wx.setWxMpMaterialNewsBatchGetResult(getWxMpMaterialNewsBatchGetResult);
//					log.info("更新永久素材");
//					System.out.println("更新永久素材");
//					this.save(wx);
//			}
//
//		} catch (WxErrorException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
////
////	try {
////
////		WxMpMaterialNewsGet wx = this.findWxMpMaterialNews();
////		// 判断数量是否有变化，有变化则更新
////		int count = this.wxMpService.getMaterialService().materialCount().getNewsCount();
////		WxMpMaterialNewsBatchGetResult getWxMpMaterialNewsBatchGetResult = new WxMpMaterialNewsBatchGetResult();
////
////		if (Common.isEmpty(wx) || wx.getWxMpMaterialNewsBatchGetResult().getTotalCount() != count) {
////			System.out.println("通过微信api获取永久素材");
////			getWxMpMaterialNewsBatchGetResult.setTotalCount(count);
////			List<WxMaterialNewsBatchGetNewsItem> items = new ArrayList<WxMaterialNewsBatchGetNewsItem>();
////			int pageNum = this.getpageNum(20, count);
////			int nowpage = 0;
////			int nums = 20;
////			for (int i = 0; i < pageNum; i++) {
////				WxMpMaterialNewsBatchGetResult re = this.wxMpService.getMaterialService()
////						.materialNewsBatchGet(nowpage, nums);
////				items.addAll(re.getItems());
////				nowpage = nowpage + 20;
////				nums = nums + 20;
////			}
////			getWxMpMaterialNewsBatchGetResult.setItems(items);
////		}
////
////		if (Common.isEmpty(wx)) {
////			WxMpMaterialNewsGet news = new WxMpMaterialNewsGet();
////			news.setWxMpMaterialNewsBatchGetResult(getWxMpMaterialNewsBatchGetResult);
////			log.info("获取永久素材");
////			System.out.println("获取永久素材");
////			this.save(news);
////		} else {
////			if (wx.getWxMpMaterialNewsBatchGetResult().getTotalCount() != count) {
////				wx.setWxMpMaterialNewsBatchGetResult(getWxMpMaterialNewsBatchGetResult);
////				log.info("更新永久素材");
////				System.out.println("更新永久素材");
////				this.save(wx);
////			}
////		}
////
////	} catch (WxErrorException e) {
////		// TODO Auto-generated catch block
////		e.printStackTrace();
////	}
//	
//	
//	
//	
//
//	@Override
//	public WxMpMaterialNewsGet findWxMpMaterialNews() {
//		Query query = new Query();
//		WxMpMaterialNewsGet wxn = this.findOneByQuery(query, WxMpMaterialNewsGet.class);
//		return wxn;
//	}
//
//	public int getpageNum(int count, int allnum) {
//
//		return allnum / count + 1;
//	}
//
//}
