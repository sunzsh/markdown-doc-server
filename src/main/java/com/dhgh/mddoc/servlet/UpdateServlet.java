package com.dhgh.mddoc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhgh.mddoc.user.User;
import com.dhgh.mddoc.util.CMDExcute;
import com.dhgh.mddoc.util.CurrentInfo;
import com.dhgh.mddoc.util.MDReader;
import com.dhgh.mddoc.util.ParseMD5;
import com.dhgh.mddoc.util.MDReader.MDFileNotExists;

public class UpdateServlet extends HttpServlet{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -9031786211864128970L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String api = req.getParameter("api");
		resp.setContentType("text/html; charset=utf-8");
		PrintWriter out = resp.getWriter();
		if (api == null || api.trim().length() == 0) {
			out.println("{\"result\":\"0\", \"msg\": \"api不能为空\"}");
			return;
		}

		boolean hasAuth = User.hasAuth(api, CurrentInfo.getCurrentUser());
		if (!hasAuth) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		
		String rootPath = MDReader.getDocRoot4Config();
		String file = null;
		if (rootPath == null) {
			URL url = MDReader.class.getResource(api);
			if (url == null) {
				out.println("{\"result\":\"0\", \"msg\": \"api路径不正确\"}");
				return;
			}
			file = url.getFile();
		} else {
			file = rootPath + api;
		}
		String cmd = "pushd "+file+" && git pull && popd";
		System.out.println(cmd);
		String result = CMDExcute.exec(cmd);
		out.println("{\"result\":\"0\", \"msg\": \""+result.trim().replaceAll("\"", "\\\"").replaceAll("\r\n",	"\\\\r\\\\n").replaceAll("\n", "\\\\r\\\\n")+"\"}");
	}

	
}
