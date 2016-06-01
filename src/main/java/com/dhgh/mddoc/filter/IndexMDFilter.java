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

public class IndexMDFilter implements Filter{
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (ResPassFilter.isResource(request)) {
			chain.doFilter(request, response);
			return;
		}
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String uri = req.getRequestURI();
		
		if (uri.matches(".*\\/[^(\\/|\\.)]*\\/?$")) {
			resp.sendRedirect(uri.replaceAll("\\/$", "") + "/index.md");
		} else {
			chain.doFilter(request, response);
		}
	}
	
	@Override
	public void destroy() {
	}

}
