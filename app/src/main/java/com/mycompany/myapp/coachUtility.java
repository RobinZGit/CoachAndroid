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
	protected int Gl_SelectedIndexInit = 0;

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
	/*
		//=========== тест =====
	    {
			{{{"name","тест  "} ,{"id","тест"} }},
			{{{"disabled","0"}}},
			{{{"tags","зарядка#"}}},
			{{{"parameters"},{"par1","float","длительность сек","1.5"}
					,{"par2","string","сказать","ураааа"}
					,{ "par3","string","количество","6"}
					,{"par4","string","дисперсия","3.9"}
					//,{"par5","string","пропорция перебежки (к 6)","1"}
				} },
			{},{},{},{},{},{}, //резерв
			{    
    			{{"par3","раз по"},{"par1","сек с дисперсией"},{"par4"},{"сказать","par2"}},     

			}
		},
		*/
		//=====================
		//=========== универсальная настраиваемая 1 =====
	    
		{
			{{{"name","универсальный случайный счетчик до 10 видов команд"} ,{"id","универсальная_настраиваемая_1"} }},
			{{{"disabled","0"}}},
			{{{"tags","универсальные"}}},
			{{{"parameters"}
					//,{"par999","float","наименование тренировки","универсальный случайный счетчик до 10 видов команд"}
			        ,{"par000","float","скорость (%)","100"}
					,{"par001","string","общее количество упражнений","50"}
					
					,{"par011","string","команда упр 1","1"}
					,{"par012","string","длительность упр 1 (сек)","3"}
					,{"par013","string","пропорция упр 1","1"} 
			
					,{"par021","string","команда упр 2","2"}
					,{"par022","string","длительность упр 2 (сек)","3"}
					,{"par023","string","пропорция упр 2","1"} 
					
					,{"par031","string","команда упр 3","3"}
					,{"par032","string","длительность упр 3 (сек)","3"}
					,{"par033","string","пропорция упр 3","1"} 
					
					,{"par041","string","команда упр 4","4"}
					,{"par042","string","длительность упр 4 (сек)","3"}
					,{"par043","string","пропорция упр 4","1"} 
					
				    ,{"par051","string","команда упр 5","5"}
					,{"par052","string","длительность упр 5 (сек)","3"}
					,{"par053","string","пропорция упр 5","1"} 
					
			    	,{"par061","string","команда упр 6","6"}
					,{"par062","string","длительность упр 6 (сек)","3"}
					,{"par063","string","пропорция упр 6","1"} 
					
	        		,{"par071","string","команда упр 7","7"}
					,{"par072","string","длительность упр 7 (сек)","3"}
					,{"par073","string","пропорция упр 7","1"} 
					
				    ,{"par081","string","команда упр 8","8"}
					,{"par082","string","длительность упр 8 (сек)","3"}
					,{"par083","string","пропорция упр 8","1"} 
					
				    ,{"par091","string","команда упр 9","9"}
					,{"par092","string","длительность упр 9 (сек)","3"}
					,{"par093","string","пропорция упр 9","1"} 
					
					,{"par101","string","команда упр 10","10"}
					,{"par102","string","длительность упр 10 (сек)","3"}
					,{"par103","string","пропорция упр 10","1"} 
				}       },

			{},{},{},{},{},{}, //резерв
			{   
				//{{"1","раз по"},{"10", "сек с дисперсией"},{"0"},{"сказать","первая цифра - положение корпуса. вторая цифра - положение шайбы"}}, 
    			{{"par001","раз по"},{"1","сек с дисперсией"},{"0"},{"сказать","->30"}},     

			}
		},

		//=========== счетчик для роллерхок =====
	    {
			{{{"name","счет 1-4 номер квадрата + 1-4 номер квадрата шайбы "} ,{"id","роллерхоккей_счетчик"} }},
			{{{"disabled","0"}}},
			{{{"tags","хоккей#счетчики"}}},
			{{{"parameters"}
			        ,{"par1","float","скорость (%)","100"}
					,{"par2","string","комментарий к тренировке",""}
	                ,{"par00","string","пропорция пустого движения (ни броска ни перебежки)","4"}
					,{"par3","string","перебежка (если не нужна - оставить пустое поле)","перебежка"}
					,{"par4","string","длительность перебежка (сек)","2"}
					,{"par5","string","пропорция перебежки ","3"} 
					,{"par06","string","бросок (если не нужен - оставить пустое поле)","бросок"}
					,{"par61","string","длительность броска (сек)","3"}
					,{"par7","string","пропорция броска ","3"} 
					}       },
					
			{},{},{},{},{},{}, //резерв
			{   
				//{{"1","раз по"},{"10", "сек с дисперсией"},{"0"},{"сказать","первая цифра - положение корпуса. вторая цифра - положение шайбы"}}, 
    			{{"150","раз по"},{"<par1>","сек с дисперсией"},{"0"},{"сказать","->26,26,27"}},     

			}
		},
		//=====================
		
		//=========== счетчик для роллерхок 2 =====
	    {
			{{{"name","счет 1-N  "} ,{"id","роллерхоккей_счетчик_1_N"} }},
			{{{"disabled","0"}}},
			{{{"tags","хоккей#счетчики"}}},
			{{{"parameters"}
			        ,{"par001","float","скорость (%)","100"}
					,{"par2","string","комментарий к тренировки",""}
					//,{"par3","string","1","1"}
					,{"par004","string","длительность интервала(сек)","2"}
					,{"par005","string","1","1"}
					,{"par500","string","пропорция 1","1"}
					,{"par006","string","2","2"}
					,{"par600","string","пропорция 2","1"}
					,{"par007","string","3","3"}
					,{"par700","string","пропорция 3","1"}
					,{"par008","string","4","4"}
					,{"par800","string","пропорция 4","1"}
					,{"par009","string","5","5"}
					,{"par900","string","пропорция 5","0"}
					,{"par010","string","6","6"}
				//	,{"par1000","string","пропорция 6","0"}
					//,{"par5","string","пропорция перебежки (к 10)","3"} 
					//,{"par6","string","бросок (если не нужна - оставить пустое поле)","бросок"}
					//,{"par7","string","пропорция броска (к 10)","2"} 
				}       },

			{},{},{},{},{},{}, //резерв
			{   
				//{{"1","раз по"},{"10", "сек с дисперсией"},{"0"},{"сказать","первая цифра - положение корпуса. вторая цифра - положение шайбы"}}, 
    			{{"100","раз по"},{"par1","сек с дисперсией"},{"0"},{"сказать","->29"}},     

			}
		},
		//=====================
		
		//=========== табата 1 =====
	    {
			{{{"name","табата со случайными упражнениями (8 раз по 20 сек упр и 10 сек отдых)"} ,{"id","табата_случайные_упры"} }},
			{{{"disabled","0"}}},
			{{{"tags","зарядка#"}}},
			{{{"parameters"},{"par1","float","интенсивность (%)","100"}
					,{"par2","string","комментарий к тренировке",""}
					//,{ "par3","string","перебежка (если не нужна - оставить пустой)","перебежка"}
					//,{"par4","string","длительность перебежка (сек)","3"}
					//,{"par5","string","пропорция перебежки (к 6)","1"}
					} },
			{},{},{},{},{},{}, //резерв
			{    
    			{{"20/(3*(100/par1))","раз по"},{"3*(100/par1)","сек с дисперсией"},{"0"},{"сказать","->28"}},     
				{{"5","раз по"},{"2","сек с дисперсией"},{"0"},{"сказать","отдых"}}, 
				{{"20/(3*(100/par1))","раз по"},{"3*(100/par1)","сек с дисперсией"},{"0"},{"сказать","->28"}},   
				{{"5","раз по"},{"2","сек с дисперсией"},{"0"},{"сказать","отдых"}}, 
				{{"20/(3*(100/par1))","раз по"},{"3*(100/par1)","сек с дисперсией"},{"0"},{"сказать","->28"}},     
				{{"5","раз по"},{"2","сек с дисперсией"},{"0"},{"сказать","отдых"}}, 
				{{"20/(3*(100/par1))","раз по"},{"3*(100/par1)","сек с дисперсией"},{"0"},{"сказать","->28"}},     
				{{"5","раз по"},{"2","сек с дисперсией"},{"0"},{"сказать","отдых"}}, 
				{{"20/(3*(100/par1))","раз по"},{"3*(100/par1)","сек с дисперсией"},{"0"},{"сказать","->28"}},     
				{{"5","раз по"},{"2","сек с дисперсией"},{"0"},{"сказать","отдых"}}, 
				{{"20/(3*(100/par1))","раз по"},{"3*(100/par1)","сек с дисперсией"},{"0"},{"сказать","->28"}},     
				{{"5","раз по"},{"2","сек с дисперсией"},{"0"},{"сказать","отдых"}}, 
				{{"20/(3*(100/par1))","раз по"},{"3*(100/par1)","сек с дисперсией"},{"0"},{"сказать","->28"}},     
				{{"5","раз по"},{"2","сек с дисперсией"},{"0"},{"сказать","отдых"}}, 
				{{"20/(3*(100/par1))","раз по"},{"3*(100/par1)","сек с дисперсией"},{"0"},{"сказать","->28"}},     
				{{"5","раз по"},{"2","сек с дисперсией"},{"0"},{"сказать","отдых"}}, 
				
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
			{{{"parameters"},
			  {"par1","string","комментарий к тренировке",""},
			 // {"par2","string","комментарий к тренировке",""},
			
			}       },

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
		/*0*/{{{"name","ссылка12"},{"id","ссылкатест"}}, {{   "ОБЩАЯ ФРАЗА",""},{ "СКАЗАТЬ:","->1,2","С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:", "1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""}}},
	    /*1*/{{{"name","сказать о или ы1"},{"id","вдохвыдох1"}}, {{  "ОБЩАЯ ФРАЗА", ""},{ "СКАЗАТЬ:","iif(остатокделна4(номеритерации1)-1,о,ы)" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""}}},
	    /*2*/{{{"name","сказать о или ы2"},{"id"," вдохвыдох2"}}, {{   "ОБЩАЯ ФРАЗА",""},{"СКАЗАТЬ:", "iif(grdssin(номеритерации1*20*grdssin(номеритерации1/80)),о,ы)" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""}}},
	    /*3*/{{{"name","сказать о или ы3"},{"id"," вдохвыдох3"}}, {{   "ОБЩАЯ ФРАЗА",""},{ "СКАЗАТЬ:","iif(остатокделна4(номеритерации1),о,ы)" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""}}},

	    //==  для хоккея  ==========
		/*4*/{{{"name","ближний1 средний1 дальний1"},{"id","ближний1_средний1_дальний1"}}, {{  "ОБЩАЯ ФРАЗА", ""},{ "СКАЗАТЬ:","ближний" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{ "СКАЗАТЬ:", "средний" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{ "СКАЗАТЬ:","дальний" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""} }},
		/*5*/{{{"name","вверх1 центр1 вниз1"},{"id","вверх1_центр1_вниз1"}}, {{   "ОБЩАЯ ФРАЗА",""},{ "СКАЗАТЬ:","вверх" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1"  ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{ "СКАЗАТЬ:","центр" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{ "СКАЗАТЬ:","вниз" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" } }},
		/*6*/{{{"name","влево1 центр1 вправо1"},{"id","влево1_центр1_вправо1"}}, {{   "ОБЩАЯ ФРАЗА",""},{ "СКАЗАТЬ:","влево" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1"  ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{ "СКАЗАТЬ:","центр" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" },{ "СКАЗАТЬ:","вправо" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1"  ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""} }},
		/*7*/{{{"name","слева1 пусто2 справа1"},{"id","слева1_пусто2_справа1"}}, {{   "ОБЩАЯ ФРАЗА",""},{ "СКАЗАТЬ:","объехать слева" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1"  ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{ "СКАЗАТЬ:","" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","2"  ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{ "СКАЗАТЬ:","объехать справа" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" } }},
		/*8*/{{{"name","пас3 пусто4 бросок3"},{"id","пас3_пусто4_бросок3"}}, {{  "ОБЩАЯ ФРАЗА", ""},{  "СКАЗАТЬ:","пас" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","3"  ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{ "СКАЗАТЬ:","" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","4"  ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{ "СКАЗАТЬ:","бросок" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","3"  ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""} }},
		/*9*/{{{"name","щелчок1 бросок4 кистевой2"},{"id","щелчок1_бросок4_кистевой2"}}, {{  "ОБЩАЯ ФРАЗА", ""},{ "СКАЗАТЬ:","щелчок" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{"СКАЗАТЬ:","бросок" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","4" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" },{ "СКАЗАТЬ:","кистевой" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","2" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},}},
		/*10*/{{{"name","с удобной3 с неудобной1"},{"id","судобной3_снеудобной1"}}, {{  "ОБЩАЯ ФРАЗА", ""},{ "СКАЗАТЬ:","" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","3" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{ "СКАЗАТЬ:","с неудобной" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""} }},
		/*11*/{{{"name","бросок2 кистевой1"},{"id","бросок2_кистевой1"}}, {{   "ОБЩАЯ ФРАЗА",""},{ "СКАЗАТЬ:","бросок" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","2" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{"СКАЗАТЬ:","кистевой" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},}},
		/*12*/{{{"name","щелчок"},{"id","щелчок_1"}}, {{  "ОБЩАЯ ФРАЗА", ""},{"СКАЗАТЬ:", "щелчок" ,"1"  ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},}},
		/*13*/{{{"name","(бросок2 кистевой1)(удобн3 неудобн1)"},{"id","(бросок2_кистевой1)(удоб3_неудоб1)"}}, {{   "ОБЩАЯ ФРАЗА",""},{ "СКАЗАТЬ:","->11,10" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" },}},
		/*14*/{{{"name","щелчок1 (бросок2 кистевой1)(удобн3 неудобн1)3"},{"id","щелчок1_(бросок2_кистевой1)(удоб3_неудоб1)_1"}}, {{  "ОБЩАЯ ФРАЗА", ""},{ "СКАЗАТЬ:","щелчок" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},  { "СКАЗАТЬ:","->13","С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"3" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},}},
		/*15*/{{{"name","ближняя2 дальная1 средняя2 дист"},{"id","ближ2_даль1_сред2"}}, {{  "ОБЩАЯ ФРАЗА", ""},{ "СКАЗАТЬ:","ближняя дистанция" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","2" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" }  ,{ "СКАЗАТЬ:","средняя дистанция" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","2"  ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""}  ,{ "СКАЗАТЬ:","дальняя дистанция" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" } ,}},
		//с акцентами
		//1
		/*16*/{{{"name","слева1 пусто6 справа1 (скорость)"},{"id","слева1_пусто6_справа1(скорость)"}}, {{  "ОБЩАЯ ФРАЗА", ""},{ "СКАЗАТЬ:","объехать слева" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1"  ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{  "СКАЗАТЬ:","" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","6" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{ "СКАЗАТЬ:","объехать справа" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1"  ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""} }},
		/*17*/{{{"name","пас3 пусто3 бросок7(бросок)"},{"id","пас3_пусто3_бросок7(бросок)"}}, {{  "ОБЩАЯ ФРАЗА", ""},{ "СКАЗАТЬ:","пас" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","3" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" },{  "СКАЗАТЬ:","","С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"3" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" },{ "СКАЗАТЬ:","бросок" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","7" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" } }},
		/*18*/{{{"name","щелчок7 бросок3 кистевой2(щелчок)"},{"id","щелчок7_бросок3_кистевой2(щелчок)"}}, {{  "ОБЩАЯ ФРАЗА",""},{ "СКАЗАТЬ:","щелчок" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","7" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" },{  "СКАЗАТЬ:","бросок" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","3" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{ "СКАЗАТЬ:","кистевой" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","2" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" },}},
		/*19*/{{{"name","с удобной1 с неудобной2(неудобная"},{"id","судобной1_снеудобной2(неудобная)"}}, {{  "ОБЩАЯ ФРАЗА", ""},{ "СКАЗАТЬ:","" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" },{"СКАЗАТЬ:", "с неудобной" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","2" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" } }},
		/*20*/{{{"name","бросок1 кистевой3(кистевой)"},{"id","бросок1_кистевой3(кистевой)"}}, {{  "ОБЩАЯ ФРАЗА",""},{  "СКАЗАТЬ:","бросок" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" },{ "СКАЗАТЬ:", "кистевой" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","3" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},}},
		/*21*/{{{"name","РЕЗЕРВ(бросок2 кистевой1)(удобн3 неудобн1)"},{"id","(РЕЗЕРВбросок2_кистевой1)(удоб3_неудоб1)"}}, {{   "ОБЩАЯ ФРАЗА",""},{ "СКАЗАТЬ:","->11,10" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" },}},
		/*22*/{{{"name","РЕЗЕРВщелчок1 (бросок2 кистевой1)(удобн3 неудобн1)3"},{"id","РЕЗЕРВщелчок1_(бросок2_кистевой1)(удоб3_неудоб1)_1"}}, {{   "ОБЩАЯ ФРАЗА",""},{ "СКАЗАТЬ:","щелчок" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" },  { "СКАЗАТЬ:","->13" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","3" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" },}},
		/*23*/{{{"name","ближняя1 дальная4 средняя1 дист(дальняя)"},{"id","ближ1_даль4_сред1(дальняя)"}}, {{  "ОБЩАЯ ФРАЗА",""},{"СКАЗАТЬ:",  "ближняя дистанция" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" }  ,{  "СКАЗАТЬ:", "средняя дистанция" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" }  ,{  "СКАЗАТЬ:", "дальняя дистанция" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","4" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","" } ,}},
		//2
		/*24*/{{{"name","слева2 пусто1 справа2 (маневр)"},{"id","слева2_пусто1_справа2(маневр)"}}, {{   "ОБЩАЯ ФРАЗА",""},{  "СКАЗАТЬ:", "объехать слева" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","2" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{ "СКАЗАТЬ:", "" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{  "СКАЗАТЬ:", "объехать справа" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","2" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""} }},
		/*25*/{{{"name","пас7 пусто3 бросок3(пас)"},{"id","пас7_пусто3_бросок3(пас)"}}, {{  "ОБЩАЯ ФРАЗА", ""},{ "СКАЗАТЬ:",  "пас" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","7" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{  "СКАЗАТЬ:","" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","3" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""},{  "СКАЗАТЬ:", "бросок" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:","3" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""} }},
		/*26*/{{{"name","счетчик роллерхокк корпус шайба"},{"id","счетчик_роллерхок"}}, {{ "ОБЩАЯ ФРАЗА",""},{ "СКАЗАТЬ:","1","С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"1","ДЛИТЕЛЬНОСТЬ (СЕК):","1*(100/par1)"},{ "СКАЗАТЬ:","2" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"1","ДЛИТЕЛЬНОСТЬ (СЕК):","1*(100/par1)" },{  "СКАЗАТЬ:","3" , "С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","1*(100/par1)"},{  "СКАЗАТЬ:","4", "С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","1*(100/par1)"}  }},//,{"5" ,"1"},{ "6" ,"1"} ,{ "7" ,"1"},{"8" ,"1"},{ "9" ,"1"} }},
		/*27*/{{{"name","счетчик роллерхокк  перебежка "},{"id","счетчик_роллерхок_перебежка"}}, {{ "ОБЩАЯ ФРАЗА",""}
		        ,{  "СКАЗАТЬ:","" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"par00" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","0,01"}
				,{  "СКАЗАТЬ:","par3" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" , "par5" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","par4*(100/par1)"} 
				,{  "СКАЗАТЬ:","par06" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" , "par7" ,"ДЛИТЕЛЬНОСТЬ (СЕК):","par61*(100/par1)"} 
					}},//,{"5" ,"1"},{ "6" ,"1"} ,{ "7" ,"1"},{"8" ,"1"},{ "9" ,"1"} }},
		/*28*/{{{"name","табата со случайными упр"},{"id","табата_случ_упр"}}, {{ "ОБЩАЯ ФРАЗА",""}
		        ,{  "СКАЗАТЬ:","подпрыгнуть" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"2" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""}//"3*(100/par1)"}
				,{  "СКАЗАТЬ:","подтянуться" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" , "1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""}//"3*(100/par1)"} 
				,{  "СКАЗАТЬ:","отжаться" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"2" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""}//"3*(100/par1)"}
				,{  "СКАЗАТЬ:","левый пистолетик" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""}//"3*(100/par1)"}
				,{  "СКАЗАТЬ:","правый пистолетик" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"1" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""}//"3*(100/par1)"}
				,{  "СКАЗАТЬ:","книжка" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"2" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""}//"""}//"}
				,{  "СКАЗАТЬ:","лодочка" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"2" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""}//"3*(100/par1)"}
				,{  "СКАЗАТЬ:","левая лодочка" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"2" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""}//"3*(100/par1)"}
				,{  "СКАЗАТЬ:","правая лодочка" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"2" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""}//"3*(100/par1)"}
				,{  "СКАЗАТЬ:","встать на руки" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"2" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""}//"3*(100/par1)"}
				,{  "СКАЗАТЬ:","курочка" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"2" ,"ДЛИТЕЛЬНОСТЬ (СЕК):",""}//"3*(100/par1)"}
			}},//,{"5" ,"1"},{ "6" ,"1"} ,{ "7" ,"1"},{"8" ,"1"},{ "9" ,"1"} }},
			
		/*29*/{{{"name","счетчик 1-N"},{"id","счетчик_1_N"}}, {{ "ОБЩАЯ ФРАЗА",""}
		        ,{ "СКАЗАТЬ:","parp05" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"par500","ДЛИТЕЛЬНОСТЬ (СЕК):","1*(100/par001)" }
	            ,{ "СКАЗАТЬ:","par006" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"par600","ДЛИТЕЛЬНОСТЬ (СЕК):","1*(100/par001)" }
				,{ "СКАЗАТЬ:","par007" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"par700","ДЛИТЕЛЬНОСТЬ (СЕК):","1*(100/par001)" }
				,{ "СКАЗАТЬ:","par008" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"par800","ДЛИТЕЛЬНОСТЬ (СЕК):","1*(100/par001)" }
				,{ "СКАЗАТЬ:","par009" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"par900","ДЛИТЕЛЬНОСТЬ (СЕК):","1*(100/par001)" }
				//,{ "СКАЗАТЬ:","par010" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"par1000","ДЛИТЕЛЬНОСТЬ (СЕК):","1*(100/par1)" }
		    }},//,{"5" ,"1"},{ "6" ,"1"} ,{ "7" ,"1"},{"8" ,"1"},{ "9" ,"1"} }},
			
		/*30*/{{{"name","универсальная 1-10"},{"id","универсальная_1_10"}}, {{ "ОБЩАЯ ФРАЗА",""}
		        ,{ "СКАЗАТЬ:","par011" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"par013","ДЛИТЕЛЬНОСТЬ (СЕК):","  par012*(100/par000)" }
	            ,{ "СКАЗАТЬ:","par021" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"par023","ДЛИТЕЛЬНОСТЬ (СЕК):","  par022*(100/par000)" }
				,{ "СКАЗАТЬ:","par031" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"par033","ДЛИТЕЛЬНОСТЬ (СЕК):","  par032*(100/par000)" }
				,{ "СКАЗАТЬ:","par041" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"par043","ДЛИТЕЛЬНОСТЬ (СЕК):","  par042*(100/par000)" }
				,{ "СКАЗАТЬ:","par051" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"par053","ДЛИТЕЛЬНОСТЬ (СЕК):","  par052*(100/par000)" }
				//,{ "СКАЗАТЬ:","par010" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"par1000","ДЛИТЕЛЬНОСТЬ (СЕК):","  par012*(100/par1)" }
				,{ "СКАЗАТЬ:","par061" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"par063","ДЛИТЕЛЬНОСТЬ (СЕК):","  par062*(100/par000)" }
	            ,{ "СКАЗАТЬ:","par071" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"par073","ДЛИТЕЛЬНОСТЬ (СЕК):","  par072*(100/par000)" }
				,{ "СКАЗАТЬ:","par081" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"par083","ДЛИТЕЛЬНОСТЬ (СЕК):","  par082*(100/par000)" }
				,{ "СКАЗАТЬ:","par091" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"par093","ДЛИТЕЛЬНОСТЬ (СЕК):","  par092*(100/par000)" }
				,{ "СКАЗАТЬ:","par101" ,"С ПРОПОРЦ. ВЕРОЯТНОСТЬЮ:" ,"par103","ДЛИТЕЛЬНОСТЬ (СЕК):","  par102*(100/par000)" }
				
		    }},
		
	};




	//>>>>>>>>>>>>>>>>>>>>>>


}
