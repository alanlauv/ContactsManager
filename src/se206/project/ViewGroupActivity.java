package se206.project;

import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class ViewGroupActivity extends Activity {

	private ListView listView;
	private ImageButton buttonRemove;

	private Group group;
	private int itemPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_group);

		Intent intent = getIntent();
		group = (Group) intent.getSerializableExtra("Group");

		setTitle(group.getName());
		listView = (ListView)findViewById(R.id.view_group_listview);
		buttonRemove = (ImageButton)findViewById(R.id.view_group_button_remove);
		
		buttonRemove.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(MainActivity.buttonClick);
				
				AlertDialog.Builder builder = new AlertDialog.Builder(ViewGroupActivity.this);

				builder.setTitle("Remove this contact from " + group.getName() + "?");
				builder.setMessage("This cannot be undone!");
				builder.setNegativeButton("Cancel", null);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Contact contact = group.getGroupList().get(itemPosition);
						contact.setGroup(null);
						ContactsDatabaseHelper contactsDB = new ContactsDatabaseHelper(ViewGroupActivity.this);
						contactsDB.updateContact(contact);
						
						group.getGroupList().remove(itemPosition);
						((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
						listView.setSelection(0);

						setResult(Activity.RESULT_OK, new Intent());
					}
					
				});
				builder.create().show();
			}
			
		});

		setupListView();
	}

	/**
	 * Sets up ListView using a custom ContactArrayAdapter
	 * 
	 */
	private void setupListView() {

		List<Contact> contactList = group.getGroupList();

		Collections.sort(contactList, Contact.Comparators.FIRSTNAME);

		ContactArrayAdapter adapter = new ContactArrayAdapter(ViewGroupActivity.this, R.layout.main_listview_item, contactList);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			// When user clicks on a contact on the list a dialog box will show with three
			// options: view contact, edit contact, delete contact
			@Override
			public void onItemClick(AdapterView<?> parentView, View clickedView,
					int clickedViewPos, long id) {
				itemPosition = clickedViewPos;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_group, menu);
		return true;
	}

}
