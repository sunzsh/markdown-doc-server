package com.dhgh.mddoc.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Pattern;

public class MDReader {
	public static String DOC_ROOT = null;
	public static String getDocRoot4Config() throws FileNotFoundException, IOException {
		if (DOC_ROOT != null) {
			if (DOC_ROOT.equals("")) {
				return null;
			} else {
				return DOC_ROOT;
			}
		}
		Properties prop = new Properties();
		prop.load(new FileInputStream(MDReader.class.getResource("/config.properties").getPath())); 
		String docRoot = prop.getProperty("doc-root");
		if (docRoot == null || docRoot.trim().length() == 0) {
			DOC_ROOT = "";
			return null;
		}
		DOC_ROOT = docRoot;
		return DOC_ROOT;
	}
	
	
	public static MDDocument getDocument(String uri) throws MDFileNotExists, FileNotFoundException, IOException {
		String rootPath = MDReader.getDocRoot4Config();
		String file = null;
		if (rootPath == null) {
			URL url = MDReader.class.getResource(uri);
			if (url == null) {
				throw new MDFileNotExists();
			}
			file = url.getFile();
		} else {
			file = rootPath + uri;
		}
		MDDocument mdDocument = null;
		try {
			mdDocument = ReadFromFile.readFile(file);
			return mdDocument;
		} catch (IOException e) {
			e.printStackTrace();
			throw new MDFileNotExists();
		}
	}
	
	public static class MDDocument {
		private String fileName;
		private String body;
		private String title;
		public MDDocument() { }
		public MDDocument(String fileName, String body) {
			this.fileName = fileName;
			this.body = body;
			initTitle();
		}
		
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
			initTitle();
		}
		public String getTitle() {
			if (this.title == null) {
				return this.fileName.replaceAll("\\..*", "");
			}
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}

		public void initTitle() {
			if (this.body == null) {
				return;
			}
			String[] content = this.body.split("(\r\n)|(\n)");

			Pattern p = Pattern.compile("^\\s*\\<\\!\\-\\-\\s*title\\:\\s*.*\\s*\\-\\-\\>\\s*$", Pattern.CASE_INSENSITIVE); 
			for (String line : content) {
				if (line.trim().length() == 0) {
					continue;
				} else {
					if (p.matcher(line).find()) {
						this.title = line.replaceAll("^\\s*\\<\\!\\-\\-\\s*(?i)title\\:\\s*", "").replaceAll("\\s*\\-\\-\\>\\s*$", "");
						break;
					}
					break;
				}
			}
		}
	}
	
	/**
	 * 文件不存在
	 */
	public static class MDFileNotExists extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7701467538454493465L;
		
		public MDFileNotExists() { }
	}
}
