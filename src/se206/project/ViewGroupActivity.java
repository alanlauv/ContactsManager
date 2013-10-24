package se206.project;

import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.ListView;

public class ViewGroupActivity extends Activity {

	private ListView listView;

	private Group group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_group);

		Intent intent = getIntent();
		group = (Group) intent.getSerializableExtra("Group");

		setTitle(group.getName());
		listView = (ListView)findViewById(R.id.view_group_listview);

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_group, menu);
		return true;
	}

}
