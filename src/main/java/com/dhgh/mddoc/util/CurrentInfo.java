package com.dhgh.mddoc.util;

import java.util.HashMap;

import com.dhgh.mddoc.user.User;

public class CurrentInfo {

	public static final String CURRENT_MEMUSER = "CURRENT_USER";
	
	private static ThreadLocal<HashMap<String, Object>> threadLocals = new ThreadLocal<HashMap<String, Object>>();

	public static User getCurrentUser() {
		Object user = getValue(CurrentInfo.CURRENT_MEMUSER);
		if (user != null) {
			return (User)user;
		}
		return null;
	}
		
	/**
	 * 取得线程变量值
	 * 
	 * @param name
	 *            线程变量名
	 * @return 线程变量值
	 */
	public static Object getValue(String name) {
		if(threadLocals.get()==null){
			threadLocals.set(new HashMap<String, Object>());
		}
		return threadLocals.get().get(name);
	}

	/**
	 * 设置线程变量值
	 * 
	 * @param name
	 *            线程变量名
	 * @param value
	 *            线程变量值
	 */
	public static void setValue(String name, Object value) {
		if (threadLocals.get() == null) {
			threadLocals.set(new HashMap<String, Object>());
		}
		threadLocals.get().put(name, value);
	}

	
	public static void clear(){
		if(threadLocals.get() != null){
			threadLocals.get().clear();
		}
	}
	
}
