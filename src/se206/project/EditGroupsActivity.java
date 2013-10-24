package se206.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.SimpleAdapter;
import android.widget.Toast;
/**
 * This class represents the edit groups activity, which allows users to edit
 * all the groups currently in the contacts manager application.
 * 
 * @author Alan Lau, alau645, 2714269
 *
 */
public class EditGroupsActivity extends Activity {

	private static final int EDIT_GROUP = 1;
	private static final String TEXT1 = "text1";
	private static final String TEXT2 = "text2";

	// data used to feed into adapter
	private GroupsDatabaseHelper database = new GroupsDatabaseHelper(EditGroupsActivity.this);
	private List<Group> groupList = new ArrayList<Group>();
	private List<Map<String, String>> displayList =  new ArrayList<Map<String, String>>();

	private ImageButton buttonNewGroup;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups);

		buttonNewGroup = (ImageButton)findViewById(R.id.groups_button_new);
		listView = (ListView)findViewById(R.id.groups_listview);

		setupListView();

		// New group button which shows a dialog box allowing user to input a
		// new group name and then creates a new group
		buttonNewGroup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(MainActivity.buttonClick);

				AlertDialog.Builder builder = new AlertDialog.Builder(EditGroupsActivity.this);
				builder.setTitle("Enter new group name:");
				final EditText input = new EditText(EditGroupsActivity.this);
				builder.setView(input);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String groupName = input.getText().toString().trim();

						boolean isValidName = checkGroupName(groupName);

						// If group does not already exist, make group, add into database, refresh view
						if (isValidName) {
							Group group = new Group(input.getText().toString());
							groupList.add(group);
							database.addGroup(group);
							setupDisplayList();
							refreshListView();
							setResult(Activity.RESULT_OK, new Intent()); // refresh spinner in AddEdit
						}
					}

				});
				builder.create().show();
			}

		});
	}

	/**
	 * Sets up ListView using a SimpleAdapter. Top text shows group name and subtitle shows
	 * the number in the group.
	 * 
	 */
	private void setupListView() {
		// Set up data to feed into adapter
		ContactsDatabaseHelper contactsDB = new ContactsDatabaseHelper(EditGroupsActivity.this);
		groupList = database.getAllGroups(contactsDB.getAllContacts());

		setupDisplayList();

		final String[] fromMapKey = new String[] {TEXT1, TEXT2};
		int[] ids = {android.R.id.text1, android.R.id.text2};

		SimpleAdapter adapter = new SimpleAdapter(EditGroupsActivity.this, displayList,
				android.R.layout.simple_list_item_2, fromMapKey, ids);

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			// When user clicks on a contact on the list a dialog box will show with three
			// options: view group, edit group name, delete group
			@Override
			public void onItemClick(AdapterView<?> parentView, View clickedView,
					final int clickedViewPos, long id) {

				AlertDialog.Builder builder = new AlertDialog.Builder(EditGroupsActivity.this);

				final Group selectedGroup = groupList.get(clickedViewPos);

				builder.setTitle(selectedGroup.getName());
				String[] contactOptions = {"View Group", "Edit Group Name", "Delete Group"};
				builder.setItems(contactOptions, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// View group, takes user to view group activity
						if (which == 0) {
							Intent intent = new Intent();
							intent.setClass(EditGroupsActivity.this, ViewGroupActivity.class);
							intent.putExtra("Group", selectedGroup);
							startActivityForResult(intent, EDIT_GROUP);

							// Edit group, asks user for new name in dialog box
						} else if (which == 1) {
							AlertDialog.Builder builder = new AlertDialog.Builder(EditGroupsActivity.this);

							builder.setTitle("Enter new group name:");
							final EditText inputName = new EditText(EditGroupsActivity.this);
							builder.setView(inputName);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									String newName = inputName.getText().toString();

									// Check if newName is valid
									boolean isValidName = checkGroupName(newName);

									// Add to database etc, if valid name
									if (isValidName) {
										groupList.get(clickedViewPos).setName(newName);
										updateContactsDB(clickedViewPos);
										database.updateGroup(groupList.get(clickedViewPos));
										setupDisplayList();
										refreshListView();
										setResult(Activity.RESULT_OK, new Intent()); // refresh spinner in AddEdit
									}
								}
							});
							builder.create().show();

							// Delete group, asks user to confirm deletion
						} else if (which == 2) {
							AlertDialog.Builder builder = new AlertDialog.Builder(EditGroupsActivity.this);
							builder.setTitle("Delete this group?");
							builder.setMessage("This cannot be undone!");
							builder.setNegativeButton("Cancel", null);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// Delete from database and refresh view etc
									database.deleteGroup(selectedGroup);
									groupList.get(clickedViewPos).setName(null);
									updateContactsDB(clickedViewPos);
									groupList.remove(clickedViewPos);
									displayList.remove(clickedViewPos);
									refreshListView();
									setResult(Activity.RESULT_OK, new Intent()); // refresh spinner in AddEdit
								}

							});
							builder.setCancelable(true);
							builder.create().show();
						}

					}

				});
				builder.create().show();
			}

		});
	}

	/**
	 * Sets up the data list that is being fed into the adapter for the list view
	 * Sorts by alphabetical order
	 */
	private void setupDisplayList() {

		displayList.clear();
		Collections.sort(groupList);

		// Top line - group name, Bottom line - number of contacts in this group
		for(Group group : groupList) {
			final Map<String, String> listItemMap = new HashMap<String, String>();
			listItemMap.put(TEXT1, group.getName());
			listItemMap.put(TEXT2, group.getCount() + " contact(s)");
			displayList.add(listItemMap);
		}
	}

	/**
	 * Checks if the naming of a new or editing of a group name is a valid name
	 * 
	 * @param newName name of group
	 * @return boolean true if name is valid, otherwise false
	 */
	private boolean checkGroupName(String newName) {

		// Check is input is not "" - invalid - show toast message
		if (newName.isEmpty()) {
			String displayString = "Invalid group name";
			Toast.makeText(EditGroupsActivity.this, displayString, Toast.LENGTH_LONG).show();
			return false;

			// Valid string
		} else {

			// Check if group already exists
			boolean groupAlreadyExists = false;
			for (Group group : groupList) {

				// If already exists then show toast message
				if (group.getName().compareTo(newName) == 0) {
					String displayString = newName + " already exists";
					Toast.makeText(EditGroupsActivity.this, displayString, Toast.LENGTH_LONG).show();
					groupAlreadyExists = true;
					break;
				}
			}
			return !groupAlreadyExists;
		}
	}

	/**
	 * Updates the contact Db when a contact gets assigned to a group
	 * 
	 * @param position int position of item on the list
	 */
	private void updateContactsDB(int position) {
		ContactsDatabaseHelper contactsDB = new ContactsDatabaseHelper(EditGroupsActivity.this);
		for (Contact contact : groupList.get(position).getGroupList()) {
			contactsDB.updateContact(contact);
		}
	}

	/**
	 * Refreshes the list view and sets view to the top of the list
	 */
	private void refreshListView() {
		((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
		listView.setSelection(0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch(requestCode) {
		case (EDIT_GROUP):
			// A group has been edited from the View group activity
			// deletion of contact from the this group
			if (resultCode == Activity.RESULT_OK) {
				// update the database, unassign any contact groups that have been removed, refresh, etc.
				ContactsDatabaseHelper contactsDB = new ContactsDatabaseHelper(EditGroupsActivity.this);
				groupList.clear();
				groupList.addAll(database.getAllGroups(contactsDB.getAllContacts()));
				setupDisplayList();
				refreshListView();
				setResult(Activity.RESULT_OK, new Intent()); // refresh spinner in AddEdit
			}
		break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_groups, menu);
		return true;
	}

}
