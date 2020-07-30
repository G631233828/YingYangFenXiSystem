package zhongchiedu.wechat.pojo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.commons.utils.Types.menuType;

@Getter
@Setter
@ToString
public class Button {
	private String name;
	private List sub_button;
	private menuType type;// button的类型
	private String url;// url链接
	private String key;
}