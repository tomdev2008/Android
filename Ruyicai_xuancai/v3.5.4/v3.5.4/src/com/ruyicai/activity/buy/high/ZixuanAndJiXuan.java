package com.ruyicai.activity.buy.high;
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
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroidxuancai.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.BaseActivity;
import com.ruyicai.activity.buy.dlc.Dlc;
import com.ruyicai.activity.buy.miss.BuyViewItemMiss;
import com.ruyicai.activity.buy.miss.MainViewPagerAdapter;
import com.ruyicai.activity.buy.miss.NumViewItem;
import com.ruyicai.activity.buy.miss.ZHmissViewItem;
import com.ruyicai.activity.buy.ssc.Ssc;
import com.ruyicai.activity.buy.zixuan.AddView;
import com.ruyicai.activity.buy.zixuan.JiXuanBtn;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.code.CodeInterface;
import com.ruyicai.code.ssc.OneStarCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.custom.jc.button.MyButton;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.jixuan.SscBalls;
import com.ruyicai.json.miss.MissConstant;
import com.ruyicai.json.miss.MissJson;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.MissInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.ruyicai.util.SensorActivity;

public abstract class ZixuanAndJiXuan extends BaseActivity implements OnCheckedChangeListener,OnClickListener,SeekBar.OnSeekBarChangeListener,HandlerMsg{
	protected int BallResId[] = { R.drawable.grey, R.drawable.red };
	protected LayoutInflater inflater;
	protected LinearLayout buyview;
	protected BallTable   BallTable ;
	protected RadioGroup group;
	protected CodeInterface sscCode = new OneStarCode();
	protected String []  childtype=null;
	protected boolean isbigsmall=false;
	public int iProgressBeishu = 1, iProgressQishu = 1;
	public BetAndGiftPojo betAndGift=new BetAndGiftPojo();//投注信息类
	MyHandler handler = new MyHandler(this);//自定义handler
	public String phonenum,sessionId,userno;
	ProgressDialog progressdialog;
	public String codeStr;
	public String lotno,sellWay = MissConstant.SSC_5X_ZX;
	public String highttype;
	public int type;
	public final static int NULL = 0;
	public final static int ONE = 1;
	public final static int TWO = 2;
	public final static int THREE = 3;
	public final static int FIVE = 5;
	public final static int TWO_ZUXUAN = 6;
	public final static int TWO_HEZHI = 7;
	public final static int FIVE_TONGXUAN = 8;
	public final static int BIG_SMALL = 9;
	public final static String PAGE = "1";
	public final static String MAX = "5";
	public final static int TIME = 5*60000;//获取期号线程刷新时间单位是分
	private final static String ERROR_WIN = "0000";
	int iZhuShu;
	int zhushuforshouyi;
    Dialog touZhuDialog;
	TextView issueText;
	TextView alertText;
	TextView textZhuma;
	public static String lotnoStr;
	public static boolean isTime = true;
	public static boolean isStart = true;
	public static JSONArray prizeInfos = null;
	public boolean isViewEnd = true;
	public boolean isViewStart = true;
	public static long startTime;
	long endTime ;
	public  AddView addView;
	private boolean isJiXuan = false;
	private Button codeInfo;
	private TextView textTitleAlert;
	protected boolean isTen = true;
	protected int MAX_ZHU = 10000;//每笔最多为1万注
	private final int All_ZHU = 99;
	private Context context;
	public int radioId = 0;
	public boolean isMiss = true;//是否进行遗漏值查询
	public boolean isshouyi=false;
	public int hightballs;
	private CheckBox shouyi;
	private final int UP = 30;
	protected boolean isMove = false;
	public Map<Integer,HighItemView> missView = new HashMap<Integer,HighItemView>();
	float startX;
	float startY;
	protected LinearLayout childtypes;
	protected View layoutMain;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		setContentView(R.layout.sscbuyview);
		Constants.type="hight";
		childtype= new String[]{"直选","机选"};
	
	}
    /**
    * 点击小球提示金额
    * @param areaNum
    * @param iProgressBeishu
    * @return
    */
   public abstract String textSumMoney(AreaNum areaNum[],int iProgressBeishu);
   /**
    * 判断是否满足投注条件
    */
   public abstract String isTouzhu();
   /**
    * 返回注数
    */
   public abstract int getZhuShu();
   /**
    * 返回投注提示框提示信息
    */
   public abstract String getZhuma();
   /**
    * 返回机选投注注码
    */
   public abstract String getZhuma(Balls ball);
   /**
    * 投注联网信息
    */
   public abstract void touzhuNet();
   /**
    * 单选按钮响应事件
    * @param checkedId
    */
   public abstract void onCheckAction(int checkedId);
    
	public void init(){
	childtypes = (LinearLayout)findViewById(R.id.sscchildtype);
	childtypes.setVisibility(View.VISIBLE);
	childtypes.removeAllViews();
	buyview=(LinearLayout)findViewById(R.id.buyview);
	inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	group = new RadioGroup(this); 
	group.setOrientation(RadioGroup.HORIZONTAL);
	// 设置屏幕宽度
     for(int i = 0 ; i < childtype.length ; i++){
         RadioButton radio = new RadioButton(this);
         radio.setText(childtype[i]);
         radio.setTextColor(Color.BLACK);
         radio.setId(i);
         radio.setTextSize(14);
         radio.setButtonDrawable(R.drawable.radio_select);
         radio.setPadding(Constants.PADDING, 0, 15, 0);
         group.addView(radio);
         group.setOnCheckedChangeListener(this);
        }
    group.check(0);
     //将单选按钮组添加到布局中
    childtypes.addView(group);
	
	}
	/**
	 * 初始化选区
	 */
	public AreaNum[] initArea() {
		areaNums = new AreaNum[1];
        String title = "请选择投注号码" ;
		areaNums[0] = new AreaNum(10,10,1, 11, BallResId, 0, 0,Color.RED, title,false,true);
		return areaNums;
	}
	
	//单选框切换直选，机选
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		radioId = checkedId;
		switch(checkedId){		
		case 0:
			iProgressBeishu = 1;iProgressQishu = 1;
			initArea();
			createView(areaNums, sscCode,type,false,checkedId,true);
			BallTable=areaNums[0].table;
		break;
		case 1:
			iProgressBeishu = 1;iProgressQishu = 1;
            SscBalls onestar = new SscBalls(1);
			createviewmechine(onestar,checkedId);
		break;	
		}
			
	}
	
	private TextView mTextSumMoney;
	private ImageButton zixuanTouzhu;
	protected TextView textTitle;
	protected TextView textPrize;
	public SeekBar mSeekBarBeishu, mSeekBarQishu;
	private EditText mTextBeishu, mTextQishu;
    public int iScreenWidth;
    protected CodeInterface code;//注码接口类
    protected View view ;
	public Toast toast;
	private boolean toLogin = false;
