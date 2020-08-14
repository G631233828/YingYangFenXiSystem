package zhongchiedu.wechat.service;

import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.wechat.pojo.WeChatSetting;

public interface WeChatSettingService extends GeneralService<WeChatSetting>{
	void saveOrUpdate(WeChatSetting weChatSetting);

}
