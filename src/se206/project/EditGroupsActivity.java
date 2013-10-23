package se206.project;

import java.util.ArrayList;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
/**
 * This class represents the edit groups activity, which allows users to edit
 * all the groups currently in the contacts manager application.
 * 
 * @author Alan Lau, alau645, 2714269
 *
 */
public class EditGroupsActivity extends Activity {
	
	private Button buttonNewGroup;
	private ListView listView;
	private GroupsDatabaseHelper database = new GroupsDatabaseHelper(EditGroupsActivity.this);
	private List<Group> groupList = new ArrayList<Group>();
	private List<Map<String, String>> displayList =  new ArrayList<Map<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups);
		
		setupListView();
		
		buttonNewGroup = (Button)findViewById(R.id.groups_button_new);
		listView = (ListView)findViewById(R.id.groups_listview);
		
		// New group button which shows a dialog box allowing user to input a
		// new group name and then creates a new group
		buttonNewGroup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(EditGroupsActivity.this);
				builder.setTitle("Enter new group name:");
				final EditText input = new EditText(EditGroupsActivity.this);
				builder.setView(input);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Group group = new Group(input.getText().toString());
						groupList.add(group);
						database.addGroup(group);
						refreshListView();
					}
				});
				builder.create().show();
			}
				
		});
	}
	
	private void setupListView() {
		ContactsDatabaseHelper contactsDB = new ContactsDatabaseHelper(EditGroupsActivity.this);
		groupList = database.getAllGroups(contactsDB.getAllContacts());

		final String TEXT1 = "text1";
		final String TEXT2 = "text2";
		
		for(Group group : groupList) {
			final Map<String, String> listItemMap = new HashMap<String, String>();
			listItemMap.put(TEXT1, group.getName());
			listItemMap.put(TEXT2, "" + group.getCount());
			displayList.add(listItemMap);
		}
		
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
						if (which == 0) { // View group
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
									selectedGroup.setName(inputName.getText().toString());
									refreshListView();
								}
							});
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
									refreshListView();
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
