package com.ruyicai.activity.notice;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.dialog.ExitDialogFactory;
import com.ruyicai.net.newtransaction.NoticeWinInterface;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;

/**
 * 开奖公告
 * @author haojie
 *
 */
public class NoticeMainActivity extends Activity  {

	public static final String TAG = "NoticePrizesOfLottery";
	public final static String LOTTERYTYPE = "LOTTERYTYPE";
	public final static String WINNINGNUM = "WINNINGNUM";
	public final static String DATE = "DATA";
	public final static String ISSUE = "ISSUE";
	public final static String FINALDATE = "FINALDATE";
	public final static String MONEYSUM = "MONEYSUM";
	public final static String WINCODE = "winCode";
	public final static String OPENTIME = "openTime";
	public final static String BATCHCODE = "batchCode";
	public final static int a = R.drawable.kaijiang_lotterytype;

	public final static int ID_MAINLISTVIEW = 1;
    public static float  SCALE  = 1;
	// 代码添加：超级大乐透
	int redBallViewNum = 7;
	int redBallViewWidth = 22;
	public static  int BALL_WIDTH = 46;
	TextView text_lotteryName; // 彩票种类的TextView
	List<Map<String, Object>> list; // zlm 8.9 添加排列三、超级大乐透
	static BallTable kaiJiangGongGaoBallTable = null;
	static LinearLayout iV;
	static String strChuanZhi;
	private static int[] aRedColorResId = { R.drawable.red };
	private static int[] aBlueColorResId = { R.drawable.blue };
	// 周黎鸣 7.5 代码修改：添加代码
	private boolean iQuitFlag = true;
	// 进度条
	private ProgressDialog progressdialog;
	private static final int DIALOG1_KEY = 0;//
	private static final Integer[] mIcon = { R.drawable.join_ssq,R.drawable.join_fc3d, R.drawable.join_qlc,
											 R.drawable.join_dlt , R.drawable.join_pl3,R.drawable.join_pl5,
											 R.drawable.join_qxc,R.drawable.join_ssc,R.drawable.join_11x5,
											 R.drawable.join_11ydj,R.drawable.twenty,R.drawable.join_sfc,R.drawable.join_rx9,
											 R.drawable.join_6cb,R.drawable.join_jqc,R.drawable.join_jcz,R.drawable.join_jcl}; // zlm 8.9 添加排列三、超级大乐透图标
	private static final String[] titles = {"双色球","福彩3D","七乐彩","大乐透","排列三","排列五","七星彩","时时彩","11选5","11运夺金","22选5","胜负彩","任选九","六场半","进球彩","竞足彩","竞篮彩"};
    public static boolean isFirstNotice = true; 
	/**
	 * 消息处理函数
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				showListView(ID_MAINLISTVIEW);
				progressdialog.dismiss();
				break;
			}

		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_prizes_main);
		setScale();
	}
    /**
     * 根据手机屏幕设置球的大小和球的缩放比例
     */
	private void setScale() {
		int screenWith=PublicMethod.getDisplayWidth(this);
		if(screenWith<=240){
			BALL_WIDTH=46*120/240;
			SCALE = (float)140/240;
		}else if(screenWith>240&&screenWith<=320){
			BALL_WIDTH=46*160/240;
			SCALE = (float)180/240;
		}else if(screenWith==480){
			BALL_WIDTH=46;
			SCALE = 1;
		}else if(screenWith>480){
			BALL_WIDTH=80;
			SCALE = (float)1.5;
		}
	}

