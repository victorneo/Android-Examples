package com.contact;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ContactManagerActivity extends Activity {
	
	TableLayout tablelayout_Contacts = null;
	Button insertButton = null;
	EditText nameEdit = null;
	EditText contactEdit = null;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tablelayout_Contacts = (TableLayout) findViewById(R.id.tableLayout_Contacts);
        
        tablelayout_Contacts.setStretchAllColumns(true);   
        tablelayout_Contacts.setShrinkAllColumns(true);   
        
        nameEdit = (EditText) findViewById(R.id.editText_Name);
        contactEdit = (EditText) findViewById(R.id.editText_Number);

        insertButton = (Button) findViewById(R.id.button1);
        insertButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DBAdaptor dbAdaptor = new DBAdaptor(getApplicationContext());
				try{
					dbAdaptor.open();
					String name = nameEdit.getText().toString();
					String contact = contactEdit.getText().toString();
					dbAdaptor.insertContact(name, contact);
					nameEdit.setText("");
					contactEdit.setText("");
					nameEdit.requestFocus();
					Toast.makeText(getApplicationContext(), name + " added.", 2000).show();
				}catch(Exception ex){
					Log.e("Contact Manager", ex.getMessage());
				}finally{
					if(dbAdaptor != null){
						dbAdaptor.close();
					}
				}
				refreshTable();
			}
		});
        
    }
    
    @Override
	protected void onResume() {
    	super.onResume();
		refreshTable();
		restoreInputFromPreferences();
	}
    
    @Override
    protected void onPause(){
    	super.onPause();
    	saveInputAsPreferences();
    }

    public void saveInputAsPreferences(){
    	String name = nameEdit.getText().toString();
    	String contact = contactEdit.getText().toString();
    	
    	SharedPreferences prefs = getSharedPreferences("preferences",MODE_PRIVATE);
    	SharedPreferences.Editor editor = prefs.edit();
    	editor.putString("name", name);
    	editor.putString("contact", contact);
    	editor.commit();
    }
    
    public void restoreInputFromPreferences(){
    	SharedPreferences prefs = getSharedPreferences("preferences",MODE_PRIVATE);
    	if(prefs.contains("name")){
    		String name = prefs.getString("name","");
    		nameEdit.setText(name);
    	}
    	if(prefs.contains("contact")){
    		String contact = prefs.getString("contact","");
    		contactEdit.setText(contact);
    	}
    }

	public void refreshTable(){
    	tablelayout_Contacts.removeAllViews();
    	TableRow rowTitle = new TableRow(this);
    	rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);
    	
    	TextView title = new TextView(this);
    	title.setText("Contacts");
    	title.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
    	title.setGravity(Gravity.CENTER);
    	title.setTypeface(Typeface.SERIF,Typeface.BOLD);
    	
    	TableRow.LayoutParams params = new TableRow.LayoutParams();
    	params.span = 3;
    	
    	rowTitle.addView(title,params);
    	
    	tablelayout_Contacts.addView(rowTitle);
    	
    	DBAdaptor dbAdaptor = new DBAdaptor(getApplicationContext());
    	Cursor cursor = null;
    	
    	try{
    		dbAdaptor.open();
    		cursor = dbAdaptor.getAllContacts();
    		cursor.moveToFirst();
    		String name = null;
    		String contact = null;
    		TextView idView = null;
    		TextView nameView = null;
    		TextView contactView = null;
    		TableRow row = null;
    		long id = 0;
    		do{
    			id = cursor.getLong(0);
    			name = cursor.getString(1);
    			contact = cursor.getString(2);
    			
    			idView = new TextView(getApplicationContext());
    			idView.setText("" + id);
    			
    			nameView = new TextView(getApplicationContext());
    			nameView.setText(name);
    			
    			contactView = new TextView(getApplicationContext());
    			contactView.setText(contact);
    			
    			row = new TableRow(this);
    			row.setGravity(Gravity.CENTER_HORIZONTAL);
    			row.addView(idView);
    			row.addView(nameView);
    			row.addView(contactView);
    			
    			tablelayout_Contacts.addView(row);
    		}while(cursor.moveToNext());
    	}catch(Exception ex){
    		Log.d("Contact manager",ex.getMessage());
    	}finally{
    		if (cursor != null)
    			cursor.close();
    		if(dbAdaptor != null)
    			dbAdaptor.close();
    	}
    }
    
    
}