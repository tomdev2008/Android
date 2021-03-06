package com.ruyicai.activity.buy.nmk3;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.palmdream.RuyicaiAndroidxuancai.R;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.ssc.Ssc;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.PublicMethod;

/**
 * Nmk3HeZhiActivity：内蒙古快三和值
 * 
 * @author PengCX
 * 
 */
public class Nmk3HeZhiActivity extends ZixuanAndJiXuan {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		childtype = new String[] { "直选" };
		setContentView(R.layout.sscbuyview);
		highttype = "NMK3-HE";
		init();
		childtypes.setVisibility(View.GONE);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		onCheckAction(checkedId);
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensor.stopAction();
		baseSensor.stopAction();
		editZhuma.setText(R.string.please_choose_number);
	}

	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// 显示投注注数和投注金额的提示
		int zhuShu = getZhuShu();

		if (zhuShu == 0) {
			return "请选择投注号码";
		} else {
			return "共" + zhuShu + "注，共" + zhuShu * 2 + "元";
		}
	}

	@Override
	public String isTouzhu() {
		// 点击号码篮后触发判断投注的合法性
		if (getZhuShu() == 0) {
			return "请至少选择一注";
		} else if (getZhuShu() > 10000) {
			return "false";
		} else {
			return "true";
		}
	}

	@Override
	public int getZhuShu() {
		// 返回当前投注的注数
		return areaNums[0].table.getHighlightBallNums();
	}

	@Override
	public String getZhuma() {
		// 拼接投注的注码格式，用户投注与后台使用
		String zhuMa = "";

		// 获取注码的各个部分
		String playMethodPart = getPlayMethodPart();
		String mutiplePart = getMutiplePart();
		String numberNumsPart = getNumberNumsPart();
		String numbersPart = getNumbersPart();
		String endFlagPart = "^";

		// 拼接注码
		zhuMa = playMethodPart + mutiplePart + numberNumsPart + numbersPart
				+ endFlagPart;

		return zhuMa;
	}

	/**
	 * 获取号码部分
	 * 
	 * @return 号码部分
	 */
	private String getNumbersPart() {
		// 获取高亮小球号码数组
		int[] numbers = areaNums[0].table.getHighlightBallNOs();
		StringBuffer numbersPart = new StringBuffer();

		// 循环号码数组，并拼接
		for (int num_i = 0; num_i < numbers.length; num_i++) {
			numbersPart.append(PublicMethod.getZhuMa(numbers[num_i]));
		}

		return numbersPart.toString();
	}

	/**
	 * 获取号码个数部分
	 * 
	 * @return 号码个数部分
	 */
	private String getNumberNumsPart() {

		return PublicMethod
				.getZhuMa(areaNums[0].table.getHighlightBallNOs().length);

	}

	/**
	 * 获取倍数字段
	 * 
	 * @return 倍数部分
	 */
	private String getMutiplePart() {
		// 获取注码的时候默认使用1倍，在投注详情界面的倍数才对后台有效
		return "0001";
	}

	/**
	 * 获取玩法部分
	 * 
	 * @return 玩法部分
	 */
	private String getPlayMethodPart() {
		return "10";
	}

	@Override
	public String getZhuma(Balls ball) {
		return null;
	}

	@Override
	public void touzhuNet() {
		// 设置投注信息彩种，注码，金额和期号等投注信息
		betAndGift.setLotno(Constants.LOTNO_NMK3);
		betAndGift.setBet_code(getZhuma());
		int zhuShu = getZhuShu();
		betAndGift.setAmount("" + zhuShu * 200);
		betAndGift.setBatchcode(Nmk3Activity.batchCode);
	}

	@Override
	public void onCheckAction(int checkedId) {
		switch (checkedId) {
		case 0:
			initArea();
			createView(areaNums, sscCode, ZixuanAndJiXuan.NMK3_HEZHI, true,
					checkedId, false);
			break;
		}
	}

	/**
	 * 初始化选号控件
	 */
	public AreaNum[] initArea() {
		areaNums = new AreaNum[1];
		areaNums[0] = new AreaNum(14, 10, 1, 14, BallResId, 0, 4, Color.RED,
				"", false, true);
		return areaNums;
	}

	/*
	 * 设置投注信息类的彩种编号和投注类型
	 */
	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_NMK3);
		codeInfo.setTouZhuType("hezhi");
	}

}
