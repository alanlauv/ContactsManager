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
import android.widget.ImageButton;
import android.widget.ListView;
/**
 * This class represents the list view of this group and the contacts belonging to this group.
 * User can remove a contact from this group in this view
 * 
 * @author Alan Lau, alau645, 2714269
 *
 */
public class ViewGroupActivity extends Activity {

	private ListView listView;
	private ImageButton buttonRemove;

	private Group group;

	// Position of contact in the list view
	private int itemPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_group);

		// Get this group from previous activity
		Intent intent = getIntent();
		group = (Group) intent.getSerializableExtra("Group");

		setTitle(group.getName());
		listView = (ListView)findViewById(R.id.view_group_listview);
		buttonRemove = (ImageButton)findViewById(R.id.view_group_button_remove);

		setupListView();

		buttonRemove.setOnClickListener(new View.OnClickListener() {

			// Clicking on this button will show a dialog confirming user's removal of this contact
			// from the group
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

						// Assign no group to contact and update it in database
						Contact contact = group.getGroupList().get(itemPosition);
						contact.setGroup(null);
						ContactsDatabaseHelper contactsDB = new ContactsDatabaseHelper(ViewGroupActivity.this);
						contactsDB.updateContact(contact);

						// Remove from view and refresh
						group.getGroupList().remove(itemPosition);
						((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
						listView.setSelection(0);

						// Tell previous activity that this group has been modified
						setResult(Activity.RESULT_OK, new Intent());
					}

				});
				builder.create().show();
			}

		});
	}

	/**
	 * Sets up ListView using a custom ContactArrayAdapter and sorts by first name.
	 * 
	 */
	private void setupListView() {

		// Get data to feed into adapter
		List<Contact> contactList = group.getGroupList();

		Collections.sort(contactList, Contact.Comparators.FIRSTNAME);

		ContactArrayAdapter adapter = new ContactArrayAdapter(ViewGroupActivity.this,
				R.layout.main_listview_item, contactList);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			// When user clicks on a contact on the list itemPosition will update itself
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
