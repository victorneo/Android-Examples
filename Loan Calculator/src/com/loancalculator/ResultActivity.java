package com.loancalculator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class ResultActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.d(this.getClass().getName(), "Now in On Create");
		this.setContentView(R.layout.result);
		
		Bundle bundle = getIntent().getExtras();
		Log.d(this.getClass().getName(),"Interest Rate: "+bundle.getString("interestrate"));
		
		float interestrate = Float.parseFloat(bundle.getString("interestrate"));
		float period = Float.parseFloat(bundle.getString("period"));
		float amount = Float.parseFloat(bundle.getString("amount"));
		
		float monthly = ((amount * (interestrate/100)*period)+amount)/(period * 12);
		Intent data = new Intent();
		data.setData(Uri.parse("" + monthly));
		setResult(1,data); // first param to indicate resultCode
		this.finish();
	}
}
