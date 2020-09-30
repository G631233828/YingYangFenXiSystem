///**
// * 
// */
//package zhongchiedu.app.controller.system;
//
//import javax.servlet.http.HttpSession;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import lombok.extern.slf4j.Slf4j;
//import zhongchiedu.commons.utils.BasicDataResult;
//import zhongchiedu.framework.pagination.Pagination;
//import zhongchiedu.system.log.annotation.SystemControllerLog;
//import zhongchiedu.system.pojo.SysRegin;
//import zhongchiedu.system.pojo.SysRole;
//import zhongchiedu.system.service.SysReginService;
//
///**  
//* <p>Title: SysReginController</p>  
//* <p>Description: </p>  
//* @author 郭建波  
//* @date 2020年1月14日  
//*/
//@Controller
//@RequestMapping("/admin")
//@Slf4j
//public class SysReginController {
//	
//	@Autowired
//	private SysReginService sysReginService;
//	
//	@GetMapping("sysRegins")
//	// @RequiresPermissions(value = "admin:sysOperation:list")
//	@SystemControllerLog(description = "查询所有区域")
//	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
//			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
//		// 分页查询数据
//		log.info("查询所有区域");
//		Pagination<SysRegin> pagination = this.sysReginService.findPagination(pageNo, pageSize);
//		model.addAttribute("pageList", pagination);
//		
//		return "system/sysRegin/list";
//	}
//	
//	
//	@GetMapping("/sysRegin/{id}")
//	public String toeditPage(@PathVariable String id, Model model) {
//		log.info("修改区域"+id);
//		SysRegin sysRegin = this.sysReginService.findOneById(id, SysRegin.class);
//		model.addAttribute("sysRegin", sysRegin);
//		return "system/sysRegin/add";
//
//	}
//	
//	@GetMapping("/sysRegin")
//	public String addSysRolePage() {
//		return "system/sysRegin/add";
//	}
//	   
//	
//	@DeleteMapping("/sysRegin/{id}")
//	//@RequiresPermissions(value = "admin:sysOperation:delete")
//	@SystemControllerLog(description = "删除区域")
//	public String delete(@PathVariable String id) {
//		log.info("删除区域" + id);
//		this.sysReginService.delete(id);
//		log.info("删除区域" + id + "成功");
//		return "redirect:/admin/sysRegins";
//	}
//	
//	
//	@PostMapping("/sysRegin")
//	// @RequiresPermissions(value = "admin:sysOperation:add")
//	@SystemControllerLog(description = "添加区域")
//	public String addsysRole(
//			@ModelAttribute("sysRegin") SysRegin sysRegin) {
//		this.sysReginService.saveOrUpdate(sysRegin);
//		return "redirect:sysRegins";
//	}
//	
//	
//	
//	@PutMapping("/sysRegin")
////	@RequiresPermissions(value = "admin:sysOperation:edit")
//	@SystemControllerLog(description = "修改区域")
//	public String editSysRole(
//			@ModelAttribute("sysRegin") SysRegin sysRegin) {
//		this.sysReginService.saveOrUpdate(sysRegin);
//		return "redirect:sysRegins";
//	}
//	
//	@RequestMapping(value = "/sysRegin/ajaxgetRepletes", method = RequestMethod.POST)
//	@ResponseBody
//	public BasicDataResult ajaxgetRepletes(@RequestParam(value = "name", defaultValue = "") String name) {
//		return this.sysReginService.ajaxgetRepletes(name);
//	}
//
//	@RequestMapping(value = "/sysRegin/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public BasicDataResult toDisable(@RequestParam(value = "id", defaultValue = "") String id) {
//		return this.sysReginService.toDisable(id);
//
//	}
//	
//	
//	
//	
//	
//	
//}
