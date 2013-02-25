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

import com.google.android.gms.internal.f;

import visvateam.outsource.idmanager.activities.R;
import exp.mtparet.dragdrop.data.OneItem;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ItemAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<OneItem> alPicture;

	private boolean isModeEdit;

	public ItemAdapter(Context context, ArrayList<OneItem> alPicture,
			boolean isModeEdit) {
		this.context = context;
		this.alPicture = alPicture;
		this.isModeEdit = isModeEdit;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return alPicture.size();
	}

	@Override
	public OneItem getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.alPicture.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {

		if (convertView == null) {
			convertView = (RelativeLayout) RelativeLayout.inflate(context,
					R.layout.list_id_row, null);

		}

		/* image logo */
		ImageView iv = (ImageView) convertView.findViewById(R.id.imageView1);
		iv.setImageDrawable(context.getResources().getDrawable(
				alPicture.get(position).getId()));
		iv.setContentDescription(this.alPicture.get(position).getName());

		/*btn edit*/
		
		Button btnEdit = (Button)convertView.findViewById(R.id.btn_id_item_edit);
	
		/* button delete */
		Button btnDelete = (Button) convertView.findViewById(R.id.btn_id_item_delete);

		if (isModeEdit){
			btnEdit.setVisibility(View.VISIBLE);
			btnDelete.setVisibility(View.VISIBLE);
		}
		else{
			btnEdit.setVisibility(View.GONE);
			btnDelete.setVisibility(View.GONE);
		}

		return convertView;
	}

	public void addPicture(OneItem onItem, int position) {

		if (position < alPicture.size()) {
			alPicture.add(position, onItem);
		} else {
			alPicture.add(onItem);
		}

		notifyDataSetChanged();
	}

	public void addPicture(OneItem onItem) {

		alPicture.add(onItem);

		notifyDataSetChanged();
	}

	public void removeItem(int arg1) {
		alPicture.remove(arg1);
		notifyDataSetChanged();
	}
	
	public void updateModeForListView(boolean isEdit) {
		this.isModeEdit = isEdit;
		notifyDataSetChanged();
	}

}
