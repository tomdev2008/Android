package com.ruyicai.activity.buy.jc.zq.view;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.JcCommonMethod;
import com.ruyicai.activity.buy.jc.JcMainView;
import com.ruyicai.activity.common.CommonViewHolder;
import com.ruyicai.code.jc.zq.FootSpf;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.PublicMethod;

/**
 * 胜负类
 * 
 * @author Administrator
 * 
 */
public class SPfView extends JcMainView {
	private final int MAX_TEAM = 8;
	FootSpf footSpfCode;
	public static boolean isRQSPF = false;

	public SPfView(Context context, BetAndGiftPojo betAndGift, Handler handler,
			LinearLayout layout, String type, boolean isdanguan,
			List<String> checkTeam) {
		super(context, betAndGift, handler, layout, type, isdanguan, checkTeam);
		footSpfCode = new FootSpf(context);
	}

	@Override
	public int getTeamNum() {
		return MAX_TEAM;
	}

	@Override
	public String getLotno() {
		return Constants.LOTNO_JCZQ;
	}

	@Override
	public BaseExpandableListAdapter getAdapter() {
		return adapter;
	}

	@Override
	public String getTitle() {
		if (isDanguan) {
			return context.getString(R.string.jczq_sf_danguan_title).toString();
		} else {
			return context.getString(R.string.jczq_sf_guoguan_title).toString();
		}
	}

	@Override
	public String getTypeTitle() {
		return context.getString(R.string.jczq_dialog_sf_guoguan_title)
				.toString();
	}

	public void setDifferValue(JSONObject jsonItem, Info itemInfo)
			throws JSONException {
		itemInfo.setLevel(jsonItem.getString("v1"));
	}

	/**
	 * 
	 * 获取注码
	 * 
	 */
	public String getCode(String key, List<Info> listInfo) {
		return footSpfCode.getCode(key, listInfo);
	}

	/**
	 * 获取倍率
	 */
	@Override
	public List<double[]> getOdds(List<Info> listInfo) {
		return footSpfCode.getOddsList(listInfo);
	}

