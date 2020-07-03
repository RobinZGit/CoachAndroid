package com.mycompany.myapp;
//package ru.startandroid.develop.p0921servicesimple;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import android.speech.tts.*;
import android.util.*;
import android.view.View;
import android.view.View.OnClickListener;
import java.util.Date;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;
import android.widget.*;
import android.content.*;
import java.util.Locale;
import java.sql.*;
import android.icu.text.*;
import android.content.*;
import com.mycompany.myapp.MainActivity;
import javax.microedition.khronos.opengles.*;
//

public class coachService extends Service
{

	public coachUtility Data;
	protected ArrayList<Double> alRithm;
	protected ArrayList<String> alSpeek;

	private TextToSpeech TTS;
	private Date gldNow;
	private Date gldNow1;
	private int Gl_CountOfIterations = 0;
	private int Gl_CountOfAllIterations1;
	private int GlnRepeat = 1;//сколько раз повторить всю тренировку
    private int Gl_nBegTraining;
	private String GltxtSpeek;
	private int GlnDelayMs = 0; //отсрочка старта 
	private String glsTextToSpeek;
	private int nRandProp;
	private int nCurrProp;
	private int nFullProp;
	private MatchParser GlParser;
	String[][][] acopy;
	Date dCurrWait;
	int nRandDispers;

	private double nomeriteratsii = 1;
	private double nomeriteratsii1 = 1;
	private double durationMs = 1;

	private Timer glTimer;
	private TimerTask glTimerTask;  

	public void onCreate()
	{
		super.onCreate();

		Data = new coachUtility();

		GlParser = new MatchParser();
		GlParser.setVariable("номеритерации", nomeriteratsii);
		GlParser.setVariable("номеритерации1", nomeriteratsii1);
		GlParser.setVariable("прошломс", 0.0);
		GlParser.setVariable("прошлосек", 0.0);


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
	}

	public int onStartCommand(Intent intent, int flags, int startId)
	{
		try
		{
			Data.sARithm = intent.getStringExtra("aRithm");
            Data.sADeals100 = intent.getStringExtra("aDeals100");
			Data.aRithm = Data.mSplit4d100(Data.sARithm);
			Data.aDeals100 = Data.mSplit4d100(Data.sADeals100);

			GlnRepeat =  intent.getIntExtra("Repeat", 1);
			if (GlnRepeat > 1)
			{
				acopy = new String[Data.aRithm[10].length * GlnRepeat][][];
				for (int i=0;i < GlnRepeat;i++)
					System.arraycopy(Data.aRithm[10] , 0, acopy, i * Data.aRithm[10] .length, Data.aRithm[10] .length);
				Data.aRithm[10] = Arrays.copyOf(Data.aRithm[10], Data.aRithm[10].length * GlnRepeat);
				System.arraycopy(acopy, 0, Data.aRithm[10], 0, acopy.length);
			}

			GlnDelayMs = intent.getIntExtra("Delay", 0);
			Date d = new Date();
			Gl_nBegTraining = intent.getIntExtra("nBegTraining", (int)d.getTime());
			if (intent.getIntExtra("CountOfAllIterations", -1) > 0)
			    Gl_CountOfAllIterations1 = GlnRepeat * intent.getIntExtra("CountOfAllIterations", -1);

			startserv(intent.getBooleanExtra("wait", false), intent.getIntExtra("CountOfAllIterations", -1), GlnDelayMs);
		}
		catch (Exception e)
		{}

		return  START_STICKY;//! super.onStartCommand(intent, flags, startId);
	}



	public void onDestroy()
	{
		pause();
		super.onDestroy();
	}

	public IBinder onBind(Intent intent)
	{
		return null;
	}

	void startserv(boolean flWait, int pCountOfAllIterations, int pnDelay)
	{
	    if (!flWait)
		{
			new Thread(new Runnable() {
					public void run()
					{
						fMainCircleIterationNew(false);
					}
				}).start();
	    }
		else pause();			
	}

