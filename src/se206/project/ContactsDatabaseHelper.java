package se206.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactsDatabaseHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "ContactsDatabase.db";
	public static final String TABLE_CONTACTS = "ContactsTable";
	
	private static final String CONTACT_ID = "_id";
	private static final String CONTACT_FIRSTNAME = "firstName";
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
