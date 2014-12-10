package com.bookfinder;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@SuppressLint("NewApi")
public class FB_Fragment extends Fragment {
	int position;
	int Cid;
	int judge = 0;
	ArrayList<String> booknamelist = new ArrayList<String>();
	private ListView blist;

	public FB_Fragment()
	{}
	
	public FB_Fragment(int position, int Cid) {
		this.position = position;
		this.Cid = Cid;
		// TODO Auto-generated constructor stub
	}

	// String[] list2 = { "2.1  b1", "2.2  b2", "2.3  b3", "2.4  b4" };

	// String[] list3 = { "3.1  b1", "3.2  b2", "3.3  b3", "3.4  b4" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.v("book_number", "" + SQLHelper.GetBooks(getActivity(), Cid).size());
		View rootView = inflater
				.inflate(R.layout.fb_fragment, container, false);
		blist = (ListView) rootView.findViewById(R.id.listr);
		for (int i = 0; i < SQLHelper.GetBooks(getActivity(), Cid).size(); i++) {
			booknamelist.add(SQLHelper.GetBooks(getActivity(), Cid).get(i)
					.getBookName());
		}
		for (int i = 0; i < booknamelist.size(); i++) {
			String c = booknamelist.get(i);
			Log.v("Blist>>>>>>", c);
		}

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_list_item_1,
				booknamelist);
		blist.setAdapter(adapter);
		blist.setOnItemClickListener(new booklistener());

		blist.setOnItemLongClickListener(new deletelistener());

	}

	class booklistener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (judge == 0) {
				gobook(SQLHelper.GetBooks(getActivity(), Cid).get(position)
						.getBookID());
			}
		}
	}

	class deletelistener implements ListView.OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			judge = 1;
			DeleteDialog(SQLHelper.GetBooks(getActivity(), Cid).get(position)
					.getBookID());
			return false;
		}
	}

	private void DeleteDialog(final int position) {
		AlertDialog.Builder builder = new Builder(getActivity());

		builder.setMessage("Sure to delete the book?");
		builder.setTitle("Warning");

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				deletebook(position);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getActivity(), android.R.layout.simple_list_item_1,
						booknamelist);
				blist.setAdapter(adapter);
				adapter.notifyDataSetChanged();

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

	private void gobook(int id) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), Details_page.class);
		intent.putExtra("BID", id);
		startActivity(intent);
	}

	private void deletebook(int id) {
		SQLHelper.DeleteBook(getActivity(), Cid, id);
		Intent intent = new Intent();
		intent.setClass(getActivity(), BookList.class);
		startActivity(intent);

	}

	/*
	 * @Override public void onAttach(Activity activity) { // TODOm,
	 * Auto-generated method stub super.onAttach(activity); try { mListener =
	 * (OnItemClickedListener) activity; } catch (ClassCastException e) {
	 * e.printStackTrace(); } }
	 */
	
	public void onBackPressed(){
		Intent intent = new Intent(getActivity(), Details_page.class);
		startActivity(intent);
	}

}
