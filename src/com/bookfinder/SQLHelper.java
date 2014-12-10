package com.bookfinder;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class SQLHelper {

	public static void CreateCategory(Context context, String categoryName) {
		CategoryDataSource categoryDataSource = new CategoryDataSource(context);
		categoryDataSource.open();

		categoryDataSource.createCategory(categoryName);

		categoryDataSource.close();
	}

	public static ArrayList<BookInfo> GetBooks(Context context, int categoryID) {
		MyListDataSource myListDataSource = new MyListDataSource(context);
		myListDataSource.open();

		ArrayList<MyListInfo> myListInfoList = null;
		myListInfoList = myListDataSource.getMyList(categoryID);
		myListDataSource.close();
		
		//for(MyListInfo mylist : myListInfoList)
		//Log.v("test", "list: " + mylist.getBookID());

		BookDataSource bookDataSource = new BookDataSource(context);
		bookDataSource.open();

		ArrayList<BookInfo> bookInfoList = null;
		bookInfoList = bookDataSource.getMyList(myListInfoList);
		bookDataSource.close();

		return bookInfoList;

	}
	
	public static ArrayList<BookInfo> GetAllBooks(Context context) {
		BookDataSource bookDataSource = new BookDataSource(context);
		bookDataSource.open();

		ArrayList<BookInfo> bookInfList = null;
		bookInfList = bookDataSource.getBooks();
		bookDataSource.close();

		return bookInfList;
	}
	
	public static BookInfo GetOneBook(Context context, int bookID) {
		BookDataSource bookDataSource = new BookDataSource(context);
		bookDataSource.open();

		BookInfo bookInfo = null;
		bookInfo = bookDataSource.getBook(bookID);
		bookDataSource.close();

		return bookInfo;
	}

	public static BookInfo GetOneBookByNFC(Context context, String nfcCode) {
		BookDataSource bookDataSource = new BookDataSource(context);
		bookDataSource.open();

		BookInfo bookInfo = null;
		bookInfo = bookDataSource.getBookByNFC(nfcCode);
		bookDataSource.close();

		return bookInfo;
	}

	public static void CreateOneBook(Context context, String bookName, String author,
			String version, String bookImage, String description, String level,
			String section, String shelve, String bookNumber,
			String nfcReferencecode)
	{
		BookDataSource bookDataSource = new BookDataSource(context);
		bookDataSource.open();

		bookDataSource.createBook(bookName, author, version, bookImage, description, level, section, shelve, bookNumber, nfcReferencecode);
		bookDataSource.close();
	}
	
	public static void clearDatabase(Context context)
	{
		
		CategoryDataSource categoryDataSource = new CategoryDataSource(context);
		categoryDataSource.open();
		categoryDataSource.clearCategory();
		categoryDataSource.close();
		
		MyListDataSource myListDataSource = new MyListDataSource(context);
		myListDataSource.open();
		myListDataSource.clearMyList();
		myListDataSource.close();
		
		BookDataSource bookDataSource = new BookDataSource(context);
		bookDataSource.open();
		bookDataSource.clearBook();
		bookDataSource.close();
		
	}
	
	public static void SaveBook(Context context, int categoryID, int bookID) {
		MyListDataSource myListDataSource = new MyListDataSource(context);
		myListDataSource.open();

		myListDataSource.createMyList(categoryID, bookID);

		myListDataSource.close();
	}

	public static void UpdateBookCategory(Context context, int categoryID,
			int bookID) {
		MyListDataSource myListDataSource = new MyListDataSource(context);
		myListDataSource.open();

		myListDataSource.updateCategory(categoryID, bookID);

		myListDataSource.close();
	}

	public static void DeleteBook(Context context, int categoryID, int bookID) {
		MyListDataSource myListDataSource = new MyListDataSource(context);
		myListDataSource.open();

		myListDataSource.deleteMyList(categoryID, bookID);

		myListDataSource.close();

	}

	public static void DeleteWholeCategory(Context context, int categoryID) {
		MyListDataSource myListDataSource = new MyListDataSource(context);
		myListDataSource.open();

		myListDataSource.deleteCategory(categoryID);

		myListDataSource.close();

		CategoryDataSource categoryDataSource = new CategoryDataSource(context);
		categoryDataSource.open();

		categoryDataSource.deleteCategory(categoryID);

		categoryDataSource.close();

	}

	public static ArrayList<CategoryInfo> GetCategories(Context context) {
		CategoryDataSource categoryDataSource = new CategoryDataSource(context);
		categoryDataSource.open();

		ArrayList<CategoryInfo> categoryInfoList = null;
		categoryInfoList = categoryDataSource.getAllCategories();

		categoryDataSource.close();

		return categoryInfoList;
	}

}
