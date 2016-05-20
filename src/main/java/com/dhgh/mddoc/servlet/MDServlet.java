package com.dhgh.mddoc.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pegdown.Extensions;
import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.plugins.PegDownPlugins;

import com.dhgh.mddoc.util.MDReader;
import com.dhgh.mddoc.util.MDReader.MDDocument;
import com.dhgh.mddoc.util.MDReader.MDFileNotExists;

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
		
		MDDocument mdDocument = null;
		try {
			mdDocument = MDReader.getDocument(uri);
		} catch (MDFileNotExists e) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		PegDownProcessor pegDownProcessor = new PegDownProcessor(Extensions.ALL);
		String body = pegDownProcessor.markdownToHtml(mdDocument.getBody());

		html.append("<!DOCTYPE html>\r\n");
		html.append("<html>\r\n");
		html.append("\t<head>\r\n");
		html.append("\t\t<title>"+ mdDocument.getTitle() +"</title>\r\n");
		html.append("\t\t<link type=\"text/css\" rel=\"stylesheet\" href=\""+req.getContextPath()+"/github.css\" />\r\n");
		html.append("\t</head>\r\n");
		html.append("\t<body>\r\n");
		html.append("<div class=\"container\">");
		html.append("<div id=\"markup\">");
		html.append("<article id=\"content\" class=\"markdown-body\">");
		
		html.append(body);

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
