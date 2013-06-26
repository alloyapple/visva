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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.NoSuchPaddingException;

import com.japanappstudio.IDxPassword.activities.EditIdPasswordActivity;
import com.japanappstudio.IDxPassword.contants.Contants;
import com.japanappstudio.IDxPassword.database.IdManagerPreference;
import com.japanappstudio.IDxPassword.idxpwdatabase.ElementID;
import com.japanappstudio.IDxPassword.sercurity.CipherUtil;

import com.japanappstudio.IDxPassword.activities.R;
import exp.mtparet.dragdrop.view.ListViewDragDrop;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
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
	private ArrayList<ElementID> idItemList;
	private ListViewDragDrop idListView;
	private boolean isModeEdit;
	private Handler mHandler;
	private int currentFolderOrder;
	private int currentFolderId;
	private IdManagerPreference mIdManagerPreference;
	private Drawable mDrawableIcon;

	public ItemAdapter(Context context, ArrayList<ElementID> idItemList,
			boolean isModeEdit, Handler mHandler, ListViewDragDrop idListView,
			int currentFoldeId, int currentFolderOrder) {
		this.context = context;
		this.idItemList = idItemList;
		this.isModeEdit = isModeEdit;
		this.mHandler = mHandler;
		this.idListView = idListView;
		this.currentFolderOrder = currentFolderOrder;
		this.currentFolderId = currentFoldeId;
		this.mIdManagerPreference = IdManagerPreference.getInstance(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return idItemList.size();
	}

	@Override
	public ElementID getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.idItemList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	class ViewHolder {
		TextView txtIdName;
		TextView txtIdUrl;
		ImageView iv;
		Button btnEdit;
		Button btnDelete;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {

		if (convertView == null) {
			convertView = (RelativeLayout) RelativeLayout.inflate(context,
					R.layout.list_id_row, null);
		}
		/* image logo */
		ImageView iv = (ImageView) convertView.findViewById(R.id.imageView1);
		iv.setImageDrawable(getImageDataBase(idItemList.get(position)
				.geteIconData()));
		iv.setContentDescription(this.idItemList.get(position).geteTitle());

		/* text name vs text url */
		TextView txtIdName = (TextView) convertView
				.findViewById(R.id.txt_id_item_name);
		txtIdName.setText(this.idItemList.get(position).geteTitle());
		txtIdName.setSelected(true);
		TextView txtIdUrl = (TextView) convertView
				.findViewById(R.id.txt_id_item_url);
		txtIdUrl.setText(this.idItemList.get(position).geteUrl());
		txtIdUrl.setSelected(true);

		/* btn edit */
		Button btnEdit = (Button) convertView
				.findViewById(R.id.btn_id_item_edit);
		btnEdit.setOnClickListener(mOnEditClickListener);
		iv.setOnClickListener(mOnEditClickListener);
		/* button delete */
		Button btnDelete = (Button) convertView
				.findViewById(R.id.btn_id_item_delete);
		btnDelete.setOnClickListener(mOnDeleteClickListener);

		if (isModeEdit && (currentFolderOrder >= 0)) {
			btnEdit.setVisibility(View.VISIBLE);
			btnDelete.setVisibility(View.VISIBLE);
			btnDelete.setFocusable(true);
			btnEdit.setFocusable(true);
			txtIdUrl.setSelected(true);
			txtIdName.setSelected(true);
		} else {
			btnEdit.setVisibility(View.GONE);
			btnDelete.setVisibility(View.GONE);
			btnDelete.setFocusable(false);
			btnEdit.setFocusable(false);
			txtIdUrl.setSelected(false);
			txtIdName.setSelected(false);
		}

		return convertView;
	}

	public void removeItem(int position) {
		idItemList.remove(position);
		notifyDataSetChanged();
	}

	public void updateModeForListView(boolean isEdit, int currentFolderOrder) {
		this.isModeEdit = isEdit;
		this.currentFolderOrder = currentFolderOrder;
		Log.e("run here", "run here edu " + isModeEdit);
		// this.isModeEdit = mIdManagerPreference.isEditMode();
		notifyDataSetChanged();
	}

	private OnClickListener mOnDeleteClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.e("click edit", "click edit");
			final int position = idListView.getPositionForView((View) v
					.getParent());
			Message msg = mHandler.obtainMessage();
			msg.arg1 = DIALOG_DELETE_ID;
			msg.arg2 = position;
			mHandler.sendMessage(msg);
		}
	};

	private OnClickListener mOnEditClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			final int position = idListView.getPositionForView((View) v
					.getParent());
			Message msg = mHandler.obtainMessage();
			msg.arg1 = DIALOG_EDIT_ID;
			msg.arg2 = position;
			mHandler.sendMessage(msg);
			Intent intent = new Intent(context, EditIdPasswordActivity.class);
			intent.putExtra(Contants.IS_INTENT_CREATE_NEW_ID, 0);
			intent.putExtra(Contants.CURRENT_FOLDER_ID, currentFolderId);
			mIdManagerPreference.setCurrentFolderId(currentFolderId);
			intent.putExtra(Contants.CURRENT_PASSWORD_ID,
					idItemList.get(position).geteId());
			context.startActivity(intent);
		}
	};

	public ArrayList<ElementID> getIdItemList() {
		return idItemList;
	}

	public void setIdItemList(ArrayList<ElementID> idItemList,
			int currentFolderOrder, int currentFolderId) {
		this.currentFolderId = currentFolderId;
		this.currentFolderOrder = currentFolderOrder;
		this.idItemList = idItemList;
		notifyDataSetChanged();
	}

	public void add(int to, ElementID element) {
		idItemList.add(to, element);
		notifyDataSetChanged();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private Drawable getIconDatabase(String icon) {
		Log.e("icon icon", "icon123 " + icon);
		if (null != icon && !"".equals(icon)) {
			File dir = new File(Contants.PATH_ID_FILES);
			if (!dir.exists())
				dir.mkdirs();
			File inputFile = new File(dir, icon);

			byte[] cipherBytes = new byte[(int) inputFile.length()];
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(inputFile);
				fis.read(cipherBytes, 0, cipherBytes.length);
				fis.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			byte[] decryptBytes = null;
			try {
				decryptBytes = CipherUtil.decrypt(cipherBytes);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Bitmap bmp = BitmapFactory.decodeByteArray(decryptBytes, 0,
					decryptBytes.length);
			return (Drawable) new BitmapDrawable(bmp);
		} else {
			mDrawableIcon = context.getResources().getDrawable(
					R.drawable.default_icon);
			return mDrawableIcon;
		}
	}

	public Drawable getImageDataBase(byte[] data) {
		if (data == null || data.length == 0) {
			return context.getResources().getDrawable(R.drawable.default_icon);
		}

		Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
		BitmapDrawable result = new BitmapDrawable(bMap);
		return result;
	}
}
