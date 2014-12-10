package com.bookfinder;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CategoryDataSource {
	private SQLiteDatabase database;
	private BookFinderSQLHelper dbHelper;
	private String[] allColumns = { BookFinderSQLHelper.COLUMN_NAME_CATEGORY_ID,
			BookFinderSQLHelper.COLUMN_NAME_CATEGORYNAME };

	public CategoryDataSource(Context context) {
		dbHelper = new BookFinderSQLHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public CategoryInfo createCategory(String itemName) {
		ContentValues values = new ContentValues();

		values.put(BookFinderSQLHelper.COLUMN_NAME_CATEGORYNAME, itemName);

		long insertId = database.insert(BookFinderSQLHelper.TABLE_CATEGORY, null,
				values);
		Cursor cursor = database.query(BookFinderSQLHelper.TABLE_CATEGORY,
				allColumns, BookFinderSQLHelper.COLUMN_NAME_CATEGORY_ID + " = "
						+ insertId, null, null, null, null);
		cursor.moveToFirst();
		CategoryInfo categoryInfo = cursorToCategoryInfo(cursor);
		cursor.close();
		return categoryInfo;
	}

	public void deleteCategory(int categoryID) {

		database.delete(BookFinderSQLHelper.TABLE_CATEGORY,
				BookFinderSQLHelper.COLUMN_NAME_CATEGORY_ID + " = " + categoryID, null);
	}
	
	public void clearCategory()
	{
		database.delete(BookFinderSQLHelper.TABLE_CATEGORY,
				null, null);
	}

	public ArrayList<CategoryInfo> getAllCategories() {
		ArrayList<CategoryInfo> categories = new ArrayList<CategoryInfo>();

		Cursor cursor = database.query(BookFinderSQLHelper.TABLE_CATEGORY,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CategoryInfo category = cursorToCategoryInfo(cursor);
			categories.add(category);
			cursor.moveToNext();
		}

		cursor.close();
		return categories;
	}

	public void updateCategory(int categoryID, String categoryName) {
		String strFilter = BookFinderSQLHelper.COLUMN_NAME_CATEGORY_ID + "="
				+ categoryID;
		ContentValues values = new ContentValues();

		values.put(BookFinderSQLHelper.COLUMN_NAME_CATEGORYNAME, categoryName);

		database.update(BookFinderSQLHelper.TABLE_CATEGORY, values, strFilter, null);
	}

	private CategoryInfo cursorToCategoryInfo(Cursor cursor) {
		CategoryInfo categoryInfo = new CategoryInfo();
		categoryInfo.setCategoryID(cursor.getInt(0));
		categoryInfo.setCategoryName(cursor.getString(1));

		return categoryInfo;
	}
}
