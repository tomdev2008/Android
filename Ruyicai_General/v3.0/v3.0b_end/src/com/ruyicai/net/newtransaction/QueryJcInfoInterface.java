package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.Constants;
import com.ruyicai.util.ProtocolManager;

/**
 * 竞彩对阵查询
 * @author Administrator
 *
 */
public class QueryJcInfoInterface {
	private static String COMMAND = "QueryLot";
	private static  QueryJcInfoInterface instance = null;
	public final static String JCZQ = "1";
	public final static String JCLQ = "0";
	private  QueryJcInfoInterface(){
	}
	
	public synchronized static  QueryJcInfoInterface getInstance(){
		if(instance == null){
			instance = new  QueryJcInfoInterface();
		}
		return instance;
	}
/**
 * 查询参与合买的方法
 * jcType:1代表足球0代表篮球
 */
	public static String  queryJcInfo(String jcType){
		
		JSONObject jsonProtocol = ProtocolManager.getInstance().getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND,COMMAND);
			jsonProtocol.put(ProtocolManager.TYPE,"jcDuiZhen");
			jsonProtocol.put(ProtocolManager.JCTYPE,jcType);
			jsonProtocol.put(ProtocolManager.JCVALUETYPE,"1");
			return InternetUtils.GetMethodOpenHttpConnectSecurity(Constants.LOT_SERVER, jsonProtocol.toString());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return "";
	}
}
