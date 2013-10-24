package se206.project;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupArrayAdapter extends ArrayAdapter<Contact> {

	private Context context;
	private int layoutResourceId;   
	private List<Contact> data = null;

	public GroupArrayAdapter(Context context, int layoutResourceId, List<Contact> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ContactHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new ContactHolder();
			holder.divider = (TextView)row.findViewById(R.id.main_divider);
			holder.imgPhoto = (ImageView)row.findViewById(R.id.main_contact_img);
			holder.txtName = (TextView)row.findViewById(R.id.main_contact_name);
			holder.txtNumber = (TextView)row.findViewById(R.id.main_contact_number);

			row.setTag(holder);
		} else {
			holder = (ContactHolder)row.getTag();
		}

		Contact contact = data.get(position);
		if (contact.getGroup() == null) {
			holder.divider.setVisibility(View.GONE);
		} else if (position == 0) {
			holder.divider.setText(contact.getGroup());
		} else if (data.get(position-1).getGroup() != null && contact.getGroup().compareTo(data.get(position-1).getGroup()) != 0) {
			holder.divider.setText(contact.getGroup());
		} else {
			holder.divider.setVisibility(View.GONE);
		}
		holder.txtName.setText(contact.getFullName());
		holder.txtNumber.setText(contact.getMobileph());
		byte[] bytesPhoto = contact.getPhoto();
		if (bytesPhoto != null) {
			Bitmap bmpPhoto = BitmapFactory.decodeByteArray(bytesPhoto, 0, bytesPhoto.length);
			holder.imgPhoto.setImageBitmap(bmpPhoto);
		} else { // no photo
			holder.imgPhoto.setImageResource(R.drawable.grey_android_logo);
		}

		return row;
	}

	static class ContactHolder {
		private ImageView imgPhoto;
		private TextView txtName, txtNumber, divider;
	}
}


