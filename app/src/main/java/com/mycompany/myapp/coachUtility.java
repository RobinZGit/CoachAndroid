package com.mycompany.myapp;
import android.app.*;
import android.os.*;
//
//import android.speech.tts.*;
import android.util.*;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import java.util.*;
//import android.widget.*;
import android.content.*;
import android.speech.tts.*;


public class coachUtility
{
	protected SharedPreferences mSettings;
	protected SharedPreferences mSettingsData;
	final String sDel1 ="》,";//"\"•,";   
	final String sDel2 ="}•,";
	final String sDel3 ="}°,";
	final String sDel4 ="}°°,";
	final String sDel5 ="}°°°,";

	final String sBegDel1 ="《";  //"\",";   
	final String sBegDel2 ="{•,";
	final String sBegDel3 ="{°,";
	final String sBegDel4 ="{°°,";
	final String sBegDel5 ="{°°°,";

	final String SET_FILE_NAME = "coachsettings_test2";
	final String SET_TRAIN_INDEX = "trainindex";
	final String SET_TRAIN_ID = "trainid";

	//<<для сброса настроек, чтобы вернуться к чтению данных из массивов в этом классе, изменить имена ниже
	final String SET_AMETARITHM_FILE_NAME = "ametarithmanddeals_tmp36";
	final String SET_AMETARITHM_BCK1_FILE_NAME = "bck1_ametarithmanddeals_tmp36";
	//>>

	final String SET_AMETARITHM = "ametarithm";
	final String SET_ARITHM = "arithm";
    final String SET_ADEALS100 = "adeals100";

	protected String sAMetaRithm="";
	protected String sARithm="";
	protected String sADeals100="";

	protected String[][][][][] aMetaRithm;
	protected String[][][][] aRithm;
	protected String[][][][] aDeals100;

	protected void onCreate(Bundle savedInstanceState)
	{

	}


	public String aMetaRithm2TechString(String[][][][][] aSource)
	{
		String sRet = "";//new String(sBegDel5);// sDelim ;
		for (String[][][][] a4d: aSource)
		{
			sRet += sBegDel5;
			for (String[][][] a3d: a4d)
			{
				sRet += sBegDel4;
				for (String[][] a2d: a3d)
				{
					sRet += sBegDel3;
					for (String[] a1d: a2d)
					{
						sRet += sBegDel2;
						for (String item : a1d)
						{
							sRet += ""  + sBegDel1 + item.trim() + "" + sDel1 + ""; 
						}
						sRet += sDel2 /*+" \n"*/; 
					}
					sRet += sDel3/* +" \n"*/; 
				}
				sRet += sDel4 + "\n"; 
			}
			sRet += sDel5  + "\n\n\n\n";//\n\n==============================="+"\n"; 
		}   
		return sRet;
	}

	public String TechString2aMetaRithm(String _str)
	{
		String str = //new String(
			_str.replace(sBegDel1, "").replace(sBegDel2, "").replace(sBegDel3, "").replace(sBegDel4, "").replace(sBegDel5, "")
			;//);
		int i1 = 0;
		int i2 = 0;
		int i3 = 0;
		int i4 = 0;
		ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<String>>>>>   
			al5d = new  ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<String>>>>>();
		for (String item1: str.split(sDel5))
		{
			al5d.add(null);
			for (String item2: item1.split(sDel4))
			{
				al5d.get(i4).add(null);
				for (String item3: item2.split(sDel3))
				{
					al5d.get(i4).get(i3).add(null);
					for (String item4: item3.split(sDel2))
					{
						al5d.get(i4).get(i3).get(i2).add(null);
						for (String item5: item4.split(sDel1))
						{
							al5d.get(i4).get(i3).get(i2).get(i1).add(item5.trim());
							i1++;
						}
						i2++;
					}
					i3++; 
				}
				i4++;
			}  
		}	
		return al5d.toString();
	}

	public String a12string(String[] as1)
	{
		String sret="";
		for (String s: as1) sret += s + sDel5;
		return sret;
	}
	public String al12string(ArrayList<String>  as1)
	{
		String sret="";
		for (String s: as1) sret += s + sDel5;
		return sret;
	}

