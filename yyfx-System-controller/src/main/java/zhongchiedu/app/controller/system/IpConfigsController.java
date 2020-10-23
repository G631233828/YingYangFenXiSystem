package zhongchiedu.app.controller.system;

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

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.system.log.annotation.SystemControllerLog;
import zhongchiedu.system.pojo.IpConfigs;
import zhongchiedu.system.pojo.SysUnit;
import zhongchiedu.system.service.IpConfigsService;

@Controller
@RequestMapping("/admin")
@Slf4j
public class IpConfigsController {
	
	
	@Autowired
	private IpConfigsService ipConfigsService;
	
	
	@GetMapping("ipconfigs")
	@RequiresPermissions(value = "ipconfigs:list")
	@SystemControllerLog(description = "查询所有ip")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
		// 分页查询数据
		log.info("查询所有ip");
		Pagination<IpConfigs> pagination = this.ipConfigsService.findPagination(pageNo, pageSize);
		model.addAttribute("pageList", pagination);
		
		return "system/ipconfigs/list";
	}
	
	
	/**
	 * 
	 * <p>Title: toeditPage</p>  
	 * <p>Description:跳转到编辑页面 </p>  
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/ipconfig/{id}")
	@RequiresPermissions(value = "ipconfigs:edit")
	@SystemControllerLog(description = "编辑ip")
	public String toeditPage(@PathVariable String id, Model model) {
		log.info("修改ip"+id);
		IpConfigs ipconfig = this.ipConfigsService.findOneById(id, IpConfigs.class);
		model.addAttribute("ipconfig", ipconfig);
		return "system/ipconfigs/add";

	}
	
	/**
	 * 
	 * <p>
	 * Title: addIpconfigPage
	 * </p>
	 * <p>
	 * Description:跳转到添加界面
	 * </p>
	 * 
	 * @return
	 */
	@GetMapping("/ipconfig")
	@RequiresPermissions(value = "ipconfigs:add")
	public String addIpconfigPage(Model model) {
		return "system/ipconfigs/add";
	}
	
	
	
	@DeleteMapping("/ipconfig/{id}")
	@RequiresPermissions(value = "ipconfigs:delete")
	@SystemControllerLog(description = "删除ip")
	public String delete(@PathVariable String id,HttpSession session) {
		log.info("删除计量单位" + id);
		this.ipConfigsService.delete(id);
		log.info("删除计量单位" + id + "成功");
		return "redirect:/admin/ipconfigs";
	}
	
	
	/**
	 * 
	 * <p>
	 * Title: addsysRole
	 * </p>
	 * <p>
	 * Description: 添加
	 * </p>
	 * whiteOrBlack
	 * @param sysRole
	 * @return
	 */
	@PostMapping("/ipconfig")
	@RequiresPermissions(value = "ipconfigs:add")
	@SystemControllerLog(description = "添加ip")
	public String addSysUnit(HttpServletRequest request, @ModelAttribute("ipConfigs") IpConfigs ipConfigs) {
		this.ipConfigsService.saveOrUpdate(ipConfigs);
		return "redirect:/admin/ipconfigs";
	}

	
	
	
	@PutMapping("/ipconfig")
	@RequiresPermissions(value = "ipconfigs:edit")
	@SystemControllerLog(description = "编辑ip")
	public String editSysUnit(HttpServletRequest request, @ModelAttribute("ipConfigs") IpConfigs ipConfigs) {
		this.ipConfigsService.saveOrUpdate(ipConfigs);
		return "redirect:/admin/ipconfigs";
	}

	
	@RequestMapping(value = "/ipconfig/ajaxgetRepletes", method = RequestMethod.POST)
	@ResponseBody
	public BasicDataResult ajaxgetRepletes(@RequestParam(value = "ip", defaultValue = "") String ip) {
		return this.ipConfigsService.ajaxgetRepletes(ip);
	}

	@RequestMapping(value = "/ipconfig/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult toDisable(@RequestParam(value = "id", defaultValue = "") String id) {
		return this.ipConfigsService.toDisable(id);
	}
	
	
	
	
	

}
