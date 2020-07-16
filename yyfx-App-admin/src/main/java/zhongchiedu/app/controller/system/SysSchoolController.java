/**
 * 
 */
package zhongchiedu.app.controller.system;

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
import zhongchiedu.system.pojo.SysSchool;
import zhongchiedu.system.service.SysSchoolService;

/**  
* <p>Title: SysSchoolController</p>  
* <p>Description: </p>  
* @author 郭建波  
* @date 2020年1月15日  
*/
@Controller
@RequestMapping("/admin")
@Slf4j
public class SysSchoolController {

	
	@Autowired
	private SysSchoolService sysSchoolService;
	
	@GetMapping("sysSchools")
	@RequiresPermissions(value = "sysschool:list")
	@SystemControllerLog(description = "查询所有学校")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
		// 分页查询数据
		log.info("查询所有学校");
		Pagination<SysSchool> pagination = this.sysSchoolService.findPagination(pageNo, pageSize);
		model.addAttribute("pageList", pagination);
		return "system/sysSchool/list";
	}
	
	
	/**
	 * 
	 * <p>Title: toeditPage</p>  
	 * <p>Description:跳转到编辑页面 </p>  
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/sysSchool/{id}")
	@RequiresPermissions(value = "sysschool:edit")
	public String toeditPage(@PathVariable String id, Model model) {
		log.info("修改学校"+id);
		SysSchool sysSchool = this.sysSchoolService.findOneById(id, SysSchool.class);
		model.addAttribute("sysSchool", sysSchool);
		return "system/sysSchool/add";

	}
	
	
	/**
	 * 
	 * <p>
	 * Title: addSysSchoolPage
	 * </p>
	 * <p>
	 * Description:跳转到添加界面
	 * </p>
	 * 
	 * @return
	 */
	@GetMapping("/sysSchool")
	public String addSysSchoolPage() {
		return "system/sysSchool/add";
	}
	
	
	
	@DeleteMapping("/sysSchool/{id}")
	@RequiresPermissions(value = "sysschool:delete")
	@SystemControllerLog(description = "删除学校")
	public String delete(@PathVariable String id) {
		log.info("删除学校" + id);
		this.sysSchoolService.delete(id);
		log.info("删除学校" + id + "成功");
		return "redirect:/admin/sysSchools";
	}
	
	
	@PostMapping("/sysSchool")
	@RequiresPermissions(value = "sysschool:add")
	@SystemControllerLog(description = "添加学校")
	public String addsysRole(
			@ModelAttribute("sysSchool") SysSchool sysSchool) {
		this.sysSchoolService.saveOrUpdate(sysSchool);
		return "redirect:/admin/sysSchools";
	}
	
	
	@PutMapping("/sysSchool")
	@RequiresPermissions(value = "sysschool:edit")
	@SystemControllerLog(description = "修改学校")
	public String editSysRole(
			@ModelAttribute("sysSchool") SysSchool sysSchool) {
		this.sysSchoolService.saveOrUpdate(sysSchool);
		return "redirect:/admin/sysSchools";
	}
	
	
	@RequestMapping(value = "/sysSchool/ajaxgetRepletes", method = RequestMethod.POST)
	@ResponseBody
	public BasicDataResult ajaxgetRepletes(@RequestParam(value = "name", defaultValue = "") String name) {
		return this.sysSchoolService.ajaxgetRepletes(name);
	}

	@RequestMapping(value = "/sysSchool/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult toDisable(@RequestParam(value = "id", defaultValue = "") String id) {
		return this.sysSchoolService.toDisable(id);

	}
	
	
	
	
	
}
