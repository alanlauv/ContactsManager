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

/**
 * This class represents a custom ArrayAdapter for displaying contacts on a Group list view.
 * Has a ImageView, title and subtitle TextViews, and a divider to show group name (once)
 * 
 * @author Alan Lau, alau645, 2714269
 *
 */
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
			// View is unchanged, get data from cache
			holder = (ContactHolder)row.getTag();
		}

		// Get contact data and set it into the view
		Contact contact = data.get(position);

		// contact is not assigned to group so make this view not visible
		if (contact.getGroup() == null) {
			holder.divider.setVisibility(View.GONE);

			// First item on the group list, so set the group name on the divider
		} else if (position == 0) {
			holder.divider.setText(contact.getGroup());

			// change in groups in the group list, so set group name on the divider
		} else if (data.get(position-1).getGroup() != null
				&& contact.getGroup().compareTo(data.get(position-1).getGroup()) != 0) {
			holder.divider.setText(contact.getGroup());

			// contact is in between the body of the body, so divider set to not visible
		} else {
			holder.divider.setVisibility(View.GONE);
		}

		holder.txtName.setText(contact.getFullName());
		holder.txtNumber.setText(contact.getMobileph());

		// Check if contact has photo and convert to bitmap and set it
		byte[] bytesPhoto = contact.getPhoto();
		if (bytesPhoto != null) {
			Bitmap bmpPhoto = BitmapFactory.decodeByteArray(bytesPhoto, 0, bytesPhoto.length);
			holder.imgPhoto.setImageBitmap(bmpPhoto);

			// no contact photo, set default photo
		} else {
			holder.imgPhoto.setImageResource(R.drawable.grey_android_logo);
		}

		return row;
	}

	static class ContactHolder {
		private ImageView imgPhoto;
		private TextView txtName, txtNumber, divider;
	}
}


