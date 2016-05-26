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
import org.eclipse.jgit.notes.Note;
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

public class ShowServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8457872538937299282L;


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html; charset=utf-8");
		String id = req.getParameter("id");
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
		
//		try {
//			
//			
//		} catch (GitAPIException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		out.println(hisStr.toString());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
	
}
