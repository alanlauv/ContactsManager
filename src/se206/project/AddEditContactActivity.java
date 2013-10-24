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

	private ImageButton buttonDone;
	private ImageButton buttonPhoto;
	private EditText editTextFirstName;
	private EditText editTextLastName;
	private EditText editTextMobileph;
	private EditText editTextHomeph;
	private EditText editTextWorkph;
	private EditText editTextEmail;
	private EditText editTextHomeAdd;
	private EditText editTextDoa;
	private ImageButton buttonGroup;
	private Spinner spinnerGroup;

	private boolean isEdit = false;
	private int contactID;//TODO fix up
	private Uri selectedImage = null;//TODO fix up
	private byte[] bytesPhoto = null;//TODO fix up
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
			contactID = contact.getID();

			bytesPhoto = contact.getPhoto();
			if (bytesPhoto != null) {
				Bitmap bmpPhoto = BitmapFactory.decodeByteArray(bytesPhoto, 0, bytesPhoto.length);
				buttonPhoto.setImageBitmap(bmpPhoto);
			}
		}
		
		setupSpinner(contact);

		// Done button which takes all the info and creates a new contact or finalises
		// the editing of a contact, then finishes this activity and returns to previous
		// activity
		buttonDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(MainActivity.buttonClick);

				String firstName = editTextFirstName.getText().toString().trim();
				String lastName = editTextLastName.getText().toString().trim();
				String mobileph = editTextMobileph.getText().toString();
				String homeph = editTextHomeph.getText().toString();
				String workph = editTextWorkph.getText().toString();
				String email = editTextEmail.getText().toString();
				String homeAdd = editTextHomeAdd.getText().toString();
				String doa = editTextDoa.getText().toString();
				String group = null;
				if (spinnerGroup.getSelectedItem().toString().compareTo("No Selection") != 0) {
					group = spinnerGroup.getSelectedItem().toString();//TODO add group in database if not "No Selection"
					//GroupsDatabaseHelper database = new GroupsDatabaseHelper(AddEditContactActivity.this);
					//List<Group> groupList = database.getAllGroups();
				}

				// TODO if name is ""
				Contact contact = new Contact(firstName, lastName, mobileph,
						homeph, workph, email, homeAdd, doa, group);

				if (isEdit) {
					contact.setID(contactID);
					// Unchanged contact photo
					if (bytesPhoto != null && selectedImage == null) {
						contact.setPhoto(bytesPhoto);
					}
				}

				// Changed contact photo
				if (selectedImage != null) {
					byte[] newPhoto;
					try {
						newPhoto = readBytes(selectedImage);
						contact.setPhoto(newPhoto);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (firstName.isEmpty() && lastName.isEmpty()) {
					String displayString =  "Contact requires at least a name";
					Toast.makeText(AddEditContactActivity.this, displayString, Toast.LENGTH_LONG).show();
				} else {
					Intent resultIntent = new Intent();
					resultIntent.putExtra("Contact", contact);
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
				startActivityForResult(intent, 1); //TODO "1"
			}
		});

		// Group button which transitions to edit group activity
		buttonGroup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(AddEditContactActivity.this, EditGroupsActivity.class);
				startActivityForResult(intent, 2); //TODO "2"
			}
		});

	}

	public void setupSpinner(Contact contact) {

		GroupsDatabaseHelper database = new GroupsDatabaseHelper(AddEditContactActivity.this);
		List<String> groupNameList = database.getAllGroupNames();
		Collections.sort(groupNameList);
		if (isEdit && contact.getGroup() != null) {
			if (groupNameList.contains(contact.getGroup())) {
				groupNameList.remove(contact.getGroup());
				groupNameList.add(0, contact.getGroup());
				groupNameList.add(groupNameList.size(), "No Selection");
			} else {
				contact.setGroup(null);
			}
		} else {
			groupNameList.add(0, "No Selection");
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, groupNameList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerGroup.setAdapter(dataAdapter);
	}

	public byte[] readBytes(Uri uri) throws IOException {
		// this dynamically extends to take the bytes you read
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

		// and then we can return your byte array.
		return byteBuffer.toByteArray();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch(requestCode) {
		case 1: //TODO "1"
			if(resultCode == Activity.RESULT_OK){  
				selectedImage = intent.getData();
				buttonPhoto.setImageURI(selectedImage);
			}
			break;
		case 2: // TODO "2" new group added
			if(resultCode == Activity.RESULT_OK){
				setupSpinner(contact);
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
