package com.mycompany.myapp;

import android.app.*;
import android.os.*;
//
import android.speech.tts.*;
import android.util.*;
import android.view.View;
import android.view.View.OnClickListener;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;
import java.util.ArrayList;
import android.widget.*;
import java.util.Locale;
import java.sql.*;
import android.icu.text.*;
import android.content.*;
import android.database.sqlite.*;
import android.widget.AdapterView.*;
import java.io.*;
import android.view.inputmethod.*;
import android.view.*;
import android.text.*;
import javax.net.ssl.*;
import java.util.Map;
import java.net.URI;
//import java.io.*;//  .FileUriExposedException;
import android.net.Uri;

/*
 import java.io.ByteArrayOutputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.OutputStream;
 import java.util.regex.Pattern;
 */
//import org.apache.commons.
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.*;
public class MainActivity extends Activity
{

	@Override

	final String wordSelectedAll = "Все перечисленные ниже (даже если не выбраны)";

	//надписи на кнопке при изменении статуса тренировки (эти же константы используются как ИД статусов - не делать тексты одинаковыми!)   
	private String TXT_BTN_NOTSTARTED = "Начать тренировку";
	private String TXT_BTN_STARTED = "Идет тренировка...";
	private String TXT_BTN_PAUSED = "Тренировка приостановлена...";    
	private String TXT_BTN_FINISHED = "Тренировка завершена";
	//  
	private TextToSpeech TTS;
	private DateFormat formatter;
	private int Gl_CountOfIterations = 0;
	private int Gl_CountOfAllIterations = 0;
	private int GlnRepeat = 1;	//сколько раз повторить всю тренировку
	private String GltxtSpeek;
	private int GlnDelay = 5; //отсрочка старта по умолчанию - 5 сек
	private TextView tvTextToSpeek;
	private TextView tvDelay;
	private TextView tvRepeat;
	private EditText edtCurr;
	int nRandDispers;

	ArrayList<String> alSpinner = new ArrayList<String>();
	ArrayList<Integer> aliSpinner = new ArrayList<Integer>();
	ArrayList<String> alTags = new ArrayList<String>();

	int Gl_SelectedIndex = 0;
	String Gl_SelectedID = "";

	private boolean FLPAUSE =true;
	private boolean FLFINISH =false;

	private LinearLayout layout;
	private LinearLayout layoutParams0;
	private LinearLayout layoutTags;
	private Button btnMain;
	private Button btnFinish;
	private Button btnTest;
	private Button btnEdit;
	private Button btnSaveAll;
	private Button btnClone;
	private Button btnDisabledEdit;
	private Button btnDel;
	private Button btnLoad;

	private Spinner spinner;
	private ListView listView;
	private CheckBox chkToday;

	private boolean isChkToday = true;
	private coachUtility Data;
	private dataBaseInit Datab;

	private MatchParser GlParser1;
	private rithmFunc GlF;

	//для динамической генерации вьюх на форме
	protected final int CHECKBOX_END_ID = 10;
	protected final int PARAMTEXT_END_ID = 20;
	protected final int PARAMLABEL_END_ID = 30;
	protected final int PARAMFORMULA_END_ID = 40;
	protected int fget_checkboxId(int i)
	{
		return 100 * i + CHECKBOX_END_ID;
	}
	protected int fget_paramTextId(int i)
	{
		return 10000 * i + PARAMTEXT_END_ID;
	}
	protected int fget_paramLabelId(int i)
	{
		return 1000000 * i + PARAMLABEL_END_ID;
	}
	protected int fget_paramFormulaId(int i)
	{
		return 1000000000 * i + PARAMFORMULA_END_ID;
	}

	private void setPause(int iMode)
	{
		/*
		 SharedPreferences.Editor editor = Data.mSettings.edit();
		 editor.putInt("ISPAUSE",  iMode);
		 try{
		 editor.apply();
		 }catch (Exception e){
		 String s = e.toString();
		 s = s+"88";
		 }
		 */
	}

	private void stopserv(String s)
	{
		Intent intent = new Intent(this, coachService.class);
		intent.putExtra("CountOfAllIterations", Gl_CountOfAllIterations);
		if (s == "wait")
		{
			intent.putExtra(s, 1);
			startService(intent); //приостановить. не сбрасывать текущее состояние дел
		}
		else
		{
			intent.putExtra(s, 1);
		    stopService(intent);//завершить. сбросить текущее состояние дел   new Intent(this,coachService.class));
		}
	};
	public void resume(int pDelay, int pCountOfAllIterations, int pRepeat, Date  p_dBegTraining)
	{
		Intent intent = new Intent(this, coachService.class);
		//intent.putExtra("aRithmD", Datab.sARithmD);
		intent.putExtra("aRithm", Data.sARithm);
		
		intent.putExtra("aDeals100", Data.sADeals100);//test
		intent.putExtra("CountOfAllIterations", pCountOfAllIterations);
		intent.putExtra("Delay", pDelay);
		intent.putExtra("Repeat", pRepeat);
		intent.putExtra("nBegTraining", (int)(p_dBegTraining.getTime() - 1000000 * Math.floor(p_dBegTraining.getTime() / 1000000)));//  p_dBegTraining.getHours()*60*60*1000 +p_dBegTraining.getMinutes()*60*1000 + p_dBegTraining.getSeconds()*1000) );
		if (FLPAUSE) intent.putExtra("wait", 1); 
		startService(intent);
		
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		//перечитка массивов, мб сюда попали после редактирования
		getSettings("");//!!newform fillActivity();
	};	