	/**
	 * 将获取到的开奖信息放到常量类中
	 */
	public void analysisLotteryNoticeJsonObject(JSONObject jobject){
		
		
		//双色球信息获取
		try {
				Constants.ssqJson = jobject.getJSONObject("ssq");
		}catch(Exception e){
			//获取双色球数据出现异常
			e.printStackTrace();
		}finally{
			//判断是否已经从网络上获取到了数据
			if(Constants.ssqJson == null||!jobject.has("ssq")){
				//没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "00000000000000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {
						
					}
				}
				Constants.ssqJson = tempObj;
			}
		}
		try {
				Constants.fc3dJson = jobject.getJSONObject("ddd");
		}catch(Exception e){
			//获取福彩3D数据出现异常
			e.printStackTrace();
		}finally{
			//判断是否已经从网络上获取到了数据
			if(Constants.fc3dJson == null||!jobject.has("ddd")){
				//没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "00000000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {
						
					}
				}
				Constants.fc3dJson = tempObj;
			}
		}
		
		try {
				Constants.qlcJson = jobject.getJSONObject("qlc");
		}catch(Exception e){
			//获取七乐彩数据出现异常
			e.printStackTrace();
		}finally{
			//判断是否已经从网络上获取到了数据
			if(Constants.qlcJson == null||!jobject.has("qlc")){
				//没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE,"0000000000000000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {
						
					}
				}
				Constants.qlcJson = tempObj;
			}
		}
		
		try {
			Constants.pl3Json = jobject.getJSONObject("pl3");
		}catch(Exception e){
			//获取排列三数据出现异常
			e.printStackTrace();
		}finally{
			//判断是否已经从网络上获取到了数据
			if(Constants.pl3Json == null||!jobject.has("pl3")){
				//没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "00000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {
						
					}
				}
				Constants.pl3Json = tempObj;
			}
		}
		
		try {
				Constants.dltJson = jobject.getJSONObject("dlt");
		}catch(Exception e){
			//获取大乐透数据出现异常
			e.printStackTrace();
		}finally{
			//判断是否已经从网络上获取到了数据
			if(Constants.dltJson == null||!jobject.has("dlt")){
				//没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "00 00 00 00 00+00 0000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {
						
					}
				}
				Constants.dltJson = tempObj;
			}
		}
		
		try {
				Constants.sscJson = jobject.getJSONObject("ssc");
		}catch(Exception e){
			//获取实时彩数据出现异常
			e.printStackTrace();
		}finally{
			//判断是否已经从网络上获取到了数据
			if(Constants.sscJson == null||!jobject.has("ssc")){
				//没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "0000000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {
						
					}
				}
				Constants.sscJson = tempObj;
			}
		}
		
		try {
				Constants.dlcJson = jobject.getJSONObject("11-5");
		}catch(Exception e){
			//获取实时彩数据出现异常
			e.printStackTrace();
		}finally{
			//判断是否已经从网络上获取到了数据
			if(Constants.dlcJson==null||!jobject.has("11-5")){
				//没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "0000000000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {
						
					}
				}
				Constants.dlcJson = tempObj;
			}
		}
		

		try {
				Constants.ydjJson = jobject.getJSONObject("11-ydj");
		}catch(Exception e){
			//获取实时彩数据出现异常
			e.printStackTrace();
		}finally{
			//判断是否已经从网络上获取到了数据
			if(Constants.ydjJson == null||!jobject.has("11-ydj")){
				//没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "0000000000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {
						
					}
				}
				Constants.ydjJson = tempObj;
			}
		}
		
	try {
			Constants.twentyJson = jobject.getJSONObject("22-5");
	}catch(Exception e){
		//获取实时彩数据出现异常
		e.printStackTrace();
	}finally{
		//判断是否已经从网络上获取到了数据
		if(Constants.twentyJson == null||!jobject.has("22-5")){
			//没数据,初始化点数居
			JSONObject tempObj = new JSONObject();
			for (int i = 0; i < 5; i++) {
				try {
					tempObj.put(BATCHCODE, "");
					tempObj.put(WINCODE, "0000000000");
					tempObj.put(OPENTIME, "");
				} catch (JSONException e) {
					
				}
			}
			Constants.twentyJson = tempObj;
		}
	}
		try {
				Constants.sfcJson = jobject.getJSONObject("sfc");
		}catch(Exception e){
			//获取胜负彩数据出现异常
			e.printStackTrace();
		}finally{
			//判断是否已经从网络上获取到了数据
			if(Constants.sfcJson == null||!jobject.has("sfc")){
				//没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {
						
					}
				}
				Constants.sfcJson = tempObj;
			}
		}
		
		try {
				Constants.rx9Json = jobject.getJSONObject("rx9");
		}catch(Exception e){
			//获取任选9数据出现异常
			e.printStackTrace();
		}finally{
			//判断是否已经从网络上获取到了数据
			if(Constants.rx9Json == null||!jobject.has("rx9")){
				//没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {
						
					}
				}
				Constants.rx9Json = tempObj;
			}
		}
			
		try {
				Constants.half6Json = jobject.getJSONObject("6cb");
		}catch(Exception e){
			//获取6场半数据出现异常
			e.printStackTrace();
		}finally{
			//判断是否已经从网络上获取到了数据
			if(Constants.half6Json==null||!jobject.has("6cb")){
				//没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {
						
					}
				}
				Constants.half6Json = tempObj;
			}
		}
		
	 try {
				Constants.jqcJson = jobject.getJSONObject("jqc");
		}catch(Exception e){
			//获取进球彩数据出现异常
			e.printStackTrace();
		}finally{
			//判断是否已经从网络上获取到了数据
			if(Constants.jqcJson == null||!jobject.has("jqc")){
				//没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {
						
					}
				}
				Constants.jqcJson = tempObj;
			}
		}
		//排列五
		try {
				Constants.pl5Json = jobject.getJSONObject("pl5");
		}catch(Exception e){
			//获取进球彩数据出现异常
			e.printStackTrace();
		}finally{
			//判断是否已经从网络上获取到了数据
			if(Constants.pl5Json == null||!jobject.has("pl5")){
				//没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "00000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {
						
					}
				}
				Constants.pl5Json = tempObj;
			}
		}
		//七星彩
		try {
				Constants.qxcJson = jobject.getJSONObject("qxc");
		}catch(Exception e){
			//获取进球彩数据出现异常
			e.printStackTrace();
		}finally{
			//判断是否已经从网络上获取到了数据
			if(Constants.qxcJson==null||!jobject.has("qxc")){
				//没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "0000000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {
						
					}
				}
				Constants.qxcJson = tempObj;
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		noticeNet();
	}
	private void noticeNet() {
		showDialog(DIALOG1_KEY); // 显示网络提示框 2010/7/4
		new Thread(new Runnable() {
			@Override
			public void run() {
				//判断缓存时间,如果超过缓存时间,进行联网更新
				if(Constants.lastNoticeTime == 0||
				  (System.currentTimeMillis()-Constants.lastNoticeTime)/1000 > Constants.NOTICE_CACHE_TIME_SECOND){
					//是首次联网,或者缓存超时,联网获取数据
					Constants.lastNoticeTime = System.currentTimeMillis();
					JSONObject lotteryInfos = NoticeWinInterface.getInstance().getLotteryAllNotice();//开奖信息json对象
					//将获取到的开奖信息放到常量类中
					analysisLotteryNoticeJsonObject(lotteryInfos);
				}else{
					//不满足联网条件
				}
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			}
		}).start();
	}

	/**
	 * 开奖公告里主列表与子列表之间的跳转
	 * @param listViewID
	 *            列表ID
	 */
	private void showListView(int listViewID) {
		iQuitFlag = false; 
		switch (listViewID) {
		case ID_MAINLISTVIEW:
			iQuitFlag = true; 
			showMainListView();
			break;

		}
	}

	/**
	 * 退出检测
	 * @param keyCode      返回按键的号码
	 * @param event        事件
	 * @return
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case 4: {
				if (iQuitFlag == false) {
					setContentView(R.layout.notice_prizes_main);
					showListView(ID_MAINLISTVIEW);
				} else {
					ExitDialogFactory.createExitDialog(this);
				}
				break;
			}
		}
		return false;
	}


	/**
	 * 主列表
	 */
	private void showMainListView() {
		setContentView(R.layout.notice_prizes_main);

		ListView listview = (ListView) findViewById(R.id.notice_prizes_listview);
		list = NoticeDataProvider.getListForMainListViewSimpleAdapter();//获取开奖信息数据

		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE };
		MainEfficientAdapter adapter = new MainEfficientAdapter(this, str, list);
		listview.setDividerHeight(0);
		listview.setAdapter(adapter);
		// 设置点击监听
		OnItemClickListener clickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String iIssue = (String) list.get(position).get(ISSUE);
				NoticeActivityGroup.ISSUE = iIssue;
				// 点击福彩3D跳转到福彩3D子列表中
				if (titles[position].equals("福彩3D")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this, NoticeActivityGroup.class); 
					startActivity(intent);
				}
				// 点击七乐彩跳转到七乐彩子列表中
				if (titles[position].equals("七乐彩")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this, NoticeActivityGroup.class); 
					startActivity(intent);
				}
				// 点击双色球跳转到双色球子列表中
				if (titles[position].equals("双色球")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_SHUANGSEQIU_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this, NoticeActivityGroup.class); 
					startActivity(intent);
				}
				// 点击排列三跳转到排列三子列表中
				if (titles[position].equals("排列三")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_PAILIESAN_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this, NoticeActivityGroup.class); 
					startActivity(intent);
				}
				// 点击超级大乐透跳转到超级大乐透子列表中
				if (titles[position].equals("大乐透")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_DLT_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this, NoticeActivityGroup.class); 
					startActivity(intent);
				}
				// 点击时时彩跳转到时时彩子列表中
				if (titles[position].equals("时时彩")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_SHISHICAI_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this, NoticeActivityGroup.class); 
					startActivity(intent);
				}
				// 点击11-5跳转到11-5子列表中
				if (titles[position].equals("11选5")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_DLC_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this, NoticeActivityGroup.class); 
					startActivity(intent);
				}
				// 点击11-5跳转到11运夺金子列表中
				if (titles[position].equals("11运夺金")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_YDJ_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this, NoticeActivityGroup.class); 
					startActivity(intent);
				}
				// 点击22选5子列表中
				if (titles[position].equals("22选5")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_TWENTY_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this, NoticeActivityGroup.class); 
					startActivity(intent);
				}
				// 点击时时彩跳转到胜负彩子列表中
				if (titles[position].equals("胜负彩")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_SFC_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this, NoticeActivityGroup.class); 
					startActivity(intent);
				}
				// 点击时时彩跳转到任选九彩子列表中
				if (titles[position].equals("任选九")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_RXJ_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this, NoticeActivityGroup.class); 
					startActivity(intent);
				}
				// 点击时时彩跳转到六场半子列表中
				if (titles[position].equals("六场半")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_LCB_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this, NoticeActivityGroup.class); 
					startActivity(intent);
				}
				// 点击时时彩跳转到进球彩子列表中
				if (titles[position].equals("进球彩")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_JQC_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this, NoticeActivityGroup.class); 
					startActivity(intent);
				}
				// 点击时时彩跳转到进球彩子列表中
				if (titles[position].equals("排列五")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_PL5_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this, NoticeActivityGroup.class); 
					startActivity(intent);
				}
				// 点击时时彩跳转到进球彩子列表中
				if (titles[position].equals("七星彩")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_QXC_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this, NoticeActivityGroup.class); 
					startActivity(intent);
				}
			}

		};

		listview.setOnItemClickListener(clickListener);
	}



	

	// 主列表适配器
	public static class MainEfficientAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Map<String, Object>> mList;
		private String[] mIndex;
		private Context context;

		public MainEfficientAdapter(Context context, String[] index,
				List<Map<String, Object>> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;
			mIndex = index;
			this.context = context;
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

		// 设置主列表布局中的详细内容
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String iGameType = (String) mList.get(position).get(mIndex[0]);
			String iNumbers = (String) mList.get(position).get(mIndex[1]);
			String iDate = (String) mList.get(position).get(mIndex[2]);
			final String iIssue = (String) mList.get(position).get(mIndex[3]);
			String iIssueNo = "第"+iIssue+"期";
			ViewHolder holder = null;

			convertView = mInflater.inflate(R.layout.notice_prizes_main_layout, null);

			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.name = (TextView) convertView.findViewById(R.id.notice_prizes_main_title);
			holder.numbers = (LinearLayout) convertView.findViewById(R.id.ball_linearlayout);
			holder.date = (TextView) convertView.findViewById(R.id.notice_prizes_dateAndTimeId);
			holder.issue = (TextView) convertView.findViewById(R.id.notice_prizes_issueId);
			holder.lookBtn = (Button) convertView.findViewById(R.id.notice_prizes_main_btn_jc);
			holder.rLayout = (RelativeLayout) convertView.findViewById(R.id.notice_prizes_relative);
			holder.numbers.removeAllViews();
			convertView.setTag(holder);
		


			
			
			if (iGameType.equals("ssq")) {
				holder.name.setText(titles[position]);
				holder.icon.setImageResource(mIcon[position]);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);

				// zlm 7.28 代码修改：添加号码排序
				int i1, i2, i3;
				String iShowNumber;
				OneBallView tempBallView;
				int[] ssqInt01 = new int[6];
				int[] ssqInt02 = new int[6];
				String[] ssqStr = new String[6];

				for (i2 = 0; i2 < 6; i2++) {
					iShowNumber = iNumbers.substring(i2 * 2, i2 * 2 + 2);
					ssqInt01[i2] = Integer.valueOf(iShowNumber);
				}

				ssqInt02 = PublicMethod.sort(ssqInt01);

				for (i3 = 0; i3 < 6; i3++) {
					if (ssqInt02[i3] < 10) {
						ssqStr[i3] = "0" + ssqInt02[i3];
					} else {
						ssqStr[i3] = "" + ssqInt02[i3];
					}
				}
				for (i1 = 0; i1 < 6; i1++) {
					// iShowNumber = iNumbers.substring(i1 * 2, i1 * 2 + 2);
					tempBallView = new OneBallView(convertView.getContext(),1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ssqStr[i1],aRedColorResId);
					tempBallView.setScaleType(ScaleType.CENTER_INSIDE);
					holder.numbers.addView(tempBallView);
				}

				iShowNumber = iNumbers.substring(12, 14);
				tempBallView = new OneBallView(convertView.getContext(),1);
				tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,aBlueColorResId);

				holder.numbers.addView(tempBallView);
			} else if (iGameType.equals("fc3d")) {
				holder.name.setText(titles[position]);
				holder.icon.setImageResource(mIcon[position]);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				// zlm 7.30 代码修改：修改福彩3D号码
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 3; i1++) {
					iShowNumber = Integer.valueOf(iNumbers.substring(i1 * 2,
							i1 * 2 + 2));
					tempBallView = new OneBallView(convertView.getContext(),1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
							+ iShowNumber, aRedColorResId);
					holder.numbers.addView(tempBallView);
				}
			} else if (iGameType.equals("qlc")) {
				holder.name.setText(titles[position]);
				holder.icon.setImageResource(mIcon[position]);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
	        	// zlm 7.28 代码修改：添加号码排序
				int i1, i2, i3;
				String iShowNumber;
				OneBallView tempBallView;

				int[] ssqInt01 = new int[7];
				int[] ssqInt02 = new int[7];
				String[] ssqStr = new String[7];

				for (i2 = 0; i2 < 7; i2++) {
					iShowNumber = iNumbers.substring(i2 * 2, i2 * 2 + 2);
					ssqInt01[i2] = Integer.valueOf(iShowNumber);
				}

				ssqInt02 = PublicMethod.sort(ssqInt01);

				for (i3 = 0; i3 < 7; i3++) {
					if (ssqInt02[i3] < 10) {
						ssqStr[i3] = "0" + ssqInt02[i3];
					} else {
						ssqStr[i3] = "" + ssqInt02[i3];
					}
				}
				for (i1 = 0; i1 < 7; i1++) {

					tempBallView = new OneBallView(convertView.getContext(),1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ssqStr[i1],
							aRedColorResId);
					holder.numbers.addView(tempBallView);
				}
				// zlm 8.3 代码修改 ：添加七乐彩蓝球
				iShowNumber = iNumbers.substring(14, 16);
				tempBallView = new OneBallView(convertView.getContext(),1);
				tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,
						aBlueColorResId);

				holder.numbers.addView(tempBallView);
			} else if (iGameType.equals("cjdlt")) {
				holder.name.setText(titles[position]);
				holder.icon.setImageResource(mIcon[position]);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				// zlm 7.28 代码修改：添加号码排序
				int i1, i2, i3;
				String iShowNumber = "";
				OneBallView tempBallView;
				int[] cjdltInt01 = new int[5];
				int[] cjdltInt02 = new int[5];
				int[] cjdltInt03 = new int[2];
				int[] cjdltInt04 = new int[2];
				String[] cjdltStr = new String[5];
				String[] cjdltStr1 = new String[2];

				for (i2 = 0; i2 < 5; i2++) {
					iShowNumber = iNumbers.substring(i2 * 3, i2 * 3 + 2);
					cjdltInt01[i2] = Integer.valueOf(iShowNumber);
				}

				cjdltInt02 = PublicMethod.sort(cjdltInt01);

				for (i3 = 0; i3 < 5; i3++) {
					if (cjdltInt02[i3] < 10) {
						cjdltStr[i3] = "0" + cjdltInt02[i3];
					} else {
						cjdltStr[i3] = "" + cjdltInt02[i3];
					}
				}
				for (i1 = 0; i1 < 5; i1++) {

					tempBallView = new OneBallView(convertView.getContext(),1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, cjdltStr[i1],aRedColorResId);
					holder.numbers.addView(tempBallView);
				}

				for (i2 = 0; i2 < 2; i2++) {
					try {
						iShowNumber = iNumbers.substring(i2 * 3 + 15, i2 * 3 + 17);
					} catch (Exception e) {
						e.printStackTrace();
					}
					cjdltInt03[i2] = Integer.valueOf(iShowNumber);
				}

				cjdltInt04 = PublicMethod.sort(cjdltInt03);

				for (i3 = 0; i3 < 2; i3++) {
					if (cjdltInt04[i3] < 10) {
						cjdltStr1[i3] = "0" + cjdltInt04[i3];
					} else {
						cjdltStr1[i3] = "" + cjdltInt04[i3];
					}
				}

				for (i1 = 0; i1 < 2; i1++) {
					tempBallView = new OneBallView(convertView.getContext(),1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH,
							cjdltStr1[i1], aBlueColorResId);

					holder.numbers.addView(tempBallView);
				}
			} else if (iGameType.equals("pl3")) {
				holder.name.setText(titles[position]);
				holder.icon.setImageResource(mIcon[position]);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 3; i1++) {
					iShowNumber = Integer.valueOf(iNumbers.substring(i1, i1 + 1));
					tempBallView = new OneBallView(convertView.getContext(),1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""+ iShowNumber, aRedColorResId);
					holder.numbers.addView(tempBallView);
				}
			}else if (iGameType.equals("pl5")) {
				holder.name.setText(titles[position]);
				holder.icon.setImageResource(mIcon[position]);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 5; i1++) {
					iShowNumber = Integer.valueOf(iNumbers.substring(i1, i1 + 1));
					tempBallView = new OneBallView(convertView.getContext(),1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""+ iShowNumber, aRedColorResId);
					holder.numbers.addView(tempBallView);
				}
			} else if (iGameType.equals("qxc")) {
				holder.name.setText(titles[position]);
				holder.icon.setImageResource(mIcon[position]);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 7; i1++) {
					iShowNumber = Integer.valueOf(iNumbers.substring(i1, i1 + 1));
					tempBallView = new OneBallView(convertView.getContext(),1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""+ iShowNumber, aRedColorResId);
					holder.numbers.addView(tempBallView);
				}
			}else if (iGameType.equals("ssc")) {
				holder.name.setText(titles[position]);
				holder.icon.setImageResource(mIcon[position]);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 5; i1++) {
					iShowNumber = Integer.valueOf(iNumbers
							.substring(i1, i1 + 1));
					tempBallView = new OneBallView(convertView.getContext(),1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
							+ iShowNumber, aRedColorResId);
					holder.numbers.addView(tempBallView);

				}
			} else if (iGameType.equals("11-5")) {
				holder.name.setText(titles[position]);
				holder.icon.setImageResource(mIcon[position]);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 5; i1++) {
					iShowNumber = Integer.valueOf(iNumbers.substring(i1*2, i1*2 + 2));
					tempBallView = new OneBallView(convertView.getContext(),1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""+ iShowNumber, aRedColorResId);
					holder.numbers.addView(tempBallView);

				}
			}else if (iGameType.equals("11-ydj")) {
				holder.name.setText(titles[position]);
				holder.icon.setImageResource(mIcon[position]);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 5; i1++) {
					iShowNumber = Integer.valueOf(iNumbers.substring(i1*2, i1*2 + 2));
					tempBallView = new OneBallView(convertView.getContext(),1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""+ iShowNumber, aRedColorResId);
					holder.numbers.addView(tempBallView);

				}
			}else if (iGameType.equals("22-5")) {
				holder.name.setText(titles[position]);
				holder.icon.setImageResource(mIcon[position]);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 5; i1++) {
					iShowNumber = Integer.valueOf(iNumbers.substring(i1*2, i1*2 + 2));
					tempBallView = new OneBallView(convertView.getContext(),1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""+ iShowNumber, aRedColorResId);
					holder.numbers.addView(tempBallView);

				}
			}else if (iGameType.equals("sfc")) {
				holder.name.setText(titles[position]);
				holder.icon.setImageResource(mIcon[position]);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
			
				TextView tvFootball = new TextView(convertView.getContext());
				tvFootball.setTextColor( R.color.darkgreen);
				tvFootball.setTextSize(25);
				tvFootball.setGravity(Gravity.RIGHT);
				tvFootball.setText(iNumbers);
				holder.numbers.addView(tvFootball);
			} else if (iGameType.equals("rxj")) {
				holder.name.setText(titles[position]);
				holder.icon.setImageResource(mIcon[position]);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				TextView tvFootball = new TextView(convertView.getContext());
				tvFootball.setTextColor( R.color.darkgreen);
				tvFootball.setTextSize(25);
				tvFootball.setGravity(Gravity.RIGHT);
				tvFootball.setText(iNumbers);
				holder.numbers.addView(tvFootball);
	    	} else if (iGameType.equals("lcb")) {
	    		holder.name.setText(titles[position]);
				holder.icon.setImageResource(mIcon[position]);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				TextView tvFootball = new TextView(convertView.getContext());
				tvFootball.setGravity(Gravity.RIGHT);
				tvFootball.setTextColor( R.color.darkgreen);
				tvFootball.setTextSize(25);
				tvFootball.setText(iNumbers);
				holder.numbers.addView(tvFootball);
			} else if (iGameType.equals("jqc")) {
				holder.name.setText(titles[position]);
				holder.icon.setImageResource(mIcon[position]);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				TextView tvFootball = new TextView(convertView.getContext());
				tvFootball.setTextColor( R.color.darkgreen);
				tvFootball.setTextSize(25);
				tvFootball.setGravity(Gravity.RIGHT);
				tvFootball.setText(iNumbers);
				holder.numbers.addView(tvFootball);
			}else if (iGameType.equals("jcz")) {
				holder.name.setText(titles[position]);
				holder.icon.setImageResource(mIcon[position]);
			    holder.rLayout.setVisibility(RelativeLayout.GONE);
				holder.lookBtn.setVisibility(Button.VISIBLE);
				holder.lookBtn.setBackgroundResource(R.drawable.join_info_btn_selecter);
				holder.lookBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context, NoticeJcActivity.class); 
						
						context.startActivity(intent);
					}
				});
			}else if (iGameType.equals("jcl")) {
				holder.name.setText(titles[position]);
				holder.icon.setImageResource(mIcon[position]);
			    holder.rLayout.setVisibility(RelativeLayout.GONE);
				holder.lookBtn.setVisibility(Button.VISIBLE);
				holder.lookBtn.setBackgroundResource(R.drawable.join_info_btn_selecter);
				holder.lookBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context, NoticeJclActivity.class); 
						context.startActivity(intent);
					}
				});
			}
//			convertView.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					NoticeActivityGroup.ISSUE = iIssue;
//				}
//			});
			
			return convertView;
		}

		static class ViewHolder {
			ImageView icon;
			TextView name;
			LinearLayout numbers;
			TextView date;
			TextView issue;
			Button lookBtn;
			RelativeLayout rLayout;
		}
	}

	

	/**
	 * 网络连接提示框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG1_KEY: {
				progressdialog = new ProgressDialog(this);
				
				progressdialog.setMessage("网络连接中...");
				progressdialog.setIndeterminate(true);
				progressdialog.setCancelable(true);
				return progressdialog;
			}
		}
		return null;
	}
}
