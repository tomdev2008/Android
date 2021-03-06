/**
 * 
 */
package com.ruyicai.activity.notice;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.constant.Constants;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.PublicMethod;

/**
 * @author Administrator
 * 
 */
public class NoticeBallView extends View {
	private Paint p = null;
	private Context context;
	private int row;// 列数
	private int line;// 行数
	private int startNum;// 启始值
	List<RowInfo> list;
	List<RowInfo> blueList;
	private int with;
	private int height;
	private int WITH = 25;
	private int FIRST_WITH = 350;

	private int SECOND_WITH = 210;
	private float release = (float) 1.4;
	private String iGameType;
	private boolean isRed;

	Bitmap bitWhite;
	Bitmap bitGrey;

	Bitmap bitNoticeRed;
	Bitmap bitNoticeTopRed;
	Bitmap bitNoticeTopBlue;

	Bitmap bitNoticeBlue;
	Bitmap bitNoticeYellow;

	Bitmap bitLeftRed;
	Bitmap bitFirstLeftRed;
	Bitmap bitLeftWhite;
	Bitmap bitRedBall;
	Bitmap bitBlueBall;
	boolean isTen = true;

	/**
	 * @param context
	 */
	public NoticeBallView(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public NoticeBallView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;

	}

	public NoticeBallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

	}

	/**
	 * 
	 * @param line行数
	 * @param row列数
	 * @param startStr启始值
	 * @param list
	 *            彩种信息的数组
	 */
	public void initNoticeBall(int line, int row, int startNum,
			List<JSONObject> list, boolean isRed, String lotno, double release) {
		iGameType = lotno;
		this.isRed = isRed;
		this.line = line;
		this.row = row;
		this.startNum = startNum;
		this.list = initRowInfo(list, isRed);
		this.blueList = initRowInfo(list, false);
		this.release = (float) release;
		WITH = (int) (WITH * release);
		if(lotno.equals("gd-10")){
			if (isRed) {
				FIRST_WITH = 1;
			} else {
				FIRST_WITH = (int) (FIRST_WITH * release);
				setFistWith();
			}
		}else{
			if (isRed) {
				FIRST_WITH = (int) (FIRST_WITH * release);
				setFistWith();
			} else {
				FIRST_WITH = 1;
			}
		}

		if (lotno.equals("fc3d") || lotno.equals("pl3")) {
			with = 3 * row * WITH + FIRST_WITH;
		} else if (lotno.equals("pl5") || lotno.equals("ssc")) {
			with = 5 * row * WITH + FIRST_WITH;
		} else if (lotno.equals("qxc")) {
			with = 7 * row * WITH + FIRST_WITH;
		} else {
			with = row * WITH + FIRST_WITH;
		}
		height = (line + 1) * WITH;
		setPaint();
		initImage();
	}

	public void setFistWith() {
	    if (Constants.SCREEN_WIDTH == 240) {
			FIRST_WITH = PublicMethod.getPxInt(200, context);
			SECOND_WITH = PublicMethod.getPxInt(100, context);
		} else if (Constants.SCREEN_WIDTH == 320) {
			FIRST_WITH = PublicMethod.getPxInt(200, context);
			SECOND_WITH = PublicMethod.getPxInt(100, context);
		} else if (Constants.SCREEN_WIDTH == 480) {
			return;
		} else if (Constants.SCREEN_WIDTH == 540) {
			FIRST_WITH = PublicMethod.getPxInt(280, context);
		} else {
			return;
		}

	}

	public void initImage() {
		bitWhite = getBitmapFromRes(R.drawable.notice_center_wite, WITH, WITH);
		bitGrey = getBitmapFromRes(R.drawable.notice_center_grey, WITH, WITH);

		bitNoticeRed = getBitmapFromRes(R.drawable.notice_top_red, WITH, WITH);

		bitNoticeTopRed = getBitmapFromRes(R.drawable.notice_top_red,FIRST_WITH, WITH);
		bitNoticeTopBlue = getBitmapFromRes(R.drawable.notice_top_blue,FIRST_WITH, WITH);

		bitNoticeBlue = getBitmapFromRes(R.drawable.notice_top_blue, WITH, WITH);
		bitNoticeYellow = getBitmapFromRes(R.drawable.notice_top_yellow, WITH,
				WITH);

		bitLeftRed = getBitmapFromRes(R.drawable.notice_left_red, WITH, WITH);
		bitFirstLeftRed = getBitmapFromRes(R.drawable.notice_left_red,
				FIRST_WITH, WITH);
		bitLeftWhite = getBitmapFromRes(R.drawable.notice_left_wite, WITH, WITH);
		bitRedBall = getBitmapFromRes(R.drawable.notice_ball_red, WITH, WITH);
		bitBlueBall = getBitmapFromRes(R.drawable.notice_ball_blue, WITH, WITH);
	}

	public List<RowInfo> initRowInfo(List<JSONObject> _list, boolean isRed) {
		List<RowInfo> list = new ArrayList<RowInfo>();
		for (int i = 0; i < _list.size(); i++) {
			RowInfo info = new RowInfo();
			try {
				info.setIssue(_list.get(i).getString("batchCode"));
				info.setBallNum(parseStr(_list.get(i).getString("winCode"),isRed));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			list.add(info);
		}
		return list;

	}

	/*
	 * 设置画笔
	 * 
	 * @return void
	 */
	private void setPaint() {
		if (p == null) {
			p = new Paint();
			// p.setTypeface(font);
			// p.setTypeface(Typeface.create(Typeface.SANS_SERIF,
			// Typeface.NORMAL));
			float textSize;
			if (release > 1) {
				textSize = 22;
			} else if (release > 0.8) {
				textSize = 16
						* release
						* (Float.valueOf(Constants.SCREEN_WIDTH) / Float
								.valueOf(480));
			} else if (release > 0.6) {
				textSize = 20
						* release
						* (Float.valueOf(Constants.SCREEN_WIDTH) / Float
								.valueOf(480));
			} else {
				textSize = 27
						* release
						* (Float.valueOf(Constants.SCREEN_WIDTH) / Float
								.valueOf(480));
			}

			p.setTextSize(textSize);

			// p.setUnderlineText(true);
		}

	}

	public int[] parseStr(String iNumbers, boolean isRed) {
		if (iGameType.equalsIgnoreCase("gd11-5")) {
			// TODO
			int[] allNums = new int[5];
			for (int i = 0; i < 5; i++) {
				allNums[i] = Integer.valueOf(iNumbers.substring(i * 2,
						i * 2 + 2));
			}
			return allNums;
		}
		if (iGameType.equalsIgnoreCase("gd-10")) {
			// TODO
			int[] allNums = new int[8];
			int[] redNums = new int[2];
			int index = 0;
			for (int i = 0; i < 8; i++) {
				allNums[i] = Integer.valueOf(iNumbers.substring(i * 2,i * 2 + 2));
				if(allNums[i]==19||allNums[i]==20){
					redNums[index] = allNums[i];
					index++;
				}
			}
			if(isRed){
				return redNums;
			}else{
				return allNums;
			}
		}
		if (iGameType.equalsIgnoreCase("ssq")) {
			// zlm 7.28 代码修改：添加号码排序
			int i2;
			String iShowNumber;
			int[] ssqInt01 = new int[6];
			int[] ssqInt02 = new int[1];
			for (i2 = 0; i2 < 6; i2++) {
				iShowNumber = iNumbers.substring(i2 * 2, i2 * 2 + 2);
				ssqInt01[i2] = Integer.valueOf(iShowNumber);
			}

			ssqInt01 = PublicMethod.sort(ssqInt01);
			if (isRed) {
				return ssqInt01;
			} else {
				iShowNumber = iNumbers.substring(12, 14);
				ssqInt02[0] = Integer.valueOf(iShowNumber);
				return ssqInt02;
			}

		} else if (iGameType.equalsIgnoreCase("fc3d")) {
            isTen = false;
			// zlm 7.30 代码修改：修改福彩3D号码
			int[] allNums = new int[3];
			for (int i = 0; i < 3; i++) {
				allNums[i] = Integer.valueOf(iNumbers.substring(i * 2,
						i * 2 + 2));
			}
			return allNums;
		} else if (iGameType.equalsIgnoreCase("qlc")) {
			// zlm 7.28 代码修改：添加号码排序
			int i1, i2, i3;
			String iShowNumber;

			int[] ssqInt01 = new int[7];
			int[] ssqInt02 = new int[1];

			for (i2 = 0; i2 < 7; i2++) {
				iShowNumber = iNumbers.substring(i2 * 2, i2 * 2 + 2);
				ssqInt01[i2] = Integer.valueOf(iShowNumber);
			}

			ssqInt01 = PublicMethod.sort(ssqInt01);
			// zlm 8.3 代码修改 ：添加七乐彩蓝球
			if (isRed) {
				return ssqInt01;
			} else {
				iShowNumber = iNumbers.substring(14, 16);
				ssqInt02[0] = Integer.valueOf(iShowNumber);
				return ssqInt02;
			}

		} else if (iGameType.equalsIgnoreCase("pl3")) {
			isTen = false;
			int[] allNums = new int[3];
			for (int i = 0; i < 3; i++) {
				allNums[i] = Integer.valueOf(iNumbers.substring(i, i + 1));
			}
			return allNums;
		} else if (iGameType.equalsIgnoreCase("pl5")) {
			isTen = false;
			int[] allNums = new int[5];
			for (int i = 0; i < 5; i++) {
				allNums[i] = Integer.valueOf(iNumbers.substring(i, i + 1));
			}
			return allNums;
		} else if (iGameType.equalsIgnoreCase("qxc")) {
			isTen = false;
			int[] allNums = new int[7];
			for (int i = 0; i < 7; i++) {
				allNums[i] = Integer.valueOf(iNumbers.substring(i, i + 1));
			}
			return allNums;
		} else if (iGameType.equalsIgnoreCase("cjdlt")) {

			int i1, i2;
			String iShowNumber;
			int[] cjdltInt01 = new int[5];
			int[] cjdltInt02 = new int[2];

			for (i2 = 0; i2 < 5; i2++) {
				iShowNumber = iNumbers.substring(i2 * 3, i2 * 3 + 2);
				cjdltInt01[i2] = Integer.valueOf(iShowNumber);
			}

			cjdltInt01 = PublicMethod.sort(cjdltInt01);
			for (i2 = 0; i2 < 2; i2++) {
				iShowNumber = iNumbers.substring(i2 * 3 + 15, i2 * 3 + 17);
				cjdltInt02[i2] = Integer.valueOf(iShowNumber);
			}
			cjdltInt02 = PublicMethod.sort(cjdltInt02);
			if (isRed) {
				return cjdltInt01;
			} else {
				return cjdltInt02;
			}

		} else if (iGameType.equalsIgnoreCase("ssc")) {
			isTen = false;
			int[] allNums = new int[5];
			for (int i = 0; i < 5; i++) {
				allNums[i] = Integer.valueOf(iNumbers.substring(i, i + 1));
			}
			return allNums;
		} else if (iGameType.equalsIgnoreCase("11-5")
				|| iGameType.equalsIgnoreCase("11-ydj")) {

			int[] allNums = new int[5];
			for (int i = 0; i < 5; i++) {
				allNums[i] = Integer.valueOf(iNumbers.substring(i * 2,
						i * 2 + 2));
			}
			return allNums;
		} else if (iGameType.equalsIgnoreCase("22-5")) {

			int[] allNums = new int[5];
			for (int i = 0; i < 5; i++) {
				allNums[i] = Integer.valueOf(iNumbers.substring(i * 2,
						i * 2 + 2));
			}
			return allNums;
		} else if (iGameType.equalsIgnoreCase("sfc")) {

			int[] allNums = new int[14];
			for (int i = 0; i < 14; i++) {
				allNums[i] = Integer.valueOf(iNumbers.substring(i, i + 1));
			}
			return allNums;
		} else if (iGameType.equalsIgnoreCase("rxj")) {
			int[] allNums = new int[14];
			for (int i = 0; i < 14; i++) {
				allNums[i] = Integer.valueOf(iNumbers.substring(i, i + 1));
			}
			return allNums;
		} else if (iGameType.equalsIgnoreCase("lcb")) {
			int[] allNums = new int[12];
			for (int i = 0; i < 12; i++) {
				allNums[i] = Integer.valueOf(iNumbers.substring(i, i + 1));
			}
			return allNums;
		} else if (iGameType.equalsIgnoreCase("jqc")) {
			int[] allNums = new int[8];
			for (int i = 0; i < 8; i++) {
				allNums[i] = Integer.valueOf(iNumbers.substring(i, i + 1));
			}
			return allNums;
		}
		return null;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(with, height);
	}

	/**
	 * 画布
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/*
		 * Resources r = this.getContext().getResources(); InputStream is =
		 * r.openRawResource(iResId[iShowId]); BitmapDrawable bmpDraw = new
		 * BitmapDrawable(is); Bitmap bmp = bmpDraw.getBitmap();
		 * canvas.drawBitmap(bmp, 0, 0, p);
		 */
		canvas.drawColor(Color.TRANSPARENT);
		p.setColor(Color.parseColor("#444444"));
		p.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
		p.setAntiAlias(true);
		onDrawTop(canvas);
		if (list != null && list.get(0).getBallNum() != null) {
			if(iGameType.equals("gd-10")){
				if (isRed) {
					p.setColor(Color.GRAY);
					canvas.drawLine(FIRST_WITH, 0, FIRST_WITH, getHeight(), p);
				} else {
					onDrawLeft(canvas);
				}
			}else{
				if (isRed) {
					onDrawLeft(canvas);
				} else {
					p.setColor(Color.GRAY);
					canvas.drawLine(FIRST_WITH, 0, FIRST_WITH, getHeight(), p);
				}
			}
			if (iGameType.equals("fc3d") || iGameType.equals("pl3")
					|| iGameType.equals("pl5") || iGameType.equals("qxc")
					|| iGameType.equals("ssc")) {
				int ballNum = 3;
				if (iGameType.equals("pl5") || iGameType.equals("ssc")) {
					ballNum = 5;
				} else if (iGameType.equals("qxc")) {
					ballNum = 7;
				}
				onDrawCenterPL3(canvas, ballNum);
			} else {
				onDrawCenter(canvas);
			}
		}
	}

	/**
	 * 顶部
	 */
	public void onDrawTop(Canvas canvas) {
		// 绘制红色的背景图片 图片是一竖条根据宽度来拉伸
		if(isRed){
			canvas.drawBitmap(bitNoticeTopRed, 0, 0, null);
		}else{
			canvas.drawBitmap(bitNoticeTopBlue, 0, 0, null);
		}
		p.setColor(Color.WHITE);
		// 设置高 WITH是25 release 是1.4？
		int height = (int) (WITH - (6 * release));
		// 设置宽 WITH是25 release 是1.4？
		int with = (int) (WITH / 2 - (10 * release));
		if (isRed||(iGameType.equals("gd-10")&&!isRed)) {
			canvas.drawText("期号", with, height, p);
			canvas.drawText("开奖号码", FIRST_WITH - SECOND_WITH, height, p);
		}
		if (iGameType.equals("fc3d") || iGameType.equals("pl3")
				|| iGameType.equals("pl5") || iGameType.equals("qxc")
				|| iGameType.equals("ssc")) {
			int ballNum = 3;
			if (iGameType.equals("pl5") || iGameType.equals("ssc")) {
				ballNum = 5;
			} else if (iGameType.equals("qxc")) {
				ballNum = 7;
			}
			for (int j = 0; j < ballNum; j++) {
				for (int i = 0; i < row; i++) {
					if (j % 2 == 0) {
						canvas.drawBitmap(bitNoticeRed, FIRST_WITH + i * WITH
								+ (row * WITH * j), 0, null);
					} else if (j % 2 == 1) {
						canvas.drawBitmap(bitNoticeBlue, FIRST_WITH + i * WITH
								+ (row * WITH * j), 0, null);
					}
					int num = i + startNum;
					canvas.drawText(""+num, (FIRST_WITH + i * WITH + with) + (row * WITH * j)+4, height, p);
				}
			}
		} else {
			for (int i = 0; i < row; i++) {
				if (isRed) {
					canvas.drawBitmap(bitNoticeRed, FIRST_WITH + i * WITH, 0,
							null);
				} else {
					canvas.drawBitmap(bitNoticeBlue, FIRST_WITH + i * WITH, 0,
							null);
				}
				int num = i + startNum;
				canvas.drawText(PublicMethod.isTen(num), FIRST_WITH + i * WITH
						+ with, height, p);
			}
		}
	}

	/**
	 * 绘制左边
	 */
	public void onDrawLeft(Canvas canvas) {
		int toLeft = PublicMethod.getPxInt(20, context);
		for (int i = 0; i < line; i++) {
			p.setColor(Color.GRAY);
			int height = (int) (WITH - (6 * release));
			int with = 0;
			if (Constants.SCREEN_WIDTH == 240||Constants.SCREEN_WIDTH == 320) {
				with = WITH / 2 - 5;
			} else if (Constants.SCREEN_WIDTH == 540) {
				toLeft = PublicMethod.getPxInt(28, context);
			} else {
				with = WITH / 2 - 10;
			}
			if (i % 2 == 0) {
				canvas.drawBitmap(bitLeftWhite, 0, WITH + i * WITH, null);
			} else {
				canvas.drawBitmap(bitFirstLeftRed, 0, WITH + i * WITH, null);
			}
			canvas.drawText(list.get(line - 1 - i).getIssue(), with, WITH + i
					* WITH + height, p);
			if(isRed){
				p.setColor(Color.RED);
			}else{
				p.setColor(Color.BLUE);
			}
			String isNumber = "";
			if (list.get(line - 1 - i).getBallNum() != null) {
				for (int j = 0; j < list.get(line - 1 - i).getBallNum().length; j++) {
					int sum = list.get(i).getBallNum().length + 1;
					int textInt = list.get(line - 1 - i).getBallNum()[j];
					if(iGameType.equals("gd-10")&&(textInt==19||textInt==20)){
						p.setColor(Color.RED);
					}else{
						p.setColor(Color.BLUE);
					}
					if(isTen){
						isNumber = PublicMethod.isTen(list.get(line - 1 - i).getBallNum()[j]);
					}else{
						isNumber = Integer.toString(list.get(line - 1 - i).getBallNum()[j]);
					}
					if (j != list.get(line - 1 - i).getBallNum().length - 1) {
						isNumber += ",";
					} 
					canvas.drawText(isNumber, FIRST_WITH- SECOND_WITH - toLeft + SECOND_WITH / sum * j + 4, WITH + i * WITH + height, p);
					//篮球
					if (j == list.get(line - 1 - i).getBallNum().length - 1&& iGameType.equals("cjdlt")) {
						p.setColor(0xFF073AAC);
						canvas.drawText(","+ PublicMethod.isTen(blueList.get(line - 1 - i).getBallNum()[1]),FIRST_WITH - SECOND_WITH - toLeft + SECOND_WITH/ sum * (j + 2) + 4, WITH + i * WITH+ height, p);
						p.setColor(0xFFDE0201);
					}
					
					if (j == list.get(line - 1 - i).getBallNum().length - 1&& (iGameType.equals("ssq")|| iGameType.equals("qlc") || iGameType.equals("cjdlt"))) {
						p.setColor(0xFF073AAC);
						canvas.drawText(","+ PublicMethod.isTen(blueList.get(line - 1 - i).getBallNum()[0]),FIRST_WITH - SECOND_WITH - toLeft + SECOND_WITH/ sum * (j + 1) + 4, WITH + i * WITH+ height, p);
						p.setColor(0xFFDE0201);
					}

				}
			}
		}
		p.setColor(Color.GRAY);
		canvas.drawLine(FIRST_WITH - SECOND_WITH - toLeft, 0, FIRST_WITH
				- SECOND_WITH - toLeft, getHeight(), p);
		canvas.drawLine(FIRST_WITH, 0, FIRST_WITH, getHeight(), p);
	}

	/**
	 * 绘制表格
	 */
	public void onDrawTable(Canvas canvas, int x, int y) {
		for (int i = 0; i < line; i++) {
			for (int j = 0; j < row; j++) {
				if (i % 2 == 0) {
					if (j % 2 == 0) {
						canvas.drawBitmap(bitWhite, FIRST_WITH + j * WITH + x,
								WITH + i * WITH + y, null);
					} else {
						canvas.drawBitmap(bitGrey, FIRST_WITH + j * WITH + x,
								WITH + i * WITH + y, null);
					}
				} else {
					if (j % 2 == 0) {
						canvas.drawBitmap(bitGrey, FIRST_WITH + j * WITH + x,
								WITH + i * WITH + y, null);
					} else {
						canvas.drawBitmap(bitWhite, FIRST_WITH + j * WITH + x,
								WITH + i * WITH + y, null);
					}
				}
			}
		}
	}

	/**
	 * 绘制小球
	 * 
	 * @param canvas
	 */
	public void onDrawCenter(Canvas canvas) {
		onDrawTable(canvas, 0, 0);
		drawLine(canvas);
		for (int i = 0; i < line; i++) {
			for (int j = 0; j < row; j++) {
				int balls[] = list.get(line - 1 - i).getBallNum();
				int num = j + startNum;
				int height = (int) (WITH - (8 * release));
				int with = (int) (WITH / 2 - (8 * release));
				for (int n = 0; n < balls.length; n++) {
					if (num == balls[n]) {
						if (isRed) {
							canvas.drawBitmap(bitRedBall,
									FIRST_WITH + j * WITH, WITH + i * WITH,
									null);
						} else {
							canvas.drawBitmap(bitBlueBall, FIRST_WITH + j
									* WITH, WITH + i * WITH, null);
						}
						p.setColor(Color.WHITE);
						canvas.drawText("" + PublicMethod.isTen(balls[n]),
								FIRST_WITH + j * WITH + with, WITH + i * WITH
										+ height, p);
					}
				}
			}
		}

	}

	/**
	 * 绘制连线
	 * 
	 * @param canvas
	 */
	public void drawLine(Canvas canvas) {
		p.setColor(Color.GRAY);
		p.setStrokeWidth(3);
		boolean isDraw = false;
		int blueX = 0;
		int blueY = 0;
		int blueStartX = 0;
		int blueStartY = 0;
		int blueNum = 0;
		onDrawTable(canvas, 0, 0);
		for (int i = 0; i < line; i++) {
			for (int j = 0; j < row; j++) {
				int balls[] = list.get(line - 1 - i).getBallNum();
				int num = j + startNum;
				for (int n = 0; n < balls.length; n++) {
					if (num == balls[n]) {
						if (!isRed&&!iGameType.equals("gd-10")) {
							if (isDraw) {
								blueStartX = FIRST_WITH + j * WITH + WITH / 2;
								blueStartY = WITH + i * WITH + WITH / 2;
								canvas.drawLine(blueStartX, blueStartY, blueX,
										blueY, p);
								blueX = blueStartX;
								blueY = blueStartY;
								blueNum = num;
							} else {
								isDraw = true;
								blueNum = num;
								blueX = FIRST_WITH + j * WITH + WITH / 2;
								blueY = WITH + i * WITH + WITH / 2;
							}

						}
					}
				}
			}
		}

	}

	/**
	 * 绘制福彩3d连线
	 * 
	 * @param canvas
	 */
	public void drawLine(Canvas canvas, int x, int y, int m) {
		p.setColor(Color.GRAY);
		p.setStrokeWidth(3);
		boolean isDraw = false;
		int blueX = 0;
		int blueY = 0;
		int blueStartX = 0;
		int blueStartY = 0;
		int blueNum = 0;
		for (int i = 0; i < line; i++) {
			for (int j = 0; j < row; j++) {
				int balls[] = list.get(line - 1 - i).getBallNum();
				int num = j + startNum;
				if (num == balls[m]) {
					if (isDraw) {
						blueStartX = FIRST_WITH + j * WITH + WITH / 2;
						blueStartY = WITH + i * WITH + WITH / 2;
						canvas.drawLine(blueStartX + x, blueStartY + y, blueX
								+ x, blueY + y, p);
						blueX = blueStartX;
						blueY = blueStartY;
						blueNum = num;
					} else {
						isDraw = true;
						blueNum = num;
						blueX = FIRST_WITH + j * WITH + WITH / 2;
						blueY = WITH + i * WITH + WITH / 2;
					}
				}
			}
		}

	}

	/**
	 * 福彩3d小球
	 */
	public void onDrawCenterPL3(Canvas canvas, int ballNum) {
		for (int m = 0; m < ballNum; m++) {
			onDrawTable(canvas, m * row * WITH, 0);
			drawLine(canvas, m * row * WITH, 0, m);
			for (int i = 0; i < line; i++) {
				for (int j = 0; j < row; j++) {
					int ballColor = 0;
					if (m % 2 == 0) {
						ballColor = R.drawable.notice_ball_red;
					} else {
						ballColor = R.drawable.notice_ball_blue;
					}
					int balls[] = list.get(line - 1 - i).getBallNum();
					int num = j + startNum;
					int height = (int) (WITH - (8 * release));
					int with = (int) (WITH / 2 - (8 * release));
					if (num == balls[m]) {
						canvas.drawBitmap(bitRedBall, (FIRST_WITH + j * WITH)+ (row * WITH * m), WITH + i * WITH, null);
						p.setColor(Color.WHITE);
						canvas.drawText("" +balls[m],(FIRST_WITH + j * WITH + with) + (row * WITH * m)+4, WITH + i * WITH+ height, p);
//						canvas.drawText(text, x, y, paint);
					}
				}
			}
		}
	}

	/**
	 * 获取图片
	 */
	protected Bitmap getBitmapFromRes(int aResId, int _width, int _height) {
		Resources res = this.getContext().getResources();
		InputStream is = res.openRawResource(aResId);
		Bitmap bitmap = new BitmapDrawable(is).getBitmap();
		int width = 0;
		int height = 0;
		float sw;
		float sh;
		Matrix matrix;
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		sw = ((float) _width) / width;
		sh = ((float) _height) / height;
		matrix = new Matrix();
		matrix.postScale(sw, sh);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return bitmap;
	}

	public class RowInfo {
		private String issue = "";// 每行期号
		private int ballNum[];

		public String getIssue() {
			return issue;
		}

		public void setIssue(String issue) {
			this.issue = issue;
		}

		public int[] getBallNum() {
			return ballNum;
		}

		public void setBallNum(int[] ballNum) {
			this.ballNum = ballNum;
		}

		public RowInfo() {

		}
	}
}
