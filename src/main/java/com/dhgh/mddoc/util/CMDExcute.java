package com.dhgh.mddoc.util;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class CMDExcute {
	public static String exec(String cmd) {
		try {
			String[] cmdA = { "/bin/sh", "-c", cmd };
			Process process = Runtime.getRuntime().exec(cmdA);
			LineNumberReader br = new LineNumberReader(new InputStreamReader(process.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
