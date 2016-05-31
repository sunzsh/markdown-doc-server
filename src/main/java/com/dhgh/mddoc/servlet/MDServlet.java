package com.dhgh.mddoc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pegdown.Extensions;
import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.plugins.PegDownPlugins;

import com.dhgh.mddoc.util.HistoryCommit;
import com.dhgh.mddoc.util.MDReader;
import com.dhgh.mddoc.util.MDReader.MDDocument;
import com.dhgh.mddoc.util.MDReader.MDFileNotExists;
import com.dhgh.mddoc.util.ReadFromFile;

public class MDServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5038871760923684439L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html; charset=utf-8");
		StringBuffer html = new StringBuffer();
		String uri = req.getRequestURI();
		String id = req.getParameter("id");
		
		
		MDDocument mdDocument = null;
		HistoryCommit historyCommit = null;
		if (id != null) {
			String rootPath = MDReader.getDocRoot4Config();
			String path = null;
			String project = req.getRequestURI().substring(0, req.getRequestURI().indexOf('/', 1));
			String logFile = req.getRequestURI().substring(req.getRequestURI().indexOf('/', 1)+1);
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
			
			historyCommit = ShowServlet.fetchBlob(id, logFile, path);
			mdDocument = ReadFromFile.parseDocumentByContent(historyCommit.getContent());
		} else {
			try {
				mdDocument = MDReader.getDocument(uri);
			} catch (MDFileNotExists e) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}
		
		PegDownProcessor pegDownProcessor = new PegDownProcessor(Extensions.ALL_WITH_OPTIONALS);
		String body = pegDownProcessor.markdownToHtml(mdDocument.getBody());

		html.append("<!DOCTYPE html>\r\n");
		html.append("<html>\r\n");
		html.append("\t<head>\r\n");
		html.append("\t\t<title>"+ mdDocument.getTitle() +"</title>\r\n");
		html.append("\t\t<link type=\"text/css\" rel=\"stylesheet\" href=\""+req.getContextPath()+"/github.css\" />\r\n");
		if (id != null) {
			html.append("<script src=\"http://cdn.bootcss.com/jquery/1.11.3/jquery.min.js\"></script>");
			html.append("<script>$(function(){"
					+ "$('a').each(function(i, n){"
					+ "	var href = $(n).attr('href');"
					+ " if (href.match(/.*.md$/g)) { $(n).attr('href', href + '?id="+id+"') }"
					+ "});})</script>");
			html.append("<style>body{background-color:#eafcf7;}</style>");
		}
		html.append("\t</head>\r\n");
		html.append("\t<body>\r\n");
		html.append("<div class=\"container\">");
		html.append("<div id=\"markup\">");
		html.append("<article id=\"content\" class=\"markdown-body\">");
		
		html.append(body);
		if (id == null) {
			html.append("<h1></h1><div style='text-align:right;'><a style='' href='"+uri +".log' target='_blank'>当前文件历史记录</a></div>");
		} else {
			html.append("<h1></h1><div style='text-align:right;color:#bbbbbb'><span>"+historyCommit.getFullMessage()+"</span><br/><span>"+historyCommit.getAuth()+" @"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(historyCommit.getWhen())+"<span></div>");
		}
		
		html.append("</div>");
		html.append("</div>");
		html.append("</article>");
		html.append("\r\n\t</body>");
		html.append("\r\n</html>");
		
		PrintWriter out = resp.getWriter();
		out.println(html.toString());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
	
}
