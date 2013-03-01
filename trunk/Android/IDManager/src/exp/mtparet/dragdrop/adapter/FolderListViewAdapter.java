package exp.mtparet.dragdrop.adapter;

import java.util.ArrayList;

import exp.mtparet.dragdrop.data.FolderItem;
import exp.mtparet.dragdrop.view.ListViewDragDrop;
import visvateam.outsource.idmanager.contants.Contants;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import visvateam.outsource.idmanager.activities.R;

public class FolderListViewAdapter extends BaseAdapter {

	private static final int DELETE_FOLDER = 1;
	private static final int EDIT_FOLDER = 2;
	private boolean isEdit;
	private Context context;
	private ArrayList<FolderItem> folderList;
	private Handler mHandler;
	private ListViewDragDrop folderListView;

	public FolderListViewAdapter(Context context, ArrayList<FolderItem> folderList, boolean isEdit,
			Handler mHandler, ListViewDragDrop folderListView) {
		this.context = context;
		this.folderList = folderList;
		this.isEdit = isEdit;
		this.mHandler = mHandler;
		this.folderListView = folderListView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return folderList.size();
	}

	@Override
	public FolderItem getItem(int position) {
		// TODO Auto-generated method stub
		return this.folderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	// private class ViewHolder {
	// private Button imgFolder;
	// private Button imgFolderEdit;
	// private Button imgFolderDelete;
	// }

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			convertView = (RelativeLayout) RelativeLayout.inflate(context, R.layout.list_item_row,
					null);
		}

		Button imgFolderDelete = (Button) convertView.findViewById(R.id.img_folder_item_delete);
		Button imgFolderEdit = (Button) convertView.findViewById(R.id.img_folder_item_edit);
		TextView txtFodlerName = (TextView) convertView.findViewById(R.id.txt_folder_item_name);
		Button imgFolderIcon = (Button) convertView.findViewById(R.id.img_folder_item_icon);
		imgFolderIcon.setFocusable(false);
		txtFodlerName.setSelected(true);
		imgFolderDelete.setFocusable(false);
		imgFolderEdit.setFocusable(false);

		/* set action for button */
		imgFolderDelete.setOnClickListener(mOnDeleteClickListener);
		imgFolderEdit.setOnClickListener(mOnEditClickListener);

		convertView.setBackgroundResource(folderList.get(position).getFolderImgid());
		imgFolderIcon.setBackgroundResource(folderList.get(position).getFolderIconId());
		txtFodlerName.setText(""+folderList.get(position).getTextFolderName());
		if (folderList.get(position).getImgFolderType() == Contants.TYPE_FOLDER_NON_NORMAL) {
			imgFolderDelete.setVisibility(View.GONE);
			imgFolderEdit.setVisibility(View.GONE);
			txtFodlerName.setVisibility(View.GONE);
			imgFolderIcon.setVisibility(View.VISIBLE);
		} else {
			imgFolderIcon.setVisibility(View.GONE);
			if (isEdit == true) {
				imgFolderDelete.setVisibility(View.VISIBLE);
				imgFolderEdit.setVisibility(View.VISIBLE);
				txtFodlerName.setVisibility(View.GONE);
			} else {
				imgFolderDelete.setVisibility(View.GONE);
				imgFolderEdit.setVisibility(View.GONE);
				txtFodlerName.setVisibility(View.VISIBLE);
			}
		}

		return convertView;
	}

	public void removeItem(int position) {
		folderList.remove(position);
		notifyDataSetChanged();
	}

	public void updateModeForListView(boolean isEdit) {
		this.isEdit = isEdit;
		notifyDataSetChanged();
	}

	public void addNewFolder(FolderItem folder) {
		folderList.add(folder);
		notifyDataSetChanged();
	}

	private OnClickListener mOnDeleteClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.e("onClickDelete", "onClickDelete");
			final int position = folderListView.getPositionForView((View) v.getParent());
			Log.e("clickc lcik ", "Title clicked, row %d" + position);
			Message msg = mHandler.obtainMessage();
			msg.arg1 = DELETE_FOLDER;
			msg.arg2 = position;
			mHandler.sendMessage(msg);
		}
	};

	private OnClickListener mOnEditClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.e("OnclickEdit", "OnClickEdit");
			final int position = folderListView.getPositionForView((View) v.getParent());
			Log.e("clickc lcik ", "Title clicked, row %d" + position);
			if (isEdit
					&& folderList.get(position).getImgFolderType() == Contants.TYPE_FOLDER_NORMAL) {

				Message msg = mHandler.obtainMessage();
				msg.arg1 = EDIT_FOLDER;
				msg.arg2 = position;
				mHandler.sendMessage(msg);
			}
		}
	};
}