	//проверки целостности бд
    protected String check()
	{
		String sRet = "";		
		String s ="";
		//1. имя параметра не должно содержаться в имени другого параметра (в пределах одной тренировки)
		for (int i=0; i < Data.aMetaRithm.length; i++)
			for (int j=1; j < Data.aMetaRithm[i][3][0].length; j++)
				for (int k=j + 1; k < Data.aMetaRithm[i][3][0].length; k++)

					if (Data.aMetaRithm[i][3][0][j][0].trim().contains(Data.aMetaRithm[i][3][0][k][0].trim()))
					{
						s = "Тренировка " + String.valueOf(i)  + "  " + Data.aMetaRithm[i][0][0][0][1] + "  Некорректно задан параметр: " + Data.aMetaRithm[i][3][0][k][0].trim() + "  Имя параметра содержится в имени другого параметра: " + Data.aMetaRithm[i][3][0][j][0].trim();
						sRet = s + "\n" + sRet;

						if (Data.aMetaRithm[i][3][0][k][0].trim().contains(Data.aMetaRithm[i][3][0][j][0].trim()))
						{
							s = "Тренировка #" + String.valueOf(i + 1) + "  " + Data.aMetaRithm[i][0][0][0][1] + "  Некорректно задан параметр: " + Data.aMetaRithm[i][3][0][j][0].trim() + "  Имя параметра содержится в имени другого параметра: " + Data.aMetaRithm[i][3][0][k][0].trim();
							sRet = s + "\n" + sRet;
						}		
					}

		return sRet;

	}

