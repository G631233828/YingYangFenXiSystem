/**
 * 
 */
package zhongchiedu.app.controller.system;

import java.util.List;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.commons.utils.Contents;
import zhongchiedu.commons.utils.UserType;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.system.log.annotation.SystemControllerLog;
import zhongchiedu.system.pojo.SysRole;
import zhongchiedu.system.pojo.SysUnit;
import zhongchiedu.system.service.SysUnitService;

/**  
* <p>Title: SysUnitController</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月9日  
*/
@Controller
@RequestMapping("/admin")
@Slf4j
public class SysUnitController {
	
	@Autowired
	private SysUnitService sysUnitService;
	
	
	
	@GetMapping("sysUnits")
	@RequiresPermissions(value = "system:sysunits")
	@SystemControllerLog(description = "查询所有计量单位")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
		// 分页查询数据
		log.info("查询所有计量单位");
		Pagination<SysUnit> pagination = this.sysUnitService.findPagination(pageNo, pageSize);
		model.addAttribute("pageList", pagination);
		
		return "system/sysUnit/list";
	}
	
	
	/**
	 * 
	 * <p>Title: toeditPage</p>  
	 * <p>Description:跳转到编辑页面 </p>  
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/sysUnit/{id}")
	@RequiresPermissions(value = "system:sysunits:edit")
	public String toeditPage(@PathVariable String id, Model model) {
		log.info("修改计量单位"+id);
		SysUnit sysUnit = this.sysUnitService.findOneById(id, SysUnit.class);
		model.addAttribute("sysUnit", sysUnit);
		
		return "system/sysUnit/add";

	}
	
	/**
	 * 
	 * <p>
	 * Title: addSysUnitPage
	 * </p>
	 * <p>
	 * Description:跳转到添加界面
	 * </p>
	 * 
	 * @return
	 */
	@GetMapping("/sysUnit")
	public String addSysUnitPage(Model model) {
		return "system/sysUnit/add";
	}
	
	
	
	@DeleteMapping("/sysUnit/{id}")
	@RequiresPermissions(value = "system:sysunits:delete")
	@SystemControllerLog(description = "删除计量单位")
	public String delete(@PathVariable String id,HttpSession session) {
		log.info("删除计量单位" + id);
		this.sysUnitService.delete(id);
		log.info("删除计量单位" + id + "成功");
		return "redirect:/admin/sysUnits";
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
	@PostMapping("/sysUnit")
	@RequiresPermissions(value = "system:sysunits:add")
	@SystemControllerLog(description = "添加用户")
	public String addSysUnit(HttpServletRequest request, @ModelAttribute("sysUnit") SysUnit sysUnit) {
		this.sysUnitService.saveOrUpdate(sysUnit);
		return "redirect:/admin/sysUnits";
	}

	
	
	
	
	
	
	
	@PutMapping("/sysUnit")
	@RequiresPermissions(value = "system:sysunits:edit")
	@SystemControllerLog(description = "编辑计量单位")
	public String editSysUnit(HttpServletRequest request, @ModelAttribute("sysUnit") SysUnit sysUnit) {
		this.sysUnitService.saveOrUpdate(sysUnit);
		return "redirect:/admin/sysUnits";
	}

	
	@RequestMapping(value = "/sysUnit/ajaxgetRepletes", method = RequestMethod.POST)
	@ResponseBody
	public BasicDataResult ajaxgetRepletes(@RequestParam(value = "name", defaultValue = "") String name) {
		return this.sysUnitService.ajaxgetRepletes(name);
	}

	@RequestMapping(value = "/sysUnit/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult toDisable(@RequestParam(value = "id", defaultValue = "") String id) {
		return this.sysUnitService.toDisable(id);
	}
	
	
	
	
	
	
}
