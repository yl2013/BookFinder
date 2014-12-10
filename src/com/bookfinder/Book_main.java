package com.bookfinder;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Book_main extends Activity {
	String list1 = "jigar_list";
	String list2 = "you_list";
	String list3 = "yunli_list";
	int[] deletec;
	PendingIntent nfcPendingIntent;
	NfcAdapter nfcAdapter;

	public void InitDatabase() {
		SQLHelper.CreateCategory(getApplicationContext(), list1);
		SQLHelper.CreateCategory(getApplicationContext(), list2);
		SQLHelper.CreateCategory(getApplicationContext(), list3);// create list

		for (int i = 0; i < SQLHelper.GetCategories(getApplicationContext())
				.size(); i++) {
			Log.d("ID>>>>>>",
					""
							+ SQLHelper.GetCategories(getApplicationContext())
									.get(i).getCategoryID());
			SQLHelper.CreateOneBook(getApplicationContext(), "testbook" + i
					+ "", "author", "version", "bookImage", "description",
					"level", "section", "shelve", "bookNumber",
					"nfcReferencecode" + i + "");

			SQLHelper.CreateOneBook(getApplicationContext(), "testbook" + i + 1
					+ "", "author", "version", "bookImage", "description",
					"level", "section", "shelve", "bookNumber",
					"nfcReferencecode" + i + 1 + "");

			SQLHelper.SaveBook(
					getApplicationContext(),
					SQLHelper.GetCategories(getApplicationContext()).get(i)
							.getCategoryID(),
					SQLHelper.GetOneBookByNFC(getApplicationContext(),
							"nfcReferencecode" + i + "").getBookID());

			SQLHelper.SaveBook(
					getApplicationContext(),
					SQLHelper.GetCategories(getApplicationContext()).get(i)
							.getCategoryID(),
					SQLHelper.GetOneBookByNFC(getApplicationContext(),
							"nfcReferencecode" + i + 1 + "").getBookID());
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_main);

		if(SQLHelper.GetAllBooks(this).size()<=0)
		{
		//	InitDatabase();
			SQLHelper.CreateOneBook(this, "Beginning Android", "Mark Murphy", "1", "bookImage", "description", "1", "East", "2", "874512", "nfccode1");
		SQLHelper.CreateOneBook(this, "Android for Dummies", "Dan Gookin", "1", "bookImage", "description", "2", "West", "15", "12554", "nfccode2");
		SQLHelper.CreateOneBook(this, "Programming Android", "Laird Dornin", "2", "bookImage", "description", "3", "North", "5", "65155", "nfccode3");
		
		SQLHelper.CreateCategory(this, "Academic");
		SQLHelper.CreateCategory(this, "Novel");
		}
		
		
		//SQLHelper.clearDatabase(this);
		
		//SQLHelper.clearDatabase(getApplicationContext());
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);

		// Create the Pending Intent.
		int requestCode = 0;
		int flags = 0;

		Intent nfcIntent = new Intent(this, getClass());
		nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		nfcPendingIntent = PendingIntent.getActivity(this, requestCode,
				nfcIntent, flags);

		String action = getIntent().getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			try {
				processIntent(getIntent());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.book_main_menu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.List) {
			Intent i = new Intent(this, BookList.class);
			startActivityForResult(i, 99);
		}

		return super.onOptionsItemSelected(item);
	}

	/*public void next(View v) {
		Intent i = new Intent(this, Details_page.class);
		i.putExtra("BID", 1);
		startActivityForResult(i, 1);
	}*/
	
	

	void processIntent(Intent intent) throws UnsupportedEncodingException {
		Parcelable[] msgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		NdefMessage[] nmsgs = new NdefMessage[msgs.length];
		for (int i = 0; i < msgs.length; i++) {
			nmsgs[i] = (NdefMessage) msgs[i];
		}
		NdefRecord record = nmsgs[0].getRecords()[0];
		byte[] payload = record.getPayload();
		if (payload != null) {
			if (nmsgs[0].getRecords()[0].getTnf() != 2) {
				//Toast.makeText(this, "New Text received!", Toast.LENGTH_SHORT)
					//	.show();
				// String message = String.valueOf(record.getPayload());
				String message = getTextData(record.getPayload());
				// textview = (TextView) findViewById(R.id.textView2);
				//Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();	
				// text = String.valueOf(payload);
				// textview.setText(message);
				Intent intent_nfccode = new Intent(this, Details_page.class);
				intent_nfccode.putExtra("NFC Code", message);
			
				startActivity(intent_nfccode);
			}
		}
	}

	private String getTextData(byte[] payload) {
		if (payload == null)
			return null;
		try {
			String encoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
			int langageCodeLength = payload[0] & 0063;
			return new String(payload, langageCodeLength + 1, payload.length
					- langageCodeLength - 1, encoding);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void onBackPressed() {
	    Intent startMain = new Intent(Intent.ACTION_MAIN);      
	        startMain.addCategory(Intent.CATEGORY_HOME);                        
	        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);          
	        startActivity(startMain); 
	  }

}
