package se206.project;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * This class represents the view contact activity of the contacts manager
 * application.
 * 
 * @author Alan Lau. alau645, 2714269
 *
 */
public class ViewContactActivity extends Activity {

	private ImageView imageView;
	private TextView mobilephTextView;
	private TextView homephTextView;
	private TextView workphTextView;
	private TextView emailTextView;
	private TextView homeAddTextView;
	private TextView doaTextView;
	private TextView groupTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_contact);

		// Get this contact form the previous activity
		Intent intent = getIntent();
		Contact contact = (Contact) intent.getSerializableExtra("Contact");

		setTitle(contact.getFullName());

		imageView = (ImageView)findViewById(R.id.view_image);
		mobilephTextView = (TextView)findViewById(R.id.view_phone_mobile);
		homephTextView = (TextView)findViewById(R.id.view_phone_home);
		workphTextView = (TextView)findViewById(R.id.view_phone_work);
		emailTextView = (TextView)findViewById(R.id.view_email);
		homeAddTextView = (TextView)findViewById(R.id.view_homeAdd);
		doaTextView = (TextView)findViewById(R.id.view_doa);
		groupTextView = (TextView)findViewById(R.id.view_group);

		// check if contact has an existing photo, then convert to bitmap and set it
		byte[] bytesPhoto = contact.getPhoto();
		if (bytesPhoto != null) {
			Bitmap bmpPhoto = BitmapFactory.decodeByteArray(bytesPhoto, 0, bytesPhoto.length);
			imageView.setImageBitmap(bmpPhoto);
		}

		// Set other fields of the contact
		mobilephTextView.setText(contact.getMobileph());
		homephTextView.setText(contact.getHomeph());
		workphTextView.setText(contact.getWorkph());
		emailTextView.setText(contact.getEmail());
		homeAddTextView.setText(contact.getHomeAdd());
		doaTextView.setText(contact.getDoa());
		groupTextView.setText(contact.getGroup());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_contact, menu);
		return true;
	}

}
