package com.dhgh.mddoc.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RevertCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.pegdown.Extensions;
import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.plugins.PegDownPlugins;

import com.dhgh.mddoc.util.MDReader;
import com.dhgh.mddoc.util.MDReader.MDDocument;
import com.dhgh.mddoc.util.MDReader.MDFileNotExists;

public class HistoryServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8457872538937299282L;


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html; charset=utf-8");
		String rootPath = MDReader.getDocRoot4Config();
		String path = null;
		String project = req.getRequestURI().substring(0, req.getRequestURI().indexOf('/', 1));
		String logFile = req.getRequestURI().substring(req.getRequestURI().indexOf('/', 1)+1).replaceAll("\\.log$", "");
		if (rootPath == null) {
			URL url = MDReader.class.getResource("/");
			if (url == null) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			path = url.getFile() + project;
		} else {
			path = rootPath + project;
		}
		Git git = Git.open(new File(path));
		
		PrintWriter out = resp.getWriter();
		StringBuffer hisStr = new StringBuffer();
		hisStr.append("<!DOCTYPE html>");
		hisStr.append("<html>");
		hisStr.append("<head>");
		hisStr.append("<title>" + logFile + "日志</title>");
		hisStr.append("<link rel='stylesheet' href='http://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css'>");
		hisStr.append("</head>");
		hisStr.append("<body style='padding: 20px;'>");
		hisStr.append("<div class='list-group'>");
		try {
			RevWalk revWalk = (RevWalk)git.log().addPath(logFile).call();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(RevCommit revCommit : revWalk){
//				hisStr.append("<li><span class='time'>" + sdf.format(revCommit.getCommitterIdent().getWhen()) + "</span><a href='"+req.getRequestURI().replaceAll("\\.log$", ".show")+"?id="+revCommit.getId().name()+"'>"+revCommit.getId().name()+"</a><span class='msg'>"+revCommit.getFullMessage()+"</span><span class='user'>"+ revCommit.getAuthorIdent().getName() +"</span></li>");
				hisStr.append("<a href='"+req.getRequestURI().replaceAll("\\.log$", ".show")+"?id="+revCommit.getId().name()+"' class='list-group-item'>"
							+ "<h5 class='list-group-item-heading'>" + sdf.format(revCommit.getCommitterIdent().getWhen()) + "<span style='color:#337AB7;'> by " + revCommit.getAuthorIdent().getName() + "</span></h5>"
							+ "<p class='list-group-item-text'>" + revCommit.getFullMessage() + "</p>"
							+ "</a>");
			}
		} catch (GitAPIException e) {
			e.printStackTrace();
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		hisStr.append("</div>");
		hisStr.append("</body>");
		hisStr.append("</html>");
		out.println(hisStr.toString());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
	
}
