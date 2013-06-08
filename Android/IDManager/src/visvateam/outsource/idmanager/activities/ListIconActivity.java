package visvateam.outsource.idmanager.activities;

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
	public static int idAirline_USA[] = { R.drawable.airline_0,
			R.drawable.airline_1, R.drawable.airline_2, R.drawable.airline_3,
			R.drawable.airline_4, R.drawable.airline_5, R.drawable.airline_6 };
	public static int idAirline_JAPAN[] = { R.drawable.airline_7,
			R.drawable.airline_8, R.drawable.airline_9, R.drawable.airline_10,
			R.drawable.airline_11, R.drawable.airline_12 };
	public static int idAirline_CHINA[] = { R.drawable.airline_13,
			R.drawable.airline_14, R.drawable.airline_15,
			R.drawable.airline_16, R.drawable.airline_17, R.drawable.airline_18 };
	public static int idAirline_EURO[] = { R.drawable.airline_19,
			R.drawable.airline_20, R.drawable.airline_21,
			R.drawable.airline_22, R.drawable.airline_23, R.drawable.airline_24 };
	public static int idAirline_OTHER[] = { R.drawable.airline_25,
			R.drawable.airline_26, R.drawable.airline_27,
			R.drawable.airline_28, R.drawable.airline_29 };
//	public static int idAirline[] = {};
	public static int idEcomerce_USA[] = { R.drawable.e_commerce_0 };
	public static int idEcomerce_JAPAN[] = { R.drawable.e_commerce_1 };
	public static int idEcomerce_CHINA[] = { R.drawable.e_commerce_2 };
	public static int idEcomerce_EURO[] = { R.drawable.e_commerce_3 };
	public static int idEcomerce_OTHER[] = { R.drawable.e_commerce_4,
			R.drawable.e_commerce_5 };
//	public static int idEcomerce[] = { R.drawable.e_commerce_0,
//			R.drawable.e_commerce_1, R.drawable.e_commerce_2,
//			R.drawable.e_commerce_3, R.drawable.e_commerce_4,
//			R.drawable.e_commerce_5 };
	public static int idIsp_USA[] = { R.drawable.isp_0, R.drawable.isp_1 };
	public static int idIsp_JAPAN[] = { R.drawable.isp_2, R.drawable.isp_3 };
	public static int idIsp_CHINA[] = { R.drawable.isp_4, R.drawable.isp_5 };
	public static int idIsp_EURO[] = { R.drawable.isp_6, R.drawable.isp_7 };
	public static int idIsp_OTHER[] = { R.drawable.isp_8, R.drawable.isp_9 };
//	public static int idIsp[] = { R.drawable.isp_0, R.drawable.isp_1,
//			R.drawable.isp_2, R.drawable.isp_3, R.drawable.isp_4,
//			R.drawable.isp_5, R.drawable.isp_6, R.drawable.isp_7,
//			R.drawable.isp_8, R.drawable.isp_9 };
	public static int idCarrier_USA[] = { R.drawable.carrier_0,
			R.drawable.carrier_1 };
	public static int idCarrier_JAPAN[] = { R.drawable.carrier_2,
			R.drawable.carrier_3 };
	public static int idCarrier_CHINA[] = { R.drawable.carrier_4,
			R.drawable.carrier_5 };
	public static int idCarrier_EURO[] = { R.drawable.carrier_6,
			R.drawable.carrier_7, R.drawable.carrier_8 };
	public static int idCarrier_OTHER[] = { R.drawable.carrier_9,
			R.drawable.carrier_10, R.drawable.carrier_11 };
