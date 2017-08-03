package hansen.utils;

import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class ResourceUtil {
	private static   ResourceBundle bundle = ResourceBundle.getBundle("config");
	static{
		if (ToolUtil.isNotEmpty(bundle.getString("isDebug")) && bundle.getString("isDebug").equals("true")) {
			bundle=ResourceBundle.getBundle("config-test");
		}
	}

	public static String getRpcallowip(String type) {
		if (ToolUtil.isEmpty(type)) {
			return "";
		}
		if (type.equals("br")) {
			return bundle.getString("br_rpcallowip");
		}else if (type.equals("rtb")) {
			return bundle.getString("rtb_rpcallowip");
		}
		return  "";
	}

	public static String getAddnode(String type) {
		if (ToolUtil.isEmpty(type)) {
			return "";
		}
		if (type.equals("br")) {
			return bundle.getString("br_addnode");
		}else if (type.equals("rtb")) {
			return bundle.getString("rtb_addnode");
		}
		return "";
	}

	public static String getPort(String type) {
		if (ToolUtil.isEmpty(type)) {
			return "";
		}
		if (type.equals("br")) {
			return bundle.getString("br_port");
		}else if (type.equals("rtb")) {
			return bundle.getString("rtb_port");
		}
		return "0";
	}

	public static String getRpcport(String type) {
		if (ToolUtil.isEmpty(type)) {
			return "";
		}
		if (type.equals("br")) {
			return bundle.getString("br_rpcport");
		}else if (type.equals("rtb")) {
			return bundle.getString("rtb_rpcport");
		}
		return "0";
	}

	public static String getRpcpassword(String type) {
		if (ToolUtil.isEmpty(type)) {
			return "";
		}
		if (type.equals("br")) {
			return bundle.getString("br_rpcpassword");
		}else if (type.equals("rtb")) {
			return bundle.getString("rtb_rpcpassword");
		}
		return  "";
	}

	public static String getRpcuser(String type) {
		if (ToolUtil.isEmpty(type)) {
			return "";
		}
		if (type.equals("br")) {
			return bundle.getString("br_rpcuser");
		}else if (type.equals("rtb")) {
			return bundle.getString("rtb_rpcuser");
		}
		return  "";
	}

	public static String getDaemon() {
		return bundle.getString("daemon");
	}

	public static String getServer() {
		return bundle.getString("server");
	}

	public static String getListen() {
		return bundle.getString("listen");
	}

	public static String getIsDebug() {
		return bundle.getString("isDebug");
	}

	public static Boolean getIsEncrypt() {
		return new Boolean(bundle.getString("isEncrypt"));
	}
	public static Set<String> getRemoteIpSet() {
		Set<String> set =new HashSet<>();
		String remoteIp = bundle.getString("remoteIp");
		if (ToolUtil.isNotEmpty(remoteIp)) {
			String[] ipArr= remoteIp.split("\\,");
			for (String ip : ipArr) {
				set.add(ip);
			}
		}
		return set;
	}
	
	
}
