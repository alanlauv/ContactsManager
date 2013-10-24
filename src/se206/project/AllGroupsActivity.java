package se206.project;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
import android.widget.ListView;

public class AllGroupsActivity extends Activity {
	
	private ListView listView;
	private Button buttonAddContact;
	private Button buttonEditGroups;
	private Button buttonSearch;
	private Button buttonGroup;
	private Button buttonAll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_groups);
		
		listView = (ListView)findViewById(R.id.all_groups_listview);
		buttonAddContact = (Button)findViewById(R.id.all_groups_button_add);
		buttonEditGroups = (Button)findViewById(R.id.all_groups_button_edit);
		buttonSearch = (Button)findViewById(R.id.all_groups_button_search);
		buttonGroup = (Button)findViewById(R.id.all_groups_button_group);
		buttonAll = (Button)findViewById(R.id.all_groups_button_all);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.all_groups, menu);
		return true;
	}

}
