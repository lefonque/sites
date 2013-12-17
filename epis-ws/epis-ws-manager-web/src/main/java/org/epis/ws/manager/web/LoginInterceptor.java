package org.epis.ws.manager.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * <pre>
 * <p>로그인 여부에 따른 접근처리를 하는 Interceptor</p>
 * 로그인 처리가 성공하면 HttpSession에 로그인한 유저의 정보를 bind하기 때문에,
 * Session을 뒤져서 로그인 여부를 판단하여 접근처리를 한다.
 * </pre>
 * @author developer
 *
 */
public class LoginInterceptor implements HandlerInterceptor {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2, ModelAndView mav) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		HttpSession session = request.getSession();
		logger.trace("Request URI : [{}]",request.getRequestURI());
		logger.trace("Request URL : [{}]",request.getRequestURL());
		logger.trace("Context Path : [{}]",request.getContextPath());
		boolean result = true;
		//예외처리
		if(!StringUtils.endsWith(request.getRequestURI(), "/config/login")){
			if(session.getAttribute("LOGIN")==null){
				request.setAttribute("msg","Session Expired");
				response.sendRedirect(request.getContextPath() + "/index.jsp");
				result = false;
			}
		}
		return result;
	}

}
