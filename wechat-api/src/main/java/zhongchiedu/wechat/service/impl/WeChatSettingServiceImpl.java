package zhongchiedu.wechat.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import zhongchiedu.commons.utils.Common;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.wechat.pojo.WeChatSetting;
import zhongchiedu.wechat.service.WeChatSettingService;
@Service
public class WeChatSettingServiceImpl extends GeneralServiceImpl<WeChatSetting> implements WeChatSettingService {

	@Override
	public void saveOrUpdate(WeChatSetting weChatSetting) {
		WeChatSetting getWeChatSetting = null;
		if(Common.isNotEmpty(weChatSetting.getId())) {
			getWeChatSetting = this.findOneById(weChatSetting.getId(), WeChatSetting.class);
		}
		if(Common.isNotEmpty(getWeChatSetting)) {
			BeanUtils.copyProperties(weChatSetting, getWeChatSetting);
			this.save(getWeChatSetting);
		}else {
			this.insert(weChatSetting);
		}
	}



}
