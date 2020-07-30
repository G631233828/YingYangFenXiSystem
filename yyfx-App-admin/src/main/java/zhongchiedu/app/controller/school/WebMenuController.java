package zhongchiedu.app.controller.school;

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
import zhongchiedu.school.pojo.WebMenu;
import zhongchiedu.school.service.WebMenuService;
import zhongchiedu.system.log.annotation.SystemControllerLog;

@Controller
@RequestMapping("/school")
@Slf4j
public class WebMenuController {

	@Autowired
	private WebMenuService webMenuService;

	@GetMapping("webMenus")
	@RequiresPermissions(value = "webmenu:list")
	@SystemControllerLog(description = "查询所有资源")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
		// 分页查询数据
		log.info("查询所有资源");
		Pagination<WebMenu> pagination = this.webMenuService.findPagination(pageNo, pageSize);
		model.addAttribute("pageList", pagination);

		return "school/webMenu/list";
	}

	
	/**
	 * 跳转到添加页面
	 */
	@GetMapping("/webMenu")
	@RequiresPermissions(value = "webMenu:add")
	public String addSysResourcePage(Model model) {
		List<WebMenu> list = this.webMenuService.findWebMenu("0",null);
		model.addAttribute("resList", list);
		return "school/webMenu/add";
	}
	
	
	
	/**
	 * 
	 * <p>
	 * Title: toeditPage
	 * </p>
	 * <p>
	 * Description:跳转到编辑页面
	 * </p>
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/webMenu/{id}")
	@RequiresPermissions(value = "webmenu:edit")
	public String toeditPage(@PathVariable String id, Model model) {
		log.info("修改菜单" + id);
		// 获取所有的启用的资源目录
		List<WebMenu> list = this.webMenuService.findWebMenu("0",null);
		model.addAttribute("resList", list);
		WebMenu webMenu = this.webMenuService.findOneById(id, WebMenu.class);
		model.addAttribute("webMenu", webMenu);
		
		//通过webMenu获取到当前二级菜单的所有一级菜单
		if(webMenu.getType() == 2) {
			//获取当前二级菜单下的所有一级菜单
		//parentId  type =1
			List<WebMenu> listmenus = this.webMenuService.findWebMenu(webMenu.getParentId(), 1);
			model.addAttribute("listmenus", listmenus);
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		return "school/webMenu/add";
	}

	@DeleteMapping("/webMenu/{id}")
	@RequiresPermissions(value = "webmenu:delete")
	@SystemControllerLog(description = "删除")
	public String delete(@PathVariable String id) {
		log.info("删除" + id);
		this.webMenuService.delete(id);
		log.info("删除" + id + "成功");
		return "redirect:/school/webMenus";
	}

	@PostMapping("/webMenu")
	@RequiresPermissions(value = "webmenu:add")
	@SystemControllerLog(description = "添加资源")
	public String addWebMenu(@ModelAttribute("webMenu") WebMenu webMenu) {
		this.webMenuService.saveOrUpdate(webMenu);
		return "redirect:/school/webMenus";
	}

	@PutMapping("/webMenu")
	@RequiresPermissions(value = "webmenu:edit")
	@SystemControllerLog(description = "修改资源")
	public String editWebMenu(@ModelAttribute("webMenu") WebMenu webMenu) {
		this.webMenuService.saveOrUpdate(webMenu);
		return "redirect:/school/webMenus";
	}

	@RequestMapping(value = "/webMenu/ajaxgetRepletes", method = RequestMethod.POST)
	@ResponseBody
	public BasicDataResult ajaxgetRepletes(@RequestParam(value = "name", defaultValue = "") String name) {
		return this.webMenuService.ajaxgetRepletes(name);
	}

	@RequestMapping(value = "/webMenu/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult toDisable(@RequestParam(value = "id", defaultValue = "") String id) {
		return this.webMenuService.toDisable(id);
	}
	
	/**
	 * 根据选择的目录获取菜单
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value="/webMenu/getFirstLevel",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult getFirstLevel(@RequestParam(value = "parentId", defaultValue = "") String parentId){

		List<WebMenu> list = this.webMenuService.findWebMenu(parentId,1);
		return list!=null?BasicDataResult.build(200, "success",list):BasicDataResult.build(400, "error",null);
	}
	
	/**
	 * 根据选择的目录获取菜单
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value="/webMenu/getSecondLevel",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult getSecondLevel(@RequestParam(value = "firstLevel", defaultValue = "") String firstLevel){
		
		List<WebMenu> list = this.webMenuService.findWebMenuByFirstLevel(firstLevel);
		return list!=null?BasicDataResult.build(200, "success",list):BasicDataResult.build(400, "error",null);
	}

}