	public ArrayList<String> mSplit1d(String str, String sdel)
	{
		ArrayList<String> slret = new ArrayList<String>()  ;
		String s=//new String(
			str;//);
		while (s.indexOf(sdel) >= 0)
		{
			slret.add(s.substring(0, s.indexOf(sdel)).trim());
			s = s.substring(s.indexOf(sdel) + sdel.length(), s.length());
		}
		return slret;	 
	}


	public void  mSplit5d(String _str)
	{
        String str =//new String( 
			_str.replace(sBegDel1, "").replace(sBegDel2, "").replace(sBegDel3, "").replace(sBegDel4, "").replace(sBegDel5, "")
			;//);
		ArrayList<String> al1 = new ArrayList<String>() ;
		al1 = mSplit1d(str, sDel5);

		ArrayList<ArrayList<String>> al2 = new ArrayList<ArrayList<String>>() ;
		for (String s: al1) al2.add(mSplit1d(s, sDel4)); 

		ArrayList<ArrayList<ArrayList<String>>> al3 = new ArrayList<ArrayList<ArrayList<String>>>() ;
		for (ArrayList<String> ial1: al2)
		{
			al3.add(new ArrayList<ArrayList<String>>());
			for (String s: ial1) al3.get(al3.size() - 1).add(mSplit1d(s, sDel3)); /// al3.add!!
		}

		ArrayList<ArrayList<ArrayList<ArrayList<String>>>> al4 = new ArrayList<ArrayList<ArrayList<ArrayList<String>>>>() ;
		for (ArrayList<ArrayList<String>> ial2: al3)
		{
			al4.add(new ArrayList<ArrayList<ArrayList<String>>>());
			for (ArrayList<String> ial1: ial2)
			{ 
				al4.get(al4.size() - 1).add(new ArrayList<ArrayList<String>>());
				for (String s: ial1)  al4.get(al4.size() - 1).get(al4.get(al4.size() - 1).size() - 1).add(mSplit1d(s, sDel2)); 
			}  
		} 	 
		ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<String>>>>> al5 = new ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<String>>>>>() ;
		for (ArrayList<ArrayList<ArrayList<String>>> ial3: al4)
		{
			al5.add(new ArrayList<ArrayList<ArrayList<ArrayList<String>>>>());
			for (ArrayList<ArrayList<String>> ial2: ial3)
			{
				al5.get(al5.size() - 1).add(new ArrayList<ArrayList<ArrayList<String>>>());
				for (ArrayList<String> ial1: ial2)
				{ 
					al5.get(al5.size() - 1).get(al5.get(al5.size() - 1).size() - 1)  .add(new ArrayList<ArrayList<String>>());
					for (String s: ial1)
					{  
						al5.get(al5.size() - 1).get(al5.get(al5.size() - 1).size() - 1). get(al5.get(al5.size() - 1).get(al5.get(al5.size() - 1).size() - 1).size() - 1)   .add(mSplit1d(s, sDel1)); 
					}
				}  
			} 	  
		}

		aMetaRithm = new String[al5.size()][][][][];
		int i0 = 0;
		for (ArrayList<ArrayList<ArrayList<ArrayList<String>>>> ial5: al5)
		{
			aMetaRithm[i0]  = new String [ ial5.size() ][][][];
			int i1 = 0;
			for (ArrayList<ArrayList<ArrayList<String>>> ial4: ial5)
			{
				aMetaRithm[i0][i1] = new String [ ial4.size() ][][];
				int i2 = 0;
				for (ArrayList<ArrayList<String>> ial3: ial4)
				{
					aMetaRithm[i0][i1][i2] = new String [  ial3.size()][];
					int i3 = 0;
					for (ArrayList<String> ial2: ial3)
					{ 
						aMetaRithm[i0][i1][i2][i3] = new String [ ial2.size() ];
						int i4 = 0;
						for (String s: ial2)
						{  
							aMetaRithm[i0][i1][i2][i3][i4] = s.trim(); 
							i4++;
						}
						i3++;
					}  
					i2++;
				} 	  
				i1++;
			}
			i0++;
		}

	}
	//
	public String aDeals2TechString(String[][][] aSource)
	{
		String sRet = "";

		for (String[][] a2d: aSource)
		{
			sRet += sBegDel3;
			for (String[] a1d: a2d)
			{
				sRet += sBegDel2;
				for (String item : a1d)
				{
					sRet += "" + sBegDel1  + item.trim() + "" + sDel1 + ""; 
				}
				sRet += sDel2 /*+" \n"*/; 
			}
			sRet += sDel3 + " \n\n\n";
		}

		return sRet;
	}

