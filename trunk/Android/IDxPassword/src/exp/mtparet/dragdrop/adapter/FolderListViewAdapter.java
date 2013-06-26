package exp.mtparet.dragdrop.adapter;

import java.util.ArrayList;

import com.japanappstudio.IDxPassword.contants.Contants;
import com.japanappstudio.IDxPassword.idxpwdatabase.GroupFolder;

import exp.mtparet.dragdrop.view.DndListViewFolder;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.japanappstudio.IDxPassword.activities.R;

public class FolderListViewAdapter extends BaseAdapter {

	private static final int DELETE_FOLDER = 1;
	private static final int EDIT_FOLDER = 2;
	private boolean isEdit;
	private boolean isSearchMode;
	private Context context;
	private ArrayList<GroupFolder> folderList = new ArrayList<GroupFolder>();
	private Handler mHandler;
	private DndListViewFolder folderListView;
	private int currentFolderItem;
	private GroupFolder searchFolder;

	public FolderListViewAdapter(Context context,
			ArrayList<GroupFolder> folderList, boolean isEdit,
			Handler mHandler, DndListViewFolder folderListView,
			int currentFolderItem, boolean isSearchMode) {
		this.context = context;
		this.isEdit = isEdit;
		this.mHandler = mHandler;
		this.folderListView = folderListView;
		this.currentFolderItem = currentFolderItem;
		this.folderList = folderList;
		this.isSearchMode = isSearchMode;
		searchFolder = new GroupFolder(Contants.SEARCH_FOLDER_ID, "", 0,
				Contants.MASTER_PASSWORD_ID, Contants.SEARCH_FOLDER_ID);
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
			convertView = (RelativeLayout) RelativeLayout.inflate(context,
					R.layout.list_item_row, null);
		}

		Button imgFolderDelete = (Button) convertView
				.findViewById(R.id.img_folder_item_delete);
		Button imgFolderEdit = (Button) convertView
				.findViewById(R.id.img_folder_item_edit);
		TextView txtFodlerName = (TextView) convertView
				.findViewById(R.id.txt_folder_item_name);
		Button imgFolderIcon = (Button) convertView
				.findViewById(R.id.img_folder_item_icon);
		ImageView imgBgFolder = (ImageView) convertView
				.findViewById(R.id.img_bg_folder);
		imgFolderIcon.setFocusable(false);
		txtFodlerName.setSelected(true);
		imgFolderDelete.setFocusable(false);
		imgFolderEdit.setFocusable(false);
		txtFodlerName.setFocusable(false);
		imgBgFolder.setFocusable(false);

		/* set action for button */
		imgFolderDelete.setOnClickListener(mOnDeleteClickListener);
		imgFolderEdit.setOnClickListener(mOnEditClickListener);

		imgBgFolder.setBackgroundResource(R.drawable.folder_common);

		if (folderList.get(position).getgOrder() < 0) {
			if (position == currentFolderItem)
				imgBgFolder.setBackgroundResource(R.drawable.folder_s_select);
			else
				imgBgFolder.setBackgroundResource(R.drawable.folder_s_common);
			imgFolderDelete.setVisibility(View.GONE);
			imgFolderEdit.setVisibility(View.GONE);
			txtFodlerName.setVisibility(View.GONE);
			imgFolderIcon.setVisibility(View.VISIBLE);
			if (folderList.get(position).getgOrder() == Contants.FAVOURITE_FOLDER_ID) {
				imgFolderIcon.setBackgroundResource(R.drawable.favorite);
			} else if (folderList.get(position).getgOrder() == Contants.HISTORY_FOLDER_ID)
				imgFolderIcon.setBackgroundResource(R.drawable.history);
			else if (folderList.get(position).getgOrder() == Contants.SEARCH_FOLDER_ID) {
				imgFolderIcon.setBackgroundResource(R.drawable.ic_search);
				imgBgFolder.setBackgroundResource(R.drawable.folder_select);
				imgFolderIcon.setVisibility(View.VISIBLE);
				txtFodlerName.setVisibility(View.GONE);
			}

		} else {
			if (position == currentFolderItem
					|| (isSearchMode && position == 0))
				imgBgFolder.setBackgroundResource(R.drawable.folder_select);
			else
				imgBgFolder.setBackgroundResource(R.drawable.folder_common);
			imgFolderIcon.setVisibility(View.GONE);
			if (isEdit == true) {
				imgFolderDelete.setVisibility(View.VISIBLE);
				imgFolderEdit.setVisibility(View.VISIBLE);
				txtFodlerName.setVisibility(View.GONE);
			} else {
				if (folderList.get(position).getgOrder() == Contants.SEARCH_FOLDER_ID) {
					Log.e("adkfjhdfkj", "adsufhd ");
					imgFolderIcon.setBackgroundResource(R.drawable.search);
					imgFolderIcon.setVisibility(View.VISIBLE);
					txtFodlerName.setVisibility(View.GONE);
				} else {
					Log.e("adkfjhdfkj", "adsufhd1 ");
					imgFolderDelete.setVisibility(View.GONE);
					imgFolderEdit.setVisibility(View.GONE);
					txtFodlerName.setVisibility(View.VISIBLE);
					imgFolderIcon.setVisibility(View.GONE);
				}
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
		} else if (folderList.contains(searchFolder)) {
			for (int i = 0; i < folderList.size(); i++) {
				if (Contants.SEARCH_FOLDER_ID == folderList.get(i).getgId())
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
			final int position = folderListView.getPositionForView((View) v
					.getParent());
			Message msg = mHandler.obtainMessage();
			msg.arg1 = DELETE_FOLDER;
			msg.arg2 = position;
			mHandler.sendMessage(msg);
		}
	};

	private OnClickListener mOnEditClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			final int position = folderListView.getPositionForView((View) v
					.getParent());
			if (isEdit) {
				Message msg = mHandler.obtainMessage();
				msg.arg1 = EDIT_FOLDER;
				msg.arg2 = position;
				mHandler.sendMessage(msg);
			}
		}
	};

	public void add(int to, GroupFolder groupFolder) {
		folderList.add(to, groupFolder);
		notifyDataSetChanged();
	}
}
