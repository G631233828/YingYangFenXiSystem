package zhongchiedu.app.controller.school;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.school.pojo.News;
import zhongchiedu.school.pojo.PhotoGallery;
import zhongchiedu.school.service.PhotoGalleryService;
import zhongchiedu.system.log.annotation.SystemControllerLog;

@Controller
@RequestMapping("/school")
@Slf4j
public class PhotoGalleryController {
	
	@Autowired
	private PhotoGalleryService photoGalleryService;
	
	
	
	@GetMapping("photo")
	@RequiresPermissions(value = "photo:list")
	@SystemControllerLog(description = "查询所有相册")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
		// 分页查询数据
		Pagination<PhotoGallery> pagination = this.photoGalleryService.findPagination(0,pageNo, pageSize);
		if (pagination == null)
			pagination = new Pagination<PhotoGallery>();
		model.addAttribute("pageList", pagination);
		
		return "school/photo/list";
	}

	
	@PostMapping("/photo")
	@RequiresPermissions(value = "photo:add")
	@SystemControllerLog(description = "添加相冊")
	public String addPhotoGallery(HttpServletRequest request, @ModelAttribute("photoGallery") PhotoGallery photoGallery) {
		this.photoGalleryService.SaveOrUpdate(photoGallery);
		return "redirect:/school/photo";
	}
	
	
	@GetMapping("/photos/{id}")
	@RequiresPermissions(value = "photo:edit")
	@SystemControllerLog(description = "查看所有相片")
	public String toeditNews(Model model,@PathVariable (name = "id")String id) {
		
		PhotoGallery photo = this.photoGalleryService.findOneById(id, PhotoGallery.class);
		
		
		model.addAttribute("photo", photo);
		
		
		return "school/photo/imglist";
	}
	
	
	
	
	@RequestMapping(value = "/photo/edit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult edit(@RequestParam(value = "id", defaultValue = "") String id, String sort) {

		return this.photoGalleryService.ajaxFindPhotoGalleryById(id);
	}
	
	
	@PostMapping("/photo/upload")
	@RequiresPermissions(value = "photo:upload")
	@SystemControllerLog(description = "上传图片")
	public String uploadImg(HttpServletRequest request,
			@RequestParam("file")MultipartFile[] file,String id
			) {
		 this.photoGalleryService.photoGalleryImgUpload(id, file);
		 return "redirect:/school/photos/"+id;
	}
	
	
	@RequiresPermissions(value = "photo:delete")
	@SystemControllerLog(description = "删除相册")
	@RequestMapping(value = "/photo/{id}/{imgids}")
	public String delete(@PathVariable String id,@PathVariable String imgids) {
		this.photoGalleryService.delete(id, imgids);
		 return "redirect:/school/photos/"+id;
	}
	
	
	
	
	


	@RequestMapping(value = "/photo/toRecommend/{id}")
	@RequiresPermissions(value = "photo:edit")
	@SystemControllerLog(description = "设置推荐")
	public String toRecommend(@PathVariable(value = "id") String id, @RequestParam(value = "imgids", defaultValue = "") String imgids) {
		this.photoGalleryService.toRecommend(id, imgids);
		 return "redirect:/school/photos/"+id;
	}
	
	
	
	
	@GetMapping("/photoAudit")
	@RequiresPermissions(value = "photoAudit:list")
	@SystemControllerLog(description = "查询所有待审核相册")
	public String photoAudit(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
		// 分页查询数据
		Pagination<PhotoGallery> pagination = this.photoGalleryService.findPagination(1,pageNo, pageSize);
		if (pagination == null)
			pagination = new Pagination<PhotoGallery>();
		model.addAttribute("pageList", pagination);
		
		return "school/photoAudit/list";
	}
	
	

	@GetMapping("/photo/audit")
	@RequiresPermissions(value = "photoAudit:edit")
	@SystemControllerLog(description = "新闻审核")
	public String toaudit(String id,String type) {
		this.photoGalleryService.ToAudit(id, type);
		return "redirect:/school/photoAudit";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
