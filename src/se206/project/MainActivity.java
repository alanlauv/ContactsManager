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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
/**
 * This class represents the main screen of the contacts manager application
 * 
 * @author Alan Lau, alau645, 2714269
 *
 */
public class MainActivity extends Activity {

	private ListView listView;
	private Button buttonAddContact;
	private Button buttonSort;
	private Button buttonSearch;
	private Button buttonGroup;
	private Button buttonAll;
	private List<Contact> contactList = new ArrayList<Contact>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView)findViewById(R.id.main_listview);
		buttonAddContact = (Button)findViewById(R.id.main_button_add);
		buttonSort = (Button)findViewById(R.id.main_button_sort);
		buttonSearch = (Button)findViewById(R.id.main_button_search);
		buttonGroup = (Button)findViewById(R.id.main_button_group);
		buttonAll = (Button)findViewById(R.id.main_button_all);

		//buttonAll.setEnabled(true);

		ContactsDatabaseHelper database = new ContactsDatabaseHelper(MainActivity.this);
		database.addContact(new Contact("Alan", "Lau", "021 0210 0210", "09123456", "09654321", "myemail", "myhomeadd", "mydoa", "mygroup"));
		database.addContact(new Contact("James", "Chen", "023 0230 0230", "09123456", "09654321", "myemail", "myhomeadd", "mydoa", "mygroup"));
		database.addContact(new Contact("John", "Lee", "022 0220 0220", "09123456", "09654321", "myemail", "myhomeadd", "mydoa", "mygroup"));
		setupListView(database);

		// Button listener for adding a new contact. Starts the add contact activity
		buttonAddContact.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, AddEditContactActivity.class);
				intent.putExtra("Action", "add"); // extra info sent to the transitioning
				startActivity(intent);			  // activity to determine whether it's add or edit

			}
		});

		// Button listener for sorting the contacts list. Shows a dialog box of sorting options
		buttonSort.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

				builder.setTitle("Sort contacts by:");
				String[] sortOptions = {"First name", "Last name", "Mobile number"};
				builder.setItems(sortOptions, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// The 'which' argument contains the index position
						// of the selected item
						Collections.sort(contactList, Contact.Comparators.LASTNAME);
						
						((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
					}

				});
				builder.create().show();
			}

		});

		// Button listener for searching a contact. Shows a dialog box for user input
		buttonSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

				builder.setTitle("Search contacts:");
				builder.setMessage("Input name or number");
				final EditText searchInput = new EditText(MainActivity.this);
				builder.setView(searchInput);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//String value = searchInput.toString();

					}
				});
				builder.create().show();
			}

		});

	}

	private void setupListView(ContactsDatabaseHelper database) {
		// large amount of stub contact data for testing and formatting
		List<Map<String, String>> aList =  new ArrayList<Map<String, String>>();
		contactList = database.getAllContacts();

		final String TEXT1 = "text1";
		final String TEXT2 = "text2";
		final String[] fromMapKey = new String[] {TEXT1, TEXT2};
		int[] ids = {android.R.id.text1, android.R.id.text2};

		for(Contact c : contactList) {
			final Map<String, String> listItemMap = new HashMap<String, String>();
			listItemMap.put(TEXT1, c.getFullName());
			listItemMap.put(TEXT2, c.getMobileph());
			aList.add(listItemMap);
		}

		/*
		final Map<String, String> listItemMap1 = new HashMap<String, String>();
		listItemMap1.put(TEXT1, "Alan Lau");
		listItemMap1.put(TEXT2, "021 0210 2121");
		aList.add(listItemMap1);
		final Map<String, String> listItemMap2 = new HashMap<String, String>();
		listItemMap2.put(TEXT1, "Alex Ander");
		listItemMap2.put(TEXT2, "022 0270 2727");
		aList.add(listItemMap2);
		final Map<String, String> listItemMap3 = new HashMap<String, String>();
		listItemMap3.put(TEXT1, "Joe Bloggs");
		listItemMap3.put(TEXT2, "022 0220 2222");
		aList.add(listItemMap3);
		 */

		// Using the simple_list_item_2 layout to show Name at top line and phone number
		// at the bottom line
		SimpleAdapter la = new SimpleAdapter(MainActivity.this, aList,
				android.R.layout.simple_list_item_2, fromMapKey, ids);
		listView.setAdapter(la);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			// When user clicks on a contact on the list a dialog box will show with three
			// options: view contact, edit contact, delete contact
			@Override
			public void onItemClick(AdapterView<?> parentView, View clickedView,
					int clickedViewPos, long id) {

				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

				Contact selectedContact = contactList.get(clickedViewPos);
				builder.setTitle(selectedContact.getFullName());
				String[] contactOptions = {"View contact", "Edit contact", "Delete contact"};
				builder.setItems(contactOptions, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// View contact which start the view contact activity
						if (which == 0) {
							Intent intent = new Intent();
							intent.setClass(MainActivity.this, ViewContactActivity.class);
							startActivity(intent);
							// Edit contact which will start the edit contact activity
						} else if (which == 1) {
							Intent intent = new Intent();
							intent.setClass(MainActivity.this, AddEditContactActivity.class);
							intent.putExtra("Action", "edit");
							startActivity(intent);
							// Delete contact which will show a dialog box asking user to confirm deletion
							// of the selected contact
						} else if (which == 2) {
							AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
							builder.setTitle("Delete this contact?");
							builder.setMessage("This cannot be undone!");
							builder.setNegativeButton("Cancel", null);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									
									//((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
