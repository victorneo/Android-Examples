package com.loancalculator;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class LoanCalculatorActivity extends Activity {

	Button calculateButton = null;
	Button saveButton = null;
	Button loadButton = null;
	Spinner interestRatesSpinner = null;
	Spinner periodSpinner = null;
	EditText amountEditText = null;
	TextView toBeCalculatedTextView = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		calculateButton = (Button) findViewById(R.id.calculatorButton);
		interestRatesSpinner = (Spinner) findViewById(R.id.interestSpinner);
		periodSpinner = (Spinner) findViewById(R.id.periodSpinner);
		amountEditText = (EditText) findViewById(R.id.amountEditText);
		toBeCalculatedTextView = (TextView) findViewById(R.id.toBeCalculatedTextView);
		saveButton = (Button) findViewById(R.id.buttonSave);
		loadButton = (Button) findViewById(R.id.buttonLoad);

		ArrayAdapter<CharSequence> ir_adapter = ArrayAdapter
				.createFromResource(this, R.array.interest_rate_array,
						android.R.layout.simple_spinner_item);
		ir_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		interestRatesSpinner.setAdapter(ir_adapter);

		ir_adapter = ArrayAdapter.createFromResource(this,
				R.array.period_array, android.R.layout.simple_spinner_item);
		ir_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		periodSpinner.setAdapter(ir_adapter);

		loadButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				retrievePreferences();
			}
		});

		saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				saveAsText();
				saveAsPreferences();
			}
		});

		calculateButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("com.loancalculator.ResultActivity");
				Bundle extras = new Bundle();
				extras.putString("amount", amountEditText.getText().toString());
				extras.putString("interestrate", interestRatesSpinner
						.getSelectedItem().toString());
				extras.putString("period", periodSpinner.getSelectedItem()
						.toString());
				intent.putExtras(extras);
				startActivityForResult(intent, 1);
			}
		});
	}

	public void saveAsText() {
		String amountString = amountEditText.getText().toString();
		String interestRateString = interestRatesSpinner.getSelectedItem()
				.toString();
		String periodString = periodSpinner.getSelectedItem().toString();

		try {
			FileOutputStream fos = openFileOutput("preferences.txt",
					MODE_WORLD_WRITEABLE);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			osw.write("amount: " + amountString + "\n");
			osw.write("interest rate: " + interestRateString + "\n");
			osw.write("period: " + periodString + "\n");
			osw.flush();
			osw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void retrievePreferences() {
		SharedPreferences prefs = getSharedPreferences("preferences",
				MODE_PRIVATE);
		if (prefs.contains("amount")) {
			String amountString = prefs.getString("amount", "");
			amountEditText.setText(amountString);
		}
		if (prefs.contains("interestrate")) {
			String interestRateString = prefs.getString("interestrate", "");
			ArrayAdapter<CharSequence> adaptor = (ArrayAdapter<CharSequence>) interestRatesSpinner
					.getAdapter();
			interestRatesSpinner.setSelection(adaptor
					.getPosition(interestRateString));
		}
		if (prefs.contains("period")) {
			String periodString = prefs.getString("period", "");
			ArrayAdapter<CharSequence> adaptor = (ArrayAdapter<CharSequence>) periodSpinner
					.getAdapter();
			periodSpinner.setSelection(adaptor.getPosition(periodString));
		}
	}

	public void saveAsPreferences() {
		String amountString = amountEditText.getText().toString();
		String interestRateString = interestRatesSpinner.getSelectedItem()
				.toString();
		String periodString = periodSpinner.getSelectedItem().toString();

		SharedPreferences prefs = getSharedPreferences("preferences",
				MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("amount", amountString);
		editor.putString("interestrate", interestRateString);
		editor.putString("period", periodString);
		editor.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1) {
			if (resultCode == 1) {
				String monthly_string = data.getData().toString();
				toBeCalculatedTextView.setText(monthly_string);
				this.finishActivity(1);
			}
		}
	}

	@Override
	protected void onDestroy() {
		Log.d(this.getClass().getName(), "On Destroy called.");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.d(this.getClass().getName(), "On Pause called.");
		saveAsPreferences();
		super.onPause();
	}

	@Override
	protected void onRestart() {
		Log.d(this.getClass().getName(), "On Restart called.");
		super.onRestart();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.d(this.getClass().getName(), "On Restore Instance State Called");
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(this.getClass().getName(), "On Save Instance State Called");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onResume() {
		Log.d(this.getClass().getName(), "On Resume called.");
		retrievePreferences();
		super.onResume();
	}

	@Override
	protected void onStart() {
		Log.d(this.getClass().getName(), "On Start called.");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.d(this.getClass().getName(), "On Stop called.");
		super.onStop();
	}
}