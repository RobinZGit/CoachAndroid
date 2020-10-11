package com.mycompany.myapp;
import android.app.*;
import android.os.*;
import android.util.*;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import java.util.*;
import android.content.*;

public class EditFormActivity extends Activity
{
    @Override
	//private final String DELIMITER="\n\n<THIS IS DELIMITER>\n\n";
	//private EditText etRithm;
	//private EditText etDeals;
	private EditText etPrefs;

	private coachUtility Data;
	private boolean isSaved = false;
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Data = new coachUtility();
		/*
		try
		{
			Data.mSettings = getSharedPreferences(Data.SET_FILE_NAME, Context.MODE_PRIVATE);
			Data.mSettingsData = getSharedPreferences(Data.SET_AMETARITHM_FILE_NAME, Context.MODE_PRIVATE);
		}
		catch (Exception e)
		{}	
		*/
		setContentView(R.layout.editform);
		setTitle("Редактор ритмов");
		etPrefs = findViewById(R.id.idTextEditPrefs);
		SharedPreferences prefs = getSharedPreferences(Data.SET_AMETARITHM_FILE_NAME, Context.MODE_PRIVATE);
		String sprefs = Data.prefs2string(prefs);
		etPrefs.setText(sprefs);
    }

	public void onClickSave(View view)
	{
		//запомнить настройки
		Data.string2prefs(etPrefs.getText().toString());
		isSaved = true;
		this.finish();
	}
	public void onClickCancel(View view)
	{
		//запомнить настройки
		this.finish();
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();

		if (!isSaved)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder
				.setTitle("ttt")
				.setMessage("yyy")
				.setIcon(R.drawable.ic_launcher)
				.setCancelable(false)
				.setNegativeButton("llll", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int id)
					{dialog.cancel();	};});			
			AlertDialog alert =  builder.create();
			alert.show();

		}

	}



}
