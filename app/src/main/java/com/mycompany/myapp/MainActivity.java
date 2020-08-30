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

public class MainActivity extends Activity
{

	@Override

	final String wordSelectedAll = "Все";

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

	ArrayList<String> alParameters = new ArrayList<String>();
	ArrayList<String> alParametersV = new ArrayList<String>();
	ArrayList<String> alParametersBrief = new ArrayList<String>();
	ArrayList<String> alParametersType = new ArrayList<String>();
	ArrayList<String> alParametersFull = new ArrayList<String>();
	ArrayList<String> alParametersFullV = new ArrayList<String>();
	ArrayList<String> alParametersFullV2 = new ArrayList<String>();
	ArrayList<String> alParametersFullBrief = new ArrayList<String>();
	ArrayList<String> alParametersFullType = new ArrayList<String>();
	ArrayList<String> alParametersFullInd = new ArrayList<String>();


	int Gl_SelectedIndex = 0;
	String Gl_SelectedID = "";

	private LinearLayout layout;
	private LinearLayout layout0;
	private Button btnMain;
	private Button btnFinish;
	private Button btnTest;
	private Button btnEdit;
	private Spinner spinner;
	private CheckBox chkAll;

	private boolean isChkAll = false;
	private coachUtility Data;

	private MatchParser GlParser1;

	//для динамической генерации вьюх на форме
	protected int fget_checkboxId(int i)
	{
		return 1000 * i + 10;
	}
	protected int fget_paramTextId(int i)
	{
		return 100000 * i + 20;
	}

	protected int fget_paramLabelId(int i)
	{
		return 100000000 * i + 30;
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
		    stopService(intent);//завершить. сбросить текущее состояние дел   new Intent(this,coachService.class));
	};
	public void resume(int pDelay, int pCountOfAllIterations, int pRepeat, Date  p_dBegTraining)
	{
		Intent intent = new Intent(this, coachService.class);
		intent.putExtra("aRithm", Data.sARithm);
		intent.putExtra("aDeals100", Data.sADeals100);//test
		intent.putExtra("CountOfAllIterations", pCountOfAllIterations);
		intent.putExtra("Delay", pDelay);
		intent.putExtra("Repeat", pRepeat);
		intent.putExtra("nBegTraining", (int)(p_dBegTraining.getTime() - 1000000 * Math.floor(p_dBegTraining.getTime() / 1000000)));//  p_dBegTraining.getHours()*60*60*1000 +p_dBegTraining.getMinutes()*60*1000 + p_dBegTraining.getSeconds()*1000) );

		startService(intent);
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		//перечитка массивов, мб сюда попали после редактирования
		fillActivity();
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
		//if(j!=k){
					if (Data.aMetaRithm[i][3][0][j][0].trim().contains(Data.aMetaRithm[i][3][0][k][0].trim()))
					{
						s = "Тренировка " + String.valueOf(i)  + "  " + Data.aMetaRithm[i][0][0][0][1] + "  Некорректно задан параметр: " + Data.aMetaRithm[i][3][0][k][0].trim() + "  Имя параметра содержится в имени другого параметра: " + Data.aMetaRithm[i][3][0][j][0].trim();
						sRet = s + "\n" + sRet;
						//Toast toast = Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG);
						//toast.show();
						//}
						if (Data.aMetaRithm[i][3][0][k][0].trim().contains(Data.aMetaRithm[i][3][0][j][0].trim()))
						{
							s = "Тренировка #" + String.valueOf(i + 1) + "  " + Data.aMetaRithm[i][0][0][0][1] + "  Некорректно задан параметр: " + Data.aMetaRithm[i][3][0][j][0].trim() + "  Имя параметра содержится в имени другого параметра: " + Data.aMetaRithm[i][3][0][k][0].trim();
							sRet = s + "\n" + sRet;
						}		
					}

		return sRet;

	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//! не гасить экран.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		GlParser1 = new MatchParser();
		Data = new coachUtility();
		Data.mSettings = getSharedPreferences(Data.SET_FILE_NAME, Context.MODE_PRIVATE);
        Data.mSettingsData = getSharedPreferences(Data.SET_AMETARITHM_FILE_NAME, Context.MODE_PRIVATE);

		formatter = new SimpleDateFormat("HH:mm:ss");
		setContentView(R.layout.main);
		setTitle("Генератор ритмов");

		layout = findViewById(R.id.linearLayout);
		tvTextToSpeek = findViewById(R.id.idTextsToSpeek);
		tvDelay = findViewById(R.id.idTxtDelay);
		tvRepeat = findViewById(R.id.idTxtRepeat);
		btnMain = findViewById(R.id.btnStartStop);
		btnFinish = findViewById(R.id.btnFinish);
		btnEdit = findViewById(R.id.btnEdit);
		btnTest = findViewById(R.id.btnTest);
		spinner = findViewById(R.id.chkTrain);	
		chkAll = findViewById(R.id.chkAll);	

		btnTest.setVisibility(View.GONE); //тестовая кнопка невидима
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


		fillActivity();

		//проверки целостности бд
		String s=check();
		if (s.length() > 0)
		{
			tvDelay.setText(s);	
			//btnMain.setText(s);
			btnMain.setEnabled(false);
			Toast toast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
			toast.show();
		}  
        else
	    {		
			setParameters(); //оно надо? уже есть в fillactivity. переделать форму надо..
			btnMain.setText(TXT_BTN_NOTSTARTED);
		}
	};


