package zhongchiedu.wechat.service;

import java.util.List;

import zhongchiedu.commons.utils.BasicDataResult;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.wechat.pojo.Menu;
import zhongchiedu.wechat.pojo.WeChatMenu;

public interface WeChatMenuService  extends GeneralService<WeChatMenu>{

	
	 List<WeChatMenu> findWeChatMenus();
	
	 void saveOrUpdate(WeChatMenu weChatMenu);
	
	 int getParentSize(String parentId);
	 
	 BasicDataResult findWeChatMenuByid(String id);
	
	 void deleteWeChatMenuById(String id);
	
	 BasicDataResult release();
	 
	 Menu getMenu();
	
}
