/**
 * 
 */
package com.ruyicai.activity.buy.ssq;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;

public class Ssq extends BuyActivityGroup {

	private String[] titles = { "自选", "胆拖" };
	private String[] topTitles = { "双色球", "双色球" };
	private Class[] allId = { SsqZiZhiXuan.class, SsqZiDanTuo.class };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*Add by fansm 20130416 start*/
		if (Constants.isDebug) {
			PublicMethod.outLog(this.getClass().getSimpleName(), "onCreate()");
		    PublicMethod.getActivityFromStack(this);
		}
		/*Add by fansm 20130416 end*/
		setLotno(Constants.LOTNO_SSQ);

		init(titles, topTitles, allId);
		setIssue(Constants.LOTNO_SSQ);
		setlastbatchcode(Constants.LOTNO_SSQ);
	}

	public boolean getIsLuck() {
		return true;
	}

	/**
	 * 添加模拟选号：覆盖父类方法，是模拟选号的选项布局为可见
	 * 
	 * @param view
	 *            popUpWindow布局视图
	 */
	public void addSimulateSelectNumber(View view) {
		LinearLayout simulaterLayout = (LinearLayout) view
				.findViewById(R.id.buy_group_one_layout5);
		simulaterLayout.setVisibility(View.VISIBLE);

		simulaterLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Ssq.this,
						SimulateSelectNumberActivity.class);
				startActivity(intent);
				popupwindow.dismiss();
			}
		});
	}
}
