package com.dhgh.mddoc.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ResPassFilter implements Filter{
	private static final String IS_RESOURCE_ATTR_KEY = "MDDOC_IS_RESOURCE_FILTER_KEY";
	
	public static boolean isResource(ServletRequest request) {
		return request.getAttribute(IS_RESOURCE_ATTR_KEY) != null;
	}
	private String[] types;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String typesStr = filterConfig.getInitParameter("types");
		if (typesStr != null && typesStr.trim().length() > 0) {
			types = typesStr.split("\\s*\\|\\s*");
		} else {
			types = null;
		}
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String uri = req.getRequestURI();
		if (types != null) {
			for (String type : types) {
				if (uri.endsWith(type)) {
					req.setAttribute(IS_RESOURCE_ATTR_KEY, 1l);
					break;
				}
			}
		}
		
		chain.doFilter(request, response);
	}
	
	@Override
	public void destroy() {
	}

}
