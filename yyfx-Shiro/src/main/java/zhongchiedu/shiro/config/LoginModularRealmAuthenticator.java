/**
 * 
 */
package zhongchiedu.shiro.config;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.shiro.token.LoginToken;

/**
 * <p>
 * Title: LoginModularRealmAuthenticator
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author 郭建波
 * @date 2020年1月16日
 */
@Slf4j
public class LoginModularRealmAuthenticator extends
		ModularRealmAuthenticator {/*
									 * <p>Title: doAuthenticate</p> <p>Description: </p>
									 * 
									 * @param authenticationToken
									 * 
									 * @return
									 * 
									 * @throws AuthenticationException
									 * 
									 * @see org.apache.shiro.authc.pam.ModularRealmAuthenticator#doAuthenticate(org.
									 * apache.shiro.authc.AuthenticationToken)
									 */
	@Override
	protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		log.info("LoginModularRealmAuthenticator -------   execute");

		assertRealmsConfigured();// 判断getRealms是否返回为空
		// 强制转换回自定义的CustomizedToken
		LoginToken loginToken = (LoginToken) authenticationToken;
		String loginType = loginToken.getLoginType();
		// 获取所有Realm
		// 所有Realm
		Collection<Realm> realms = getRealms();
		Collection<Realm> typeRealms = new ArrayList<>();
		// 登陆类型对应所有的realm
		for (Realm realm : realms) {
			if (realm.getName().contains(loginType)) {
				typeRealms.add(realm);
			}
		}
		// 判断是单Realm还是多Realm
		if (typeRealms.size() == 1) {
			log.info("doSingleRealmAuthentication() execute ");
			return doSingleRealmAuthentication(typeRealms.iterator().next(), loginToken);
		} else {
			log.info("doMultiRealmAuthentication() execute ");
			return doMultiRealmAuthentication(typeRealms, loginToken);
		}

	}

}
