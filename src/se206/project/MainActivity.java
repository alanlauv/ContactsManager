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
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
/**
 * This class represents the main screen of the contacts manager application. Has two
 * different views - Group and All.
 * 
 * @author Alan Lau, alau645, 2714269
 *
 */
public class MainActivity extends Activity {

	// Values used for passing between activities through intents
	private static final int ADD_CONTACT = 1;
	protected static final int EDIT_CONTACT = 2;
	private static final int EDIT_GROUPS = 3;

	// Animation for button click. Makes it go opaque when clicked
	protected static final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);

	// Check for whether currently in Group or All view.
	private boolean isGroupView = false;

	private ListView listView;
	private ImageButton buttonAddContact, buttonSort, buttonSearch, buttonGroup, buttonAll;

	// List used to feed data into adapter
	private List<Contact> contactList = new ArrayList<Contact>();
	private ContactsDatabaseHelper database = new ContactsDatabaseHelper(MainActivity.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView)findViewById(R.id.main_listview);
		buttonAddContact = (ImageButton)findViewById(R.id.main_button_add);
		buttonSort = (ImageButton)findViewById(R.id.main_button_sort);
		buttonSearch = (ImageButton)findViewById(R.id.main_button_search);
		buttonGroup = (ImageButton)findViewById(R.id.main_button_group);
		buttonAll = (ImageButton)findViewById(R.id.main_button_all);

		setupListView();

		// Button listener for adding a new contact. Starts the add contact activity
		buttonAddContact.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				v.startAnimation(buttonClick);

				// Start add activity with extra info sent to the transitioning
				// activity to determine whether it's add or edit
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, AddEditContactActivity.class);
				intent.putExtra("Action", ADD_CONTACT);
				startActivityForResult(intent, ADD_CONTACT);
			}
		});

		// Button listener for sorting the contacts list. Shows a dialog box of sorting options
		buttonSort.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(buttonClick);

				// Check if currently in Group view, then start EditGroups activity
				// instead of sorting - sorting removed from Group view
				if (listView.getAdapter().getClass() == GroupArrayAdapter.class) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, EditGroupsActivity.class);
					startActivityForResult(intent, EDIT_GROUPS);

					// Currently in All view so start sorting dialog
				} else {
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

								// Set isGroupView to false - just in case - then refresh view
								isGroupView = false;
								refreshListView();
							}
						}

					});
					builder.create().show();
				}
			}

		});

		// Button listener for searching a contact. Shows a dialog box for user input
		buttonSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(buttonClick);

				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

				builder.setTitle("Search contacts:");
				builder.setMessage("Input name or number");
				final EditText searchInput = new EditText(MainActivity.this);
				builder.setView(searchInput);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String value = searchInput.getText().toString();

						// Check string is "" - invalid - then toast message user
						if (value.isEmpty()) {
							String displayString = "Invalid search input";
							Toast.makeText(MainActivity.this, displayString, Toast.LENGTH_LONG).show();

							// Valid input string.
						} else {
							// Reset and get fresh data before search
							contactList.clear();
							contactList.addAll(database.getAllContacts());

							// Search, and add into another contact list
							List<Contact> searchList = new ArrayList<Contact>();
							for (Contact c : contactList) {
								if (c.getFirstName().compareToIgnoreCase(value) == 0
										|| c.getLastName().compareToIgnoreCase(value) == 0
										|| c.getMobileph().compareTo(value) == 0) {
									searchList.add(c);
								}
							}

							// Display how many results were found to user
							String displayString = searchList.size() + " result(s) found for " + value;
							Toast.makeText(MainActivity.this, displayString, Toast.LENGTH_LONG).show();

							// Results found, clear data list and add the results into it
							if (!searchList.isEmpty()) {
								contactList.clear();
								contactList.addAll(searchList);
								isGroupView = false;
								refreshListView();
							}
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
				v.startAnimation(buttonClick);

				// Set GroupArrayAdapter with no data - refreshListView sets the data
				isGroupView = true;
				listView.setAdapter(new GroupArrayAdapter(MainActivity.this,
						R.layout.main_group_listview_item, new ArrayList<Contact>()));
				refreshListView();
			}
		});

		// Button listener for showing all contacts (default view)
		buttonAll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(buttonClick);

				// Reset and get fresh data and sort by first name
				contactList.clear();
				contactList.addAll(database.getAllContacts());
				Collections.sort(contactList, Contact.Comparators.FIRSTNAME);
				isGroupView = false;
				refreshListView();
			}
		});

	}

	/**
	 * Sets up ListView using a custom ArrayAdapter, default view is All view
	 * 
	 */
	private void setupListView() {

		// Get data and sort by first name - default
		contactList = database.getAllContacts();
		Collections.sort(contactList, Contact.Comparators.FIRSTNAME);

		ContactArrayAdapter adapter = new ContactArrayAdapter(MainActivity.this,
				R.layout.main_listview_item, contactList);
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

	/**
	 * Refreshes the listview to the changed underlying data set, and returns
	 * the view back to the top of the list.
	 * 
	 * In all view: sort button
	 * In group view: sort button -> add group button
	 */
	private void refreshListView() {
		// Check if currently in Group view
		if (isGroupView && listView.getAdapter().getClass() == GroupArrayAdapter.class) {
			// Change image of sort button to add group button image
			buttonSort.setImageResource(R.drawable.contact_group_add);

			// Get contacts from all groups and feed into data list
			// not all contacts will show (contacts without a group)
			GroupsDatabaseHelper groupDB = new GroupsDatabaseHelper(MainActivity.this);
			List<Group> groupList = groupDB.getAllGroups(contactList);
			Collections.sort(groupList);
			contactList.clear();
			for (Group group : groupList) {
				contactList.addAll(group.getGroupList());
			}
			GroupArrayAdapter adapter = new GroupArrayAdapter(MainActivity.this, R.layout.main_group_listview_item, contactList);
			listView.setAdapter(adapter);

			// Change view from Group to All
		} else if (!isGroupView && listView.getAdapter().getClass() == GroupArrayAdapter.class) {
			// Change image button back to sort
			buttonSort.setImageResource(R.drawable.sort_icon);
			ContactArrayAdapter adapter = new ContactArrayAdapter(MainActivity.this, R.layout.main_listview_item, contactList);
			listView.setAdapter(adapter);

			// Normal update, no change in views
		} else {
			((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
		}

		// Return view to top of list
		listView.setSelection(0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch(requestCode) {
		// Return from Add contact activity with changes
		case (ADD_CONTACT):
			if (resultCode == Activity.RESULT_OK) {
				// Get contact and its ID and add into data list
				Contact contact = (Contact) intent.getSerializableExtra("Contact");
				int id = database.addContact(contact);
				contact.setID(id);
				contactList.add(contact);

				// Check if contact assigned to a group. If not then should go to All view so
				// set isGroupView to false so contact can be shown on All view
				if (contact.getGroup() == null) {
					contactList.clear();
					contactList.addAll(database.getAllContacts());
					Collections.sort(contactList, Contact.Comparators.FIRSTNAME);
					isGroupView = false;
				}
				refreshListView();
			}
		break;
		// Return from Edit contact activity with changes
		case (EDIT_CONTACT):
			if (resultCode == Activity.RESULT_OK) {
				// Get contact and regular updates to database and data list
				Contact contact = (Contact) intent.getSerializableExtra("Contact");
				database.updateContact(contact);
				contactList.clear();
				contactList.addAll(database.getAllContacts());
				Collections.sort(contactList, Contact.Comparators.FIRSTNAME);
				refreshListView();
			}
		break;
		// Return from edit groups activity with changes
		case (EDIT_GROUPS):
			if (resultCode == Activity.RESULT_OK) {
				// Should be in Group view, refresh data list to see changes
				contactList.clear();
				contactList.addAll(database.getAllContacts());
				refreshListView();
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
