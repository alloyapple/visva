package com.visva.voicerecorder.view.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ListView;

import com.visva.voicerecorder.MainActivity;
import com.visva.voicerecorder.record.RecordingListView;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.view.adapter.RecordingAdapter;

/**
 * A fragment that has two state:
 * - The first one: a calendar to choose the day of recording
 * - The second one: a list to show all the recording session of a day
 */
public class ArrangeByTimeRecordingFragment extends Fragment {
	public static int id;
	public ListView listView;
	public int state = 0;
	public long lastDate = -1;
	
	public void onStart(){
		super.onStart();
	}
	
	public void handleBackButtonClick(){
		ViewGroup vg = (ViewGroup)getView();
		vg.removeAllViews();
		if(!(vg.getChildAt(0) instanceof CalendarView)){
			vg.addView(this._createDefaultView());
		}	
	}
	
	private View _createDefaultView(){
		// display a list of phone number
		CalendarView calendarView = new CalendarView(this.getActivity());
		state = 0;
		if(lastDate != -1){
			calendarView.setDate(lastDate);
		}
		calendarView.setOnDateChangeListener(new OnDateChangeListener(){
			public void onSelectedDayChange(CalendarView calendar, int year,
					int month, int date) {
				month++; // start from 0
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				try{
					Date oldDate = formatter.parse(date+"-"+month+"-"+year);
					lastDate = oldDate.getTime();
				}catch(Exception e){
					e.printStackTrace();
				}
//				ArrayList<RecordingSession> sessions = 
//						MainActivity.recordingManager.getByDate(date, month, year);
//				if(sessions.size() == 0){
//					AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
//					dialog.setTitle("Thông báo");
//					dialog.setMessage("Không có cuộc ghi âm nào trong ngày "+date+"/"+month+"/"+year);
//					dialog.setCancelable(true);
//					dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
//					    public void onClick(DialogInterface dialog, int buttonId) {
//					        dialog.cancel();
//					    }
//					});
//					dialog.show();
//					return;
//				}
				ViewGroup vg = (ViewGroup)getView();
				vg.removeAllViews();
				// show the list !
				listView = new RecordingListView(getActivity());
//	    		RecordingAdapter adapter = new RecordingAdapter(getActivity(), 
//	    				android.R.layout.simple_list_item_activated_1 , 
//	    				sessions);
//	    		listView.setAdapter(adapter);
	    		vg.addView(listView);
	    		state = 1;
			}
		});
        return calendarView;
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		Log.d("GHIAM", "attach allrecording");
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		return this._createDefaultView();
    }
}
