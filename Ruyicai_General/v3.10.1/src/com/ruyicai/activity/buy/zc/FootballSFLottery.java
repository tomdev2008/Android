package com.ruyicai.activity.buy.zc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.jc.explain.zq.JcExplainActivity;
import com.ruyicai.activity.buy.zc.pojo.TeamInfo;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.gift.GiftActivity;
import com.ruyicai.activity.join.JoinStartActivity;
import com.ruyicai.constant.Constants;
import com.ruyicai.controller.Controller;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.ExplainInterface;
import com.ruyicai.net.newtransaction.FootballLotteryAdvanceBatchcode;
import com.ruyicai.net.transaction.FootballInterface;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

/**
 * 足彩-胜负彩
 * @author win
 *
 */
public class FootballSFLottery extends FootballFourteen implements HandlerMsg {
	private String codeStr;
	private RadioButton check;
	private RadioButton joinCheck;

	MyHandler handlertouzhu = new MyHandler((HandlerMsg) this);// 自定义handler
	String lotno = Constants.LOTNO_SFC;
	MyHandler touzhuhandler = new MyHandler(this);
	int lieNum;
	/**add by yejc 20130425 start*/
	private boolean isaWait = false;
	private SpannableString[] spanBactchCodes;
	/**add by yejc 20130425 end*/
	
	String inflater = Context.LAYOUT_INFLATER_SERVICE;
	LayoutInflater layoutInflater;
	ScrollView mHScrollView;
	LinearLayout buyView;
	ListView mlist;
	TextView mTextSumMoney;
	List<Map<String, Object>> list;
	int iProgressBeishu = 1;
	ImageButton sfc_btn_touzhu;
	private Vector<BallTable> ballTables = new Vector<BallTable>();