	public String aRithm2TechString(String[][][][] aSource)
	{
		String sRet = //new String(
			sBegDel5
			;//);
		for (String[][][] a3d: aSource)
		{
			sRet += sBegDel4;
			for (String[][] a2d: a3d)
			{
				sRet += sBegDel3;
				for (String[] a1d: a2d)
				{
					sRet += sBegDel2;
					for (String item : a1d)
					{
						sRet += "" + sBegDel1  + item.trim() + "" + sDel1 + ""; 
					}
					sRet += sDel2 /*+" \n"*/; 
				}
				sRet += sDel3/* +" \n"*/; 
			}
			sRet += sDel4; 
		}
		sRet += sDel5  + "\n"; 
		//}   
		return sRet;
	}

	public void mSplit4d(String _str)
	{
        String str = //new String( 
			_str.replace(sBegDel1, "").replace(sBegDel2, "").replace(sBegDel3, "").replace(sBegDel4, "").replace(sBegDel5, "")
			;//);
		ArrayList<String> al1 = new ArrayList<String>() ;
		al1 = mSplit1d(str, sDel4);

		ArrayList<ArrayList<String>> al2 = new ArrayList<ArrayList<String>>() ;
		for (String s: al1) al2.add(mSplit1d(s, sDel3)); 

		ArrayList<ArrayList<ArrayList<String>>> al3 = new ArrayList<ArrayList<ArrayList<String>>>() ;
		for (ArrayList<String> ial1: al2)
		{
			al3.add(new ArrayList<ArrayList<String>>());
			for (String s: ial1) al3.get(al3.size() - 1).add(mSplit1d(s, sDel2)); 
		}

		ArrayList<ArrayList<ArrayList<ArrayList<String>>>> al4 = new ArrayList<ArrayList<ArrayList<ArrayList<String>>>>() ;
		for (ArrayList<ArrayList<String>> ial2: al3)
		{
			al4.add(new ArrayList<ArrayList<ArrayList<String>>>());
			for (ArrayList<String> ial1: ial2)
			{ 
				al4.get(al4.size() - 1).add(new ArrayList<ArrayList<String>>());
				for (String s: ial1)  al4.get(al4.size() - 1).get(al4.get(al4.size() - 1).size() - 1).add(mSplit1d(s, sDel1)); 
			}  
		} 	 

		aRithm  = new String [ al4.size() ][][][];
		int i1 = 0;
		for (ArrayList<ArrayList<ArrayList<String>>> ial4: al4)
		{
			aRithm[i1] = new String [ ial4.size() ][][];
			int i2 = 0;
			for (ArrayList<ArrayList<String>> ial3: ial4)
			{
				aRithm[i1][i2] = new String [  ial3.size()][];
				int i3 = 0;
				for (ArrayList<String> ial2: ial3)
				{ 
					aRithm[i1][i2][i3] = new String [ ial2.size() ];
					int i4 = 0;
					for (String s: ial2)
					{  
						aRithm[i1][i2][i3][i4] = s.trim(); 
						i4++;
					}
					i3++;
				}  
				i2++;
			} 	  
			i1++;
		}

	}

//переход на новый формат дел для замены mSplit4d
	public String[][][][] mSplit4d100(String _str)
	{
        String str = //new String( 
			_str.replace(sBegDel1, "").replace(sBegDel2, "").replace(sBegDel3, "").replace(sBegDel4, "").replace(sBegDel5, "")
			;//);
		ArrayList<String> al1 = new ArrayList<String>() ;
		al1 = mSplit1d(str, sDel4);

		ArrayList<ArrayList<String>> al2 = new ArrayList<ArrayList<String>>() ;
		for (String s: al1) al2.add(mSplit1d(s, sDel3)); 

		ArrayList<ArrayList<ArrayList<String>>> al3 = new ArrayList<ArrayList<ArrayList<String>>>() ;
		for (ArrayList<String> ial1: al2)
		{
			al3.add(new ArrayList<ArrayList<String>>());
			for (String s: ial1) al3.get(al3.size() - 1).add(mSplit1d(s, sDel2)); /// al3.add!!
		}

		ArrayList<ArrayList<ArrayList<ArrayList<String>>>> al4 = new ArrayList<ArrayList<ArrayList<ArrayList<String>>>>() ;
		for (ArrayList<ArrayList<String>> ial2: al3)
		{
			al4.add(new ArrayList<ArrayList<ArrayList<String>>>());
			for (ArrayList<String> ial1: ial2)
			{ 
				al4.get(al4.size() - 1).add(new ArrayList<ArrayList<String>>());
				for (String s: ial1)  al4.get(al4.size() - 1).get(al4.get(al4.size() - 1).size() - 1).add(mSplit1d(s, sDel1)); 
			}  
		} 	 

		String [][][][] aRet  = new String [ al4.size() ][][][];
		int i1 = 0;
		for (ArrayList<ArrayList<ArrayList<String>>> ial4: al4)
		{
			aRet[i1] = new String [ ial4.size() ][][];
			int i2 = 0;
			for (ArrayList<ArrayList<String>> ial3: ial4)
			{
				aRet[i1][i2] = new String [  ial3.size()][];
				int i3 = 0;
				for (ArrayList<String> ial2: ial3)
				{ 
					aRet[i1][i2][i3] = new String [ ial2.size() ];
					int i4 = 0;
					for (String s: ial2)
					{  
						aRet[i1][i2][i3][i4] = s.trim(); 
						i4++;
					}
					i3++;
				}  
				i2++;
			} 	  
			i1++;
		}
		return aRet;

	}

//#####################################################
//####    БАЗА ДАННЫХ ПО УМОЛЧАНИЮ    #########	
//#####################################################	
	public String[][][][][]  aMetaRithmInit = 
	{  

		//=========== счетчик для роллерхок =====
	    {
			{{{"name","счет 1-4 номер квадрата + 1-9 номер квадрата шайбы "} ,{"id","роллерхоккей_счетчик"} }},
			{{{"disabled","0"}}},
			{{{"tags","хоккей#"}}},
			{{{"parameters"},{"par1","int","интервал счетчика","2"},{"par2","string","комментарий к тренировке",""},{"par3","string","последняя цифра","4"}}       },
			{},{},{},{},{},{}, //резерв
			{   
				{{"1","раз по"},{"10", "сек с дисперсией"},{"0"},{"сказать","первая цифра - положение корпуса. вторая цифра - положение шайбы"}}, 
    			{{"150","раз по"},{"par1","сек с дисперсией"},{"0"},{"сказать","->26,26"}},     

			}
		},
		//=====================
		//===========хоккей =====
	    {
			{{{"name","хоккей 20 мин. 1 период в 2 пятерки"} ,{"id","хоккей_1пер_2пят"} }},
			{{{"disabled","0"}}},
			{{{"tags","хоккей#"}}},
			{{{"parameters"},{"par1","string","комментарий к тренировке",""}}       },

			{},{},{},{},{},{}, //резерв
			{   
				{{"1","раз по"},{"5", "сек с дисперсией"},{"0"},{"сказать","хоккей один период в две пятерки"}}, 
				{{"18","раз по"},{"10","сек с дисперсией"},{"0"},{"сказать","->4,4,7,8"}},     //катание 3 мин 
				{{"1","раз по"},{"3", "сек с дисперсией"},{"0"},{"сказать","Смена. Броски"}},
				{{"1","раз по"},{"15", "сек с дисперсией"},{"0"},{"сказать",""}},
				{{"20","раз по"},{"9","сек с дисперсией"},{"0"},{"сказать","->15,14,5,6"}},    //броски 3 мин

				{{"1","раз по"},{"5", "сек с дисперсией"},{"0"},{"сказать","Смена.Катание"}}, 
				{{"18","раз по"},{"10","сек с дисперсией"},{"0"},{"сказать","->4,4,16,17"}},     //катание 3 мин (16 скорость 17 бросок)
				{{"1","раз по"},{"3", "сек с дисперсией"},{"0"},{"сказать","Смена. Броски"}},
				{{"1","раз по"},{"15", "сек с дисперсией"},{"0"},{"сказать",""}},
				{{"20","раз по"},{"9","сек с дисперсией"},{"0"},{"сказать","->15,14,5,6"}},    //броски 3 мин

				{{"1","раз по"},{"5", "сек с дисперсией"},{"0"},{"сказать","Смена.Катание"}}, 
				{{"18","раз по"},{"10","сек с дисперсией"},{"0"},{"сказать","->4,4,24,25"}},     //катание 3 мин (24 маневр 25 пас)
				{{"1","раз по"},{"3", "сек с дисперсией"},{"0"},{"сказать","Смена. Броски"}},
				{{"1","раз по"},{"15", "сек с дисперсией"},{"0"},{"сказать",""}},
				{{"20","раз по"},{"9","сек с дисперсией"},{"0"},{"сказать","->15,14,5,6"}},    //броски 3 мин

				{{"1","раз по"},{"3", "сек с дисперсией"},{"0"},{"сказать","Период завершен."}}, 

			}
		},
		//=====================
		//===========дых -1 =====

	    {
			{{{"name","экспер 110011100 медленнаяая дыхательная (220+ 120*sin(3.14*полноевремя(0)/780))/(2*3*60) "},{"id","экспер_дых_ур1"}}},
			{{{"disabled","0"}}},
			{{{"tags","дых#"}}},
			{{{"parameters"},{"par1","string","комментарий к тренировке",""}}       },

			{},{},{},{},{},{}, //резерв
			// {{"param1","descreat","<min>","<max>"},{"param2","descreat","<min>","<max>"},}
			{   
				//{{"1500"},{"прошлосек+1"},{"0"},{"1"}},
				{{"1500"},{"0.5"},{"0"},{"сказать","->2"}},
			}

		},
		//======================
		//===========дых 0 =====

	    {
			{{{"name","медленнаяая дыхательная (220+ 120*sin(3.14*полноевремя(0)/780))/(2*3*60) "},{"id","медл_дых_ур1"}}},
			{{{"disabled","0"}}},
			{{{"tags","дых#"}}},
			{{{"parameters"},{"par1","string","комментарий к тренировке",""}}       },

			{},{},{},{},{},{}, //резерв
			// {{"param1","descreat","<min>","<max>"},{"param2","descreat","<min>","<max>"},}
			{   
				//{{"1500"},{"прошлосек+1"},{"0"},{"1"}},
				{{"1500"},{"(250+ 120*sin(3.14*полноевремя(0)/1000))/(2*3*60)"},{"0"},{"сказать","->1"}},
			}

		},
		//======================
	    //===========дых 1 =====
		{
			{{{"name","медленная дыхательная уровень 2(290+ 140*sin(3.14*полноевремя(0)/780))/(2*3*60) "},{"id","медл_дых_ур2"}}},
			{{{"disabled","0"}}},
			{{{"tags","дых#для ходьбы#"}}},
			{{{"parameters"},{"par1","string","комментарий к тренировке",""}}       },

			{},{},{},{},{},{}, //резерв
			// {{"param1","descreat","<min>","<max>"},{"param2","descreat","<min>","<max>"},}
			{   
				//{{"1500"},{"прошлосек+1"},{"0"},{"1"}},
				{{"1"},{"4"},{"0"},{"текст дела","медленная дыхательная второй уровень"}},
				{{"1"},{"6"},{"0"},{"текст дела",""}},
				{{"1500"},{"(290+ 140*sin(3.14*полноевремя(0)/1000))/(2*3*60)"},{"0"},{"сказать","->1"}},

			}

		},
		//======================
	    //===========дых 1 =====
	    {
			{{{"name","быстрая дыхательная (220+ 120*sin(3.14*полноевремя(0)/780))/(2*3*60) "} ,{"id","быстр_дых_ур1"} }},
			{{{"disabled","0"}}},
			{{{"tags","дых#для вело#"}}},
			{{{"parameters"},{"par1","string","комментарий к тренировке",""}}       },

			{},{},{},{},{},{}, //резерв
			// {{"param1","descreat","<min>","<max>"},{"param2","descreat","<min>","<max>"},}
			{   
				//{{"1500"},{"прошлосек+1"},{"0"},{"1"}},
				{{"1500"},{"(220+ 120*sin(3.14*полноевремя(0)/780))/(2*3*60)"},{"0"},{"сказать","->1"}},
			}

		},
		//======================

		//===========дых 2 =====
	    {
			{{{"name","уж слишком дыхат (200+ 120*sin(3.14*полноевремя(0)/580))/(2*3*60)"}  ,{"id","слишком_быстр_дых_ур2"}}},
			{{{"disabled","0"}}},
			{{{"tags","дых#"}}},
			{{{"parameters"},{"par1","string","комментарий к тренировке",""}}       },

			{},{},{},{},{},{}, //резерв
			{   
				{{"1500"},{"(200+ 120*sin(3.14*полноевремя(0)/580))/(2*3*60)"},{"0"},{"сказать","->1"}},
			}
		},

		//===========бег 180+- =====
	    {
			{{{"name","бег частота 180 плюс минус 10"} ,{"id","тест_бег_ур1"} }},
			{{{"disabled","0"}}},
			{{{"tags","дых#бег#"}}},
			{{{"parameters"},{"par1","string","комментарий к тренировке",""}}       },

			{},{},{},{},{},{}, //резерв
			{   
				//{{"5500"},{" (180+"},{"0"},{"1"}},
				{{"5500"},{" (840+ 0*sin(3.14*полноевремя(0)/500))/(2*3*60)"},{"0"},{"сказать","->1"}},
			}
		},
		//=====================




	};

