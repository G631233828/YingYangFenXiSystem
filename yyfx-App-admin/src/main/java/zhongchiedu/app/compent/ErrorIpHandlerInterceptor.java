//package zhongchiedu.app.compent;
//
//import java.util.Enumeration;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import lombok.extern.slf4j.Slf4j;
//import zhongchiedu.xss.XssFilterUtil;
//
//@Component
//@Slf4j
//public class ErrorIpHandlerInterceptor implements HandlerInterceptor{@Override
//	
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//	String uri = request.getRequestURI().toString();
//	String newuri = XssFilterUtil.clean(uri);
//	System.out.println("old"+uri);
//	System.out.println("new"+newuri);
//	
//	
////		if(isErrorIp!=null&&isErrorIp.equals("true")) {
////			response.setHeader("Content-Type", "text/html;charset=utf-8");  //设置响应头的编码
////			response.setCharacterEncoding("UTF-8");
////			response.getWriter().append("<h1 style=\"text-align:center;\">您涉嫌非法访问，ip已被监控，如在继续ip将被移入黑名单!</h1>");
////			return false;
////		}
////	
//	
//		return HandlerInterceptor.super.preHandle(request, response, handler);
//	}
//
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//			ModelAndView modelAndView) throws Exception {
//		// TODO Auto-generated method stub
//		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//	}
//
//	@Override
//	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
//			throws Exception {
//		// TODO Auto-generated method stub
//		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//
//}
////@Override
////public String getParameter(String name) {
////    if (("content".equals(name) || name.endsWith("WithHtml")) && !isIncludeRichText) {
////        return super.getParameter(name);
////    }
////    name = XssFilterUtil.clean(name);
////    String value = super.getParameter(name);
////    if (StringUtils.isNotBlank(value)) {
////    	String oldvalue = value;
////    	String newvalue = XssFilterUtil.clean(value);
////    	if(!oldvalue.equals(newvalue)) {
////    		this.setErrorip(true);
////    	}
////        value = newvalue;
////    }
////    System.out.println("1111"+this.isErrorip());
////    return value;
////}
////
////@Override
////public String[] getParameterValues(String name) {
////    String[] arr = super.getParameterValues(name);
////    if (arr != null) {
////        for (int i = 0; i < arr.length; i++) {
////        	String a = arr[i];
////        	String b =  XssFilterUtil.clean(arr[i]);
////        	if(!a.equals(b)) {
////        		this.setErrorip(true);
////        	}
////            arr[i] = XssFilterUtil.clean(arr[i]);
////        }
////    }
////    System.out.println("1111"+this.isErrorip());
////    return arr;
////}
