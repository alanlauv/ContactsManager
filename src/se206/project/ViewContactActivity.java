package se206.project;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
/**
 * This class represents the view contact activity of the contacts manager
 * application.
 * 
 * @author Alan Lau. alau645, 2714269
 *
 */
public class ViewContactActivity extends Activity {
	
	private ImageView imageView;
	private TextView mobilephTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_contact);

		Intent intent = getIntent();
		Contact contact = (Contact) intent.getSerializableExtra("Contact");

		setTitle(contact.getFullName());
		
		imageView = (ImageView)findViewById(R.id.view_image);
		mobilephTextView = (TextView)findViewById(R.id.view_phone_mobile);
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_contact, menu);
		return true;
	}

}
