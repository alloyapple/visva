package visvateam.outsource.idmanager.activities;

import java.util.ArrayList;
import java.util.Random;

import visvateam.outsource.idmanager.contants.Contants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class PasswordGeneratorActivity extends Activity {
	private int number = 6;
	private boolean isNumber = true;
	private boolean isCapital = true;
	private boolean isLowerCase = true;
	private boolean isSign = true;
	private boolean isDuplicate = true;
	private ArrayList<Character> mTotalArrayChars = new ArrayList<Character>();
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_generator);
		((TextView) findViewById(R.id.id_text_num_chracter)).setText("" + 6);

	}

	public void onReturn(View v) {
		if (resultGenarator.toString() != "")
			EditIdPasswordActivity.mStringOfSelectItem = resultGenarator
					.toString();
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
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Input");
		builder.setMessage("Enter number of characters:");
		builder.setIcon(R.drawable.icon);

		// Use an EditText view to get user input.
		final EditText inputkeyCode = new EditText(this);
		inputkeyCode.setId(Contants.TEXT_ID);
		inputkeyCode.setInputType(InputType.TYPE_CLASS_NUMBER);
		builder.setView(inputkeyCode);
		inputkeyCode.setText("");
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				String numberCharacters = inputkeyCode.getText().toString();
				int number;
				try {
					number = Integer.parseInt(numberCharacters);
				} catch (Exception e) {
					// TODO: handle exception
					number = -1;
				}
				setTextNumberCharacter(number);
				return;
			}
		});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		builder.create().show();
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
				if(mTotalArrayChars.size()<=0)
					break;
			}
			((TextView) findViewById(R.id.id_text_result_generator))
					.setText(resultGenarator.toString());
		}
	}

}