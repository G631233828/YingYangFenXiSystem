package zhongchiedu.app.config;



import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionTrackingMode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import zhongchiedu.app.compent.LoginHandlerInterceptor;
import zhongchiedu.app.compent.WebHandlerInterceptor;

@Configuration
public class MVCConfig extends WebMvcConfigurerAdapter {

	@Value("${upload.imgpath}")
	private String imgpath;
//	@Value("${upload.kinderitor}")
//	private String kinderitor;
	@Value("${upload.savedir}")
	private String dir;
	@Value("${upload.ueditor}")
	private String ueditor;
	@Value("${upload.ueditordir}")
	private String ueditordir;
	
	 @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//	    	registry.addResourceHandler("/img/**").addResourceLocations("file:"+filepath+"/"); 
//	    	registry.addResourceHandler(savePath+"/**").addResourceLocations("file:"+savePath+"/");
//	    	registry.addResourceHandler(kinderitor+"/**").addResourceLocations("file:"+dir+kinderitor+"/");
	    	registry.addResourceHandler(imgpath+"/**").addResourceLocations("file:"+dir+imgpath+"/");
	    	registry.addResourceHandler(ueditor+"/**").addResourceLocations("file:"+dir+ueditor+"/");
	    	registry.addResourceHandler(ueditordir+"/**").addResourceLocations("file:"+ueditordir+"/");
		    	super.addResourceHandlers(registry);
		    }
			  

		  
//
//    @Bean
//    public SessionInterceptor sessionInterceptor(){
//    	return new SessionInterceptor();
//    }
    
//	
//	@Override
//	public void addViewControllers(ViewControllerRegistry registry) {
//
//		registry.addViewController("/").setViewName("/website/index");
//
//	}

//	@Bean
//	public MiniInterceptor miniInterceptor(){
//		return new MiniInterceptor();
//	}
	
	@Bean
	public LoginHandlerInterceptor loginHandlerInterceptor(){
		return new LoginHandlerInterceptor();
	}
	
	@Bean
	public WebHandlerInterceptor webHandlerInterceptor() {
		return new WebHandlerInterceptor();
	}
	
	
	/**
	 * shiro 界面整合 thymeleaf
	 * @return
	 */
	@Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

	


	// 配置多个访问地址自动转发
	@Bean
	public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
		WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter() {
			@Override
			public void addViewControllers(ViewControllerRegistry registry) {
				registry.addViewController("/login").setViewName("index");
				registry.addViewController("/toindex").setViewName("index");
				registry.addViewController("/").setViewName("/web/index");
//				registry.addViewController("/").setViewName("/website/index");
			}


			//注册拦截器
			//不要使用new xxx否则拦截器中会注入失败
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				//registry.addInterceptor(sessionInterceptor()).addPathPatterns("/**").excludePathPatterns("/index.html","/user/login");
				registry.addInterceptor(loginHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns("/index.html","/user/login");
				registry.addInterceptor(webHandlerInterceptor()).addPathPatterns("/web/**").excludePathPatterns("/assets/**");
			}
			
		};
		return adapter;
	}
	
	
	
	/**
	 * 解决静态资源中出现了jsessionid的问题
	 * @return
	 */
	@Bean
	public ServletContextInitializer servletContextInitializer1() {
	    return new ServletContextInitializer() {
	        @Override
	        public void onStartup(ServletContext servletContext) throws ServletException {
	            servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE) );
	        }
	    };
	}
	
	
	

	
	
	
	
	
	
	
	
	
	
}
	
