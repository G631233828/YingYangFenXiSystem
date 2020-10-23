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
import zhongchiedu.system.pojo.Log;
import zhongchiedu.system.service.impl.LogServiceImpl;

@Controller
@RequestMapping("/admin")
public class LogController {

	private @Autowired LogServiceImpl logService;

	@GetMapping("logs")
	 @RequiresPermissions(value = "logs:list")
	 @SystemControllerLog(description = "查询所有日志")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
		// 分页查询数据
		Pagination<Log> pagination;
		try {
			pagination = this.logService.findAllLog(pageNo, pageSize, "0");
			if (pagination == null)
				pagination = new Pagination<Log>();

			model.addAttribute("pageList", pagination);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "admin/log/logs";
	}

	@GetMapping("errors")
	 @RequiresPermissions(value = "logs:errorlist")
	 @SystemControllerLog(description = "查询所有日志")
	public String errors(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {
		// 分页查询数据
		Pagination<Log> pagination;
		try {
			pagination = this.logService.findAllLog(pageNo, pageSize, "1");
			if (pagination == null)
				pagination = new Pagination<Log>();

			model.addAttribute("pageList", pagination);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "admin/log/errors";
	}

}
