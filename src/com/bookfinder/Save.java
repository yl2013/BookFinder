package com.bookfinder;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Save extends Activity {

	int BID;
	int CID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save);

		Intent intent = getIntent();

		BID = intent.getIntExtra("BID", 0);
		Log.v("BID>>>>>>", "" + BID);

		final ListView listview = (ListView) findViewById(R.id.listView1);
		
		TextView tv = (TextView) findViewById(R.id.textView15);
		tv.setText(SQLHelper.GetOneBook(getApplicationContext(), BID).getBookName());
		
		listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		ArrayList<CategoryInfo> ulist = SQLHelper.GetCategories(this);

		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < ulist.size(); ++i) {
			list.add(ulist.get(i).getCategoryName());

		}
		@SuppressWarnings({ "unchecked", "rawtypes" })
		ArrayAdapter<String> adapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				for(int i=0;i<SQLHelper.GetCategories(getApplicationContext()).size();i++){
					
					
					if(list.get(position).equals(SQLHelper.GetCategories(getApplicationContext()).get(i).getCategoryName())){
						CID=SQLHelper.GetCategories(getApplicationContext()).get(i).getCategoryID();
						
					}			}
				Log.v("CID>>>>", "" + CID);
				listview.setSelector(android.R.color.holo_blue_light);
				//Toast.makeText(getApplicationContext(), "Category Selected",
					//	Toast.LENGTH_SHORT).show();
				// SQLHelper.SaveBook(getApplicationContext(), CID, BID);
				// for(int i =0;i<SQLHelper.GetBooks(getApplicationContext(),
				// CID).size();i++){
				// Log.v("fuck>>>>",""+SQLHelper.GetBooks(getApplicationContext(),
				// CID).get(i).getBookName());}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.save_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.Create_Ulist) {
			// todo entry name and create

			final EditText inputServer = new EditText(this);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Enter the list name you want.")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(inputServer).setNegativeButton("Cancel", null);
			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							SQLHelper.CreateCategory(getApplicationContext(),
									inputServer.getText().toString());

							Intent intent = new Intent();
							intent.putExtra("BID", BID);
							intent.setClass(getApplicationContext(), Save.class);
							startActivity(intent);
						}
					});
			builder.show();
		}
		return super.onOptionsItemSelected(item);
	}

	public void save(View v) {
		Log.v("save_info>>>>", "" + CID);
		Log.v("save_info>>>>", "" + BID);
		SQLHelper.SaveBook(getApplicationContext(), CID, BID);
		Toast.makeText(getApplicationContext(), "Book Saved",
				Toast.LENGTH_LONG).show();
		for (int i = 0; i < SQLHelper.GetBooks(getApplicationContext(), CID)
				.size(); i++) {
			Log.v("bookname>>>>",
					SQLHelper.GetBooks(getApplicationContext(), CID).get(i)
							.getBookName());
		}
		Intent intent = new Intent(this, Details_page.class);
		intent.putExtra("BID", BID);
		startActivity(intent);

	}

}
