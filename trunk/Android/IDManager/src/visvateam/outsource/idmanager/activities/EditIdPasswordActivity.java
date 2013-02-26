package visvateam.outsource.idmanager.activities;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class EditIdPasswordActivity extends Activity {
	private ListView mListView;
	private ImageButton mBtnLike;
	private static int MAX_ITEM = 15;
	private String nameItem[] = { "ID1", "Pass1", "ID2", "Pass2", "ID3",
			"Pass3", "ID4", "Pass4", "ID5", "Pass5", "ID6", "Pass6", "ID7",
			"Pass7", "ID8", "Pass8", "ID9", "Pass9", "ID10", "Pass10", "ID11",
			"Pass11", "ID12", "Pass12", "ID13", "Pass13", "ID14", "Pass14",
			"ID15", "Pass15" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_id_pass);
		mListView = (ListView) findViewById(R.id.id_listview_item_add);
		ArrayList<Item> mItems = new ArrayList<EditIdPasswordActivity.Item>();
		for (int i = 0; i < MAX_ITEM * 2; i++) {
			Item item = new Item();
			item.mNameItem = nameItem[i];
			item.mContentItem = "";
			mItems.add(item);

		}
		mListView.setAdapter(new ItemAddAdapter(this, mItems));

	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, EditIdPasswordActivity.class);
		activity.startActivity(i);
	}

	public void onImgAvatar(View v) {

	}

	public void onToGenerator(int i) {
		PasswordGeneratorActivity.startActivity(this);

	}

	public void onReturn(View v) {
		finish();
	}

	public void onInfo(View v) {

	}

	public void onGoogleHome(View v) {

	}


	class ItemAddAdapter extends BaseAdapter {
		Activity mActivity;
		ArrayList<Item> mItems;

		public ItemAddAdapter(Activity pActivity, ArrayList<Item> pItems) {
			super();
			mActivity = pActivity;
			mItems = pItems;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 30;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			final int pos = position;
			LayoutInflater inflater = mActivity.getLayoutInflater();
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_id_pass_add, null);
				holder = new ViewHolder();
				holder.nameItem = (EditText) convertView
						.findViewById(R.id.id_txt_nameItem);
				holder.nameItem.setText(mItems.get(position).mNameItem);
				holder.contentItem = (EditText) convertView
						.findViewById(R.id.id_txt_detailItem);
				holder.contentItem.setText(mItems.get(position).mContentItem);
				((ImageButton) convertView.findViewById(R.id.id_btn_generator))
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								onToGenerator(pos);
							}
						});
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}

	}

	private class ViewHolder {
		EditText nameItem;
		EditText contentItem;

	}

	private class Item {
		public String mNameItem;
		public String mContentItem;
	}

}
