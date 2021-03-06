/**
 * 
 */
package com.ruyicai.activity.buy.dlc;

import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.buy.BuyGameDialog;
import com.ruyicai.activity.buy.HighFrequencyNoticeHistroyActivity;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.zixuan.AddView;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.more.LuckChoose2;
import com.ruyicai.activity.usercenter.BetQueryActivity;
import com.ruyicai.code.dlc.DlcCode;
import com.ruyicai.code.dlc.DlcDanTuoCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.custom.jc.button.MyButton;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.jixuan.DlcQxBalls;
import com.ruyicai.jixuan.DlcRxBalls;
import com.ruyicai.json.miss.DlcMissJson;
import com.ruyicai.json.miss.MissConstant;
import com.ruyicai.json.miss.SscZMissJson;
import com.ruyicai.net.newtransaction.GetLotNohighFrequency;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * 多乐彩（11选5）
 * @author Administrator
 *
 */
public class Dlc extends ZixuanAndJiXuan {
	protected String types[] = {"R2","R3","R4","R5","R6","R7","R8","R1","Q2","Q3","Z2","Z3"};//类型
	protected int nums[] = {2,3,4,5,6,7,8,1,2,3,2,3};//单式机选个数
	protected int numsdantuo[]={};
	protected int maxs[] = {3,4,7,10,8,9,8,6,11,11,8,9};//选区最大小球数
	public static String state = "";//当前类型
    public int num = 1;//当前单式机选个数
    protected int max = 6;//选区最大小球数
    protected Spinner typeSpinner;
    private BallTable  oneBallTable;
    private BallTable  twoBallTable;
    private BallTable  thirdBallTable;
    public boolean isJiXuan = false;
    protected boolean is11_5DanTuo = false;
    protected TextView titleOne;//大标题
	protected TextView issue;//期号
	protected TextView time;//截止时间
	protected Button imgRetrun;//返回购彩大厅按钮
	public  static String batchCode;//期号
	private int    lesstime;//剩余时间
	private Handler handler = new Handler();
	public String lotno;
	private boolean isRun = true;
	private PopupWindow popupwindow;
	private BuyGameDialog gameDialog;
	private Context context;
	Handler gameHandler = new Handler();
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
	    inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    layoutMain = inflater.inflate(R.layout.buy_dlc_main, null);
		setContentView(layoutMain);
		highttype = "DLC";
		setLotno();
		initView();
		initSpinner();
		setIssue(lotno);
		MobclickAgent.onEvent(this,"jiangxi11xuan5" ); //BY贺思明 点击首页的“江西11选5”图标	
		MobclickAgent.onEvent(this,"gaopingoucaijiemian " );//BY贺思明 高频购彩页面
	}
	/**
	 * 中奖提示
	 */
	public void setTextPrize(int type){
		textPrize.setTextSize(11);
		if(state.equals("R1")){
			textPrize.setText(getString(R.string.h_11_5_prize_xq_1));
		}else if(state.equals("Q2")){
			textPrize.setText(getString(R.string.h_11_5_prize_xq_2));
		}else if(state.equals("Q3")){
			textPrize.setText(getString(R.string.h_11_5_prize_xq_3));
		}else if(state.equals("Z3")){
			if(is11_5DanTuo){
				textPrize.setText(getString(R.string.h_11_5_prize_dan_zx_3));
			}else{
				textPrize.setText(getString(R.string.h_11_5_prize_zx_3));
			}
		}else  if(state.equals("Z2")){
			if(is11_5DanTuo){
				textPrize.setText(getString(R.string.h_11_5_prize_dan_zx_2));
			}else{
				textPrize.setText(getString(R.string.h_11_5_prize_zx_2));
			}
		}else  if(state.equals("R2")){
			if(is11_5DanTuo){
				textPrize.setText(getString(R.string.h_11_5_prize_dan_rx_2));
			}else{
				textPrize.setText(getString(R.string.h_11_5_prize_rx_2));
			}
		}else  if(state.equals("R3")){
			if(is11_5DanTuo){
				textPrize.setText(getString(R.string.h_11_5_prize_dan_rx_3));
			}else{
				textPrize.setText(getString(R.string.h_11_5_prize_rx_3));
			}
		}else  if(state.equals("R4")){
			if(is11_5DanTuo){
				textPrize.setText(getString(R.string.h_11_5_prize_dan_rx_4));
			}else{
				textPrize.setText(getString(R.string.h_11_5_prize_rx_4));
			}
		}else  if(state.equals("R5")){
			if(is11_5DanTuo){
				textPrize.setText(getString(R.string.h_11_5_prize_dan_rx_5));
			}else{
				textPrize.setText(getString(R.string.h_11_5_prize_rx_5));
			}
		}else  if(state.equals("R6")){
			if(is11_5DanTuo){
				textPrize.setText(getString(R.string.h_11_5_prize_dan_rx_6));
			}else{
				textPrize.setText(getString(R.string.h_11_5_prize_rx_6));
			}
		}else  if(state.equals("R7")){
			if(is11_5DanTuo){
				textPrize.setText(getString(R.string.h_11_5_prize_dan_rx_7));
			}else{
				textPrize.setText(getString(R.string.h_11_5_prize_rx_7));
			}
		}else  if(state.equals("R8")){
			if(is11_5DanTuo){
				textPrize.setText(getString(R.string.h_11_5_prize_dan_rx_8));
			}else{
				textPrize.setText(getString(R.string.h_11_5_prize_rx_8));
			}
		}
	}
	/**
	 * 设置彩种编号
	 * @param lotno
	 */
    public void setLotno(){
    	this.lotno = Constants.LOTNO_11_5;
    	lotnoStr = lotno;
    }

	/**
	 * 初始化组件
	 */
	private void initView() {
		titleOne = (TextView) findViewById(R.id.layout_main_text_title_one);
		issue = (TextView) findViewById(R.id.layout_main_text_issue);
		time = (TextView) findViewById(R.id.layout_main_text_time);
		imgRetrun = (Button) findViewById(R.id.layout_main_img_return);
		
		titleOne.setText(getString(R.string.dlc));
		imgRetrun.setVisibility(View.VISIBLE);
		// ImageView的返回事件
		imgRetrun.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				createDialog();
			}
		});
	}
	
	/**
	 * 是否显示幸运选号菜单
	 */
	public boolean getIsLuck() {
		return true;
	}
	
	/**
	 * 创建下拉列表
	 */
	private void createDialog() {
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView = (LinearLayout) inflate.inflate(
				R.layout.buy_group_window, null);
		popupwindow = new PopupWindow(popupView, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		popupwindow.setTouchable(true); // 设置PopupWindow可触摸
		popupwindow.setOutsideTouchable(true);
		popupView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}
				return false;
			}
		});
		popupwindow.showAsDropDown(imgRetrun);
		final LinearLayout layoutGame = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout1);
		final LinearLayout layoutHosity = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout2);
		final LinearLayout layoutLuck = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout3);
		final LinearLayout layoutQuery = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout4);
		final LinearLayout layoutParentLuck = (LinearLayout) popupView
				.findViewById(R.id.buy_group_one_layout3);
		layoutGame.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				layoutGame.setBackgroundResource(R.drawable.buy_group_layout_b);
				if(gameDialog == null){
					gameDialog = new BuyGameDialog(context, lotno,gameHandler);
				}
				gameDialog.showDialog();
				popupwindow.dismiss();
			}
		});
		layoutHosity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				layoutHosity.setBackgroundResource(R.drawable.buy_group_layout_b);
				turnHosity();
				popupwindow.dismiss();
			}

		});
		layoutQuery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RWSharedPreferences shellRW = new RWSharedPreferences(context, "addInfo");
				String userno = shellRW.getStringValue(ShellRWConstants.USERNO);
				if (userno == null || userno.equals("")) {
					Intent intentSession = new Intent(context, UserLogin.class);
					startActivity(intentSession);
				} else {
					Intent intent = new Intent(Dlc.this,BetQueryActivity.class);
					intent.putExtra("lotno", lotno);
					startActivity(intent);
				}
				popupwindow.dismiss();
			}

		});
		
		//显示幸运选号菜单
		if(getIsLuck()){
			layoutParentLuck.setVisibility(LinearLayout.VISIBLE);
			layoutLuck.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					layoutLuck.setBackgroundResource(R.drawable.buy_group_layout_b);
					Intent intent = new Intent(Dlc.this, LuckChoose2.class);
					intent.putExtra("lotno", lotno);
					intent.putExtra("caipiaoWanfaIndex", typeSpinner.getSelectedItemPosition());
					startActivity(intent);
					popupwindow.dismiss();
				}
			});
		}else{
			layoutParentLuck.setVisibility(LinearLayout.GONE);
		}
	}
	public void turnHosity() {
		Intent intent = new Intent(Dlc.this,HighFrequencyNoticeHistroyActivity.class);
		intent.putExtra("lotno", lotno);
		startActivity(intent);
	}
	/**
	 * 设置大标题
	 * @param title
	 */
	public void setTitleOne(String title){
		titleOne.setText(title);
	}
	/**
	 * 初始化spinner组件
	 */
	public void initSpinner(){
		typeSpinner = (Spinner) findViewById(R.id.buy_dlc_spinner);
		childtypes = (LinearLayout)findViewById(R.id.buy_dlc_top);
		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				int position = typeSpinner.getSelectedItemPosition();
			    action(position);
			    startSensor();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
		typeSpinner.setSelection(3);
	}
	/**
	 * 初始化group
	 */
	public void initGroup(){
		if(state.equals("R1")||state.equals("Q2")||state.equals("Q3")){
			childtype= new String[]{"自选"};  
			setGroupVisable(false);
			init();
			childtypes.setVisibility(View.GONE);
		}else  if(state.equals("Z2")||state.equals("Z3")){
			childtype= new String[]{"组选","胆拖"};
			init();
		}else{
			childtype= new String[]{"自选","胆拖"};
			init();
		}
	    group.setOnCheckedChangeListener(this);
	    group.check(0);   
	}
	/**
	 * RadioGroup是否隐藏
	 * @param lotno
	 */
	public void setGroupVisable(boolean isVisable){
		if(isVisable){
			group.setVisibility(RadioGroup.VISIBLE);
		}else{
			group.setVisibility(RadioGroup.GONE);
		}
	}
	

	/**
	 * 赋值给当前期
	 * @param type彩种编号
	 */
	public void setIssue(final String  lotno){
		final Handler sscHandler = new Handler();
		issue.setText("期号获取中....");
		time.setText("获取中...");
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String error_code = "00";
				String re = "";
				String message="";
					re = GetLotNohighFrequency.getInstance().getInfo(lotno);
					if (!re.equalsIgnoreCase("")) {
						try {
							JSONObject obj = new JSONObject(re);
							message = obj.getString("message");
					        error_code = obj.getString("error_code");
					        lesstime = Integer.valueOf(obj.getString("time_remaining"));
							batchCode = obj.getString("batchcode");	
							while(isRun){
								if(isEnd(lesstime)){
									sscHandler.post(new Runnable(){
										public void run() {
										issue.setText("第" + batchCode + "期");
										time.setText("剩余时间:" + PublicMethod.isTen(lesstime/60)+":"+PublicMethod.isTen(lesstime%60));
										}});
									Thread.sleep(1000);
									lesstime--;
								}else{
									sscHandler.post(new Runnable() {
										public void run() {
											issue.setText("第" + batchCode + "期");
											time.setText("剩余时间:00:00");	
											nextIssue();
								      }});
									break;
								}
							}
						}catch (Exception e) {
							sscHandler.post(new Runnable() {
								public void run() {
									issue.setText("获取期号失败");
									time.setText("获取失败");
						}});
						}
					} else {
						
					}
			}
		});
		thread.start();
	}
	private boolean isEnd(int time) {
		if(time>0){
			return true;
		}else{
			return false;
		}
	}
	private void nextIssue() {
		new AlertDialog.Builder(Dlc.this).setTitle("提示").setMessage(titleOne.getText().toString()+"第"+batchCode+"期已经结束,是否转入下一期")
		        .setNegativeButton("转入下一期", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				setIssue(lotno);
			}
		
		}).setNeutralButton("返回主页面", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			 Dlc.this.finish();
			}
		 }).create().show();
	}
	public void updatePage() {
		Intent intent = new Intent(Dlc.this, Dlc.class);
		startActivity(intent);
		finish();
	}
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	/**
	 * 单选框切换直选，机选
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		onCheckAction(checkedId);
	}
	public void onCheckAction(int checkedId){
		radioId = checkedId;
		switch(checkedId){		
		case 0:
			is11_5DanTuo=false;
			isJiXuan = false;
			createViewZx(checkedId);
			startSensor();	
		break;
		case 1:	
			stopSensor();
			is11_5DanTuo=true;
			isJiXuan = false;
			createViewDT(checkedId);
		break;	
		}
	}
	/**
	 * 初始化自选选区
	 */
	public void createViewZx(int id){
		iProgressBeishu = 1;iProgressQishu = 1;
		sscCode = new DlcCode();
		initArea();
		if(state.equals("R5")){
			lineNum = 2;
			textSize = 2;
			createViewNew(areaNums, sscCode,ZixuanAndJiXuan.NULL,true,id);
		}else if(state.equals("R7")){
			lineNum = 2;
			textSize = 2;
			createViewNew(areaNums, sscCode,ZixuanAndJiXuan.NULL,true,id);
		}else if(state.equals("R8")){
			lineNum = 2;
			textSize = 2;
			createViewNew(areaNums, sscCode,ZixuanAndJiXuan.NULL,true,id);
		}else if(state.equals("Q3")){
			lineNum = 2;
			textSize = 2;
			createViewNew(areaNums, sscCode,ZixuanAndJiXuan.NULL,true,id);
		}else{
			createView(areaNums, sscCode,ZixuanAndJiXuan.NULL,true,id,true);
		}
	}
	/**
	 * 初始化机选选区
	 */
	public void createViewJx(int id){
		iProgressBeishu = 1;
		iProgressQishu = 1;
		if(state.equals("Q2")||state.equals("Q3")){
			DlcQxBalls dlcb= new DlcQxBalls(num);
			createviewmechine(dlcb,id);
		}else{
			DlcRxBalls dlcb = new DlcRxBalls(num);
			createviewmechine(dlcb,id);
		}
	
	}
	
	/**
	 * 初始化胆拖选区
	 */
	public void createViewDT(int id){
		iProgressBeishu = 1;
		iProgressQishu = 1;
		initDTArea();
		sscCode = new DlcDanTuoCode();
		createViewDanTuo(areaNums, sscCode,ZixuanAndJiXuan.NULL,true,id,true);
	
	}

	
	
	/**
	 * 初始化选区
	 */
	public AreaNum[] initArea() {
		String wantitle = getString(R.string.qxc_first);
		String qiantitle = getString(R.string.qxc_second);
		String baititle = getString(R.string.qxc_third);
		if(state.equals("Q2")){
			areaNums = new AreaNum[2];
			areaNums[0] = new AreaNum(11,10,1, 11, BallResId, 0, 1,Color.RED, wantitle,false,true,true);
			areaNums[1] = new AreaNum(11,10,1, 11, BallResId, 0, 1,Color.RED, qiantitle,false,true,true);
		}else if(state.equals("Q3")){
			areaNums = new AreaNum[3];
			areaNums[0] = new AreaNum(11,10,1, 11, BallResId, 0, 1,Color.RED, wantitle,false,true,true);
			areaNums[1] = new AreaNum(11,10,1, 11, BallResId, 0, 1,Color.RED, qiantitle,false,true,true);
			areaNums[2] = new AreaNum(11,10,1, 11, BallResId, 0, 1,Color.RED, baititle,false,true,true);
		}else if(state.equals("R1")){
			areaNums = new AreaNum[1];
	        String title = "请选择投注号码" ;
			areaNums[0] = new AreaNum(11,10,1, 11, BallResId, 0, 1,Color.RED, title,false,true);
		}else if(state.equals("Z2")){
			areaNums = new AreaNum[1];
	        String title = "请选择投注号码" ;
			areaNums[0] = new AreaNum(11,10,2, 11, BallResId, 0, 1,Color.RED, title,false,true);
		}else if(state.equals("Z3")){
			areaNums = new AreaNum[1];
	        String title = "请选择投注号码" ;
			areaNums[0] = new AreaNum(11,10,3, 11, BallResId, 0, 1,Color.RED, title,false,true);
		}else{
			areaNums = new AreaNum[1];
	        String title = "请选择投注号码" ;
	        int isChoseNum = typeSpinner.getSelectedItemPosition()+2;
			areaNums[0] = new AreaNum(11,10,isChoseNum, 11, BallResId, 0, 1,Color.RED, title,false,true);
		}
       return areaNums;
	}
	
	/**
	 * 初始化胆拖选区
	 */
	public void initDTArea() {
			areaNums = new AreaNum[2];
			areaNums[0] = new AreaNum(11,10, num-1, BallResId, 0, 1,Color.RED, "胆码");
			areaNums[1] = new AreaNum(11,10, 10, BallResId, 0, 1,Color.RED, "拖码");
	}
	/**
	 * spinner处理事件
	 */
	public void action(int position){
	    state = types[position];
        num = nums[position];
        max = maxs[position];
        missView.clear();
		initGroup();
		setSellWay();
	}
	public void setSellWay(){
		if(state.equals("Q2")||state.equals("R1")){
			if(!sellWay.equals(MissConstant.DLC_MV_Q3)){
				sellWay = MissConstant.DLC_MV_Q3;
			}
		}else if(state.equals("Z2")){
			if(!sellWay.equals(MissConstant.DLC_MV_Q2Z)){
				sellWay = MissConstant.DLC_MV_Q2Z;
			}
		}else if(state.equals("Z3")){
			if(!sellWay.equals(MissConstant.DLC_MV_Q3Z)){
				sellWay = MissConstant.DLC_MV_Q3Z;
			}
		}else if(state.equals("R5")){
			isMissNet(new SscZMissJson(),MissConstant.DLC_MV_ZH_R5,true);//获取遗漏值
			sellWay = MissConstant.DLC_MV_RX;
		}else if(state.equals("R7")){
			isMissNet(new SscZMissJson(),MissConstant.DLC_MV_ZH_R7,true);//获取遗漏值
			sellWay = MissConstant.DLC_MV_RX;
		}else if(state.equals("R8")){
			isMissNet(new SscZMissJson(),MissConstant.DLC_ZH_R8,true);//获取遗漏值
			sellWay = MissConstant.DLC_MV_RX;
		}else if(state.equals("Q3")){
			sellWay = MissConstant.DLC_MV_Q3;
			isMissNet(new SscZMissJson(),MissConstant.DLC_MV_Q3_ZH,true);//获取遗漏值
		}else{
				sellWay = MissConstant.DLC_MV_RX;
		}
		isMissNet(new DlcMissJson(),sellWay,false);//获取遗漏值
	}
	/**
	 * 根据小球id判断是哪个选区
	 * 
	 * @param iBallId
	 */
	public void isBallTable(int iBallId){
		int nBallId = 0; 
		for(int i=0;i<areaNums.length;i++){
			nBallId = iBallId;
			iBallId = iBallId - areaNums[i].areaNum;
		
		 if(iBallId<0){
				if(is11_5DanTuo){
					if(i==0){
						int isHighLight = areaNums[0].table.changeBallState(areaNums[0].chosenBallSum, nBallId);
						if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT&& areaNums[1].table.getOneBallStatue(nBallId) !=0) {
							areaNums[1].table.clearOnBallHighlight(nBallId);
							toast.setText(getResources().getString(R.string.ssq_toast_danma_title));
							toast.show();
						}
		
					}else if(i==1){
						int isHighLight = areaNums[1].table.changeBallState(areaNums[1].chosenBallSum, nBallId);
						if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT&& areaNums[0].table.getOneBallStatue(nBallId) !=0) {
							areaNums[0].table.clearOnBallHighlight(nBallId);
							toast.setText(getResources().getString(R.string.ssq_toast_tuoma_title));
							toast.show();
						}
					}
			}else  if(state.equals("Q2")){
						if(i==0){
							 int isHighLight = areaNums[0].table.changeBallState(areaNums[0].chosenBallSum, nBallId);
							 if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
								areaNums[1].table.clearOnBallHighlight(nBallId);
							 }
						}else{
							int isHighLight = areaNums[1].table.changeBallState(areaNums[1].chosenBallSum, nBallId);
							if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
								areaNums[0].table.clearOnBallHighlight(nBallId);
							}
						}			  
				  }else if(state.equals("Q3")){
						if(i==0){
							 int isHighLight = areaNums[0].table.changeBallState(areaNums[0].chosenBallSum, nBallId);
							 if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
								areaNums[1].table.clearOnBallHighlight(nBallId);
								areaNums[2].table.clearOnBallHighlight(nBallId);
							 }
						}else if(i==1){
							int isHighLight = areaNums[1].table.changeBallState(areaNums[1].chosenBallSum, nBallId);
							if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
								areaNums[0].table.clearOnBallHighlight(nBallId);
								areaNums[2].table.clearOnBallHighlight(nBallId);
							}
						}else{
							int isHighLight = areaNums[2].table.changeBallState(areaNums[2].chosenBallSum, nBallId);
							if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
								areaNums[0].table.clearOnBallHighlight(nBallId);
								areaNums[1].table.clearOnBallHighlight(nBallId);
							}
						}	
				  }else{
					  areaNums[i].table.changeBallState(areaNums[i].chosenBallSum, nBallId);
				  }
				  break;
			}

	     }

	}
	
	 /**
	    * 点击小球提示金额
	    * @param areaNum
	    * @param iProgressBeishu
	    * @return
	    */
	   public String textSumMoney(AreaNum areaNum[],int iProgressBeishu){
		   String textSum = "";
		   int iZhuShu = getZhuShu();
		   if(is11_5DanTuo){
			   int dan = areaNum[0].table.getHighlightBallNums();
	    	   int tuo = areaNum[1].table.getHighlightBallNums();
	    	   if (dan + tuo < num+1 ) {
	   			int num2 =num+1-dan-tuo;
	            	  if(dan==0){
	            		  textSum= "至少选择1个胆码";  
	             	  }else{
	             		 textSum= "至少还需要"+num2+"个拖码";
	             	  } 
	   		} else if(tuo==0){
	   			textSum= "至少选择1个胆码";  
	   		}else {
	   			textSum = "共" + iZhuShu + "注，共"+ (iZhuShu * 2) + "元";
	   	
	   		}
		   }else if(state.equals("Q2")){//求排序
	    	   int oneNum = areaNum[0].table.getHighlightBallNums();
	    	   int twoNum = areaNum[1].table.getHighlightBallNums();
	    	   if(oneNum==0){
	    		   textSum = "第一位还需要1个小球";
	    	   }else if(twoNum==0){
	    		   textSum = "第二位还需要1个小球";
	    	   }else{
				   textSum = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
	    	   }
		   }else if(state.equals("Q3")){
				if(isMove&&itemViewArray.get(newPosition).isZHmiss){
					int onClickNum = getClickNum();
					if(onClickNum == 0){
						textSum = getResources().getString(R.string.please_choose_number);
					}else{
						textSum = "共" + onClickNum + "注，共" + (onClickNum * 2) + "元";
					}
				}else{
				   int oneNum = areaNum[0].table.getHighlightBallNums();
				   int twoNum = areaNum[1].table.getHighlightBallNums();
				   int thirdNum = areaNum[2].table.getHighlightBallNums();
				   if(oneNum==0){
		    		   textSum = "第一位还需要1个小球";
		    	   }else if(twoNum==0){
		    		   textSum = "第二位还需要1个小球";
		    	   }else if(thirdNum==0){
		    		   textSum = "第三位还需要1个小球";
		    	   }else{
					   textSum = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
		    	   }
				}
		   }else if(state.equals("R5")||state.equals("R7")||state.equals("R8")){//求组合
				if(isMove&&itemViewArray.get(newPosition).isZHmiss){
					int onClickNum = getClickNum();
					if(onClickNum == 0){
						textSum = getResources().getString(R.string.please_choose_number);
					}else{
						textSum = "共" + onClickNum + "注，共" + (onClickNum * 2) + "元";
					}
				}else{
				   int ballNums = areaNum[0].table.getHighlightBallNums();
				   int oneNum = num - ballNums;
				   if(oneNum>0){
					   textSum = "还需要"+oneNum+"个小球";
				   }else{
					   textSum = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
				   }
				}
			   
		   }else{
			   int ballNums = areaNum[0].table.getHighlightBallNums();
			   int oneNum = num - ballNums;
			   if(oneNum>0){
				   textSum = "还需要"+oneNum+"个小球";
			   }else{
				   textSum = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
			   }
		   }
		return textSum ;
		   
	   };
	   /**
	    * 判断是否满足投注条件
	    */
	   public String  isTouzhu(){
		  String isTouzhu = "";
			  int iZhuShu = getZhuShu();
			  if(is11_5DanTuo){
				  
				  int dan = areaNums[0].table.getHighlightBallNums();
					int tuo =areaNums[1].table.getHighlightBallNums();
					if ( dan + tuo < num+1 || dan < 1
							|| dan > num-1 
							|| tuo < 2 || tuo > 10) {
						if(state.equals("R2")||state.equals("Z2")){
							isTouzhu = "请选择:\"1个胆码；\n" + " 2~10个拖码；\n" + " 胆码与拖码个数之和不小于"+(num+1)+"个";
						}else{
						isTouzhu = "请选择:\n1~"+(num-1)+"个胆码；\n" + " 2~10个拖码；\n" + " 胆码与拖码个数之和不小于"+(num+1)+"个";
						}
					} else if (iZhuShu > MAX_ZHU) {
						isTouzhu = "false";
					} else {
						isTouzhu = "true";
					}
				  
			  }else if(state.equals("Q2")){

		    	   if(iZhuShu==0){
		    		    isTouzhu = "请在第一位和第二位至少选择一个球，再进行投注！";
		    	   }else if(iZhuShu  > MAX_ZHU) {
						isTouzhu = "false";
				   }else{
					   isTouzhu = "true";
				   }
		      }else if(state.equals("Q3")){
		  		if(isMove&&itemViewArray.get(newPosition).isZHmiss){
					int onClickNum = getClickNum();
					if(onClickNum==0){
						isTouzhu = "请至少选择一注！";
					}else{
						isTouzhu = "true";
					}
				}else{
		    	   if(iZhuShu==0){
		    		   isTouzhu = "请在第一位、第二位和第三位至少选择一个球，再进行投注！";
		    	   }else if(iZhuShu  > MAX_ZHU) {
		    		   isTouzhu = "false";
		    	   }else{
					   isTouzhu = "true";
				   }
				}
		      }else if(state.equals("R5")||state.equals("R7")||state.equals("R8")){  
		  		if(isMove&&itemViewArray.get(newPosition).isZHmiss){
					int onClickNum = getClickNum();
					if(onClickNum==0){
						isTouzhu = "请至少选择一注！";
					}else{
						isTouzhu = "true";
					}
				}else{
		   	       int ballNums = areaNums[0].table.getHighlightBallNums();
				   int oneNum = num - ballNums;
				   if(oneNum>0){
					   isTouzhu = "请再选择"+oneNum+"球，再进行投注！";
				   }else if(iZhuShu  > MAX_ZHU) {
						isTouzhu = "false";
				   }else{
						isTouzhu = "true";
				   }
				}
		      }else{
		   	       int ballNums = areaNums[0].table.getHighlightBallNums();
				   int oneNum = num - ballNums;
				   if(oneNum>0){
					   isTouzhu = "请再选择"+oneNum+"球，再进行投注！";
				   }else if(iZhuShu  > MAX_ZHU) {
						isTouzhu = "false";
				   }else{
						isTouzhu = "true";
				   }
		      }
		      return isTouzhu;
	   }
	   /**
	    * 投注注码
	    * @return
	    */
	   public String getZhuma(){
		   String zhuma="";
		   if(is11_5DanTuo){
		   zhuma = DlcDanTuoCode.zhuma(areaNums, state);
		   }else{
		   zhuma = DlcCode.zhuma(areaNums, state);
		   }
		   return zhuma;
	   }
		@Override
		public String getZhuma(Balls ball) {
			   String zhuma="";
			   zhuma = DlcRxBalls.getZhuma(ball, state); 
			   return zhuma;
		}
		
	   
	   /**
	    * 自选提醒框注码
	    * @return
	    */
	   public String getZxAlertZhuma(){
		int iZhuShu = getZhuShu();
		String zhuma = "";
		if(is11_5DanTuo){
			int[] dan = areaNums[0].table.getHighlightBallNOs();
			int[] tuo = areaNums[1].table.getHighlightBallNOs();
			zhuma = "胆码："+ PublicMethod.getStrZhuMa(dan)
					+"\n托码："+ PublicMethod.getStrZhuMa(tuo);
		}else if(state.equals("Q2")){
			int[] one = areaNums[0].table.getHighlightBallNOs();
			int[] two = areaNums[1].table.getHighlightBallNOs();
			zhuma = "第一位："+ PublicMethod.getStrZhuMa(one)
			+"\n第二位："+ PublicMethod.getStrZhuMa(two);
		}else if(state.equals("Q3")){
			int[] one = areaNums[0].table.getHighlightBallNOs();
			int[] two = areaNums[1].table.getHighlightBallNOs();
			int[] third = areaNums[2].table.getHighlightBallNOs();
			zhuma = "第一位："+ PublicMethod.getStrZhuMa(one)
			+"\n第二位："+ PublicMethod.getStrZhuMa(two)
			+"\n第三位："+ PublicMethod.getStrZhuMa(third);
		}else{
			int[] one = areaNums[0].table.getHighlightBallNOs();
			zhuma = PublicMethod.getStrZhuMa(one);
		}
		codeStr = "注码：\n"+zhuma;
		return codeStr;   
	   }


	   /**
	    * 获得总注数
	    * @return
	    */
	   public int getZhuShu(){
		   int zhushu = 0;
		   if(isJiXuan){
			   zhushu = balls.size()*iProgressBeishu;
		   }else if(is11_5DanTuo){
			   int dan = areaNums[0].table.getHighlightBallNums();
	    	   int tuo = areaNums[1].table.getHighlightBallNums();
	    	   zhushu = (int) getDTZhuShu(dan, tuo,iProgressBeishu);
		   }else {
			if(state.equals("Q2")){
		     	   int oneNum = areaNums[0].table.getHighlightBallNums();
		    	   int twoNum = areaNums[1].table.getHighlightBallNums();
		    	   zhushu = oneNum*twoNum*iProgressBeishu;
			}else if(state.equals("Q3")){
				   int oneNum = areaNums[0].table.getHighlightBallNums();
				   int twoNum = areaNums[1].table.getHighlightBallNums();
				   int thirdNum = areaNums[2].table.getHighlightBallNums();
		    	   zhushu = oneNum*twoNum*thirdNum*iProgressBeishu;
			}else{
				   int ballNums = areaNums[0].table.getHighlightBallNums();
				   zhushu = (int) PublicMethod.zuhe(num,ballNums)*iProgressBeishu;
			}
		   }
		return zhushu;
	   }
	   
		/**
		 * 复式玩法注数计算方法
		 * @param int aRedBalls 红球个数
		 * 
		 * @return long 注数
		 */
		protected long getDTZhuShu(int dan, int tuo,int iProgressBeishu) {
			long ssqZhuShu = 0L;
			if (dan > 0 && tuo > 0 ) {
				ssqZhuShu += (PublicMethod.zuhe(num - dan, tuo) * iProgressBeishu);
			}
			return ssqZhuShu;
		}
		/**
		 * 添加到号码篮
		 */
		public void getCodeInfo(AddView addView){
			int zhuShu = getZhuShu();
			CodeInfo codeInfo = addView.initCodeInfo(getAmt(zhuShu), zhuShu);
			setLotoNoAndType(codeInfo);
			code.setZHmiss(false);
			codeInfo.setTouZhuCode(getZhuma());
			for(AreaNum areaNum:areaNums){
				int[] codes = areaNum.table.getHighlightBallNOs();
				hightballs=codes.length;
				String codeStr = "";
				for (int i = 0; i < codes.length; i++) {
					codeStr += PublicMethod.isTen(codes[i]);
					if (i != codes.length - 1) {
						codeStr += ",";
					}
				}
				codeInfo.addAreaCode(codeStr,areaNum.textColor);
			}   
			addView.addCodeInfo(codeInfo);
		}
		/**
		 * 组合遗漏添加到选号篮
		 * @param addView
		 */
		public void getZCodeInfo(AddView addView){
			List<MyButton> missBtnList = itemViewArray.get(newPosition).missBtnList;
			for(int i=0;i<missBtnList.size();i++){
				MyButton myBtn = missBtnList.get(i);
				if(myBtn.isOnClick()){
					int zhuShu = 1;
					CodeInfo codeInfo = addView.initCodeInfo(getAmt(zhuShu), zhuShu);
					String codeStr = myBtn.getBtnText();
					code.setZHmiss(true);
					code.setIsZHcode(codeStr);
					codeInfo.setTouZhuCode(getZhuma());
					codeInfo.addAreaCode(codeStr,Color.RED);
					addView.addCodeInfo(codeInfo);
				}
			}
		}
	   /**
	    * 投注联网
	    */
	   public void touzhuNet(){
			int zhuShu=getZhuShu();
			if(isJiXuan){
			betAndGift.setSellway("1")	;
			}else{
			betAndGift.setSellway("0");
			}//1代表机选 0代表自选
			betAndGift.setLotno(lotno);
			betAndGift.setBet_code(getZhuma());
			betAndGift.setAmount(""+zhuShu*200);
			betAndGift.setBatchcode(batchCode);
		   
	   }

		protected void onStart() {
			// TODO Auto-generated method stub
			super.onStart();

		}

		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			setLotno();
	       
		}

		protected void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
		}   
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			isRun = false;
			batchCode = "";
		}
		
	void setLotoNoAndType(CodeInfo codeInfo){
		codeInfo.setLotoNo(Constants.LOTNO_11_5);
		if(radioId == 1){
			codeInfo.setTouZhuType("dantuo");
		}else{
			codeInfo.setTouZhuType("zhixuan");
		}
	}

}
