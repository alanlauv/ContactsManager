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
	
	private static final int SORT_FIRSTNAME = 0;
	private static final int SORT_LASTNAME = 1;
	private static final int SORT_MOBILEPH = 2;
	private static final String TEXT1 = "text1";
	private static final String TEXT2 = "text2";

	private ListView listView;
	private Button buttonAddContact;
	private Button buttonSort;
	private Button buttonSearch;
	private Button buttonGroup;
	private Button buttonAll;
	private List<Contact> contactList = new ArrayList<Contact>();
	private List<Map<String, String>> displayList =  new ArrayList<Map<String, String>>();

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
		//database.addContact(new Contact("Alan", "Lau", "021 0210 0210", "09123456", "09654321", "myemail", "myhomeadd", "mydoa", "mygroup"));
		//database.addContact(new Contact("James", "Chen", "023 0230 0230", "09123456", "09654321", "myemail", "myhomeadd", "mydoa", "mygroup"));
		//database.addContact(new Contact("John", "Lee", "022 0220 0220", "09123456", "09654321", "myemail", "myhomeadd", "mydoa", "mygroup"));
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
						if (which >= 0 && which <= 2) {
							sortDisplayList(which);
							((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
						}
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

		// Button listener for group list contact view
		buttonGroup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				//((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
			}
		});
		
		// Button listener for showing all contacts (default view)
		buttonAll.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sortDisplayList(0);
				((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
			}
		});

	}

	private void setupListView(final ContactsDatabaseHelper database) {
		
		contactList = database.getAllContacts();

		final String[] fromMapKey = new String[] {TEXT1, TEXT2};
		int[] ids = {android.R.id.text1, android.R.id.text2};

		sortDisplayList(1);

		// Using the simple_list_item_2 layout to show Name at top line and phone number
		// at the bottom line
		SimpleAdapter la = new SimpleAdapter(MainActivity.this, displayList,
				android.R.layout.simple_list_item_2, fromMapKey, ids);
		listView.setAdapter(la);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			// When user clicks on a contact on the list a dialog box will show with three
			// options: view contact, edit contact, delete contact
			@Override
			public void onItemClick(AdapterView<?> parentView, View clickedView,
					final int clickedViewPos, long id) {

				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

				final Contact selectedContact = contactList.get(clickedViewPos);

				builder.setTitle(selectedContact.getFullName());
				String[] contactOptions = {"View contact", "Edit contact", "Delete contact"};
				builder.setItems(contactOptions, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// View contact which start the view contact activity
						if (which == 0) {
							Intent intent = new Intent("se206.project.ViewContactActivity");
							intent.setClass(MainActivity.this, ViewContactActivity.class);
							intent.putExtra("Contact", selectedContact);
							startActivity(intent);
						// Edit contact which will start the edit contact activity
						} else if (which == 1) {
							Intent intent = new Intent();
							intent.setClass(MainActivity.this, AddEditContactActivity.class);
							intent.putExtra("Action", "edit");
							intent.putExtra("Contact", selectedContact);
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
									database.deleteContact(selectedContact);
									displayList.remove(clickedViewPos);
									contactList.remove(clickedViewPos);
									((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
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
	
	private void sortDisplayList(int sortType) {

		displayList.clear();

		if (sortType == SORT_FIRSTNAME) {
			Collections.sort(contactList, Contact.Comparators.FIRSTNAME);
			for(Contact c : contactList) {
				final Map<String, String> listItemMap = new HashMap<String, String>();
				listItemMap.put(TEXT1, c.getFullName());
				listItemMap.put(TEXT2, c.getMobileph());
				displayList.add(listItemMap);
			}
		} else if (sortType == SORT_LASTNAME) {
			Collections.sort(contactList, Contact.Comparators.LASTNAME);
			for(Contact c : contactList) {
				final Map<String, String> listItemMap = new HashMap<String, String>();
				listItemMap.put(TEXT1, c.getLastName() + " " + c.getFirstName());
				listItemMap.put(TEXT2, c.getMobileph());
				displayList.add(listItemMap);
			}
		} else if (sortType == SORT_MOBILEPH) {
			Collections.sort(contactList, Contact.Comparators.MOBILEPH);
			for(Contact c : contactList) {
				final Map<String, String> listItemMap = new HashMap<String, String>();
				listItemMap.put(TEXT1, c.getFullName());
				listItemMap.put(TEXT2, c.getMobileph());
				displayList.add(listItemMap);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
