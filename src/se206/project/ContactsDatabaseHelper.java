package se206.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactsDatabaseHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "ContactsDatabase.db";
	public static final String TABLE_CONTACTS = "ContactsTable";
	
	public static final String CONTACT_ID = "_id";
	public static final String CONTACT_FIRSTNAME = "firstName";
	public static final String CONTACT_LASTNAME = "lastName";
	public static final String CONTACT_MOBILEPH = "mobileph";
	public static final String CONTACT_HOMEPH = "homeph";
	public static final String CONTACT_WORKPH = "workph";
	public static final String CONTACT_EMAIL = "email";
	public static final String CONTACT_HOMEADD = "homeAdd";
	public static final String CONTACT_DOA = "doa";
	public static final String CONTACT_PHOTO = "photo";
	public static final String CONTACT_GROUP = "group";
	
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
			+ CONTACT_PHOTO + " TEXT,"
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
}
