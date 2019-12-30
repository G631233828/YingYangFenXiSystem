package zhongchiedu.app.controller.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.system.log.annotation.SystemControllerLog;
import zhongchiedu.system.pojo.Role;
import zhongchiedu.system.pojo.User;
import zhongchiedu.system.service.impl.RoleServiceImpl;
import zhongchiedu.system.service.impl.UserServiceImpl;

@Controller
@RequestMapping("/admin")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private RoleServiceImpl roleService;

	
	@Value("${upload-imgpath}")
	private String imgPath;
	@Value("${upload-dir}")
	private String dir;

	
	
	@GetMapping("users")
	@RequiresPermissions(value = "user:list")
	@SystemControllerLog(description = "查询所有用户")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
		// 分页查询数据
		Pagination<User> pagination;
		try {
			pagination = userService.findPaginationByQuery(new Query(), pageNo, pageSize, User.class);
			if (pagination == null)
				pagination = new Pagination<User>();

			model.addAttribute("pageList", pagination);
		} catch (Exception e) {
			log.info("查询所有用户信息失败——————————》" + e.toString());
			e.printStackTrace();
		}

		return "admin/user/list";
	}

	/**
	 * 跳转到添加页面
	 */
	@GetMapping("/user")
	@RequiresPermissions(value = "user:add")
	public String addUserPage(Model model) {

		List<Role> list = this.roleService.findAllRoleByisDisable();

		model.addAttribute("roleList", list);
		return "admin/user/add";
	}

	@PostMapping("/user")
	@RequiresPermissions(value = "user:add")
	@SystemControllerLog(description = "添加用户")
	public String addUser(HttpServletRequest request, @ModelAttribute("user") User user,
			@RequestParam(value = "roleId", defaultValue = "") String roleId, @RequestParam("file") MultipartFile[] file,
			@RequestParam(value = "oldheadImg", defaultValue = "") String oldheadImg) {
		this.userService.saveOrUpdateUser(user, roleId,file,imgPath,dir,oldheadImg);

		return "redirect:users";
	}

	@PutMapping("/user")
	@RequiresPermissions(value = "user:edit")
	@SystemControllerLog(description = "修改用户")
	public String editUser(HttpServletRequest request, @ModelAttribute("user") User user,
			@RequestParam(value = "roleId", defaultValue = "") String roleId, @RequestParam("file")MultipartFile[] file,
			@RequestParam(value = "oldheadImg", defaultValue = "") String oldheadImg) {
		
		this.userService.saveOrUpdateUser(user, roleId,file,imgPath,dir,oldheadImg);

		return "redirect:users";
	}

	/**
	 * 跳转到编辑界面
	 * 
	 * @return
	 */
	@GetMapping("/user{id}")
	@RequiresPermissions(value = "user:edit")
	@SystemControllerLog(description = "编辑用户")
	public String toeditPage(@PathVariable String id, Model model) {

		List<Role> list = this.roleService.findAllRoleByisDisable();

		model.addAttribute("roleList", list);

		User user = this.userService.findOneById(id, User.class);

		model.addAttribute("user", user);

		return "admin/user/add";

	}

	@DeleteMapping("/user/{id}")
	@RequiresPermissions(value = "user:delete")
	@SystemControllerLog(description = "删除用户")
	public String delete(@PathVariable String id) {
		String[] strids = id.split(",");
		for (String delids : strids) {
			log.info("删除用户---》" + delids);
			User rm = this.userService.findOneById(delids, User.class);
			this.userService.remove(rm);// 删除某个id
		}
		return "redirect:/users";
	}

	/**
	 * 通过ajax获取是否存在重复账号的信息
	 * 
	 * @param printWriter
	 * @param session
	 * @param response
	 */
	@RequestMapping(value = "/user/ajaxgetRepletes", method = RequestMethod.POST)
	@ResponseBody
	public BasicDataResult ajaxgetRepletes(@RequestParam(value = "accountName", defaultValue = "") String accountName) {

		return this.userService.ajaxgetRepletes(accountName);

	}
	
	@RequestMapping(value = "/user/checkPassword", method = RequestMethod.POST)
	@ResponseBody
	public BasicDataResult checkPassword(@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "password", defaultValue = "") String password) {
		
		return this.userService.checkPassword(id, password);
		
	}

	@RequestMapping(value = "/user/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult userDisable(@RequestParam(value = "id", defaultValue = "") String id) {

		return this.userService.userDisable(id);

	}
	
	@RequestMapping(value = "/user/editPassword", method = RequestMethod.POST)
	@ResponseBody
	public BasicDataResult editPassword(@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "password2", defaultValue = "") String password2) {
		
		return this.userService.editPassword(id, password2);
	}
	
	
	

}
