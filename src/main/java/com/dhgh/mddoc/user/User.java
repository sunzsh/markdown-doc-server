package com.dhgh.mddoc.user;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import com.dhgh.mddoc.util.MDReader;
import com.dhgh.mddoc.util.ParseMD5;

public class User {
	public User() { }
	public User(String loginName,String loginPwd, String...auths) {
		this.loginName = loginName;
		this.loginPwd = loginPwd;
		this.auths = auths;
	}
	
	private String loginName;
	private String loginPwd;
	private String[] auths;
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPwd() {
		return loginPwd;
	}
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}
	public String[] getAuths() {
		return auths;
	}
	public void setAuths(String[] auths) {
		this.auths = auths;
	}

	public static final String SYS_USER_TOKEN = "*b3(#^.~kD}32V";
	public static final String SYS_PASSWORD_TOKEN = ">3Jo&-=A#>";
	
	public static Map<String, User> allUser = new HashMap<String, User>();
	public static void registerUser(String loginName,String loginPwd, String[]auths) {
		if (auths == null) {
			auths = new String[]{};
		}
		allUser.put(loginName, new User(loginName, ParseMD5.parseStrToMd5L32((ParseMD5.parseStrToMd5L32(loginPwd).toLowerCase()+SYS_PASSWORD_TOKEN)).toLowerCase(), auths));
	}
	
	public static boolean hasAuth(String requestProject, User user) {
		if (user == null) {
			return false;
		}
		if (user.getAuths().length == 0) {
			return false;
		}
		for (String au : user.getAuths()) {
			if (au.equals("*") || au.equals(requestProject)) {
				return true;
			}
		}
		return false;
	}
	static {
		allUser = new HashMap<>();
		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(MDReader.class.getResource("/users.properties").getPath()));
			Enumeration<String> names = (Enumeration<String>) prop.propertyNames();
			while(names.hasMoreElements()) {
				String name = names.nextElement();
				String valueStr = prop.getProperty(name);
				String[] valueArr = valueStr.split("\\:");
				String pwd = valueArr[0].trim();
				String[] auths = null;
				if (valueArr.length > 1) {
					auths = valueArr[1].split("\\s*,\\s*");
				}
				registerUser(name, pwd, auths);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
