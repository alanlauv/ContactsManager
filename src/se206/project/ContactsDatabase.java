package se206.project;

import android.content.ContentValues;
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
		ContentValues values = new ContentValues();
		values.put(ContactsDatabaseHelper.CONTACT_FIRSTNAME, c.getFirstName());
		values.put(ContactsDatabaseHelper.CONTACT_LASTNAME, c.getLastName());
		values.put(ContactsDatabaseHelper.CONTACT_MOBILEPH, c.getMobileph());
		values.put(ContactsDatabaseHelper.CONTACT_HOMEPH, c.getHomeph());
		values.put(ContactsDatabaseHelper.CONTACT_PHOTO, c.getWorkph());
		values.put(ContactsDatabaseHelper.CONTACT_EMAIL, c.getEmail());
		values.put(ContactsDatabaseHelper.CONTACT_HOMEADD, c.getHomeph());
		values.put(ContactsDatabaseHelper.CONTACT_DOA, c.getDoa());
		//values.put(ContactsDatabaseHelper.CONTACT_PHOTO, c.getPhoto());
		values.put(ContactsDatabaseHelper.CONTACT_GROUP, c.getGroup());
		
		database.insert(dbHelper.TABLE_CONTACTS, null, values);
	}
}
