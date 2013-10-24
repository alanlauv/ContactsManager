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

	private static final String TEXT1 = "text1";
	private static final String TEXT2 = "text2";
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
						if (groupName.isEmpty()) {
							String displayString = "Invalid group name";
							Toast.makeText(EditGroupsActivity.this, displayString, Toast.LENGTH_LONG).show();
						} else {
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

	private void setupListView() {
		ContactsDatabaseHelper contactsDB = new ContactsDatabaseHelper(EditGroupsActivity.this);
		groupList = database.getAllGroups(contactsDB.getAllContacts());

		setupDisplayList();

		final String[] fromMapKey = new String[] {TEXT1, TEXT2};
		int[] ids = {android.R.id.text1, android.R.id.text2};

		SimpleAdapter adapter = new SimpleAdapter(EditGroupsActivity.this, displayList,
				android.R.layout.simple_list_item_2, fromMapKey, ids);

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
						if (which == 0) { // View group TODO
							//Intent intent = new Intent();
							//intent.setClass(EditGroupsActivity.this, ViewGroupActivity.class);
							//intent.putExtra("Group", selectedGroup);
							//startActivity(intent);
						} else if (which == 1) { // Edit group name
							AlertDialog.Builder builder = new AlertDialog.Builder(EditGroupsActivity.this);

							builder.setTitle("Enter new group name:");
							final EditText inputName = new EditText(EditGroupsActivity.this);
							builder.setView(inputName);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									groupList.get(clickedViewPos).setName(inputName.getText().toString());
									database.updateGroup(groupList.get(clickedViewPos));
									setupDisplayList();
									refreshListView();
									setResult(Activity.RESULT_OK, new Intent()); // refresh spinner in AddEdit
								}
							});
							builder.create().show();
						} else if (which == 2) { // Delete group
							AlertDialog.Builder builder = new AlertDialog.Builder(EditGroupsActivity.this);
							builder.setTitle("Delete this group?");
							builder.setMessage("This cannot be undone!");
							builder.setNegativeButton("Cancel", null);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									database.deleteGroup(selectedGroup);
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

	private void setupDisplayList() {

		displayList.clear();
		Collections.sort(groupList);

		for(Group group : groupList) {
			final Map<String, String> listItemMap = new HashMap<String, String>();
			listItemMap.put(TEXT1, group.getName());
			listItemMap.put(TEXT2, group.getCount() + " contact(s)");
			displayList.add(listItemMap);
		}
	}

	private void refreshListView() {
		((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
		listView.setSelection(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_groups, menu);
		return true;
	}

}
