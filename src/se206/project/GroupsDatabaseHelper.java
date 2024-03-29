package se206.project;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * This class represents a database of the groups
 * 
 * @author Alan Lau, alau645, 2714269
 *
 */
public class GroupsDatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "GroupsDatabase.db";
	private static final String TABLE_GROUPS = "GroupsTable";

	private static final String GROUP_ID = "_id";
	private static final String GROUP_NAME = "name";

	private static final String CREATE_GROUPS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_GROUPS + " ("
			+ GROUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ GROUP_NAME + " TEXT);";
	private static final String DELETE_GROUPS_TABLE = "DROP TABLE IF EXISTS " + TABLE_GROUPS;

	public GroupsDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_GROUPS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DELETE_GROUPS_TABLE);
		onCreate(db);
	}

	/**
	 * Adds this group into this database
	 * 
	 * @param group
	 */
	public void addGroup(Group group) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(GroupsDatabaseHelper.GROUP_NAME, group.getName());

		db.insert(TABLE_GROUPS, null, values);
		db.close();
	}

	/**
	 * Deletes this group from this database
	 * 
	 * @param group
	 */
	public void deleteGroup(Group group) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_GROUPS, GROUP_ID + "=" + group.getID(), null);
		db.close();
	}

	/**
	 * Returns a list of all the groups in this database
	 * 
	 * @param contactList from contacts database
	 * @return List<Group>
	 */
	public List<Group> getAllGroups(List<Contact> contactList) {
		List<Group> groupList = new ArrayList<Group>();
		// Select All Query
		String buildSQL = "SELECT * FROM " + TABLE_GROUPS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(buildSQL, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Group group = new Group(cursor.getString(1));
				group.setID(Integer.parseInt(cursor.getString(0)));
				group.setGroupList(contactList);

				groupList.add(group);
			} while (cursor.moveToNext());
		}

		return groupList;
	}

	/**
	 * Returns a list of the names of all the groups in this database
	 * 
	 * @return List<String>
	 */
	public List<String> getAllGroupNames() {
		List<String> groupNameList = new ArrayList<String>();
		// Select All Query
		String buildSQL = "SELECT * FROM " + TABLE_GROUPS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(buildSQL, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				groupNameList.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}

		return groupNameList;
	}

	/**
	 * Updates this database with this group
	 * 
	 * @param group edited group
	 * @return int group ID
	 */
	public int updateGroup(Group group) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(GroupsDatabaseHelper.GROUP_NAME, group.getName());

		// updating row
		return db.update(TABLE_GROUPS, values, GROUP_ID + " = ?",
				new String[] { String.valueOf(group.getID()) });
	}
}
