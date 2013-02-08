package exp.mtparet.dragdrop.adapter;

import java.util.HashMap;
import visvateam.outsource.idmanager.activities.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FolderListViewAdapter extends BaseAdapter {

	private int[] imgFolderId;
	private int[] imgFolderIconId;
	private int[] imgFolderEditId;
	Activity activity;
	private boolean isEdit;

	// constructor
	public FolderListViewAdapter(Activity activity, int[] imgFolderId, int[] imgFolderIconId,
			boolean isEdit) {
		this.activity = activity;
		this.imgFolderIconId = imgFolderIconId;
		this.imgFolderId = imgFolderId;
		this.isEdit = isEdit;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgFolderId.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return imgFolderId[arg0];
	}

	private class ViewHolder {
		private ImageView imgFolder;
		private ImageView imgFolderIcon;
		private ImageView imgFolderEdit;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		LayoutInflater inflater = activity.getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_row, null);
			holder = new ViewHolder();
			holder.imgFolder = (ImageView) convertView.findViewById(R.id.img_folder);
			holder.imgFolderEdit = (ImageView) convertView.findViewById(R.id.img_folder_edit);
			holder.imgFolderIcon = (ImageView) convertView.findViewById(R.id.img_folder_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.imgFolder.setBackgroundResource(R.drawable.folder_s_common);
		holder.imgFolderIcon.setBackgroundResource(R.drawable.favorite);
		holder.imgFolderEdit.setBackgroundResource(R.drawable.delete);
		if (isEdit == true) {
			holder.imgFolderEdit.setVisibility(View.VISIBLE);
		} else
			holder.imgFolderEdit.setVisibility(View.GONE);

		return convertView;
	}

}
