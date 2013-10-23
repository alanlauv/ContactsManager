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

public class ContactArrayAdapter extends ArrayAdapter {

	private Context context;
	private int layoutResourceId;   
	private List<Contact> data = null;

	public ContactArrayAdapter(Context context, int layoutResourceId, List<Contact> data) {
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
			holder.imgPhoto = (ImageView)row.findViewById(R.id.main_contact_img);
			holder.txtName = (TextView)row.findViewById(R.id.main_contact_name);

			row.setTag(holder);
		}
		else {
			holder = (ContactHolder)row.getTag();
		}

		Contact contact = data.get(position);
		holder.txtName.setText(contact.getFullName());
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
		private TextView txtName;
	}
}
