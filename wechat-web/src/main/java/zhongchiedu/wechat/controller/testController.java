package zhongchiedu.wechat.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;


@RestController
@RequestMapping("/weiweb")
public class testController {

	@Autowired
	private WxMpService wxMpService;
	
	@RequestMapping("/index")
	public String test() throws WxErrorException {
		
	System.out.println(wxMpService.getAccessToken());
		return "temp/index.html";
	}
	

	
	public static void main(String[] args) throws WxErrorException {
		WxMpService wx = new WxMpServiceImpl();
		String token = wx.getAccessToken();
		System.out.println(token);
	}

	
	
	
	
	
	
	
	
	
}
