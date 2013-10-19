package se206.project;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
	private ListOfGroups listOfGroups = new ListOfGroups();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups);
		
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
						//String groupName =input.toString();
						
					}
				});
				builder.create().show();
			}
				
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_groups, menu);
		return true;
	}

}
