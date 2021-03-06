package com.ktds.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ktds.common.session.Session;
import com.ktds.member.vo.MembersVO;

public class SessionInterceptor extends HandlerInterceptorAdapter{
	
	@Override
	public boolean preHandle(
			HttpServletRequest request
			, HttpServletResponse response
			, Object handler) // handler ==> Controller
			throws Exception {
		
		HttpSession session = request.getSession();
		MembersVO membersVO = (MembersVO) session.getAttribute(Session.USER);
		
		if ( membersVO == null ) {
			response.sendRedirect("/HelloSpring/member/login");
			return false;
		}
		
		return true;
	}
	
}
