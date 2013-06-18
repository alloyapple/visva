package com.lemon.fromangle;

import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class DialogDateTimePicker extends Dialog {

	private int minutes, hours;
	private Date date;
//	private Context context;
	DateTimeDialogListerner listener;

	private TimePicker timePicker1;
	private DatePicker datePicker1;
	private Button btnCancel;
	private Button btnOk;

	public interface DateTimeDialogListerner {

		void onSelectDateTime(Date date, int hour, int minute);

	}

	public DialogDateTimePicker(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public DialogDateTimePicker(Context context, Date date, int hour,
			int minute, DateTimeDialogListerner listener) {
		super(context);
		this.listener = listener;
		this.date = date;
		this.minutes = minute;
		this.hours = hour;
//		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_datetime_picker);
		initUI();

	}

	@SuppressWarnings("deprecation")
	private void initUI() {
		timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
		datePicker1 = (DatePicker) findViewById(R.id.datePicker1);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnOk = (Button) findViewById(R.id.btnOk);
		timePicker1.setCurrentHour(hours);
		timePicker1.setCurrentMinute(minutes);
		datePicker1.updateDate(date.getYear() + 1900, date.getMonth(),
				date.getDate());
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogDateTimePicker.this.dismiss();
			}
		});
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogDateTimePicker.this.dismiss();
				Date d = new Date(datePicker1.getYear() - 1900, datePicker1
						.getMonth(), datePicker1.getDayOfMonth());
				listener.onSelectDateTime(d, timePicker1.getCurrentHour(),
						timePicker1.getCurrentMinute());

			}
		});

	}
}