//	public DrawerGallery mGallery;
//	public List<BuyViewItem> itemViewArray ;
	protected int lineNum = 3;//组合遗漏每行按钮数
	protected int textSize = 1; 
	
	protected ViewPager mGallery;
	// 缓存需要左右滑动的视图群的列表容器
	public List<BuyViewItemMiss> itemViewArray;
	
	
	/**
	 * 创建可滑动直选页面
	 * @param areaNum
	 * @param code
	 * @param type
	 * @param isTen
	 * id 当前view的id
	 */
	public void createViewNew(AreaNum areaNum[],CodeInterface code,int type,boolean isTen,int id) {
	   sensor.stopAction();
	   isJiXuan = false;
	   isMove = true;
	   setNewPosition(0);
	   this.code = code;
	   buyview.removeAllViews();
	   if(missView.get(id)==null){
		   inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   View zhixuanview = inflater.inflate(R.layout.ssczhixuan_new, null);
		   initZixuanView(areaNum,zhixuanview);
		   initViewItemNew(areaNum,isTen,zhixuanview);
		   initBotm(zhixuanview);
		   missView.put(id,new HighItemView(zhixuanview,areaNum,addView,itemViewArray,editZhuma));
		   missView.get(id).setmGallery(mGallery);
		   refreshView(type, id);
	   }else{
		   mGallery.setCurrentItem(0);
		   refreshView(type, id);
	   }
	   
	}
	/**
	 * 创建不可滑动直选页面
	 * @param areaNum
	 * @param code
	 * @param type
	 * @param isTen
	 */
	public void createView(AreaNum areaNum[],CodeInterface code,int type,boolean isTen,int id,boolean isMiss) {
	   sensor.stopAction();
	   isJiXuan = false;
	   isMove = false;
	   this.code = code;
	   buyview.removeAllViews();
	   if(missView.get(id)==null){
		   inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   View zhixuanview = inflater.inflate(R.layout.ssczhixuan, null);
		   initZixuanView(areaNum,zhixuanview);
		   initViewItem(areaNum,isTen,zhixuanview,isMiss);	
		   initBotm(zhixuanview);
		   missView.put(id,new HighItemView(zhixuanview,areaNum,addView,null,editZhuma));
		   refreshView(type, id);
	   }else{
		   refreshView(type, id);
	   }
	}
	/**
	 * 创建胆拖页面
	 * @param areaNum
	 * @param code
	 * @param type
	 * @param isTen
	 */
	public void createViewDanTuo(AreaNum areaNum[],CodeInterface code,int type,boolean isTen,int id,boolean isMiss) {
		   sensor.stopAction();
		   isJiXuan = false;
		   isMove = false;
		   this.code = code;
		   buyview.removeAllViews();
		   if(missView.get(id)==null){
			   inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			   View zhixuanview = inflater.inflate(R.layout.ssczhixuan, null);
			   initZixuanView(areaNum,zhixuanview);
			   initViewItemDan(areaNum,isTen,zhixuanview,isMiss);
			   initBotm(zhixuanview);
			   missView.put(id,new HighItemView(zhixuanview, areaNum,addView,null,editZhuma));
			   missView.get(id).setMissList(missView.get(0).getMissList());
			   refreshView(type, id);
		   }else{
			   refreshView(type, id);
			   missView.get(id).setMissList(missView.get(0).getMissList());
		   }
		   initMissText(missView.get(id).getAreaNum(),true,id);
	}
	private AddView initAddView(View zhixuanview,boolean isZixuan) {
		final TextView textNum = (TextView)zhixuanview.findViewById(R.id.buy_zixuan_add_text_num);
		return new AddView(textNum,this,isZixuan);
	}
	private void refreshView(int type, int id) {
		areaNums = missView.get(id).getAreaNum();
		addView = missView.get(id).getAddView();
		editZhuma = missView.get(id).editZhuma;
		itemViewArray = missView.get(id).getItemViewArray();
		if(missView.get(id).getmGallery()!=null){
			mGallery = missView.get(id).getmGallery();
		}
		this.type = type;
		showEditTitle(type);
		setTextPrize(type);
		buyview.addView(missView.get(id).getView());
	}
	private void initMissText(AreaNum areaNums[],boolean isDanTuo,int id) {
		List<List> missList = missView.get(id).getMissList();
		for(int i=0;i<areaNums.length;i++){
				int index = 0; 
				if(highttype.equals("SSC")){
					index = areaNums.length-1-i;
				}else if(highttype.equals("DLC")){
					index = i;
				}
				if(missList.size()>0&&missList.size()>index&&!isDanTuo){
					PublicMethod.setMissText(areaNums[i].table.textList, missList.get(index));
				}else if(missList.size()>0){
					PublicMethod.setMissText(areaNums[i].table.textList, missList.get(0));
				}
		}
	}
		
	private void initViewItemDan(AreaNum areaNums[],boolean isTen,View zhixuanview,boolean isMiss) {
		iScreenWidth = PublicMethod.getDisplayWidth(this);
		int tableLayoutIds[]={R.id.buy_zixuan_table_one,R.id.buy_zixuan_table_two,R.id.buy_zixuan_table_third,R.id.buy_zixuan_table_four,R.id.buy_zixuan_table_five};
		int textViewIds[]={R.id.buy_zixuan_text_one,R.id.buy_zixuan_text_two,R.id.buy_zixuan_text_third,R.id.buy_zixuan_text_four,R.id.buy_zixuan_text_five};
		int linearViewIds[]={R.id.buy_zixuan_linear_one,R.id.buy_zixuan_linear_two,R.id.buy_zixuan_linear_third,R.id.buy_zixuan_linear_four,R.id.buy_zixuan_linear_five};
		//初始化选区
		for(int i=0;i<areaNums.length;i++){
			areaNums[i].initView(tableLayoutIds[i],textViewIds[i],linearViewIds[i],zhixuanview);
			AreaNum areaNum = areaNums[i];
			if(i!=0){
				areaNum.aIdStart=areaNums[i-1].areaNum+areaNums[i-1].aIdStart;
			}
			areaNums[i].table =  makeBallTable(areaNums[i].tableLayout, iScreenWidth, areaNum.areaNum,areaNum.ballResId, areaNum.aIdStart, areaNum.aBallViewText, this, this,isTen,null,isMiss);
			areaNums[i].init();

		}
	}
	/**
	 * 初始化自选底部
	 * @param type
	 */
	private void initBotm(View zhixuanview) {
		Button add_dialog = (Button) zhixuanview.findViewById(R.id.buy_zixuan_img_add_delet);
		final TextView textNum = (TextView)zhixuanview.findViewById(R.id.buy_zixuan_add_text_num);
		addView = new AddView(textNum,this,false);
		add_dialog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(addView.getSize()>0){
					showAddViewDialog();
				}else{
					Toast.makeText(ZixuanAndJiXuan.this, ZixuanAndJiXuan.this.getString(R.string.buy_add_dialog_alert), Toast.LENGTH_SHORT).show();	
				}
			}
		});
		Button add = (Button) zhixuanview.findViewById(R.id.buy_zixuan_img_add);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String alertStr = isTouzhu();
				if(alertStr.equals("true")){
					addToCodes();
				}else if(alertStr.equals("false")){
					dialogExcessive();
				}else{
					alertInfo(alertStr);
				}
			}
		
		});
		
		Button delete = (Button)zhixuanview.findViewById(R.id.buy_zixuan_img_delele);
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<areaNums.length;i++){
		             areaNums[i].table.clearAllHighlights();
			     }
			}
		});
		Button zixuanTouzhu = (Button) zhixuanview.findViewById(R.id.buy_zixuan_img_touzhu);
		zixuanTouzhu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				beginTouZhu();
			}
		});
	}
	private void initViewItem(AreaNum[] areaNums,boolean isTen,View zhixuanview,boolean isMiss){
		    iScreenWidth = PublicMethod.getDisplayWidth(this);
			int tableLayoutIds[]={R.id.buy_zixuan_table_one,R.id.buy_zixuan_table_two,R.id.buy_zixuan_table_third,R.id.buy_zixuan_table_four,R.id.buy_zixuan_table_five};
			int textViewIds[]={R.id.buy_zixuan_text_one,R.id.buy_zixuan_text_two,R.id.buy_zixuan_text_third,R.id.buy_zixuan_text_four,R.id.buy_zixuan_text_five};
			int linearViewIds[]={R.id.buy_zixuan_linear_one,R.id.buy_zixuan_linear_two,R.id.buy_zixuan_linear_third,R.id.buy_zixuan_linear_four,R.id.buy_zixuan_linear_five};
			
			//初始化选区
			for(int i=0;i<areaNums.length;i++){
	        	areaNums[i].initView(tableLayoutIds[i],textViewIds[i],linearViewIds[i],zhixuanview);
				AreaNum areaNum = areaNums[i];
				if(i!=0){
					areaNum.aIdStart=areaNums[i-1].areaNum+areaNums[i-1].aIdStart;
				}
				areaNums[i].table =  makeBallTable(areaNums[i].tableLayout, iScreenWidth, areaNum.areaNum,areaNum.ballResId, areaNum.aIdStart, areaNum.aBallViewText, this, this,isTen,null,isMiss);
				areaNums[i].init();
				Button btn = new Button(this);
				Button btnDw = new Button(this);
				if(areaNum.isJxBtn){
					btn.setVisibility(Button.VISIBLE);
					btnDw.setVisibility(Button.VISIBLE);
					areaNum.jixuanBtn = new JiXuanBtn(areaNum.isRed, btn,btnDw, areaNum.chosenBallSum,this,view,areaNum .table,areaNum.areaMin,i);
				}else{
					btn.setVisibility(Button.GONE);
					btnDw.setVisibility(Button.GONE);
					areaNum.jixuanBtn = new JiXuanBtn(areaNum.isRed, btn,btnDw, areaNum.chosenBallSum,this,view,areaNum .table,areaNum.areaMin,i);
				}
				if(areaNum.isSensor){
					this.areaNums = areaNums;
				}
				
			}
	}

	/**
	 * 初始化选区界面
	 */
	public void initViewItemNew(AreaNum[] areaNum,boolean isTen,View zhixuanview) {
	   mGallery = (ViewPager) zhixuanview.findViewById(R.id.buy_zixuan_viewpager);
	   mGallery.removeAllViews();	
	   NumViewItem numView = new NumViewItem(this,areaNum,null,true);
	   ZHmissViewItem zhView = new ZHmissViewItem(this,null,lineNum,textSize);
	   itemViewArray = new ArrayList<BuyViewItemMiss>();
	   itemViewArray.add(numView);
	   itemViewArray.add(zhView);
		// 设置 ViewPager 的 Adapter
	   MainViewPagerAdapter MianAdapter = new MainViewPagerAdapter(null);
	   View view = numView.createView();
	   numView.rightBtn(view);
	   numView.rightBtnBG(R.drawable.buy_zh_miss_btn);
	   
	   MianAdapter.addView(view);
	   MianAdapter.addView(zhView.createView());
	   
	   mGallery.setAdapter(MianAdapter);
		// 设置第一显示页面
	   mGallery.setCurrentItem(0);
	   mGallery.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
                setNewPosition(arg0);
				// activity从1到2滑动，2被加载后掉用此方法
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// 从1到2滑动，在1滑动前调用
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});//设置监听器
	}
	/**
	 * 初始化滑动
	 */
	public void initGallery() {

	}
	private List<String> test(){
		List<String> missList = new ArrayList<String>();
		for(int i=0;i<10;i++){
			missList.add(""+i*20);
		}
		return missList;
	}
	private void initZixuanView(AreaNum[] areaNum,View zhixuanview) {
		mTextSumMoney = (TextView)zhixuanview. findViewById(R.id.buy_zixuan_text_sum_money);
		editZhuma = (EditText) zhixuanview.findViewById(R.id.buy_zixuan_edit_zhuma);
		textTitle = (TextView) zhixuanview.findViewById(R.id.buy_zixuan_text_title);
		textPrize = (TextView) zhixuanview.findViewById(R.id.buy_zixuan_text_prize);
		mTextSumMoney.setText(getResources().getString(R.string.please_choose_number));
	}
	

	/**
	 * 将选取信息添加到号码篮里
	 */
	private void addToCodes() {
		if(getZhuShu()>MAX_ZHU){
			dialogExcessive();
		}else if(addView.getSize()>=All_ZHU){
			Toast.makeText(ZixuanAndJiXuan.this,ZixuanAndJiXuan.this.getString(R.string.buy_add_view_zhu_alert) , Toast.LENGTH_SHORT).show();
		}else{
			if(type == BIG_SMALL){
				getCodeInfoDX(addView);
			}else{
				if(isMove&&itemViewArray.get(newPosition).isZHmiss){
					getZCodeInfo(addView);
				}else{
					getCodeInfo(addView);
				}
			}
			addView.updateTextNum();
			again();
		}
	}
	private void showAddViewDialog() {
		addView.createDialog(ZixuanAndJiXuan.this.getString(R.string.buy_add_dialog_title));
		addView.showDialog();
	}
	public void getCodeInfo(AddView addView){
		int zhuShu = getZhuShu();
		CodeInfo codeInfo = addView.initCodeInfo(getAmt(zhuShu), zhuShu);
		code.setZHmiss(false);
		codeInfo.setTouZhuCode(getZhuma());
		for(AreaNum areaNum:areaNums){
			int[] codes = areaNum.table.getHighlightBallNOs();
			hightballs=codes.length;
			String codeStr = "";
			for (int i = 0; i < codes.length; i++) {
				codeStr += codes[i];
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
				String[] alertStr = codeStr.split("\\,");
				for(String str:alertStr){
					codeInfo.addAreaCode(str,Color.RED);
				}
				addView.addCodeInfo(codeInfo);
			}
		}
	}
	/**
	 * 计算组合遗漏选中按钮数
	 * @return
	 */
	public int getClickNum() {
		int onClickNum = 0;
		List<MyButton> missBtnList = itemViewArray.get(newPosition).missBtnList;
		for(int i=0;i<missBtnList.size();i++){
			MyButton myBtn = missBtnList.get(i);
			if(myBtn.isOnClick()){
				onClickNum++;
			}
		}
		return onClickNum;
	}
	/**
	 * 添加大小
	 * @param addView
	 */
	public void getCodeInfoDX(AddView addView){
		int zhuShu = getZhuShu();
		CodeInfo codeInfo = addView.initCodeInfo(getAmt(zhuShu), zhuShu);
		codeInfo.setTouZhuCode(getZhuma());
		for(AreaNum areaNum:areaNums){
			String[] codes = areaNum.table.getHighlightStr();
			String codeStr = "";
			for (int i = 0; i < codes.length; i++) {
				codeStr += codes[i];
				if (i != codes.length - 1) {
					codeStr += ",";
				}
			}
			codeInfo.addAreaCode(codeStr,areaNum.textColor);
		}   
		addView.addCodeInfo(codeInfo);
	}
	public int getAmt(int zhuShu){
		if(betAndGift!=null){
			return zhuShu * betAndGift.getAmt();
		}else{
			return 0;
		}
		
	}
	public void showEditTitle(int type){
		textTitle.setTextSize(11);
		textTitle.setText("已选:");
		textTitle.setTextSize(15);
	}
	public void setTextPrize(int type){
		textPrize.setTextSize(11);
		switch(type){
		case ONE:
			textPrize.setText(getString(R.string.ssc_prize_one));
			break;
		case TWO:
			textPrize.setText(getString(R.string.ssc_prize_two_zx));
			break;
		case THREE:
			textPrize.setText(getString(R.string.ssc_prize_third));
			break;
		case FIVE:
			textPrize.setText(getString(R.string.ssc_prize_five_zx));
			break;
		case TWO_ZUXUAN:
			textPrize.setText(getString(R.string.ssc_prize_two_zu));
			break;
		case TWO_HEZHI:
			textPrize.setText(getString(R.string.ssc_prize_two_hz));
			break;
		case FIVE_TONGXUAN:
			textPrize.setText(getString(R.string.ssc_prize_five_tx));
			break;
		case BIG_SMALL:
			textPrize.setText(getString(R.string.ssc_prize__dx));
			break;
		}
		
	}
	//创建机选页面

	private Spinner jixuanZhu;
	private LinearLayout zhumaView;
	private SsqSensor sensor = new SsqSensor(this);
	private boolean isOnclik = true;
	public Vector<Balls> balls = new Vector();
	protected Balls ballOne;
	public Toast toastjixuan;
	
	public void createviewmechine(Balls balles,int id){
	   isJiXuan = true;
	   isMove = false;
	   buyview.removeAllViews();
	   if(missView.get(id)==null){
		   View jixuanview = inflater.inflate(R.layout.sscmechine, null);
		   initJiXuanView(balles,jixuanview);
		   missView.put(id,new HighItemView(jixuanview,null,addView,null,editZhuma));
	   }else{
		   initJiXuanView(balles,missView.get(id).getView());
	   }
	}
	private void initJiXuanView(Balls balles,View jixuanview) {
		   buyview.addView(jixuanview);
		   sensor.startAction();
		   this.ballOne = balles;
		   zhumaView = (LinearLayout)jixuanview. findViewById(R.id.buy_danshi_jixuan_linear_zhuma);
		   zhumaView.removeAllViews();
		   toastjixuan = Toast.makeText(this, "左右摇晃手机，重新选号！", Toast.LENGTH_SHORT);
		   toastjixuan.show();
		   balls = new Vector<Balls>();
				// 初始化spinner
				jixuanZhu = (Spinner) jixuanview.findViewById(R.id.buy_danshi_jixuan_spinner);
				if(isbigsmall){
				TextView textview = (TextView)jixuanview.findViewById(R.id.TextView01);
				textview.setText("注数:一注");
				jixuanZhu.setVisibility(View.GONE);	
				jixuanZhu.setSelection(0);
				}else{
				jixuanZhu.setSelection(4);
				jixuanZhu.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						int position = jixuanZhu.getSelectedItemPosition();
						if (isOnclik) {
							zhumaView.removeAllViews();
							balls = new Vector();
							for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
								Balls ball = ballOne.createBalls();
								balls.add(ball);
							}
							createTable(zhumaView);
						} else {
							isOnclik = true;
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});
			}
   
			int index = jixuanZhu.getSelectedItemPosition() + 1;
			for (int i = 0; i < index; i++) {
				Balls ball = ballOne.createBalls();
				balls.add(ball);
			}
			createTable(zhumaView);
			sensor.onVibrator();// 震动
			Button zixuanTouzhu = (Button)jixuanview. findViewById(R.id.buy_danshi_jixuan_img_touzhu);
			zixuanTouzhu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					beginTouZhu();
				}
			});
			final TextView textNum = (TextView)findViewById(R.id.buy_zixuan_add_text_num);
			Button add_dialog = (Button) findViewById(R.id.buy_zixuan_img_add_delet);
			addView = new AddView(textNum,this,false);
			add_dialog.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(addView.getSize()>0){
						showAddViewDialog();
					}else{
						 Toast.makeText(ZixuanAndJiXuan.this, ZixuanAndJiXuan.this.getString(R.string.buy_add_dialog_alert), Toast.LENGTH_SHORT).show();	
					}
				}
			});
			Button add = (Button) findViewById(R.id.buy_zixuan_img_add);
			add.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
		           addJxToCodes();
				}
			
			});
	}
	/**
	 * 将选取信息添加到号码篮里
	 */
	private void addJxToCodes() {
			if(addView.getSize()+balls.size()-1>=All_ZHU){
				Toast.makeText(ZixuanAndJiXuan.this,ZixuanAndJiXuan.this.getString(R.string.buy_add_view_zhu_alert) , Toast.LENGTH_SHORT).show();
			}else{
				if(type == BIG_SMALL){
					getJxCodeInfoDX(addView);
				}else{
					getJxCodeInfo(addView);
				}
				addView.updateTextNum();
				againJx();
			}
	}
	
	public void getJxCodeInfo(AddView addView){
		for(int i=0;i<balls.size();i++){
			Balls ball = balls.get(i);
			String codeStr = getAddZhuma(i);
			CodeInfo codeInfo = addView.initCodeInfo(betAndGift.getAmt(), 1);
			codeInfo.addAreaCode(codeStr,Color.BLACK);
			codeInfo.setTouZhuCode(getZhuma(ball));
			addView.addCodeInfo(codeInfo);
		}   
		addView.setIsJXcode(ballOne.getZhuma(balls, iProgressBeishu));
	}
	public void getJxCodeInfoDX(AddView addView){
		for(int i=0;i<balls.size();i++){
			Balls ball = balls.get(i);
			String codeStr = getAddZhumaDX(i);
			CodeInfo codeInfo = addView.initCodeInfo(betAndGift.getAmt(), 1);
			codeInfo.addAreaCode(codeStr,Color.BLACK);
			codeInfo.setTouZhuCode(getZhuma(ball));
			addView.addCodeInfo(codeInfo);
		}   
		addView.setIsJXcode(ballOne.getZhuma(balls, iProgressBeishu));
	}
	/**
	 * 重新选择方法
	 */
	private void againJx() {
		zhumaView.removeAllViews();
		balls = new Vector();
		for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
			Balls ball = ballOne.createBalls();
			balls.add(ball);
		}
		createTable(zhumaView);
	}
	/**
	 * 购彩篮显示注码
	 * @return
	 */
	private String getAddZhuma(int index){
		String zhumaString = "";
			for(int j=0;j<balls.get(index).getVZhuma().size();j++){
				if(isTen){
					zhumaString += balls.get(index).getTenShowZhuma(j);
				}else{
					zhumaString += balls.get(index).getShowZhuma(j);
				}
				
				if(j!=balls.get(index).getVZhuma().size()-1){
					zhumaString += "|";
				}
			}
		return zhumaString ;	
	}
	/**
	 * 购彩篮显示注码
	 * @return
	 */
	private String getAddZhumaDX(int index){
		String zhumaString = "";
			for(int j=0;j<balls.get(index).getVZhuma().size();j++){
				 String str =balls.get(index).getTenShowZhuma(j);
				if(str.equals("00")){
					zhumaString += "大";
				}else if(str.equals("01")){
					zhumaString += "小";
				}else if(str.equals("02")){
					zhumaString += "单";
				}else if(str.equals("03")){
					zhumaString += "双";
				}
				if(j!=balls.get(index).getVZhuma().size()-1){
					zhumaString += "|";
				}
			}
		return zhumaString ;	
	}
	//机选创建小球
	public void createTable(LinearLayout layout) {
		for (int i = 0; i < balls.size(); i++) {
			final int index = i;
			int iScreenWidth = PublicMethod.getDisplayWidth(this);
			LinearLayout lines = new LinearLayout(layout.getContext());
			for(int j=0;j<balls.get(i).getVZhuma().size();j++){
			    String color = (String) balls.get(i).getVColor().get(j);
			    TableLayout table;
			    if(isbigsmall){
				table = PublicMethod.makeBallTableJiXuanbigsmall(null,iScreenWidth,BallResId, balls.get(i).getBalls(j), this); 	
			    }else
			    {
				table = PublicMethod.makeBallTableJiXuan(null,iScreenWidth,BallResId, balls.get(i).getBalls(j), this);
			    }
				lines.addView(table);
			}	
			ImageButton delet = new ImageButton(lines.getContext());
			delet.setBackgroundResource(R.drawable.shanchu);
			delet.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (balls.size() > 1) {
						zhumaView.removeAllViews();
						balls.remove(index);
						isOnclik = false;
						jixuanZhu.setSelection(balls.size() - 1);
						createTable(zhumaView);
					} else {
						Toast.makeText(ZixuanAndJiXuan.this, getResources().getText(R.string.zhixuan_jixuan_toast),Toast.LENGTH_SHORT).show();

					}
   
				}
			});
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			param.setMargins(10, 5, 0, 0);
			lines.addView(delet, param);
			lines.setGravity(Gravity.CENTER_HORIZONTAL);
            if(i%2==0){
				lines.setBackgroundResource(R.drawable.jixuan_list_bg);
			}
            lines.setPadding(0, 3, 0, 0);
			layout.addView(lines);
			
		}

	}
	/**
	 * 投注方法
	 */
	public void beginTouZhu() {
            if(isJiXuan){
            	touZhuJX();
            }else{
            	touZhuZX();
            }
	}
	public void touZhuJX(){
		
		if (balls.size() == 0) {
			alertInfo("请至少选择1注彩票");
		} else {
			if(addView.getSize()==0){
				addJxToCodes();
				alertJX(); 
			}else{
				showAddViewDialog();
			}
			
		}
	}
	public void touZhuZX(){
		String alertStr = isTouzhu();
		if(alertStr.equals("true")&&addView.getSize()==0){
			addToCodes();
			alertZX();
		}else if(alertStr.equals("true")&&addView.getSize()>0){
			addToCodes();
			showAddViewDialog();
		}else if(addView.getSize()>0){
			showAddViewDialog();
		}else if(alertStr.equals("false")){
			dialogExcessive();
		}else{
			alertInfo(alertStr);
		}
	}
	
	/**
	 * fqc edit 添加一个参数 isBeiShu 来判断当前是倍数还是期数 。
	 * 
	 * @param idFind
	 * @param iV
	 * @param isAdd
	 * @param mSeekBar
	 * @param isBeiShu
	 */
	public void setSeekWhenAddOrSub(int idFind, final int isAdd,final SeekBar mSeekBar, final boolean isBeiShu,View view) {
		ImageButton subtractBeishuBtn = (ImageButton) view.findViewById(idFind);
		subtractBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isBeiShu) {
					if (isAdd == 1) {
						mSeekBar.setProgress(++iProgressBeishu);
					} else {
						mSeekBar.setProgress(--iProgressBeishu);
					}
					iProgressBeishu = mSeekBar.getProgress();
				} else {
					if (isAdd == 1) {
						mSeekBar.setProgress(++iProgressQishu);
					} else {
						mSeekBar.setProgress(--iProgressQishu);
					}
					iProgressQishu = mSeekBar.getProgress();
				}
			}
		});
	}

	/**
	 * seekBar组件的监听器
	 */
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if (progress < 1)
			seekBar.setProgress(1);
		int iProgress = seekBar.getProgress();
		switch (seekBar.getId()) {
		case R.id.buy_zixuan_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
//			changeTextSumMoney();
			break;
		case R.id.buy_zixuan_seek_qishu:
			iProgressQishu = iProgress;
			mTextQishu.setText("" + iProgressQishu);
			break;
		default:
			break;
		}
		alertText.setText(getTouzhuAlert());

	}

	/**
	 * 计算注数和金额的方法
	 * 
	 */
	public void changeTextSumMoney() {     
		String text = textSumMoney(areaNums,iProgressBeishu);
		if(toast == null){
			toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
		}else{
			toast.setText(text);
			toast.show();
		}

	}
	/**
	 * 创建BallTable
	 * 
	 * @param LinearLayout
	 *            aParentView 上一级Layout
	 * @param int aLayoutId 当前BallTable的LayoutId
	 * @param int aFieldWidth BallTable区域的宽度（如屏幕宽度）
	 * @param int aBallNum 小球个数
	 * @param int aBallViewWidth 小球视图的宽度（图片宽度）
	 * @param int[] aResId 小球图片Id
	 * @param int aIdStart 小球Id起始数值
	 * @return BallTable
	 */
	public BallTable makeBallTable(TableLayout tableLayout, int aFieldWidth,int aBallNum, int[] aResId, int aIdStart, int aBallViewText,
			Context context, OnClickListener onclick,boolean isTen,List<String> missValues,boolean isMiss) {
		TableLayout tabble = tableLayout;
		BallTable iBallTable = new BallTable(aIdStart,context);
		int iBallNum = aBallNum;
		int viewNumPerLine = 8; // 时时彩设置每列小球的个数为10
		if(isTen){
			 viewNumPerLine = 10;
		}
		
		int iFieldWidth = aFieldWidth;
		int scrollBarWidth = 6;
		int iBallViewWidth = (iFieldWidth - scrollBarWidth) / viewNumPerLine - 2;
		int lineNum = iBallNum / viewNumPerLine;
		int lastLineViewNum = iBallNum % viewNumPerLine;
		int margin = (iFieldWidth - scrollBarWidth - (iBallViewWidth + 2)
				* viewNumPerLine) / 2;
		int iBallViewNo = 0;
		int[] rankInt = null;
		if(missValues!=null){
			rankInt = rankList(missValues);
		}
		for (int row = 0; row < lineNum; row++) {
			TableRow tableRow = new TableRow(context);
			TableRow tableRowText = new TableRow(context);

			for (int col = 0; col < viewNumPerLine; col++) {
				String iStrTemp = setTemp(aBallViewText, iBallViewNo,col);
				OneBallView tempBallView = new OneBallView(context);
				tempBallView.setId(aIdStart + iBallViewNo);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,aResId);
				tempBallView.setOnClickListener(onclick);
				iBallTable.addBallView(tempBallView);

				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin, 1, 1, 1);
				} else if (col == viewNumPerLine - 1) {
					lp.setMargins(1, 1, margin + scrollBarWidth + 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				if(isMiss){
					TextView textView = new TextView(context);
					if(missValues!=null){
						String missValue = missValues.get(iBallViewNo);
						textView.setText(missValue);
						if(rankInt[0]== Integer.parseInt(missValue)||rankInt[1]== Integer.parseInt(missValue)){
							textView.setTextColor(Color.RED);
						}
					}else{
						textView.setText("0");
					}
					textView.setGravity(Gravity.CENTER);
					tableRowText.addView(textView, lp);
					iBallTable.textList.add(textView);
				}
				iBallViewNo++;
			}
			tabble.addView(tableRow, new TableLayout.LayoutParams(PublicConst.FP, PublicConst.WC));
			tabble.addView(tableRowText, new TableLayout.LayoutParams(PublicConst.FP, PublicConst.WC));
		}
		if (lastLineViewNum > 0) {
			TableRow tableRow = new TableRow(context);
			TableRow tableRowText = new TableRow(context);
			for (int col = 0; col < lastLineViewNum; col++) {
				String iStrTemp = setTemp(aBallViewText, iBallViewNo,col);
				OneBallView tempBallView = new OneBallView(context);
				tempBallView.setId(aIdStart + iBallViewNo);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,aResId);
				tempBallView.setOnClickListener(onclick);
				iBallTable.addBallView(tempBallView);
				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin, 1, 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				if(isMiss){
					TextView textView = new TextView(context);
					if(missValues!=null){
						String missValue = missValues.get(iBallViewNo);
						textView.setText(missValue);
						if(rankInt[0] == Integer.parseInt(missValue)||rankInt[1] == Integer.parseInt(missValue)){
							textView.setTextColor(Color.RED);
						}
					}else{
						textView.setText("0");
					}
					textView.setGravity(Gravity.CENTER);
					tableRowText.addView(textView, lp);
					iBallTable.textList.add(textView);
				}
				iBallViewNo++;
			}
			// 新建的TableRow添加到TableLayout
			tabble.addView(tableRow, new TableLayout.LayoutParams(PublicConst.FP, PublicConst.WC));
			tabble.addView(tableRowText, new TableLayout.LayoutParams(PublicConst.FP, PublicConst.WC));
		}
		return iBallTable;
	}
	public String setTemp(int aBallViewText, int iBallViewNo,int col) {
		String iStrTemp = "";
		if (aBallViewText == 0) {
			iStrTemp = "" + (iBallViewNo);// 小球从0开始
		} else if (aBallViewText == 1) {
			iStrTemp = "" + (iBallViewNo + 1);// 小球从1开始
		} else if (aBallViewText == 3) {
			iStrTemp = "" + (iBallViewNo + 3);// 小球从3开始
		}else{
			iStrTemp = "" + (aBallViewText + iBallViewNo);
		}
		return iStrTemp;
	}
	public int[] rankList(List<String> myArray){
		int[] rankInt = new int[myArray.size()] ;
		for(int n=0;n<myArray.size();n++){
			rankInt[n] = Integer.parseInt(myArray.get(n));
		}
        // 取长度最长的词组 -- 冒泡法
        for (int j = 1; j < rankInt.length;j++)
        {
            for (int i = 0; i < rankInt.length - 1; i++)
            {
                 // 如果 myArray[i] > myArray[i+1] ，则 myArray[i] 上浮一位
                if (rankInt[i] < rankInt[i + 1])
                {
                    int temp = rankInt[i];
                    rankInt[i] = rankInt[i + 1];
                    rankInt[i + 1] = temp;
                }
            }
        }
		return rankInt;
	}
	/**
	 * 小球被点击事件
	 * 
	 * @param v
	 */
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int iBallId = v.getId();
		isBallTable(iBallId);
		showEditText();
		changeTextSumMoney();

	}
	/**
	 * 根据小球id判断是哪个选区
	 * 
	 * @param iBallId
	 */
	public void isBallTable(int iBallId){
		int nBallId = 0; 
		if(!isMove){
			for(int i=0;i<areaNums.length;i++){
				nBallId = iBallId;
				iBallId = iBallId - areaNums[i].areaNum;
				if(iBallId<0){
					  areaNums[i].table.changeBallState(areaNums[i].chosenBallSum, nBallId);
					  break;
				}

		     }
		}else{
			AreaNum areaNums[] = itemViewArray.get(0).areaNums;
			for(int i=0;i<areaNums.length;i++){
				nBallId = iBallId;
				AreaNum areaNum = areaNums[i];
				iBallId = iBallId - areaNum.areaNum;
				if(iBallId<0){
					areaNum.table.changeBallState(areaNum.chosenBallSum, nBallId);
					break;
				}
		     }	
		}
	}
	
	/**
	 * 显示在输入框的注码
	 * 
	 */
	public void showEditText(){
         SpannableStringBuilder builder = new SpannableStringBuilder(); 
		 String zhumas = "";
		 int num = 0;//高亮小球数
		 int length = 0;
			for(int j=0;j<areaNums.length;j++){
				BallTable ballTable = areaNums[j].table;
				int[] zhuMa = ballTable.getHighlightBallNOs();
				if(j!=0){
				    zhumas +=" + ";
				}
				for (int i = 0; i < ballTable.getHighlightBallNOs().length; i++) {
					if(highttype.equals("SSC")){
					zhumas += (zhuMa[i])+"";
					}else if(highttype.equals("DLC")){
					zhumas += PublicMethod.getZhuMa(zhuMa[i]);
					}
					if (i != ballTable.getHighlightBallNOs().length - 1){
						zhumas += ",";
					}
				}
				num+=zhuMa.length;
			}
             if(num==0){
            	 editZhuma.setText("");
            	 showEditTitle(this.type);
             }else{
				builder.append(zhumas);
				String zhuma[]=zhumas.split("\\+");
				  for(int i=0;i<zhuma.length;i++){
					  if(i!=0){
							length += zhuma[i].length()+1;
						  }else{
							length += zhuma[i].length();
						  }
				     if(i!=zhuma.length-1){	
						  builder.setSpan(new ForegroundColorSpan(Color.BLACK), length, length+1, Spanned.SPAN_COMPOSING);  
					  }
					  builder.setSpan(new ForegroundColorSpan(areaNums[i].textColor), length-zhuma[i].length(), length, Spanned.SPAN_COMPOSING);  

				  }
			    editZhuma.setText(builder, BufferType.EDITABLE);
			    showEditTitle(NULL);
             }
	}
	

	
	public void alertJX() {
		sensor.stopAction();
//		if(touZhuDialog == null){
//			initTouZhuDialog(getTouzhuAlert());
//		}else{
//		    initAlerDialog(getTouzhuAlert());
//		}
//		touZhuDialog.setOnDismissListener(new OnDismissListener() {
//			@Override
//			public void onDismiss(DialogInterface dialog) {
//				// TODO Auto-generated method stub
//				sensor.startAction();
//			}
//		});
		 touzhuNet();
		 initBet();
		 ApplicationAddview app=(ApplicationAddview)getApplicationContext();
		 app.setPojo(betAndGift);
	     app.setAddview(addView);
	     Intent intent =new Intent(ZixuanAndJiXuan.this,HghtOrderdeail.class); 
	     app.setHtextzhuma(addView.getsharezhuma());
	     app.setHightball(hightballs);
	     app.setHzhushu(addView.getAllZhu());
		 startActivity(intent);

	}
	public void alertZX() {
		 touzhuNet();
		 initBet();
		 ApplicationAddview app=(ApplicationAddview)getApplicationContext();
		 app.setPojo(betAndGift);
	     app.setAddview(addView);
	     app.setHtextzhuma(addView.getsharezhuma());
	     app.setHightball(hightballs);
	     app.setHzhushu(addView.getAllZhu());
	     Intent intent =new Intent(ZixuanAndJiXuan.this,HghtOrderdeail.class); 
		 startActivity(intent);
	}
	/**
	 * 初始化倍数和期数进度条
	 * @param view
	 */
    public void initImageView(View view){
		mSeekBarBeishu = (SeekBar) view.findViewById(R.id.buy_zixuan_seek_beishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mSeekBarQishu = (SeekBar) view.findViewById(R.id.buy_zixuan_seek_qishu);
		mSeekBarQishu.setOnSeekBarChangeListener(this);
		mSeekBarQishu.setProgress(iProgressQishu);
		mTextBeishu = (EditText) view.findViewById(R.id.buy_zixuan_text_beishu);
		mTextBeishu.setText("" + iProgressBeishu);
		mTextQishu = (EditText) view.findViewById(R.id.buy_zixuan_text_qishu);
		mTextQishu.setText("" + iProgressQishu);
		setProgressMax(2000);//设置倍数和期数最大值
		PublicMethod.setEditOnclick(mTextBeishu,mSeekBarBeishu,new Handler());
		PublicMethod.setEditOnclick(mTextQishu,mSeekBarQishu,new Handler());
		/*
		 * 点击加减图标，对seekbar进行设置：
		 * 
		 * @param int idFind, 图标的id View iV, 当前的view final int isAdd, 1表示加 -1表示减
		 * final SeekBar mSeekBar
		 * 
		 * @return void
		 */
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_subtract_beishu, -1,mSeekBarBeishu, true,view);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_add_beishu, 1, mSeekBarBeishu,true,view);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_subtract_qihao, -1,mSeekBarQishu, false,view);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_add_qishu, 1, mSeekBarQishu,false,view);
    }
	/**
	 * 第一次启动投注提示框
	 */
	public void initTouZhuDialog(String alert){
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v= inflater.inflate(R.layout.alert_dialog_touzhu_new, null);
		RadioGroup group =(RadioGroup) v.findViewById(R.id.RadioGroup01);
		group.setVisibility(RadioGroup.GONE);
		touZhuDialog = new Dialog(this,R.style.MyDialog);
		issueText =(TextView) v.findViewById(R.id.alert_dialog_touzhu_textview_qihao);
		shouyi=(CheckBox)v.findViewById(R.id.checkboxzhuihao);
		shouyi.setChecked(false);
		if(addView.getSize()<=1){
			shouyi.setVisibility(view.VISIBLE);
			}else{
			shouyi.setVisibility(view.GONE);	
		}
		alertText =(TextView) v.findViewById(R.id.alert_dialog_touzhu_text_one);
		textZhuma =(TextView) v.findViewById(R.id.alert_dialog_touzhu_text_zhuma);
		textTitleAlert = (TextView) v.findViewById(R.id.alert_dialog_touzhu_text_zhuma_title);
		issueText.setText(getBatchCode());
		alertText.setText(alert);
		textTitleAlert.setText("注码："+"共有"+addView.getSize()+"笔投注");
		addView.getCodeList().get(addView.getSize()-1).setTextCodeColor(textZhuma);
		codeInfo = (Button) v.findViewById(R.id.alert_dialog_touzhu_btn_look_code);
		isCodeText(codeInfo);
		codeInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addView.createCodeInfoDialog();
				addView.showDialog();
			}
		});
		initImageView(v);
		CheckBox checkPrize = (CheckBox) v.findViewById(R.id.alert_dialog_touzhu_check_prize);
		checkPrize.setChecked(true);
		
		//设置betAndGift.prizeend与checkPrize保持一致
		betAndGift.setPrizeend("1");
				
		checkPrize.setButtonDrawable(R.drawable.check_on_off);
		checkPrize.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					betAndGift.setPrizeend("1");
				}else{
					betAndGift.setPrizeend("0");
				}
			}
		});
		shouyi.setChecked(false);
		shouyi.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				isshouyi=isChecked;
			}
		});
		Button cancel = (Button) v.findViewById(R.id.alert_dialog_touzhu_button_cancel);
		Button ok = (Button) v.findViewById(R.id.alert_dialog_touzhu_button_ok);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				touZhuDialog.cancel();
				clearProgress();
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RWSharedPreferences pre = new RWSharedPreferences(ZixuanAndJiXuan.this, "addInfo");
				sessionId = pre.getStringValue("sessionid");
				phonenum = pre.getStringValue("phonenum");
				userno = pre.getStringValue("userno");
				if (sessionId.equals("")) {
					toLogin = true;
					Intent intentSession = new Intent(ZixuanAndJiXuan.this, UserLogin.class);
					startActivityForResult(intentSession, 0);
				} else {
					toLogin = false;				
					touZhu();
				}
			}
	
		});
		
		touZhuDialog.setCancelable(false);
		touZhuDialog.setContentView(v);
		touZhuDialog.show();
	}
	/**
	 * 用户投注
	 */
	private void touZhu() {
		initBet();
		touZhuDialog.cancel();
		if(isshouyi){
			toRevenueActivity();
		}else{
		touZhuNet();
		clearProgress();
		}
	}
	private void isCodeText(Button codeInfo) {
		if(addView.getSize()>1){
			codeInfo.setVisibility(Button.VISIBLE);
		}else{
			codeInfo.setVisibility(Button.GONE);
		}
	}
	/**
	 * 清空倍数和期数的进度条
	 */
	public void clearProgress(){
		iProgressBeishu = 1;
		iProgressQishu = 1;
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mSeekBarQishu.setProgress(iProgressQishu);
	}
	/**
	 * 再次启动提示框
	 */
	public void initAlerDialog(String alert){
		clearProgress();
		issueText.setText(getBatchCode());
		alertText.setText(alert);
		textTitleAlert.setText("注码："+"共有"+addView.getSize()+"笔投注");
		addView.getCodeList().get(addView.getSize()-1).setTextCodeColor(textZhuma);
		isCodeText(codeInfo);
//		isshouyi = false;
		shouyi.setChecked(false);
		if(addView.getSize()<=1){
			shouyi.setVisibility(view.VISIBLE);
			}else{
			shouyi.setVisibility(view.GONE);	
		}
		touZhuDialog.show();
	}
	/**
	* 当前注数
	*/
	public void setZhuShu(int zhushu){
		iZhuShu = zhushu;
	}
	/**
	 * 自选投注提示框中的信息
	 */
	public String getTouzhuAlert(){
	return "注数：" + addView.getAllZhu() + "注    " + "金额：" + iProgressQishu * addView.getAllAmt()*iProgressBeishu+ "元"; 
	}

	/**
	 * 返回当前期数
	 * @return
	 */
	public String getBatchCode(){
		String batchCode = "";
		if(highttype.equals("SSC")){
			batchCode = Ssc.batchCode;
		 }else if(highttype.equals("DLC")){
			batchCode = Dlc.batchCode;
		 }
		return batchCode += "期" ;
	}
	   /**
	    * 机选提醒框注码
	    * @return
	    */
	   public String getJxAlertZhuma(){
			String zhumaString = "";
			for (int i = 0; i < balls.size(); i++) {
				for(int j=0;j<balls.get(i).getVZhuma().size();j++){
					if(highttype.equals("SSC")){
						zhumaString += balls.get(i).getSpecialShowZhuma(j);
					 }else if(highttype.equals("DLC")){
						 zhumaString += balls.get(i).getTenSpecialShowZhuma(j);
					 }
					
					if(j!=balls.get(i).getVZhuma().size()-1){
						zhumaString += ",";
					}
				}
				if (i != balls.size() - 1) {
					zhumaString += "\n";
				}
			}
			codeStr = "注码：\n"+zhumaString;
			 return codeStr;	 
	   }

	public String getStrZhuMa(int balls[]) {
		String str = "";
		for (int i = 0; i < balls.length; i++) {
			str += (balls[i]);
			if (i != (balls.length - 1))
				str += ",";
		}
		return str;

	}
	/**
	 * 提示框1 用来提醒选球规则
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	public void alertInfo(String string) {   
		Builder dialog = new AlertDialog.Builder(this).setTitle("请选择号码")
				.setMessage(string).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {
							}
						});
		dialog.show();

	}
	/**
	 * 单笔投注大于1万注时的对话框
	 */
	public void dialogExcessive() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ZixuanAndJiXuan.this);
		builder.setTitle(ZixuanAndJiXuan.this.getString(R.string.toast_touzhu_title).toString());
		builder.setMessage("单笔投注不能大于1万注！");
		// 确定
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				});
		builder.show();
	}
	
	/**
	 * 重新方法
	 * 
	 */
	public void again(){
		if(areaNums!=null){
			if(!isMove){
				for(int i=0;i<areaNums.length;i++){
		            areaNums[i].table.clearAllHighlights();					
				}
			}else if(itemViewArray.get(newPosition).isZHmiss){
				 againZH(newPosition);
			}else{
				for(int i=0;i<itemViewArray.get(0).areaNums.length;i++){
		            itemViewArray.get(0).areaNums[i].table.clearAllHighlights();	
				}
			}
		showEditText();
		}
	}
