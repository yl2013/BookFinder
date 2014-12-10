package com.bookfinder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BookFinderSQLHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "bookfinder.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_CATEGORY = "categoryInfo";
	public static final String COLUMN_NAME_CATEGORY_ID = "categoryid";
	public static final String COLUMN_NAME_CATEGORYNAME = "categoryname";



	private static final String CREATE_CATEGORY = "create table " + TABLE_CATEGORY
			+ "(" + COLUMN_NAME_CATEGORY_ID
			+ " integer primary key autoincrement, " 
			+ COLUMN_NAME_CATEGORYNAME + " text not null " 
			+ ");";
	
	public static final String TABLE_BOOK = "bookinfo";
	public static final String COLUMN_NAME_BOOK_ID = "bookid";
	public static final String COLUMN_NAME_BOOKNAME = "bookname";
	public static final String COLUMN_NAME_AUTHOR = "author";
	public static final String COLUMN_NAME_VERSION = "version";
	public static final String COLUMN_NAME_BOOKIMAGE = "bookImage";
	public static final String COLUMN_NAME_DESCRIPTION = "description";
	public static final String COLUMN_NAME_LEVEL = "level";
	public static final String COLUMN_NAME_SECTION = "section";
	public static final String COLUMN_NAME_SHELVE = "shelve";
	public static final String COLUMN_NAME_BOOKNUMBER = "bookNumber";
	public static final String COLUMN_NAME_REFCODE = "nfcReferencecode";

	private static final String CREATE_BOOK = "create table " + TABLE_BOOK
			+ "(" + COLUMN_NAME_BOOK_ID
			+ " integer primary key autoincrement, " 
			+ COLUMN_NAME_BOOKNAME + " text not null, " 
			+ COLUMN_NAME_AUTHOR + " text not null, "
			+ COLUMN_NAME_VERSION + " text not null, "
			+ COLUMN_NAME_BOOKIMAGE + " text not null, "
			+ COLUMN_NAME_DESCRIPTION + " text not null, "
			+ COLUMN_NAME_LEVEL + " text not null, "
			+ COLUMN_NAME_SECTION + " text not null, "
			+ COLUMN_NAME_SHELVE + " text not null, "
			+ COLUMN_NAME_BOOKNUMBER + " text not null, "
			+ COLUMN_NAME_REFCODE + " text not null "
			+ ");";

	
	public static final String TABLE_MYLIST = "mylistinfo";
	public static final String COLUMN_NAME_LIST_ID = "listid";
	public static final String COLUMN_NAME_BOOKID = "bookid";
	public static final String COLUMN_NAME_CATEGORYID = "categoryid";

	private static final String CREATE_MYLIST = "create table " + TABLE_MYLIST
			+ "(" + COLUMN_NAME_LIST_ID
			+ " integer primary key autoincrement, " 
			+ COLUMN_NAME_CATEGORYID + " integer not null, "
			+ COLUMN_NAME_BOOKID + " integer not null " 
			+ ");";
	
	public BookFinderSQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_CATEGORY);
		database.execSQL(CREATE_BOOK);
		database.execSQL(CREATE_MYLIST);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(BookFinderSQLHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYLIST);
		onCreate(db);
	}

}
