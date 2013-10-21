package se206.project;

import java.io.ByteArrayOutputStream;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
/**
 * This class represents the add and edit contact screen of the contacts
 * manager application
 * 
 * @author Alan Lau, alau645, 2714269
 *
 */
public class AddEditContactActivity extends Activity {

	private Button buttonDone;
	private ImageButton buttonPhoto;
	private EditText editTextFirstName;
	private EditText editTextLastName;
	private EditText editTextMobileph;
	private EditText editTextHomeph;
	private EditText editTextWorkph;
	private EditText editTextEmail;
	private EditText editTextHomeAdd;
	private EditText editTextDoa;
	private Button buttonGroup;
	private Spinner spinnerGroup;

	private boolean isEdit;
	private int contactID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);

		// extra info sent from previous main activity to determine whether
		// this activity functions either as an add or edit contacts activity
		Bundle extras = getIntent().getExtras();
		String action = extras.getString("Action");
		isEdit = action.equals("edit");

		Intent intent = getIntent();
		Contact contact = (Contact) intent.getSerializableExtra("Contact");

		buttonDone = (Button)findViewById(R.id.add_button_done);
		buttonPhoto = (ImageButton)findViewById(R.id.add_button_photo);
		editTextFirstName = (EditText)findViewById(R.id.add_firstname_input);
		editTextLastName = (EditText)findViewById(R.id.add_lastname_input);
		editTextMobileph = (EditText)findViewById(R.id.add_mobileph_input);
		editTextHomeph = (EditText)findViewById(R.id.add_homeph_input);
		editTextWorkph = (EditText)findViewById(R.id.add_workph_input);
		editTextEmail = (EditText)findViewById(R.id.add_email_input);
		editTextHomeAdd = (EditText)findViewById(R.id.add_homeadd_input);
		editTextDoa = (EditText)findViewById(R.id.add_doa_input);
		buttonGroup = (Button)findViewById(R.id.add_button_group);
		spinnerGroup = (Spinner)findViewById(R.id.add_spinner_group);

		// activity running in edit mode, presets all EditText fields with info
		// from the selected contact
		if (isEdit) { // stub fields
			setTitle(contact.getFullName());
			editTextFirstName.setText(contact.getFirstName());
			editTextLastName.setText(contact.getLastName());
			editTextMobileph.setText(contact.getMobileph());
			editTextHomeph.setText(contact.getHomeph());
			editTextWorkph.setText(contact.getWorkph());
			editTextEmail.setText(contact.getEmail());
			editTextHomeAdd.setText(contact.getHomeAdd());
			editTextDoa.setText(contact.getDoa());
			contactID = contact.getID();

			byte[] bytesPhoto = contact.getPhoto();
			if (bytesPhoto != null) {
				Bitmap bmpPhoto = BitmapFactory.decodeByteArray(bytesPhoto, 0, bytesPhoto.length);
				buttonPhoto.setImageBitmap(bmpPhoto);
			}
		}

		// Done button which takes all the info and creates a new contact or finalises
		// the editing of a contact, then finishes this activity and returns to previous
		// activity
		buttonDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String firstName = editTextFirstName.getText().toString();
				String lastName = editTextLastName.getText().toString();
				String mobileph = editTextMobileph.getText().toString();
				String homeph = editTextHomeph.getText().toString();
				String workph = editTextWorkph.getText().toString();
				String email = editTextEmail.getText().toString();
				String homeAdd = editTextHomeAdd.getText().toString();
				String doa = editTextDoa.getText().toString();
				String group = "";

				// TODO if name is ""
				Contact contact = new Contact(firstName, lastName, mobileph,
						homeph, workph, email, homeAdd, doa, group);

				if (isEdit) {
					contact.setID(contactID);
				}

				Intent resultIntent = new Intent();
				resultIntent.putExtra("Contact", contact);
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}
		});

		// Photo button which allows user to select a photo from the gallery for a contact
		buttonPhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 1); //TODO "1"

			}
		});

		// Group button which transitions to edit group activity
		buttonGroup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(AddEditContactActivity.this, EditGroupsActivity.class);
				startActivity(intent);

			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch(requestCode) {
		case 1: //TODO "1"
			if(resultCode == Activity.RESULT_OK){  
				Uri selectedImage = intent.getData();
				buttonPhoto.setImageURI(selectedImage);
			}
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_contact, menu);
		return true;
	}

}
