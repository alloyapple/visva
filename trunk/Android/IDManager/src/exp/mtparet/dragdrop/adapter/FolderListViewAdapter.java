package exp.mtparet.dragdrop.adapter;

import java.util.ArrayList;

import exp.mtparet.dragdrop.data.FolderItem;
import visvateam.outsource.idmanager.activities.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class FolderListViewAdapter extends BaseAdapter {

	private boolean isEdit;
	private Context context;
	private ArrayList<FolderItem> folderList;

	public FolderListViewAdapter(Context context, ArrayList<FolderItem> folderList, boolean isEdit) {
		this.context = context;
		this.folderList = folderList;
		this.isEdit = isEdit;
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
		private ImageView imgFolder;
		private ImageView imgFolderIcon;
		private ImageView imgFolderDelete;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;

		if (convertView == null) {
			convertView = (FrameLayout) FrameLayout.inflate(context, R.layout.list_item_row, null);
			holder = new ViewHolder();
			holder.imgFolder = (ImageView) convertView.findViewById(R.id.img_folder);
			holder.imgFolderDelete = (ImageView) convertView.findViewById(R.id.img_folder_edit);
			holder.imgFolderIcon = (ImageView) convertView.findViewById(R.id.img_folder_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.imgFolder.setBackgroundResource(folderList.get(position).getId());
		holder.imgFolderIcon.setBackgroundResource(folderList.get(position).getFolderIconId());

		if (isEdit == true && folderList.get(position).getImgFolderType() == 1) {
			holder.imgFolderDelete.setVisibility(View.VISIBLE);
		} else
			holder.imgFolderDelete.setVisibility(View.GONE);

		return convertView;
	}

	public void onRemoveItem(int position) {

	}

	public void updateModeForListView(boolean isEdit) {
		this.isEdit = isEdit;
		notifyDataSetChanged();
	}

	public void addNewFolder(FolderItem folder) {
		folderList.add(folder);
		notifyDataSetChanged();
	}
}
