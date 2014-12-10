package com.bookfinder;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BookDataSource {
	private SQLiteDatabase database;
	private BookFinderSQLHelper dbHelper;
	private String[] allColumns = { BookFinderSQLHelper.COLUMN_NAME_BOOK_ID,
			BookFinderSQLHelper.COLUMN_NAME_BOOKNAME,
			BookFinderSQLHelper.COLUMN_NAME_AUTHOR,
			BookFinderSQLHelper.COLUMN_NAME_VERSION,
			BookFinderSQLHelper.COLUMN_NAME_BOOKIMAGE,
			BookFinderSQLHelper.COLUMN_NAME_DESCRIPTION,
			BookFinderSQLHelper.COLUMN_NAME_LEVEL,
			BookFinderSQLHelper.COLUMN_NAME_SECTION,
			BookFinderSQLHelper.COLUMN_NAME_SHELVE,
			BookFinderSQLHelper.COLUMN_NAME_BOOKNUMBER,
			BookFinderSQLHelper.COLUMN_NAME_REFCODE };

	public BookDataSource(Context context) {
		dbHelper = new BookFinderSQLHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public BookInfo createBook(String bookName, String author, String version,
			String bookImage, String description, String level, String section,
			String shelve, String bookNumber, String nfcReferencecode) {
		ContentValues values = new ContentValues();

		values.put(BookFinderSQLHelper.COLUMN_NAME_BOOKNAME, bookName);
		values.put(BookFinderSQLHelper.COLUMN_NAME_AUTHOR, author);
		values.put(BookFinderSQLHelper.COLUMN_NAME_VERSION, version);
		values.put(BookFinderSQLHelper.COLUMN_NAME_BOOKIMAGE, bookImage);
		values.put(BookFinderSQLHelper.COLUMN_NAME_DESCRIPTION, description);
		values.put(BookFinderSQLHelper.COLUMN_NAME_LEVEL, level);
		values.put(BookFinderSQLHelper.COLUMN_NAME_SECTION, section);
		values.put(BookFinderSQLHelper.COLUMN_NAME_SHELVE, shelve);
		values.put(BookFinderSQLHelper.COLUMN_NAME_BOOKNUMBER, bookNumber);
		values.put(BookFinderSQLHelper.COLUMN_NAME_REFCODE, nfcReferencecode);

		long insertId = database.insert(BookFinderSQLHelper.TABLE_BOOK, null,
				values);
		// Log.v("test", insertId+"");
		Cursor cursor = database.query(BookFinderSQLHelper.TABLE_BOOK,
				allColumns, BookFinderSQLHelper.COLUMN_NAME_BOOK_ID + " = "
						+ insertId, null, null, null, null);
		cursor.moveToFirst();
		BookInfo bookInfo = cursorToBookInfo(cursor);
		cursor.close();
		return bookInfo;
	}

	public void deleteCategory(BookInfo bookInfo) {
		int id = bookInfo.getBookID();

		database.delete(BookFinderSQLHelper.TABLE_BOOK,
				BookFinderSQLHelper.COLUMN_NAME_BOOK_ID + " = " + id, null);
	}

	public BookInfo getBook(int bookID) {
		String strFilter = BookFinderSQLHelper.COLUMN_NAME_BOOK_ID + " = "
				+ bookID;
		Cursor cursor = database.query(BookFinderSQLHelper.TABLE_BOOK,
				allColumns, strFilter, null, null, null, null);

		cursor.moveToFirst();
		BookInfo bookInfo = cursorToBookInfo(cursor);
		cursor.close();

		return bookInfo;
	}

	public BookInfo getBookByNFC(String nfcCode) {
		String strFilter = BookFinderSQLHelper.COLUMN_NAME_REFCODE + " like '"
				+ nfcCode + "'";
		Cursor cursor = database.query(BookFinderSQLHelper.TABLE_BOOK,
				allColumns, strFilter, null, null, null, null);

		cursor.moveToFirst();
		BookInfo bookInfo = cursorToBookInfo(cursor);
		cursor.close();

		return bookInfo;
	}

	public ArrayList<BookInfo> getMyList(ArrayList<MyListInfo> myListInfoList) {
		ArrayList<BookInfo> list = new ArrayList<BookInfo>();
		for (MyListInfo myList : myListInfoList) {
			String strFilter = BookFinderSQLHelper.COLUMN_NAME_BOOK_ID + " = "
					+ myList.getBookID();
			Cursor cursor = database.query(BookFinderSQLHelper.TABLE_BOOK,
					allColumns, strFilter, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				BookInfo bookInfo = cursorToBookInfo(cursor);
				list.add(bookInfo);
				cursor.moveToNext();
			}

			cursor.close();
		}
		return list;
	}

	public ArrayList<BookInfo> getBooks() {
		ArrayList<BookInfo> list = new ArrayList<BookInfo>();
		Cursor cursor = database.query(BookFinderSQLHelper.TABLE_BOOK,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			BookInfo bookInfo = cursorToBookInfo(cursor);
			list.add(bookInfo);
			cursor.moveToNext();
		}

		cursor.close();
		return list;
	}

	public void clearBook() {
		database.delete(BookFinderSQLHelper.TABLE_BOOK, null, null);
	}

	private BookInfo cursorToBookInfo(Cursor cursor) {
		BookInfo listInfo = new BookInfo();
		listInfo.setBookID(cursor.getInt(0));
		listInfo.setBookName(cursor.getString(1));
		listInfo.setAuthor(cursor.getString(2));
		listInfo.setVersion(cursor.getString(3));
		listInfo.setBookImage(cursor.getString(4));
		listInfo.setDescription(cursor.getString(5));
		listInfo.setLevel(cursor.getString(6));
		listInfo.setSection(cursor.getString(7));
		listInfo.setShelve(cursor.getString(8));
		listInfo.setBookNumber(cursor.getString(9));
		listInfo.setNfcReferencecode(cursor.getString(10));

		return listInfo;
	}
}