	//начитка тэгов
	protected void setTags()
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
								fillSpinner();
							}
						});
					layout.addView(cb, lp);
				}
			}
		}

	}

	//начитка параметров
	protected void setParameters()
	{

		//btnEdit.setText(String.valueOf(Gl_SelectedIndex));

		layout0 = findViewById(R.id.linearLayout);
		for (int i=0; i < alParametersFull.toArray().length; i++)
		{
			layout0.removeView(findViewById(1 + fget_paramTextId(i)));
			layout0.removeView(findViewById(1 + fget_paramLabelId(i)));
		}
		alParameters.clear(); //при редактировании трен параметры могут добавляться. но не будут удаляться
		alParametersV.clear(); 
		alParametersBrief.clear();
		alParametersType.clear();

		alParametersFull.clear(); //при редактировании трен параметры могут добавляться. но не будут удаляться
		alParametersFullV.clear(); 
		alParametersFullBrief.clear();
		alParametersFullType.clear();
		alParametersFullInd.clear();

		String s0 ="";
		int nPCnt =-1;
		String[][] aS0;
		for (int ind=0; ind < Data.aMetaRithm.length; ind++)
		{
			aS0 = Data.aMetaRithm[ind][3][0];
			if (aS0.length > 1)
				for (int j = 1; j < aS0.length; j++)
			//!!! if (!(alParameters.indexOf(aS0[j]) >= 0)) 
					if (!(aS0[j][2].trim() == ""))
					{
						if (ind == Gl_SelectedIndex)
						{	
							nPCnt++;
							s0 = aS0[j][2];
							alParameters.add(s0);
							s0 = aS0[j][3];
							alParametersV.add(s0);
							s0 = aS0[j][0];
							alParametersBrief.add(s0);
							s0 = aS0[j][1];
							alParametersType.add(s0);
						}	
						s0 = aS0[j][2];
						alParametersFull.add(s0);
						s0 = aS0[j][3];
						alParametersFullV.add(s0);
						s0 = aS0[j][0];
						alParametersFullBrief.add(s0);
						s0 = aS0[j][1];
						alParametersFullType.add(s0);
						alParametersFullInd.add(String.valueOf(ind));
					}
		}

		//
		if ((alParameters.toArray().length > 0)
			&& (findViewById(fget_paramTextId(1) + 2) == null)
			)
		{
			TextView tv = new TextView(this);
			LayoutParams lp00 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			tv.setText("ПАРАМЕТРЫ ТРЕНИРОВКИ:");
			tv.setId(fget_paramTextId(1) + 2); 
			layout0.addView(tv, lp00);
		}

		for (int i=0;i < alParametersFull.toArray().length; i++)
		{
			if (!(alParametersFull.get(i).trim() == ""))
			{
				if (findViewById(fget_paramLabelId(1 + i)) == null)
				{
					//название параметра
					TextView tv = new TextView(this);
					LayoutParams lp0 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					tv.setText("  " + alParametersFull.get(i).toLowerCase() + ":");
					tv.setId(fget_paramLabelId(1 + i));
					layout0.addView(tv, lp0);
				}
				else
				{
					TextView tv1 =findViewById(fget_paramLabelId(1 + i));
					if (Integer.parseInt(alParametersFullInd.get(i)) == Integer.parseInt(String.valueOf(Gl_SelectedIndex)))
						tv1.setVisibility(View.VISIBLE);
					else 
						tv1.setVisibility(View.GONE);
				}

				if (findViewById(fget_paramTextId(1 + i)) == null)
				{   
				    //значение параметра
					EditText edt = new EditText(this);
					LayoutParams lp0 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					edt.setText(alParametersFullV.get(i));
					edt.setId(fget_paramTextId(1 + i));
					/*
					 edt.addTextChangedListener(new TextWatcher() {  
					 public void afterTextChanged(Editable s) {}  
					 public void beforeTextChanged(CharSequence s, int start, int count, int after) {}  
					 public void onTextChanged(CharSequence s, int start, int before, int count) {  
					 // tvTextToSpeek.setText(s.toString());
					 // alParametersV2.add(0,s.toString());
					 }  
					 });  
					 */
					layout0.addView(edt, lp0);
					// 
				}
				else
				{
					EditText edt1 =findViewById(fget_paramTextId(1 + i));
					if (Integer.parseInt(alParametersFullInd.get(i))  == Integer.parseInt(String.valueOf(Gl_SelectedIndex)))
						edt1.setVisibility(View.VISIBLE);
					else 
						edt1.setVisibility(View.GONE);
				}
			}
		}
	}

	protected void fillActivity()
	{

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

			//test
			ss = Data.aRithm2TechString(Data.aDeals100Init);
			Data.sADeals100 = ss;

		}		

		Data.mSplit5d(Data.sAMetaRithm);

		//параметы -  все со всех тренировок, добавляем в имя параметра индекс тренировки для уникальности имени
		/*???? 
		 for (int i0 =0; i0 < Data.aMetaRithm.length; i0++)
		 for (int i=0; i < Data.aMetaRithm[i0][3][0].length - 1; i++)
		 Data.aMetaRithm[i0][0][0][0][1].replace(Data.aMetaRithm[i0][3][0][i + 1][0].trim(),Data.aMetaRithm[i0][3][0][i + 1][3].trim());
		 */
		//

		//loadTrainData();//???



        //начитка тэгов
		setTags();

		//выбор тренировки		
		fillSpinner();
		if (Data.mSettings.contains(Data.SET_TRAIN_INDEX))
			spinner.setSelection(aliSpinner.indexOf(Data.mSettings.getInt(Data.SET_TRAIN_INDEX, 0)));
	    tvTextToSpeek.setText(Data.aMetaRithm[Gl_SelectedIndex][2][0][0][1]);

		//начитка параметров
		setParameters();
	}

	public void onClickFinish(View view)
	{
		//try{
		stopserv("exit");
		//}
		//catch (Exception e){};	
		this.finish();
	}

    public void onClickMain(View view)
	{
		//считываем измененные (мб) параметры
		int ii=1;
		for (int i=0; i < alParametersFull.toArray().length - 1; i++)
		{  //int ii=1;
			if (Integer.parseInt(alParametersFullInd.get(i)) == Gl_SelectedIndex)
			{
				edtCurr = findViewById(fget_paramTextId(1 + i));	
				alParametersFullV.set(i, edtCurr.getText().toString().trim());
				alParametersV.set(ii - 1, edtCurr.getText().toString().trim());
				//alParametersFullV.set(
				//заменим значение параметра в массиве, чтобы сохранилось в настройках
				Data.aMetaRithm[Gl_SelectedIndex][3][0][ii][3] = alParametersFullV.get(i);
				Data.aRithm = Data.aMetaRithm[Gl_SelectedIndex];
				Data.aRithm[3][0][ii][3] = alParametersV.get(ii - 1);
				String ss = Data.aMetaRithm2TechString(Data.aMetaRithm);
				Data.sAMetaRithm = ss;
				ss = Data.aRithm2TechString(Data.aRithm);
				Data.sARithm = ss;
				ii += 1;
			}
		}
		//парсим по параметрам количества подходов, длительность и пр
		for (int i=0; i < Data.aRithm[3][0].length - 1; i++)
			try
			{
				// ??? не работает?
				// if ((Data.aRithm[3][0][i + 1][1].trim().toLowerCase() == "int")
				// ||(Data.aRithm[3][0][i + 1][1].trim().toLowerCase() == "integer")
				// ||(Data.aRithm[3][0][i + 1][1].trim().toLowerCase() == "float")
				// ||(Data.aRithm[3][0][i + 1][1].trim().toLowerCase() == "double"))	 
				// //  GlParser.setVariable(aParamBrief[i].trim(), Double.parseDouble(aParamV[i].trim()));

				GlParser1.setVariable(Data.aRithm[3][0][i + 1][0].trim(), Double.parseDouble(Data.aRithm[3][0][i + 1][3].trim()));

			}
			catch (Exception e)
			{}
	    //наименование
		//?? Data.aRithm[0][0][0][1] = GlParser1.SParse(Data.aRithm[0][0][0][1]) ;
		//?? Data.aMetaRithm[Gl_SelectedIndex][0][0][0][1] = Data.aRithm[0][0][0][1];

		for (int i=0; i < Data.aRithm[10].length; i++)
		{
			//sn = String.valueOf(Integer.parseInt(Data.aRithm[10][0][0][0]) );
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
			{}  
		}


		//запомнить настройки
		SharedPreferences.Editor editor = Data.mSettings.edit();
		editor.putInt(Data.SET_TRAIN_INDEX,  Gl_SelectedIndex);
		editor.putString(Data.SET_TRAIN_ID,  Gl_SelectedID);
		editor.apply();
		editor = Data.mSettingsData.edit();
		editor.putString(Data.SET_AMETARITHM, Data.sAMetaRithm);
		editor.putString(Data.SET_ARITHM, Data.sARithm);
		editor.putString(Data.SET_ADEALS100, Data.sADeals100);
		editor.apply();
		//tvDelay.setText(   Data.mSettingsData.getString(Data.sAParemetersFull,""));
		spinner.setEnabled(false);
		chkAll.setEnabled(false);
		btnEdit.setEnabled(false);

		if (Gl_SelectedIndex < 0)
		{
			//String gripe = "Не выбрана тренировка";
			//EOFException e= new EOFException(gripe);
			//throw e;
		}
		if (btnMain.getText() == TXT_BTN_FINISHED)
		{

		}
		else
		{	
			if (((btnMain.getText() == TXT_BTN_NOTSTARTED) || (btnMain.getText() == TXT_BTN_PAUSED)))
			{ 
				Date dNow = new Date();
				Data.aRithm = Data.aMetaRithm[Gl_SelectedIndex];
				Data.sARithm = Data.aRithm2TechString(Data.aRithm);

			    if (btnMain.getText() == TXT_BTN_PAUSED)
				{
					speak("тренировка продолжена"); 
					resume(1 * 1000, Gl_CountOfAllIterations, 1, dNow); //&&&  не работает отсрочка
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
					// try{
					// 	Gl_CountOfAllIterations += GlParser1.Parse( Data.aRithm[10][j][0][0]);
					// } catch (Exception e){ 
					    Gl_CountOfAllIterations +=  Double.parseDouble(Data.aRithm[10][j][0][0]);
					// }
					resume(GlnDelay * 1000, Gl_CountOfAllIterations, GlnRepeat, dNow); 
				}
				btnMain.setText(TXT_BTN_STARTED);

			}
			else
			{
				btnMain.setText(TXT_BTN_PAUSED);
				stopserv("wait");
				speak("тренировка приостановлена"); 

			}
		}
	}


	public void speak(String s)
	{
		try
		{
			TTS.speak(s, TextToSpeech.QUEUE_FLUSH, null); 
			//  tvTextToSpeek.setText( String.valueOf(Gl_CountOfIterations) + " | "+ glsTextToSpeek);//  String.valueOf(Gl_CountOfIterations));
		}
		catch (Exception e)
		{}
	}

	public void loadTrainData()
	{
		btnMain.setText(TXT_BTN_NOTSTARTED);

		/*Вернуть?
		 GltxtSpeek = "===  Тренировка:  =========================\n\n";
		 int nDuration = 0;
		 for(int i = 0; i< Gl_Data.aRithm.length; i++){ 
		 nDuration +=Float.parseFloat(Gl_Data.aRithm[i][0][0])*Float.parseFloat(Gl_Data.aRithm[i][1][0]);   
		 };
		 glTrainingTimeMs = nDuration * 1000; 
		 int nSecDur = nDuration % 60  ;
		 double nMinDur = (Math.floor(nDuration / 60)) % 60  ;
		 double nHDur = Math.floor(Math.floor(nDuration / 60) / 60) % 60;
		 GltxtSpeek +="\nПродолжительность:  " + Math.round(nHDur) + " часов, " + Math.round(nMinDur) + " минут, " + nSecDur + " секунд\n" ;

		 GltxtSpeek = GltxtSpeek + "\n" + Gl_Data.ShortDescription + "\n\n";

		 for(int i = 0; i< Gl_Data.aRithm.length; i++){ 
		 GltxtSpeek +="\nВ ритме:  ";
		 GltxtSpeek += Gl_Data.aRithm[i][0][0] + " раз по " + Gl_Data.aRithm[i][1][0] + " сек. с дисперсией " + Gl_Data.aRithm[i][2][0] + "%";//\n";
		 Gl_CountOfAllIterations += Integer.parseInt(Gl_Data.aRithm[i][0][0]);
		 GltxtSpeek += "  Упражнения:\n";
		 for(int j = 0; j< Gl_Data.aRithm[i][3].length ; j++) {
		 GltxtSpeek += "  " + (j+1) + ": ";// \n";//":\n";
		 GltxtSpeek += "  " + Gl_Data.aDeals[  Integer.parseInt(Gl_Data.aRithm[i][3][j] ) ][0][0] + ", одно из перечисленных (с долей вероятности):  \n";
		 int nFProp = 0;
		 for (int l=1; l<Gl_Data.aDeals[  Integer.parseInt( Gl_Data.aRithm[i][3][j])].length; l++)  nFProp +=  Integer.parseInt(Gl_Data.aDeals[  Integer.parseInt( Gl_Data.aRithm[i][3][j] ) ][l][1]);       
		 for (int l=1; l<Gl_Data.aDeals[  Integer.parseInt( Gl_Data.aRithm[i][3][j])].length; l++){
		 GltxtSpeek +="          " + Gl_Data.aDeals[ Integer.parseInt(  Gl_Data.aRithm[i][3][j])][l][0] + " ( " + Gl_Data.aDeals[ Integer.parseInt(Gl_Data.aRithm[i][3][j])][l][1] + " из " + nFProp + ")\n";
		 }  
		 }    
		 }

		 GltxtSpeek += "\n\n Для изменения тренировки необходимо редактировать запускаемый файл html.\n";   
		 tvTextToSpeek.setText(GltxtSpeek);
		 */

		tvTextToSpeek.setText(Data.aMetaRithm[Gl_SelectedIndex][2][0][0][1]);
		//fillActivity();

        //начитка параметров
		//??setParameters();
	}

	public void onClickChkAll(View view)
	{
		isChkAll = !isChkAll;
		fillSpinner();

		//loadTrainData();
		fillActivity();  //ОНО НАДО???
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

	public void fillSpinner()
	{
		alSpinner.clear();
		aliSpinner.clear();
		for (int i = 0; i < Data.aMetaRithm.length; i++)
		{// начитываем в массив список доступных тренировок
			if ((!(Data.aMetaRithm[i][1][0][0][1] == "1")) | isChkAll)
				if (IsRithmInSelectedTags(i)) 
				{
					alSpinner.add(Data.aMetaRithm[i][0][0][0][1]);
					aliSpinner.add(i);
				}	
		}	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, alSpinner);
		adapter.setDropDownViewResource(android.R.layout.simple_gallery_item);
		spinner.setAdapter(adapter);

		OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				Gl_SelectedIndex = position;//!! aliSpinner.get(position);
				Gl_SelectedID = Data.aMetaRithm[Gl_SelectedIndex][0][0][1][1];
				loadTrainData();
				setParameters();
				//fillActivity();cs

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{		
			}
		};
		spinner.setOnItemSelectedListener(itemSelectedListener);
	}
    //

	//<<ФОРМА РЕДАКТИРОВАНИЯ
	public void onClickEdit(View view)
	{
		fillActivity();
		Intent intent = new Intent(MainActivity.this, EditFormActivity.class);
		startActivity(intent);
	}
	//>>

    public void onClickTest(View view)
	{
		if (IsRithmInSelectedTags(2))  tvTextToSpeek.setText("yyyyyes"); 
		else  tvTextToSpeek.setText("nnnnno") ;
    }


	//=======================

	// 2do: ПРИВЕСТИ ФОРМУ В НОРМАЛЬНЫЙ ВИД !!! (get set save)

	//
	public void setSlectedIndex()
	{
		//запомнить индекс выбранной тренировки
		SharedPreferences.Editor editor = Data.mSettings.edit();
		editor.putInt(Data.SET_TRAIN_INDEX,  Gl_SelectedIndex);
		editor.putString(Data.SET_TRAIN_ID,  Gl_SelectedID);
		editor.apply();
	}

	public void setAllSettings()
	{
		//запомнить настройки
		setSlectedIndex();
		SharedPreferences.Editor editor = Data.mSettings.edit();
		editor = Data.mSettingsData.edit();
		editor.putString(Data.SET_AMETARITHM, Data.sAMetaRithm);
		editor.putString(Data.SET_ARITHM, Data.sARithm);
		editor.putString(Data.SET_ADEALS100, Data.sADeals100);
		editor.apply();
	}

	protected void getSelectedIndex()
	{
		if   (Data.mSettingsData.contains(Data.SET_TRAIN_INDEX))
		{
			Gl_SelectedIndex = Integer.parseInt(Data.mSettingsData.getString(Data.SET_TRAIN_INDEX, ""));
			//Gl_SelectedID = Data.mSettingsData.getString(Data.SET_TRAIN_ID, "");
		}
		else
		{
			Gl_SelectedIndex = Data.Gl_SelectedIndexInit;
			//Gl_SelectedID = Data.mSettingsData.getString(Data.SET_TRAIN_ID, "");
		}
		//??
		//  Gl_SelectedIndex = spinner.getSelectedItemPosition();
	}

	protected void getAllSettings()
	{
        getSelectedIndex();
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

			//test
			ss = Data.aRithm2TechString(Data.aDeals100Init);
			Data.sADeals100 = ss;

		}		



		//??ВСЕ НИЖЕ С ФОРМЫ А ВСЕ ВЫШЕ ИЗ СОХРАНЕННОГО ФАЙЛА
		//СДЕЛАТЬ ЕДИНООБРАЗНО!!!!

		// Gl_SelectedIndex = spinner.getSelectedItemPosition();
		Data.mSplit5d(Data.sAMetaRithm);
		/*
		 //начитка тэгов
		 setTags();

		 //выбор тренировки		
		 //fillSpinner();
		 //if (Data.mSettings.contains(Data.SET_TRAIN_INDEX))
		 //	spinner.setSelection(aliSpinner.indexOf(Data.mSettings.getInt(Data.SET_TRAIN_INDEX, 0)));
		 //tvTextToSpeek.setText(Data.aMetaRithm[Gl_SelectedIndex][2][0][0][1]);

		 //начитка параметров
		 setParameters();
		 */
	}

}
