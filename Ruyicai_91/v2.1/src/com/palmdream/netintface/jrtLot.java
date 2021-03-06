package com.palmdream.netintface;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.PublicConst;
import com.palmdream.RuyicaiAndroid.PublicMethod;
import com.palmdream.RuyicaiAndroid.R;

import com.palmdream.RuyicaiAndroid.UserLogin;
import com.palmdream.RuyicaiAndroid.ssqtest;

public class jrtLot {

	// private static String login_sessionId =null;

	// private static String baseUrl = "http://219.148.162.68/jrtLot/"; // 服务器

	// private static String baseUrl = "http://192.168.1.26:9999/jrtLot/";// 汤泉
	// private static String baseUrl = "http://192.168.1.142:8080/jrtLot/";//
	// 沈鹏兰
	// private static String baseUrl = "http://192.168.1.13:8080/jrtLot/";// 张富强
	// private static String baseUrl = "http://192.168.1.191:8080/jrtLot1.4/";//
	//
	// // 徐丽

	// private static String baseUrl = "http://219.148.162.70:81/jrtLot/";
	// //测试服务器
	// private static String baseUrl = "http://219.148.162.70:8001/jrtLot/";
	// //测试服务器
	// private static String baseUrl = "http://219.148.162.70/jrtLot/";
	// //开发测试服务器 新接口-20100702
	private static String baseUrl = "http://219.148.162.70:8000/jrtLot/";

	// //预上线服务器
	// private static String baseUrl = "http://192.168.0.12:8080/jrtLot/";

	// 富强
	// private static String privateKey="2010skj0630";
	// private static String iBaseUrl = "http://219.148.162.70:8000/jrtLot/";
	// private static String aBaseUrl = "http://219.148.162.70:8000/jrtTC/";
	// private static String aBaseUrl = "http://192.168.1.159:8888/jrtTC/";
	// http://124.207.135.19:8088/jrtLot/

