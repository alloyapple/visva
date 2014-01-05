package vn.com.shoppie.activity;

import vn.com.shoppie.R;
import vn.com.shoppie.util.SUtilXml;
import vn.com.shoppie.view.LayoutTop;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ActivitySendMessage extends VisvaAbstractActivity {
	LayoutTop lyTop;
	EditText edtName;
	EditText edtEmail;
	EditText edtPhone;
	EditText edtContent;

	String name;
	String email;
	String phone;
	String message="";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_send_message);

		findViewById();

		if (SettingPreference.getUserName(this).length() > 0) {
			name = SettingPreference.getUserName(this);
			edtName.setText(name);
			edtName.setInputType(InputType.TYPE_NULL);

		}
		if (SettingPreference.getUserEmail(this).length() > 0) {
			email = SettingPreference.getUserEmail(this);
			edtEmail.setText(email);
			edtEmail.setInputType(InputType.TYPE_NULL);
		}
		
		if (SettingPreference.getUserPhone(this).length() > 0) {
			phone = SettingPreference.getUserPhone(this);
			edtPhone.setText(phone);
			edtPhone.setInputType(InputType.TYPE_NULL);
		}
		
		
	}

	public void findViewById() {
		RelativeLayout parent = (RelativeLayout) findViewById(R.id.layout_top);
		lyTop = new LayoutTop(this, parent);
		lyTop.mBtnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnClicked(v);
			}
		});
		lyTop.mTvTitle.setVisibility(View.INVISIBLE);
		lyTop.mBtnRight.setVisibility(View.INVISIBLE);

		edtName = (EditText) findViewById(R.id.edt_name);
		edtEmail = (EditText) findViewById(R.id.edt_email);
		edtPhone = (EditText) findViewById(R.id.edt_phone);
		edtContent = (EditText) findViewById(R.id.edt_content);
		
		lyTop.setText("PHẢN HỒI");
	}

	public void btnClicked(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			message=((EditText)findViewById(R.id.edt_content)).getText().toString();
			if(message==null || message.equals("")){
				Toast.makeText(this, "Message can not be empty!", Toast.LENGTH_SHORT).show();
				return;
			}
			if(SUtilXml.getInstance().sendFeedBack(this, message)){
				Toast.makeText(this, "Post feedback success!", Toast.LENGTH_SHORT).show();
				this.finish();
			}else{
				Toast.makeText(this, "Post feedback failed!", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_discard:
			new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Discard Msg").setMessage("Message would be discarded?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					edtContent.setText("");
					ActivitySendMessage.this.finish();
				}

			}).setNegativeButton("No", null).show();

			break;
		case R.id.layout_top_btn_left:
			this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	public int contentView() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}
}
