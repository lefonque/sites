package org.epis.ws.manager.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

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
		logger.debug("Request URI : [{}]",request.getRequestURI());
		logger.debug("Request URL : [{}]",request.getRequestURL());
		logger.debug("Context Path : [{}]",request.getContextPath());
		boolean result = true;
		//예외처리
		if(!StringUtils.endsWith(request.getRequestURI(), "/config/login")){
			if(session.getAttribute("LOGIN")==null){
				response.sendRedirect(request.getContextPath() + "/index.jsp");
				result = false;
			}
		}
		return result;
	}

}
