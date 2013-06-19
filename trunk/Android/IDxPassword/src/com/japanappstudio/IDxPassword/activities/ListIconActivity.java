package com.japanappstudio.IDxPassword.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class ListIconActivity extends BaseActivity {
	public static final int DIALOG_DELETE = 0;
	private LinearLayout mLinearListIcon;
	private int mCatalogueName[] = { R.string.list_carrier_title,
			R.string.list_e_commerce, R.string.list_webservice,
			R.string.list_finance, R.string.list_general };
	private String mCountryName[] = { "USA", "JAPAN", "CHINA", "EURO", "OTHER" };
	private ArrayList<int[]> mIdIcon = new ArrayList<int[]>();

	public static int idEcomerce_USA[] = { R.drawable.commerce_usa_01,
			R.drawable.commerce_usa_02, R.drawable.commerce_usa_03 };
	public static int idEcomerce_JAPAN[] = { R.drawable.commerce_japan_01,
			R.drawable.commerce_japan_02, R.drawable.commerce_japan_03,
			R.drawable.commerce_japan_04, R.drawable.commerce_japan_05 };
	public static int idEcomerce_CHINA[] = {};
	public static int idEcomerce_EURO[] = {};
	public static int idEcomerce_OTHER[] = {};

	public static int idCarrier_USA[] = { R.drawable.carrier_usa_01,
			R.drawable.carrier_usa_02, R.drawable.carrier_usa_03,
			R.drawable.carrier_usa_04, R.drawable.carrier_usa_05,
			R.drawable.carrier_usa_06, R.drawable.carrier_usa_07,
			R.drawable.carrier_usa_08 };
	public static int idCarrier_JAPAN[] = { R.drawable.carrier_japan_01,
			R.drawable.carrier_japan_02, R.drawable.carrier_japan_03,
			R.drawable.carrier_japan_04, R.drawable.carrier_japan_05,
			R.drawable.carrier_japan_06, R.drawable.carrier_japan_07,
			R.drawable.carrier_japan_08, R.drawable.carrier_japan_09,
			R.drawable.carrier_japan_10 };
	public static int idCarrier_CHINA[] = {};
	public static int idCarrier_EURO[] = { R.drawable.carrier_euro_01 };
	public static int idCarrier_OTHER[] = {};

	public static int idWebservice_USA[] = { R.drawable.web_usa_01,
			R.drawable.web_usa_02, R.drawable.web_usa_03,
			R.drawable.web_usa_04, R.drawable.web_usa_05,
			R.drawable.web_usa_06, R.drawable.web_usa_07,
			R.drawable.web_usa_08, R.drawable.web_usa_09, R.drawable.web_usa_10 };
	public static int idWebservice_JAPAN[] = { R.drawable.web_japan_01,
			R.drawable.web_japan_02, R.drawable.web_japan_03,
			R.drawable.web_japan_04, R.drawable.web_japan_05,
			R.drawable.web_japan_06, R.drawable.web_japan_07,
			R.drawable.web_japan_08, R.drawable.web_japan_09,
			R.drawable.web_japan_10, R.drawable.web_japan_11,
			R.drawable.web_japan_12, R.drawable.web_japan_13,
			R.drawable.web_japan_14, R.drawable.web_japan_15,
			R.drawable.web_japan_16, R.drawable.web_japan_17,
			R.drawable.web_japan_18, R.drawable.web_japan_19,
			R.drawable.web_japan_20, R.drawable.web_japan_21,
			R.drawable.web_japan_22, R.drawable.web_japan_23,
			R.drawable.web_japan_24, R.drawable.web_japan_25,
			R.drawable.web_japan_26, R.drawable.web_japan_27,
			R.drawable.web_japan_28 };
	public static int idWebservice_CHINA[] = {};
	public static int idWebservice_EURO[] = {};
	public static int idWebservice_OTHER[] = {};

	public static int idFinance_USA[] = { R.drawable.finance_usa_01,
			R.drawable.finance_usa_02, R.drawable.finance_usa_03,
			R.drawable.finance_usa_04, R.drawable.finance_usa_05,
			R.drawable.finance_usa_06, R.drawable.finance_usa_07,
			R.drawable.finance_usa_08, R.drawable.finance_usa_09,
			R.drawable.finance_usa_10, R.drawable.finance_usa_11,
			R.drawable.finance_usa_12, R.drawable.finance_usa_13,
			R.drawable.finance_usa_14, R.drawable.finance_usa_15,
			R.drawable.finance_usa_16, R.drawable.finance_usa_17,
			R.drawable.finance_usa_18, R.drawable.finance_usa_19,
			R.drawable.finance_usa_20, R.drawable.finance_usa_21,
			R.drawable.finance_usa_22, R.drawable.finance_usa_23,
			R.drawable.finance_usa_24, R.drawable.finance_usa_25,
			R.drawable.finance_usa_26, R.drawable.finance_usa_27,
			R.drawable.finance_usa_28, R.drawable.finance_usa_29,
			R.drawable.finance_usa_30, R.drawable.finance_usa_31,
			R.drawable.finance_usa_32, R.drawable.finance_usa_33,
			R.drawable.finance_usa_34, R.drawable.finance_usa_35,
			R.drawable.finance_usa_36, R.drawable.finance_usa_37,
			R.drawable.finance_usa_38, R.drawable.finance_usa_39,
			R.drawable.finance_usa_40, R.drawable.finance_usa_41,
			R.drawable.finance_usa_42, R.drawable.finance_usa_43 };
	public static int idFinance_JAPAN[] = { R.drawable.finance_japan_01,
			R.drawable.finance_japan_02, R.drawable.finance_japan_03,
			R.drawable.finance_japan_04, R.drawable.finance_japan_05,
			R.drawable.finance_japan_06, R.drawable.finance_japan_07,
			R.drawable.finance_japan_08, R.drawable.finance_japan_09,
			R.drawable.finance_japan_10, R.drawable.finance_japan_11,
			R.drawable.finance_japan_12, R.drawable.finance_japan_13,
			R.drawable.finance_japan_14, R.drawable.finance_japan_15,
			R.drawable.finance_japan_16, R.drawable.finance_japan_17,
			R.drawable.finance_japan_18, R.drawable.finance_japan_19,
			R.drawable.finance_japan_20, R.drawable.finance_japan_21,
			R.drawable.finance_japan_22, R.drawable.finance_japan_23,
			R.drawable.finance_japan_24, R.drawable.finance_japan_25,
			R.drawable.finance_japan_26, R.drawable.finance_japan_27,
			R.drawable.finance_japan_28, R.drawable.finance_japan_29,
			R.drawable.finance_japan_30, R.drawable.finance_japan_31,
			R.drawable.finance_japan_32, R.drawable.finance_japan_33,
			R.drawable.finance_japan_34, R.drawable.finance_japan_35,
			R.drawable.finance_japan_36, R.drawable.finance_japan_37,
			R.drawable.finance_japan_38, R.drawable.finance_japan_39,
			R.drawable.finance_japan_40, R.drawable.finance_japan_41,
			R.drawable.finance_japan_42, R.drawable.finance_japan_43,
			R.drawable.finance_japan_44, R.drawable.finance_japan_45,
			R.drawable.finance_japan_46, R.drawable.finance_japan_47,
			R.drawable.finance_japan_48, R.drawable.finance_japan_49,
			R.drawable.finance_japan_50, R.drawable.finance_japan_51,
			R.drawable.finance_japan_52, R.drawable.finance_japan_53,
			R.drawable.finance_japan_54, R.drawable.finance_japan_55,
			R.drawable.finance_japan_56, R.drawable.finance_japan_57,
			R.drawable.finance_japan_58, R.drawable.finance_japan_59,
			R.drawable.finance_japan_60, R.drawable.finance_japan_61,
			R.drawable.finance_japan_62, R.drawable.finance_japan_63,
			R.drawable.finance_japan_64 };
	public static int idFinance_CHINA[] = { R.drawable.finance_china_01,
			R.drawable.finance_china_02, R.drawable.finance_china_03,
			R.drawable.finance_china_04, R.drawable.finance_china_05,
			R.drawable.finance_china_06, R.drawable.finance_china_07 };
	public static int idFinance_EURO[] = { R.drawable.finance_euro_01,
			R.drawable.finance_euro_02, R.drawable.finance_euro_03,
			R.drawable.finance_euro_04, R.drawable.finance_euro_05,
			R.drawable.finance_euro_06 };
	public static int idFinance_OTHER[] = { R.drawable.finance_other_01,
			R.drawable.finance_other_02, R.drawable.finance_other_03,
			R.drawable.finance_other_04, R.drawable.finance_other_05,
			R.drawable.finance_other_06, R.drawable.finance_other_07,
			R.drawable.finance_other_08, R.drawable.finance_other_09,
			R.drawable.finance_other_10, R.drawable.finance_other_11,
			R.drawable.finance_other_12, R.drawable.finance_other_13 };

	public static int idGeneral_USA[] = {};
	public static int idGeneral_JAPAN[] = {};
	public static int idGeneral_CHINA[] = { R.drawable.air_china_01,
			R.drawable.air_china_02, R.drawable.air_china_03 };
	public static int idGeneral_EURO[] = { R.drawable.air_euro_01 };
	public static int idGeneral_OTHER[] = { R.drawable.air_other_01,
			R.drawable.air_other_02, R.drawable.air_other_03,
			R.drawable.air_other_04, R.drawable.air_other_05,
			R.drawable.air_other_06, R.drawable.air_other_07,
			R.drawable.air_other_08, R.drawable.air_other_09,
			R.drawable.air_other_10, R.drawable.air_other_11,
			R.drawable.air_other_12, R.drawable.air_other_13,
			R.drawable.air_other_14, R.drawable.air_other_15,
			R.drawable.air_other_16, R.drawable.air_other_17,
			R.drawable.air_other_18, R.drawable.air_other_19,
			R.drawable.air_other_20, R.drawable.air_other_21,
			R.drawable.air_other_22, R.drawable.air_other_23,
			R.drawable.air_other_24, R.drawable.air_other_25,
			R.drawable.air_other_26, R.drawable.general_other_01,
			R.drawable.general_other_02, R.drawable.general_other_03,
			R.drawable.general_other_04, R.drawable.general_other_05,
			R.drawable.general_other_06, R.drawable.general_other_07,
			R.drawable.general_other_08, R.drawable.general_other_09,
			R.drawable.general_other_10, R.drawable.general_other_11,
			R.drawable.general_other_12, R.drawable.general_other_13,
			R.drawable.general_other_14, R.drawable.general_other_15,
			R.drawable.general_other_16, R.drawable.general_other_17,
			R.drawable.general_other_18, R.drawable.general_other_19,
			R.drawable.general_other_20, R.drawable.general_other_21,
			R.drawable.general_other_22, R.drawable.general_other_23,
			R.drawable.general_other_24, R.drawable.general_other_25,
			R.drawable.general_other_26, R.drawable.general_other_27,
			R.drawable.general_other_28, R.drawable.general_other_29,
			R.drawable.general_other_30, R.drawable.general_other_31,
			R.drawable.general_other_32, R.drawable.general_other_33,
			R.drawable.general_other_34, R.drawable.general_other_35,
			R.drawable.general_other_36, R.drawable.general_other_37,
			R.drawable.general_other_38, R.drawable.general_other_39,
			R.drawable.general_other_40, R.drawable.general_other_41,
			R.drawable.general_other_42, R.drawable.general_other_43,
			R.drawable.general_other_44, R.drawable.general_other_45,
			R.drawable.general_other_46, R.drawable.general_other_47,
			R.drawable.general_other_48, R.drawable.general_other_49,
			R.drawable.general_other_50 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_list_icon);
		mIdIcon.add(idCarrier_USA);
		mIdIcon.add(idCarrier_JAPAN);
		mIdIcon.add(idCarrier_CHINA);
		mIdIcon.add(idCarrier_EURO);
		mIdIcon.add(idCarrier_OTHER);
		
		mIdIcon.add(idEcomerce_USA);
		mIdIcon.add(idEcomerce_JAPAN);
		mIdIcon.add(idEcomerce_CHINA);
		mIdIcon.add(idEcomerce_EURO);
		mIdIcon.add(idEcomerce_OTHER);
		
		mIdIcon.add(idWebservice_USA);
		mIdIcon.add(idWebservice_JAPAN);
		mIdIcon.add(idWebservice_CHINA);
		mIdIcon.add(idWebservice_EURO);
		mIdIcon.add(idWebservice_OTHER);
		
		mIdIcon.add(idFinance_USA);
		mIdIcon.add(idFinance_JAPAN);
		mIdIcon.add(idFinance_CHINA);
		mIdIcon.add(idFinance_EURO);
		mIdIcon.add(idFinance_OTHER);
		
		mIdIcon.add(idGeneral_USA);
		mIdIcon.add(idGeneral_JAPAN);
		mIdIcon.add(idGeneral_CHINA);
		mIdIcon.add(idGeneral_EURO);
		mIdIcon.add(idGeneral_OTHER);
		mLinearListIcon = (LinearLayout) findViewById(R.id.id_linear_list_icon);
		initListIcon();
		initAdmod();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			onReturn(null);
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void initAdmod() {
		AdView adview = (AdView) findViewById(R.id.main_adView);
		AdRequest re = new AdRequest();
		if (adview != null) {
			adview.loadAd(re);
			adview.setVisibility(View.VISIBLE);
		}
	}

	@SuppressWarnings("deprecation")
	public void initListIcon() {
		for (int i = 0; i < mCatalogueName.length; i++) {
			LinearLayout mLinearItemCatalogue = new LinearLayout(this);
			mLinearItemCatalogue.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			mLinearItemCatalogue.setOrientation(LinearLayout.VERTICAL);
			{
				LinearLayout mLinearTitle = new LinearLayout(this);
				mLinearTitle.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT, 46));
				mLinearTitle.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.list_bar));
				mLinearTitle.setOrientation(LinearLayout.HORIZONTAL);

				final Button mBtnControl = new Button(this);
				mBtnControl.setLayoutParams(new LinearLayout.LayoutParams(30,
						30));
				((LinearLayout.LayoutParams) mBtnControl.getLayoutParams()).leftMargin = 10;
				((LinearLayout.LayoutParams) mBtnControl.getLayoutParams()).gravity = Gravity.CENTER;
				mBtnControl.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_add));
				mLinearTitle.addView(mBtnControl);

				TextView mTextView = new TextView(this);
				mTextView.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
				((LinearLayout.LayoutParams) mTextView.getLayoutParams()).leftMargin = 10;
				((LinearLayout.LayoutParams) mTextView.getLayoutParams()).topMargin = 5;
				mTextView.setTextColor(Color.WHITE);
				mTextView.setTypeface(null, Typeface.BOLD);
				mTextView.setText(getResources().getString(mCatalogueName[i]));
				mLinearTitle.addView(mTextView);

				final LinearLayout mLinearCountry = new LinearLayout(this);
				mLinearCountry.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
				mLinearCountry.setOrientation(LinearLayout.VERTICAL);
				{
					for (int m = 0; m < mCountryName.length; m++) {
						final int catalogue = i * mCountryName.length + m;
						if(mIdIcon.get(catalogue).length==0)
							continue;
						LinearLayout mLinearCountryTitle = new LinearLayout(
								this);
						mLinearCountryTitle
								.setLayoutParams(new LinearLayout.LayoutParams(
										LinearLayout.LayoutParams.FILL_PARENT,
										46));
						mLinearCountryTitle
								.setBackgroundDrawable(getResources()
										.getDrawable(R.drawable.list_bar2));
						mLinearCountryTitle
								.setOrientation(LinearLayout.HORIZONTAL);

						final Button mBtnControlCountry = new Button(this);
						mBtnControlCountry
								.setLayoutParams(new LinearLayout.LayoutParams(
										30, 30));
						((LinearLayout.LayoutParams) mBtnControlCountry
								.getLayoutParams()).leftMargin = 40;
						((LinearLayout.LayoutParams) mBtnControlCountry
								.getLayoutParams()).gravity = Gravity.CENTER;
						mBtnControlCountry.setBackgroundDrawable(getResources()
								.getDrawable(R.drawable.btn_add));
						mLinearCountryTitle.addView(mBtnControlCountry);

						TextView mTextViewCountry = new TextView(this);
						mTextViewCountry
								.setLayoutParams(new LinearLayout.LayoutParams(
										LinearLayout.LayoutParams.WRAP_CONTENT,
										LinearLayout.LayoutParams.WRAP_CONTENT));
						((LinearLayout.LayoutParams) mTextViewCountry
								.getLayoutParams()).leftMargin = 10;
						((LinearLayout.LayoutParams) mTextViewCountry
								.getLayoutParams()).topMargin = 5;
						mTextViewCountry.setTextColor(Color.WHITE);
						mTextViewCountry.setTypeface(null, Typeface.BOLD);
						mTextViewCountry.setText(mCountryName[m]);
						mLinearCountryTitle.addView(mTextViewCountry);

						mLinearCountry.addView(mLinearCountryTitle);

						final TableLayout mTableLayout = new TableLayout(this);
						mTableLayout
								.setLayoutParams(new LinearLayout.LayoutParams(
										LinearLayout.LayoutParams.FILL_PARENT,
										LinearLayout.LayoutParams.WRAP_CONTENT));
						int numRows = 0;
						int l = i * mCountryName.length + m;
						if (mIdIcon.get(l).length % 4 == 0) {
							numRows = mIdIcon.get(l).length / 4;
						} else {
							numRows = mIdIcon.get(l).length / 4 + 1;
						}
						for (int j = 0; j < numRows; j++) {
							TableRow mTableRow = new TableRow(this);
							mTableRow
									.setLayoutParams(new TableLayout.LayoutParams(
											TableLayout.LayoutParams.FILL_PARENT,
											TableLayout.LayoutParams.WRAP_CONTENT));
							((TableLayout.LayoutParams) mTableRow
									.getLayoutParams()).topMargin = 5;
							((TableLayout.LayoutParams) mTableRow
									.getLayoutParams()).bottomMargin = 5;
							for (int k = 0; k < 4; k++) {
								final int index = j * 4 + k;
								ImageButton mIcon = new ImageButton(this);
								mIcon.setLayoutParams(new TableRow.LayoutParams(
										0, TableRow.LayoutParams.WRAP_CONTENT,
										0.25f));
								((TableRow.LayoutParams) mIcon
										.getLayoutParams()).leftMargin = 5;
								((TableRow.LayoutParams) mIcon
										.getLayoutParams()).rightMargin = 5;
								if ((index < mIdIcon.get(l).length)) {
									mIcon.setBackgroundDrawable(getResources()
											.getDrawable(
													mIdIcon.get(l)[j * 4 + k]));
									mIcon.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											selectIcon(catalogue, index);
										}
									});
								} else {
									mIcon.setBackgroundColor(Color.TRANSPARENT);
								}
								mTableRow.addView(mIcon);

							}
							mTableLayout.addView(mTableRow);
						}
						mLinearCountry.addView(mTableLayout);
						mTableLayout.setVisibility(View.GONE);
						mBtnControlCountry
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										if (mTableLayout.getVisibility() == View.VISIBLE) {
											mTableLayout
													.setVisibility(View.GONE);
											mBtnControlCountry
													.setBackgroundDrawable(ListIconActivity.this
															.getResources()
															.getDrawable(
																	R.drawable.btn_add));
										} else if (mTableLayout.getVisibility() == View.GONE) {
											mTableLayout
													.setVisibility(View.VISIBLE);
											mBtnControlCountry
													.setBackgroundDrawable(ListIconActivity.this
															.getResources()
															.getDrawable(
																	R.drawable.btn_sub));
										}
									}
								});
					}

					mLinearCountry.setVisibility(View.GONE);
					mBtnControl.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (mLinearCountry.getVisibility() == View.VISIBLE) {
								mLinearCountry.setVisibility(View.GONE);
								mBtnControl
										.setBackgroundDrawable(ListIconActivity.this
												.getResources().getDrawable(
														R.drawable.btn_add));
							} else if (mLinearCountry.getVisibility() == View.GONE) {
								mLinearCountry.setVisibility(View.VISIBLE);
								mBtnControl
										.setBackgroundDrawable(ListIconActivity.this
												.getResources().getDrawable(
														R.drawable.btn_sub));
							}
						}
					});

				}

				mLinearItemCatalogue.addView(mLinearTitle);
				mLinearItemCatalogue.addView(mLinearCountry);
			}
			mLinearListIcon.addView(mLinearItemCatalogue);
		}
	}

	public void selectIcon(int cataloge, int index) {
		EditIdPasswordActivity.updateIcon(getResources().getDrawable(
				mIdIcon.get(cataloge)[index]));
		EditIdPasswordActivity.startActivity(ListIconActivity.this, 2);
		finish();
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, ListIconActivity.class);
		activity.startActivity(i);
	}

	public void onReturn(View v) {
		EditIdPasswordActivity.startActivity(ListIconActivity.this, 2);
		finish();

	}

	public void onEditImage(View v) {
		Intent i = new Intent(this, EditIconActivity.class);
		i.putExtra("modeBundleEditIcon", 1);
		startActivity(i);
		finish();
	}

	@SuppressWarnings("deprecation")
	public void onDeleteImage(View v) {
		showDialog(DIALOG_DELETE);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case DIALOG_DELETE:
			builder.setTitle(getResources().getString(R.string.confirm_delete))

					.setPositiveButton(
							getResources().getString(R.string.confirm_ok),
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									EditIdPasswordActivity
											.updateIcon(getResources()
													.getDrawable(
															R.drawable.default_icon));
									EditIdPasswordActivity.startActivity(
											ListIconActivity.this, 2);
									ListIconActivity.this.finish();
								}
							})
					.setNegativeButton(
							getResources().getString(R.string.confirm_cancel),
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});
			return builder.create();
		default:
			break;
		}
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

}
