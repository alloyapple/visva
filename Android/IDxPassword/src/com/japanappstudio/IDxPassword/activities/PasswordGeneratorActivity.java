package com.japanappstudio.IDxPassword.activities;

import java.util.ArrayList;
import java.util.Random;

import org.apache.poi.hssf.record.PageBreakRecord.Break;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.japanappstudio.IDxPassword.contants.Contants;
import com.japanappstudio.IDxPassword.database.IdManagerPreference;

import com.japanappstudio.IDxPassword.activities.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.InputFilter.LengthFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordGeneratorActivity extends BaseActivity {
	private int number = 6;
	private boolean isNumber = true;
	private boolean isCapital = true;
	private boolean isLowerCase = true;
	private boolean isSign = true;
	private boolean isDuplicate = true;
	private ArrayList<Character> mTotalArrayChars = new ArrayList<Character>();
	// private CharSequence[] mNumberChoiceArr = { "4", "5", "6", "7", "8", "9",
	// "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
	// "21" };
	private Character mNuberChars[] = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9' };
	private Character mCapitalChars[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z' };
	private Character mLowerCaseChars[] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z' };
	private Character mSignChars[] = { '@', '#', '$', '%', '&', '*', '(', ')',
			'{', '}', ',', ';', '?', '|', '.', '/', '\\', '[', ']', '+', '-',
			'>', '<', '~', '!', '^', '`', '\'', '\"', ':', '=', '_' };
	private StringBuffer resultGenarator = new StringBuffer();
	private Random random = new Random();
	private AdView adview;
	private IdManagerPreference mIdManagerPreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_generator);
		((TextView) findViewById(R.id.id_text_num_chracter)).setText("" + 6);
		mIdManagerPreference = IdManagerPreference.getInstance(this);
		initAdmod();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			EditIdPasswordActivity.startActivity(this, 2);
			finish();
			return false;

		default:
			return super.onKeyDown(keyCode, event);
		}

	}

	public void initAdmod() {
		adview = (AdView) findViewById(R.id.main_adView);
		AdRequest re = new AdRequest();
		if (adview != null) {
			adview.loadAd(re);
			if (!mIdManagerPreference.getIsPaymentNoAd())
				adview.setVisibility(View.VISIBLE);
			else
				adview.setVisibility(View.GONE);
		}
	}

	public void onReturn(View v) {
		if (resultGenarator.toString() != "")
			EditIdPasswordActivity.mStringOfSelectItem = resultGenarator
					.toString();
		else
			EditIdPasswordActivity.itemSelect = -1;
		EditIdPasswordActivity.startActivity(this, 2);
		finish();
	}

	public void onGenerate(View v) {
		generator();
	}

	public void onNumberCharacter(View v) {
		showInputNumberCharacter();
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, PasswordGeneratorActivity.class);
		activity.startActivity(i);
	}

	public void showInputNumberCharacter() {
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.icon);
		// builder.setSingleChoiceItems(mNumberChoiceArr, 0,
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int item) {
		// // TODO Auto-generated method stub
		// int number = item + 4;
		// if (number > 21)
		// number = 21;
		// setTextNumberCharacter(number);
		// dialog.dismiss();
		// return;
		// }
		// });
		// builder.create().show();
		showSetBetDialog();
	}

	public void setTextNumberCharacter(int number) {
		if (number <= 0)
			return;
		((TextView) findViewById(R.id.id_text_num_chracter)).setText(""
				+ number);
		this.number = number;
	}

	public void generator() {
		resultGenarator.setLength(0);
		if (number <= 0)
			return;
		mTotalArrayChars.clear();
		isNumber = ((CheckBox) findViewById(R.id.id_checkbox_number))
				.isChecked();
		if (isNumber) {
			for (int i = 0; i < mNuberChars.length; i++) {
				mTotalArrayChars.add(mNuberChars[i]);
			}
		}

		isCapital = ((CheckBox) findViewById(R.id.id_checkbox_capital))
				.isChecked();
		if (isCapital) {
			for (int i = 0; i < mCapitalChars.length; i++) {
				mTotalArrayChars.add(mCapitalChars[i]);
			}
		}

		isLowerCase = ((CheckBox) findViewById(R.id.id_checkbox_lower_case))
				.isChecked();
		if (isLowerCase) {
			for (int i = 0; i < mLowerCaseChars.length; i++) {
				mTotalArrayChars.add(mLowerCaseChars[i]);
			}
		}
		isSign = ((CheckBox) findViewById(R.id.id_checkbox_sign)).isChecked();
		if (isSign) {
			for (int i = 0; i < mSignChars.length; i++) {
				mTotalArrayChars.add(mSignChars[i]);
			}
		}
		if (mTotalArrayChars.size() == 0)
			return;
		isDuplicate = ((CheckBox) findViewById(R.id.id_checkbox_duplicate))
				.isChecked();
		if (!isDuplicate) {
			for (int i = 0; i < number; i++) {
				int ranChar = random.nextInt(mTotalArrayChars.size());
				resultGenarator.append(mTotalArrayChars.get(ranChar));
			}
			((TextView) findViewById(R.id.id_text_result_generator))
					.setText(resultGenarator.toString());
		} else {
			for (int i = 0; i < number; i++) {
				int ranChar = random.nextInt(mTotalArrayChars.size());
				resultGenarator.append(mTotalArrayChars.get(ranChar));
				mTotalArrayChars.remove(ranChar);
				if (mTotalArrayChars.size() <= 0)
					break;
			}
			((TextView) findViewById(R.id.id_text_result_generator))
					.setText(resultGenarator.toString());
		}
	}

	public void showSetBetDialog() {
		AlertDialog.Builder builder = new Builder(this);
		LayoutInflater infalInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		FrameLayout layout = (FrameLayout) infalInflater.inflate(R.layout.bet,
				null);

		final AutoCompleteTextView input = (AutoCompleteTextView) layout
				.findViewById(R.id.tv_bet_input);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);

		LengthFilter filter = new LengthFilter(5);
		input.setFilters(new InputFilter[] { filter });
		input.setSelectAllOnFocus(true);
		input.setTextColor(Color.BLACK);
		input.setText("" + this.number);

		String[] betSuggestion = getResources().getStringArray(
				R.array.bet_suggestion);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.item_list_s, betSuggestion);
		adapter.setNotifyOnChange(true);
		input.setAdapter(adapter);
		input.setThreshold(1);
		input.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (input.isFocused()) {
					input.showDropDown();
				}
			}
		});

		input.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (input.isFocused()) {
					input.showDropDown();
				}
			}
		});

		builder.setView(layout);
		builder.setPositiveButton(
				getResources().getString(R.string.confirm_ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						int number = PasswordGeneratorActivity.this.number;
						try {
							number = Integer.parseInt(input.getText()
									.toString());
						} catch (Exception e) {
							// TODO: handle exception
						}
						if (number < 4)
							number = 4;
						else if (number > 21)
							number = 21;
						setTextNumberCharacter(number);
					}
				});
		builder.setNegativeButton(
				getResources().getString(R.string.confirm_cancel), null);
		builder.setIcon(R.drawable.ic_launcher);
		builder.create().show();
	}
}