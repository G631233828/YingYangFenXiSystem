package zhongchiedu.app.controller.school;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.commons.utils.Common;
import zhongchiedu.school.pojo.Settings;
import zhongchiedu.school.service.SettingsService;
import zhongchiedu.system.log.annotation.SystemControllerLog;
import zhongchiedu.system.pojo.MultiMedia;
import zhongchiedu.system.service.impl.MultiMediaServiceImpl;

@Controller
@RequestMapping("/school")
@Slf4j
public class SettingsController {

	private @Autowired SettingsService settingsService;

	@Autowired
	private MultiMediaServiceImpl multiMediaService;

	@RequestMapping("/findSettings")
	// @RequiresPermissions(value = "settings:list")
	@SystemControllerLog(description = "查询网站配置")
	public String findSiteTemplate(Model model) {

		Settings settings = this.settingsService.findOneByQuery(new Query(), Settings.class);
		model.addAttribute("settings", settings);
		return "school/settings/add";
	}

	@Value("${upload.imgpath}")
	private String imgPath;

	@Value("${upload.savedir}")
	private String dir;

	@RequestMapping("/settings")
	// @RequiresPermissions(value = "settings:edit")
	@SystemControllerLog(description = "编辑网站设置")
	public String addOrEditAboutUs(@ModelAttribute("settings") Settings settings,
			@RequestParam(defaultValue = "", value = "oldIcon") String oldIcon,
			@RequestParam(defaultValue = "", value = "oldLogo") String oldLogo,
			@RequestParam(defaultValue = "", value = "oldqRcode") String oldqRcode, HttpSession session,
			@RequestParam("fileicon") MultipartFile[] fileicon, @RequestParam("filelogo") MultipartFile[] filelogo,
			@RequestParam("fileqRcode") MultipartFile[] fileqRcode,
			@RequestParam("filebanana") MultipartFile[] filebanana, HttpServletRequest request) {
		if (Common.isEmpty(settings.getId())) {
			settings.setId(null);
		}
		this.settingsService.saveOrUpdateSettings(settings, session, fileicon, filelogo, filebanana, fileqRcode, imgPath,
				oldIcon, oldLogo, oldqRcode, dir);

		return "redirect:/school/findSettings";
	}

	@RequestMapping(value = "deleteIndexImg")
	@ResponseBody
	public BasicDataResult deleteImg(String id, HttpSession session) {

		if (Common.isNotEmpty(id)) {
			Settings settings = null;
			settings = this.settingsService.findOneByQuery(new Query(), Settings.class);
			MultiMedia multiMedia = this.multiMediaService.findOneById(id, MultiMedia.class);
			if (Common.isNotEmpty(multiMedia)) {
				this.multiMediaService.remove(multiMedia);

				Query query = new Query();
				query.addCriteria(Criteria.where("belong").is("SETTINGS_BANANA"));
				List<MultiMedia> list = this.multiMediaService.find(query, MultiMedia.class);
				settings.setListBanana(list != null ? list : null);
				this.settingsService.save(settings);
				return BasicDataResult.build(200, "删除成功", id);
			}
		}
		return BasicDataResult.build(400, "删除失败", null);
	}

}
