package se206.project;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
/**
 * This class represents the add and edit contact screen of the contacts
 * manager application
 * 
 * @author Alan Lau, alau645, 2714269
 *
 */
public class AddEditContactActivity extends Activity {

	// Values used for passing between activities through intents
	private static final int CHOOSE_PHOTO = 1;
	private static final int EDIT_GROUP = 2;

	private static final String NO_SELECTION = "No Selection";

	private ImageButton buttonDone, buttonPhoto, buttonGroup;
	private EditText editTextFirstName;
	private EditText editTextLastName;
	private EditText editTextMobileph;
	private EditText editTextHomeph;
	private EditText editTextWorkph;
	private EditText editTextEmail;
	private EditText editTextHomeAdd;
	private EditText editTextDoa;
	private Spinner spinnerGroup;

	// Edit mode
	private boolean isEdit = false;

	// Image from gallery user selects
	private Uri selectedImage = null;

	// This contact
	private Contact contact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);

		// extra info sent from previous main activity to determine whether
		// this activity functions either as an add or edit contacts activity
		Bundle extras = getIntent().getExtras();
		int action = extras.getInt("Action");
		isEdit = (action == MainActivity.EDIT_CONTACT);

		buttonDone = (ImageButton)findViewById(R.id.add_button_done);
		buttonPhoto = (ImageButton)findViewById(R.id.add_button_photo);
		editTextFirstName = (EditText)findViewById(R.id.add_firstname_input);
		editTextLastName = (EditText)findViewById(R.id.add_lastname_input);
		editTextMobileph = (EditText)findViewById(R.id.add_mobileph_input);
		editTextHomeph = (EditText)findViewById(R.id.add_homeph_input);
		editTextWorkph = (EditText)findViewById(R.id.add_workph_input);
		editTextEmail = (EditText)findViewById(R.id.add_email_input);
		editTextHomeAdd = (EditText)findViewById(R.id.add_homeadd_input);
		editTextDoa = (EditText)findViewById(R.id.add_doa_input);
		buttonGroup = (ImageButton)findViewById(R.id.add_button_group);
		spinnerGroup = (Spinner)findViewById(R.id.add_spinner_group);

		// activity running in edit mode, presets all EditText fields with info
		// from the selected contact
		if (isEdit) {
			Intent intent = getIntent();
			contact = (Contact) intent.getSerializableExtra("Contact");

			setTitle(contact.getFullName());
			editTextFirstName.setText(contact.getFirstName());
			editTextLastName.setText(contact.getLastName());
			editTextMobileph.setText(contact.getMobileph());
			editTextHomeph.setText(contact.getHomeph());
			editTextWorkph.setText(contact.getWorkph());
			editTextEmail.setText(contact.getEmail());
			editTextHomeAdd.setText(contact.getHomeAdd());
			editTextDoa.setText(contact.getDoa());

			// Check if contact has a photo already, convert into bitmap and set the image button
			byte[] bytesPhoto = contact.getPhoto();
			if (bytesPhoto != null) {
				Bitmap bmpPhoto = BitmapFactory.decodeByteArray(bytesPhoto, 0, bytesPhoto.length);
				buttonPhoto.setImageBitmap(bmpPhoto);
			}
		}

		setupSpinner();

		// Done button which takes all the info and creates a new contact or finalises
		// the editing of a contact, then finishes this activity and returns to previous
		// activity
		buttonDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(MainActivity.buttonClick);

				// Get text from all edit boxes
				String firstName = editTextFirstName.getText().toString().trim();
				String lastName = editTextLastName.getText().toString().trim();
				String mobileph = editTextMobileph.getText().toString();
				String homeph = editTextHomeph.getText().toString();
				String workph = editTextWorkph.getText().toString();
				String email = editTextEmail.getText().toString();
				String homeAdd = editTextHomeAdd.getText().toString();
				String doa = editTextDoa.getText().toString();

				// Group will be null if no group chosen
				String group = null;
				if (spinnerGroup.getSelectedItem().toString().compareTo(NO_SELECTION) != 0) {
					group = spinnerGroup.getSelectedItem().toString();
				}

				Contact newContact = new Contact(firstName, lastName, mobileph,
						homeph, workph, email, homeAdd, doa, group);

				// Check if edit mode, set the ID so it can be updated in DB
				if (isEdit) {
					newContact.setID(contact.getID());
					// Unchanged contact photo
					if (contact.getPhoto() != null && selectedImage == null) {
						newContact.setPhoto(contact.getPhoto());
					}
				}

				// Changed contact photo, convert into byte array and store
				if (selectedImage != null) {
					byte[] newPhoto;
					try {
						newPhoto = readBytes(selectedImage);
						newContact.setPhoto(newPhoto);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				// Check if name is entered and not empty strings - display toast message if invalid name
				if (firstName.isEmpty() && lastName.isEmpty()) {
					String displayString =  "Contact requires at least a name";
					Toast.makeText(AddEditContactActivity.this, displayString, Toast.LENGTH_LONG).show();

					// Finish this activity and send back the new contact
				} else {
					Intent resultIntent = new Intent();
					resultIntent.putExtra("Contact", newContact);
					setResult(Activity.RESULT_OK, resultIntent);
					finish();
				}
			}
		});

		// Photo button which allows user to select a photo from the gallery for a contact
		buttonPhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, CHOOSE_PHOTO);
			}
		});

		// Group button which transitions to edit group activity
		buttonGroup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(AddEditContactActivity.this, EditGroupsActivity.class);
				startActivityForResult(intent, EDIT_GROUP);
			}
		});

	}

	/**
	 * Sets up the spinner by getting all group names and displaying it on a simple spinner
	 * dropdown item list
	 * 
	 * @param contact this contact
	 */
	private void setupSpinner() {

		// Get all groups from the database and sort
		GroupsDatabaseHelper database = new GroupsDatabaseHelper(AddEditContactActivity.this);
		List<String> groupNameList = database.getAllGroupNames();
		Collections.sort(groupNameList);

		// Check if in edit mode and this contact is assigned to a group
		if (isEdit && contact.getGroup() != null) {

			// Make this contact's group as the first item on spinner and "No Selection" as last
			if (groupNameList.contains(contact.getGroup())) {
				groupNameList.remove(contact.getGroup());
				groupNameList.add(0, contact.getGroup());
				groupNameList.add(groupNameList.size(), NO_SELECTION);

				// This contact has no group, so make first item as "No Selection"
			} else {
				contact.setGroup(null);
				groupNameList.add(0, NO_SELECTION);
			}

			// In add mode, this contact has no group, so make first item as "No Selection"
		} else {
			groupNameList.add(0, NO_SELECTION);
		}

		// Setup spinner with the data
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddEditContactActivity.this,
				android.R.layout.simple_spinner_item, groupNameList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerGroup.setAdapter(dataAdapter);
	}

	/**
	 * Converts URI image to Btye[] image.
	 * 
	 * @param uri photo choosen from gallery
	 * @return byte array of photo
	 * @throws IOException
	 */
	public byte[] readBytes(Uri uri) throws IOException {
		// this dynamically extends to take the bytes to read
		InputStream inputStream = getContentResolver().openInputStream(uri);
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

		// this is storage overwritten on each iteration with bytes
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		// we need to know how may bytes were read to write them to the byteBuffer
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}

		return byteBuffer.toByteArray();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch(requestCode) {
		// User chose photo from gallery
		case CHOOSE_PHOTO:
			if(resultCode == Activity.RESULT_OK){
				// Set button with the chosen image
				selectedImage = intent.getData();
				buttonPhoto.setImageURI(selectedImage);
			}
			break;
			// Groups have been edited, data set of spinner changed, so re-setup
		case EDIT_GROUP:
			if(resultCode == Activity.RESULT_OK){
				setupSpinner();
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
