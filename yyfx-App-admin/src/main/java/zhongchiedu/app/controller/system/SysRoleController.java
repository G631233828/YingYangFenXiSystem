/**
 * 
 */
package zhongchiedu.app.controller.system;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.system.log.annotation.SystemControllerLog;
import zhongchiedu.system.pojo.Resource;
import zhongchiedu.system.pojo.SysResource;
import zhongchiedu.system.pojo.SysRole;
import zhongchiedu.system.service.SysResourceService;
import zhongchiedu.system.service.SysRoleService;

/**  
* <p>Title: SysRoleController</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月8日  
*/
@Controller
@RequestMapping("/admin")
@Slf4j
public class SysRoleController {
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@Autowired
	private SysResourceService sysResourceService;

	@GetMapping("sysRoles")
	// @RequiresPermissions(value = "admin:sysOperation:list")
	@SystemControllerLog(description = "查询所有角色")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
		// 分页查询数据
		log.info("查询所有角色");
		Pagination<SysRole> pagination = this.sysRoleService.findPagination(pageNo, pageSize);
		model.addAttribute("pageList", pagination);
			
		List<SysResource> findAllSysResources = this.sysResourceService.findAllSysResources();
		model.addAttribute("findAllSysResources", findAllSysResources);
		
		
		return "system/sysRole/list";
	}
	
	
	/**
	 * 
	 * <p>Title: toeditPage</p>  
	 * <p>Description:跳转到编辑页面 </p>  
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/sysRole/{id}")
	public String toeditPage(@PathVariable String id, Model model) {
		log.info("修改角色"+id);
		SysRole sysRole = this.sysRoleService.findOneById(id, SysRole.class);
		model.addAttribute("sysRole", sysRole);
		return "system/sysRole/add";

	}
	
	

	/**
	 * 
	 * <p>
	 * Title: addSysRolePage
	 * </p>
	 * <p>
	 * Description:跳转到添加界面
	 * </p>
	 * 
	 * @return
	 */
	@GetMapping("/sysRole")
	public String addSysRolePage() {
		return "system/sysRole/add";
	}
	
	
	@DeleteMapping("/sysRole/{id}")
	//@RequiresPermissions(value = "admin:sysOperation:delete")
	@SystemControllerLog(description = "删除角色")
	public String delete(@PathVariable String id) {
		log.info("删除角色" + id);
		this.sysRoleService.delete(id);
		log.info("删除角色" + id + "成功");
		return "redirect:/admin/sysRoles  ";
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
	@PostMapping("/sysRole")
	// @RequiresPermissions(value = "admin:sysOperation:add")
	@SystemControllerLog(description = "添加角色")
	public String addsysRole(
			@ModelAttribute("sysRole") SysRole sysRole) {
		this.sysRoleService.saveOrUpdate(sysRole);
		return "redirect:sysRoles";
	}

	/**
	 *  
	 * <p>
	 * Title: editSysRole
	 * </p>
	 * <p>
	 * Description:修改
	 * </p>
	 * 
	 * @param sysRole
	 * @return
	 */
	@PutMapping("/sysRole")
//	@RequiresPermissions(value = "admin:sysOperation:edit")
	@SystemControllerLog(description = "修改角色")
	public String editSysRole(
			@ModelAttribute("sysRole") SysRole sysRole) {
		this.sysRoleService.saveOrUpdate(sysRole);
		return "redirect:sysRoles";
	}

	/**
	 * 
	 * <p>
	 * Title: ajaxgetRepletes
	 * </p>
	 * <p>
	 * Description: 使用ajax查看是否有重复的
	 * </p>
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/sysRole/ajaxgetRepletes", method = RequestMethod.POST)
	@ResponseBody
	public BasicDataResult ajaxgetRepletes(@RequestParam(value = "roleName", defaultValue = "") String roleName) {
		return this.sysRoleService.ajaxgetRepletes(roleName);
	}

	@RequestMapping(value = "/sysRole/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult toDisable(@RequestParam(value = "id", defaultValue = "") String id) {
		return this.sysRoleService.toDisable(id);

	}
	
	
	@ResponseBody
	@RequestMapping("/sysRole/author")
	public BasicDataResult author(HttpSession session, @RequestParam(defaultValue = "", value = "id") String id,
			@RequestParam(value = "checkallPermission", defaultValue = "") String checkallPermission) {
		BasicDataResult result = this.sysRoleService.author(id,checkallPermission);
		return result;
	}
	
	
	@ResponseBody
	@RequestMapping("/sysRole/getAuthor")
	public BasicDataResult getAuthor(@RequestParam(defaultValue = "", value = "id") String id){
		
		BasicDataResult result = this.sysRoleService.getAuthor(id);
		return result;
		
	}
	
	
	
	
	
	
	
	
	
	

}
