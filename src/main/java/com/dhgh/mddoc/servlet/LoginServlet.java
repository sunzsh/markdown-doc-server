package com.dhgh.mddoc.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhgh.mddoc.user.User;
import com.dhgh.mddoc.util.ParseMD5;

public class LoginServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3074818792365798744L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("loginName");
		String pwd = req.getParameter("password");
		String remember = req.getParameter("remember");
		resp.setContentType("text/html; charset=utf-8");
		PrintWriter out = resp.getWriter();
		if (name == null || name.trim().length() == 0) {
			out.println("{\"result\":\"0\", \"msg\": \"用户名不能为空\"}");
			return;
		}
		if (pwd == null || pwd.trim().length() == 0) {
			out.println("{\"result\":\"0\", \"msg\": \"密码不能为空\"}");
			return;
		}
		
		User user = User.allUser.get(name);
		if (user == null) {
			out.println("{\"result\":\"0\", \"msg\": \"用户名或密码不正确\"}");
			return;
		}
		if (!ParseMD5.parseStrToMd5L32(pwd+User.SYS_PASSWORD_TOKEN).toLowerCase().equalsIgnoreCase(user.getLoginPwd())) {
			out.println("{\"result\":\"0\", \"msg\": \"用户名或密码不正确\"}");
			return;
		}
		String token = ParseMD5.parseStrToMd5L32(user.getLoginPwd()+User.SYS_USER_TOKEN);
		out.println("{\"result\":\"1\", \"msg\": \"登录成功\", \"name\":\""+name+"\", \"token\":\""+token+"\", \"remeber\":\""+(remember==null||remember.trim().length()==0?"0":"1")+"\"}");
		
	}

}
