package se206.project;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
/**
 * This class represents the view contact activity of the contacts manager
 * application.
 * 
 * @author Alan Lau. alau645, 2714269
 *
 */
public class ViewContactActivity extends Activity {
	
	private ImageView imageView;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_contact);
		
		setTitle("Alan Lau");
		
		imageView = (ImageView)findViewById(R.id.view_image);
		listView = (ListView)findViewById(R.id.view_listview);
		
		// stub contact
		Contact c = new Contact("Alan", "Lau", "021 0210 2121", "", "", "", "", "", "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_contact, menu);
		return true;
	}

}
