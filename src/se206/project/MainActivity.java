package se206.project;

import java.util.ArrayList;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
/**
 * This class represents the main screen of the contacts manager application
 * 
 * @author Alan Lau, alau645, 2714269
 *
 */
public class MainActivity extends Activity {

	private static final int ADD_CONTACT = 1;
	protected static final int EDIT_CONTACT = 2;

	private ListView listView;
	private Button buttonAddContact;
	private Button buttonSort;
	private Button buttonSearch;
	private Button buttonGroup;
	private Button buttonAll;
	private List<Contact> contactList = new ArrayList<Contact>();
	private ContactsDatabaseHelper database = new ContactsDatabaseHelper(MainActivity.this);

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

		//database.addContact(new Contact("Alan", "Lau", "021 0210 0210", "09123456", "09654321", "myemail", "myhomeadd", "mydoa", "mygroup"));
		//database.addContact(new Contact("James", "Chen", "023 0230 0230", "09123456", "09654321", "myemail", "myhomeadd", "mydoa", "mygroup"));
		//database.addContact(new Contact("John", "Lee", "022 0220 0220", "09123456", "09654321", "myemail", "myhomeadd", "mydoa", "mygroup"));
		setupListView();

		// Button listener for adding a new contact. Starts the add contact activity
		buttonAddContact.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, AddEditContactActivity.class);
				intent.putExtra("Action", ADD_CONTACT); // extra info sent to the transitioning
				startActivityForResult(intent, ADD_CONTACT); // activity to determine whether it's add or edit
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
							if (which == 0) {
								Collections.sort(contactList, Contact.Comparators.FIRSTNAME);
							} else if (which == 1) {
								Collections.sort(contactList, Contact.Comparators.LASTNAME);
							} else if (which == 2) {
								Collections.sort(contactList, Contact.Comparators.MOBILEPH);
							}
							refreshListView();
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
						String value = searchInput.getText().toString();
						List<Contact> searchList = new ArrayList<Contact>();
						for (Contact c : contactList) {
							if (c.getFirstName().compareToIgnoreCase(value) == 0
								|| c.getLastName().compareToIgnoreCase(value) == 0
								|| c.getMobileph().compareTo(value) == 0) {
								searchList.add(c);
							}
						}
						if (searchList.isEmpty()) {
							String displayString = "0 results found: " + searchInput;
							Toast.makeText(MainActivity.this, displayString, Toast.LENGTH_LONG).show();
						//} else { TODO
						//	contactList.clear();
						//	contactList = searchList;
						//	refreshListView();
						}
					}
				});
				builder.create().show();
			}

		});

		// Button listener for group list contact view
		buttonGroup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO
				//((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
			}
		});
		
		// Button listener for showing all contacts (default view)
		buttonAll.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//contactList.clear();
				//contactList.addAll(database.getAllContacts());
				Collections.sort(contactList, Contact.Comparators.FIRSTNAME);
				refreshListView();
			}
		});

	}

	/**
	 * Sets up ListView using android.R.layout.simple_list_item_2
	 * 
	 */
	private void setupListView() {

		contactList = database.getAllContacts();

		Collections.sort(contactList, Contact.Comparators.FIRSTNAME);
		
		ContactArrayAdapter adapter = new ContactArrayAdapter(MainActivity.this, R.layout.main_listview_item, contactList);
		listView.setAdapter(adapter);

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
							Intent intent = new Intent();
							intent.setClass(MainActivity.this, ViewContactActivity.class);
							intent.putExtra("Contact", selectedContact);
							startActivity(intent);
						// Edit contact which will start the edit contact activity
						} else if (which == 1) {
							Intent intent = new Intent();
							intent.setClass(MainActivity.this, AddEditContactActivity.class);
							intent.putExtra("Action", EDIT_CONTACT);
							intent.putExtra("Contact", selectedContact);
							startActivityForResult(intent, EDIT_CONTACT);
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
									contactList.remove(clickedViewPos);
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
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch(requestCode) {
		case (ADD_CONTACT) : {
			if (resultCode == Activity.RESULT_OK) {
				Contact contact = (Contact) intent.getSerializableExtra("Contact");
				contactList.add(contact);
				database.addContact(contact);
				Collections.sort(contactList, Contact.Comparators.FIRSTNAME);
				refreshListView();
			}
			break;
		}
		case (EDIT_CONTACT) : {
			if (resultCode == Activity.RESULT_OK) {
				Contact contact = (Contact) intent.getSerializableExtra("Contact");
				database.updateContact(contact);
				contactList.clear();
				contactList.addAll(database.getAllContacts());
				Collections.sort(contactList, Contact.Comparators.FIRSTNAME);
				refreshListView();
			}
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