	@Override
	protected void onDestroy()
	{
		setPause(0);
		setSettings(""); 
		// TODO: Implement this method
		super.onDestroy();
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//! не гасить экран.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		GlParser1 = new MatchParser();
		GlF = new rithmFunc();
		//GlF.ev("toNum(1,22,333,4444)");
		Data = new coachUtility();
		Datab = new dataBaseInit();

		formatter = new SimpleDateFormat("HH:mm:ss");
		setContentView(R.layout.main);
		setTitle("Генератор ритмов");

		layout = findViewById(R.id.linearLayout);
		layout.setEnabled(false);
		ProgressBar progressBar = findViewById(R.id.progressBar);
		progressBar.setVisibility(ProgressBar.VISIBLE);
		layoutTags = findViewById(R.id.linearLayoutTags);
		layoutParams0 = findViewById(R.id.linearLayoutParams);

		progressBar.setVisibility(ProgressBar.INVISIBLE);
		tvTextToSpeek = findViewById(R.id.idTextsToSpeek);
		tvDelay = findViewById(R.id.idTxtDelay);
		tvRepeat = findViewById(R.id.idTxtRepeat);
		btnMain = findViewById(R.id.btnStartStop);
		btnFinish = findViewById(R.id.btnFinish);
		btnEdit = findViewById(R.id.btnEdit);
		btnTest = findViewById(R.id.btnTest);
		spinner = findViewById(R.id.chkTrain);	
		listView = findViewById(R.id.listView);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		chkToday = findViewById(R.id.chkToday);	
		chkToday.setChecked(isChkToday);
		btnSaveAll = findViewById(R.id.btnSaveAll);
		btnClone = findViewById(R.id.btnClone);
		btnDel = findViewById(R.id.btnDel);
		btnDisabledEdit = findViewById(R.id.btnDisablededit);
		btnLoad = findViewById(R.id.btnLoad);
//
		btnTest.setVisibility(View.GONE); //тестовая кнопка невидима
		btnLoad.setVisibility(View.GONE); //пока недоделано..
        listView.setVisibility(View.GONE);

		Gl_CountOfIterations = 0;
		TTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
				@Override
				public void onInit(int status)
				{
					//if(status != TextToSpeech.ERROR) {
					//	TTS.setLanguage(Locale.CHINESE);
					//}
				}
			});


		getSettings("");
        layout.setEnabled(true);
		progressBar.setVisibility(ProgressBar.INVISIBLE);

		//проверки целостности бд
		String s=check();
		//

		if (s.length() > 0)
		{

			btnMain.setEnabled(false);
			Toast toast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
			toast.show();
		}  
        else
			btnMain.setText(TXT_BTN_NOTSTARTED);
	};


	//начитка тэгов - со всех тренировок
	protected void getTags()
	{
		alTags.clear(); // при редактировании трен тэги могут добавляться. но не будут удаляться
		String s ="";
		if (!(alTags.indexOf(wordSelectedAll) >= 0)) 
			alTags.add(wordSelectedAll);
		int nTagCnt =-1;
		for (int i =0; i < Data.aMetaRithm.length; i++)
		{
			String[] aS =  Data.aMetaRithm[i][2][0][0][1].split("#");
			if (aS.length > 0)
				for (int j = 0; j < aS.length; j++)
					if (!(alTags.indexOf(aS[j]) >= 0)) 
						if (!(aS[j].trim() == ""))
						{
							nTagCnt++;
							s = aS[j];
							alTags.add(s);
						}
		}
		//alTags.sort( !!но все и вклискл дб всегда сверху!
		for (String s1: alTags)
		{
			if (!(s1.trim() == ""))
			{
				if (findViewById(fget_checkboxId(alTags.indexOf(s1))) == null)
				{
					//добавляем тэг на форму
					CheckBox cb = new CheckBox(this);
					cb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
					LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					cb.setText(s1);
					cb.setChecked(true);
					cb.setId(fget_checkboxId(alTags.indexOf(s1)));
					cb.setOnClickListener(new View.OnClickListener(){
							@Override 
							public void onClick(View v)
							{
								fillRithmList();
							}
						});
					layoutTags.addView(cb, lp);
				}
			}
		}
	}

	//начитка параметров - со всех тренировок и открыть видимость только для параметров текущей тренировки
	protected void getParameters()
	{

		layoutParams0 = findViewById(R.id.linearLayoutParams);
		layoutParams0.removeAllViews();
		//
		String[][] aS00 = Data.aMetaRithm[Gl_SelectedIndex][3][0];
		for (int ind=1; ind < aS00.length; ind++)//1 sic!
		{ 
			if (aS00.length > 1)
				if (!(aS00[ind][2].trim() == ""))
				{
					//название параметра
					TextView tv = new TextView(this);
					LayoutParams lp0 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					tv.setText("  " + aS00[ind][2].toLowerCase() + " (" + aS00[ind][0] + " ) :");
					tv.setId(fget_paramLabelId(1 + ind));
					layoutParams0.addView(tv, lp0);

					//значение параметра
					EditText edt = new EditText(this);
					lp0 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					String sVal = aS00[ind][3];
					if (aS00[ind][5].trim().length()>0)//если задана формула параметра
						sVal = GlF.ev(aS00[ind][5].trim());
					edt.setText(sVal);
					edt.setId(fget_paramTextId(1 + ind));
					layoutParams0.addView(edt, lp0);
					
					//формула - если задана
					if (aS00[ind][5].trim().length()>0){
						
						TextView tv1= new TextView(this);
						lp0 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						tv1.setText("  ! значение рассчитано по формуле:");//\""+ aS00[ind][2].toLowerCase()  +"\" рассчитано по формуле:");
						//tv1.setId(fget_paramLabelId(1 + ind));
						layoutParams0.addView(tv1, lp0);
						
						EditText edt1 = new EditText(this);
						lp0 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						edt1.setText(aS00[ind][5]);
						edt1.setTextSize(12);
						//edt1.setTextColor(Color.parseColor("#FFFFFF"));
						edt1.setId(fget_paramFormulaId(1 + ind));
						layoutParams0.addView(edt1, lp0);
					}
					
					
					
				}
		}

	}

	public void onClickFinish(View view)
	{
		//Intent intent = new Intent(this, coachService.class);

		//intent.set
		//stopService(intent);
		stopserv("exit");
		this.finish();
	}

    public void onClickMain(View view)
	{
		//FLPAUSE = !FLPAUSE;
        //запомнить дату выполнения (? перенести в сервис на окончание выполнения ?)
		Date d =new Date();
		Data.aMetaRithm[Gl_SelectedIndex][1][0][3][1] = d.toString();
		//Data.aRithm[1][0][3][1] = d.toString();
		//запомнить настройки
		setSettings("");

		//распарсить что можно до передачи в сервис
		parseSettings();

		//при запущенной тренировке (даже на паузе) отключаем реакцию кнопок 
		spinner.setEnabled(false);
		//listView.setEnabled(false);
		chkToday.setEnabled(false);
		btnEdit.setEnabled(false);
		btnSaveAll.setEnabled(false);
	    btnClone.setEnabled(false);
	    btnDisabledEdit.setEnabled(false);
	    btnDel.setEnabled(false);
	    btnLoad.setEnabled(false);
		tvDelay.setEnabled(false);
		tvRepeat.setEnabled(false);
		tvTextToSpeek.setEnabled(false);

		if (Gl_SelectedIndex < 0)
		{

		}
		if (btnMain.getText() == TXT_BTN_FINISHED)
		{

		}
		else
		{	
			if (((btnMain.getText() == TXT_BTN_NOTSTARTED) || (btnMain.getText() == TXT_BTN_PAUSED)))
			{ 
				Date dNow = new Date();
			    if (btnMain.getText() == TXT_BTN_PAUSED)
				{
					/**/
					setPause(0);
					speak("тренировка продолжена"); 
					//???	resume(1 * 1000, Gl_CountOfAllIterations, 1, dNow); //&&&  не работает отсрочка
				}
				else
				{
					//отсрочка старта
					try
					{
						GlnDelay = Integer.parseInt(tvDelay.getText().toString());
					}
					catch (Exception e)
					{
						GlnDelay = 5;
					}
					//количество повторений тренировки
					try
					{
						GlnRepeat = Integer.parseInt(tvRepeat.getText().toString());
					}
					catch (Exception e)
					{
						GlnRepeat = 1;
					}

					if (GlnDelay > 5)
						speak("до начала тренировки " + String.valueOf(GlnDelay) + "секунд");
					GltxtSpeek = "";

					for (int j=0; j < Data.aRithm[10].length; j++)
						Gl_CountOfAllIterations +=  Double.parseDouble(Data.aRithm[10][j][0][0]);

					resume(GlnDelay * 1000, Gl_CountOfAllIterations, GlnRepeat, dNow); 
				}
				btnMain.setText(TXT_BTN_STARTED);

			}
			else
			{	
				try
				{
					/**/setPause(1);
					btnMain.setText(TXT_BTN_PAUSED);
					//??				stopserv("wait");
					speak("тренировка приостановлена"); 
				}
				catch (Exception e)
				{
					String s = e.toString();
					s = s + "88";
				}
			}
		}
	}

	public void speak(String s)
	{
		try
		{
			TTS.speak  (s, TextToSpeech.QUEUE_FLUSH, null); 
		}
		catch (Exception e)
		{
			Toast.makeText(getApplicationContext(),
						   "Exception: " + e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	public void onClickChkToday(View view)
	{
		isChkToday = !isChkToday;
		getSettings(""); //newform fillActivity();  //ОНО НАДО???
	}

	public boolean IsInSelectedTags(String str)
	{
		if (!(alTags.size() > 0)) return true;	
		boolean flret = false;	
		int i = 0;
		while (!flret && (i < alTags.size()))
		{
			CheckBox cb = new CheckBox(this);
			cb =  findViewById(fget_checkboxId(i));
			flret = (alTags.get(i).equalsIgnoreCase(str)) && (cb.isChecked()) ;
			i++;
		}
		return flret;
	}

	public boolean IsRithmInSelectedTags(int index)
	{
		if (!(alTags.size() > 0)) return true;
		if (alTags.indexOf(wordSelectedAll) >= 0)
		{ 
			CheckBox cb = new CheckBox(this);
			cb =  findViewById(fget_checkboxId(alTags.indexOf(wordSelectedAll)));
			if (cb.isChecked()) 
				return true;
		}	
		boolean flRet = false;
		for (String st:Data.aMetaRithm[index][2][0][0][1].split("#"))
		{
			if (IsInSelectedTags(st))
			{
				flRet = true;
				break;
			}  
		}
		return flRet;	
	}

	public void fillRithmList()
	{
		alSpinner.clear();
		aliSpinner.clear();
		for (int i = 0; i < Data.aMetaRithm.length; i++)
		{// начитываем в массив список доступных тренировок
		    try
			{   boolean flHasParent = hasParent(i);
				boolean flHasChildren = hasChildren(i);
			    if (!flHasChildren) //временно - показываем только листы. затем нужно только верхние т.е. (!(hasParent(i) ))
					if (!(GlParser1.Parse(Data.aMetaRithm[i][1][0][0][1]) > 0) | !isChkToday)
						if (IsRithmInSelectedTags(i)) 
						{   
							String s = "";
							Date d = new Date();
							try
							{
								Date d1 = new Date(Date.parse(Data.aMetaRithm[i][1][0][3][1]));
								if (d.getDate() == d1.getDate()) s = "СЕГОДНЯ ВЫПОЛНЯЛАСЬ - "; else s = "";
							}
							catch (Exception e)
							{
								s = "";
							}  
							if (flHasChildren) s = ">> " + s;
							//if (d.getDate()==d1.getDate()) s = "СЕГОДНЯ ВЫПОЛНЯЛАСЬ - "; else s= "";
							alSpinner.add(s + 
										  Data.aMetaRithm[i][0][0][0][1]);
							aliSpinner.add(i);
						}	
		    }
			catch (Exception e)
			{
				Toast.makeText(getApplicationContext(),
							   "Exception: " + e.toString(), Toast.LENGTH_LONG).show();
			}		
		}	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.  simple_spinner_item/*.simple_list_item_single_choice .simple_spinner_item*/, alSpinner);
		//adapter.setDropDownViewResource(android.R.layout.simple_gallery_item);
//
		//<com.mycompany.myapp.VerticalScrollview
//******************??		
		spinner.setAdapter(adapter);

		//  listView.setAdapter(adapter);

		OnItemSelectedListener 
			//OnItemClickListener
			itemSelectedListener = new  OnItemSelectedListener()
		{  
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{


				Gl_SelectedIndex = aliSpinner.get(position); //???????
				Gl_SelectedID = Data.aMetaRithm[Gl_SelectedIndex][0][0][1][1];

				//boolean flHasParent = hasParent(po);
				/*
				 boolean flHasChildren = hasChildren(Gl_SelectedIndex);
				 if (flHasChildren) {

				 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, alSpinner);
				 adapter.setDropDownViewResource(android.R.layout.simple_gallery_item);
				 spinner.setAdapter(adapter);

				 }//  .inflate( .//fillSpinner();//onItemSelected(parent,view,position,id);
				 */
				btnMain.setText(TXT_BTN_NOTSTARTED);
				tvTextToSpeek.setText(Data.aMetaRithm[Gl_SelectedIndex][2][0][0][1]);
				tvDelay.setText(Data.aMetaRithm[Gl_SelectedIndex][1][0][2][1]);
				tvRepeat.setText(Data.aMetaRithm[Gl_SelectedIndex][1][0][1][1]);
				tvTextToSpeek.setText(Data.aMetaRithm[Gl_SelectedIndex][2][0][0][1]);
				getTags();//?? оно надо  ??
				getParameters();
				//spinner.setSelection(position);//111

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{		
			}
		};
//*****************
		spinner.setOnItemSelectedListener(itemSelectedListener);
		////listView .setOnItemSelectedListener(itemSelectedListener);
		/*listView.setOnItemClickListener(new OnItemClickListener() {

		 @Override
		 public void onItemClick(AdapterView<?> arg0, View view, int position,
		 long j) {
		 Gl_SelectedIndex = aliSpinner.get( position); //???????
		 Gl_SelectedID = Data.aMetaRithm[Gl_SelectedIndex][0][0][1][1];


		 btnMain.setText(TXT_BTN_NOTSTARTED);
		 tvTextToSpeek.setText(Data.aMetaRithm[Gl_SelectedIndex][2][0][0][1]);
		 tvDelay.setText(Data.aMetaRithm[Gl_SelectedIndex][1][0][2][1]);
		 tvRepeat.setText(Data.aMetaRithm[Gl_SelectedIndex][1][0][1][1]);
		 tvTextToSpeek.setText(Data.aMetaRithm[Gl_SelectedIndex][2][0][0][1]);
		 getTags();//?? оно надо  ??
		 getParameters();

		 }
		 });
		 */
	}
    //

	//<<ФОРМА РЕДАКТИРОВАНИЯ
	public void onClickEdit(View view)
	{  //speak("<silence msec=\"2000\"/>");
		//Toast.makeText(getApplicationContext(),
			//		   " <silence msec=\"2000\"/> ", Toast.LENGTH_LONG).show();
		
		getSettings(""); 
		setSettings("");
		Intent intent = new Intent(MainActivity.this, EditFormActivity.class);
		startActivity(intent);
	}
	//>>

	//для генерации уникального ид и наименования при копировании	
	private String sMakeUniqueWord(String sSource, String sAddWord, int nInd2Compare)
	{
		int nN =1;
		boolean fl = true;
		String sret = sAddWord + String.valueOf(nN) + "_" + sSource;
		while (fl)
		{
			fl = false;
			for (int i=0; i < Data.aMetaRithm.length; i++)
				fl = fl || (Data.aMetaRithm[i][0][0][nInd2Compare][1].equals(sret));
			if (fl)
			{
				nN += 1;	
				sret = sAddWord + String.valueOf(nN) + "_" + sSource;
			}
		}	

		return sAddWord + String.valueOf(nN) + "_" + sSource;
	}

	//<<сохранить в файл
	public void onClickSaveToFile(View view)
	{

        setSettings("");
		// проверяем доступность SD
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			//?? Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
			return;
		}

		SharedPreferences prefs = getSharedPreferences(Data.SET_AMETARITHM_FILE_NAME, Context.MODE_PRIVATE);
        String spref=Data.prefs2string(prefs);
		File myPath = //new File( fileLoad);// 
			new File(Environment.getExternalStorageDirectory().toString());
		File myFile = new File(myPath, "MySharedPreferences");

		try
		{
			if (!myPath.exists()) myPath.mkdir();
			FileWriter fw = new FileWriter(myFile);
			PrintWriter pw = new PrintWriter(fw);
			/*
			 Map<String,?> prefsMap = prefs.getAll();

			 for (Map.Entry<String,?> entry : prefsMap.entrySet())
			 {
			 pw.println(entry.getKey() + Data.DELIMITER  + entry.getValue().toString() + Data.DELIMITER);            
			 }
			 */
			pw.println(spref);            

			pw.close();
			fw.close();
			Toast.makeText(getApplicationContext(),
						   "Сохранено в: " + myFile, Toast.LENGTH_LONG).show();

		}
		catch (Exception e)
		{
			Toast.makeText(getApplicationContext(),
						   "Exception: " + e.toString(), Toast.LENGTH_LONG).show();
		}
	}
	//>>
	private static final int FILE_LOAD_CODE = 0;
    private String fileLoad="";
	private void showFileChooser(int nmode)
	{
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
		intent.setType("*/*"); 
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		try
		{
			startActivityForResult(
                Intent.createChooser(intent, "Select a File"),
                FILE_LOAD_CODE);
		}
		catch (android.content.ActivityNotFoundException ex)
		{
			// Potentially direct the user to the Market with a Dialog
			Toast.makeText(this, "Please install a File Manager.", 
						   Toast.LENGTH_SHORT).show();
		}
	}

	/**/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case FILE_LOAD_CODE:
				if (resultCode == RESULT_OK)
				{
					// Get the Uri of the selected file 
					Uri uri = data.getData();
//   String ss =data.getDataString();
					//File file = new File( uri.toString());// .getPath());
					fileLoad = uri.getEncodedPath();// uri.getPath();// .toString() file.getAbsolutePath();// Path();// uri.getPath().toString();// FileUtils .getPath(this, uri);
					fileLoad = uri. getQuery();
					fileLoad = uri.getEncodedUserInfo();
					fileLoad = "/document/primary:MySharedPreferences";//  "/storage/emulated/0/MySharedPreferences";
					File file = new File(fileLoad);// 
					String sPref = "";

					try
					{
						if (file.isFile())// .exists())
						{

							FileInputStream fis;

							try
							{
								fis = openFileInput(fileLoad);// "test.txt");
								fis.read(sPref.getBytes());//readString.getBytes());
								fis.close();
								Toast.makeText(this, sPref,
											   Toast.LENGTH_LONG).show();


							}
							catch (IOException e)
							{
								e.printStackTrace();

							} 
						}
					}
					catch (Exception e)
					{Toast.makeText(this, e.toString(),
									Toast.LENGTH_LONG).show();
					}}
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**/
	//<<загрузить из файла (новые и изменения старых)
	public void onClickLoadFromFile(View view)
	{
		showFileChooser(FILE_LOAD_CODE);
	}
	//>>

	//<<удалить тренировку
	public void onClickDel(View view)
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Удаление");
		alert.setMessage("Удалить тренировку " + Data.aMetaRithm[Gl_SelectedIndex][0][0][0][1] + "?");

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton)
				{

					String[][][][][] array = Data.aMetaRithm; // Original array.
					String[][][][][] acopy = new String[array.length - 1][][][][]; // Array which will contain the result.

					System.arraycopy(array, 0, acopy, 0, Gl_SelectedIndex);

					System.arraycopy(array, Gl_SelectedIndex + 1, acopy, Gl_SelectedIndex, array.length - Gl_SelectedIndex - 1);
					Gl_SelectedIndex = 0;
					Gl_SelectedID = acopy[Gl_SelectedIndex][0][0][1][1];
					Data.aMetaRithm = acopy;
					Data.aRithm = Data.aMetaRithm[Gl_SelectedIndex];
					getSettings("FROM_ARRAYS");  
					//запомнить настройки
					setSettings(""); 
                    //

					Toast.makeText(getApplicationContext(),
								   "Тренировка удалена" , Toast.LENGTH_SHORT).show();
				}
			});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton)
				{
					// Canceled.
				}
			});

		alert.show();

    }
	//>>

	//<<загрузить из файла (новые и изменения старых)
	public void onClickClone(View view)
	{
		//Toast.makeText(getApplicationContext(),
		//GlF.ev("rithmLinearFromDate2Date(на дату,10.11.2020,линейно от даты, 01.09.2020, от значения, 10, до даты,01.09.2022, до значения, 20)")
		//			   , Toast.LENGTH_SHORT).show();
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Копирование");
		alert.setMessage(
			//GlF.ev("rithmLinearFromDate2Date(на дату,10112020,линейно от даты, 01092020, от значения, 10, до даты,01092022, до значения, 20)")
		    "Введите наименование новой тренировки:"
		     );

		final EditText input = new EditText(this);
		input.setText(sMakeUniqueWord(Data.aMetaRithm[Gl_SelectedIndex][0][0][0][1], "КОПИЯ", 0));
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton)
				{
					String sNewName = input.getText().toString();
					//добавляем клон в массив
					String[][][][][] acopy = new String[Data.aMetaRithm.length + 1][][][][];
				    for (int i = 0; i < Gl_SelectedIndex + 1;i++)
					{
						acopy[i] = new String[Data.aMetaRithm[i].length][][][];
						for (int i1=0;i1 < Data.aMetaRithm[i].length;i1++)
						{
							acopy[i][i1] = new String[Data.aMetaRithm[i][i1].length][][];
							for (int i2=0;i2 < Data.aMetaRithm[i][i1].length;i2++)
							{
								acopy[i][i1][i2] = new String[Data.aMetaRithm[i][i1][i2].length][];
								for (int i3=0;i3 < Data.aMetaRithm[i][i1][i2].length;i3++)
								{
									acopy[i][i1][i2][i3] = new String[Data.aMetaRithm[i][i1][i2][i3].length];
									for (int i4=0;i4 < Data.aMetaRithm[i][i1][i2][i3].length;i4++)
									{
										acopy[i][i1][i2][i3][i4] = Data.aMetaRithm[i][i1][i2][i3][i4] ;
									}
								}
							}
						}
					} 

				    acopy[Gl_SelectedIndex + 1] = new String[Data.aMetaRithm[Gl_SelectedIndex].length][][][];
					for (int i1=0;i1 < Data.aMetaRithm[Gl_SelectedIndex].length;i1++)
					{
						acopy[Gl_SelectedIndex + 1][i1] = new String[Data.aMetaRithm[Gl_SelectedIndex][i1].length][][];
						for (int i2=0;i2 < Data.aMetaRithm[Gl_SelectedIndex][i1].length;i2++)
						{
							acopy[Gl_SelectedIndex + 1][i1][i2] = new String[Data.aMetaRithm[Gl_SelectedIndex][i1][i2].length][];
							for (int i3=0;i3 < Data.aMetaRithm[Gl_SelectedIndex][i1][i2].length;i3++)
							{
								acopy[Gl_SelectedIndex + 1][i1][i2][i3] = new String[Data.aMetaRithm[Gl_SelectedIndex][i1][i2][i3].length];
								for (int i4=0;i4 < Data.aMetaRithm[Gl_SelectedIndex][i1][i2][i3].length;i4++)
								{
									acopy[Gl_SelectedIndex + 1][i1][i2][i3][i4] = Data.aMetaRithm[Gl_SelectedIndex][i1][i2][i3][i4] ;
								}
							}
						}
					}
					acopy[Gl_SelectedIndex + 1][0][0][1][1] = sMakeUniqueWord(acopy[Gl_SelectedIndex][0][0][1][1] , "copy", 1);
					if (Data.aMetaRithm.length > (Gl_SelectedIndex + 1))
					{
						for (int i = Gl_SelectedIndex + 2; i < Data.aMetaRithm.length + 1;i++)
						{
							acopy[i] = new String[Data.aMetaRithm[i - 1].length][][][];
							for (int i1=0;i1 < Data.aMetaRithm[i - 1].length;i1++)
							{
								acopy[i][i1] = new String[Data.aMetaRithm[i - 1][i1].length][][];
								for (int i2=0;i2 < Data.aMetaRithm[i - 1][i1].length;i2++)
								{
									acopy[i][i1][i2] = new String[Data.aMetaRithm[i - 1][i1][i2].length][];
									for (int i3=0;i3 < Data.aMetaRithm[i - 1][i1][i2].length;i3++)
									{
										acopy[i][i1][i2][i3] = new String[Data.aMetaRithm[i - 1][i1][i2][i3].length];
										for (int i4=0;i4 < Data.aMetaRithm[i - 1][i1][i2][i3].length;i4++)
										{
											acopy[i][i1][i2][i3][i4] = Data.aMetaRithm[i - 1][i1][i2][i3][i4] ;
										}
									}
								}
							}
						} 
					}	
					acopy[Gl_SelectedIndex + 1][0][0][0][1] = sNewName;
					Gl_SelectedIndex = Gl_SelectedIndex + 1;
					Gl_SelectedID = acopy[Gl_SelectedIndex][0][0][1][1];
					Data.aMetaRithm = acopy;
					Data.aRithm = Data.aMetaRithm[Gl_SelectedIndex];
					getSettings("FROM_ARRAYS"); 
					//запомнить настройки
					setSettings(""); 

//************  ??????? indexof
					spinner.setSelection(aliSpinner.indexOf(Gl_SelectedIndex));
					//listView.setSelection(aliSpinner.indexOf( Gl_SelectedIndex));

					getParameters();
					Toast.makeText(getApplicationContext(),
								   "Тренировка скопирована" , Toast.LENGTH_SHORT).show();
				}
			});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton)
				{
					// Canceled.
				}
			});

		alert.show();


	}
	//>>
	//<<Изменить формулу отбора тренировки в список на текущий день
	public void onClickDisabledEdit(View view)
	{

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(Data.aMetaRithm[Gl_SelectedIndex][0][0][0][1]);
		alert.setMessage("Введите формулу отбора:");

		final EditText input = new EditText(this);
		input.setText(Data.aMetaRithm[Gl_SelectedIndex][1][0][0][1]);

		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton)
				{

					String sNewName = input.getText().toString();
					Data.aMetaRithm[Gl_SelectedIndex][1][0][0][1] = sNewName;

					getSettings("FROM_ARRAYS"); //?????????????
					//запомнить настройки
					setSettings(""); 
					//getSettings("FROM_ARRAYS"); //????????????
//*************???????
					spinner.setSelection(aliSpinner.indexOf(Gl_SelectedIndex));
					//listView.setSelection( aliSpinner.indexOf( Gl_SelectedIndex));
					getParameters();
					Toast.makeText(getApplicationContext(),
								   "Формула изменена" , Toast.LENGTH_SHORT).show();
				}
			});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton)
				{
					// Canceled.
				}
			});
		alert.show();
	}
	//>>



    public void onClickTest(View view)
	{
		if (IsRithmInSelectedTags(2))  tvTextToSpeek.setText("yyyyyes"); 
		else  tvTextToSpeek.setText("nnnnno") ;
    }

    protected void getSettings(String sFile)
	{
		//взять параметры из настроек/файла(если sFile непуст) или из умолчаний. 
		//записать их в массивы и переменные. 
		//заполнить спиннер(+выбранный индекс даже если он запрещен к выбору) 
		//нарисовать на форме методом выбора из спиннера (delay repeat tags parameters.). 
		//----------------------------
		Data.mSettings = getSharedPreferences(Data.SET_FILE_NAME, Context.MODE_PRIVATE);
        Data.mSettingsData = getSharedPreferences(Data.SET_AMETARITHM_FILE_NAME, Context.MODE_PRIVATE);
		if (sFile.isEmpty())
		{
			//взять параметры из настроек или из умолчаний. 
			if   (Data.mSettingsData.contains(Data.SET_AMETARITHM))
			{
				Data.sAMetaRithm = Data.mSettingsData.getString(Data.SET_AMETARITHM, "");
				Data.sADeals100 = Data.mSettingsData.getString(Data.SET_ADEALS100, "");
			}
			else
			{
				//else из модуля
				String ss = Data.aMetaRithm2TechString(Data.aMetaRithmInit);
				Data.sAMetaRithm = ss;
				ss = Data.aRithm2TechString(Data .aDeals100Init);
				Data.sADeals100 = ss;
			}		
			Data.mSplit5d(Data.sAMetaRithm);
        }

		//выбор тренировки		
		fillRithmList();
		if (Data.mSettings.contains(Data.SET_TRAIN_INDEX))
		{
//**************
			spinner.setSelection(aliSpinner.indexOf(Data.mSettings.getInt(Data.SET_TRAIN_INDEX, 0)));
			//listView.setSelection(aliSpinner.indexOf(Data.mSettings.getInt(Data.SET_TRAIN_INDEX, 0)));
		}
	}


    protected void setSettings(String sFile)
	{
		//из формы в массивы и переменные и в настройки 
		//и если sFile непуст то еще и в файл. 
		//---------------------------
		//if (sFile.isEmpty()) {
		//считываем измененные (мб) параметры
		//int ii=1;
		String[][] aS00 = Data.aMetaRithm[Gl_SelectedIndex][3][0];
		for (int i=1; i < aS00.length; i++)
		{ 
			//if (Integer.parseInt(alParametersFullInd.get(i)) == Gl_SelectedIndex)
			{
				edtCurr = findViewById(fget_paramTextId(1 + i));	
				Data.aMetaRithm[Gl_SelectedIndex][3][0][i][3] = edtCurr.getText().toString().trim();
				
				edtCurr = findViewById(fget_paramFormulaId(1 + i));	
				if (!(edtCurr==null))
				  Data.aMetaRithm[Gl_SelectedIndex][3][0][i][5] = edtCurr.getText().toString().trim();
				//alParametersV.set(ii - 1, edtCurr.getText().toString().trim());
				//alParametersV.set(ii - 1, edtCurr.getText().toString().trim());

				//заменим значение параметра в массиве, чтобы сохранилось в настройках
				//Data.aMetaRithm[Gl_SelectedIndex][3][0][ii][3] = alParametersFullV.get(i);
				Data.aRithm = Data.aMetaRithm[Gl_SelectedIndex];
				//Data.aRithm[3][0][ii][3] = alParametersV.get(ii - 1);
				//ii += 1;
			}
		}

		//запомнить настройки
		Data.aMetaRithm[Gl_SelectedIndex][1][0][2][1] = tvDelay.getText().toString();
		Data.aMetaRithm[Gl_SelectedIndex][1][0][1][1] = tvRepeat.getText().toString();

		String ss = Data.aMetaRithm2TechString(Data.aMetaRithm);
		Data.sAMetaRithm = ss;
		Data.aRithm = Data.aMetaRithm[Gl_SelectedIndex];
		ss = Data.aRithm2TechString(Data.aRithm);
		Data.sARithm = ss;
		SharedPreferences.Editor editor = Data.mSettings.edit();
		editor.putInt(Data.SET_TRAIN_INDEX,  Gl_SelectedIndex);
		editor.putString(Data.SET_TRAIN_ID,  Gl_SelectedID);

		editor.apply();
		editor = Data.mSettingsData.edit();
		editor.putString(Data.SET_AMETARITHM, Data.sAMetaRithm);
		editor.putString(Data.SET_ARITHM, Data.sARithm);
		editor.putString(Data.SET_ADEALS100, Data.sADeals100);
		editor.apply();
	}

	protected void parseSettings()
	{
		Data.aRithm = Data.aMetaRithm[Gl_SelectedIndex];
		Data.sARithm = Data.aRithm2TechString(Data.aRithm);
		//распарсить массивы и пр. 
		//вызывается перед передачей в сервис, сохраняться в базе не должно	 
		//---------------------------

		//парсим по параметрам количества подходов, длительность и пр
		for (int i=0; i < Data.aRithm[3][0].length - 1; i++)
			try
			{
				GlParser1.setVariable(Data.aRithm[3][0][i + 1][0].trim(), Double.parseDouble(Data.aRithm[3][0][i + 1][3].trim()));
			}
			catch (Exception e)
			{
				//Toast.makeText(getApplicationContext(),
				//			   e.toString() , Toast.LENGTH_LONG).show();

			}
		//наименование как парсить ??

		for (int i=0; i < Data.aRithm[10].length; i++)
		{
			try
			{
				//кол-во повторений
				Data.aRithm[10][i][0][0] = String.valueOf((int)GlParser1.Parse(Data.aRithm[10][i][0][0]));
				Data.aMetaRithm[Gl_SelectedIndex][10][i][0][0] = Data.aRithm[10][i][0][0];

				//длительность (в ритме, а не в деле)
				Data.aRithm[10][i][1][0] = String.valueOf(GlParser1.Parse(Data.aRithm[10][i][1][0]));
				Data.aMetaRithm[Gl_SelectedIndex][10][i][1][0] = Data.aRithm[10][i][1][0];

				//дисперсия (в ритме, а не в деле)
				Data.aRithm[10][i][2][0] = String.valueOf(GlParser1.Parse(Data.aRithm[10][i][2][0]));
				Data.aMetaRithm[Gl_SelectedIndex][10][i][2][0] = Data.aRithm[10][i][2][0];

				//если сразу задана фраза а не ссылка на дело
				Data.aRithm[10][i][3][1] = GlParser1.SParse(Data.aRithm[10][i][3][1]);
				Data.aMetaRithm[Gl_SelectedIndex][10][i][3][1] = Data.aRithm[10][i][3][1];

			}
			catch (Exception e)
			{
				Toast.makeText(getApplicationContext(),
							   e.toString() , Toast.LENGTH_LONG).show();
			}  
		}
		String ss = Data.aMetaRithm2TechString(Data.aMetaRithm);
		Data.sAMetaRithm = ss;
		ss = Data.aRithm2TechString(Data.aRithm);
		Data.sARithm = ss;
	}

	//имеются ди потомки тренировки (лист или нет)
	public boolean hasChildren(int index)
	{
		boolean fret = false;
		for (int i=0; i < Data.aMetaRithm.length; i++)
			if (Data.aMetaRithm[index][0][0][1][1].equals(Data.aMetaRithm[i][0][0][2][1]))
			{
				fret = true;  
				break;
			}	
		return fret;  
	}
	//имеются ли родитель тренировки (показывать ли ее в выборе спиннера)
	public boolean hasParent(int index)
	{
		boolean fret = false;
		for (int i=0; i < Data.aMetaRithm.length; i++)
			if (Data.aMetaRithm[index][0][0][2][1].equals(Data.aMetaRithm[i][0][0][1][1]))
			{
				fret = true;  
				break;
			}	
		return fret;  
	}
}
