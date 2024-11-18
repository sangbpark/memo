package com.memo.interceptor;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PermissionInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws IOException {
		
		// 요청 url path
		String uri = request.getRequestURI();
		log.info("[@@@@@@@@@@@ preHandle] uri:{}" , uri);
		
		// 로그인 여부 -> 세션 
		HttpSession session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		
		// /post로 시작 && 비로그인 -> 로그인 페이지로 이동, 컨트롤러 수행 방지
		if (uri.startsWith("/post") && userId == null) {
			// redirect
			response.sendRedirect("/user/sign-in-view"); // 무한 루프 할 수도 있다. 반드시 모든 페이지 확인
			
			// 원래 가려했었던 컨트롤러 수행 방지
			return false;
		}
		
		if (uri.startsWith("/user") && userId != null) {
			response.sendRedirect("/post/post-list-view");
			
			return false;
		}
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView mav) {
		// view, model이 있다는건 html이 해석되기 전 
		// 요청에 따라 마지막에 세팅하는 거 
		log.info("[$$$$$$$$$$$ postHandle]");
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			Exception ex) {
		// html이 해석이 된 상태 렌더링이 끝난 상태
		log.info("[############ afterCompletion]");
	}

}
