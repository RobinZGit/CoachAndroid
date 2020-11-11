package com.mycompany.myapp;
/*
различные функции ритмов
*/
import java.sql.*;
import java.util.function.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class rithmFunc
{
	protected String dateFormat ="dd.MM.yyyy";
	//--------------------------------
	//ГЛАВНАЯ ФУНЦКЦИЯ. ПАРСИТ СТРОКУ ВИДА "f(p1,p2,...)" И ВЫЧИСЛЯЕТ. 
	//f НУЖНО ОПРЕДеЛЯТЬ В ЭТОМ МОДУЛЕ
	public String ev(String callStr){
		String sRet=callStr;
		try{
	    String sName = callStr.substring(0,callStr.indexOf("("));
		//! если вдруг будет запятая внутри параметра, нужно не просто сделаь split а вначале заменить ,, или ,\n на что-то, а после split сделать обратную замену во всех эл-х массива
		String[] aPar = callStr.substring(callStr.indexOf("(")+1,callStr.indexOf(")")).split(",");
		switch (sName){
			case "toNum": 
				try {sRet = String.valueOf( toNum( new SimpleDateFormat(dateFormat).parse(aPar[1]),aPar[3]));}
			              catch (Exception e){sRet = "0";}
			    break;
		    case "rithmLinear": 
			    sRet =String.valueOf(0.01*Math.round( 100* rithmLinear(Double.parseDouble( aPar[1]), Double.parseDouble( aPar[3]), Double.parseDouble( aPar[5]), Double.parseDouble( aPar[7]), Double.parseDouble( aPar[9]))));
				break;
			//case "rithmLinearFromDate2Date": 
			//    sRet =String.valueOf( rithmLinearFromDate2Date(new SimpleDateFormat(dateFormat).parse(aPar[1]), new SimpleDateFormat(dateFormat).parse(aPar[3]), Double.parseDouble( aPar[5]), new SimpleDateFormat(dateFormat).parse(aPar[7]), Double.parseDouble( aPar[9])));
			//	break;
			case "rithmLinearFromDate2DateByNow": 
			    sRet =String.valueOf(0.01*Math.round( 100*  rithmLinearFromDate2DateByNow( new SimpleDateFormat(dateFormat).parse(aPar[1]), Double.parseDouble( aPar[3]), new SimpleDateFormat(dateFormat).parse(aPar[5]), Double.parseDouble( aPar[7]))));
				break;
			case "rithmSin":
				sRet = String.valueOf(rithmSin(Double.parseDouble( aPar[1]), 
											   Double.parseDouble( aPar[1]), 
											   Double.parseDouble( aPar[1]), 
				                               Integer.parseInt(aPar[1]), 
				                               Double.parseDouble( aPar[1]), 
				                               Double.parseDouble( aPar[1])) 
				                    ) ;
			    break;
		    
				
		}
		}catch (Exception e){sRet=e.toString();}// callStr;}
		
		return sRet;
	}
	//--------------------------------

	//currDate: new Date(),



	//переводит дату в число до лет/месяцев/недель/дней/часов/минут/секунд
	//в формате  yyyymmddhhmiss
	public int toNum(Date date, String sMode) {
		//date.getFullYear?????
		//seconds
		switch (sMode)
		{
			case "year": return  date.getYear();
			case "month": return date.getYear() * 100 + date.getMonth();
				//! это не неделя по порядку ! case "day": return date.getFullYear()*1000 + date.getMonth()*10 + date.getDay();
			case "date": return date.getYear() * 10000 + date.getMonth() * 100 + date.getDate();
			case "hours": return date.getYear() * 1000000 + date.getMonth() * 10000 + date.getDate() * 100 + date.getHours();
			case "minutes": return date.getYear() * 100000000 + date.getMonth() * 1000000 + date.getDate() * 10000 + date.getHours() * 100 + date.getMinutes();
			case "seconds": return (date.getYear() * 100000*100000 + date.getMonth() * 100000000 + date.getDate() * 1000000 + date.getHours() * 10000 + date.getMinutes() * 100 + date.getSeconds());
            default: return 0;
		} 
	}

   //разность дат (в годах, месяцах,днях,часах,минутах,секундах
   public long dateDiff(Date dBeg, Date dEnd, String sFormat) {
	    long remaining = dEnd.getTime() - dBeg.getTime(); // миллисекунды до даты
	    switch (sFormat)
		{
			case "year": return remaining / (1000 * 60 * 60 * 24 * 365);    
		    case "month": return remaining / (1000 * 60 * 60 * 24 * 30);   
		    case "date": return remaining / (1000 * 60 * 60 * 24);   
		    case "hours": return remaining / (1000 * 60 * 60);   
		    case "minutes": return remaining / (1000 * 60);    
		    case "seconds": return remaining / 1000; 
			default: return 0;
		}
	}

	//линейный ритм с начальным и конечным значениями {rBeg.x,rBeg.y} {rEnd.x,rEnd.y}
	//возвращает значение в точке х. 
	public double rithmLinear(double x, double oBeg_x,double  oBeg_y, double oEnd_x, double oEnd_y) {
		return oBeg_y + (x - oBeg_x) * (oEnd_y - oBeg_y) / (oEnd_x - oBeg_x);
	}
	//линейный ритм с начальным и конечным значениями {dBeg, Beg.y} {dEnd,rEnd.y}
	//возвращает значение в точке х. 
	public double rithmLinearFromDate2Date(Date x, Date oBeg_x,double  oBeg_y, Date oEnd_x, double oEnd_y) {
		return  oBeg_y +  dateDiff(oBeg_x,x,"date") * (oEnd_y - oBeg_y) /dateDiff ( oBeg_x, oEnd_x,"date")
			   ;
	}
	//аналогично предыдущему но на текущую дату
	public double rithmLinearFromDate2DateByNow(Date oBeg_x,double  oBeg_y, Date oEnd_x, double oEnd_y) {
		Date x = new Date();
		return  oBeg_y +  dateDiff(oBeg_x,x,"date") * (oEnd_y - oBeg_y) /dateDiff ( oBeg_x, oEnd_x,"date")
			;
	}
	//Math.round(glOcalendar.rithmLinear(glOcalendar.toNum(glOcalendar.currDate,'date'),{x:20200101,y:12},{x:20211212,y:20}))"
	//,"+Math.round((glOcalendar.rithmSin(glOcalendar.toNum(glOcalendar.currDate,'date'),20200101,20200128,2,0,4)))"
	//,"раз(вдох и столько же выдох)","дых. в обед равномерно увелич по шагам","","true","",""],
	
	//синусоидальный ритм с nCycles периодов на отрезке xBeg,xEnd, средним значением nAverage и амплитудой nAmplitude
	//возвращает значение в точке х. 
	public double  rithmSin(double x, 
	                        double xBeg, 
	                        double xEnd, 
							int nCycles, 
							double nAverage, 
							double nAmplitude) {
		return nAverage + nAmplitude * Math.sin((x - xBeg) * 2 * 3.1415 * nCycles / (xEnd - xBeg));
	}


	//возвращает true с вероятностью probability, иначе false (испытание Бернулли)
	//  пример вызова glOcalendar.bernulli(0.5)
	public boolean bernulli (double probability) {
		return (probability >= Math.random());
	}


	//ритм по частоте появления: oFreq.numerator/oFreq.denominator
	//    выстраивает блоки по oFreq.denominator точек и отмечает их 0 или 1
	//    начинает с 1, потом идут oFreq.numerator/oFreq.denominator-1  нулей
	//    точность дроби видит до тысячных
	//возвращает значение 0 или 1 в точке n. 
	public int rithmFrequency (int n, int oFreq_numerator, int oFreq_denominator) {
	    if (oFreq_numerator >= oFreq_denominator) return 1;
		else if ( ((n % oFreq_denominator) % Math.floor(oFreq_denominator / oFreq_numerator)) == 0) return 1; else return 0;
	}

/*
	//антисплайн - приближение дискретной последовательностью 0-1 произвольной последоваетельности func(n) (область значений 0-1, больше 1 обрежет до 1). 
	//возвращает 0 или 1 - значение антисплайна в n
	public int antiSpline: function(n, func) {
		var fret = func(0) < 0.5 ? 0 : 1;
		var fsum = fret;
		var fsum0 = func(0);
		for (var i=1; i < n;i++)
		{
			fsum0 += func(i);
			fret = (fsum0 - fsum) < 0.5 ? 0 : 1;
			fsum += fret;
		}
		return fret;
	},
*//*
	//строка из 0 и 1 для ритма  formula  на nDaysForward дней вперед - покажется либо не покажется дело ритма в соотв. день
	getRithmStr: function(period_formula, nDaysForward, formula) {
		var flSowFull = (formula.length > 0);
		var sRet = "period_formula: '" + period_formula + "'\n\n"
			+ "rithm for " + nDaysForward + " days forward:\n\n"
			+ (flSowFull ? ("formula: " + formula + "'\n\n"): "");
		//запомним тек дату объекта
		var dCalendar = new Date(glOcalendar.currDate);
		for (var i=0; i < nDaysForward; i++)
		{
			if (ev(period_formula) || (period_formula.length == 0))  
				sRet += "1" + (flSowFull ? "   :   " + ev(formula) + "\n": ""); 
			else sRet += "0";
			glOcalendar.currDate.setDate(glOcalendar.currDate.getDate() + 1);
		}
		//вернем тек дату объекта
		glOcalendar.currDate.setDate(dCalendar.getDate());
		return sRet;
	},
*/
	//номер недели для дня d
	public int getWeekNumLocal(Date d){
		return (int) Math.floor(d.getDate() / 7);
	}
	
	
}