//	public void again(int position){
//		if(itemViewArray!=null){
//			if(itemViewArray.get(position).isZHmiss){
//				 againZH(position);
//			}else{
//				for(int i=0;i<itemViewArray.get(0).areaNums.length;i++){
//		            itemViewArray.get(0).areaNums[i].table.clearAllHighlights();	
//				}
//				showEditText();
//			}
//		}else{
//			again();
//		}
//	}
	/**
	 * 清空指定选区小球
	 */
	public void again(int position){
		if(itemViewArray!=null){
			if(itemViewArray.get(0).areaNums!=null){
				  itemViewArray.get(0).areaNums[position].table.clearAllHighlights();	
			}
		}else{
			areaNums[position].table.clearAllHighlights();	
		}
	}
	private void againZH(int position) {
		List<MyButton> missBtnList = itemViewArray.get(position).missBtnList;
		if(missBtnList!=null){
			for(int i=0;i<missBtnList.size();i++){
				MyButton myBtn = missBtnList.get(i);
				if(myBtn.isOnClick()){
					myBtn.onAction();
				}
			}
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			break;
		}
	}
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(!toLogin){
			again();
			sensor.stopAction();
		}
	}
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		isTime = true;
		isStart = false;
		isViewEnd = false;
		prizeInfos = null;
		lotnoStr = "";
	}
	
	/**
	 * 重新初始化机选界面
	 */
	public void againView(){
	    sensor.startAction();
	    sensor.onVibrator();// 震动
	    toastjixuan.show();
	    jixuanZhu.setSelection(4);
		zhumaView.removeAllViews();
		balls = new Vector();
		for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
			Balls ball = ballOne.createBalls();
			balls.add(ball);
		}
		createTable(zhumaView);
	}
	/**
	 * 实现震动的类
	 * @author Administrator
	 *
	 */
	class SsqSensor extends SensorActivity {

		public SsqSensor(Context context) {
			getContext(context);
		}

		@Override
		public void action() {
				zhumaView.removeAllViews();
				balls = new Vector<Balls>();
				for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
					Balls ball = ballOne.createBalls();
					balls.add(ball);
				}
				createTable(zhumaView);
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
			progressdialog.setCancelable(true);
			return progressdialog;
		}
		}
		return null;
	}
	/**
	 * 初始化投注信息
	 */
	public void initBet(){
		betAndGift.setSessionid(sessionId);
		betAndGift.setPhonenum(phonenum);
		betAndGift.setUserno(userno);
		betAndGift.setBettype("bet");// 投注为bet,赠彩为gift 
		betAndGift.setLotmulti(""+iProgressBeishu);//lotmulti    倍数   投注的倍数
		betAndGift.setBatchnum(""+iProgressQishu);//batchnum    追号期数 默认为1（不追号）
		betAndGift.setAmount(""+addView.getAllAmt()*iProgressBeishu*100);
		betAndGift.setIsSellWays("1");
		betAndGift.setBet_code(addView.getTouzhuCode(iProgressBeishu,betAndGift.getAmt()*100));
		
	}
	
	
	public void toRevenueActivity(){
		  ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		   try {
		   ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
		            objStream.writeObject(betAndGift);
		  } catch (IOException e) {
		   return;  // should not happen, so donot do error handling
		  }
		  
		  Intent intent = new Intent(ZixuanAndJiXuan.this,High_Frequencyrevenue_Recovery.class);
		  intent.putExtra("info", byteStream.toByteArray());
		  intent.putExtra("zhuma", textZhuma.getText().toString());
		  intent.putExtra("lotno", lotnoStr);		
		  intent.putExtra("lingtball", hightballs);	
		  intent.putExtra("zhushu", addView.getAllZhu());	
		  startActivity(intent); 
		  clearArea();
		  clearProgress();
	}
	/**
	 * 投注联网
	 */
	public void touZhuNet(){
		lotno = PublicMethod.toLotno(betAndGift.getLotno());
		betAndGift.setBatchcode(PublicMethod.toIssue(betAndGift.getLotno()));
		showDialog(0); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";
			@Override
			public void run() {
				str = BetAndGiftInterface.getInstance().betOrGift(betAndGift);
				try {
					JSONObject obj = new JSONObject(str);		
					String msg = obj.getString("message");
					String error = obj.getString("error_code");
					handler.handleMsg(error,msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progressdialog.dismiss();
			}

		});
		t.start();
	}
	public void isMissNet(MissJson missJson,String sellWay,boolean isZHMiss){
		if(isZHMiss&&missView.get(radioId).isZMissNet){
			missView.get(radioId).isZMissNet = false;
			List<String> zMissList = missView.get(radioId).getZHMissList();
			if(!isJiXuan && (zMissList==null||zMissList.size()==0)){
				getMissNet(missJson,sellWay,isZHMiss);
			}
		}else if(missView.get(radioId).isMissNet){
			missView.get(radioId).isMissNet = false;
			List<List> missList = missView.get(radioId).getMissList();
			if(!isJiXuan && (missList==null||missList.size()==0)){
				getMissNet(missJson,sellWay,isZHMiss);
			}
		}
		Log.e("missView.get(radioId).isMissNet",""+missView.get(radioId).isMissNet);
	}
	/**
	 * 查询遗漏值联网
	 */
	public void getMissNet(final MissJson missJson,final String sellWay,final boolean isZHMiss){
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";
			int id = radioId;
			@Override
			public void run() {
				str = MissInterface.getInstance().betMiss(lotnoStr, sellWay);
				try {
					JSONObject obj = new JSONObject(str);		
					String msg = obj.getString("message");
					String error = obj.getString("error_code");
					missJson.jsonToList(sellWay,obj.getJSONObject("result"));
					missError(error,msg,missJson,isZHMiss,id);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});
		t.start();
	}
	private void missError(String error,final String msg,final MissJson missJson,final boolean isZHMiss,final int id) throws JSONException {
		if(error.equals(ERROR_WIN)){
			
			handler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(isZHMiss){
						missView.get(id).setZHMissList(missJson.zMissList);
						updateMissView(missJson);
					}else{
						missView.get(id).setMissList(missJson.missList);
						initMissText(missView.get(id).getAreaNum(),false,id);
					}
					
				}
			});
		}else{
			handler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	/**
	 * 刷新遗漏值界面
	 * @param missList
	 */
	private void updateMissView(MissJson missJson) {
		   if(itemViewArray !=null){
			   itemViewArray.get(1).updateView(missJson);
		   }
	}
   
	/**
	 * 格式化开奖号码
	 */
	public String formtPrizeInfo(String info){
		if(info.length()>5){
			return info;
		}else{
			String returnStr = "";
			int start = 0;
			while(start < info.length()){
				returnStr += info.substring(start,start+=1);
				if(start != info.length()){
					returnStr += ",";
				}
			}
			return returnStr;
		}
	
	}
	/**
	 * 设置倍数和期数最大值
	 * @param max
	 */
	public void setProgressMax(int max){
		mSeekBarBeishu.setMax(max);
		mSeekBarQishu.setMax(max);
	}
	@Override
	public void errorCode_0000() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String code= addView.getsharezhuma();
		clearArea();
		for(int i=0;i<areaNums.length;i++){
             areaNums[i].table.clearAllHighlights();
	     }
		editZhuma.setText("");
		PublicMethod.showDialog(ZixuanAndJiXuan.this,lotno+code);
	}
	public void clearArea(){
		if(addView != null){
			addView.clearInfo();
			addView.updateTextNum();
		}
	}
	@Override
	public void errorCode_000000() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}    
	/**
	 * 退出提示
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	public void alertExit(String string) {   
		Builder dialog = new AlertDialog.Builder(this).setTitle("温馨提示")
				.setMessage(string).setNeutralButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
                                    finish();
							}
						}).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
		dialog.show();

	}
	/**
	 * 重写放回建
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case 4:
			if(addView.getSize()!=0){
				alertExit(getString(R.string.buy_alert_exit));
			}else{
				finish();
			}
			break;
		}
		return false;
	}
}
