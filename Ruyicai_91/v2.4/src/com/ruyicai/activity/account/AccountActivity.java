package com.ruyicai.activity.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.dialog.ExitDialogFactory;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.QueryDNAInterface;
import com.ruyicai.net.newtransaction.RechargeInterface;
import com.ruyicai.net.newtransaction.pojo.RechargePojo;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.ShellRWSharesPreferences;

/**
 * 主页面 充值界面
 * @author Administrator
 *
 */
public class AccountActivity extends Activity  implements HandlerMsg {
	private String url="";
	private ProgressDialog progressdialog;
	private String re;
	private String sessionId="";
	private String phonenum="";
	private String userno="";
	private String RECHARGTYPE = "0";
	HandlerMsg msg;
	private String TITLE = "title";
	private String ISHANDINGFREE = "isHandingFree";
	private String  PICTURE = "";
	private String error_code;
	private String strExpand;
	private MyHandler handler = new MyHandler(this);
	private String message="";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        factory = LayoutInflater.from(this);
		List list = null;
		setContentView(R.layout.account_list_main);
		ListView listView = (ListView) findViewById(R.id.account_rechange_listview);
		list = setContentForMainList();
		AccountAdapter adapter = new AccountAdapter(this,list); 
		listView.setAdapter(adapter);
		AccountMainItemListener listener=new AccountMainItemListener(this);
        listView.setOnItemClickListener(listener);
	}
	class AccountAdapter extends BaseAdapter{
		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Map<String, Object>> mList;
		public AccountAdapter(Context context, List<Map<String, Object>> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;
		}
		public int getCount() {
			return mList.size();
		}
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			String title = (String) mList.get(position).get(TITLE);
			Integer  iconid = (Integer)mList.get(position).get(PICTURE);
			Integer isVisible = (Integer)mList.get(position).get(ISHANDINGFREE);
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.account_listviw_item,null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.account_recharge_listview_text);
				holder.isfreeHanding = (TextView)convertView.findViewById(R.id.ishandingfree);
				holder.lefticon = (ImageView)convertView.findViewById(R.id.account_recharge_type);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(title);
			holder.lefticon.setBackgroundResource(iconid);
			holder.isfreeHanding.setVisibility(isVisible);
			return convertView;
		}
		class ViewHolder{
			TextView title;
			ImageView lefticon;
			TextView isfreeHanding;
		}
	}
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}	
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Constants.MEMUTYPE = 0;
	}
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	LayoutInflater factory = null;
	private Dialog RechargeType=null;
    String bankCardNo = "";
	EditText bank_card_phone_bankid;
	EditText bank_card_phone_phone_num;
	EditText bank_card_phone_name;
	ShellRWSharesPreferences shellRW;
	
	//银行卡电话充值弹出框(DNA)
    protected Dialog createBankCardPhoneDialog(){
    	RECHARGTYPE = "01";	
	  final View bank_card_phone_online_view = factory.inflate(R.layout.account_bank_card_phone_online_dialog, null);
	  bank_card_phone_bankid = (EditText) bank_card_phone_online_view.findViewById(R.id.bank_card_phone_bankid);
      bank_card_phone_bankid.setText(bankCardNo);
      bank_card_phone_phone_num = (EditText)bank_card_phone_online_view.findViewById(R.id.bank_card_phone_phone_num);// 手机号
	  bank_card_phone_phone_num.setText(phonenum);
	  bank_card_phone_bankid.setEnabled(false);
	  final Button ok = (Button)bank_card_phone_online_view.findViewById(R.id.ok);
		final Button canel =(Button)bank_card_phone_online_view.findViewById(R.id.canel);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShellRWSharesPreferences pre = new ShellRWSharesPreferences(AccountActivity.this, "addInfo");
				String sessionIdStr = pre.getUserLoginInfo("sessionid");
				if (sessionIdStr == null || sessionIdStr.equals("")) {
				    Intent intentSession = new Intent(AccountActivity.this, UserLogin.class);
					startActivity(intentSession);
				} else {
					// 银行卡语音充值网络连接
					beiginBankCardPhoneOnline(bank_card_phone_online_view);
				}
			}
		});
		canel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RechargeType.dismiss();
			}
		});
		        RechargeType = new Dialog(this,R.style.dialog);
		        RechargeType.setContentView(bank_card_phone_online_view);  
		        return RechargeType;
	}
    
    //银行卡电话充值弹出框(DNA)（未绑定 ）
    protected Dialog createBankCardPhoneDialogNo(){
    	RECHARGTYPE = "01";
		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(AccountActivity.this, "addInfo");
    	final View bank_card_phone_view = factory.inflate(R.layout.account_bank_card_phone_dialog, null);
		String phonenum = pre.getUserLoginInfo("phonenum");
	    bank_card_phone_phone_num = (EditText) bank_card_phone_view.findViewById(R.id.bank_card_phone_phone_num);// 手机号
		bank_card_phone_name = (EditText) bank_card_phone_view.findViewById(R.id.bank_card_phone_phone_name);// 姓名
		bank_card_phone_phone_num.setText(phonenum);
		bank_card_phone_name.setText(name);
 		EditText bank_card_phone_bankid = (EditText) bank_card_phone_view.findViewById(R.id.bank_card_phone_bankid);// 银行卡号
 		EditText bank_card_phone_idcard = (EditText) bank_card_phone_view.findViewById(R.id.bank_card_phone_phone_idcard);// 身份证号
 		EditText bank_card_phone_home = (EditText) bank_card_phone_view.findViewById(R.id.bank_card_phone_phone_home);// 户籍所在地
 		EditText bank_card_phone_province = (EditText) bank_card_phone_view.findViewById(R.id.bank_card_phone_phone_province);// 所在省
 		bank_card_phone_bankid.setText(bankCardNo);
 		bank_card_phone_idcard.setText(certid);
 		bank_card_phone_home.setText(certAddress);
 		bank_card_phone_province.setText(bankAddress);
	    final Button ok = (Button)bank_card_phone_view.findViewById(R.id.ok);
	    final Button canel =(Button)bank_card_phone_view.findViewById(R.id.canel);
			ok.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					ShellRWSharesPreferences pre = new ShellRWSharesPreferences(AccountActivity.this, "addInfo");
					String sessionIdStr = pre.getUserLoginInfo("sessionid");
					if (sessionIdStr!=null&&sessionIdStr.equals("")) {
						Intent intentSession = new Intent(AccountActivity.this,UserLogin.class);
						startActivity(intentSession);
					}else{
						beiginBankCardPhoneNo(bank_card_phone_view);
				    }
				}
			});
			canel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					RechargeType.dismiss();
				}
			});
	        RechargeType = new Dialog(this,R.style.dialog);
	        RechargeType.setContentView(bank_card_phone_view);
	        return RechargeType;
    }
	private String phoneCardType = "0206";
	private String phoneCardValue = "100";
	private String gameCardType = "0204";
	// 移动充值卡
	private final String YIDONG = "0203";
	// 联通充值卡
    private final String LIANTONG = "0206";
	// 电信充值卡
	private final String DIANXIN = "0221";
	//电话卡充值弹出框
    protected Dialog createPhoneRechargeCardDialog(){
    	RECHARGTYPE = "02";
    	final View phone_card_recharg_view = factory.inflate(R.layout.account_phone_cards_recharge_dialog, null);
		final Spinner phone_card_spinner = (Spinner) phone_card_recharg_view.findViewById(R.id.phone_card_spinner);
		phone_card_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {

						// 点击下拉框。。。
						int position = phone_card_spinner.getSelectedItemPosition();
						if (position == 0) {
							phoneCardType = YIDONG;
						} else if (position == 1) {
							phoneCardType = LIANTONG;
						} else if (position == 2) {
							phoneCardType = DIANXIN;
						}
					}
                    @Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// 没有任何的触发事件时
					}
				});
		
		final Spinner phone_card_value_spinner = (Spinner) phone_card_recharg_view.findViewById(R.id.phone_card_value_spinner);
		phone_card_value_spinner.setSelection(4);// 默认100元
		phone_card_value_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
                        int position = phone_card_value_spinner
								.getSelectedItemPosition();
						String[] values = { "10", "20", "30", "50", "100","200", "300", "500" };
						for (int i = 0; i < values.length; i++) {
							phoneCardValue = values[position];
						}
					}
					public void onNothingSelected(AdapterView<?> arg0) {
						// 没有任何的触发事件时
						}
                 });
		final Button ok = (Button)phone_card_recharg_view.findViewById(R.id.ok);
		final Button canel =(Button)phone_card_recharg_view.findViewById(R.id.canel);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShellRWSharesPreferences pre = new ShellRWSharesPreferences(AccountActivity.this, "addInfo");
				String sessionIdStr = pre.getUserLoginInfo("sessionid");
				if (sessionIdStr!=null&&sessionIdStr.equals("")) {
					Intent intentSession = new Intent(AccountActivity.this,UserLogin.class);
					startActivity(intentSession);}
				else{
			    beginPhoneCardRecharge(phone_card_recharg_view);
			    }
			
			}
		});
		canel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RechargeType.dismiss();
			}
		});
		        RechargeType = new Dialog(this,R.style.dialog);
		        RechargeType.setContentView(phone_card_recharg_view);
    	        return RechargeType;
    }
    //支付宝充值弹出框
    protected Dialog createAlipayDialog(){
    	RECHARGTYPE = "05";
    	final View zfb_recharge_view = factory.inflate(R.layout.account_alipay_recharge_dialog, null);
    	final Button ok = (Button)zfb_recharge_view.findViewById(R.id.ok);
		final Button canel =(Button)zfb_recharge_view.findViewById(R.id.canel);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				beginAlipayRecharge(zfb_recharge_view);
			}
		});
	   canel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RechargeType.dismiss();
			}
		});
        RechargeType = new Dialog(this,R.style.dialog);
        RechargeType.setContentView(zfb_recharge_view);
        return RechargeType;
    }
    
	private final String ZHAOSHANG = "0101";
	// 建设银行
	private final String JIANSHE = "0102";
	// 工商银行
	private final String GONGSHANG = "0103";
	private String bankType = "CMBCHINA-WAP";
	//银行卡电话充值弹出框
    protected Dialog createPhoneBankRechargeDialog(){
    	RECHARGTYPE = "03";
    	final View phone_bank_recharg_view = factory.inflate(R.layout.account_phone_bank_recharg_dialog, null);
		final Spinner phone_bank_spinner = (Spinner) phone_bank_recharg_view.findViewById(R.id.phone_bank_spinner);
		phone_bank_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// 点击下拉框。。。
						int position = phone_bank_spinner.getSelectedItemPosition();
						if (position == 0) {
							bankType = ZHAOSHANG;
						} else if (position == 1) {
							bankType = JIANSHE;
						} else {
							bankType = GONGSHANG;
						}
					}
					public void onNothingSelected(AdapterView<?> arg0) {
						// 没有任何的触发事件时
					}
				});
		final Button ok = (Button)phone_bank_recharg_view.findViewById(R.id.ok);
		final Button canel =(Button)phone_bank_recharg_view.findViewById(R.id.canel);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShellRWSharesPreferences pre = new ShellRWSharesPreferences(AccountActivity.this, "addInfo");
				String sessionIdStr = pre.getUserLoginInfo("sessionid");
				if (sessionIdStr!=null&&sessionIdStr.equals("")) {
					Intent intentSession = new Intent(AccountActivity.this,UserLogin.class);
					startActivity(intentSession);}
				else{
					beginPhoneBankRecharge(phone_bank_recharg_view);
				}
			}
		});
		canel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RechargeType.dismiss();
			}
		});
        RechargeType = new Dialog(this,R.style.dialog);
        RechargeType.setContentView(phone_bank_recharg_view);    
        return RechargeType;
    }
	private final String ZHENGTU = "0204";
	// 骏网一卡通
	private final String JUNWANG = "0201";
	// 盛大卡
	private final String SHENGDA = "0202";
    
    //游戏点卡充值弹出框
    protected Dialog createGameCardDialog(){
    	RECHARGTYPE = "04";
    	final View game_card_view = factory.inflate(R.layout.account_game_card_dialog, null);
		final Spinner game_card_spinner = (Spinner) game_card_view.findViewById(R.id.game_card_spinner);
		game_card_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {

						// 点击下拉框。。。
						int position = game_card_spinner.getSelectedItemPosition();
						if (position == 0) {
							gameCardType = ZHENGTU;
						} else if (position == 1) {
							gameCardType = SHENGDA;
						} else
							gameCardType = JUNWANG;
					}
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
		final Button ok = (Button)game_card_view.findViewById(R.id.ok);
		final Button canel =(Button)game_card_view.findViewById(R.id.canel);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ShellRWSharesPreferences pre = new ShellRWSharesPreferences(AccountActivity.this, "addInfo");
				String sessionIdStr = pre.getUserLoginInfo("sessionid");
				if (sessionIdStr!=null&&sessionIdStr.equals("")) {
					Intent intentSession = new Intent(AccountActivity.this,UserLogin.class);
					startActivity(intentSession);}
				else{
				beginGameCardRecharge(game_card_view);
				}
			}
		});
		canel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RechargeType.dismiss();
			}
		});
       RechargeType = new Dialog(this,R.style.dialog);
       RechargeType.setContentView(game_card_view);    
      
    	return RechargeType;
    }