	/**
	 * 
	 * 获取注码
	 * 
	 */
	public String getAlertCode(List<Info> listInfo) {
		String codeStr = "";
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum > 0) {
				codeStr += PublicMethod.stringToHtml(info.getWeeks() + " " + info.getTeamId(), 
						Constants.JC_TOUZHU_TITLE_TEXT_COLOR) +  " ";
				codeStr += (info.getHome() + " vs " + info.getAway() + "<br>胜平负：");
				if (info.isWin()) {
					codeStr += PublicMethod.stringToHtml("胜", Constants.JC_TOUZHU_TEXT_COLOR) + "  ";
				}
				if (info.isLevel()) {
					codeStr += PublicMethod.stringToHtml("平", Constants.JC_TOUZHU_TEXT_COLOR) + "  ";
				}
				if (info.isFail()) {
					codeStr += PublicMethod.stringToHtml("负", Constants.JC_TOUZHU_TEXT_COLOR) + "  ";
				}
				if (info.isDan()) {
					codeStr += PublicMethod.stringToHtml("(胆)", Constants.JC_TOUZHU_TEXT_COLOR) + "  ";
				}

				codeStr += "<br>";
			}

		}
		return codeStr;
	}

	/**
	 * 初始化列表
	 */
	public void initListView(ExpandableListView listview, Context context,
			List<List> listInfo) {
		adapter = new JcInfoExpandableListAdapter(context, listInfo);
		listview.setAdapter(adapter);
	}
	
	public class JcInfoExpandableListAdapter extends BaseExpandableListAdapter {
		private LayoutInflater mInflater; // 扩充主列表布局
		private List<List> mList;
		public JcInfoExpandableListAdapter(Context context, List<List> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;
		}

		@Override
		public int getGroupCount() {
			if (mList == null) {
				return 0;
			}
			return mList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			ArrayList<Info> list = (ArrayList<Info>) mList.get(groupPosition);
			if (list == null) {
				return 0;
			}
			return list.size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return mList.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			ArrayList<Info> list = (ArrayList<Info>) mList.get(groupPosition);
			if (list == null) {
				return null;
			}
			return list.get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			return getConvertView(groupPosition, isExpanded, convertView, mList, mInflater);
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			CommonViewHolder.ChildViewHolder holder = null;
			final ArrayList<Info> list = (ArrayList<Info>) mList.get(groupPosition);
			final Info info = list.get(childPosition);
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.buy_jc_main_listview_item_other, null);
				holder = JcCommonMethod.initChildViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (CommonViewHolder.ChildViewHolder) convertView.getTag();
			}
			ViewOnClickListener listener = new ViewOnClickListener(holder, info);
			holder.guestLayout.setOnClickListener(listener);
			holder.homeLayout.setOnClickListener(listener);
			holder.analysis.setOnClickListener(listener);
			holder.vsLayout.setOnClickListener(listener);
			
			JcCommonMethod.setDividerShowState(childPosition, holder);
			JcCommonMethod.setTeamTime(info, holder);
			JcCommonMethod.setJcZqTeamName(info, holder);
			setDanShowState(info, holder);
			setOddsShowState(info, holder);
			setViewStyle(holder.homeLayout, holder.homeTeam, holder.homeOdds, info.isWin());
			setViewStyle(holder.guestLayout, holder.guestTeam, holder.guestOdds, info.isFail());
			setViewStyle(holder.vsLayout, holder.textVS, holder.textOdds, info.isLevel());
			if (isDanguan || isHunHe()) {
				holder.btnDan.setVisibility(Button.GONE);
			} else {
				holder.btnDan.setVisibility(Button.VISIBLE);
				holder.btnDan.setOnClickListener(listener);
			}
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
		
		private void setOddsShowState(Info info, CommonViewHolder.ChildViewHolder holder) {
			if (isRQSPF) {
				if (!"".equals(info.getLetPoint())) {
					holder.textVS.setText(info.getLetPoint());
				} else {
					holder.textVS.setText("0");
				}
				holder.homeOdds.setText(info.getLetV3Win());
				holder.textOdds.setText(info.getLetV1Level());
				holder.guestOdds.setText(info.getLetV0Fail());
			} else {
				holder.textVS.setText("VS");
				holder.homeOdds.setText(info.getWin());
				holder.textOdds.setText(info.getLevel());
				holder.guestOdds.setText(info.getFail());
			}
		}
	}
	
	public class ViewOnClickListener implements View.OnClickListener {
		private CommonViewHolder.ChildViewHolder holder;
		private Info info;
		public ViewOnClickListener(CommonViewHolder.ChildViewHolder holder, Info info) {
			this.holder = holder;
			this.info = info;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.home_layout:
				setHomeLayoutShowState();
				break;

			case R.id.guest_layout:
				setGuestLayoutShowState();
				break;
				
			case R.id.vs_layout:
				setVSShowState();
				break;
				
			case R.id.game_dan:
				setGameDanShowState(info, holder);
				break;
				
			case R.id.game_analysis:
				trunExplain(getEvent(Constants.JCFOOT, info),
						info.getHome(), info.getAway());
				break;
			}
		}
		
		private void setGuestLayoutShowState() {
			if (info.onclikNum > 0 || isCheckTeam()) {
				info.setFail(!info.isFail());
				if (info.isFail()) {
					info.onclikNum++;
				} else {
					info.onclikNum--;
				}
				setViewStyle(holder.guestLayout, holder.guestTeam, holder.guestOdds, info.isFail());
				isNoDan(info, holder.btnDan);
				setTeamNum();
			}
		}
		
		private void setHomeLayoutShowState() {
			if (info.onclikNum > 0 || isCheckTeam()) {
				info.setWin(!info.isWin());
				if (info.isWin()) {
					info.onclikNum++;
				} else {
					info.onclikNum--;
				}
				setViewStyle(holder.homeLayout, holder.homeTeam, holder.homeOdds, info.isWin());
				isNoDan(info, holder.btnDan);
				setTeamNum();
			}
		}
		
		private void setVSShowState() {
			if (info.onclikNum > 0 || isCheckTeam()) {
				info.setLevel(!info.isLevel());
				if (info.isLevel()) {
					info.onclikNum++;
				} else {
					info.onclikNum--;
				}
				setViewStyle(holder.vsLayout, holder.textVS, holder.textOdds, info.isLevel());
				isNoDan(info, holder.btnDan);
				setTeamNum();
			}
		}
	}

	@Override
	public String getPlayType() {
		if (isDanguan) {
			return "J00001_0";
		} else {
			return "J00001_1";
		}
	}
	
	/**add by yejc 20130823 start*/
	private void setViewStyle(LinearLayout teamLayout, TextView team, TextView odds, boolean isChecked) {
		if (isChecked) {
			teamLayout.setBackgroundResource(R.drawable.team_name_bj_yellow);
			team.setBackgroundResource(R.drawable.team_name_bj_top_yellow);
			team.setTextColor(white);
			odds.setTextColor(white);
		} else {
			teamLayout.setBackgroundResource(android.R.color.transparent);
			team.setBackgroundResource(android.R.color.transparent);
			team.setTextColor(black);
			odds.setTextColor(oddsColor);
		}
	}
	/**add by yejc 20130823 end*/

}
