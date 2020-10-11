package com.mycompany.myapp;

	import android.app.*;
	import android.os.*;
	import java.util.*;


	public class MatchParser
	{
		private HashMap<String, Double> variables;

		public MatchParser()
		{
			variables = new HashMap<String, Double>();
		}

		public void setVariable(String variableName, Double variableValue)
		{
			variables.put(variableName, variableValue);
		}

		public Double getVariable(String variableName)
		{
			if (!variables.containsKey(variableName)) {
				System.err.println( "Error: Try get unexists variable '"+variableName+"'" );
				return 0.0;
			}
			return variables.get(variableName);
		}

		public double Parse(String s) throws Exception
		{   
		
			String s1 = s.replace(" ","");
		    String sF="";
			String sT="";
			String sIf="";
			String sIfName = "iif(";//аналог клипперовского iif
			int pos1 = 0;
			int pos2 = 0;
		    if (s1.indexOf(sIfName) >= 0 ){
				sIf = s1.substring(sIfName.length(),s1.indexOf(","));
				pos1 = s1.indexOf(",");
				pos2 = s1.indexOf(",",pos1+1); //!!но не получится так рекурсивный иф..
				sT = s1.substring(pos1 + 1, pos2);
				sF = s1.substring(pos2 + 1,s1.length() );
			    if (Parse(sIf) > 0) s1 = sT; else s1 = sF; 
			}
			Result result = PlusMinus(s1);
			if (!result.rest.isEmpty()) {
				System.err.println("Error: can't full parse");
				System.err.println("rest: " + result.rest);
			}
			return result.acc;
		}
		
    //=======
	public String SParse(String s) //throws Exception
	{   

		String s1 = s;//.replace(" "," ");
		String sF="";
		String sT="";
		String sIf="";
		String sRet = s1;
		String sIfName = "iif(";//аналог клипперовского iif
		String sParseName = "parse(";//парсинг
		int pos1 = 0;
		int pos2 = 0;
		if (s1.indexOf(sIfName) >= 0 ){
			sIf = s1.substring(sIfName.length(),s1.indexOf(","));
			pos1 = s1.indexOf(",");
			pos2 = s1.indexOf(",",pos1+1); //!!но не получится так рекурсивный иф..
			sT = s1.substring(pos1 + 1, pos2);
			sF = s1.substring(pos2 + 1,s1.length());
			try{
			  if (Parse(sIf) > 0.0) sRet = sT; else sRet = sF;
			} catch (Exception e ) {}  
		}
		/*
		if (s1.indexOf(sParseName) >= 0 ){
			sIf = s1.substring(sIfName.length(),s1.indexOf(","));
			pos1 = s1.indexOf(",");
			pos2 = s1.indexOf(",",pos1+1); //!!но не получится так рекурсивный иф..
			sT = s1.substring(pos1 + 1, pos2);
			sF = s1.substring(pos2 + 1,s1.length());
			try{
				if (Parse(sIf) > 0.0) sRet = sT; else sRet = sF;
			} catch (Exception e ) {}  
		    
		}
		*/
		return sRet;
	}
		
		//**************
		/*
	   public double iif(Double pIf, String sIfTrue, String sIfFalse) throws Exception
	   {
		
		  Result result = PlusMinus("0");
	   	  if (pIf > 0) {
		    result = PlusMinus(sIfTrue);}
		  else {
		    result = PlusMinus(sIfFalse);}
		
		  if (!result.rest.isEmpty()) {
			System.err.println("Error: can't full parse");
			System.err.println("rest: " + result.rest);
		  }
		  return result.acc;
	    }
		
	    public String siif(Double pIf, String sIfTrue, String sIfFalse) throws Exception
	    {
		    String result = "";
		   if (pIf > 0) {
		     result = sIfTrue;}
		   else {
		     result = sIfFalse;}


		   return result;
	     }
		 */
		//*************

		private Result PlusMinus(String s) throws Exception
		{
			Result current = MulDiv(s);
			double acc = current.acc;

			while (current.rest.length() > 0) {
				if (!(current.rest.charAt(0) == '+' || current.rest.charAt(0) == '-')) break;

				char sign = current.rest.charAt(0);
				String next = current.rest.substring(1);

				current = MulDiv(next);
				if (sign == '+') {
					acc += current.acc;
				} else {
					acc -= current.acc;
				}
			}
			return new Result(acc, current.rest);
		}

		private Result Bracket(String s) throws Exception
		{
			char zeroChar = s.charAt(0);
			if (zeroChar == '(') {
				Result r = PlusMinus(s.substring(1));
				if (!r.rest.isEmpty() && r.rest.charAt(0) == ')') {
					r.rest = r.rest.substring(1);
				} else {
					System.err.println("Error: not close bracket");
				}
				return r;
			}
			return FunctionVariable(s);
		}

		private Result FunctionVariable(String s) throws Exception
		{
			String f = "";
			int i = 0;
			// ищем название функции или переменной
			// имя обязательно должна начинаться с буквы
			while (i < s.length() && (Character.isLetter(s.charAt(i)) || ( Character.isDigit(s.charAt(i)) && i > 0 ) )) {
				f += s.charAt(i);
				i++;
			}
			if (!f.isEmpty()) { // если что-нибудь нашли
				if ( s.length() > i && s.charAt( i ) == '(') { // и следующий символ скобка значит - это функция
					Result r = Bracket(s.substring(f.length()));
					return processFunction(f, r);
				} else { // иначе - это переменная
					return new Result(getVariable(f), s.substring(f.length()));
				}
			}
			return Num(s);
		}

		private Result MulDiv(String s) throws Exception
		{
			Result current = Bracket(s);

			double acc = current.acc;
			while (true) {
				if (current.rest.length() == 0) {
					return current;
				}
				char sign = current.rest.charAt(0);
				if ((sign != '*' && sign != '/')) return current;

				String next = current.rest.substring(1);
				Result right = Bracket(next);

				if (sign == '*') {
					acc *= right.acc;
				} else {
					acc /= right.acc;
				}

				current = new Result(acc, right.rest);
			}
		}

		private Result Num(String s) throws Exception
		{
			int i = 0;
			int dot_cnt = 0;
			boolean negative = false;
			// число также может начинаться с минуса
			if( s.charAt(0) == '-' ){
				negative = true;
				s = s.substring( 1 );
			}
			// разрешаем только цифры и точку
			while (i < s.length() && (Character.isDigit(s.charAt(i)) || s.charAt(i) == '.')) {
				// но также проверям, что в числе может быть только одна точка!
				if (s.charAt(i) == '.' && ++dot_cnt > 1) {
					throw new Exception("not valid number '" + s.substring(0, i + 1) + "'");
				}
				i++;
			}
			if( i == 0 ){ // что-либо похожее на число мы не нашли
				throw new Exception( "can't get valid number in '" + s + "'" );
			}

			double dPart = Double.parseDouble(s.substring(0, i));
			if( negative ) dPart = -dPart;
			String restPart = s.substring(i);

			return new Result(dPart, restPart);
		}

		// Тут определяем все нашие функции, которыми мы можем пользоватся в формулах
		private Result processFunction(String func, Result r)
		{
			if (func.equals("sin")) {
				return new Result(Math.sin(Math.toRadians(r.acc)), r.rest);
			} else if (func.equals("cos")) {
				return new Result(Math.cos(Math.toRadians(r.acc)), r.rest);
			} else if (func.equals("tan")) {
				return new Result(Math.tan(Math.toRadians(r.acc)), r.rest);
			} else if (func.equals("grdscos")) {
				return new Result(Math.cos(r.acc), r.rest);
		    } else if (func.equals("grdssin")) {
				return new Result(Math.sin(r.acc), r.rest);
			}  else if (func.equals("нечетное")) {
			return new Result(r.acc%2, r.rest);
			} else if (func.equals("остатокделна4")) {
				return new Result(r.acc%4, r.rest);
			} else if (func.equals("целоеделна4")) {
				return new Result(Math.round( r.acc/4), r.rest);
			} else if (func.equals("целоеделна8")) {
				return new Result(Math.round( r.acc/8), r.rest);
		    } else if (func.equals("irandom")) {
				return new Result(Math.floor( Math.random()*r.acc), r.rest);
			} else if (func.equals("random")) {
				return new Result(Math.random()*r.acc, r.rest);
			} else if (func.equals("round")) {
				return new Result(Math.round(r.acc), r.rest);
		    } else if (func.equals("полноевремя")) { // ! не зависит от аргумента
				Date d = new Date();
				//return new Result(/*d.getYear()*366*31*24*60*60 + d.getMonth()*31*24*60*60 +  d.getDay()*24*60*60 + d.getHours()*60*60 + */d.getMinutes()*60 + d.getSeconds(), r.rest);
				return new Result(d.getTime(), r.rest);
		    
		    } else if (func.equals("bernulli01")) {
				rithmFunc rf = new rithmFunc();
				int ri =0;
				if (rf.bernulli(r.acc)) ri=1; else ri=0;
		    	return new Result( ri, r.rest);
			} 
			else {
				System.err.println("function '" + func + "' is not defined");
			}
			return r;
		}
	}