//    //第一次DNA充值绑定弹出框
    protected Dialog createBankPhoneCardRegisterDialog(){
    	RECHARGTYPE = "01";//
    	final View phone_bank_card_view = factory.inflate(R.layout.account_bank_card_phone_register_dialog, null);
    	RechargeType = new AlertDialog.Builder(this).setTitle(R.string.bank_cards_recharge).setView(phone_bank_card_view).setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								beginRegisterBankPhoneCard(phone_bank_card_view);
								/* User clicked OK so do some stuff */
							}
						}).setNegativeButton(R.string.cancel,null).create();
    	return RechargeType;
    }
    //支付宝充值
     private void beginAlipayRecharge(View Vi) {

		final EditText zfb_recharge_value = (EditText) Vi.findViewById(R.id.zfb_recharge_value);
		
		final String zfb_recharge_value_string = zfb_recharge_value.getText().toString();
		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(AccountActivity.this, "addInfo");
		String sessionIdStr = pre.getUserLoginInfo("sessionid");
		if (sessionIdStr.equals("")) {
			Intent intentSession = new Intent(AccountActivity.this,UserLogin.class);
			startActivity(intentSession);
		} else {
			if (zfb_recharge_value_string.equals("")|| zfb_recharge_value_string.length() == 0) {
				Toast.makeText(this, "不能为空！", Toast.LENGTH_LONG).show();
			} else {
				// 支付宝充值网络获取
				// 改为线程 2010/7/9陈晨
				RechargePojo rechargepojo =new RechargePojo();;
				rechargepojo.setAmount(zfb_recharge_value_string);
				rechargepojo.setRechargetype(RECHARGTYPE);
				rechargepojo.setCardtype("0300");
				recharge(rechargepojo);
			}
		}
	}
     
     //电话银行充值
     private void beginPhoneBankRecharge(View Vi) {
 		final EditText phone_bank_enter_value = (EditText) Vi.findViewById(R.id.phone_bank_enter_value);
 		Editable phone_bank_value = phone_bank_enter_value.getText();
 		final String phone_bank_value_string = String.valueOf(phone_bank_value);
 		// 手机银行网络连接
 		if ((!(bankType.equals("")) && bankType != null)&& (!(phone_bank_value_string.equals("")) && phone_bank_value_string != null)) {
 		if (Integer.parseInt(phone_bank_value_string) >= 1) {
 			    RechargePojo rechargepojo =new RechargePojo();;
 			    rechargepojo.setCardtype(bankType);
 			    rechargepojo.setAmount(phone_bank_value_string);
 			    rechargepojo.setRechargetype(RECHARGTYPE);
 	            recharge(rechargepojo);
 		} else{
 				Toast.makeText(this, "充值金额至少为1元！", Toast.LENGTH_LONG).show();
 		}
 		} else{
 			
 			Toast.makeText(this, "不能为空！", Toast.LENGTH_LONG).show();
 		}
 	}
      // 银行卡语音充值(DNA)
 	  private void beiginBankCardPhoneOnline(View vi) {
 		
 		EditText bank_card_phone_recharge_value = (EditText) vi.findViewById(R.id.bank_card_phone_recharge_value);
 		final String rechargevalue = bank_card_phone_recharge_value.getText().toString();
 		EditText bank_card_phone_phone_num = (EditText) vi.findViewById(R.id.bank_card_phone_phone_num);
 		final String cardphonenum = bank_card_phone_phone_num.getText().toString();
 		final String cardid = bank_card_phone_bankid.getText().toString();

 		// 充值金额20元以上的整数金额：输入20以上整数数字
 		// 手机号码：数字11位 ；

 		// 银行卡语音充值网络连接
        ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(AccountActivity.this, "addInfo");
 		final String phonenum = shellRW.getUserLoginInfo("phonenum");
 		final String sessionid = shellRW.getUserLoginInfo("sessionid");

 		// 需要传的参数 100 充值金额 106232601047067 银行卡号 acceptphonenum 接电话手机号
 		// ui还缺少银行卡号
 		// fqc edit 7/13/2010 手机卡在线充值的文本格式 充值金额20元以上的整数金额：输入20以上整数数字 手机号码：数字11位
 		// ；
 		if ((!(cardphonenum.equals("")) && cardphonenum != null)&& (!(rechargevalue.equals("")) && rechargevalue != null)&& (!(cardid.equals("")) && cardid != null)) {
 			if (cardphonenum.length() == 11) {
 				if (Integer.parseInt(rechargevalue) >= 20) {

 			String acceptphonenum = cardphonenum;
 			String 	strExpand = " , , , ," + acceptphonenum + ",true";
 			String	bank_card_id = cardid;
 			RechargePojo rechargepojo =new RechargePojo();;
 			rechargepojo.setAmount(rechargevalue);
 			rechargepojo.setCardno(bank_card_id);
 			rechargepojo.setCardtype("0101");
 			rechargepojo.setRechargetype(RECHARGTYPE);
 			rechargepojo.setIswhite("true");
 		    recharge(rechargepojo);
 		    bank_card_phone_recharge_value.setText("");
 		    bank_card_phone_phone_num.setText("");
            } else {
 					Toast.makeText(this, "至少充值金额为20元！", Toast.LENGTH_LONG).show();
 			}
 			} else {
 				Toast.makeText(getBaseContext(), "手机号长度必须为11位！",Toast.LENGTH_LONG).show();
 			}
 		} else
 			Toast.makeText(this, "不能为空！", Toast.LENGTH_LONG).show();
 	}


 	// 银行卡语音充值(未绑定)DNA
 	private void beiginBankCardPhoneNo(View vi) {
 		EditText bank_card_phone_recharge_value = (EditText) vi.findViewById(R.id.bank_card_phone_recharge_value);// 金额
 		final String value = bank_card_phone_recharge_value.getText().toString();
 		EditText bank_card_phone_bankid = (EditText) vi.findViewById(R.id.bank_card_phone_bankid);// 银行卡号
 		final String bankid = bank_card_phone_bankid.getText().toString();
 		final String name = bank_card_phone_name.getText().toString();
 		EditText bank_card_phone_idcard = (EditText) vi.findViewById(R.id.bank_card_phone_phone_idcard);// 身份证号
 		final String idcard = bank_card_phone_idcard.getText().toString();
 		EditText bank_card_phone_home = (EditText) vi.findViewById(R.id.bank_card_phone_phone_home);// 户籍所在地
 		final String home = bank_card_phone_home.getText().toString();
 		EditText bank_card_phone_province = (EditText) vi.findViewById(R.id.bank_card_phone_phone_province);// 所在省
 		final String province = bank_card_phone_province.getText().toString();
 		final String num = bank_card_phone_phone_num.getText().toString();

 		// 银行卡语音充值网络连接
 		// 需要传的参数 100 充值金额 106232601047067 银行卡号 acceptphonenum 接电话手机号
 		// ui还缺少银行卡号
 		// fqc edit 7/13/2010 手机卡在线充值的文本格式 充值金额20元以上的整数金额：输入20以上整数数字 手机号码：数字11位
 		if ((!(num.equals("")) && num != null)
 				&& (!(value.equals("")) && value != null)
 				&& (!(bankid.equals("")) && bankid != null)
 				&& (!(name.equals("")) && name != null)
 				&& (!(idcard.equals("")) && idcard != null)
 				&& (!(home.equals("")) && home != null)
 				&& (!(province.equals("")) && province != null)) {
 			if (num.length() == 11) {
 				if (Integer.parseInt(value) >= 20) {
 					String acceptphonenum = num;
 					strExpand = name + "," + idcard + "," + province
 							+ "," + home + " ," + acceptphonenum + ",false";

 		         	String 		bank_card_id = bankid;
 		         	RechargePojo rechargepojo =new RechargePojo();;
 		         	rechargepojo.setAmount(value);
 		         	rechargepojo.setCardno(bank_card_id);
 		 			rechargepojo.setCardtype("0101");
 		 			rechargepojo.setRechargetype(RECHARGTYPE);
 		 			rechargepojo.setName(name);
 		 	        rechargepojo.setCertid(idcard);
 		 	        rechargepojo.setAddressname(home);
 		 	        rechargepojo.setPhonenum(acceptphonenum);
 		 	        rechargepojo.setBankaddress(province);
 		 	        rechargepojo.setIswhite("false");
 		 		    recharge(rechargepojo);
 				//	bank_phone_card_net(phonenum, value, bank_card_id,sessionid);
 					bank_card_phone_recharge_value.setText("");
 					bank_card_phone_phone_num.setText("");
 					bank_card_phone_bankid.setText("");
 				} else {
 					Toast.makeText(this, "至少充值金额为20元！", Toast.LENGTH_LONG).show();
 				}
 			} else {
 				Toast.makeText(getBaseContext(), "手机号长度必须为11位！",Toast.LENGTH_LONG).show();
 			}
 		} else{
 			Toast.makeText(this, "不能为空！", Toast.LENGTH_LONG).show();
 		}

 	}
 	/**
 	 * 开始注册银行电话卡
 	 * @param vi
 	 */
 	private void beginRegisterBankPhoneCard(View vi) {

 		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(AccountActivity.this, "addInfo");
 		final String phonenum = shellRW.getUserLoginInfo("phonenum");
 		final String sessionid = shellRW.getUserLoginInfo("sessionid");
 		// 得到真实姓名、身份证号、开户银行所在地、开户银行所在地、银行卡号、金额、手机号 20100711
 		EditText bank_card_phone_username = (EditText) vi.findViewById(R.id.bank_card_phone_user_name);
 		final String bank_card_phone_username_string = bank_card_phone_username.getText().toString();

 		EditText bank_card_phone_user_idnum = (EditText) vi.findViewById(R.id.bank_card_phone_user_idnum);
 		final String bank_card_phone_user_idnum_string = bank_card_phone_user_idnum.getText().toString();

 		EditText bank_card_phone_open_bank = (EditText) vi.findViewById(R.id.bank_card_phone_open_bank);
 		final String bank_card_phone_open_bank_string = bank_card_phone_open_bank.getText().toString();

 		EditText bank_card_phone_open_bankuser_address = (EditText) vi.findViewById(R.id.bank_card_phone_open_bankuser_address);
 		final String bank_card_phone_open_bankuser_address_string = bank_card_phone_open_bankuser_address
 				.getText().toString();

 		EditText bank_card_phone_bankid = (EditText) vi.findViewById(R.id.bank_card_phone_bankid);
 		final String bank_card_phone_bankid_string = bank_card_phone_bankid.getText().toString();

 		EditText bank_card_phone_recharge_value = (EditText) vi.findViewById(R.id.bank_card_phone_recharge_value);
 		final String bank_card_phone_recharge_value_string = bank_card_phone_recharge_value
 				.getText().toString();

 		EditText bank_card_phone_phone_num = (EditText) vi.findViewById(R.id.bank_card_phone_phone_num);
 		final String bank_card_phone_phone_num_string = bank_card_phone_phone_num.getText().toString();

 	  strExpand = bank_card_phone_username_string + ","
 				+ bank_card_phone_user_idnum_string + ","
 				+ bank_card_phone_open_bank_string + ","
 				+ bank_card_phone_open_bankuser_address_string + ","
 				+ bank_card_phone_phone_num_string + ",false";

 	    String 	bank_card_id = bank_card_phone_bankid_string;
 	        RechargePojo rechargepojo =new RechargePojo();;
 	        rechargepojo.setName(bank_card_phone_username_string);
 	        rechargepojo.setCertid(bank_card_phone_user_idnum_string);
 	        rechargepojo.setCardtype("0101");
 	        rechargepojo.setBankaddress(bank_card_phone_open_bankuser_address_string);
 	        rechargepojo.setPhonenum(bank_card_phone_phone_num_string);
 	        rechargepojo.setAmount(bank_card_phone_recharge_value_string);
			rechargepojo.setCertid(bank_card_id);
			rechargepojo.setRechargetype(RECHARGTYPE);
		    recharge(rechargepojo);
 	}

 	/**
 	 *  手机卡充值
 	 * @param view
 	 */
 	private void beginPhoneCardRecharge(View view) {

 		final EditText phone_card_rechargecard_info = (EditText) view.findViewById(R.id.phone_card_rechargecard_info);
 		final String phone_card_rechargecard_info_string = phone_card_rechargecard_info.getText().toString();
 		final EditText phone_card_rechargecard_password = (EditText) view.findViewById(R.id.phone_card_rechargecard_password);
 		final String phone_card_rechargecard_password_string = phone_card_rechargecard_password.getText().toString();
 		// 手机充值卡充值
 		// 参数含义：0203 充值卡类型 10 充值钱数 5000充值总额 10623260104706723 充值卡号
 		// 261324590869999653 充值密码 y00003银行标识默认
 		// ui缺少充值的钱数
 		if ((!(phone_card_rechargecard_info_string.equals("")) && phone_card_rechargecard_info_string != null)
 				&& (!(phone_card_rechargecard_password_string.equals("")) && phone_card_rechargecard_password_string != null)) {
 			if (isCardString(phone_card_rechargecard_info_string)) {
 				// 改为线程 陈晨 200/7/9
 			RechargePojo rechargepojo =new RechargePojo();;
 			  rechargepojo.setRechargetype(RECHARGTYPE);
 			  rechargepojo.setCardtype(phoneCardType);
 			  rechargepojo.setAmount(phoneCardValue);
 			  rechargepojo.setCardno(phone_card_rechargecard_info_string);
 			  rechargepojo.setCardpwd(phone_card_rechargecard_password_string);
 			  recharge(rechargepojo);
 			} else {
 				Toast.makeText(this, "充值卡序列号应为数字或字母！", Toast.LENGTH_LONG).show();
 			}
 		} else
 			Toast.makeText(this, "不能为空！", Toast.LENGTH_LONG).show();
 	}

 	private void beginGameCardRecharge(View Vi) {

 	final  EditText game_card_number = (EditText) Vi.findViewById(R.id.game_card_number);
 	String	game_card_number_string = game_card_number.getText().toString();
 	final  EditText game_card_password = (EditText) Vi.findViewById(R.id.game_card_password);
 	String	game_card_password_string = game_card_password.getText().toString();
 	final  EditText game_card_total_value = (EditText) Vi.findViewById(R.id.game_card_total_value);
 	String game_card_total_value_string = game_card_total_value.getText().toString();
 		
 		if (game_card_number_string.equals("")|| game_card_password_string.equals("")|| game_card_total_value_string.equals("")) {
 			Message msg = new Message();
 			
 		} else if (isCardString(game_card_number_string)) {
 			RechargePojo rechargepojo =new RechargePojo();;
 			rechargepojo.setRechargetype(RECHARGTYPE);
 			rechargepojo.setCardtype(gameCardType);
 			rechargepojo.setAmount(game_card_total_value_string);
 			rechargepojo.setCardno(game_card_number_string);
 			rechargepojo.setCardpwd(game_card_password_string);
 			recharge(rechargepojo);
 		} else {
 			Toast.makeText(getBaseContext(), "卡号格式输入不正确！", Toast.LENGTH_LONG).show();
 		}
 	}
   private boolean isCardString(String cardNumber) {
 		int length = cardNumber.length();
 		boolean isRight = true;
 		for (int i = 0; i < length - 1; i++) {
 			if (cardNumber.charAt(i) < '0'
 					|| (cardNumber.charAt(i) > '9' && cardNumber.charAt(i) < 'A')
 					|| (cardNumber.charAt(i) > 'Z' && cardNumber.charAt(i) < 'a')
 					|| (cardNumber.charAt(i) > 'z')) {
 				isRight = false;
 			}
 		}
 		return isRight;

 	}
	/**
 	 * DNA充值账户绑定查询
 	 */
 	public void  checkDNA() {
 	 
 		RECHARGTYPE = "01";//为chenckDNA
 		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(AccountActivity.this, "addInfo");
 		final String sessionId = pre.getUserLoginInfo("sessionid");
 		final String mobile = pre.getUserLoginInfo("phonenum");
 		phonenum = mobile;
 		final String userno = pre.getUserLoginInfo("userno");
 		if(sessionId==null||sessionId.equals("")){
 				Intent intentSession = new Intent(AccountActivity.this,UserLogin.class);
 				startActivityForResult(intentSession,0);
 		}else{
 		showDialog(0);
 		new Thread(new Runnable() {
 			@Override
 			public void run() {
 			String error_code = "00";
 			String message = "";
 				try {
	 				 re = QueryDNAInterface.getInstance().queryDNA(mobile, sessionId, userno);
	 				 JSONObject	obj = new JSONObject(re);
	 				 error_code = obj.getString("error_code");
	 				 message = obj.getString("message");
 				} catch (Exception e) {
 					e.printStackTrace();
 				}
 			    if(error_code.equals("0047")){
 			    handler.post(new Runnable() {
					public void run() {
						createBankCardPhoneDialogNo().show();
					}
				});
 			    }else if(error_code.equals("0000")){
 				    handler.post(new Runnable() {
 						public void run() {
 							dialogDNA(re);
 						}
 					});
 			    }else{
 			    	handler.handleMsg(error_code, message);
 			    }
 			   progressdialog.dismiss();
 			}
 		}).start();
 		}
 	}

 	/**
 	 * 创建dna对话框
 	 * @版本：
 	 */
 	private String name="";
 	private String certid="";
 	private String bankAddress="";
 	private String certAddress="";
 	public void dialogDNA(String str) {
 		String bindState = "";
 		try {
 			JSONObject obj = new JSONObject(str);
 			bindState = obj.getString("bindstate");
 			bankCardNo = obj.getString("bankcardno");
 			name = obj.getString("name");
 			certid =obj.getString("certid");
 			bankAddress = obj.getString("bankaddress");
 			certAddress = obj.getString("addressname");			
 		} catch (JSONException e) {
 			e.printStackTrace();
 		}
 		if (bindState.equals("1")) {// 已经绑定

 			createBankCardPhoneDialog().show();
 		}else if(bindState.equals("0")){
 			createBankCardPhoneDialogNo().show();
 		}
 	}
    //充值
    private void recharge(final RechargePojo rechargepojo) {
    	ShellRWSharesPreferences pre = new ShellRWSharesPreferences(AccountActivity.this, "addInfo");
		sessionId = pre.getUserLoginInfo("sessionid");
		phonenum = pre.getUserLoginInfo("phonenum");
		userno = pre.getUserLoginInfo("userno");
        showDialog(0); 
	    new Thread(new Runnable() {
			
			@Override
			public void run() {
				String error_code = "00";
			    message = "";
				// TODO Auto-generated method stub
				try{
						 rechargepojo.setSessionid(sessionId);
					     rechargepojo.setPhonenum(phonenum);
						 rechargepojo.setUserno(userno);
						 String re = RechargeInterface.getInstance().recharge(rechargepojo);
						 JSONObject	obj = new JSONObject(re);
						 error_code = obj.getString("error_code");
						 message = obj.getString("message");
						 if(error_code.equals("0000")){
							 if(RECHARGTYPE.equals("05")){
								 url = obj.getString("return_url"); 
							 }else if(RECHARGTYPE.equals("03")){
								 url = obj.getString("reqestUrl");  
							 }
						 }
					 }catch(JSONException e){
						e.printStackTrace();
					 }
					 if(error_code.equals("001400")){
						 handler.post(new Runnable() {
							public void run() {
								createBankPhoneCardRegisterDialog().show();	
							}
						 });
					 }else{
						 handler.handleMsg(error_code, message);
					 }
			     progressdialog.dismiss();
			}
		}).start();
		
	}
    //添加充值方式数据
	private List<Map<String, Object>> setContentForMainList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);
		// 银行卡电话充值
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TITLE, getString(R.string.bank_cards_recharge));
		map.put(PICTURE,R.drawable.recharge_bank);
		map.put(ISHANDINGFREE,TextView.VISIBLE);
		list.add(map);
		// 支付宝充值
		map = new HashMap<String, Object>();
		map.put(TITLE, getString(R.string.zhfb_cards_recharge));
		map.put(PICTURE, R.drawable.recharge_alipay);
		map.put(ISHANDINGFREE,TextView.VISIBLE);
		list.add(map);
		// 手机话费充值卡
		map = new HashMap<String, Object>();
		map.put(TITLE, getString(R.string.phone_cards_recharge));
		map.put(PICTURE, R.drawable.recharge_phone);
		map.put(ISHANDINGFREE,TextView.INVISIBLE);
		list.add(map);
		// 手机银行充值
