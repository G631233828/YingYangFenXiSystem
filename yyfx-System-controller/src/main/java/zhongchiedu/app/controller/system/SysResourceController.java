/**
 * 
 */
package zhongchiedu.app.controller.system;

import java.util.List;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.system.log.annotation.SystemControllerLog;
import zhongchiedu.system.pojo.SysOperationAuthority;
import zhongchiedu.system.pojo.SysResource;
import zhongchiedu.system.service.SysOperationAuthorityService;
import zhongchiedu.system.service.SysResourceService;
import zhongchiedu.system.service.SysRoleService;

/**
 * <p>
 * Title: SysResourceController
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author 郭建波
 * @date 2019年12月30日
 */
@Controller
@RequestMapping("/admin")
@Slf4j
public class SysResourceController {

	@Autowired
	private SysResourceService sysResourceService;
	@Autowired
	private SysOperationAuthorityService sysOperationAuthorityService;
	@Autowired
	private SysRoleService sysRoleService;
	

	@GetMapping("sysResources")
	@RequiresPermissions(value = "sysresource:list")
	@SystemControllerLog(description = "查询所有资源")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
		// 分页查询数据
		log.info("查询所有资源");
		Pagination<SysResource> pagination = this.sysResourceService.findPagination(pageNo, pageSize);
		model.addAttribute("pageList", pagination);

		List<SysOperationAuthority> operationlist = this.sysOperationAuthorityService.findAllSysOperationAuthorityByIsDisable();
		model.addAttribute("operationlist", operationlist);
		
		return "system/sysResource/list";
	}

	/**
	 * 跳转到添加页面
	 */
	@GetMapping("/sysResource")
	@RequiresPermissions(value = "sysresource:add")
	public String addSysResourcePage(Model model) {
		// 获取所有的启用的资源目录
		List<SysResource> list = this.sysResourceService.findSysResourceMenu("0");
		model.addAttribute("resList", list);
		List<SysOperationAuthority> operationlist = this.sysOperationAuthorityService.findAllSysOperationAuthorityByIsDisable();
		model.addAttribute("operationlist", operationlist);
		return "system/sysResource/add";
	}
	
	/**
	 * 
	 * <p>Title: toeditPage</p>  
	 * <p>Description:跳转到编辑页面 </p>  
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/sysResource/{id}")
	@RequiresPermissions(value = "sysresource:edit")
	public String toeditPage(@PathVariable String id, Model model) {
		log.info("修改资源配置"+id);
		// 获取所有的启用的资源目录
		List<SysResource> list = this.sysResourceService.findSysResourceMenu("0");
		model.addAttribute("resList", list);
		SysResource sysResource = this.sysResourceService.findOneById(id, SysResource.class);
		model.addAttribute("sysResource", sysResource);
		List<SysOperationAuthority> operationlist = this.sysOperationAuthorityService.findAllSysOperationAuthorityByIsDisable();
		model.addAttribute("operationlist", operationlist);
		return "system/sysResource/add";

	}

	@DeleteMapping("/sysResource/{id}")
	@RequiresPermissions(value = "sysresource:delete")
	@SystemControllerLog(description = "删除")
	public String delete(@PathVariable String id) {
		log.info("删除" + id);
		this.sysResourceService.delete(id);
		log.info("删除" + id + "成功");
		return "redirect:/admin/sysResources";
	}

	@PostMapping("/sysResource")
	@RequiresPermissions(value = "sysresource:add")
	@SystemControllerLog(description = "添加资源")
	public String addSysResource(@ModelAttribute("sysResource") SysResource sysResource) {
		this.sysResourceService.saveOrUpdate(sysResource);
		return "redirect:/admin/sysResources";
	}

	@PutMapping("/sysResource")
	@RequiresPermissions(value = "sysresource:edit")
	@SystemControllerLog(description = "修改资源")
	public String editSysResource(@ModelAttribute("sysResource") SysResource sysResource) {
		this.sysResourceService.saveOrUpdate(sysResource);
		return "redirect:/admin/sysResources";
	}
	
	@RequestMapping(value = "/sysResource/ajaxgetRepletes", method = RequestMethod.POST)
	@ResponseBody
	public BasicDataResult ajaxgetRepletes(@RequestParam(value = "name", defaultValue = "") String name) {
		return this.sysResourceService.ajaxgetRepletes(name);
	}

	@RequestMapping(value = "/sysResource/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult toDisable(@RequestParam(value = "id", defaultValue = "") String id) {
		return this.sysResourceService.toDisable(id);
	}
	
	@RequestMapping(value = "/sysResource/setOperationAuthority", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult setOperationAuthority(@RequestParam(value = "param", defaultValue = "") String param) {
	return this.sysRoleService.createSysOperationAuthority(param);
	}

}
