/**
 * 
 */
package zhongchiedu.app.controller.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.commons.utils.Contents;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.system.log.annotation.SystemControllerLog;
import zhongchiedu.system.pojo.SysRole;
import zhongchiedu.system.pojo.SysUser;
import zhongchiedu.system.service.SysRoleService;
import zhongchiedu.system.service.SysUserService;

/**  
* <p>Title: SysUserController</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月9日  
*/
@Controller
@RequestMapping("/admin")
@Slf4j
public class SysUserController {
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@Value("${upload-imgpath}")
	private String imgPath;
	@Value("${upload-dir}")
	private String dir;
	
	@GetMapping("sysUsers")
	// @RequiresPermissions(value = "admin:sysOperation:list")
	@SystemControllerLog(description = "查询所有用户")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
		// 分页查询数据
		log.info("查询所有用户");
		Pagination<SysUser> pagination = this.sysUserService.findPagination(pageNo, pageSize);
		model.addAttribute("pageList", pagination);
		
		return "system/sysUser/list";
	}
	
	
	/**
	 * 
	 * <p>Title: toeditPage</p>  
	 * <p>Description:跳转到编辑页面 </p>  
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/sysUser/{id}")
	public String toeditPage(@PathVariable String id, Model model) {
		log.info("修改用户"+id);
		SysUser sysUser = this.sysUserService.findOneById(id, SysUser.class);
		model.addAttribute("sysUser", sysUser);
		
		List<SysRole> list = this.sysRoleService.findAllSysRoleByIsDisable();

		model.addAttribute("roleList", list);
		
		return "system/sysUser/add";

	}
	
	/**
	 * 
	 * <p>
	 * Title: addSysUserPage
	 * </p>
	 * <p>
	 * Description:跳转到添加界面
	 * </p>
	 * 
	 * @return
	 */
	@GetMapping("/sysUser")
	public String addSysUserPage(Model model) {
		List<SysRole> list = this.sysRoleService.findAllSysRoleByIsDisable();
		model.addAttribute("roleList", list);
		return "system/sysUser/add";
	}
	
	
	
	@DeleteMapping("/sysUser/{id}")
	//@RequiresPermissions(value = "admin:sysOperation:delete")
	@SystemControllerLog(description = "删除用户")
	public String delete(@PathVariable String id,HttpSession session) {
		log.info("删除用户" + id);
		SysUser suser = (SysUser) session.getAttribute(Contents.SYSUSER_SESSION);
		this.sysUserService.delete(id,suser);
		log.info("删除用户" + id + "成功");
		return "redirect:/admin/sysUsers";
	}
	
	
	/**
	 * 
	 * <p>
	 * Title: addsysRole
	 * </p>
	 * <p>
	 * Description: 添加
	 * </p>
	 * 
	 * @param sysRole
	 * @return
	 */
	@PostMapping("/sysUser")
	// @RequiresPermissions(value = "admin:sysOperation:add")
	@SystemControllerLog(description = "添加用户")
	public String addSysUser(HttpServletRequest request, @ModelAttribute("sysUser") SysUser sysUser,
			@RequestParam(value = "roleId", defaultValue = "") String roleId, @RequestParam("file") MultipartFile[] file,
			@RequestParam(value = "oldheadImg", defaultValue = "") String oldheadImg) {
		this.sysUserService.saveOrUpdate(sysUser, roleId, file, imgPath, dir, oldheadImg);

		return "redirect:/admin/sysUsers";
	}

	
	
	
	
	
	
	
	@PutMapping("/sysUser")
	// @RequiresPermissions(value = "user:edit")
	@SystemControllerLog(description = "编辑用户")
	public String editSysUser(HttpServletRequest request, @ModelAttribute("sysUser") SysUser sysUser,
			@RequestParam(value = "roleId", defaultValue = "") String roleId, @RequestParam("file") MultipartFile[] file,
			@RequestParam(value = "oldheadImg", defaultValue = "") String oldheadImg) {
		this.sysUserService.saveOrUpdate(sysUser, roleId, file, imgPath, dir, oldheadImg);

		return "redirect:/admin/sysUsers";
	}

	
	@RequestMapping(value = "/sysUser/ajaxgetRepletes", method = RequestMethod.POST)
	@ResponseBody
	public BasicDataResult ajaxgetRepletes(@RequestParam(value = "accountName", defaultValue = "") String accountName) {
		return this.sysUserService.ajaxgetRepletes(accountName);
	}

	@RequestMapping(value = "/sysUser/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult toDisable(@RequestParam(value = "id", defaultValue = "") String id) {
		return this.sysUserService.toDisable(id);
	}
	
	@RequestMapping(value = "/sysUser/checkPassword", method = RequestMethod.POST)
	@ResponseBody
	public BasicDataResult checkPassword(@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "password", defaultValue = "") String password) {
		return this.sysUserService.checkPassword(id, password);
	}
	
	
	@RequestMapping(value = "/sysUser/editPassword", method = RequestMethod.POST)
	@ResponseBody
	public BasicDataResult editPassword(@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "password2", defaultValue = "") String password2) {
		return this.sysUserService.editPassword(id, password2);
	}
	
	
	
	
	
}
