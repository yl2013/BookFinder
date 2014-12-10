package com.bookfinder;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MyListDataSource {
	private SQLiteDatabase database;
	private BookFinderSQLHelper dbHelper;
	private String[] allColumns = { BookFinderSQLHelper.COLUMN_NAME_LIST_ID,
			BookFinderSQLHelper.COLUMN_NAME_CATEGORYID, BookFinderSQLHelper.COLUMN_NAME_BOOKID };

	public MyListDataSource(Context context) {
		dbHelper = new BookFinderSQLHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public MyListInfo createMyList(int categoryID, int bookID) {
		ContentValues values = new ContentValues();

		values.put(BookFinderSQLHelper.COLUMN_NAME_CATEGORYID, categoryID);
		values.put(BookFinderSQLHelper.COLUMN_NAME_BOOKID, bookID);
		//Log.v("test", "getMyList: " + bookID);
		long insertId = database.insert(BookFinderSQLHelper.TABLE_MYLIST, null,
				values);
		//Log.v("test", "ListID" + insertId);
		Cursor cursor = database.query(BookFinderSQLHelper.TABLE_MYLIST,
				allColumns, BookFinderSQLHelper.COLUMN_NAME_LIST_ID + " = "
						+ insertId, null, null, null, null);
		cursor.moveToFirst();
		MyListInfo myListInfo = cursorToListInfo(cursor);
		cursor.close();
		return myListInfo;
	}

	public void deleteMyList(int categoryID, int bookID) {

		database.delete(BookFinderSQLHelper.TABLE_MYLIST,
				BookFinderSQLHelper.COLUMN_NAME_CATEGORYID + " = " + categoryID 
				+ " and " + BookFinderSQLHelper.COLUMN_NAME_BOOKID + "=" + bookID, null);
	}
	
	public void clearMyList()
	{
		database.delete(BookFinderSQLHelper.TABLE_MYLIST,
				null, null);
	}
	
	public void deleteCategory(int categoryID) {

		database.delete(BookFinderSQLHelper.TABLE_MYLIST,
				BookFinderSQLHelper.COLUMN_NAME_CATEGORYID + " = " + categoryID, null);
	}

	public ArrayList<MyListInfo> getMyList(int categoryID) {
		ArrayList<MyListInfo> list = new ArrayList<MyListInfo>();
		String strFilter = BookFinderSQLHelper.COLUMN_NAME_CATEGORYID + " = "
				+ categoryID;
		Cursor cursor = database.query(BookFinderSQLHelper.TABLE_MYLIST,
				allColumns, strFilter, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			MyListInfo listInfo = cursorToListInfo(cursor);
			//Log.v("test", "MyListInfo: " + listInfo.getListID());
			list.add(listInfo);
			cursor.moveToNext();
		}

		cursor.close();
		return list;
	}

	public void updateCategory(int categoryID, int bookID) {
		String strFilter = BookFinderSQLHelper.COLUMN_NAME_BOOKID + "="
				+ bookID;
		ContentValues values = new ContentValues();

		values.put(BookFinderSQLHelper.COLUMN_NAME_CATEGORYID, categoryID);

		database.update(BookFinderSQLHelper.TABLE_MYLIST, values, strFilter, null);
	}

	private MyListInfo cursorToListInfo(Cursor cursor) {
		MyListInfo listInfo = new MyListInfo();
		listInfo.setListID(cursor.getInt(0));
		listInfo.setCategoryID(cursor.getInt(1));
		listInfo.setBookID(cursor.getInt(2));

		return listInfo;
	}
}
