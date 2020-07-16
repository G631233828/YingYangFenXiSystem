package zhongchiedu.app.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import zhongchiedu.school.pojo.WebMenu;
import zhongchiedu.school.service.NewsService;
import zhongchiedu.school.service.WebMenuService;

@RequestMapping("/web")
@Controller
public class WebController {
	
	@Autowired
	private WebMenuService webMenuService;
	@Autowired
	private NewsService newsService;

	

	@RequestMapping(value = "/index")
	public String toindex(Model model) {

		

		return "web/index";
	}
	
//	@RequestMapping("aaa/{name}/{value}")
//	//@RequiresPermissions(value = "webmenu:edit")
//	@SystemControllerLog(description = "修改资源")
//	public String test(@PathVariable("name")String name,@PathVariable("value")String value) {
//		System.out.println(name);
//		System.out.println(value);
//		System.out.println(name);  
//		System.out.println(value);
//		System.out.println(name);
//		System.out.println(value);
//		return "redirect:/school/webMenus";
//	}
//	
	
	
	@RequestMapping(value = "/list/{menuId}")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
			@PathVariable(value = "menuId",required = true)String menuId) {
		//1.获取相同目录的菜单
		WebMenu webMenu = this.webMenuService.findWebMenuById(menuId);
		// type = 1 
		if(webMenu.getType()==1) {
			List<WebMenu> webMenusleft = this.webMenuService.findWebMenuByFirstLevel(webMenu.getId());
			model.addAttribute("webMenusleft", webMenusleft);
			//网页当前位置导航 
			model.addAttribute("one", webMenu);
			//获取当前模块下的所有新闻
			
		}else if(webMenu.getType()== 2) {
			List<WebMenu> webMenusleft = this.webMenuService.findWebMenuByFirstLevel(webMenu.getFirstLevel());
			model.addAttribute("webMenusleft", webMenusleft);
			WebMenu one = this.webMenuService.findWebMenuById(webMenu.getFirstLevel());
			model.addAttribute("one", one);
			model.addAttribute("two", webMenu);
			//获取当前二级目录的所有新闻
			
			
		}
		model.addAttribute("webMenu", webMenu);
		
		
		
		

		return "web/list";
	}
	
//	@RequestMapping(value = "/list/{menuid}/{newsid}")
//	public String list(Model model,@PathVariable(value = "menuid",required = true)String menuid,@PathVariable(value ="newsid",required = false)String newsid) {
//		
//		
//		
//		//menuid 菜单 id  type =1 的菜单id  跳转到list界面
//		//newsid新闻id				        跳转到article界面
//		if(Common.isNotEmpty(newsid)) {
//			//查询新闻
//			News news = this.newsService.findNewsById(newsid);
//			model.addAttribute("news", news);
//			return "web/article";
//		}
//		else {
//			
//			
//		}
//		
//		
//		
//		return "web/index";
//	}
//	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
