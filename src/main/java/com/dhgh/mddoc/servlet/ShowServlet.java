package com.dhgh.mddoc.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import com.dhgh.mddoc.util.MDReader;

public class ShowServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8457872538937299282L;


	private static String fetchBlob(String revSpec, String path, String rootPath)
			throws MissingObjectException, IncorrectObjectTypeException, IOException {
		Git git = Git.open(new File(rootPath)); 
        Repository repository = git.getRepository();  
        
		// Resolve the revision specification
		final ObjectId id = repository.resolve(revSpec);

		// Makes it simpler to release the allocated resources in one go
		ObjectReader reader = repository.newObjectReader();
		RevWalk walk = null;
		TreeWalk treewalk = null;
		try {
			// Get the commit object for that revision
			walk = new RevWalk(reader);
			RevCommit commit = walk.parseCommit(id);

			// Get the revision's file tree
			RevTree tree = commit.getTree();
			// .. and narrow it down to the single file's path
			treewalk = TreeWalk.forPath(reader, path, tree);

			if (treewalk != null) {
				// use the blob id to read the file's data
				byte[] data = reader.open(treewalk.getObjectId(0)).getBytes();
				return new String(data, "utf-8");
			} else {
				return "";
			}
		} finally {
			if (walk != null) {
				walk.close();
			}
			if (treewalk != null) {
				treewalk.close();
			}
		}
	}


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/raw; charset=utf-8");
		PrintWriter out = resp.getWriter();
		String id = req.getParameter("id");
		String rootPath = MDReader.getDocRoot4Config();
		String path = null;
		String project = req.getRequestURI().substring(0, req.getRequestURI().indexOf('/', 1));
		String logFile = req.getRequestURI().substring(req.getRequestURI().indexOf('/', 1)+1).replaceAll("\\.show$", "");
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
		String content = fetchBlob(id, logFile, path);
		out.println(content);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
	
}