//	public static int idCarrier[] = { R.drawable.carrier_0,
//			R.drawable.carrier_1, R.drawable.carrier_2, R.drawable.carrier_3,
//			R.drawable.carrier_4, R.drawable.carrier_5, R.drawable.carrier_6,
//			R.drawable.carrier_7, R.drawable.carrier_8, R.drawable.carrier_9,
//			R.drawable.carrier_10, R.drawable.carrier_11 };
	public static int idWebservice_USA[] = { R.drawable.webservice_0,
			R.drawable.webservice_1, R.drawable.webservice_2,
			R.drawable.webservice_3, R.drawable.webservice_4,
			R.drawable.webservice_5, R.drawable.webservice_6 };
	public static int idWebservice_JAPAN[] = { R.drawable.webservice_7,
			R.drawable.webservice_8, R.drawable.webservice_9,
			R.drawable.webservice_10, R.drawable.webservice_11,
			R.drawable.webservice_12 };
	public static int idWebservice_CHINA[] = { R.drawable.webservice_13,
			R.drawable.webservice_14, R.drawable.webservice_15,
			R.drawable.webservice_16, R.drawable.webservice_17,
			R.drawable.webservice_18 };
	public static int idWebservice_EURO[] = { R.drawable.webservice_19,
			R.drawable.webservice_20, R.drawable.webservice_21,
			R.drawable.webservice_22, R.drawable.webservice_23,
			R.drawable.webservice_24 };
	public static int idWebservice_OTHER[] = { R.drawable.webservice_25,
			R.drawable.webservice_26, R.drawable.webservice_27,
			R.drawable.webservice_28, R.drawable.webservice_29,
			R.drawable.webservice_30, R.drawable.webservice_31,
			R.drawable.webservice_32, R.drawable.webservice_33 };
	public static int idFinance_USA[] = { R.drawable.finance_0,
			R.drawable.finance_1, R.drawable.finance_2, R.drawable.finance_3,
			R.drawable.finance_4, R.drawable.finance_5, R.drawable.finance_6,
			R.drawable.finance_7, R.drawable.finance_8, R.drawable.finance_9,
			R.drawable.finance_10, R.drawable.finance_11,
			R.drawable.finance_12, R.drawable.finance_13,
			R.drawable.finance_14, R.drawable.finance_15,
			R.drawable.finance_16, R.drawable.finance_17,
			R.drawable.finance_18, R.drawable.finance_19,
			R.drawable.finance_20, R.drawable.finance_21 };
	public static int idFinance_JAPAN[] = { R.drawable.finance_22,
			R.drawable.finance_23, R.drawable.finance_24,
			R.drawable.finance_25, R.drawable.finance_26,
			R.drawable.finance_27, R.drawable.finance_28,
			R.drawable.finance_29, R.drawable.finance_30,
			R.drawable.finance_31, R.drawable.finance_32,
			R.drawable.finance_33, R.drawable.finance_34,
			R.drawable.finance_35, R.drawable.finance_36,
			R.drawable.finance_37, R.drawable.finance_38,
			R.drawable.finance_39, R.drawable.finance_40,
			R.drawable.finance_41, R.drawable.finance_42, R.drawable.finance_43 };
	public static int idFinance_CHINA[] = { R.drawable.finance_44,
			R.drawable.finance_45, R.drawable.finance_46,
			R.drawable.finance_47, R.drawable.finance_48,
			R.drawable.finance_49, R.drawable.finance_50,
			R.drawable.finance_51, R.drawable.finance_52,
			R.drawable.finance_53, R.drawable.finance_54,
			R.drawable.finance_55, R.drawable.finance_56,
			R.drawable.finance_57, R.drawable.finance_58,
			R.drawable.finance_59, R.drawable.finance_60,
			R.drawable.finance_61, R.drawable.finance_62,
			R.drawable.finance_63, R.drawable.finance_64, R.drawable.finance_65 };
	public static int idFinance_EURO[] = { R.drawable.finance_66,
			R.drawable.finance_67, R.drawable.finance_68,
			R.drawable.finance_69, R.drawable.finance_70,
			R.drawable.finance_71, R.drawable.finance_72,
			R.drawable.finance_73, R.drawable.finance_74,
			R.drawable.finance_75, R.drawable.finance_76,
			R.drawable.finance_77, R.drawable.finance_78,
			R.drawable.finance_79, R.drawable.finance_80,
			R.drawable.finance_81, R.drawable.finance_82,
			R.drawable.finance_83, R.drawable.finance_84,
			R.drawable.finance_85, R.drawable.finance_86, R.drawable.finance_87 };
	public static int idFinance_OTHER[] = { R.drawable.finance_88,
			R.drawable.finance_89, R.drawable.finance_90,
			R.drawable.finance_91, R.drawable.finance_92,
			R.drawable.finance_93, R.drawable.finance_94,
			R.drawable.finance_95, R.drawable.finance_96,
			R.drawable.finance_97, R.drawable.finance_98,
			R.drawable.finance_99, R.drawable.finance_100,
			R.drawable.finance_101, R.drawable.finance_102,
			R.drawable.finance_103, R.drawable.finance_104,
			R.drawable.finance_105, R.drawable.finance_106,
			R.drawable.finance_107, R.drawable.finance_108,
			R.drawable.finance_109, R.drawable.finance_110,
			R.drawable.finance_111, R.drawable.finance_112,
			R.drawable.finance_113, R.drawable.finance_114,
			R.drawable.finance_115, R.drawable.finance_116,
			R.drawable.finance_117, R.drawable.finance_118,
			R.drawable.finance_119, R.drawable.finance_120,
			R.drawable.finance_121, R.drawable.finance_122,
			R.drawable.finance_123, R.drawable.finance_124,
			R.drawable.finance_125, R.drawable.finance_126,
			R.drawable.finance_127, R.drawable.finance_128,
			R.drawable.finance_129, R.drawable.finance_130,
			R.drawable.finance_131 };
	public static int idGeneral_USA[] = { R.drawable.general_1,
			R.drawable.general_2, R.drawable.general_3, R.drawable.general_4,
			R.drawable.general_5, R.drawable.general_6, R.drawable.general_7,
			R.drawable.general_8, R.drawable.general_9, R.drawable.general_10 };
	public static int idGeneral_JAPAN[] = { R.drawable.general_11,
			R.drawable.general_12, R.drawable.general_13,
			R.drawable.general_14, R.drawable.general_15,
			R.drawable.general_16, R.drawable.general_17,
			R.drawable.general_18, R.drawable.general_19, R.drawable.general_20 };
	public static int idGeneral_CHINA[] = { R.drawable.general_21,
			R.drawable.general_22, R.drawable.general_23,
			R.drawable.general_24, R.drawable.general_25,
			R.drawable.general_26, R.drawable.general_27,
			R.drawable.general_28, R.drawable.general_29, R.drawable.general_31 };
	public static int idGeneral_EURO[] = { R.drawable.general_32,
			R.drawable.general_33, R.drawable.general_34,
			R.drawable.general_35, R.drawable.general_36,
			R.drawable.general_37, R.drawable.general_38,
			R.drawable.general_39, R.drawable.general_40, R.drawable.general_41 };
	public static int idGeneral_OTHER[] = { R.drawable.general_42,
			R.drawable.general_43, R.drawable.general_44,
			R.drawable.general_45, R.drawable.general_46,
			R.drawable.general_47, R.drawable.general_48,
			R.drawable.general_49, R.drawable.general_50, R.drawable.general_51 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_list_icon);
		mIdIcon.add(idGeneral_USA);
		mIdIcon.add(idGeneral_JAPAN);
		mIdIcon.add(idGeneral_CHINA);
		mIdIcon.add(idGeneral_EURO);
		mIdIcon.add(idGeneral_OTHER);
		
		mIdIcon.add(idFinance_USA);
		mIdIcon.add(idFinance_JAPAN);
		mIdIcon.add(idFinance_CHINA);
		mIdIcon.add(idFinance_EURO);
		mIdIcon.add(idFinance_OTHER);
		
		mIdIcon.add(idWebservice_USA);
		mIdIcon.add(idWebservice_JAPAN);
		mIdIcon.add(idWebservice_CHINA);
		mIdIcon.add(idWebservice_EURO);
		mIdIcon.add(idWebservice_OTHER);
		
		mIdIcon.add(idEcomerce_USA);
		mIdIcon.add(idEcomerce_JAPAN);
		mIdIcon.add(idEcomerce_CHINA);
		mIdIcon.add(idEcomerce_EURO);
		mIdIcon.add(idEcomerce_OTHER);
		
		mIdIcon.add(idCarrier_USA);
		mIdIcon.add(idCarrier_JAPAN);
		mIdIcon.add(idCarrier_CHINA);
		mIdIcon.add(idCarrier_EURO);
		mIdIcon.add(idCarrier_OTHER);
		
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
					mBtnControl.setLayoutParams(new LinearLayout.LayoutParams(
							30, 30));
					((LinearLayout.LayoutParams) mBtnControl.getLayoutParams()).leftMargin = 10;
					((LinearLayout.LayoutParams) mBtnControl.getLayoutParams()).gravity = Gravity.CENTER;
					mBtnControl.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.btn_add));
					mLinearTitle.addView(mBtnControl);

					TextView mTextView = new TextView(this);
					mTextView.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT));
					((LinearLayout.LayoutParams) mTextView.getLayoutParams()).leftMargin = 10;
					((LinearLayout.LayoutParams) mTextView.getLayoutParams()).topMargin = 5;
					mTextView.setTextColor(Color.WHITE);
					mTextView.setTypeface(null, Typeface.BOLD);
					mTextView.setText(getResources().getString(
							mCatalogueName[i]));
					mLinearTitle.addView(mTextView);
				
				final LinearLayout mLinearCountry = new LinearLayout(this);
				mLinearCountry.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
				mLinearCountry.setOrientation(LinearLayout.VERTICAL);
				{
					for (int m = 0; m < mCatalogueName.length; m++) {
						final int catalogue = i*mCountryName.length+m;;
						LinearLayout mLinearCountryTitle = new LinearLayout(
								this);
//						mLinearCountryTitle.setAlpha(0.5f);
						mLinearCountryTitle
								.setLayoutParams(new LinearLayout.LayoutParams(
										LinearLayout.LayoutParams.FILL_PARENT,
										46));
						mLinearCountryTitle
								.setBackgroundDrawable(getResources()
										.getDrawable(R.drawable.list_bar));
//						((LinearLayout.LayoutParams) mLinearCountryTitle
//								.getLayoutParams()).leftMargin = 10;
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
							mBtnControlCountry
									.setBackgroundDrawable(getResources()
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
						int l=i*mCountryName.length+m;
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
						mBtnControlCountry.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if(mTableLayout.getVisibility()==View.VISIBLE){
									mTableLayout.setVisibility(View.GONE);
									mBtnControlCountry.setBackgroundDrawable(ListIconActivity.this.getResources().getDrawable(R.drawable.btn_add));
								}else if(mTableLayout.getVisibility()==View.GONE){
									mTableLayout.setVisibility(View.VISIBLE);
									mBtnControlCountry.setBackgroundDrawable(ListIconActivity.this.getResources().getDrawable(R.drawable.btn_sub));
								}
							}
						});
					}
					
					mLinearCountry.setVisibility(View.GONE);
					mBtnControl.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(mLinearCountry.getVisibility()==View.VISIBLE){
								mLinearCountry.setVisibility(View.GONE);
								mBtnControl.setBackgroundDrawable(ListIconActivity.this.getResources().getDrawable(R.drawable.btn_add));
							}else if(mLinearCountry.getVisibility()==View.GONE){
								mLinearCountry.setVisibility(View.VISIBLE);
								mBtnControl.setBackgroundDrawable(ListIconActivity.this.getResources().getDrawable(R.drawable.btn_sub));
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
