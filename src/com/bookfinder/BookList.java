package com.bookfinder;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class BookList extends Activity {

	TypedArray menuIcons;

	// nav drawer title
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private List<RowItem> rowItems;
	private CustomAdapter adapter;
	private ArrayList<CategoryInfo> mylistguodu;

	int judge = 1;//use to differ the longclick and click ->longclick means delete the item( list or book).

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_fragment_w);

		mTitle = mDrawerTitle = getTitle();

		mylistguodu = SQLHelper.GetCategories(getApplicationContext());//copy the Categorylist form the database

		menuIcons = getResources().obtainTypedArray(R.array.icons);//get the pic for every list 

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.slider_list);

		rowItems = new ArrayList<RowItem>();//contains a  pic and a listname

		for (int i = 0; i < mylistguodu.size(); i++) {
			RowItem items = new RowItem(mylistguodu.get(i).getCategoryName(),
					menuIcons.getResourceId(0, -1));
			rowItems.add(items);//add the listname to the arrylist rowitems
		}

		menuIcons.recycle();//recycle pic

		adapter = new CustomAdapter(getApplicationContext(), rowItems);

		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new SlideitemListener());//open a list
		mDrawerList.setOnItemLongClickListener(new deletelistener());//delete a list

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_launcher, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			updateDisplay(0);
		}
	}

	class SlideitemListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			updateDisplay(position);
		}

	}

	class deletelistener implements ListView.OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			judge = 1;
			DeleteDialog(SQLHelper.GetCategories(getApplicationContext())
					.get(position).getCategoryID());
			return false;
		}
	}

	private void DeleteDialog(final int position) {
		AlertDialog.Builder builder = new Builder(this);

		builder.setMessage("Sure to delete the List?");
		builder.setTitle("Warning");

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				deletelist(position);

				// Intent intent =new Intent();
				// intent.setClass(getActivity(), BookList.class);
				// startActivity(intent);

			}
		});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		builder.create().show();
	}

	@SuppressWarnings("unused")
	private void updateDisplay(int position) {
		Fragment fragment = null;

		fragment = new FB_Fragment(position, SQLHelper
				.GetCategories(getApplicationContext()).get(position)
				.getCategoryID());//get the booknamelist form the rightside

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			setTitle(mylistguodu.get(position).getCategoryName());
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}

	}

	//longclick to deletelist
	private void deletelist(int id) {
		SQLHelper.DeleteWholeCategory(getApplicationContext(), id);
		Intent intent = new Intent();
		intent.setClass(this, BookList.class);
		startActivity(intent);

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}*/

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}*/

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	/*@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}*/

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	public void onBackPressed(){
		Intent intent = new Intent(this, Book_main.class);
		startActivity(intent);
	}

}