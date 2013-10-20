package se206.project;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ContactsDatabase {

	private SQLiteDatabase database;
	private ContactsDatabaseHelper dbHelper;
	
	public ContactsDatabase(Context context) {
		dbHelper = new ContactsDatabaseHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void insertContact(Contact c) {
		
	}
}