//		map = new HashMap<String, Object>();//手机银行充值暂且不使用
//		map.put(TITLE, getString(R.string.phone_bank_cards_recharge));
//		map.put(PICTURE, R.drawable.recharge_phonebank);
//		list.add(map);
		// 游戏点卡充值
		map = new HashMap<String, Object>();
		map.put(PICTURE, R.drawable.recharge_game);
		map.put(TITLE, getString(R.string.game_cards_recharge));
		map.put(ISHANDINGFREE,TextView.INVISIBLE);
		list.add(map);
		// 电脑网上充值
	    return list;
	}
	  
	
    /**
     * 重写放回建
     */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		   case 4:
	        ExitDialogFactory.createExitDialog(this);
           break;
		}
		return false;
	}
	/**
 	 * intent回调函数
 	 * 用户登录过后直接弹出充值框
 	 */
 	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 		switch (resultCode) {
 		case RESULT_OK:
 		//	Log.v("back", "back");
 			TextView AccountType = (TextView)findViewById(R.id.account_recharge_listview_text);
 			String textString = AccountType.getText().toString();
 		      	if (getString(R.string.bank_cards_recharge).equals(textString)) {
 		      		checkDNA();
 				} else if (getString(R.string.phone_cards_recharge).equals(textString)) {
 				  createPhoneRechargeCardDialog().show();
 				} else if (getString(R.string.zhfb_cards_recharge).equals(textString)) {
 				  createAlipayDialog().show();
 				} else if (getString(R.string.phone_bank_cards_recharge).equals(textString)) {
 				  createPhoneBankRechargeDialog().show();
 				} else if (getString(R.string.game_cards_recharge).equals(textString)) {
 				  createGameCardDialog().show();
 		        } 
 			break;
 		}
 	}
 	    
 	//主列表Item点击事件处理类
    public class AccountMainItemListener implements OnItemClickListener{
	    private Context context;
	    public AccountMainItemListener(Context context){
	    	    this.context = context;
	    }
		@Override
	    public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
		    TextView AccountType = (TextView) view.findViewById(R.id.account_recharge_listview_text);
			String textString = AccountType.getText().toString();
			if (context.getString(R.string.bank_cards_recharge).equals(textString)) {
			  checkDNA();
			} else if (context.getString(R.string.phone_cards_recharge).equals(textString)) {
			  createPhoneRechargeCardDialog().show();
			} else if (context.getString(R.string.zhfb_cards_recharge).equals(textString)) {
			  createAlipayDialog().show(); 
			} else if (context.getString(R.string.phone_bank_cards_recharge).equals(textString)) {
			  createPhoneBankRechargeDialog().show();
			} else if (context.getString(R.string.game_cards_recharge).equals(textString)) {
			  createGameCardDialog().show();
	        } 
		}
	}
   	    /**
    	 * 网络连接提示框
    	 */
    	protected Dialog onCreateDialog(int id) {
    		switch (id) {
    		case 0: {
    			progressdialog = new ProgressDialog(this);
    			// progressdialog.setTitle("Indeterminate");
    			progressdialog.setMessage("网络连接中...");
    			progressdialog.setIndeterminate(true);
    		}
    			progressdialog.setCancelable(true);
    			return progressdialog;
    		}
    		return null;
    	} 
		@Override
		public void errorCode_0000() {
			// TODO Auto-generated method stub
		if(RECHARGTYPE.equals("05")||RECHARGTYPE.equals("03")){
			PublicMethod.openUrlByString(AccountActivity.this, url);
		}
		 RechargeType.dismiss();
		 Toast.makeText(AccountActivity.this,message ,Toast.LENGTH_SHORT).show();
		}
		@Override
		public void errorCode_000000() {
		}
		@Override
		public Context getContext() {
			// TODO Auto-generated method stub
			return this;
		}
}