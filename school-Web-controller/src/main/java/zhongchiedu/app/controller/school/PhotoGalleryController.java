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
	//@RequiresPermissions(value = "webmenu:list")
	@SystemControllerLog(description = "查询所有相册")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
		// 分页查询数据
		Pagination<PhotoGallery> pagination = this.photoGalleryService.findPagination(pageNo, pageSize);
		if (pagination == null)
			pagination = new Pagination<PhotoGallery>();
		model.addAttribute("pageList", pagination);
		
		return "school/photo/list";
	}

	
	@PostMapping("/photo")
//	@RequiresPermissions(value = "news:add")
	@SystemControllerLog(description = "添加相冊")
	public String addPhotoGallery(HttpServletRequest request, @ModelAttribute("photoGallery") PhotoGallery photoGallery) {
		this.photoGalleryService.SaveOrUpdate(photoGallery);
		return "redirect:/school/photo";
	}
	
	
	@GetMapping("/photos/{id}")
	//@RequiresPermissions(value = "news:edit")
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
//	@RequiresPermissions(value = "news:add")
//	@SystemControllerLog(description = "添加文章")
	public String uploadImg(HttpServletRequest request,
			@RequestParam("file")MultipartFile[] file,String id
			) {
		
		 this.photoGalleryService.photoGalleryImgUpload(id, file);
		
		 return "redirect:/school/photos/"+id;
	}
	
	
//	@RequiresPermissions(value = "news:delete")
//	@SystemControllerLog(description = "删除")
	public String delete(@PathVariable String id,@PathVariable String imgids) {
		
		this.photoGalleryService.delete(id, imgids);
		
		 return "redirect:/school/photos/"+id;
		//return "redirect:/school/findNews?webMenuId="+news.getWebMenu().getId();
	}
	
	
	
	
	

	
	//TODO
	//1.后台上传图片已经完成
	//2.需要优化提示，优化代码
	//3.需要添加推荐图片，此处是队友的接口
	//4.首页中需要将推荐图片显示出来
	//5.添加后台登陆需要验证码
	@RequestMapping(value = "/photo/toRecommend/{id}")
	public String toRecommend(@PathVariable(value = "id") String id, @RequestParam(value = "imgids", defaultValue = "") String imgids) {

		System.out.println(id);
		System.out.println(imgids);
		this.photoGalleryService.toRecommend(id, imgids);
		
		 return "redirect:/school/photos/"+id;
		//return this.photoGalleryService.ajaxFindPhotoGalleryById(id);
	}
	
	
	
	
	
	
	
	
	
	
	

}
