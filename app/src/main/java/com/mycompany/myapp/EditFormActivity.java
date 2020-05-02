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
	
	private EditText etRithm;
	private EditText etDeals;

	private coachUtility Data;
	private boolean isSaved = false;
	protected void onCreate(Bundle savedInstanceState)
	{
	  super.onCreate(savedInstanceState);
	  Data = new coachUtility();
	  try{
	    Data.mSettings = getSharedPreferences(Data.SET_FILE_NAME,Context.MODE_PRIVATE);
	    Data.mSettingsData = getSharedPreferences(Data.SET_AMETARITHM_FILE_NAME,Context.MODE_PRIVATE);
	  } catch (Exception e) {}	
	  setContentView(R.layout.editform);
	  setTitle("Редактор ритмов");
	  
	  etRithm = findViewById(R.id.idTextEditRithm);
	  etDeals = findViewById(R.id.idTextEditDeals);
	  
	 // Data = new coachUtility();
	  try{
	    if  (Data.mSettingsData.contains(Data.SET_AMETARITHM) ){
			Data.sAMetaRithm= Data.mSettingsData.getString(Data.SET_AMETARITHM,"");
			etRithm.setText(Data.sAMetaRithm);
			//Data.sADeals=Data.mSettingsData.getString(Data.SET_ADEALS,"");
			//etDeals.setText(Data.sADeals);
			Data.sADeals100=Data.mSettingsData.getString(Data.SET_ADEALS100,"");
			etDeals.setText(Data.sADeals100);
	     }  
	     else {
		   //else из модуля
		   String ss = Data.aMetaRithm2TechString( Data.aMetaRithmInit);
	       etRithm.setText(ss);
		 //  ss = Data.aDeals2TechString( Data.aDealsInit);
	     //  etDeals.setText(ss);
			 ss = Data.aRithm2TechString( Data.aDeals100Init);
			 etDeals.setText(ss);
        }		
	  } catch (Exception e) {
		  String ss = Data.aMetaRithm2TechString( Data.aMetaRithmInit);
		  etRithm.setText(ss);
		 // ss = Data.aDeals2TechString( Data.aDealsInit);
		  ss = Data.aRithm2TechString( Data.aDeals100Init);
		  etDeals.setText(ss);  
	  }	
    }
	
	public void onClickSave (View view) {
      //запомнить настройки
	  Data.sAMetaRithm = etRithm.getText().toString();
	//!!!  Data.sADeals = etDeals.getText().toString();
		Data.sADeals100 = etDeals.getText().toString();
	  SharedPreferences.Editor editor = Data.mSettingsData.edit();
	  editor.putString(Data.SET_AMETARITHM,Data.sAMetaRithm);
	  editor.putString(Data.SET_ADEALS100,Data.sADeals100);
	//!!!!!	editor.putString(Data.SET_ADEALS,Data.sADeals); 
	  editor.apply();
	  isSaved = true;
	  this.finish();
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		
		if(!isSaved){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder
			.setTitle("ttt")
			.setMessage("yyy")
			.setIcon(R.drawable.ic_launcher)
			.setCancelable(false)
			.setNegativeButton("llll", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id){dialog.cancel();	};});			
			AlertDialog alert =  builder.create();
				alert.show();
			
		}
		
	}
	
	
   
}
