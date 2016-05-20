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

public class SetUserFilter implements Filter{
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		Cookie[] cookies = req.getCookies();
		String loginName = null;
		String token = null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("MDDOC-TOKEN")) {
				token = cookie.getValue();
			} else if (cookie.getName().equals("MDDOC-NAME")) {
				loginName = cookie.getValue();
			}
			if (loginName != null && token != null) {
				break;
			}
 		}
		Cookie lastCookie = new Cookie("MDDOC-LAST-URL", req.getContextPath() + req.getRequestURI());
		lastCookie.setPath("/");
		resp.addCookie(lastCookie);
		if (loginName ==  null || token == null) {
			return;
		}
		User user = User.allUser.get(loginName);
		if (user == null) {
			return;
		}
		
		if (!token.equalsIgnoreCase(ParseMD5.parseStrToMd5L32(user.getLoginPwd()+User.SYS_USER_TOKEN))) {
			return;
		}
		
		CurrentInfo.setValue(CurrentInfo.CURRENT_MEMUSER, user);
		chain.doFilter(request, response);
	}
	
	@Override
	public void destroy() {
	}

}
