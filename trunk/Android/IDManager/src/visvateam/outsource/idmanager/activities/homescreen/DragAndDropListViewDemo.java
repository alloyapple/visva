/*
 *Copyright 2011 Matthieu Paret
 *
 *This file is part of DragAndDrop.
 *
 *DragAndDrop is free software: you can redistribute it and/or modify
 *it under the terms of the GNU Lesser General Public License as published by
 *the Free Software Foundation, either version 3 of the License, or
 *(at your option) any later version.
 *
 *DragAndDrop is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU Lesser General Public License
 *along with DragAndDrop.  If not, see <http://www.gnu.org/licenses/>.
 */

package visvateam.outsource.idmanager.activities.homescreen;

import java.util.ArrayList;

import visvateam.outsource.idmanager.activities.R;
import visvateam.outsource.idmanager.activities.SettingActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import exp.mtparet.dragdrop.adapter.ItemAdapter;
import exp.mtparet.dragdrop.adapter.ReceverAdapter;
import exp.mtparet.dragdrop.data.OneItem;
import exp.mtparet.dragdrop.data.ReceiverOneItem;
import exp.mtparet.dragdrop.view.ListViewDragDrop;

public class DragAndDropListViewDemo extends Activity implements OnClickListener {

	private LinearLayout mainRelativeLayout;
	private ListViewDragDrop lvPicture;
	private ListViewDragDrop lvRecever;
	private ItemAdapter itemAdapter;
	private RelativeLayout layoutDrag;
	private ReceverAdapter receverAdapter;
	private ImageView imageDrag;
	private OneItem oneItemSelected;
	private Context context;

	private ListView folderListView;
	private Button btnSetting;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;

		mainRelativeLayout = (LinearLayout) LinearLayout.inflate(context, R.layout.page_home_demo,
				null);
		lvPicture = (ListViewDragDrop) mainRelativeLayout.findViewById(R.id.list_view_item);
		lvRecever = (ListViewDragDrop) mainRelativeLayout.findViewById(R.id.list_view_folder);

		folderListView = (ListView) findViewById(R.id.list_view_folder);
		imageDrag = (ImageView) mainRelativeLayout.findViewById(R.id.imageView1);
		layoutDrag = (RelativeLayout) mainRelativeLayout.findViewById(R.id.layoutDrag);

		itemAdapter = new ItemAdapter(context, constructList());
		receverAdapter = new ReceverAdapter(context, new ArrayList<OneItem>());

		lvPicture.setAdapter(itemAdapter);
		lvRecever.setAdapter(receverAdapter);

		/**
		 * Set selected Listener to know what item must be drag
		 */
		lvPicture.setOnItemSelectedListener(mOnItemSelectedListener);

		/**
		 * Set an touch listener to know what is the position when item are move
		 * out of the listview
		 */
		lvPicture.setOnItemMoveListener(mOnItemMoveListener);

		/**
		 * Listener to know if the item is droped out of this origin ListView
		 */
		lvPicture.setOnItemUpOutListener(mOnItemUpOutListener);

		/**
		 * Listener to know on what position the new item must be insert
		 */
		lvRecever.setOnItemReceiverListener(listenerReceivePicture);

		/**
		 * An listemer to remove an item
		 */
		lvRecever.setOnItemLongClickListener(listenerRemoveItem);

		setContentView(mainRelativeLayout);

		btnSetting = (Button) findViewById(R.id.btn_setting);
		btnSetting.setOnClickListener(this);
	}

	/**
	 * Save selected item
	 */
	private AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

			/**
			 * retrieve selected item from adapterview
			 */
			oneItemSelected = (OneItem) arg0.getItemAtPosition(arg2);
			Log.e("one item info", "infro " + oneItemSelected.getName());
			imageDrag.setImageDrawable(context.getResources().getDrawable(oneItemSelected.getId()));
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	};

	private OnTouchListener mOnItemUpOutListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			return lvRecever.onUpReceive(event);
		}
	};

	private OnItemClickListener listenerReceivePicture = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (oneItemSelected != null)
				receverAdapter.addPicture(oneItemSelected, arg2);
		}

	};

	private OnItemLongClickListener listenerRemoveItem = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			receverAdapter.removeItem(arg2);
			return false;
		}

	};

	private OnTouchListener mOnItemMoveListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) imageDrag
					.getLayoutParams();
			imageDrag.setVisibility(ImageView.VISIBLE);

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				imageDrag.bringToFront();
				return true;
			}

			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				layout.leftMargin = (int) event.getX() + imageDrag.getWidth() / 2;
				layout.topMargin = (int) event.getY() - imageDrag.getHeight() / 2;
			}

			if (event.getAction() == MotionEvent.ACTION_UP) {
				imageDrag.setVisibility(View.GONE);
			}
			// set params
			imageDrag.setLayoutParams(layout);

			return true;
		}

	};

	private ArrayList<OneItem> constructList() {
		ArrayList<OneItem> al = new ArrayList<OneItem>();

		OneItem op = new OneItem(R.drawable.unionpay2, "unionpay2");
		al.add(op);

		OneItem op2 = new OneItem(R.drawable.bank_of_china, "bank_of_china");
		al.add(op2);

		OneItem op3 = new OneItem(R.drawable.bank_of_shanghai, "bank_of_shanghai");
		al.add(op3);

		OneItem op4 = new OneItem(R.drawable.china_construction_bank_corporation,
				"china_construction_bank_corporation");
		al.add(op4);

		OneItem op5 = new OneItem(R.drawable.agricultural_bank_of_china,
				"agricultural_bank_of_china");
		al.add(op5);

		OneItem op6 = new OneItem(R.drawable.china_construction_bank_corporation,
				"china_construction_bank_corporation");
		al.add(op6);

		OneItem op7 = new OneItem(R.drawable.bank_of_shanghai, "bank_of_shanghai");
		al.add(op7);
		return al;
	}

	private ArrayList<ReceiverOneItem> constructListFolder() {
		// ArrayList<OneItem> al = new ArrayList<OneItem>();
		//
		// OneItem op = new OneItem(R.drawable.unionpay2, "unionpay2");
		// al.add(op);
		//
		// OneItem op2 = new OneItem(R.drawable.bank_of_china, "bank_of_china");
		// al.add(op2);
		//
		// OneItem op3 = new OneItem(R.drawable.bank_of_shanghai,
		// "bank_of_shanghai");
		// al.add(op3);
		//
		// OneItem op4 = new OneItem(
		// R.drawable.china_construction_bank_corporation,
		// "china_construction_bank_corporation");
		// al.add(op4);
		//
		// OneItem op5 = new OneItem(R.drawable.agricultural_bank_of_china,
		// "agricultural_bank_of_china");
		// al.add(op5);
		//
		// OneItem op6 = new
		// OneItem(R.drawable.china_construction_bank_corporation,
		// "china_construction_bank_corporation");
		// al.add(op6);
		//
		// OneItem op7 = new OneItem(R.drawable.bank_of_shanghai,
		// "bank_of_shanghai");
		// al.add(op7);
		// return al;
		ArrayList<ReceiverOneItem> receiverOneItem = new ArrayList<ReceiverOneItem>();
		// ReceiverOneItem op = new ReceiverOneItem(idImgFolder,
		// idImgFolderIcon, idImgEdit, name);
		return receiverOneItem;
	}

	@Override
	public void onClick(View v) {
		if (v == btnSetting) {
			Intent intentSetting = new Intent(DragAndDropListViewDemo.this, SettingActivity.class);
			startActivity(intentSetting);
		}
	}
}
