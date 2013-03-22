package exp.mtparet.dragdrop.adapter;

import java.util.ArrayList;
import exp.mtparet.dragdrop.view.ListViewDragDrop;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.idxpwdatabase.GroupFolder;
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
	private boolean isSearchMode;
	private Context context;
	private ArrayList<GroupFolder> folderList = new ArrayList<GroupFolder>();
	private Handler mHandler;
	private ListViewDragDrop folderListView;
	private int currentFolderItem;
	private GroupFolder searchFolder;

	public FolderListViewAdapter(Context context, ArrayList<GroupFolder> folderList,
			boolean isEdit, Handler mHandler, ListViewDragDrop folderListView,
			int currentFolderItem, boolean isSearchMode) {
		this.context = context;
		this.isEdit = isEdit;
		this.mHandler = mHandler;
		this.folderListView = folderListView;
		this.currentFolderItem = currentFolderItem;
		this.folderList = folderList;
		this.isSearchMode = isSearchMode;
		searchFolder = new GroupFolder(Contants.SEARCH_FOLDER_ID, "", 0,
				Contants.MASTER_PASSWORD_ID, 0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return folderList.size();
	}

	@Override
	public GroupFolder getItem(int position) {
		// TODO Auto-generated method stub
		return this.folderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

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

		convertView.setBackgroundResource(R.drawable.folder_common);

		if (folderList.get(position).getgOrder() < 0) {
			if (position == currentFolderItem)
				convertView.setBackgroundResource(R.drawable.folder_s_select);
			else
				convertView.setBackgroundResource(R.drawable.folder_s_common);
//			if (isSearchMode && position == 0) {
//				convertView.setBackgroundResource(R.drawable.folder_s_select);
//				isSearchMode = false;
//				Message msg = mHandler.obtainMessage();
//				msg.arg1 = Contants.IS_SEARCH_MODE;
//				msg.arg2 = 0;
//				mHandler.sendMessage(msg);
//			}
			imgFolderDelete.setVisibility(View.GONE);
			imgFolderEdit.setVisibility(View.GONE);
			txtFodlerName.setVisibility(View.GONE);
			imgFolderIcon.setVisibility(View.VISIBLE);
			if (folderList.get(position).getgOrder() == Contants.FAVOURITE_FOLDER_ID) {
				imgFolderIcon.setBackgroundResource(R.drawable.favorite);
			} else if (folderList.get(position).getgOrder() == Contants.HISTORY_FOLDER_ID)
				imgFolderIcon.setBackgroundResource(R.drawable.history);

		} else {
			if (position == currentFolderItem || (isSearchMode && position == 0))
				convertView.setBackgroundResource(R.drawable.folder_select);
			else 
				convertView.setBackgroundResource(R.drawable.folder_common);
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
		
		txtFodlerName.setText("" + folderList.get(position).getgName());
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

	public void setFolderSelected(int currentFolderItem) {
		this.currentFolderItem = currentFolderItem;
		notifyDataSetChanged();
	}

	public void addNewFolder(GroupFolder folder, int gOrder) {
		currentFolderItem = 0;
		folderList.add(gOrder, folder);
		notifyDataSetChanged();
	}

	public void updateSearchMode(boolean isSearchMode, int currentFolderItem) {
		this.currentFolderItem = currentFolderItem;
		this.isSearchMode = isSearchMode;
		if (isSearchMode) {
			if (!folderList.contains(searchFolder)) {
				folderList.add(0, searchFolder);
			}
		} else if (folderList.contains(searchFolder))
		{
			for(int i = 0 ; i <folderList.size();i++){
				if(Contants.SEARCH_FOLDER_ID == folderList.get(i).getgId())
					folderList.remove(i);
			}
		}
		notifyDataSetChanged();
	}

	public void updateFolderList(ArrayList<GroupFolder> folderItems) {
		this.folderList = folderItems;
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
			if (isEdit) {
				Message msg = mHandler.obtainMessage();
				msg.arg1 = EDIT_FOLDER;
				msg.arg2 = position;
				mHandler.sendMessage(msg);
			}
		}
	};
}
