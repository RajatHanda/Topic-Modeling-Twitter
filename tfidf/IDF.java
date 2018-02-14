package tfidf;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class IDF {

	public HashMap<String, Double> IDFList=new HashMap<String, Double>();
	
	public void getIDF(ArrayList<DocumentTF> DocList)
	{
		for(DocumentTF d1:DocList)
		{
			Set<?> set=d1.TermFrequencies.entrySet();
			Iterator<?> i = set.iterator();
		      // Display elements
		      while(i.hasNext()) {
		         @SuppressWarnings("unchecked")
				Map.Entry<String,Double> me = (Map.Entry<String,Double>)i.next();
		        String key=(String)me.getKey();
		        if(IDFList.containsKey(key))
		        	continue;
		        else
		        {
		        	int numDocs=0;
		        	for(DocumentTF d2:DocList)
		        		if(d2.TermFrequencies.containsKey(key))
		        			numDocs++;
		        	IDFList.put(key, new Double(1.0d+Math.log((double)DocList.size()/(double)numDocs)));
		        }
		      }			
		}
	}
	
	public ArrayList<String> getIDF(String[] Arguments,HashMap<String, Double> IDFList2)
	{
		ArrayList<String> nonEssentials=new ArrayList<String>();
		for(String s:Arguments)
		{
			if(stopWords.StopWords.containsKey(s))
			{
				if(!nonEssentials.contains(s))
					nonEssentials.add(s);
			}
			else
			{
				if(IDFList2.containsKey(s))
					IDFList.put(s, IDFList2.get(s));
				else
					IDFList.put(s, new Double(0));
			}
		}
		return nonEssentials;
	}
	
	public void displayIDFList(DecimalFormat d1)
	{
		try {
			FileWriter fw=new FileWriter("IDFLog.txt");
			fw.write("IDF List : \nWord\tIDF\n");
			Set<?> set=IDFList.entrySet();
			Iterator<?> i = set.iterator();
		      // Display elements
		      while(i.hasNext()) {
		         @SuppressWarnings("unchecked")
				Map.Entry<String,Double> me = (Map.Entry<String,Double>)i.next();
		         fw.write(me.getKey()+"\t"+d1.format(me.getValue())+"\n");
		      }
		      fw.close();
		      System.out.println("IDF List logged in IDFLog.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
