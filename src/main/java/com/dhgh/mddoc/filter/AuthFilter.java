package com.dhgh.mddoc.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhgh.mddoc.user.User;
import com.dhgh.mddoc.util.CurrentInfo;
import com.dhgh.mddoc.util.ParseMD5;

public class AuthFilter implements Filter{
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		

		User user = CurrentInfo.getCurrentUser();
		if (user ==  null) {
			gotoLogin(req, resp);
			return;
		}

		String uri = req.getRequestURI();
		int index = uri.indexOf('/', 1);
		String requestProject = null;
		if (index >= 0) {
			requestProject = uri.substring(0, index);
		} else {
			requestProject = "";
		}
		
		boolean hasAuth = User.hasAuth(requestProject, user);
		if (!hasAuth) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		
		chain.doFilter(request, response);
	}
	
	public void gotoLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.sendRedirect(req.getContextPath()+ "/login.html");
	}

	@Override
	public void destroy() {
	}

}
