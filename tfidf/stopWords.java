package tfidf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class stopWords {
	
	public static String StopFile="";
	public static HashMap<String, Object> StopWords=new HashMap<String, Object>();
	
	public static boolean getStop()
	{
		try {
			if(StopFile.isEmpty())
				return false;
			if(!StopWords.isEmpty())
				return true;
			BufferedReader br=new BufferedReader(new FileReader(StopFile));
			String TopS;
			while((TopS=br.readLine())!=null)
				StopWords.put(TopS, null);
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public static void setStopFilename(String s1)
	{
		StopFile=s1;
	}
	
}
