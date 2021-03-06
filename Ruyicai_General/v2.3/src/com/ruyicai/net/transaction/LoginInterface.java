package com.ruyicai.net.transaction;

import java.util.Random;

import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.URLEncoder;

public class LoginInterface {
	public static String error_code = "00";
	public static String sessionid;

	public static String userlogin(String username, String password) {
		try {
			String re = login(username, password);
			JSONObject obj;

			obj = new JSONObject(re);
			error_code = obj.getString("error_code");
			if (error_code.equals("0000")) {
				sessionid = obj.getString("sessionid");
				Log.e("tag", "sessionid="+sessionid);
			}

		} catch (Exception e) {
			
		}
		return error_code;

	}
	
	/**
	 * �ͻ��˵�¼����
	 * 
	 * @param mobile_code
	 * @param password
	 * @return
	 */
	public static String login(String mobile_code, String password) {
		String action = "user.do";
		String method = "login";
		String reValue = "";
		try {
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			paras.put("mobile_code", mobile_code);
			paras.put("password", password);
			para = URLEncoder.encode(paras.toString());

			reValue = InternetUtils.GetMethodOpenHttpConnect(Constants.SERVER_URL + action + "?method="
					+ method + "&jsonString=" + para);
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}
}
