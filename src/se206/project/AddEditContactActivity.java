package se206.project;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);

		// extra info sent from previous main activity to determine whether
		// this activity functions either as an add or edit contacts activity
		Bundle extras = getIntent().getExtras();
		String action = extras.getString("Action");
		boolean isEdit = action.equals("edit");

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

		// Done button which takes all the info and creates a new contact or finalises
		// the editing of a contact, then finishes this activity and returns to previous
		// activity
		buttonDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// Photo button which allows user to select a photo from the gallery for a contact
		buttonPhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, 1);

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

		// activity running in edit mode, presets all EditText fields with info
		// from the selected contact
		if (isEdit) { // stub fields
			setTitle("Edit Contact");
			editTextFirstName.setText("Alan");
			editTextLastName.setText("Lau");
			editTextMobileph.setText("021 0210 2121");
			editTextHomeph.setText("09 321 3213");
			editTextWorkph.setText("09 123 1231");
			editTextEmail.setText("alau645@aucklanduni.ac.nz");
			editTextHomeAdd.setText("123 Random St");
			editTextDoa.setText("1234/01/31");
			
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_contact, menu);
		return true;
	}

}
