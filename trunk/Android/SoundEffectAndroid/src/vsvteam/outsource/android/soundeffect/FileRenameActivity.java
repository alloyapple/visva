package vsvteam.outsource.android.soundeffect;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FileRenameActivity extends Activity implements OnClickListener {
	private EditText editText;
	private Button btnRename;
	private String filePath;
	private String fileNameFrom;
	private File fileFrom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_rename);
		filePath = getIntent().getExtras().getString("fileRenamePath");
		fileFrom = new File(filePath);
		fileNameFrom = fileFrom.getName().substring(0, (fileFrom.getName().length() - 4));
		editText = (EditText) findViewById(R.id.editText_rename);
		editText.setText(fileNameFrom);
		btnRename = (Button) findViewById(R.id.btn_rename);
		btnRename.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btnRename) {
			if (!"".equals(editText.getText())) {
				renameAudioFile(filePath, editText.getText().toString());
			} else {

			}
			finish();
		}
	}

	/**
	 * rename audio file at position to fileName
	 * 
	 * @param position
	 * @param fileName
	 */
	private void renameAudioFile(String fileNameFrom, String fileNameTo) {
		File dir = new File(fileNameFrom);

		if (dir.exists()) {
			File from = new File(fileNameFrom);
			File to = new File(dir.getParentFile()+"/" + fileNameTo +".mp3");
			if (from.exists())
				from.renameTo(to);
		}
	}

}
