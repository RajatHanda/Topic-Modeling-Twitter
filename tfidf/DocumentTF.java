package tfidf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DocumentTF {
	public String filename="";
	public HashMap<String, Double> TermFrequencies=new HashMap<String, Double>();
	public long numTerms=0;
	public int DocumentId=-1;
	
	public void NormalizeTermFrequencies()
	{
		Set<?> set=TermFrequencies.entrySet();
		Iterator<?> i = set.iterator();
	      // Display elements
	      while(i.hasNext()) {
	         @SuppressWarnings("unchecked")
			Map.Entry<String,Double> me = (Map.Entry<String,Double>)i.next();
	         TermFrequencies.put((String)me.getKey(), new Double((Double)me.getValue()/(double)numTerms));
	      }
	}
	
	public ArrayList<String> processTermFrequencies(String[] tokens1)
	{
		ArrayList<String> nonEssentials=new ArrayList<String>();
		for(int i=0;i<tokens1.length;i++)
		{
			if(!stopWords.StopWords.containsKey(tokens1[i]))
			{	
				numTerms++;
				if(TermFrequencies.containsKey(tokens1[i]))
					TermFrequencies.put(tokens1[i], new Double(TermFrequencies.get(tokens1[i])+1));
				else
					TermFrequencies.put(tokens1[i], new Double(1));
			}
			else
			{
				if(!nonEssentials.contains(tokens1[i]))
					nonEssentials.add(tokens1[i]);
			}
		}
		return nonEssentials;
	}
	
	public ArrayList<String> getTermFrequencies()
	{
		try {
			ArrayList<String> nonEssentials=null;
			if(!TermFrequencies.isEmpty())
				return null;
			BufferedReader br=new BufferedReader(new FileReader(filename));
			String TopS;
			numTerms=0;
			while((TopS=br.readLine())!=null)
			{
				TopS=TopS.toLowerCase();
				String[] tokens1=TopS.split("[^0-9a-z]+");
				nonEssentials=processTermFrequencies(tokens1);
			}
			br.close();
			NormalizeTermFrequencies();
			return nonEssentials;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String> getTermFrequencies(String[] Tokens)
	{
		ArrayList<String> nonEssentials=processTermFrequencies(Tokens);
		NormalizeTermFrequencies();
		return nonEssentials;
	}
	
	public static void displayNonEssentials(ArrayList<String> nonEssentials)
	{
		System.out.print('[');
		for(String s:nonEssentials)
			System.out.print(s+",");
		System.out.print(']');
	}
	
	public void displayTermFrequencies(DecimalFormat d1)
	{
		try {
			FileWriter fw=new FileWriter("TermFrequencies"+DocumentId+".txt");
			fw.write("Word\tTF\n");
			Set<?> set=TermFrequencies.entrySet();
			Iterator<?> i = set.iterator();
		      // Display elements
		      while(i.hasNext()) {
		         @SuppressWarnings("unchecked")
				Map.Entry<String,Double> me = (Map.Entry<String,Double>)i.next();
		         fw.write(me.getKey()+"\t"+d1.format(me.getValue())+"\n");
		      }
		      fw.close();
		      System.out.println("\nTerm Frequency List for this Document logged in "+"TermFrequencies"+DocumentId+".txt file");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DocumentTF()
	{
	}
	
	public DocumentTF(String filename,int DocumentId) {
		// TODO Auto-generated constructor stub
		this.filename=filename;
		this.DocumentId=DocumentId;
		ArrayList<String> nonEssentials=this.getTermFrequencies();
		if(!nonEssentials.isEmpty())
		{
			System.out.print("\nDocument "+this.DocumentId+" ("+this.filename+") : ");
			displayNonEssentials(nonEssentials);
		}
	}
	
}
