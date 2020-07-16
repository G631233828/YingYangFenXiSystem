///**
// * 
// */
//package zhongchiedu.app.controller.system;
//
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
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
//import org.springframework.web.multipart.MultipartFile;
//
//import lombok.extern.slf4j.Slf4j;
//import zhongchiedu.commons.utils.BasicDataResult;
//import zhongchiedu.commons.utils.Contents;
//import zhongchiedu.commons.utils.UserType;
//import zhongchiedu.framework.pagination.Pagination;
//import zhongchiedu.system.log.annotation.SystemControllerLog;
//import zhongchiedu.system.pojo.SysRole;
//import zhongchiedu.system.pojo.SysSchool;
//import zhongchiedu.system.pojo.SysUser;
//import zhongchiedu.system.service.SysRoleService;
//import zhongchiedu.system.service.SysSchoolService;
//import zhongchiedu.system.service.SysUserService;
//
///**  
//* <p>Title: SysSchoolAccountController</p>  
//* <p>Description: </p>  
//* @author 郭建波  
//* @date 2020年1月9日  
//*/
//@Controller
//@RequestMapping("/admin")
//@Slf4j
//public class SysSchoolAccountController {
//	@Autowired
//	private SysUserService sysSchoolAccountService;
//	
//	@Autowired
//	private SysRoleService sysRoleService;
//	
//	@Autowired
//	private SysSchoolService sysSchoolService;
//	
//	@Value("${upload-imgpath}")
//	private String imgPath;
//	@Value("${upload-dir}")
//	private String dir;
//	
//	@GetMapping("sysSchoolAccounts")
//	// @RequiresPermissions(value = "admin:sysOperation:list")
//	@SystemControllerLog(description = "查询学校管理员")
//	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
//			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
//		SysUser user = (SysUser) session.getAttribute(Contents.USER_SESSION);
//		// 分页查询数据
//		log.info("查询所有学校管理员");
//		Pagination<SysUser> pagination = this.sysSchoolAccountService.findPagination(user,pageNo, pageSize);
//		model.addAttribute("pageList", pagination);
//		
//		return "system/sysSchoolAccount/list";
//	}
//	
//	
//	/**
//	 * 
//	 * <p>Title: toeditPage</p>  
//	 * <p>Description:跳转到编辑页面 </p>  
//	 * @param id
//	 * @param model
//	 * @return
//	 */
//	@GetMapping("/sysSchoolAccount/{id}")
//	public String toeditPage(@PathVariable String id, Model model) {
//		log.info("修改学校管理员"+id);
//		SysUser sysSchoolAccount = this.sysSchoolAccountService.findOneById(id, SysUser.class);
//		model.addAttribute("sysSchoolAccount", sysSchoolAccount);
//		List<SysRole> list = this.sysRoleService.findAllSysRoleByIsDisable();
//		model.addAttribute("roleList", list);
//		List<SysSchool> schools = this.sysSchoolService.findAllSysSchoolByIsDisable();
//		model.addAttribute("schools", schools);
//		return "system/sysSchoolAccount/add";
//
//	}
//	
//	/**
//	 * 
//	 * <p>
//	 * Title: addSysUserPage
//	 * </p>
//	 * <p>
//	 * Description:跳转到添加界面
//	 * </p>
//	 * 
//	 * @return
//	 */
//	@GetMapping("/sysSchoolAccount")
//	public String addSysUserPage(Model model) {
//		List<SysRole> list = this.sysRoleService.findAllSysRoleByIsDisable();
//		model.addAttribute("roleList", list);
//		List<SysSchool> schools = this.sysSchoolService.findAllSysSchoolByIsDisable();
//		model.addAttribute("schools", schools);
//		return "system/sysSchoolAccount/add";
//	}
//	
//	
//	
//	@DeleteMapping("/sysSchoolAccount/{id}")
//	//@RequiresPermissions(value = "admin:sysOperation:delete")
//	@SystemControllerLog(description = "删除学校管理")
//	public String delete(@PathVariable String id,HttpSession session) {
//		log.info("删除学校管理" + id);
//		SysUser suser = (SysUser) session.getAttribute(Contents.SYSUSER_SESSION);
//		this.sysSchoolAccountService.delete(id,suser);
//		log.info("删除学校管理" + id + "成功");
//		return "redirect:/admin/sysSchoolAccounts";
//	}
//	
//	
//	/**
//	 * 
//	 * <p>
//	 * Title: addsysRole
//	 * </p>
//	 * <p>
//	 * Description: 添加
//	 * </p>
//	 * 
//	 * @param sysRole
//	 * @return
//	 */
//	@PostMapping("/sysSchoolAccount")
//	// @RequiresPermissions(value = "admin:sysOperation:add")
//	@SystemControllerLog(description = "添加学校管理")
//	public String addSysUser(HttpServletRequest request, @ModelAttribute("sysSchoolAccount") SysUser sysSchoolAccount,
//			@RequestParam(value = "roleId", defaultValue = "") String roleId, @RequestParam("file") MultipartFile[] file,
//			@RequestParam(value = "oldheadImg", defaultValue = "") String oldheadImg) {
//		this.sysSchoolAccountService.saveOrUpdate(sysSchoolAccount, roleId, file, imgPath, dir, oldheadImg);
//
//		return "redirect:/admin/sysSchoolAccounts";
//	}
//
//	
//	
//	
//	
//	
//	
//	
//	@PutMapping("/sysSchoolAccount")
//	// @RequiresPermissions(value = "user:edit")
//	@SystemControllerLog(description = "编辑学校管理")
//	public String editSysUser(HttpServletRequest request, @ModelAttribute("sysSchoolAccount") SysUser sysSchoolAccount,
//			@RequestParam(value = "roleId", defaultValue = "") String roleId, @RequestParam("file") MultipartFile[] file,
//			@RequestParam(value = "oldheadImg", defaultValue = "") String oldheadImg) {
//		this.sysSchoolAccountService.saveOrUpdate(sysSchoolAccount, roleId, file, imgPath, dir, oldheadImg);
//
//		return "redirect:/admin/sysSchoolAccounts";
//	}
//
//	
//	@RequestMapping(value = "/sysSchoolAccount/ajaxgetRepletes", method = RequestMethod.POST)
//	@ResponseBody
//	public BasicDataResult ajaxgetRepletes(@RequestParam(value = "accountName", defaultValue = "") String accountName) {
//		return this.sysSchoolAccountService.ajaxgetRepletes(accountName);
//	}
//
//	@RequestMapping(value = "/sysSchoolAccount/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public BasicDataResult toDisable(@RequestParam(value = "id", defaultValue = "") String id) {
//		return this.sysSchoolAccountService.toDisable(id);
//	}
//	
//	@RequestMapping(value = "/sysSchoolAccount/checkPassword", method = RequestMethod.POST)
//	@ResponseBody
//	public BasicDataResult checkPassword(@RequestParam(value = "id", defaultValue = "") String id,
//			@RequestParam(value = "password", defaultValue = "") String password) {
//		return this.sysSchoolAccountService.checkPassword(id, password);
//	}
//	
//	
//	@RequestMapping(value = "/sysSchoolAccount/editPassword", method = RequestMethod.POST)
//	@ResponseBody
//	public BasicDataResult editPassword(@RequestParam(value = "id", defaultValue = "") String id,
//			@RequestParam(value = "password2", defaultValue = "") String password2) {
//		return this.sysSchoolAccountService.editPassword(id, password2);
//	}
//	
//	
//	
//}