	private void pause()
	{
		try
		{
			this.glTimer.cancel();
			this.glTimer.purge();
			this.glTimer = null;
		}
		catch (Exception e)
		{}
	}

	public void resume(int ms)
	{

		try
		{
			this.glTimer = new Timer();
			this.glTimerTask = new TimerTask() {
				@Override
				public void run()
				{
					fMainCircleIterationNew(true);
				}
			};

			this.glTimer.
				schedule(glTimerTask, ms);
		}
		catch (Exception e)
		{
			//tvTextToSpeek.setText(tvTextToSpeek.getText()+e.getMessage());
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

	//текст текущего упражнения. aDealIndexes - массив индексов дел , которые делаются в текущем срабатывани ритма
	public String fget_TextToSpeek100(String[] aDealIndexes)
	{
		String sTextToSpeek = "";
		String sPoint = "";
		int k = 1;

		for (int i=0, len0=aDealIndexes.length; i < len0; i++)
		{
			sTextToSpeek += Data.aDeals100[Integer.parseInt(aDealIndexes[i].trim())][1][0][0] + " ";
			nFullProp = 0;

			for (int j=1, len= Data.aDeals100[Integer.parseInt(aDealIndexes[i].trim())][1].length; j < len; j++)
			{
				nFullProp += Integer.parseInt(Data.aDeals100[Integer.parseInt(aDealIndexes[i].trim())][1][j][1].trim()); // Сумма пропорций навыка
			}

		    nRandProp = (int) (Math.floor(Math.random() * (nFullProp - 0 + 1)) + 0); //случайное число для определения одного из элементов навыка
			nCurrProp = 0;//0 sic!
			k = 1;

			while ((Data.aDeals100[Integer.parseInt(aDealIndexes[i].trim())][1].length > k)
			       && (nCurrProp <= nRandProp))
			{//искомый элемент навыка - последний, до которого сумма пропорций < nRandProp  //<= sic!
				nCurrProp += Integer.parseInt(Data.aDeals100[Integer.parseInt(aDealIndexes[i].trim())][1][k][1].trim());
				k = k + 1;
			}

			sPoint = Data.aDeals100[Integer.parseInt(aDealIndexes[i].trim())][1][k - 1][0];
            if (sPoint.indexOf(">") > 0)
			{
				sPoint = sPoint.replace("->", "");	
				String[] aPointIndexes = sPoint.split(",");
				sTextToSpeek += " " +  fget_TextToSpeek100(aPointIndexes) + ", ";
			}
			else  sTextToSpeek += " " + GlParser.SParse(Data.aDeals100[Integer.parseInt(aDealIndexes[i].trim())][1][k - 1][0]) + ", "; //-1 sic! 
		}

		return sTextToSpeek;
	} //fget_TextToSpeek100

	//если нет ссылок на дела - сразу выдает текст, иначе вычисляет 
	public String fget_BeforeTextToSpeek100(String pDealIndexes)
	{

		String sTextToSpeek = "";
		String sDealIndexes = pDealIndexes;
		if (sDealIndexes.indexOf(">") > 0)
		{
			sDealIndexes = sDealIndexes.replace("->", "");	
			String[] aDealIndexes = sDealIndexes.split(",");
			sTextToSpeek += " " +  fget_TextToSpeek100(aDealIndexes) + ", ";
		}
		else
		{
			sTextToSpeek += " " + GlParser.SParse(pDealIndexes) + ", "; 
		}

		//подстановка параметров
		for (int i=0; i < Data.aRithm[3][0].length - 1; i++)
			try
			{
				sTextToSpeek = sTextToSpeek.replace(Data.aRithm[3][0][i + 1][0].trim(), Data.aRithm[3][0][i + 1][3].trim());
			}
			catch (Exception e)
			{}
		return sTextToSpeek;
	} //fget_BeforeTextToSpeek


	private void fDecGl_aRithm()
	{
		Gl_CountOfIterations++;
		if (Integer.parseInt(Data.aRithm[10][0][0][0]) > 1)
		{
			Data.aRithm[10][0][0][0] = String.valueOf(Integer.parseInt(Data.aRithm[10][0][0][0]) - 1);
		}
		else
		{
			acopy = new String[Data.aRithm[10].length - 1][][];
			int index = 0;//удалить 0й элемент
			System.arraycopy(Data.aRithm[10], 1, acopy, index, Data.aRithm[10].length - index - 1);
			System.arraycopy(acopy, index, Data.aRithm[10], index, acopy.length);
		}
	}; 
	private void fMainCircleIterationNew(boolean pFlWait)
	{ 
		if (true)
		{
			nRandDispers = 0;//test (int)Math.floor(Math.random() *Integer.parseInt(((Data.aRithm[0][1][0]))) * Integer.parseInt(((Data.aRithm[0][2][0]))) / 100);

		    glsTextToSpeek =  fget_BeforeTextToSpeek100(Data.aRithm[10][0][3][1]);
			gldNow = new Date();
			GltxtSpeek += "\n" + glsTextToSpeek ;

		    nomeriteratsii += 1;
		    GlParser.setVariable("номеритерации", nomeriteratsii);
		    nomeriteratsii1 += 1;
		    if (Gl_CountOfIterations < nomeriteratsii)
			{
				nomeriteratsii = 1;
				nomeriteratsii1 = 0;
			}
		    GlParser.setVariable("номеритерации1", nomeriteratsii1);

		    pause();

		    if ((Gl_CountOfIterations >= Gl_CountOfAllIterations1)
				&& (Gl_CountOfAllIterations1 > 0)
				)
			{
				//! btnMain.setText(TXT_BTN_FINISHED);
				gldNow = new Date();
			    //! tvTextToSpeek.append( formatter.format(gldNow) + " тренировка завершена" );
				speak("тренировка завершена"); 
		    }
		    else
			{
				if (GlnDelayMs > 0)
				{
					resume(GlnDelayMs);
					GlnDelayMs = 0;
				} 
				else
				{
					// speak(aParamBrief[1]);
					speak(glsTextToSpeek); 
					fDecGl_aRithm();
					//
					gldNow1 = new Date();
					durationMs =   (int)(gldNow1.getTime() - 1000000 * Math.floor(gldNow1.getTime() / 1000000)) - Gl_nBegTraining;//  (double)(gldNow1.getTime() - Gl_nBegTraining);
					GlParser.setVariable("прошломс",  durationMs);
					GlParser.setVariable("прошлосек", durationMs / 1000);
					GlParser.setVariable("прошломин", durationMs / (60000));
					GlParser.setVariable("прошлочас", durationMs / (3600000));

					for (int i=0; i < Data.aRithm[3][0].length - 1/*aParamV.length*/; i++)
						try
						{
                            /* ??? не работает?
							 if ((Data.aRithm[3][0][i + 1][1].trim().toLowerCase() == "int")
							 ||(Data.aRithm[3][0][i + 1][1].trim().toLowerCase() == "integer")
							 ||(Data.aRithm[3][0][i + 1][1].trim().toLowerCase() == "float")
							 ||(Data.aRithm[3][0][i + 1][1].trim().toLowerCase() == "double"))	 
							 //  GlParser.setVariable(aParamBrief[i].trim(), Double.parseDouble(aParamV[i].trim()));
							 */
							GlParser.setVariable(Data.aRithm[3][0][i + 1][0].trim(), Double.parseDouble(Data.aRithm[3][0][i + 1][3].trim()));

						}
						catch (Exception e)
						{}
					//
					double nd=0 ;
					try
					{
						nd =  GlParser.Parse(Data.aRithm[10][0][1][0]);
					}
					catch (Exception e)
					{}
					resume((int) (nRandDispers + Math.round(nd * 1000)));
				}
			}
		}  
	}; //fMain   
}
