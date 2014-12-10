package com.bookfinder;

import java.nio.charset.Charset;
import java.util.Locale;

import com.bookfinder.BookList;
import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

public class Details_page extends Activity {

	int BID;
	private static final int RESULT_SETTINGS = 1;
	NfcAdapter mNfcAdapter;
	private String payload = "";
	byte statusByte;
	private String nfc_code;
	private String overview;
	TextView book_name, book_author, book_version, level, section, shelve,
			book_no;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_page);
		Intent intent = getIntent();

		try {

			BID = intent.getIntExtra("BID", 0);
			Log.v("BOOKID", "" + BID);
			if (BID != 0){
				display_BookDetails1(BID);
				nfc_code=  SQLHelper.GetOneBook(getApplicationContext(), BID).getNfcReferencecode();
			}
			else {
				nfc_code = intent.getExtras().getString("NFC Code");
				//Toast.makeText(getApplicationContext(), nfc_code,
					//	Toast.LENGTH_LONG).show();
				
				/*if (nfc_code == null) {
					Toast.makeText(getApplicationContext(), "Invalid NFC code",
							Toast.LENGTH_LONG).show();
					return;
				}*/
				display_BookDetails(nfc_code);
				BID=SQLHelper.GetOneBookByNFC(getApplicationContext(), nfc_code).getBookID();
			
				}
			mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
			if (mNfcAdapter == null) {
				Toast.makeText(getApplicationContext(),
						"NFC apdater is not available", Toast.LENGTH_LONG)
						.show();
				finish();
				
				return;
			
		}

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Invalid BOOKID",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.details_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.Save:

			//Toast.makeText(getApplicationContext(), "SAVE", Toast.LENGTH_SHORT)
				//	.show();

			Intent intent = new Intent();

			intent.setClass(this, Save.class);

			intent.putExtra("BID", BID);
			startActivity(intent);
			return true;

		case R.id.List:
			//Toast.makeText(getApplicationContext(), "LIST", Toast.LENGTH_SHORT)
				//	.show();
			clicked_list();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public void clicked_list() {
		Intent i = new Intent(this, BookList.class);
		startActivityForResult(i, 99);

	}

	public void share(View v) {
	/*	if (nfc_code == null || nfc_code == "") {
			if (BID > 0) {
				BookInfo book = SQLHelper.GetOneBook(this, BID);
				nfc_code = book.getNfcReferencecode();
			} else {
				Toast.makeText(this, "Invalid Data. Cannot share the book!",
						Toast.LENGTH_SHORT).show();
				return;
			}
		}*/ 
		//Toast.makeText(this, nfc_code,
			//	Toast.LENGTH_SHORT).show();
		NdefMessage message = create_RTD_TEXT_NdefMessage(nfc_code);
		try {
			mNfcAdapter.setNdefPushMessage(message, this);
			Toast.makeText(this, "Touch another mobile to share the message",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(this, "Unable to share",
					Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onResume() {
	super.onResume();
	if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
	processIntent(getIntent());
	}
	}
	
	@Override
	public void onNewIntent(Intent intent) {
	setIntent(intent);
	}
	void processIntent(Intent intent) {
	NdefMessage[] messages = getNdefMessages(getIntent());
	for(int i=0; i<messages.length; i++)
	{
	for(int j=0; j<messages[0].getRecords().length; j++)
	{

	NdefRecord record = messages[i].getRecords()[j];
	statusByte = record.getPayload()[0];
	int languageCodeLength= statusByte & 0x3F;
	int isUTF8 = statusByte-languageCodeLength;
	if(isUTF8 == 0x00)
	{
	payload = new String(
	record.getPayload(),1+languageCodeLength,
	record.getPayload().length-1-languageCodeLength,
	Charset.forName("UTF-8"));
	}
	else if (isUTF8==-0x80)
	{
	payload = new String
	(record.getPayload(),
	1+languageCodeLength,
	record.getPayload().length-1-languageCodeLength,
	Charset.forName("UTF-16")
	);
	}
	//messageText.setText("Text received: "+ payload);
	}
	}
	}
	NdefMessage create_RTD_TEXT_NdefMessage(String inputText)
	{
	Locale locale = new Locale("en","US");
	byte[] langBytes = locale.getLanguage().getBytes(
	Charset.forName("US-ASCII"));
	boolean encodeInUtf8 = false;
	Charset utfEncoding = encodeInUtf8 ?
	Charset.forName("UTF-8"):Charset.forName("UTF-16");
	int utfBit = encodeInUtf8 ? 0 : (1 << 7);
	byte status = (byte) (utfBit + langBytes.length);
	byte[] textBytes = inputText.getBytes(utfEncoding);
	byte[] data = new byte[1 + langBytes.length + textBytes.length];
	data[0] = (byte) status;
	System.arraycopy(langBytes, 0, data, 1, langBytes.length);
	System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
	NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
	NdefRecord.RTD_TEXT, new byte[0], data);
	NdefMessage message= new NdefMessage(new NdefRecord[] { textRecord});
	return message;
	}
	NdefMessage[] getNdefMessages(Intent intent)
	{
	NdefMessage[] msgs = null;
	if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()))
	{
	Parcelable[]
	rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
	if (rawMsgs != null)
	{
	msgs = new NdefMessage[rawMsgs.length];
	for (int i=0; i < rawMsgs.length; i++) {
	msgs[i] = (NdefMessage) rawMsgs[i];
	}
	} else {
	byte[] empty = new byte[] {};
	NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN,
	empty, empty, empty);
	NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
	msgs = new NdefMessage[] { msg };
	}
	} else {
	Log.d("Book Finder", "Unknown intent.");
	finish();
	}
	return msgs;
	}
	
	public void display_BookDetails1(int BID) {
		BookInfo b = SQLHelper.GetOneBook(this, BID);
		book_name = (TextView) findViewById(R.id.textView9);
		book_author = (TextView) findViewById(R.id.textView10);
		book_version = (TextView) findViewById(R.id.textView11);
		level = (TextView) findViewById(R.id.textView12);
		section = (TextView) findViewById(R.id.textView13);
		shelve = (TextView) findViewById(R.id.textView14);
		book_no = (TextView) findViewById(R.id.textView15);

		book_name.setText(b.getBookName());
		book_author.setText(b.getAuthor());
		book_version.setText(b.getVersion());
		level.setText(b.getLevel());
		section.setText(b.getSection());
		shelve.setText(b.getShelve());
		book_no.setText(b.getBookNumber());

	}

	public void display_BookDetails(String nfc_code) {
		BookInfo b = SQLHelper.GetOneBookByNFC(this, nfc_code);
		book_name = (TextView) findViewById(R.id.textView9);
		book_author = (TextView) findViewById(R.id.textView10);
		book_version = (TextView) findViewById(R.id.textView11);
		level = (TextView) findViewById(R.id.textView12);
		section = (TextView) findViewById(R.id.textView13);
		shelve = (TextView) findViewById(R.id.textView14);
		book_no = (TextView) findViewById(R.id.textView15);
		
	//	Toast.makeText(this,"inside Display_BookDetails", Toast.LENGTH_SHORT).show();
		
		
		book_name.setText(b.getBookName());
		book_author.setText(b.getAuthor());
		book_version.setText(b.getVersion());
		level.setText(b.getLevel());
		section.setText(b.getSection());
		shelve.setText(b.getShelve());
		book_no.setText(b.getBookNumber());

	}
}