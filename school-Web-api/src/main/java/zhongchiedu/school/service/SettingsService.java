package zhongchiedu.school.service;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.Settings;

/**
 * 网站设置
 * @author fliay
 *
 */

public interface SettingsService extends GeneralService<Settings> {

	public void saveOrUpdateSettings(Settings settings, HttpSession session, MultipartFile[] fileicon,
			MultipartFile[] filelogo, MultipartFile[] filebanana,MultipartFile[] fileqRcode, String imgPath, 
			String oldIcon, String oldLogo,String oldqRcode,String dir);
}