	int index;
	// 进度条
	private static final int DIALOG1_KEY = 0;
	private ProgressDialog progressdialog;
	private JSONObject obj;
	String re;
	private Vector<TeamInfo> teamInfos = new Vector<TeamInfo>();
	private String[] bactchCodes;// 预售期的期号数组
	private List<Object> bactchArray = new ArrayList<Object>();// 这个list中存放预售期期号和截止时间的信息
	String advanceBatchCodeData;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBatchCodeView();
		showDialog(DIALOG1_KEY);
		getZCAdvanceBatchCodeData(Constants.LOTNO_SFC);
	}

	/**
	 * 消息处理函数
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常！", Toast.LENGTH_LONG)
						.show();
				break;
			case 1:
				progressdialog.dismiss();
				initList();
				break;
			case 2:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "发生异常！", Toast.LENGTH_LONG)
						.show();
				break;
			case 3:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "传入参数不是JSON格式！",
						Toast.LENGTH_LONG).show();
				break;
			case 4:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "系统正在维护请稍候再试",
						Toast.LENGTH_LONG).show();
				break;
			case 5:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "传入期号错误！", Toast.LENGTH_LONG)
						.show();
				break;

			case 6:
				progressdialog.dismiss();
				FootballContantDialog
						.alertIssueNOFQueue(FootballSFLottery.this);
				break;
			case 7:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "转码异常！", Toast.LENGTH_LONG)
						.show();
				break;
			case 8:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常！", Toast.LENGTH_LONG)
						.show();
				break;
			case 9:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注失败余额不足！", Toast.LENGTH_LONG)
						.show();

				break;
			case 10:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "获取信息失败！", Toast.LENGTH_LONG)
						.show();
				break;
			case 11:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "彩票投注中！", Toast.LENGTH_LONG)
						.show();
				break;
			case 12:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注成功，出票成功！",
						Toast.LENGTH_LONG).show();

				for (int i = 0; i < ballTables.size(); i++) {
					ballTables.get(i).clearAllHighlights();
				}
				Intent intent = new Intent(UserLogin.SUCCESS);
				sendBroadcast(intent);
				break;
			case 14:
				progressdialog.dismiss();
				if (isFinishing() == false) {
					PublicMethod.showDialog(FootballSFLottery.this);
				}
				for (int i = 0; i < ballTables.size(); i++) {
					ballTables.get(i).clearAllHighlights();
				}
				changeTextSumMoney(getZhuShu());
				Intent intent1 = new Intent(UserLogin.SUCCESS);
				sendBroadcast(intent1);
				break;
			case 15:
				progressdialog.dismiss();
				// 30分钟后再次登录 陈晨 20100719
				Intent intentSession = new Intent(FootballSFLottery.this,
						UserLogin.class);
				startActivity(intentSession);
				break;
			case 16:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常！", Toast.LENGTH_LONG)
						.show();
				break;
			case 17:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注失败！", Toast.LENGTH_LONG)
						.show();
				break;
			case 18:
				// 将图片的背景设置回原来的图案表示可点击
				sfc_btn_touzhu.setImageResource(R.drawable.imageselecter);
				break;
			case 19:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "没有期号可以投注！", Toast.LENGTH_LONG)
						.show();
				break;
			case 20:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "传入场次错误！", Toast.LENGTH_LONG)
						.show();
				break;
			case 21:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "返回对阵为空！", Toast.LENGTH_LONG)
						.show();
				break;
			case 22:
				progressdialog.dismiss();
				alertZC(re);
				break;
			case 23:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注期号不存在，或已过期！",
						Toast.LENGTH_LONG).show();
				break;
			case 24:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), msg.obj + "",
						Toast.LENGTH_SHORT).show();
				break;
			case 25:
				progressdialog.dismiss();
				if (isOne) {
					isOne = false;
					getTeamInfo(0);
				} else {
					showBatchcodesDialog();
				}
				break;
			}
		}
	};

	static int iScreenWidth;

	/**
	 * 初始化列表
	 */
	public void initList() {
		mlist = (ListView) findViewById(R.id.buy_footballlottery_list);
		list = getListForMainListAdapter();
		ballTables.clear();// 每次初始化足彩选区列表就清空BallTable的 Vector中的数据
		ListViewAdapter adapter = new ListViewAdapter(this, list);
		mlist.setAdapter(adapter);
	}

	public void createVeiw() {
		sfc_btn_touzhu = (ImageButton) findViewById(R.id.buy_footballlottery_img_touzhu);
		sfc_btn_touzhu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				beginTouZhu(); // 1表示当前为单式
			}
		});
	}

	/**
	 * 网络连接提示框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			progressdialog = new ProgressDialog(this);

			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);

			return progressdialog;
		}
		}
		return null;
	}

	/**
	 * 假设数组的id为ai 每个小球的id为ai*10+小球.Resid 这样就能保证小球id的唯一性
	 */
	/** 小球起始id */
	public static final int SHENGFC_START_ID = 0x83000001;
	public int iAllBallWidth;
	public View views[] = new View[14];
	
	//add by yejc 20130325
	private class ListViewAdapter extends BaseAdapter {
		
		private List<Map<String, Object>> mList;
		private LayoutInflater mInflater; // 扩充主列表布局
		ViewHolder holder;

		public ListViewAdapter(Context context, List<Map<String, Object>> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.buy_zc_sfc_main_listview_item,
						parent, false);
				holder = new ViewHolder();
				holder.type = (TextView)convertView.findViewById(R.id.game_type);
				holder.date = (TextView)convertView.findViewById(R.id.game_date);
				holder.number = (TextView)convertView.findViewById(R.id.game_num);
				holder.homeTeam = (TextView)convertView.findViewById(R.id.home_team);
				holder.guestTeam = (TextView)convertView.findViewById(R.id.guest_team);
				holder.analysis = (TextView)convertView.findViewById(R.id.zc_sfc_analysis);
				holder.layout = (LinearLayout) convertView.findViewById(R.id.shengfucai_layout);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.type.setText((String) mList.get(position).get(TYPE));
			holder.type.getPaint().setFakeBoldText(true); 

			holder.date.setText((String) mList.get(position).get(DATE));
			holder.number.setText((String) mList.get(position).get(TEAM_ID));
			holder.homeTeam.setText((String) mList.get(position).get(TEAM1)+"\n"+"(主)");
			holder.homeTeam.getPaint().setFakeBoldText(true); 
			holder.guestTeam.setText((String) mList.get(position).get(TEAM2)+"\n"+"(客)");
			holder.guestTeam.getPaint().setFakeBoldText(true); 
			
			int START_ID = SHENGFC_START_ID + position * 3;
			if (isaWait) {
				BallTable shengfcRow = makeBallTable(holder.layout, R.id.shengfucai_ball,
						aResIdForWait, START_ID, (String) mList.get(position).get(RESULT));
				if (ballTables.size() < mList.size()) {
					ballTables.add(shengfcRow);
				}
			} else {
				BallTable shengfcRow = makeBallTable(holder.layout, R.id.shengfucai_ball,
						aResId, START_ID, (String) mList.get(position).get(RESULT));
				if (ballTables.size() < mList.size()) {
					ballTables.add(shengfcRow);
				}
				Vector<OneBallView> BallViews = shengfcRow.getBallViews();
				for (int i = 0; i < BallViews.size(); i++) {
					final OneBallView ball = BallViews.get(i);
					ball.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							ball.changeBallColor();
							changeTextSumMoney(getZhuShu());
							
							setTeamNum(getTeamNum(ballTables, false));
						}
					});
				}
			}
			
			final int index = position;
			holder.analysis.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Constants.currentTickType = "zuCai";
					Intent intent = new Intent(FootballSFLottery.this, JcExplainActivity.class);
					String event = Constants.LOTNO_SFC + "_" + mList.get(index).get(ISSUE)
							+ "_" + mList.get(index).get(TEAM_ID);
					intent.putExtra("event", event);
					intent.putExtra(LOTNO_ZC, LOTNO_ZC);;
					FootballSFLottery.this.startActivity(intent);
				}
			});
			
			return convertView;
		}
		
	}
	
	private class ViewHolder {
		TextView type;
		TextView date;
		TextView number;
		TextView homeTeam;
		TextView guestTeam;
		TextView analysis;
		LinearLayout layout;
	}
	//end

	/**
	 * 主列表中相应的数据
	 */
	private List<Map<String, Object>> getListForMainListAdapter() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < teamInfos.size(); i++) {
			map = new HashMap<String, Object>();
			map.put(TEAM1, teamInfos.get(i).hTeam);
			map.put(TEAM2, teamInfos.get(i).vTeam);
			map.put(SCORES1, teamInfos.get(i).hRankNum);
			map.put(SCORES2, teamInfos.get(i).vRankNum);
			
			//add by yejc 20130325
			map.put(TYPE, teamInfos.get(i).type);
			map.put(DATE, teamInfos.get(i).date);
			map.put(TEAM_ID, teamInfos.get(i).teamId);
			map.put(RESULT, teamInfos.get(i).result);
			map.put(ISSUE, teamInfos.get(i).issue);
			//end
			list.add(map);
		}
		return list;
	}

	/**
	 * 胜负彩的注数计算方法
	 */
	private int getZhuShu() {
		int iReturnValue = 0;
		for (int i = 0; i < ballTables.size(); i++) {
			if (i != 0) {
				iReturnValue *= ballTables.get(i).getHighlightBallNums();
			} else {
				iReturnValue = ballTables.get(i).getHighlightBallNums();
			}
		}
		return iReturnValue;
	}

	/**
	 * 获取注码
	 */
	public String getZhuma() {
		StringBuffer t_str = new StringBuffer();
		for (int i = 0; i < ballTables.size(); i++) {
			int balls[] = ballTables.get(i).getHighlightBallNOs();
			for (int j = 0; j < balls.length; j++) {
				t_str.append(balls[j]);
			}
			if (i < ballTables.size() - 1) {
				t_str.append(",");
			}
		}
		return t_str.toString();
	}

	public void beginTouZhu() {
		int iZhuShu = getZhuShu();// 注数是注数*倍数的结果
		RWSharedPreferences pre = new RWSharedPreferences(
				FootballSFLottery.this, "addInfo");
		sessionid = pre.getStringValue("sessionid");
		FootballLottery.tabPosition = 0;
		if (sessionid == null || sessionid.equals("")) {
			Intent intentSession = new Intent(FootballSFLottery.this,
					UserLogin.class);
			startActivityForResult(intentSession, 0);
			sfc_btn_touzhu.setClickable(true);
		} else {
			if (isTouZhu()) {
				Toast.makeText(FootballSFLottery.this, "请至少选择一注！",
						Toast.LENGTH_SHORT).show();
			} else if (iZhuShu * 2 > 20000) {
				DialogExcessive();
			} else {
				initBetPojo();
				toorderdetail();
			}
		}
	}

	void toorderdetail() {
		ApplicationAddview app = (ApplicationAddview) getApplicationContext();
		app.setPojo(betPojo);
		Intent intent = new Intent(FootballSFLottery.this,
				Footballorderdail.class);
		intent.putExtra("tpye", "zc");
		intent.putExtra("zhuma", getZhuma());
		startActivity(intent);
	}

	public boolean isTouZhu() {
		for (int i = 0; i < ballTables.size(); i++) {
			if (ballTables.get(i).getHighlightBallNums() == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 分析数据解析
	 * 
	 * @param re
	 */
	private void alertZC(String re) {
		// 解析数据
		JSONObject re1;
		JSONObject obj;
		String hTeam8 = "";
		String vTeam8 = "";
		String avgOdds = "";
		String title = "";
		try {
			re1 = new JSONObject(re);
			obj = re1.getJSONObject("value");
			hTeam8 = obj.getString("HTeam8");
			vTeam8 = obj.getString("VTeam8");
			avgOdds = obj.getString("avgOdds");
			title += obj.getString("HTeam");
			title += "VS";
			title += obj.getString("VTeam");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		LayoutInflater factory = LayoutInflater.from(this);
		View view = factory.inflate(R.layout.football_anaylese, null);
		TextView row1_1 = (TextView) view.findViewById(R.id.sheng_zhishu);
		TextView row1_2 = (TextView) view
				.findViewById(R.id.zhudui_jinshi_sheng);
		TextView row1_3 = (TextView) view.findViewById(R.id.kedui_jinshi_sheng);
		TextView row2_1 = (TextView) view.findViewById(R.id.ping_zhishu);
		TextView row2_2 = (TextView) view.findViewById(R.id.zhudui_jinshi_ping);
		TextView row2_3 = (TextView) view.findViewById(R.id.kedui_jinshi_ping);
		TextView row3_1 = (TextView) view.findViewById(R.id.fu_zhishu);
		TextView row3_2 = (TextView) view.findViewById(R.id.zhudui_jinshi_fu);
		TextView row3_3 = (TextView) view.findViewById(R.id.kedui_jinshi_fu);
		TextView row4_1 = (TextView) view
				.findViewById(R.id.zhudui_jinshi_jinqiu);
		TextView row4_2 = (TextView) view
				.findViewById(R.id.kedui_jinshi_jinqiu);
		if (!hTeam8.equals("")) {
			String hteam[] = hTeam8.split("\\|");
			row1_2.setText(hteam[0]);
			row2_2.setText(hteam[1]);
			row3_2.setText(hteam[2]);
			row4_1.setText(hteam[3] + "|" + hteam[4]);
		}
		if (!vTeam8.equals("")) {
			String vteam[] = vTeam8.split("\\|");
			row1_3.setText(vteam[0]);
			row2_3.setText(vteam[1]);
			row3_3.setText(vteam[2]);
			row4_2.setText(vteam[3] + "|" + vteam[4]);
		}
		if (!avgOdds.equals("")) {
			String avg[] = avgOdds.split("\\|");
			row1_1.setText(avg[0].substring(1));
			row2_1.setText(avg[1].substring(1));
			row3_1.setText(avg[2].substring(1));
		}
		Builder dialog = new AlertDialog.Builder(this).setTitle(title)
				.setView(view)
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		dialog.show();
	}

	/**
	 * 获取对阵矩阵
	 */
	public void getData(final String lotno, final String batchCode) {
		showDialog(DIALOG1_KEY);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String error_code = "00";
				re = FootballInterface.getInstance()
						.getZCData(lotno, batchCode);
				try {
					obj = new JSONObject(re);
					error_code = obj.getString("error_code");
					if (error_code.equals("000000")) {
						teamInfos.clear();
						JSONArray value = obj.getJSONArray("value");
						for (int i = 0; i < value.length(); i++) {
							JSONObject re = value.getJSONObject(i);
							TeamInfo team = new TeamInfo();
							team.hTeam = re.getString("HTeam");
							team.vTeam = re.getString("VTeam");
							String rank = re.getString("leagueRank");
							team.num = re.getString("num");
							if (rank != null && !rank.equals("")) {
								String str[] = rank.split("\\|");
								team.hRankNum = str[0];
								team.vRankNum = str[1];
							}
							
							//add by yejc 20130325
							team.date = re.getString("date");
							team.type = re.getString("name");
							team.result = re.getString("avgOdds");
							team.issue = batchCode;
							team.teamId = re.getString("tempId");
							//end
							teamInfos.add(team);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (error_code.equals("00")) {
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);

				} else if (error_code.equals("000000")) {
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				} else if (error_code.equals("100000")) {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				} else if (error_code.equals("200001")) {
					Message msg = new Message();
					msg.what = 3;
					handler.sendMessage(msg);
				} else if (error_code.equals("200002")) {
					Message msg = new Message();
					msg.what = 4;
					handler.sendMessage(msg);
				} else if (error_code.equals("200003")) {
					Message msg = new Message();
					msg.what = 5;
					handler.sendMessage(msg);
				} else if (error_code.equals("200005")) {
					Message msg = new Message();
					msg.what = 6;
					handler.sendMessage(msg);
				} else if (error_code.equals("200008")) {
					Message msg = new Message();
					msg.what = 7;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}

	/** 获取分析的数据 */
	public void getInfo(final int index) {
		showDialog(DIALOG1_KEY);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String error_code = "00";
				re = FootballInterface.getInstance().getZCInfo(qihaoxinxi[2],
						batchCode, teamInfos.get(index).num);
				try {
					obj = new JSONObject(re);
					error_code = obj.getString("error_code");
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (error_code.equals("00")) {
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				} else if (error_code.equals("000000")) {
					Message msg = new Message();
					msg.what = 22;
					handler.sendMessage(msg);
				} else if (error_code.equals("100000")) {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				} else if (error_code.equals("200001")) {
					Message msg = new Message();
					msg.what = 3;
					handler.sendMessage(msg);
				} else if (error_code.equals("200002")) {
					Message msg = new Message();
					msg.what = 4;
					handler.sendMessage(msg);
				} else if (error_code.equals("200003")) {
					Message msg = new Message();
					msg.what = 5;
					handler.sendMessage(msg);
				} else if (error_code.equals("200005")) {
					Message msg = new Message();
					msg.what = 6;
					handler.sendMessage(msg);
				} else if (error_code.equals("200008")) {
					Message msg = new Message();
					msg.what = 7;
					handler.sendMessage(msg);
				} else if (error_code.equals("200004")) {
					Message msg = new Message();
					msg.what = 20;
					handler.sendMessage(msg);
				} else if (error_code.equals("200006")) {
					Message msg = new Message();
					msg.what = 21;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}

	protected void onDestroy() {
		super.onDestroy();
		for (int i = 0; i < ballTables.size(); i++) {
			ballTables.get(i).clearAllHighlights();
		}
	}

	private void initBetPojo() {
		RWSharedPreferences pre = new RWSharedPreferences(
				FootballSFLottery.this, "addInfo");
		sessionid = pre.getStringValue("sessionid");
		phonenum = pre.getStringValue("phonenum");
		userno = pre.getStringValue("userno");
		betPojo.setPhonenum(phonenum);
		betPojo.setSessionid(sessionid);
		betPojo.setUserno(userno);
		betPojo.setBet_code(getZhuma());
		betPojo.setLotno(lotno);
		betPojo.setBatchnum("1");
		betPojo.setBatchcode(batchCode);
		betPojo.setLotmulti(String.valueOf(iProgressBeishu));
		betPojo.setBettype("bet");
		betPojo.setAmount(getZhuShu() * 200 + "");
		betPojo.setZhushu(getZhuShu() + "");
	}

	public void toActivity(String zhuma) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
			objStream.writeObject(betPojo);
		} catch (IOException e) {
			return; // should not happen, so donot do error handling
		}

		Intent intent = new Intent(FootballSFLottery.this, GiftActivity.class);
		intent.putExtra("info", byteStream.toByteArray());
		intent.putExtra("zhuma", zhuma);
		startActivity(intent);
	}

	/**
	 * 单复式投注调用函数
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	public void alert(String string, final String zhuma) {
		codeStr = zhuma;
		isGift = false;
		isJoin = false;
		isTouzhu = true;
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.alert_dialog_touzhu, null);
		LinearLayout layout = (LinearLayout) v
				.findViewById(R.id.alert_dialog_touzhu_linear_qihao_beishu);
		layout.setVisibility(LinearLayout.GONE);
		final AlertDialog dialog = new AlertDialog.Builder(this).setTitle(
				"您选择的是").create();
		dialog.show();
		TextView text = (TextView) v
				.findViewById(R.id.alert_dialog_touzhu_text_one);
		text.setText(string);
		TextView textZhuma = (TextView) v
				.findViewById(R.id.alert_dialog_touzhu_text_zhuma);
		textZhuma.setText(zhuma);
		Button cancel = (Button) v
				.findViewById(R.id.alert_dialog_touzhu_button_cancel);
		Button ok = (Button) v.findViewById(R.id.alert_dialog_touzhu_button_ok);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
				initBetPojo();
				if (isGift) {
					toActivity(zhuma);
				} else if (isJoin) {
					toJoinActivity();
				} else if (isTouzhu) {
					touZhuNet();
				}
			}
		});
		check = (RadioButton) v.findViewById(R.id.alert_dialog_touzhu_check);
		joinCheck = (RadioButton) v.findViewById(R.id.alert_dialog_join_check);
		RadioButton touzhuCheck = (RadioButton) v
				.findViewById(R.id.alert_dialog_touzhu1_check);
		touzhuCheck.setChecked(true);
		check.setPadding(50, 0, 0, 0);
		check.setButtonDrawable(R.drawable.check_select);
		// 实现记住密码 和 复选框的状态
		check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				isGift = isChecked;
			}
		});
		joinCheck.setPadding(50, 0, 0, 0);
		joinCheck.setButtonDrawable(R.drawable.check_select);
		// 实现记住密码 和 复选框的状态
		joinCheck
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						isJoin = isChecked;
					}
				});
		touzhuCheck.setPadding(50, 0, 0, 0);
		touzhuCheck.setButtonDrawable(R.drawable.check_select);
		// 实现记住密码 和 复选框的状态
		touzhuCheck
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						isTouzhu = isChecked;
					}
				});

		dialog.getWindow().setContentView(v);
	}

	/**
	 * 投注联网
	 */
	public void touZhuNet() {
		Controller.getInstance(FootballSFLottery.this).doBettingAction(touzhuhandler, betPojo);
	}

	public void toJoinActivity() {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
			objStream.writeObject(betPojo);
		} catch (IOException e) {
			return; // should not happen, so donot do error handling
		}

		Intent intent = new Intent(FootballSFLottery.this,
				JoinStartActivity.class);
		intent.putExtra("info", byteStream.toByteArray());
		startActivity(intent);

	}

	@Override
	public void errorCode_0000() {
		for (int i = 0; i < ballTables.size(); i++) {
			ballTables.get(i).clearAllHighlights();
		}
		String lotnoString = PublicMethod.toLotno(lotno);
		PublicMethod.showDialog(FootballSFLottery.this, lotnoString + codeStr);
	}

	@Override
	public void errorCode_000000() {
	}

	@Override
	public Context getContext() {
		return this;
	}

	private void showBatchcodesDialog() {
		AlertDialog batchCodedialog = new AlertDialog.Builder(
				FootballSFLottery.this).setTitle("胜负彩预售期")
				.setItems(spanBactchCodes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						/* User clicked so do some stuff */
						getTeamInfo(which);
					}
				}).create();
		batchCodedialog.show();
	}

	private void getTeamInfo(int which) {
		AdvanceBatchCode batchMsg = (AdvanceBatchCode) bactchArray.get(which);
		/**add by yejc 20130425 start*/
		if ("5".equals(batchMsg.getState())) {
			isaWait = true;
		} else {
			isaWait = false;
		}
		/**add by yejc 20130425 end*/
		switch (which) {
		case 0:
			layout_football_issue.setTextColor(0xffcc0000);
			break;

		default:
			layout_football_issue.setTextColor(0xff000000);
			break;
		}
		batchCode = bactchCodes[which];
		layout_football_issue.setText(batchMsg.getBatchCode());
		layout_football_time.setText(batchMsg.getEndTime());

		if (list != null) {
			list.clear();
		}
		getData(Constants.LOTNO_SFC, bactchCodes[which]);
	}

	private void getZCAdvanceBatchCodeData(final String Lotno) {
		progressdialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				advanceBatchCodeData = FootballLotteryAdvanceBatchcode
						.getInstance().getAdvanceBatchCodeList(Lotno);
				try {
					JSONObject advanceBatchCode = new JSONObject(
							advanceBatchCodeData);
					String errorCode = advanceBatchCode.getString("error_code");
					String message = advanceBatchCode.getString("message");
					if (errorCode.equals("0000")) {
						JSONArray batchCodeArray = advanceBatchCode
								.getJSONArray("result");
						bactchArray.clear();
						bactchCodes = new String[batchCodeArray.length()];
						/**add by yejc 20130425 start*/
						spanBactchCodes = new SpannableString[batchCodeArray.length()];
						/**add by yejc 20130425 end*/
						for (int i = 0; i < batchCodeArray.length(); i++) {
							JSONObject item = batchCodeArray.getJSONObject(i);
							AdvanceBatchCode aa = new AdvanceBatchCode();
							aa.setBatchCode(formatBatchCode(item
									.getString("batchCode")));
							aa.setEndTime(formatEndtime(item
									.getString("endTime")));
							/**add by yejc 20130425 start*/
							String batchCode = item.getString("batchCode");
							if (item.has("state")) {
								aa.setState(item.getString("state"));
								if (batchCode != null) {
									if ("5".equals(item.getString("state"))) {
										String waitIssue = getResources().getString(R.string.football_wait_issue);
										SpannableString text = new SpannableString(batchCode + waitIssue);
										text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, batchCode.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
										text.setSpan(new ForegroundColorSpan(Color.RED), batchCode.length(), batchCode.length()+waitIssue.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
										spanBactchCodes[i] = text;
									} else {
										SpannableString text = new SpannableString(batchCode);
										text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, batchCode.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
										spanBactchCodes[i] = text;
									}
								}
							} else {
								if (batchCode != null) {
									SpannableString text = new SpannableString(batchCode);
									text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, batchCode.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									spanBactchCodes[i] = text;
								}
							}
							/**add by yejc 20130425 end*/
							bactchCodes[i] = item.getString("batchCode").trim();
							bactchArray.add(aa);
							if (qihaoxinxi[1] != null
									|| "".equals(qihaoxinxi[1])) {
								qihaoxinxi[1] = item.getString("endTime");
							}
						}
						Message msg = handler.obtainMessage();
						msg.what = 25;
						msg.obj = message;
						handler.sendMessage(msg);
					} else {
						Message msg = handler.obtainMessage();
						msg.what = 24;
						msg.obj = message;
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progressdialog.dismiss();
			}
		}).start();
	}

	@Override
	void initBatchCodeView() {
		layout_football_issue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getZCAdvanceBatchCodeData(Constants.LOTNO_SFC);
			}
		});
	}
	
	public Vector<BallTable> getBallTableVector() {
		return ballTables;
	}

}
