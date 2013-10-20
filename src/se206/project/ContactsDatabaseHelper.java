package se206.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactsDatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "ContactsDatabase.db";
	private static final String TABLE_CONTACTS = "ContactsTable";

	private static final String CONTACT_ID = "_id";
	private static final String CONTACT_FIRSTNAME = "firstName";
	private static final String CONTACT_LASTNAME = "lastName";
	private static final String CONTACT_MOBILEPH = "mobileph";
	private static final String CONTACT_HOMEPH = "homeph";
	private static final String CONTACT_WORKPH = "workph";
	private static final String CONTACT_EMAIL = "email";
	private static final String CONTACT_HOMEADD = "homeAdd";
	private static final String CONTACT_DOA = "doa";
	private static final String CONTACT_PHOTO = "photo";
	private static final String CONTACT_GROUP = "group";

	private static final String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + " ("
			+ CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ CONTACT_FIRSTNAME + " TEXT,"
			+ CONTACT_LASTNAME  + " TEXT,"
			+ CONTACT_MOBILEPH + " TEXT,"
			+ CONTACT_HOMEPH + " TEXT,"
			+ CONTACT_WORKPH + " TEXT,"
			+ CONTACT_EMAIL + " TEXT,"
			+ CONTACT_HOMEADD + " TEXT,"
			+ CONTACT_DOA + " TEXT,"
			//+ CONTACT_PHOTO + " TEXT,"
			+ CONTACT_GROUP + " TEXT);";
	private static final String DELETE_CONTACTS_TABLE = "DROP TABLE IF EXISTS " + TABLE_CONTACTS;

	public ContactsDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DELETE_CONTACTS_TABLE);
		onCreate(db);
	}

	public void addContact(Contact c) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(ContactsDatabaseHelper.CONTACT_FIRSTNAME, c.getFirstName());
		values.put(ContactsDatabaseHelper.CONTACT_LASTNAME, c.getLastName());
		values.put(ContactsDatabaseHelper.CONTACT_MOBILEPH, c.getMobileph());
		values.put(ContactsDatabaseHelper.CONTACT_HOMEPH, c.getHomeph());
		values.put(ContactsDatabaseHelper.CONTACT_WORKPH, c.getWorkph());
		values.put(ContactsDatabaseHelper.CONTACT_EMAIL, c.getEmail());
		values.put(ContactsDatabaseHelper.CONTACT_HOMEADD, c.getHomeph());
		values.put(ContactsDatabaseHelper.CONTACT_DOA, c.getDoa());
		//values.put(ContactsDatabaseHelper.CONTACT_PHOTO, c.getPhoto());
		values.put(ContactsDatabaseHelper.CONTACT_GROUP, c.getGroup());

		db.insert(TABLE_CONTACTS, null, values);
		db.close();
	}

	public void deleteContact(Contact c) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, CONTACT_ID + " = ?",
				new String[] { String.valueOf(c.getID()) });
		db.close();
	}

	public Contact getContact(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CONTACTS, new String[] { CONTACT_ID,
				CONTACT_FIRSTNAME, CONTACT_LASTNAME, CONTACT_MOBILEPH,
				CONTACT_HOMEPH, CONTACT_WORKPH, CONTACT_EMAIL, CONTACT_HOMEADD,
				CONTACT_DOA, CONTACT_GROUP }, CONTACT_ID + "=?",
						new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Contact contact = new Contact(cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5), cursor.getString(6), cursor.getString(7),
				cursor.getString(8), cursor.getString(9));

		return contact;
	}
}