	/**
	 * 124.207.135.19
	 * 
	 * 客户端追号请求
	 * 
	 * @param mobile_number
	 *            手机号
	 * @param count
	 *            追号期数
	 * @param bet_code
	 *            投注码
	 * @param amoun
	 *            投注金额
	 * @return 是否成功 0失败，1成功
	 */
	/**
	 * 客户端投注追号请求
	 * 
	 * @param bet_code
	 *            投注码
	 * @param count
	 *            追号期数
	 * @param amount
	 *            投注金额
	 * @param type
	 *            当期是否投注 Y表示当期就投注。即投注+追号。N表示当期不投注，即只执行追号定制。
	 * @param sessionid
	 * @return
	 */
	public static String addNumber(String bet_code, String count,
			String amount, String type, String sessionid) {
		String action = "addNumAttemper.do";
		String method = "addNumber";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			paras.put("bet_code", bet_code);
			paras.put("amount", amount);
			paras.put("Type", type);
			paras.put("count", count);
			para = URLEncoder.encode(paras.toString());
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	// public static String addNumber(String mobile_number,String
	// bet_code,String amount ,String count ,String sessionid){
	// String action = "touzhu.do";
	// String method = "addNumber";
	// String reValue = "";
	// iHttp http = null;
	// try {
	// http = new iHttp();
	// Random rdm = new Random();
	// int transctionId = rdm.nextInt();
	// String para = "";
	// JSONObject paras = new JSONObject();
	// paras.put("inputCharset", 2);
	// paras.put("version", "v2.0");
	// paras.put("language", 1);
	// paras.put("transctionid", transctionId);
	// paras.put("mobile_number", mobile_number);
	// paras.put("bet_code", bet_code);
	// paras.put("amount", amount);
	// paras.put("count", count);
	// para = URLEncoder.encode(paras.toString());
	// String re =
	// http.getViaHttpConnection(baseUrl+action+";jsessionid="+sessionid+"?method="+method+"&jsonString="+para);
	// if(re!=null && re.length()>0){
	// reValue =re;
	// // JSONObject obj = new JSONObject(re);
	// // PublicMethod.myOutput("-----------------------");
	// // PublicMethod.myOutput("error_code="+obj.getString("error_code"));
	// // if(obj.getString("error_code").equals("0000")){
	// // PublicMethod.myOutput(reValue);
	// // }
	// }
	// } catch (Exception e) {
	// PublicMethod.myOutput(e.getMessage());
	// }
	//		
	// return reValue;
	// }

	/**
	 * 获取最新版程序的版本号和下载url
	 * 
	 * @return
	 */
	public static String getLastVersion() {
		String reValue = "";
		try {
			String action = "user.do";
			String method = "getlastversion";
			iHttp http = null;

			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";ConnectionTimeout=10000?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return reValue;
	}

	/**
	 * 获取最新版程序的版本号和下载url
	 * 
	 * @return
	 */
	public static String clientEditionSelect(String mobleType) {
		String reValue = "";
		try {
			String action = "touzhu.do";
			String method = "clientEditionSelect";
			iHttp http = null;

			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			paras.put("mobleType", mobleType);
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";ConnectionTimeout=10000?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
		}
		return reValue;
	}

	/**
	 * 
	 * 客户端即时信息请求
	 * 
	 * @param mobile_number
	 *            手机号
	 * @param count
	 *            追号期数
	 * @param bet_code
	 *            投注码
	 * @param amount
	 *            投注金额
	 * @return 是否成功 0失败，1成功
	 */
	public static String tnotice() {
		String action = "user.do";
		String method = "tnotice";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action + "?method="
					+ method + "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = URLEncoder.decode(re);
				// JSONObject obj = new JSONObject(re);
				// PublicMethod.myOutput("--------news--------------");
				// PublicMethod.myOutput("error_code="+obj.getString("error_code"));
				// if(obj.getString("error_code").equals("0000")){
				// PublicMethod.myOutput(reValue);
				// }
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 客户端追号套餐查询
	 * 
	 * @param mobile_number
	 * @param sessionID
	 * @return
	 */
	public static String addSelect(String mobile_number, String sessionid) {
		String action = "touzhu.do";
		String method = "addSelect";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			paras.put("mobile_code", mobile_number);
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONObject obj = new JSONObject(re);

				// lee
				// JSONArray jsonObject3 = new JSONArray(re);

				// jsonObject3=obj.getJSONArray("jsonObject3");
				// PublicMethod.myOutput("======================="+jsonObject3.length());
				// for(int i=0;i<jsonObject3.length();i++){
				// JSONObject jsonObject2 = jsonObject3.getJSONObject(i);
				// String lotNo = jsonObject2.getString("lotNo");
				// PublicMethod.myOutput("lotNo="+lotNo);
				// PublicMethod.myOutput("batchNum="+jsonObject2.getString("batchNum"));
				// PublicMethod.myOutput("lastNum="+jsonObject2.getString("lastNum"));
				// PublicMethod.myOutput("beginBatch="+jsonObject2.getString("beginBatch"));
				// PublicMethod.myOutput("lastBatch="+jsonObject2.getString("lastBatch"));
				// PublicMethod.myOutput("betNum="+jsonObject2.getString("betNum"));
				// PublicMethod.myOutput("amount="+jsonObject2.getString("amount"));
				// }

			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 客户端账户明细查询
	 * 
	 * @param mobile_number
	 * @param sessionID
	 * @return
	 */
	public static String accountDetailSelect(String mobile_code,
			String startdate, String enddate, String type, String sessionid) {
		String action = "touzhu.do";
		String method = "accountDetailSelect";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			paras.put("mobile_code", mobile_code);
			paras.put("startdate", startdate);
			paras.put("enddate", enddate);
			paras.put("type", type);
			para = URLEncoder.encode(paras.toString());

			PublicMethod.myOutput("-------------------url=" + baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = URLEncoder.decode(re);
				// JSONObject obj = new JSONObject(re);
				// JSONArray jsonObject3 = new JSONArray(re);
				// jsonObject3=obj.getJSONArray("jsonObject3");
				// PublicMethod.myOutput("======================="+jsonObject3.length());
				// for(int i=0;i<jsonObject3.length();i++){
				// JSONObject jsonObject2 = jsonObject3.getJSONObject(i);
				// String lotNo = jsonObject2.getString("lotNo");
				// PublicMethod.myOutput("lotNo="+lotNo);
				// PublicMethod.myOutput("batchNum="+jsonObject2.getString("batchNum"));
				// PublicMethod.myOutput("lastNum="+jsonObject2.getString("lastNum"));
				// PublicMethod.myOutput("beginBatch="+jsonObject2.getString("beginBatch"));
				// PublicMethod.myOutput("lastBatch="+jsonObject2.getString("lastBatch"));
				// PublicMethod.myOutput("betNum="+jsonObject2.getString("betNum"));
				// PublicMethod.myOutput("amount="+jsonObject2.getString("amount"));
				// }

			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 客户端投注请求
	 * 
	 * @param mobile_code
	 * @param bet_code
	 *            投注码
	 * @param amount
	 *            购彩金额
	 * @param sessionid
	 * @return
	 */
	public static String betting(String mobile_code, String bet_code,
			String amount, String sessionid) {
		String action = "touzhu.do";
		String method = "betting";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			paras.put("mobile_code", mobile_code);
			paras.put("bet_code", bet_code);
			paras.put("amount", amount);
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONObject obj = new JSONObject(re);
				// PublicMethod.myOutput("----------------");
				// PublicMethod.myOutput(obj.getString("error_code"));
				// PublicMethod.myOutput("===================");
				// if(obj.getString("error_code").equals("0000")){
				// PublicMethod.myOutput("amount="+obj.getString("amount"));
				// PublicMethod.myOutput("term_begin_datetime="+obj.getString("term_begin_datetime"));
				// PublicMethod.myOutput("deposit_sum="+obj.getString("deposit_sum"));
				// PublicMethod.myOutput("sell_term_code="+obj.getString("sell_term_code"));
				// PublicMethod.myOutput("valid_term_code="+obj.getString("valid_term_code"));
				// PublicMethod.myOutput("run_code="+obj.getString("run_code"));
				// PublicMethod.myOutput("check_code="+obj.getString("check_code"));
				// PublicMethod.myOutput();
				// // winingSelect("13866666671", obj.getString("check_code"),
				// login_sessionId);
				// PublicMethod.myOutput("----------------------------------=");
				// }
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 客户端购彩查询请求
	 * 
	 * @param mobile_code
	 * @param startDate
	 * @param endDate
	 * @param sessionID
	 * @return
	 */
	public static String bettingSelect(String sessionid) {// String mobile_code,
		// String action = "touzhu.do";
		// String method = "bettingSelect";
		String action = "betQuery.do";
		String method = "betquery";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			// paras.put("mobile_code", mobile_code);
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = URLEncoder.decode(re);
				// JSONArray jsonObject3 = new JSONArray(re);
				// PublicMethod.myOutput("======================="+jsonObject3.length());
				// for(int i=0;i<jsonObject3.length();i++){
				// JSONObject jsonObject2 = jsonObject3.getJSONObject(i);
				// PublicMethod.myOutput("error_code ="+jsonObject2.getString("error_code"));
				// PublicMethod.myOutput("play_name="+jsonObject2.getString("play_name"));
				// PublicMethod.myOutput("valid_term_code="+jsonObject2.getString("valid_term_code"));
				// PublicMethod.myOutput("sell_run_code="+jsonObject2.getString("sell_run_code"));
				// PublicMethod.myOutput("sell_datetime="+jsonObject2.getString("sell_datetime"));
				// //
				// PublicMethod.myOutput("prize_info="+jsonObject2.getString("prize_info"));
				// PublicMethod.myOutput("win_big_flag="+jsonObject2.getString("win_big_flag"));
				// PublicMethod.myOutput("abandon_date_time="+jsonObject2.getString("abandon_date_time"));
				// PublicMethod.myOutput("encash_flag="+jsonObject2.getString("encash_flag"));
				// PublicMethod.myOutput("encash_term="+jsonObject2.getString("encash_term"));
				// PublicMethod.myOutput("cash_date_time="+jsonObject2.getString("cash_date_time"));
				// PublicMethod.myOutput("-------------------------------------------------");
				// }
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 客户端提现请求
	 * 
	 * @param mobile_number
	 * @param amount
	 * @param sessionid
	 * @return
	 */
	public static String cash(String mobile_code, String withdrawel_money,
			String bank_name, String ID_number, String sessionid) {
		String action = "user.do";
		String method = "cash";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			paras.put("mobile_code", mobile_code);
			paras.put("withdrawel_money", withdrawel_money);
			paras.put("bank_name", bank_name);
			paras.put("ID_number", ID_number);
			para = URLEncoder.encode(paras.toString());
			PublicMethod.myOutput(baseUrl + action + ";jsessionid=" + sessionid
					+ "?method=" + method + "&jsonString=" + para);
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONObject obj = new JSONObject(re);
				// PublicMethod.myOutput("______________________________");
				// PublicMethod.myOutput("error_code="+obj.getString("error_code"));
				// if(obj.getString("error_code").equals("0000")){
				//					
				// }
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 客户端退出请求
	 * 
	 * @param mobile_code
	 * @param sessionID
	 * @return
	 */
	public static String exit(String mobile_code, String sessionid) {
		String action = "user.do";
		String method = "exit";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			paras.put("mobile_code", mobile_code);
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONObject obj = new JSONObject(re);
				// PublicMethod.myOutput("______________________________");
				// PublicMethod.myOutput("error_code_exit="+obj.getString("error_code"));
				// if(obj.getString("error_code").equals("0000")){
				// // reValue = obj.getString("error_code");
				// }
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 客户端赠送彩票请求
	 * 
	 * @param from_mobile_code
	 *            客户端手机号
	 * @param to_mobile_code
	 *            受赠方手机号
	 * @param bet_code
	 *            投注码
	 * @param amount
	 *            购彩金额
	 * @param sessionID
	 * @return
	 */
	public static String gift(String from_mobile_code, String to_mobile_code,
			String bet_code, String amount, String sessionid) {
		// String action = "touzhu.do";
		String action = "giftAttemper.do";
		String method = "gift";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			paras.put("from_mobile_code", from_mobile_code);
			paras.put("to_mobile_code", to_mobile_code);
			paras.put("bet_code", bet_code);
			paras.put("amount", amount);
			// paras.put("userno", userno);
			Log.e("para===", paras.toString());
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONObject obj = new JSONObject(re);
				// PublicMethod.myOutput("error_code="+obj.getString("error_code"));
				// if(obj.getString("error_code").equals("0000")){
				//					
				// PublicMethod.myOutput("term_begin_datetime="+obj.getString("term_begin_datetime"));
				// PublicMethod.myOutput("amount="+obj.getString("amount"));
				// PublicMethod.myOutput("deposit_sum="+obj.getString("deposit_sum"));
				// PublicMethod.myOutput("sell_term_code="+obj.getString("sell_term_code"));
				// PublicMethod.myOutput("valid_term_code="+obj.getString("valid_term_code"));
				// PublicMethod.myOutput("run_code="+obj.getString("run_code"));
				// PublicMethod.myOutput("check_code="+obj.getString("check_code"));
				// PublicMethod.myOutput("to_mobile_code="+obj.getString("to_mobile_code"));
				//					
				// PublicMethod.myOutput("----------");
				// }
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 客户端登录请求
	 * 
	 * @param mobile_code
	 * @param password
	 * @return
	 */
	public static String login(String mobile_code, String password) {
		String action = "user.do";
		String method = "login";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
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

			String re = http.getViaHttpConnection(baseUrl + action + "?method="
					+ method + "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONObject obj = new JSONObject(re);
				// login_sessionId = obj.getString("sessionid");
				// PublicMethod.myOutput("login_sessionid="+login_sessionId);
				// PublicMethod.myOutput(obj.getString("error_code")+"=error_code");
				// if(obj.getString("error_code").equals("0000")){
				// reValue = obj.getString("sessionid");

				// PublicMethod.myOutput(reValue);
				// addNumber("13866666660","01-F47101-002-50-01020304050619","10","3",reValue);
				// exit("13866666666",reValue);
				// findUserBalance("13866666671",reValue);
				// changePassword("13866666666", "123456", "654321", reValue);
				// gift("13866666671","13411111112","01-F47101-001-10-01020304050602","20",reValue);
				// betting("13866666671","01-F47101-001-50-01020304050612","100",reValue);
				// cash("15810709603","20","工行","111",login_sessionId);
				// addSelect("13866666660",reValue);
				// packageDeal("13866666666", "F47101", "50", "1",reValue);
				// packageUpdate("13866666666", "F47102", "30", reValue);
				// bettingSelect("13411111111", "20081001", "20091203",
				// reValue);
				// lotterySelect("F47101","12341");
				// winingSelect("13866666671", "123456", login_sessionId);
				// giftSelect("13866666671", reValue);
				// giftedSelect("13411111112", reValue);
				// addRequest(reValue);
				//

				// cardCharge("13866666671","0010020000000002", "901773",
				// reValue);
				// yeePayCardCharge("13866666671","0010020000000002",
				// "901773","50", reValue);
				// bankCharge("15810709603","120",login_sessionId);
				// }
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 推荐好友
	 * 
	 * @param mobile_code
	 * @param friend_number
	 *            好友手机号
	 * @return
	 */
	public static String recommendFriend(String mobile_code,
			String friend_number, String sessionid) {

		// lee
		// String action = "user.do";
		// String method = "recommendFriend";

		String reValue = "";

		return reValue;
	}

	/**
	 * 用户注册
	 * 
	 * @param user_name
	 *            用户名（手机号码）
	 * @param password
	 *            密码
	 * @param id
	 *            身份证号默认18个1
	 * @return
	 */
	public static String channel_id = "C10144";// 掌上应用汇
	public static String channel = "279";// 掌上应用汇

	public static String register(String mobile_code, String user_password,
			String id) {
		String action = "user.do";
		String method = "register";
		// 运营系统
		// String channel_id = "C00001";
		// 网秦渠道号
		// String channel_id = "C10002";
		// 另一个渠道
		// String channel_id = "C10004";
		// String channel_id = "C10005";
		// String channel_id = "C10006";
		// String channel_id = "C10051";
		// 91android 渠道号
		// String channel_id="C10074";
		// 安卓市场 渠道号
		// String channel_id="C10071";
		// String channel_id="C10081";//机趣网
		// String channel_id="C10117";//宇龙酷派
		// String channel_id="C10119";//tompda-andriod
		// String channel_id="C10043";//掌软A
		// String channel_id = "C10144";// 掌上应用汇
		// String channel_id="010102 ";//当乐wap

		// String channel ="100" ;//开伟的channel
		// String channel ="155" ;//91android
		// String channel ="151" ; //安卓市场
		// String channel ="166" ;//机趣网
		// String channel ="227" ;//宇龙酷派
		// String channel ="232" ;//tompda-andriod
		// String channel ="278" ;//掌软A
		// String channel = "279";// 掌上应用汇
		// String channel ="247" ;//当乐wap

		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);

			paras.put("channel_id", channel_id);
			paras.put("channel", channel);
			paras.put("transctionid", transctionId);
			paras.put("mobile_code", mobile_code);
			paras.put("user_password", user_password);
			paras.put("Id", id);// 身份证号，默认18个1
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action + "?method="
					+ method + "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONObject obj = new JSONObject(re);
				// PublicMethod.myOutput("Error_code1="+obj.getString("error_code"));
				// if(obj.getString("error_code").equals("0000")){
				// // reValue = obj.getString("error_code");
				// }
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;

	}

	/**
	 * 客户端修改密码
	 * 
	 * @param mobile_code
	 * @param oldPassword
	 * @param newPassword
	 * @param sessionID
	 * @return
	 */
	public static String changePassword(String mobile_number, String old_Pwd,
			String new_Pwd, String sessionid) {
		String action = "user.do";
		String method = "changePassword";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			paras.put("mobile_number", mobile_number);
			paras.put("old_Pwd", old_Pwd);
			paras.put("new_Pwd", new_Pwd);
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONObject obj = new JSONObject(re);
				// PublicMethod.myOutput(obj.getString("error_code")+"=error_code");
				// if(obj.getString("error_code").equals("0000")){
				// }
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 客户端余额查询
	 * 
	 * @param mobile_code
	 * @param sessionID
	 * @return
	 */
	public static String findUserBalance(String mobile_code, String sessionid) {
		String action = "user.do";
		String method = "findUserBalance";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			paras.put("mobile_code", mobile_code);
			para = URLEncoder.encode(paras.toString());

			// ghfhgjhggjhkh111shkk222bvnb333

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONObject obj = new JSONObject(re);
				// PublicMethod.myOutput(obj.getString("error_code")+"=error_code");
				// if(obj.getString("error_code").equals("0000")){
				// PublicMethod.myOutput("deposit_amount="+obj.getString("deposit_amount"));
				// PublicMethod.myOutput("Valid_amount="+obj.getString("Valid_amount"));
				// }
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}
		return reValue;
	}

	/**
	 * 客户端中奖查询
	 * 
	 * @param mobile_code
	 * @param check
	 *            校验码
	 * @param sessionID
	 * @return
	 */
	public static String winingSelect(String mobile_code, String sessionid) {
		String action = "touzhu.do";
		String method = "winingSelect";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			paras.put("mobile_code", mobile_code);
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONObject obj = new JSONObject(re);

				// JSONArray jsonObject3 = new JSONArray(re);
				// PublicMethod.myOutput("======================="+jsonObject3.length());
				// for(int i=0;i<jsonObject3.length();i++){
				// JSONObject jsonObject2 = jsonObject3.getJSONObject(i);
				// PublicMethod.myOutput("error_code ="+jsonObject2.getString("error_code"));
				// //
				// PublicMethod.myOutput("check_code="+jsonObject2.getString("check_code"));
				// PublicMethod.myOutput("play_name="+jsonObject2.getString("play_name"));
				// PublicMethod.myOutput("district_code="+jsonObject2.getString("district_code"));
				// PublicMethod.myOutput("sell_datetime="+jsonObject2.getString("sell_datetime"));
				// PublicMethod.myOutput("sell_run_code="+jsonObject2.getString("sell_run_code"));
				// PublicMethod.myOutput("valid_term_code="+jsonObject2.getString("valid_term_code"));
				// PublicMethod.myOutput("abandon_date_time="+jsonObject2.getString("abandon_date_time"));
				// PublicMethod.myOutput("encash_flag="+jsonObject2.getString("encash_flag"));
				// PublicMethod.myOutput("encash_term="+jsonObject2.getString("encash_term"));
				// PublicMethod.myOutput("cash_date_time="+jsonObject2.getString("cash_date_time"));
				// PublicMethod.myOutput("-------------------------------------------------");
				// }
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 客户端开奖查询
	 * 
	 * @param play_name
	 *            彩票种类
	 * @param term_code
	 *            彩票期号
	 * @return
	 */
	public static String lotterySelect(String play_name) {
		// String action = "touzhu.do";
		// String action="allSelect.do";
		// 新接口 陈晨 20100716
		String action = "twininfoQuery.do";
		String method = "lotterySelect";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			// paras.put("mobile_code", "mobile_code");
			paras.put("play_name", play_name);
			para = URLEncoder.encode(paras.toString());

			// String where="";
			// JSONObject jwhere=new JSONObject();
			// jwhere.put("batchCode", "");
			// jwhere.put("lotNo", "");
			// jwhere.put("startDate", "");
			// jwhere.put("stopDate", "");
			// jwhere.put("startLine", "1");
			// jwhere.put("stopLine", "4");
			// where=URLEncoder.encode(jwhere.toString());
			// PublicMethod.myOutput("-------------------url"+baseUrl+action+";jsessionid="+""+"?method="+method+"&jsonString="+para+"&jsonWhere="+where);
			// String re =
			// http.getViaHttpConnection(baseUrl+action+";jsessionid="+""+"?method="+method+"&jsonString="+para+"&jsonWhere="+where);

			String re = http.getViaHttpConnection(baseUrl + action + "?method="
					+ method + "&jsonString=" + para);
			PublicMethod.myOutput("===============" + re);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONObject obj = new JSONObject(re);
				// PublicMethod.myOutput(obj.getString("error_code")+"=error_code");
				// if(obj.getString("error_code").equals("0000")){
				// reValue = obj.getString("play_name");
				// PublicMethod.myOutput("play_name="+reValue);
				// PublicMethod.myOutput("term_code="+obj.getString("term_code"));
				// PublicMethod.myOutput("win_base_code="+obj.getString("win_base_code"));
				// PublicMethod.myOutput("act_sell_amount="+obj.getString("act_sell_amount"));
				// PublicMethod.myOutput("valid_sell_amount="+obj.getString("valid_sell_amount"));
				// }
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}
		return reValue;
	}

	/**
	 * 客户端套餐请求//已测试
	 * 
	 * @param mobile_number
	 * @param sessionID
	 * @param lottry_type
	 *            套餐类别 (菜种)
	 * @param amount
	 *            每次套餐金额 以分为单位
	 * @param agency_code
	 *            渠道号
	 * @return
	 */
	public static String packageDeal(String lottery_type, String amount,
			String sessionid) {
		// String action = "newPackage.do";
		String action = "packDealAttemper.do";
		String method = "packageDeal";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();

			// 通信准备
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);

			// paras.put("mobile_code", mobile_number);
			paras.put("lottery_type", lottery_type);
			paras.put("amount", amount);
			paras.put("agency_code", "");
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			Log.e("re====", "" + re);
			PublicMethod.myOutput(re);
			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 客户端套餐查询请求//已测试
	 * 
	 * @param mobile_number
	 * @param sessionID
	 * @return
	 */
	public static String packageSelect(String sessionid) {
		// String action = "newPackage.do";
		String action = "packDealQuery.do";
		String method = "packageSelect";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			// paras.put("mobile_code", mobile_code);
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = URLEncoder.decode(re);
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 客户端套餐修改请求//已测试
	 * 
	 * @param mobile_number
	 * @param lottry_type
	 *            彩票种类
	 * @param amount
	 *            套餐金额
	 * @param sessionid
	 * @return
	 */
	public static String packageUpdate(String tsubscribeId, String amount,
			String sessionid) {
		String action = "packDealUpdate.do";
		String method = "packageUpdate";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			paras.put("tsubscribeId", tsubscribeId);
			// paras.put("mobile_number", mobile_number);
			// paras.put("lottery_type", lottery_type);
			paras.put("amount", amount);
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONObject obj = new JSONObject(re);
				// PublicMethod.myOutput(obj.getString("error_code")+"=error_code");
				// if(obj.getString("error_code").equals("0000")){
				//
				// }
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}
		return reValue;
	}

	/**
	 * 客户端套餐冻结请求//已测试
	 * 
	 * @param mobile_number
	 * @param sessionid
	 * @return
	 */
	public static String packageFreeze(String sessionid, String tsubscribeId) {
		// String action = "touzhu.do";
		// String action = "newPackage.do";
		String action = "packDealStop.do";
		String method = "packageFreeze";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			// paras.put("mobile_code", mobile_code);
			paras.put("tsubscribeId", tsubscribeId);
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONObject obj = new JSONObject(re);
				// PublicMethod.myOutput(obj.getString("error_code")+"=error_code");
				// if(obj.getString("error_code").equals("0000")){
				//
				// }
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}
		return reValue;
	}

	/**
	 * 客户端赠送彩票查询请求//已测试
	 * 
	 * @param mobile_code
	 * @param
	 * @param
	 * @param sessionID
	 * @return
	 */
	public static String giftSelect(String sessionid) {
		String action = "giftQuery.do";
		String method = "giftquery";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			// paras.put("mobile_code", mobile_code);
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONArray jsonObject3 = new JSONArray(re);
				// PublicMethod.myOutput("======================="+jsonObject3.length());
				// for(int i=0;i<jsonObject3.length();i++){
				// JSONObject jsonObject2 = jsonObject3.getJSONObject(i);
				// PublicMethod.myOutput("error_code ="+jsonObject2.getString("error_code"));
				// PublicMethod.myOutput("to_mobile_code="+jsonObject2.getString("to_mobile_code"));
				// PublicMethod.myOutput("term_begin_datetime="+jsonObject2.getString("term_begin_datetime"));
				// PublicMethod.myOutput("amount="+jsonObject2.getString("amount"));
				// PublicMethod.myOutput("sell_term_code="+jsonObject2.getString("sell_term_code"));
				// PublicMethod.myOutput("valid_term_code="+jsonObject2.getString("valid_term_code"));
				// PublicMethod.myOutput("run_code="+jsonObject2.getString("run_code"));
				// //
				// PublicMethod.myOutput("check_code="+jsonObject2.getString("check_code"));
				// PublicMethod.myOutput("bet_code="+jsonObject2.getString("bet_code"));
				// PublicMethod.myOutput("-------------------------------------------------");
				// }
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 客户端被赠送彩票查询请求//已测试
	 * 
	 * @param mobile_code
	 * @param
	 * @param
	 * @param sessionID
	 * @return
	 */
	public static String giftedSelect(String sessionid) {
		String action = "giftAcceptQuery.do";
		String method = "giftedquery";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			// paras.put("mobile_code", mobile_code);
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONArray jsonObject3 = new JSONArray(re);
				// PublicMethod.myOutput("======================="+jsonObject3.length());
				// for(int i=0;i<jsonObject3.length();i++){
				// JSONObject jsonObject2 = jsonObject3.getJSONObject(i);
				// PublicMethod.myOutput("error_code ="+jsonObject2.getString("error_code"));
				// PublicMethod.myOutput("from_mobile_code="+jsonObject2.getString("from_mobile_code"));
				// PublicMethod.myOutput("term_begin_datetime="+jsonObject2.getString("term_begin_datetime"));
				// PublicMethod.myOutput("amount="+jsonObject2.getString("amount"));
				// PublicMethod.myOutput("sell_term_code="+jsonObject2.getString("sell_term_code"));
				// PublicMethod.myOutput("valid_term_code="+jsonObject2.getString("valid_term_code"));
				// PublicMethod.myOutput("run_code="+jsonObject2.getString("run_code"));
				// //
				// PublicMethod.myOutput("check_code="+jsonObject2.getString("check_code"));
				// PublicMethod.myOutput("bet_code="+jsonObject2.getString("bet_code"));
				// PublicMethod.myOutput("-------------------------------------------------");
				// }
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 客户端可选支付方式选择请求
	 * 
	 * @param
	 * @param sessionid
	 * @param
	 * @param
	 * @param
	 */
	public static String addRequest(String sessionid) {
		String action = "charge.do";
		String method = "addRequest";
		String reValue = "";
		iHttp http = null;
		String para = "";
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			para = URLEncoder.encode(paras.toString());
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONObject obj = new JSONObject(re);
				// PublicMethod.myOutput("error_code="+obj.getString("error_code"));
				// if(obj.getString("error_code").equals("0000"))
				// {
				// PublicMethod.myOutput("addMoneyByBank="+obj.getString("addMoneyByBank"));
				// PublicMethod.myOutput("addMoneyByPoint="+obj.getString("addMoneyByPoint"));
				// PublicMethod.myOutput("addMoneyByCard="+obj.getString("addMoneyByCard"));
				// }
			}
		} catch (Exception e) {
		}
		return reValue;
	}

	/**
	 * 客户端非银行卡充值请求
	 * 
	 * @param mobile_code
	 * @param cardNo
	 * @param cardPassword
	 * @param sessionid
	 * @param
	 */
	public static String cardCharge(String mobile_code, String cardNo,
			String cardPassword, String sessionid) {
		String action = "charge.do";
		String method = "cardCharge";
		String reValue = "";
		iHttp http = null;
		String para = "";

		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionId", transctionId);
			paras.put("mobile_code", mobile_code);
			paras.put("cardNo", cardNo);
			paras.put("cardPassword", cardPassword);

			para = URLEncoder.encode(paras.toString());
			// PublicMethod.myOutput("-=-=-=-=-=-=-=-=-=-=-=-=-");
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONObject obj = new JSONObject(re);
				// PublicMethod.myOutput("error_code="+obj.getString("error_code"));
				// if(obj.getString("error_code").equals("0"))
				// {
				// PublicMethod.myOutput("transaction_money="+obj.getString("transaction_money"));
				// PublicMethod.myOutput("deposit_amount="+obj.getString("deposit_amount"));
				// PublicMethod.myOutput("valid_amount="+obj.getString("valid_amount"));
				// }
			}
		} catch (Exception e) {
		}
		return reValue;
	}

	/**
	 * 客户端银行卡充值请求 wap浏览器金软通
	 * 
	 * @param mobile_code
	 * @param recharge_way
	 *            充值方式
	 * @param transaction_money
	 *            充值金额
	 * @param bank_id
	 *            银行标识
	 * @param
	 */
	public static String bankCharge(String mobile_code, String bankType,
			String transaction_money, String sessionid) {
		String action = "charge.do";
		String method = "bankCharge";
		String reValue = "";
		iHttp http = null;
		String para = "";

		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionId", transctionId);
			paras.put("mobile_code", mobile_code);
			paras.put("transaction_money", transaction_money);
			paras.put("pd_FrpId", bankType);
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = URLEncoder.decode(re);
				PublicMethod.myOutput(re);
				// JSONObject obj = new JSONObject(re);
				// PublicMethod.myOutput("error_code="+obj.getString("error_code"));
				// if(obj.getString("error_code").equals("0000"))
				// {
				// PublicMethod.myOutput("p0_Cmd="+obj.getString("p0_Cmd"));
				// PublicMethod.myOutput("p1_MerId="+obj.getString("p1_MerId"));
				// PublicMethod.myOutput("p2_Order="+obj.getString("p2_Order"));
				// PublicMethod.myOutput("p3_Amt="+obj.getString("p3_Amt"));
				// PublicMethod.myOutput("p4_Cur="+obj.getString("p4_Cur"));
				// PublicMethod.myOutput("p5_Pid="+obj.getString("p5_Pid"));
				// PublicMethod.myOutput("p6_Pcat="+obj.getString("p6_Pcat"));
				// PublicMethod.myOutput("p7_Pdesc="+obj.getString("p7_Pdesc"));
				// PublicMethod.myOutput("p8_Url="+obj.getString("p8_Url"));
				// PublicMethod.myOutput("p9_SAF="+obj.getString("p9_SAF"));
				// PublicMethod.myOutput("pa_MP="+obj.getString("pa_MP"));
				// PublicMethod.myOutput("pd_FrpId="+obj.getString("pd_FrpId"));
				// PublicMethod.myOutput("pr_NeedResponse="+obj.getString("pr_NeedResponse"));
				// PublicMethod.myOutput("hmac="+obj.getString("hmac"));
				// PublicMethod.myOutput("nodeAuthorizationURL="+obj.getString("nodeAuthorizationURL"));
				// PublicMethod.myOutput("--------------============----------");
				// String amp
				// =obj.getString("nodeAuthorizationURL")+"?p0_Cmd="+obj.getString("p0_Cmd")
				// +"&p1_MerId="+obj.getString("p1_MerId")+"&p2_Order="+obj.getString("p2_Order")
				// +"&p3_Amt="+obj.getString("p3_Amt")+"&p4_Cur="+obj.getString("p4_Cur")
				// +"&p5_Pid="+obj.getString("p5_Pid")+"&p6_Pcat="+obj.getString("p6_Pcat")
				// +"&p7_Pdesc="+obj.getString("p7_Pdesc")+"&p8_Url="+obj.getString("p8_Url")
				// +"&p9_SAF="+obj.getString("p9_SAF")+"&pa_MP="+obj.getString("pa_MP")
				// +"&pd_FrpId="+obj.getString("pd_FrpId")+"&pr_NeedResponse="+obj.getString("pr_NeedResponse")
				// +"&hmac="+obj.getString("hmac");
				//					
				// PublicMethod.myOutput(amp);

				// }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reValue;
	}

	/**
	 * 客户端epay卡充值请求
	 * 
	 * @param mobile_code
	 * @param cardNo
	 * @param cardPassword
	 * @param amount
	 * @param
	 */
	public static String yeePayCardCharge(String mobile_code, String cardNo,
			String cardPassword, String amount, String totalAmount,
			String pd_FrpId, String sessionid) {
		String action = "charge.do";
		String method = "yeePayCardCharge";
		String reValue = "";
		iHttp http = null;
		String para = "";

		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionId", transctionId);
			paras.put("mobile_code", mobile_code);
			paras.put("cardNo", cardNo);
			paras.put("cardPassword", cardPassword);
			paras.put("amount", amount);
			paras.put("totalAmount", totalAmount);
			paras.put("pd_FrpId", pd_FrpId);

			para = URLEncoder.encode(paras.toString());
			PublicMethod.myOutput("-=-=-=-=-=-=-=-=-=-=-=-=-");
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONObject obj = new JSONObject(re);
				// PublicMethod.myOutput("error_code="+obj.getString("error_code"));
				// if(obj.getString("error_code").equals("0000"))
				// {
				// PublicMethod.myOutput("++++++++++++++++-------------");
				// PublicMethod.myOutput("orderId="+obj.getString("orderId"));
				// PublicMethod.myOutput("amt="+obj.getString("amt"));
				// PublicMethod.myOutput("cardNo="+obj.getString("cardNo"));
				// PublicMethod.myOutput("userNo="+obj.getString("userNo"));
				// PublicMethod.myOutput("+++++++++++++++++++++++++++++++");
				// }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reValue;
	}

	/**
	 * 客户端专家分析请求//已测试
	 * 
	 * @param
	 * @param
	 * @param
	 * @param
	 * @param
	 */
	public static String analysis(String sessionid) {
		String action = "user.do";
		String method = "analysis";
		String reValue = "";
		iHttp http = null;
		String para = "";
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			para = URLEncoder.encode(paras.toString());
			String re = http.getViaHttpConnection(baseUrl + action + "?method="
					+ method + "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = URLEncoder.decode(re);
			}
		} catch (Exception e) {
		}
		return reValue;
	}

	/**
	 * 客户端当前期号请求
	 * 
	 * @param
	 * @param
	 * @param
	 * @param
	 * @param
	 */
	public static String getLotNo(String lotNo) {
		String action = "touzhu.do";
		String method = "getLotNo";
		String reValue = "";
		iHttp http = null;
		String para = "";
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			paras.put("lotNo", lotNo);
			para = URLEncoder.encode(paras.toString());
			PublicMethod.myOutput("url=" + baseUrl + action + "?method="
					+ method + "&jsonString=" + para);
			String re = http.getViaHttpConnection(baseUrl + action + "?method="
					+ method + "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
		}
		return reValue;
	}

	/**
	 * 非跳转类账户充值
	 * 
	 * @param accesstype
	 *            接入方式 B表示web，W表示wap，C表示客户端
	 * @param mobile_code
	 *            登录的手机号码
	 * @param cardType
	 *            支付方式
	 * @param transaction_money
	 *            充值金额 (分为单位)
	 * @param totalAmount
	 *            卡面值
	 * @param card_no
	 *            卡号
	 * @param card_pwd
	 *            卡密码
	 * @param bankId
	 *            银行标识
	 * @param expand
	 *            扩展参数
	 * @param sessionid
	 * @return
	 */
	public static String attemperRequest(String accesstype, String mobile_code,
			String cardType, String transaction_money, String totalAmount,
			String card_no, String card_pwd, String bankId, String expand,
			String sessionid) {
		String reValue = "";
		String action = "attemper.do";
		String method = "attemperRequest";
		iHttp http = null;
		String para = "";
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);

			paras.put("accesstype", accesstype);
			paras.put("mobile_code", mobile_code);
			paras.put("cardType", cardType);
			paras.put("transaction_money", transaction_money);
			paras.put("totalAmount", totalAmount);
			paras.put("card_no", card_no);
			paras.put("card_pwd", card_pwd);
			paras.put("bankId", bankId);
			paras.put("expand", expand);
			para = URLEncoder.encode(paras.toString());

			String temp = baseUrl + action + ";jsessionid=" + sessionid
					+ "?method=" + method + "&jsonString=" + para;

			temp = GT.encodingString(temp, "GBK", "UTF-8");
			String re = http.getViaHttpConnection(temp);
			// String re = http.getViaHttpConnection(baseUrl+action+
			// ";jsessionid="+sessionid+"?method="+method+"&jsonString="+para);
			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {

		}

		return reValue;
	}

	/**
	 * 跳转类账户充值
	 * 
	 * @param accesstype
	 *            接入方式 B表示web，W表示wap，C表示客户端
	 * @param mobile_code
	 *            登录的手机号码
	 * @param cardType
	 *            支付方式
	 * @param transaction_money
	 *            充值金额 (分为单位)
	 * @param bankId
	 *            银行标识
	 * @param expand
	 *            扩展参数
	 * @param sessionid
	 * @return
	 */
	public static String wapAttemperRequest(String accesstype,
			String mobile_code, String cardType, String transaction_money,
			String bankId, String expand, String sessionid) {
		String reValue = "";
		String action = "attemper.do";
		String method = "attemperRequest";
		iHttp http = null;
		String para = "";
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);

			paras.put("accesstype", accesstype);
			paras.put("mobile_code", mobile_code);
			paras.put("cardType", cardType);
			paras.put("transaction_money", transaction_money);
			paras.put("bankId", bankId);
			paras.put("expand", expand);
			paras.put("sessionId", sessionid);
			// paras.put("card_no", "");
			Log.e("paras===", paras.toString());
			Log.e("sessionid===", sessionid);
			para = URLEncoder.encode(paras.toString());

			PublicMethod.myOutput("cardType === " + cardType);
			PublicMethod.myOutput("bankId === " + bankId);

			PublicMethod.myOutput("url=" + baseUrl + action + ";jsessionid="
					+ sessionid + "?method=" + method + "&jsonString=" + para);

			// 219.148.162.70/jrtLot/attemper.do?method="attemperRequest"&jsonString="本地拼好的Json串"。

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			// String re = http.getViaHttpConnection(baseUrl + action +
			// "?method="
			// + method + "&jsonString=" + para);

			PublicMethod.myOutput("re == " + re);
			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {

		}

		return reValue;
	}

	/**
	 * 客户端追号套餐查询新接口
	 * 
	 * @param mobile_number
	 * @param sessionID
	 * @return
	 */
	public static String addNumQuery(String sessionid) {// String mobile_number,
		String action = "addNumberQuery.do";
		String method = "addNumQuery";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			// paras.put("mobile_code", mobile_number);
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONObject obj = new JSONObject(re);

				// lee
				// JSONArray jsonObject3 = new JSONArray(re);

				// jsonObject3=obj.getJSONArray("jsonObject3");
				// PublicMethod.myOutput("======================="+jsonObject3.length());
				// for(int i=0;i<jsonObject3.length();i++){
				// JSONObject jsonObject2 = jsonObject3.getJSONObject(i);
				// String lotNo = jsonObject2.getString("lotNo");
				// PublicMethod.myOutput("lotNo="+lotNo);
				// PublicMethod.myOutput("batchNum="+jsonObject2.getString("batchNum"));
				// PublicMethod.myOutput("lastNum="+jsonObject2.getString("lastNum"));
				// PublicMethod.myOutput("beginBatch="+jsonObject2.getString("beginBatch"));
				// PublicMethod.myOutput("lastBatch="+jsonObject2.getString("lastBatch"));
				// PublicMethod.myOutput("betNum="+jsonObject2.getString("betNum"));
				// PublicMethod.myOutput("amount="+jsonObject2.getString("amount"));
				// }

			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	// 体彩开奖公告查询
	public static String tlotterySelect(String play_name) {
		// String action = "touzhu.do";
		// String action="allSelect.do";
		// 新接口 陈晨 2010.08.10
		String action = "TCallSelect.do";
		String method = "lotterySelect";
		String reValue = "";
		String iStrUrl = "";
		iHttp http = null;
		// int random=PublicMethod.getRandomByRange(1,100);
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			// paras.put("inputCharset", 2);
			// paras.put("version", "v2.0");
			// paras.put("language", 1);
			// paras.put("transctionid", transctionId);
			// paras.put("mobile_code", "mobile_code");
			// paras.put("play_name", play_name);
			paras.put("lotNo", play_name);
			para = URLEncoder.encode(paras.toString());

			String where = "";
			JSONObject jwhere = new JSONObject();
			jwhere.put("lotNo", "");
			jwhere.put("batchCode", "");
			jwhere.put("startDate", "");
			jwhere.put("stopDate", "");
			jwhere.put("startLine", "1");
			jwhere.put("stopLine", "4");
			where = URLEncoder.encode(jwhere.toString());
			// PublicMethod.myOutput("-------------------url"+baseUrl+action+";jsessionid="+""+"?method="+method+"&jsonString="+para+"&jsonWhere="+where);
			// iStrUrl =
			// baseUrl+action+""+"?method="+method+"&jsonString="+para+"&round="+transctionId+"&key="+PublicMethod.getMD5Str(para+transctionId+privateKey)+"&jsonWhere="+where;
			String re = http.getViaHttpConnection(baseUrl + action + ""
					+ "?method=" + method + "&jsonString=" + para
					+ "&jsonWhere=" + where);// "&round="+transctionId+"&key="+MD5.toMd5(paras.toString()+transctionId+privateKey)+
			// // +";jsessionid="
			// // String re =
			// http.getViaHttpConnection(baseUrl+action+"?method="+method+"&jsonString="+para);
			PublicMethod.myOutput("===============" + re);
			if (re != null && re.length() > 0) {
				reValue = re;
				// JSONObject obj = new JSONObject(re);
				// PublicMethod.myOutput(obj.getString("error_code")+"=error_code");
				// if(obj.getString("error_code").equals("0000")){
				// reValue = obj.getString("play_name");
				// PublicMethod.myOutput("play_name="+reValue);
				// PublicMethod.myOutput("term_code="+obj.getString("term_code"));
				// PublicMethod.myOutput("win_base_code="+obj.getString("win_base_code"));
				// PublicMethod.myOutput("act_sell_amount="+obj.getString("act_sell_amount"));
				// PublicMethod.myOutput("valid_sell_amount="+obj.getString("valid_sell_amount"));
				// }
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}
		return reValue;
		// return iStrUrl;
	}

	// 体彩投注接口 陈晨 8.12
	/**
	 * 客户端体彩投注请求
	 * 
	 * @param mobile_code
	 * @param bet_code
	 *            投注码
	 * @param amount
	 *            购彩金额
	 * @param sessionid
	 * @return
	 */
	public static String bettingTC(String mobileCode, String lotNo,
			String betCode, String lotMulti, String amount, String oneMoney,
			String batchnum, String sessionid) {
		String action = "lotTCBet.do";
		String method = "addZh";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			// // paras.put("inputCharset", 2);
			// // paras.put("version", "v2.0");
			// // paras.put("language", 1);
			// // paras.put("transctionid", transctionId);
			paras.put("mobileCode", mobileCode);
			// paras.put("sessionid", sessionid);
			paras.put("lotNo", lotNo);
			paras.put("betCode", betCode);
			paras.put("lotMulti", lotMulti);
			paras.put("amount", amount);
			paras.put("oneMoney", oneMoney);
			paras.put("batchnum", batchnum);
			para = URLEncoder.encode(paras.toString());
			// para="{mobileCode:\""+mobileCode+"\","+"sessionid:\""+sessionid+"\","+"lotNo:\""+lotNo+"\","+"betCode:\""+betCode+"\","+"lotMulti:\""+lotMulti+"\","+"oneMoney:\""+oneMoney+"\","+"amount:\""+amount+"\"}";
			// para = paras.toString();
			// PublicMethod.myOutput("=====transctionId=========="+transctionId);
			PublicMethod.myOutput("=======url===" + baseUrl + action
					+ "?method=" + method + "&jsonString=" + para);
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);// +"&round="+transctionId+"&key="+MD5.toMd5(
			// paras.toString()+transctionId+privateKey))+";jsessionid="+sessionid
			if (re != null && re.length() > 0) {
				reValue = re;

			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	// 体彩投注接口 陈晨 8.12
	/**
	 * 客户端体彩中奖查询请求
	 * 
	 * @param mobile_code
	 * @param bet_code
	 *            投注码
	 * @param amount
	 *            购彩金额
	 * @param sessionid
	 * @return
	 */
	public static String winingSelectTC(String mobileCode, String startLine,
			String stopLine, String sessionid) {
		System.out.println("------jrtlot-sessionid------" + sessionid);
		String action = "TCallSelect.do";
		String method = "zhongjiangSelect";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			// paras.put("inputCharset", 2);
			// paras.put("version", "v2.0");
			// paras.put("language", 1);
			// paras.put("transctionid", transctionId);
			paras.put("mobileCode", mobileCode);
			// paras.put("sessionid",sessionid);
			para = URLEncoder.encode(paras.toString());
			System.out.println("=======para==========" + para);
			// para = paras.toString();

			String where = "";
			JSONObject jwhere = new JSONObject();
			jwhere.put("lotNo", "");
			jwhere.put("batchCode", "");
			jwhere.put("startDate", "");
			jwhere.put("stopDate", "");
			jwhere.put("startLine", startLine);
			jwhere.put("stopLine", stopLine);
			where = URLEncoder.encode(jwhere.toString());
			PublicMethod.myOutput("-------------------url" + baseUrl + action
					+ "?method=" + method + "&jsonString=" + para
					+ "&jsonWhere=" + where);
			// iStrUrl =
			// baseUrl+action+""+"?method="+method+"&jsonString="+para+"&round="+transctionId+"&key="+PublicMethod.getMD5Str(para+transctionId+privateKey)+"&jsonWhere="+where;
			// String re =
			// http.getViaHttpConnection(aBaseUrl+action+"?method="+method+"&jsonString="+para+"&round="+transctionId+"&key="+MD5.toMd5(paras.toString()+transctionId+privateKey)+"&jsonWhere="+where);
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para + "&jsonWhere=" + where);// +"&round="+transctionId+"&key="+MD5.toMd5(para+transctionId+privateKey)+";jsessionid="+sessionid
			if (re != null && re.length() > 0) {
				reValue = re;

			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 客户端购彩查询请求(排列三、超级大乐透)
	 * 
	 * @param mobile_code
	 * @param startDate
	 * @param endDate
	 * @param sessionID
	 * @return
	 */
	public static String bettingSelectTC(String mobileCode, String lotNo,
			String startLine, String stopLine, String sessionid) {// String
		// mobile_code,
		// String action = "touzhu.do";
		// String method = "bettingSelect";
		String action = "TCallSelect.do";
		String method = "touzhuSelect";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("mobileCode", mobileCode);
			paras.put("lotNo", lotNo);
			// paras.put("mobile_code", mobile_code);
			para = URLEncoder.encode(paras.toString());

			String where = "";
			JSONObject jwhere = new JSONObject();
			jwhere.put("lotNo", "");
			jwhere.put("batchCode", "");
			jwhere.put("startDate", "");
			jwhere.put("stopDate", "");
			jwhere.put("startLine", startLine);
			jwhere.put("stopLine", stopLine);
			where = URLEncoder.encode(jwhere.toString());
			// String re =
			// http.getViaHttpConnection(baseUrl+action+";jsessionid="+sessionid+"?method="+method+"&jsonString="+para);
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para + "&jsonWhere=" + where);// +"&round="+transctionId+"&key="+MD5.toMd5(paras.toString()+transctionId+privateKey)
			if (re != null && re.length() > 0) {
				reValue = URLEncoder.decode(re);
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 客户端购彩查询请求(排列三、超级大乐透)
	 * 
	 * @param mobile_code
	 * @param startDate
	 * @param endDate
	 * @param sessionID
	 * @return
	 */
	public static String bettingSelectTCNew(String starTime, String endTime,
			String sessionid) {// String
		// mobile_code,
		// String action = "touzhu.do";
		// String method = "bettingSelect";
		String action = "betQuery.do";
		String method = "betAndBetTegQuery";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("startTime", starTime);
			paras.put("endTime", endTime);
			// paras.put("mobile_code", mobile_code);
			para = URLEncoder.encode(paras.toString());

			String where = "";
			JSONObject jwhere = new JSONObject();
			jwhere.put("lotNo", "");
			jwhere.put("batchCode", "");
			jwhere.put("startDate", "");
			jwhere.put("stopDate", "");
			// jwhere.put("startLine", startLine);
			// jwhere.put("stopLine", stopLine);
			where = URLEncoder.encode(jwhere.toString());
			// String re =
			// http.getViaHttpConnection(baseUrl+action+";jsessionid="+sessionid+"?method="+method+"&jsonString="+para);
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);// +"&round="+transctionId+"&key="+MD5.toMd5(paras.toString()+transctionId+privateKey)
			Log.e("re======", re);
			if (re != null && re.length() > 0) {
				reValue = URLEncoder.decode(re);
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 体彩赠送彩票请求
	 * 
	 * @param from_mobile_code
	 *            客户端手机号
	 * @param to_mobile_code
	 *            受赠方手机号
	 * @param bet_code
	 *            投注码
	 * @param amount
	 *            购彩金额
	 * @param sessionID
	 * @return
	 */
	public static String giftTC(String from_mobile_code, String to_mobile_code,
			String lotNo, String bet_code, String lotMulti, String amount,
			String oneMoney, String note_code, String sessionid, String advice) {
		// String action = "touzhu.do";
		String action = "presentedlottery.do";
		String method = "presented";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			// Random rdm = new Random();
			// int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("from_mobile_code", from_mobile_code);
			paras.put("to_mobile_code", to_mobile_code);
			paras.put("lotNo", lotNo);
			paras.put("bet_code", bet_code);
			paras.put("lotMulti", lotMulti);
			paras.put("amount", amount);
			paras.put("oneMoney", oneMoney);
			paras.put("note_code", note_code);
			paras.put("advice", advice);
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 体彩客户端赠送彩票查询请求//已测试
	 * 
	 * @param mobile_code
	 * @param
	 * @param
	 * @param sessionID
	 * @return
	 */
	public static String giftSelectTC(String mobileCode, String sessionid,
			String startLine, String stopLine) {
		String action = "TCallSelect.do";
		String method = "giftAllSelect";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("mobileCode", mobileCode);
			para = URLEncoder.encode(paras.toString());

			String where = "";
			JSONObject jwhere = new JSONObject();
			jwhere.put("lotNo", "");
			jwhere.put("batchCode", "");
			jwhere.put("startDate", "");
			jwhere.put("stopDate", "");
			jwhere.put("startLine", startLine);
			jwhere.put("stopLine", stopLine);
			where = URLEncoder.encode(jwhere.toString());
			// String re =
			// http.getViaHttpConnection(baseUrl+action+";jsessionid="+sessionid+"?method="+method+"&jsonString="+para);
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para + "&jsonWhere=" + where);

			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 体彩客户端被赠送彩票查询请求//已测试
	 * 
	 * @param mobile_code
	 * @param
	 * @param
	 * @param sessionID
	 * @return
	 */
	public static String giftedSelectTC(String mobileCode, String sessionid,
			String startLine, String stopLine) {
		String action = "TCallSelect.do";
		String method = "giftedAllSelect";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("mobileCode", mobileCode);
			para = URLEncoder.encode(paras.toString());

			String where = "";
			JSONObject jwhere = new JSONObject();
			jwhere.put("lotNo", "");
			jwhere.put("batchCode", "");
			jwhere.put("startDate", "");
			jwhere.put("stopDate", "");
			jwhere.put("startLine", startLine);
			jwhere.put("stopLine", stopLine);
			where = URLEncoder.encode(jwhere.toString());
			// String re =
			// http.getViaHttpConnection(baseUrl+action+";jsessionid="+sessionid+"?method="+method+"&jsonString="+para);
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para + "&jsonWhere=" + where);

			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 客户端追号套餐查询
	 * 
	 * @param mobile_number
	 * @param sessionID
	 * @return
	 */
	public static String addNumQueryTC(String sessionid, String mobileCode,
			String startLine, String stopLine) {// String mobile_number,
		String action = "TCallSelect.do";
		String method = "trackingNumberSelect";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("mobileCode", mobileCode);
			para = URLEncoder.encode(paras.toString());

			String where = "";
			JSONObject jwhere = new JSONObject();
			jwhere.put("lotNo", "");
			jwhere.put("batchCode", "");
			jwhere.put("startDate", "");
			jwhere.put("stopDate", "");
			jwhere.put("startLine", startLine);
			jwhere.put("stopLine", stopLine);
			where = URLEncoder.encode(jwhere.toString());
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para + "&jsonWhere=" + where);
			if (re != null && re.length() > 0) {
				reValue = re;

			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 体彩套餐请求//已测试
	 * 
	 * @param mobile_number
	 * @param sessionID
	 * @param lottry_type
	 *            套餐类别 (菜种)
	 * @param amount
	 *            每次套餐金额 以分为单位
	 * @param agency_code
	 *            渠道号
	 * @return
	 */
	public static String packageDealTC(String mobileCode, String lotNo,
			String amount, String sessionid) {
		// String action = "newPackage.do";
		String action = "lotTCBet.do";
		String method = "addTc";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			// Random rdm = new Random();
			// int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();

			// 通信准备
			// paras.put("inputCharset", 2);
			// paras.put("version", "v2.0");
			// paras.put("language", 1);
			// paras.put("transctionid", transctionId);

			paras.put("mobileCode", mobileCode);
			paras.put("lotNo", lotNo);
			paras.put("amount", amount);
			// paras.put("agency_code", "");
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			Log.e("re====", "" + re);
			PublicMethod.myOutput(re);
			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 取消追号
	 * 
	 * @param mobileCode
	 * @param sessionid
	 * @param tsubscribeId
	 * @return
	 */
	public static String cancelTrankingNumber(String mobileCode,
			String sessionid, String tsubscribeId) {
		// String action = "newPackage.do";
		String action = "lotTCBet.do";
		String method = "trankingNumberUpdate";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			String para = "";
			JSONObject paras = new JSONObject();

			paras.put("mobileCode", mobileCode);
			paras.put("tsubscribeId", tsubscribeId);

			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);

			PublicMethod.myOutput(re);
			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 判断用户是否完善信
	 * 
	 * @return
	 */
	public static boolean ifPerfectIfo(Context context, String sessionid) {
		jrtLot.context = context;
		String action = "idcardcheck.do";
		String method = "validation";
		iHttp http = null;

		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method);

			// 对返回码进行处理
			JSONObject obj;
			obj = new JSONObject(re);

			String error_code = obj.getString("error_code");
			if (error_code.equals("000001")) {// 身份证为空，完善信息对话框
				perfectIfoDialog(sessionid);
			} else if (error_code.equals("000002")) {// 原来的身份证错误请重新修改，修改信息对话框
				changePerfectIfoDialog(sessionid);
			} else if (error_code.equals("070002")) {
				Intent intent1 = new Intent(UserLogin.UNSUCCESS);
				context.sendBroadcast(intent1);
				Intent intent2 = new Intent(context, UserLogin.class);
				context.startActivity(intent2);
			} else if (error_code.equals("000005")) {
				Toast.makeText(context, "年龄不足18周岁", Toast.LENGTH_SHORT).show();
			} else if (error_code.equals("000000")) {
				return true;
			}

		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}
		return false;
	}

	/**
	 * 完善信息
	 * 
	 * @param sessionid
	 *            用户登录后的id
	 * @param idcardno
	 *            身份证号
	 * @return
	 */
	public static String perfectIfo(String sessionid, String idcardno) {
		String action = "idcardadd.do";
		String method = "add";
		iHttp http = null;
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("idcardno", idcardno);
			para = URLEncoder.encode(paras.toString());
			Log.e("sessionid===", sessionid);
			Log.e("idcardno===", idcardno);
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);

			// 对返回码进行处理
			JSONObject object;
			object = new JSONObject(re);
			String error_code = object.getString("error_code");

			if (error_code.equals("000000")) {// 修改成功
				Toast.makeText(jrtLot.context, "修改成功", Toast.LENGTH_SHORT)
						.show();
			} else if (error_code.equals("000001")) {// 身份证为空，完善信息对话框
				Toast.makeText(jrtLot.context, "身份证为空", Toast.LENGTH_SHORT)
						.show();
			} else if (error_code.equals("000002")) {// 原来的身份证错误请重新修改，修改信息对话框
				Toast.makeText(jrtLot.context, "原来的身份证错误请重新修改",
						Toast.LENGTH_SHORT).show();
			} else if (error_code.equals("000003")) {// 修改失败
				Toast.makeText(jrtLot.context, "修改失败", Toast.LENGTH_SHORT)
						.show();
			} else if (error_code.equals("070002")) {// 用户未登录
				Toast.makeText(jrtLot.context, "用户未登录", Toast.LENGTH_SHORT)
						.show();
			} else if (error_code.equals("000004")) {// 身份证已被使用
				Toast.makeText(jrtLot.context, "身份证号码已被使用", Toast.LENGTH_SHORT)
						.show();
			} else if (error_code.equals("000005")) {// 年龄不足18周岁
				Toast.makeText(context, "年龄不足18周岁", Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return null;
	}

	public static String sessionid;
	public static Context context;

	/**
	 * 完善信息对话框
	 * 
	 */
	public static void perfectIfoDialog(String sessionid) {
		jrtLot.sessionid = sessionid;
		LayoutInflater inflater = LayoutInflater.from(context);
		final View textEntryView = inflater.inflate(
				R.layout.perfect_message_dialog, null);
		final EditText name = (EditText) textEntryView
				.findViewById(R.id.et_perfect_message_name);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.perfect_message_title);
		builder.setView(textEntryView);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						perfectIfo(jrtLot.sessionid, name.getText().toString());

					}

				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}

				});
		builder.show();
	}

	/**
	 * 修改信息对话框
	 * 
	 */
	public static void changePerfectIfoDialog(String sessionid) {
		jrtLot.sessionid = sessionid;
		LayoutInflater inflater = LayoutInflater.from(context);
		final View textEntryView = inflater.inflate(
				R.layout.perfect_message_dialog, null);
		final EditText name = (EditText) textEntryView
				.findViewById(R.id.et_perfect_message_name);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.change_message_title);
		builder.setView(textEntryView);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						perfectIfo(jrtLot.sessionid, name.getText().toString());

					}

				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}

				});
		builder.show();
	}

	/**
	 * 提现请求接口
	 * 
	 * @param mobileCode
	 *            手机号
	 * @param sessionid
	 * @param withdrawel_money
	 *            提现金额
	 * @param bank_name
	 *            银行标识
	 * @param ID_number
	 *            银行卡号
	 * @return
	 */
	public static String getMoneyQuest(String name, String sessionid,
			String withdrawel_money, String bank_name, String ID_number,
			String areaName) {
		String action = "cashaction.do";
		String method = "cash";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("NAME", name);
			paras.put("withdrawel_money", withdrawel_money);
			paras.put("bank_name", bank_name);
			paras.put("ID_number", ID_number);
			paras.put("PROVNAME", "");
			paras.put("AREANAME", areaName);
			para = URLEncoder.encode(paras.toString(), "UTF-8");
			// para = URLEncoder.encode(para);
			// String re =
			// http.getViaHttpConnection(baseUrl+action+";jsessionid="+sessionid+"?method="+method+"&jsonString="+para);
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);

			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 提现修改接口
	 * 
	 * @param name账户名
	 * @param sessionid
	 * @param bankAccount账户
	 * @param bankName开户银行名
	 * @param provName开户银行所在省
	 * @param areaName开户银行所在市
	 * @param state提现状态
	 * @param userId
	 * @return
	 */
	public static String changeQuest(String name, String sessionid,
			String bankAccount, String bankName, int state, String mobileCode,
			String areaName, String amt) {
		String action = "WithdrawTeviewAction.do";
		String method = "editCashDetail";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("name", name);
			paras.put("bankAccount", bankAccount);
			paras.put("bankName", bankName);
			paras.put("provName", "");
			paras.put("areaName", areaName);
			paras.put("state", state);
			paras.put("mobile_code", mobileCode);
			paras.put("subBankName", "");
			paras.put("amt", amt);
			para = URLEncoder.encode(paras.toString());
			para = URLEncoder.encode(para);
			// http.getViaHttpConnection(baseUrl+action+";jsessionid="+sessionid+"?method="+method+"&jsonString="+para);
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);

			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 提现取消接口
	 * 
	 * @param mobile_code
	 * @param sessionid
	 * @return
	 */
	public static String cancelQuest(String mobileCode, String sessionid) {
		String action = "WithdrawTeviewAction.do";
		String method = "cancelTcashDetail";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("mobile_code", mobileCode);
			para = URLEncoder.encode(paras.toString());
			// String re =
			// http.getViaHttpConnection(baseUrl+action+";jsessionid="+sessionid+"?method="+method+"&jsonString="+para);
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);

			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 查询提现状态接口
	 * 
	 * @param mobile_code手机号
	 * @param sessionid
	 * @return
	 */
	public static String stateQuset(String mobileCode, String sessionid) {
		String action = "WithdrawTeviewAction.do";
		String method = "getCashDetailState";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("mobile_code", mobileCode);
			para = URLEncoder.encode(paras.toString());
			// String re =
			// http.getViaHttpConnection(baseUrl+action+";jsessionid="+sessionid+"?method="+method+"&jsonString="+para);
			String re = http.getViaHttpConnection_(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			// re = new String(re.getBytes("UTF-8"), "GBK");
			// re = new String(re.getBytes("ISO-8859-1"), "UTF-8");
			// re = new String(re.getBytes("ISO-8859-1"), "GBK");
			// re = URLDecoder.decode(re.toString(), "GBK");
			JSONObject ojb = new JSONObject(re);
			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	// uv和pv统计接口
	private static String jrtLotUrlPV = "http://219.148.162.70:8000/tongji/";

	public static void setPara(int count, String channelId)
			throws JSONException {
		// 获取sessionId判断用户是否登录
		try {
			iHttp http = new iHttp();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("type", count);
			jsonObject.put("channelId", channelId);
			http.getViaHttpConnection(jrtLotUrlPV + "pv/savepv.do?jsonString="
					+ URLEncoder.encode(jsonObject.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 合买查询请求
	 * 
	 * @param lotNo
	 *            彩种编号
	 * @param case_amt
	 *            金额阶梯
	 * @param case_time
	 *            发起时间标识
	 * @param case_jindu
	 *            进度标识
	 * @param reqiFlag
	 *            人气标识
	 * @param gaoshouFlag
	 *            高手标识
	 * @param lishiFlag
	 *            历史标识
	 * @param case_batch
	 *            期号
	 */
	public static String queryJoin(String lotNo, String case_amt,
			String case_time, String case_jindu, String reqiFlag,
			String gaoshouFlag, String lishiFlag, String case_batch, String end) {
		String action = "lotcase.do";
		String method = "searchCaseInfo";
		String jsonString = "searchCaseInfoJson";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("lotNo", lotNo);
			paras.put("case_amt", case_amt);
			paras.put("case_time", case_time);
			paras.put("case_jindu", case_jindu);
			paras.put("case_title", "");
			paras.put("reqiFlag", reqiFlag);
			paras.put("gaoshouFlag", gaoshouFlag);
			paras.put("lishiFlag", lishiFlag);
			paras.put("case_batch", case_batch);
			paras.put("startPage", "0");
			paras.put("endPage", end);
			para = URLEncoder.encode(paras.toString());
			// http.getViaHttpConnection(baseUrl+action+";jsessionid="+sessionid+"?method="+method+"&jsonString="+para);
			String re = http.getViaHttpConnection_(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method + "&"
					+ jsonString + "=" + para);

			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 发起合买
	 * 
	 * mobile_code 用户手机号码
	 * 
	 * allamt 合买总金额
	 * 
	 * num 倍数
	 * 
	 * oneamt 每份金额
	 * 
	 * name 方案文件名称
	 * 
	 * muchcontent 复式合买方案内容
	 * 
	 * batch 期号
	 * 
	 * lotno 彩种编号
	 * 
	 * drawway 方案玩法
	 * 
	 * baodiamt 保底份数
	 * 
	 * buyamt_by_user 认购份数
	 * 
	 * allNum 方案总份数
	 * 
	 * pushmoney 发起人提成比例
	 * 
	 * title 方案标题
	 * 
	 * descible 方案推荐描述
	 * 
	 * open_flag 公开标识
	 * 
	 * acceptype 接入方式
	 * 
	 * @return
	 */
	public static String startJoin(String mobile_code, String allamt,
			String num, String oneamt, String name, String muchcontent,
			String batch, String lotno, String drawway, String baodiamt,
			String buyamt_by_user, String allNum, String pushmoney,
			String open_flag, String sessionid, String descible) {
		String action = "lotcase.do";
		String method = "makeCase";
		String jsonString = "caseinfojson";
		String reValue = "";
		iHttp http = null;
		String allAmt = Integer.toString(Integer.parseInt(allamt) * 100);
		String oneAmt = Integer.toString(Integer.parseInt(oneamt) * 100);
		try {
			http = new iHttp();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("mobile_code", mobile_code);
			paras.put("allamt", allAmt);
			paras.put("num", num);
			paras.put("oneamt", oneAmt);
			paras.put("name", name);
			paras.put("muchcontent", muchcontent);
			paras.put("batch", batch);
			paras.put("lotno", lotno);
			paras.put("drawway", "0");
			paras.put("baodiamt", baodiamt);
			paras.put("buyamt_by_user", buyamt_by_user);
			paras.put("allNum", allNum);
			paras.put("pushmoney", pushmoney);
			paras.put("title", "如意彩合买");
			paras.put("descible", descible);
			paras.put("open_flag", open_flag);
			paras.put("acceptype", "C");
			para = URLEncoder.encode(paras.toString());
			// para = URLEncoder.encode(para);
			// http.getViaHttpConnection(baseUrl+action+";jsessionid="+sessionid+"?method="+method+"&jsonString="+para);
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method + "&"
					+ jsonString + "=" + para);

			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 参与合买
	 * 
	 * @param mobile_code
	 * @param case_id
	 * @param case_buyNumByUser
	 *            认购份数
	 * @param case_buyAmtByUser
	 *            认购金额
	 * @return
	 */
	public static String buyJoin(String mobile_code, String case_id,
			String case_buyNumByUser, String case_buyAmtByUser, String sessionid) {
		String action = "lotcase.do";
		String method = "buyTlotByCase";
		String jsonString = "caseBuyjson";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("mobile_code", mobile_code);
			paras.put("case_id", case_id);
			paras.put("case_buyNumByUser", case_buyNumByUser);
			paras.put("case_buyAmtByUser", case_buyAmtByUser);
			paras.put("case_acceptype", "C");
			para = URLEncoder.encode(paras.toString());
			// http.getViaHttpConnection(baseUrl+action+";jsessionid="+sessionid+"?method="+method+"&jsonString="+para);
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method + "&"
					+ jsonString + "=" + para);

			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 查询详细信息
	 * 
	 * @param mobile_code
	 *            手机号
	 * @param case_caseId
	 *            方案id
	 * @return
	 */
	public static String joinInfo(String mobile_code, String case_caseId,
			String sessionid) {
		String action = "lotcase.do";
		String method = "searCaseInfoById";
		String jsonString = "caseinfojson";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("mobile_code", mobile_code);
			paras.put("case_caseId", case_caseId);
			para = URLEncoder.encode(paras.toString());
			// http.getViaHttpConnection(baseUrl+action+";jsessionid="+sessionid+"?method="+method+"&jsonString="+para);
			String re = http.getViaHttpConnection_(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method + "&"
					+ jsonString + "=" + para);

			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 得到截止时间
	 * 
	 * @param lotNo
	 * @return
	 */
	public static String getTerm(String lotNo) {
		String action = "touzhu.do";
		String method = "getLotNo";
		iHttp http = null;
		String para = "";
		String term = "";
		try {
			http = new iHttp();
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			paras.put("lotNo", lotNo);
			para = URLEncoder.encode(paras.toString());

			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				JSONObject obj = new JSONObject(re);
				if (obj.has("batchCode")) {
					term = obj.getString("batchCode");
				}
			}
		} catch (Exception e) {
		}
		return term;
	}

	/**
	 * 发起合买查询
	 * 
	 * @param mobile_code
	 * @param batch期号
	 * @param lotno
	 *            彩种
	 * @return
	 */
	public static String checkBuy(String mobile_code, String batch,
			String lotno, String sessionid) {
		String action = "lotcase.do";
		String method = "searchCaseByfqr";
		// String jsonString = "caseinfojson";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("mobile_code", mobile_code);
			paras.put("batch", batch);
			paras.put("lotno", lotno);
			paras.put("startPage", "0");
			paras.put("endPage", "100");
			para = URLEncoder.encode(paras.toString());
			// http.getViaHttpConnection(baseUrl+action+";jsessionid="+sessionid+"?method="+method+"&jsonString="+para);
			String re = http.getViaHttpConnection_(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonObject=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 用户参与合买查询信息请求
	 * 
	 * @param mobile_code
	 * @param sessionid
	 * @return
	 */
	public static String checkJoin(String mobile_code, String sessionid) {
		String action = "lotcase.do";
		String method = "searchCaseByUserNo";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("mobile_code", mobile_code);
			paras.put("startPage", "0");
			paras.put("endPage", "100");
			para = URLEncoder.encode(paras.toString());
			// http.getViaHttpConnection(baseUrl+action+";jsessionid="+sessionid+"?method="+method+"&jsonString="+para);
			String re = http.getViaHttpConnection_(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonObject=" + para);

			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 撤销合买
	 * 
	 * @param mobile_code
	 * @param case_caseId
	 * @param sessionid
	 * @return
	 */

	public static String remove(String mobile_code, String case_caseId,
			String sessionid) {
		String action = "lotcase.do";
		String method = "caseChexiao";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("mobile_code", mobile_code);
			paras.put("case_caseId", case_caseId);
			para = URLEncoder.encode(paras.toString());
			String re = http.getViaHttpConnection(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&casechexiaoJson=" + para);

			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 参与合买详细信息
	 * 
	 * @param case_caseId
	 * @param sessionid
	 * @return
	 */
	public static String joinDetails(String mobile_code, String case_caseId,
			String sessionid) {
		String action = "lotcase.do";
		String method = "searchBuyCaseInfo";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("mobileId", mobile_code);
			paras.put("case_id", case_caseId);
			paras.put("startPage", "0");
			paras.put("endPage", "100");
			Log.e("paras===", paras.toString());
			para = URLEncoder.encode(paras.toString());
			// http.getViaHttpConnection(baseUrl+action+";jsessionid="+sessionid+"?method="+method+"&jsonString="+para);
			String re = http.getViaHttpConnection_(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&tbuylotinfoJson=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * DNA充值账户绑定查询请求
	 * 
	 * @param mobile
	 * @param sessionid
	 * @return
	 */
	public static String checkType(String mobile, String sessionid) {
		String action = "dnabind.do";
		String method = "bindInfoFind";
		String reValue = "";
		iHttp http = null;
		try {
			http = new iHttp();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("Mobile", mobile);

			para = URLEncoder.encode(paras.toString());
			String re = http.getViaHttpConnection_(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);

			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

}
