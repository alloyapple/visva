package vsvteam.outsource.leanappandroid.mapobjects;

import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.database.TProcessDataBase;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Html;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ObjProcessBox extends MapObject {
	private TProcessDataBase mTProcessDataBase;

	public ObjProcessBox(Context pContext, float pX, float pY,
			TProcessDataBase pTProcessDataBase) {
		super(pContext, pX, pY);
		// TODO Auto-generated constructor stub
		mTProcessDataBase = pTProcessDataBase;
		mLayout.setOrientation(LinearLayout.VERTICAL);
		mLayout.setLayoutParams(new RelativeLayout.LayoutParams(120, 140));
		getParam().setMargins((int) pX, (int) pY, getParam().rightMargin,
				getParam().bottomMargin);
		mLayout.requestLayout();
		mLayout.setBackgroundResource(R.drawable.obj_process_box);
		initialize();
	}

	public void initialize() {
		TextView mTextViewName = new TextView(mContext);
		LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 0, 0.18f);
		mTextViewName.setLayoutParams(param1);
		param1.bottomMargin = 4;
		mTextViewName.setGravity(Gravity.CENTER | Gravity.CENTER);
		mTextViewName.setLines(2);
		mTextViewName.setText(mTProcessDataBase.getProcessName());
		mTextViewName.setTextColor(Color.BLACK);

		TextView mTextViewScript = new TextView(mContext);
		mTextViewScript.setLayoutParams(param1);
		param1.bottomMargin = 4;
		mTextViewScript.setGravity(Gravity.CENTER | Gravity.CENTER);
		mTextViewScript.setLines(2);
		mTextViewScript.setText(mTProcessDataBase.getProcessDescription());
		mTextViewScript.setTextColor(Color.BLACK);

		TextView mTextViewInfo = new TextView(mContext);
		LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 0, 0.5f);
		mTextViewInfo.setLayoutParams(param2);
		param2.leftMargin = 6;
		param2.topMargin = 4;
		mTextViewInfo.setGravity(Gravity.LEFT | Gravity.TOP);
		mTextViewInfo.setLines(4);
		String info = mContext.getResources().getString(
				R.string.content_process_box);
		info.replace("YYY",
				String.valueOf(mTProcessDataBase.getValueAddingTime()));
		info.replace("XXX",
				String.valueOf(mTProcessDataBase.getNonValueAddingTime()));
		info.replace("UUU", String.valueOf(mTProcessDataBase.getUpTime()));
		info.replace("$$$",
				String.valueOf(mTProcessDataBase.getDefectPercent()));
		mTextViewInfo.setText(Html.fromHtml(info));
		mTextViewInfo.setTextColor(Color.BLACK);
		LinearLayout mLinear = new LinearLayout(mContext);
		mLinear.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 0, 0.14f));
		mLinear.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout mLinearOperator = new LinearLayout(mContext);
		mLinearOperator.setLayoutParams(new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.FILL_PARENT, 0.5f));
		mLinearOperator.setOrientation(LinearLayout.HORIZONTAL);
		ImageView iconOperator = new ImageView(mContext);
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		mParams.leftMargin = 6;
		mParams.topMargin = 4;
		iconOperator.setLayoutParams(mParams);
		iconOperator.setImageResource(R.drawable.obj_operator);
		((LinearLayout.LayoutParams) iconOperator.getLayoutParams()).leftMargin = 4;
		mLinearOperator.addView(iconOperator);
		TextView mTextNumberOperator = new TextView(mContext);
		mTextNumberOperator.setLayoutParams(mParams);
		mTextNumberOperator.setGravity(Gravity.CENTER | Gravity.CENTER);
		mTextNumberOperator.setText("2");
		mTextNumberOperator.setTextColor(Color.BLACK);
		mLinearOperator.addView(mTextNumberOperator);

		LinearLayout mLinearGrass = new LinearLayout(mContext);
		mLinearGrass.setLayoutParams(new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.FILL_PARENT, 0.5f));
		mLinearGrass.setOrientation(LinearLayout.HORIZONTAL);

		ImageView iconGrass = new ImageView(mContext);
		iconGrass.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		iconGrass.setImageResource(R.drawable.obj_glass);
		mLinearGrass.addView(iconGrass);
		mLinear.addView(mLinearOperator);
		mLinear.addView(mLinearGrass);

		mLayout.addView(mTextViewName);
		mLayout.addView(mTextViewScript);
		mLayout.addView(mTextViewInfo);
		mLayout.addView(mLinear);
	}

	@Override
	public void drawObjects(Canvas cv) {
		// TODO Auto-generated method stub

	}

}