	/*
	 {°°°,{°°,{°,{•,《name》,《разминка амосова》,}•,{•,《id》,《медл_дых_ур2》,}•,}°,}°°,
	 {°°,{°,{•,《disabled》,《0》,}•,}°,}°°,
	 {°°,{°,{•,《tags》,《дых#для ходьбы#в покое#тест#》,}•,}°,}°°,
	 {°°,}°°,
	 {°°,}°°,
	 {°°,}°°,
	 {°°,}°°,
	 {°°,}°°,
	 {°°,}°°,
	 {°°,}°°,
	 {°°,{°,{•,《1》,}•,{•,《4》,}•,{•,《0》,}•,{•,《сказать》,《разминка амосова》,}•,}°,{°,{•,《1》,}•,{•,《7》,}•,{•,《0》,}•,{•,《сказать》,《》,}•,}°,{°,{•,《20》,}•,{•,《(200+ 50*номеритерации1)/1000》,}•,{•,《0》,}•,{•,《сказать》,《а》,}•,}°,}°°,
	 }°°°,
	 */

	//=====================

	public String[][][][]  aDeals100Init =
	{   //==  для дыхательных  ======
	    /*0*/{{{"name","ссылка12"},{"id","ссылкатест"}}, {{""},{"->1,2","1"}}},
	    /*1*/{{{"name","сказать о или ы1"},{"id","вдохвыдох1"}}, {{""},{ "iif(остатокделна4(номеритерации1)-1,о,ы)" ,"1"}}},
	    /*2*/{{{"name","сказать о или ы2"},{"id"," вдохвыдох2"}}, {{""},{ "iif(grdssin(номеритерации1*20*grdssin(номеритерации1/80)),о,ы)" ,"1"}}},
	    /*3*/{{{"name","сказать о или ы3"},{"id"," вдохвыдох3"}}, {{""},{ "iif(остатокделна4(номеритерации1),о,ы)" ,"1"}}},

	    //==  для хоккея  ==========
		/*4*/{{{"name","ближний1 средний1 дальний1"},{"id","ближний1_средний1_дальний1"}}, {{""},{ "ближний" ,"1"},{"средний" ,"1"},{ "дальний" ,"1"} }},
		/*5*/{{{"name","вверх1 центр1 вниз1"},{"id","вверх1_центр1_вниз1"}}, {{""},{ "вверх" ,"1"},{"центр" ,"1"},{ "вниз" ,"1"} }},
		/*6*/{{{"name","влево1 центр1 вправо1"},{"id","влево1_центр1_вправо1"}}, {{""},{ "влево" ,"1"},{"центр" ,"1"},{ "вправо" ,"1"} }},
		/*7*/{{{"name","слева1 пусто2 справа1"},{"id","слева1_пусто2_справа1"}}, {{""},{ "объехать слева" ,"1"},{"" ,"2"},{ "объехать справа" ,"1"} }},
		/*8*/{{{"name","пас3 пусто4 бросок3"},{"id","пас3_пусто4_бросок3"}}, {{""},{ "пас" ,"3"},{"" ,"4"},{ "бросок" ,"3"} }},
		/*9*/{{{"name","щелчок1 бросок4 кистевой2"},{"id","щелчок1_бросок4_кистевой2"}}, {{""},{ "щелчок" ,"1"},{"бросок" ,"4"},{"кистевой" ,"2"},}},
		/*10*/{{{"name","с удобной3 с неудобной1"},{"id","судобной3_снеудобной1"}}, {{""},{ "" ,"3"},{ "с неудобной" ,"1"} }},
		/*11*/{{{"name","бросок2 кистевой1"},{"id","бросок2_кистевой1"}}, {{""},{"бросок" ,"2"},{"кистевой" ,"1"},}},
		/*12*/{{{"name","щелчок"},{"id","щелчок_1"}}, {{""},{ "щелчок" ,"1"},}},
		/*13*/{{{"name","(бросок2 кистевой1)(удобн3 неудобн1)"},{"id","(бросок2_кистевой1)(удоб3_неудоб1)"}}, {{""},{ "->11,10" ,"1"},}},
		/*14*/{{{"name","щелчок1 (бросок2 кистевой1)(удобн3 неудобн1)3"},{"id","щелчок1_(бросок2_кистевой1)(удоб3_неудоб1)_1"}}, {{""},{ "щелчок" ,"1"},  { "->13" ,"3"},}},
		/*15*/{{{"name","ближняя2 дальная1 средняя2 дист"},{"id","ближ2_даль1_сред2"}}, {{""},{ "ближняя дистанция" ,"2"}  ,{ "средняя дистанция" ,"2"}  ,{ "дальняя дистанция" ,"1"} ,}},
		//с акцентами
		//1
		/*16*/{{{"name","слева1 пусто6 справа1 (скорость)"},{"id","слева1_пусто6_справа1(скорость)"}}, {{""},{ "объехать слева" ,"1"},{"" ,"6"},{ "объехать справа" ,"1"} }},
		/*17*/{{{"name","пас3 пусто3 бросок7(бросок)"},{"id","пас3_пусто3_бросок7(бросок)"}}, {{""},{ "пас" ,"3"},{"" ,"3"},{ "бросок" ,"7"} }},
		/*18*/{{{"name","щелчок7 бросок3 кистевой2(щелчок)"},{"id","щелчок7_бросок3_кистевой2(щелчок)"}}, {{""},{ "щелчок" ,"7"},{"бросок" ,"3"},{"кистевой" ,"2"},}},
		/*19*/{{{"name","с удобной1 с неудобной2(неудобная"},{"id","судобной1_снеудобной2(неудобная)"}}, {{""},{ "" ,"1"},{ "с неудобной" ,"2"} }},
		/*20*/{{{"name","бросок1 кистевой3(кистевой)"},{"id","бросок1_кистевой3(кистевой)"}}, {{""},{"бросок" ,"1"},{"кистевой" ,"3"},}},
		/*21*/{{{"name","РЕЗЕРВ(бросок2 кистевой1)(удобн3 неудобн1)"},{"id","(РЕЗЕРВбросок2_кистевой1)(удоб3_неудоб1)"}}, {{""},{ "->11,10" ,"1"},}},
		/*22*/{{{"name","РЕЗЕРВщелчок1 (бросок2 кистевой1)(удобн3 неудобн1)3"},{"id","РЕЗЕРВщелчок1_(бросок2_кистевой1)(удоб3_неудоб1)_1"}}, {{""},{ "щелчок" ,"1"},  { "->13" ,"3"},}},
		/*23*/{{{"name","ближняя1 дальная4 средняя1 дист(дальняя)"},{"id","ближ1_даль4_сред1(дальняя)"}}, {{""},{ "ближняя дистанция" ,"1"}  ,{ "средняя дистанция" ,"1"}  ,{ "дальняя дистанция" ,"4"} ,}},
		//2
		/*24*/{{{"name","слева2 пусто1 справа2 (маневр)"},{"id","слева2_пусто1_справа2(маневр)"}}, {{""},{ "объехать слева" ,"2"},{"" ,"1"},{ "объехать справа" ,"2"} }},
		/*25*/{{{"name","пас7 пусто3 бросок3(пас)"},{"id","пас7_пусто3_бросок3(пас)"}}, {{""},{ "пас" ,"7"},{"" ,"3"},{ "бросок" ,"3"} }},
		/*26*/{{{"name","счетчик роллерхокк корпус шайба"},{"id","счетчик_роллерхок"}}, {{""},{ "1" ,"1"},{"2" ,"1"},{ "3" ,"1"},{ "par3" ,"1"}  }},//,{"5" ,"1"},{ "6" ,"1"} ,{ "7" ,"1"},{"8" ,"1"},{ "9" ,"1"} }},

	};




	//>>>>>>>>>>>>>>>>>>>>>>


}
