package exp.mtparet.dragdrop.adapter;

import java.util.ArrayList;

import exp.mtparet.dragdrop.data.FolderItem;
import exp.mtparet.dragdrop.view.ListViewDragDrop;
import visvateam.outsource.idmanager.activities.R;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Button;

public class FolderListViewAdapter extends BaseAdapter {

	private static final int DELETE_FOLDER = 1;
	private static final int EDIT_FOLDER = 2;
	private boolean isEdit;
	private Context context; 
	private ArrayList<FolderItem> folderList;
	private Handler mHandler;
	private ListViewDragDrop folderListView;

	public FolderListViewAdapter(Context context, ArrayList<FolderItem> folderList, boolean isEdit,
			Handler mHandler,ListViewDragDrop folderListView) {
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
		return folderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private class ViewHolder {
		private Button imgFolder;
		private Button imgFolderIcon;
		private Button imgFolderDelete;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;

		if (convertView == null) {
			convertView = (FrameLayout) FrameLayout.inflate(context, R.layout.list_item_row, null);
			holder = new ViewHolder();
			holder.imgFolder = (Button) convertView.findViewById(R.id.img_folder);
			holder.imgFolderDelete = (Button) convertView.findViewById(R.id.img_folder_edit);
			holder.imgFolderIcon = (Button) convertView.findViewById(R.id.img_folder_icon);

			/* set action for button */
			holder.imgFolderDelete.setOnClickListener(mOnDeleteClickListener);
			holder.imgFolderIcon.setOnClickListener(mOnEditClickListener);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.imgFolder.setBackgroundResource(folderList.get(position).getFolderImgid());
		holder.imgFolderIcon.setBackgroundResource(folderList.get(position).getFolderIconId());

		if (isEdit == true && folderList.get(position).getImgFolderType() == 1) {
			holder.imgFolderDelete.setVisibility(View.VISIBLE);
		} else
			holder.imgFolderDelete.setVisibility(View.GONE);

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
            Log.e("clickc lcik ", "Title clicked, row %d"+ position);
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
            Log.e("clickc lcik ", "Title clicked, row %d"+ position);
			Message msg = mHandler.obtainMessage();
			msg.arg1 = EDIT_FOLDER;
			msg.arg2 = position;
			mHandler.sendMessage(msg);
		}
	};

}
