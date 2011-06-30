package com.contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdaptor {
	
	public static final String KEY_ROW_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_NUMBER = "number";
	
	public static final String DATABASE_NAME = "contacts";
	public static final String DATABASE_TABLE = "contacts";
	public static final int DATABASE_VERSION = 1;
	
	public static final String DATABASE_CREATE = 
		"create table contacts (_id integer primary key autoincrement, " +
		"name text not null, number text not null);";
	
	@SuppressWarnings("unused")
	private final Context context;
	
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public DBAdaptor(Context ctx){
		this.context = ctx;
		DBHelper = new DatabaseHelper(ctx);
		
	}
	
	public DBAdaptor open() throws SQLException{
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		DBHelper.close();
	}
	
	public long insertContact(String name, String number){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_NUMBER, number);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	public Cursor getAllContacts(){
		return db.query(DATABASE_TABLE, new String[]{KEY_ROW_ID,KEY_NAME,KEY_NUMBER}, 
				null, null, null, null, null);
	}
	
	public boolean deleteContact(long rowId){
		return db.delete(DATABASE_TABLE, KEY_ROW_ID + "=" + rowId, null) > 0;
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		
		public DatabaseHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
			onCreate(db);
		}	
	}
}
