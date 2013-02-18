package visvateam.outsource.idmanager.activities;
import visvateam.outsource.idmanager.activities.homescreen.DragAndDropListViewDemo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MasterPasswordActivity extends Activity implements OnClickListener{

	private Button btnDone;
	private EditText editTextMasterPW;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.master_password);
		
		//
		btnDone = (Button)findViewById(R.id.btn_confirm_master_pw);
		btnDone.setOnClickListener(this);
		editTextMasterPW = (EditText)findViewById(R.id.editText_master_pw);
	}

	public void confirmMaster(View v) {
	}

	public void onReturn(View v) {

	}
	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, MasterPasswordActivity.class);
		activity.startActivity(i);
	}

	@Override
	public void onClick(View v) {
		if(v == btnDone){
			Intent intent = new Intent(MasterPasswordActivity.this, DragAndDropListViewDemo.class);
			startActivity(intent);
		}
		
	}
}
