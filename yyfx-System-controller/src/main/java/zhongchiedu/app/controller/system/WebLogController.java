package zhongchiedu.app.controller.system;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.system.log.annotation.SystemControllerLog;
import zhongchiedu.system.pojo.WebLog;
import zhongchiedu.system.service.impl.WebLogServiceImpl;

@Controller
@RequestMapping("/admin")
public class WebLogController {

	private @Autowired WebLogServiceImpl weblogService;

	@GetMapping("weblogs")
	 @RequiresPermissions(value = "weblogs:list")
	 @SystemControllerLog(description = "查询网站所有日志")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
		// 分页查询数据
		Pagination<WebLog> pagination;
		try {
			pagination = this.weblogService.findAllWebLog(pageNo, pageSize, "0");
			if (pagination == null)
				pagination = new Pagination<WebLog>();

			model.addAttribute("pageList", pagination);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "admin/weblog/logs";
	}

	@GetMapping("weberrors")
	 @RequiresPermissions(value = "weblogs:errorlist")
	 @SystemControllerLog(description = "查询网站所有错误日志")
	public String errors(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
		// 分页查询数据
		Pagination<WebLog> pagination;
		try {
			pagination = this.weblogService.findAllWebLog(pageNo, pageSize, "1");
			if (pagination == null)
				pagination = new Pagination<WebLog>();

			model.addAttribute("pageList", pagination);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "admin/weblog/errors";
	}

}
