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

package exp.mtparet.dragdrop.adapter;

import java.util.ArrayList;

import visvateam.outsource.idmanager.activities.EditIdPasswordActivity;
import visvateam.outsource.idmanager.activities.R;
import visvateam.outsource.idmanager.contants.Contants;
import exp.mtparet.dragdrop.data.OneItem;
import exp.mtparet.dragdrop.view.ListViewDragDrop;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter {

	private static final int DIALOG_DELETE_ID = 3;
	private static final int DIALOG_EDIT_ID = 4;

	private Context context;
	private ArrayList<OneItem> idItemList;
	private ListViewDragDrop idListView;

	private boolean isModeEdit;
	private Handler mHandler;
	private int currentFolderId;

	public ItemAdapter(Context context, ArrayList<OneItem> idItemList, boolean isModeEdit,
			Handler mHandler, ListViewDragDrop idListView, int currentFolderId) {
		this.context = context;
		this.idItemList = idItemList;
		this.isModeEdit = isModeEdit;
		this.mHandler = mHandler;
		this.idListView = idListView;
		this.currentFolderId = currentFolderId;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return idItemList.size();
	}

	@Override
	public OneItem getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.idItemList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {

		if (convertView == null) {
			convertView = (RelativeLayout) RelativeLayout.inflate(context, R.layout.list_id_row,
					null);
		}

		/* image logo */
		ImageView iv = (ImageView) convertView.findViewById(R.id.imageView1);
		iv.setImageDrawable(context.getResources()
				.getDrawable(idItemList.get(position).getIconId()));
		iv.setContentDescription(this.idItemList.get(position).getName());

		/* text name vs text url */
		TextView txtIdName = (TextView) convertView.findViewById(R.id.txt_id_item_name);
		txtIdName.setText(this.idItemList.get(position).getName());
		txtIdName.setSelected(true);
		TextView txtIdUrl = (TextView) convertView.findViewById(R.id.txt_id_item_url);
		txtIdUrl.setText(this.idItemList.get(position).getUrl());
		txtIdUrl.setSelected(true);

		/* btn edit */
		Button btnEdit = (Button) convertView.findViewById(R.id.btn_id_item_edit);
		btnEdit.setOnClickListener(mOnEditClickListener);
		/* button delete */
		Button btnDelete = (Button) convertView.findViewById(R.id.btn_id_item_delete);
		btnDelete.setOnClickListener(mOnDeleteClickListener);
		if (isModeEdit) {
			btnEdit.setVisibility(View.VISIBLE);
			btnDelete.setVisibility(View.VISIBLE);
		} else {
			btnEdit.setVisibility(View.GONE);
			btnDelete.setVisibility(View.GONE);
		}

		return convertView;
	}

	public void addPicture(OneItem onItem, int position) {

		if (position < idItemList.size()) {
			idItemList.add(position, onItem);
		} else {
			idItemList.add(onItem);
		}

		notifyDataSetChanged();
	}

	public void addPicture(OneItem onItem) {

		idItemList.add(onItem);

		notifyDataSetChanged();
	}

	public void removeItem(int position) {
		idItemList.remove(position);
		notifyDataSetChanged();
	}

	public void updateModeForListView(boolean isEdit) {
		this.isModeEdit = isEdit;
		notifyDataSetChanged();
	}

	private OnClickListener mOnDeleteClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.e("onClickDelete", "onClickDelete");
			final int position = idListView.getPositionForView((View) v.getParent());
			Log.e("clickc lcik ", "Title clicked, row %d" + position);
			Message msg = mHandler.obtainMessage();
			msg.arg1 = DIALOG_DELETE_ID;
			msg.arg2 = position;
			mHandler.sendMessage(msg);
		}
	};

	private OnClickListener mOnEditClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.e("OnclickEdit", "OnClickEdit");
			final int position = idListView.getPositionForView((View) v.getParent());
			Log.e("clickc lcik ", "Title clicked, row %d" + position);
			Message msg = mHandler.obtainMessage();
			msg.arg1 = DIALOG_EDIT_ID;
			msg.arg2 = position;
			mHandler.sendMessage(msg);
			Intent intent = new Intent(context, EditIdPasswordActivity.class);
			intent.putExtra(Contants.IS_INTENT_CREATE_NEW_ID, false);
			intent.putExtra(Contants.CURRENT_FOLDER_ID, currentFolderId);
			intent.putExtra(Contants.CURRENT_PASSWORD_ID, idItemList.get(position).getPasswordId());
			Log.e("currnet position "+position, "passid "+idItemList.get(position).getPasswordId());
			context.startActivity(intent);
		}
	};

	public ArrayList<OneItem> getIdItemList() {
		return idItemList;
	}

	public void setIdItemList(ArrayList<OneItem> idItemList, int currentFolderId) {
		this.currentFolderId = currentFolderId;
		this.idItemList = idItemList;
		notifyDataSetChanged();
	}
}
