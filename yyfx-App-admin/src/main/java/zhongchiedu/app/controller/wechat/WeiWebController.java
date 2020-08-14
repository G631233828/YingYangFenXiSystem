package zhongchiedu.app.controller.wechat;

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
import zhongchiedu.system.log.annotation.SystemControllerLog;
import zhongchiedu.system.pojo.MultiMedia;
import zhongchiedu.system.service.impl.MultiMediaServiceImpl;
import zhongchiedu.wechat.pojo.WeiWeb;
import zhongchiedu.wechat.service.WeiWebService;

/**
 * 微网站配置
 * 
 * @author fliay
 *
 */
@Controller
@RequestMapping("/wechat")
@Slf4j
public class WeiWebController {

	@Autowired
	private MultiMediaServiceImpl multiMediaService;

	@Autowired
	private WeiWebService weiWebService;

	@Value("${upload.imgpath}")
	private String imgPath;

	@Value("${upload.savedir}")
	private String dir;

	@RequestMapping("/findWeiWebs")
//	@RequiresPermissions(value = "weiWeb:edit")
	@SystemControllerLog(description = "查询微网站配置")
	public String findSettings(Model model) {

		WeiWeb weiWeb = this.weiWebService.findOneByQuery(new Query(), WeiWeb.class);
		model.addAttribute("weiWeb", weiWeb);
		return "wechat/weiWeb/add";
	}

	
	@RequestMapping("/weiWeb")
//	@RequiresPermissions(value = "settings:edit")
	@SystemControllerLog(description = "编辑微网站设置")
	public String addOrUpdateSettings(@ModelAttribute("weiWeb") WeiWeb weiWeb,
			@RequestParam(defaultValue = "", value = "oldLogo") String oldLogo,
			@RequestParam("filelogo") MultipartFile[] filelogo,
			@RequestParam("filebananas") MultipartFile[] filebananas, 
			HttpServletRequest request) {
		if (Common.isEmpty( weiWeb.getId())) {
			 weiWeb.setId(null);
		}
		this.weiWebService.saveOrUpdate(weiWeb, filelogo, filebananas, imgPath, oldLogo, dir);

		return "redirect:/wechat/findWeiWebs";
	}

	@RequestMapping(value = "/deleteWeiWebImg")
	@ResponseBody
	public BasicDataResult deleteImg(String id, HttpSession session) {

		if (Common.isNotEmpty(id)) {
			WeiWeb weiWeb = null;
			weiWeb = this.weiWebService.findOneByQuery(new Query(), WeiWeb.class);
			MultiMedia multiMedia = this.multiMediaService.findOneById(id, MultiMedia.class);
			if (Common.isNotEmpty(multiMedia)) {
				this.multiMediaService.remove(multiMedia);

				Query query = new Query();
				query.addCriteria(Criteria.where("belong").is("WEIWEB_LISTBANANA"));
				List<MultiMedia> list = this.multiMediaService.find(query, MultiMedia.class);
				weiWeb.setListBanana(list != null ? list : null);
				this.weiWebService.save(weiWeb);
				return BasicDataResult.build(200, "删除成功", id);
			}
		}
		return BasicDataResult.build(400, "删除失败", null);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
