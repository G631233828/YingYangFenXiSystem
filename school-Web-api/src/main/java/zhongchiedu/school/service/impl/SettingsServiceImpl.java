package zhongchiedu.school.service.impl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.Common;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.Settings;
import zhongchiedu.school.service.SettingsService;
import zhongchiedu.system.pojo.MultiMedia;
import zhongchiedu.system.service.impl.MultiMediaServiceImpl;

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
public class SettingsServiceImpl extends GeneralServiceImpl<Settings> implements SettingsService {

	@Autowired
	private MultiMediaServiceImpl multiMediaSerice;

	@Override
	public void saveOrUpdateSettings(Settings settings, HttpSession session, MultipartFile[] fileicon,
			MultipartFile[] filelogo,  MultipartFile[] filebananas,MultipartFile[] filebanana, MultipartFile[] fileqRcode, String imgPath,
			String oldIcon, String oldLogo, String oldqRcode, String oldbanana, String dir) {

		Settings getsettings = null;
		// icon
		List<MultiMedia> icon = this.multiMediaSerice.uploadPictures(fileicon, dir, imgPath, "SETTINGS_ICON", 80, 80);

		// logo
		List<MultiMedia> logo = this.multiMediaSerice.uploadPictures(filelogo, dir, imgPath, "SETTINGS_LOGO", 548, 162);
		// banana
		List<MultiMedia> banana = this.multiMediaSerice.uploadPictures(filebanana, dir, imgPath, "SETTINGS_BANANA", 1903, 480);

		// logo
		List<MultiMedia> qRcord = this.multiMediaSerice.uploadPictures(fileqRcode, dir, imgPath, "SETTINGS_QRCORD", 187, 70);

		// listbanana
		List<MultiMedia> list = this.multiMediaSerice.uploadPictures(filebananas, dir, imgPath, "SETTINGS_LISTBANANA", 1903,
				600);

		if (Common.isNotEmpty(settings.getId())) {

			getsettings = this.findOneById(settings.getId(), Settings.class);

			if (Common.isNotEmpty(getsettings)) {
				settings.setLogo(Common.isNotEmpty(oldLogo) ? getsettings.getLogo() : null);
				settings.setIcon(Common.isNotEmpty(oldIcon) ? getsettings.getIcon() : null);
				settings.setQRcode(Common.isNotEmpty(oldqRcode) ? getsettings.getQRcode() : null);
				settings.setBanana(Common.isNotEmpty(oldbanana)?getsettings.getBanana():null);
				if (Common.isNotEmpty(getsettings.getListBanana())) {
					List<MultiMedia> lm = getsettings.getListBanana();
					list.addAll(lm);
				}
			}
		}

		if (icon.size() > 0) {
			settings.setIcon(icon.get(0));
		}
		if (logo.size() > 0) {
			settings.setLogo(logo.get(0));
		}
		if (qRcord.size() > 0) {
			settings.setQRcode(qRcord.get(0));
		}
		if(banana.size()>0) {
			settings.setBanana(banana.get(0));
		}
		if (list.size() > 0) {
			settings.setListBanana(list);
		}

		if (Common.isNotEmpty(getsettings)) {
			BeanUtils.copyProperties(settings, getsettings);
			getsettings.setListBanana(Common.isNotEmpty(filebananas) ? list : getsettings.getListBanana());
			this.save(getsettings);
		} else {
			this.insert(settings);
		}

	}

}
