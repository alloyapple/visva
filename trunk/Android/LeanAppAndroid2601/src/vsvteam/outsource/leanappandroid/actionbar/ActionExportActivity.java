package vsvteam.outsource.leanappandroid.actionbar;

import java.io.File;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.itextpdf.text.Document;

import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.database.LeanAppAndroidSharePreference;
import vsvteam.outsource.leanappandroid.exportcontrol.ExportExcel;
import vsvteam.outsource.leanappandroid.exportcontrol.SendBoxController;
import vsvteam.outsource.leanappandroid.exportcontrol.SendEmailController;
import vsvteam.outsource.leanappandroid.exportcontrol.excelcreator.ExcelDocumentController;
import vsvteam.outsource.leanappandroid.exportcontrol.pdfcreator.PDFDocumentController;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class ActionExportActivity extends Activity implements OnClickListener {

	// =============================Control Define ==========================
	private ImageView btnExport;
	private ImageView btnSetting;
	private ImageView btnVersion;
	private ImageView btnChangeProject;
	// =============================Class Define ============================
	private SendBoxController mBox;
	private SendEmailController mEmail;
	private ExcelDocumentController mExcel;
	private PDFDocumentController mPdf;
	//
	DropboxAPI<AndroidAuthSession> mApi;

	// =============================Constant Define =========================
	private static final byte DARK = 1;
	private static final byte BRIGHT = 2;
	public static final String PATH_FOLDER = "/LeanApp/";
	public static final String PATH_FOLDER_DROPBOX = "/Photos/";
	public static final int FORMAT_PDF = 0;
	public static final int FORMAT_EXCEL = 1;
	public static final int FORMAT_VIDEO = 2;

	public static final int SEND_EMAIL = 0;
	// public static final int SEND_ICLOUD = 1;
	public static final int SEND_DROP_BOX = 1;
	public static final int SEND_BOX = 2;
	public static final int SEND_GOOGLE_DRIVE = 3;
  
	public static final int ELEMENT_ALL = 0;
	public static final int ELEMENT_STREAM_MAP = 1;
	public static final int ELEMENT_CYCLE_TIME = 2;
	public static final int ELEMENT_PQPR = 3;
	public static final int ELEMENT_CHART = 4;
	public static final int ELEMENT_FOCUS_IMPROVEMENTS = 5;

	public static final String FILE_NAME_EXCEL = "test.xls";
	private static final String FILE_NAME_PDF = "test";
	public static final String PATH_FOLDER_DOCUMENT = Environment.getExternalStorageDirectory()
			.getPath() + "/LeanApp/Documents/";
	// ============================Variable Define ==========================

	private int idCheckBoxElement[] = { R.id.id_checkbox_element_all,
			R.id.id_checkbox_element_stream_map, R.id.id_checkbox_element_cycle_time,
			R.id.id_checkbox_element_pqpr, R.id.id_checkbox_element_chart,
			R.id.id_checkbox_element_focus_improvements };
	private int idCheckBoxExportTo[] = { R.id.id_checkbox_email, R.id.id_checkbox_dropbox,
			R.id.id_checkbox_box, R.id.id_checkbox_google_driver };
	private int idCheckBoxFormat[] = { R.id.id_checkbox_pdf, R.id.id_checkbox_excel };

	private int mFormat;
	private int mElement;
	private int mTypeExport;
	private boolean mIsIncludeVideo;

	private String pathFile = "";
	private boolean isSendPdfFile = true;
	private OnCheckedChangeListener mListenerElements = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				for (int j = 0; j < 6; j++) {
					if (buttonView.getId() == idCheckBoxElement[j]) {
						((CheckBox) findViewById(idCheckBoxElement[j])).setChecked(true);
					} else {
						((CheckBox) findViewById(idCheckBoxElement[j])).setChecked(false);
					}
				}
			}
		}
	};
	private OnCheckedChangeListener mListenerExportTo = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				for (int j = 0; j < 4; j++) {
					if (buttonView.getId() == idCheckBoxExportTo[j]) {
						((CheckBox) findViewById(idCheckBoxExportTo[j])).setChecked(true);
					} else {
						((CheckBox) findViewById(idCheckBoxExportTo[j])).setChecked(false);
					}
				}
			}
		}
	};
	private OnCheckedChangeListener mListenerFormat = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				for (int j = 0; j < 2; j++) {
					if (buttonView.getId() == idCheckBoxFormat[j]) {
						((CheckBox) findViewById(idCheckBoxFormat[j])).setChecked(true);
					} else {
						((CheckBox) findViewById(idCheckBoxFormat[j])).setChecked(false);
					}
				}
			}
		}
	};
	public Handler mHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* save instancestate */

		setContentView(R.layout.page_action_export);

		/* initialize control */
		initControl();

		mEmail = new SendEmailController(getParent());
		mBox = new SendBoxController(this, getParent());
		mExcel = new ExcelDocumentController(getParent());
		mPdf = new PDFDocumentController(getParent());
	}

	private void initControl() {
		visibleView((FrameLayout) findViewById(R.id.id_frame_parent_export),
				(LinearLayout) findViewById(R.id.id_linear_export), View.VISIBLE);
		for (int i = 0; i < 6; i++) {
			((CheckBox) findViewById(idCheckBoxElement[i]))
					.setOnCheckedChangeListener(mListenerElements);
		}
		for (int i = 0; i < 4; i++) {
			((CheckBox) findViewById(idCheckBoxExportTo[i]))
					.setOnCheckedChangeListener(mListenerExportTo);
		}
		for (int i = 0; i < 2; i++) {
			((CheckBox) findViewById(idCheckBoxFormat[i]))
					.setOnCheckedChangeListener(mListenerFormat);
		}
		btnExport = (ImageView) findViewById(R.id.img_choice_project_export);
		btnExport.setOnClickListener(this);
		//
		btnSetting = (ImageView) findViewById(R.id.img_choice_project_setting);
		btnSetting.setOnClickListener(this);
		//
		btnVersion = (ImageView) findViewById(R.id.img_choice_project_version);
		btnVersion.setOnClickListener(this);
		//
		btnChangeProject = (ImageView) findViewById(R.id.img_choice_project_change_project);
		btnChangeProject.setOnClickListener(this);
		((Button) findViewById(R.id.id_btn_export)).setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int typeExport = LeanAppAndroidSharePreference.getInstance(this).getModeExport(
				LeanAppAndroidSharePreference.TYPE_EXPORT);
		if (typeExport < 0)
			return;
		else {
			switch (typeExport) {
			case SEND_DROP_BOX:
				// mDropBox.sendFile();
				(LeanAppAndroidSharePreference.getInstance(this)).setModeExport(
						LeanAppAndroidSharePreference.TYPE_EXPORT, -1);
				break;

			default:
				break;
			}
		}
	}

	public void setTheme(View v, byte type) {
		AlphaAnimation alpha;
		if (type == DARK)
			alpha = new AlphaAnimation(0.3F, 0.3F);
		else
			alpha = new AlphaAnimation(1.0F, 1.0F);
		alpha.setDuration(0); // Make animation instant
		alpha.setFillAfter(true); // Tell it to persist after the animation ends
		// And then on your layout
		v.startAnimation(alpha);

	}

	public void visibleView(View parent, View v, int visible) {
		if (visible == View.VISIBLE) {
			setTheme(parent, DARK);
			v.setVisibility(View.VISIBLE);
			ScaleAnimation scale = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0f);
			scale.setDuration(500);
			v.startAnimation(scale);
		} else {
			ScaleAnimation scale = new ScaleAnimation(1, 1, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 1f);
			scale.setDuration(300);
			v.startAnimation(scale);
			v.setVisibility(View.GONE);
			setTheme(parent, BRIGHT);
		}
	}

	public int getTypeExport() {
		for (int i = 0; i < 4; i++) {
			if (((CheckBox) findViewById(idCheckBoxExportTo[i])).isChecked()) {
				return i;
			}
		}
		return 0;
	}

	public int getTypeFormat() {
		for (int i = 0; i < 2; i++) {
			if (((CheckBox) findViewById(idCheckBoxFormat[i])).isChecked()) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.id_btn_export:
			export();
			break;

		default:
			break;
		}
	}

	private void export() {
		if (!isExternalStorageAvailable()) {
			showToast("Sdcard is unvailable.Check it again");
		} else {
			mFormat = getTypeFormat();
			String fileName = null ;
			switch (mFormat) {
			case FORMAT_PDF:
				fileName = saveFileToPDF();
				isSendPdfFile = true;
				// createPdfFile(FILE_NAME_PDF);
				break;
			case FORMAT_EXCEL:
				fileName = saveFileToExcel();
				isSendPdfFile = false;
				// createExcelFile(FILE_NAME_EXCEL);
				break;
			default:
				break;
			}
			if(!"".equals(fileName))
				sendFile(fileName);
		}
	}

	private void sendFile(String fileName) {
		mTypeExport = getTypeExport();
		switch (mTypeExport) {
		case SEND_EMAIL:
			mEmail.upload(pathFile, mTypeExport, false, null);
			break;
		case SEND_DROP_BOX:
			/* export file to dropbox */
			Intent intentDropBox = new Intent(ActionExportActivity.this, DropBoxSyncActivity.class);
			intentDropBox.putExtra("isSendFileFormat", isSendPdfFile);
			startActivity(intentDropBox);
			break;
		case SEND_BOX:
			mBox.upload(null);
			break;
		case SEND_GOOGLE_DRIVE:
			break;
		default:
			break;
		}
		if (mIsIncludeVideo) {
			switch (mTypeExport) {
			case SEND_EMAIL:
				break;
			case SEND_DROP_BOX:
				break;
			case SEND_BOX:
				break;
			case SEND_GOOGLE_DRIVE:
				break;
			default:
				break;
			}
		}
	}

	private String saveFileToPDF() {
		String fileName = FILE_NAME_PDF;
		pathFile = PATH_FOLDER + FILE_NAME_PDF;
		Document dcm = mPdf.creatPDFDocument(PATH_FOLDER_DOCUMENT, FILE_NAME_PDF);

		switch (mElement) {
		case ELEMENT_ALL:
			break;
		case ELEMENT_STREAM_MAP:
			break;
		case ELEMENT_CYCLE_TIME:
			break;
		case ELEMENT_PQPR:
			break;
		case ELEMENT_CHART:
			break;
		case ELEMENT_FOCUS_IMPROVEMENTS:
			break;
		default:
			break;
		}
		return fileName;
	}

	public String saveFileToExcel() {
		String fileName = FILE_NAME_EXCEL;
		pathFile = PATH_FOLDER + FILE_NAME_EXCEL;
		File file = mExcel.saveExcelFile( FILE_NAME_EXCEL);
		switch (mElement) {
		case ELEMENT_ALL:
			break;
		case ELEMENT_STREAM_MAP:
			break;
		case ELEMENT_CYCLE_TIME:
			break;
		case ELEMENT_PQPR:
			break;
		case ELEMENT_CHART:
			break;
		case ELEMENT_FOCUS_IMPROVEMENTS:
			break;
		default:
			break;
		}
		return fileName;
	}

	/**
	 * check sdcard is read only
	 * 
	 * @return
	 */
	public boolean isExternalStorageReadOnly() {
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
			return true;
		}
		return false;
	}

	/**
	 * check sdcard is available or not
	 * 
	 * @return
	 */
	public boolean isExternalStorageAvailable() {
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
			return true;
		}
		return false;
	}

	private void showToast(String msg) {
		Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		error.show();
	}
}
