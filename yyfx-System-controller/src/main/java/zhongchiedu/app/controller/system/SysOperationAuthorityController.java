/**
 * 
 */
package zhongchiedu.app.controller.system;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
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
import zhongchiedu.system.service.SysOperationAuthorityService;

/**
 * <p>
 * Title: SysOperationAuthorityController
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author 郭建波
 * @date 2019年12月27日
 */
@Controller
@RequestMapping("/admin")
@Slf4j
public class SysOperationAuthorityController {
	
	@Autowired
	private SysOperationAuthorityService sysOperationAuthorityService;

	@GetMapping("sysOperations")
	@RequiresPermissions(value = "sysoperation:list")
	@SystemControllerLog(description = "查询所有按钮配置")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
		// 分页查询数据
		log.info("查询所有按钮配置");
		Pagination<SysOperationAuthority> pagination = this.sysOperationAuthorityService.findPagination(pageNo, pageSize);
		model.addAttribute("pageList", pagination);
		return "system/sysOperationAuthority/list";
	}
	
	
	/**
	 * 
	 * <p>Title: toeditPage</p>  
	 * <p>Description:跳转到编辑页面 </p>  
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/sysOperation/{id}")
	@RequiresPermissions(value = "sysoperation:edit")
	public String toeditPage(@PathVariable String id, Model model) {
		log.info("修改按钮权限"+id);
		SysOperationAuthority sysOperationAuthority = this.sysOperationAuthorityService.findOneById(id, SysOperationAuthority.class);
		model.addAttribute("sysOperationAuthority", sysOperationAuthority);
		return "system/sysOperationAuthority/add";

	}
	
	

	/**
	 * 
	 * <p>
	 * Title: addSysOperationPage
	 * </p>
	 * <p>
	 * Description:跳转到添加界面
	 * </p>
	 * 
	 * @return
	 */
	@GetMapping("/sysOperation")
	public String addSysOperationPage() {
		return "system/sysOperationAuthority/add";
	}
	
	
	@DeleteMapping("/sysOperation/{id}")
	@RequiresPermissions(value = "sysoperation:delete")
	@SystemControllerLog(description = "删除按钮")
	public String delete(@PathVariable String id) {
		log.info("删除按钮" + id);
		this.sysOperationAuthorityService.delete(id);
		log.info("删除按钮" + id + "成功");
		return "redirect:/admin/sysOperations";
	}
	

	/**
	 * 
	 * <p>
	 * Title: addSysOperation
	 * </p>
	 * <p>
	 * Description: 添加
	 * </p>
	 * 
	 * @param sysOperationAuthority
	 * @return
	 */
	@PostMapping("/sysOperation")
	@RequiresPermissions(value = "sysoperation:add")
	@SystemControllerLog(description = "添加按钮")
	public String addSysOperation(
			@ModelAttribute("sysOperationAuthority") SysOperationAuthority sysOperationAuthority) {
		this.sysOperationAuthorityService.saveOrUpdate(sysOperationAuthority);
		return "redirect:sysOperations";
	}

	/**
	 *  
	 * <p>
	 * Title: editSysOperation
	 * </p>
	 * <p>
	 * Description:修改
	 * </p>
	 * 
	 * @param sysOperationAuthority
	 * @return
	 */
	@PutMapping("/sysOperation")
	@RequiresPermissions(value = "sysoperation:edit")
	@SystemControllerLog(description = "修改按钮配置")
	public String editSysOperation(
			@ModelAttribute("sysOperationAuthority") SysOperationAuthority sysOperationAuthority) {
		this.sysOperationAuthorityService.saveOrUpdate(sysOperationAuthority);
		return "redirect:sysOperations";
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
	@RequestMapping(value = "/sysOperation/ajaxgetRepletes", method = RequestMethod.POST)
	@ResponseBody
	public BasicDataResult ajaxgetRepletes(@RequestParam(value = "name", defaultValue = "") String name) {
		return this.sysOperationAuthorityService.ajaxgetRepletes(name);
	}

	@RequestMapping(value = "/sysOperation/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult toDisable(@RequestParam(value = "id", defaultValue = "") String id) {
		return this.sysOperationAuthorityService.toDisable(id);

	}

}
