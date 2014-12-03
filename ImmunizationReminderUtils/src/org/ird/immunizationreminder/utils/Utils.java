package org.ird.immunizationreminder.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

public class Utils {
	private static Random rnd=new Random();
	public static boolean isNumberBetween(String number,int min,int max){
		try{
			int num=Integer.parseInt(number);
			if(num<min || num>max){
				return false;
			}
		}catch (Exception e) {
			return false;
		}
		return true;
	}
	public static int getRandomNumber(int range) {
		int num=rnd.nextInt(range);
		return num+1;
	}
	public static int getRandomNumber(int min,int max) {
		int numRange=max-min+1;
		int num=rnd.nextInt(numRange)+min;
		return num;
	}
	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			System.out.println(getRandomNumber(3,4));
		}
	}
	public static String getListAsString(List<String> collection,String separator){
		StringBuilder strb=new StringBuilder();
		int len=collection.size();
		
		int index=0;
		for (String string : collection) {
			if(index==len-1){
				strb.append(string);
			}else{
				strb.append(string+separator);
			}
			index++;
		}
		return strb.toString();
	}
	public static StringBuilder convertStreamToStringBuilder(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;

		while ((line = reader.readLine()) != null) {
		sb.append(line + "\n");
		}
		is.close();
		return sb;
	}
}
