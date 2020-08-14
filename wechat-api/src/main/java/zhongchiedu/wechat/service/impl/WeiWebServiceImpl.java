package zhongchiedu.wechat.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.Common;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.system.pojo.MultiMedia;
import zhongchiedu.system.service.impl.MultiMediaServiceImpl;
import zhongchiedu.wechat.pojo.WeiWeb;
import zhongchiedu.wechat.service.WeiWebService;

@Service
@Slf4j
public class WeiWebServiceImpl extends GeneralServiceImpl<WeiWeb> implements WeiWebService {

	@Autowired
	private MultiMediaServiceImpl multiMediaSerice;

	@Override
	public void saveOrUpdate(WeiWeb weiWeb, MultipartFile[] filelogo, MultipartFile[] filebananas, String imgPath,
			String oldLogo, String dir) {
		WeiWeb getweiWeb = null;
		List<MultiMedia> logo = this.multiMediaSerice.uploadPictures(filelogo, dir, imgPath, "WEIWEB_LOGO", 80, 80);

		List<MultiMedia> list = this.multiMediaSerice.uploadPictures(filebananas, dir, imgPath, "WEIWEB_LISTBANANA",
				360, 200);

		if (Common.isNotEmpty(weiWeb.getId())) {
			getweiWeb = this.findOneById(weiWeb.getId(), WeiWeb.class);
			if(Common.isNotEmpty(getweiWeb)) {
				weiWeb.setLogo(Common.isNotEmpty(oldLogo) ? getweiWeb.getLogo() : null);
				if (Common.isNotEmpty(getweiWeb.getListBanana())) {
					List<MultiMedia> lm = getweiWeb.getListBanana();
					list.addAll(lm);
				}
				
			}

		}
		if (logo.size() > 0) {
			weiWeb.setLogo(logo.get(0));
		}
		if (list.size() > 0) {
			weiWeb.setListBanana(list);
		}

		if (Common.isNotEmpty(getweiWeb)) {
			BeanUtils.copyProperties(weiWeb, getweiWeb);
			getweiWeb.setListBanana(Common.isNotEmpty(filebananas) ? list : getweiWeb.getListBanana());
			this.save(getweiWeb);
		} else {
			this.insert(weiWeb);
		}
		

	}

}
