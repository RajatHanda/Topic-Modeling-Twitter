package tfidf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Scanner;

/*
 * Theoretical Reference of TFIDF taken from https://janav.wordpress.com/2013/10/27/tf-idf-and-cosine-similarity/
 */

public class RunnableMain {

	public static ArrayList<DocumentTF> DocList=new ArrayList<DocumentTF>();
	public static IDF IDFList=new IDF();
	public static ArrayList<Docker> QuerryOutput=new ArrayList<Docker>();
	
	public static Locale locale=new Locale("en", "UK");
	public static DecimalFormat decimalFormat=(DecimalFormat)NumberFormat.getNumberInstance(locale);
	public static String pattern="##0.0000";	
	
	public static void getDocList(String DocFilename)
	{
		try {
			BufferedReader br=new BufferedReader(new FileReader(DocFilename));
			String TopS;
			DocList.clear();
			System.out.print("Non Essentials in Documents : ");
			for(int i=0;(TopS=br.readLine())!=null;i++)
			{
				DocList.add(new DocumentTF(TopS, i));
				DocList.get(DocList.size()-1).displayTermFrequencies(decimalFormat);
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Docker> getCosineSimilarity(String[] tokens,DocumentTF qd,IDF qidf)
	{
		ArrayList<Docker> q1=new ArrayList<Docker>();
		for(DocumentTF d1:DocList)
		{
			System.out.println("\nDocument "+d1.DocumentId+" ("+d1.filename+") : ");
			System.out.println("Word\tQuerry TF\tQuerry IDF\tDocument TF\tDocument IDF\tQuerry TFIDF\tDocument TFIDF");
			double dotProduct=0d,querry=0d,document=0d;
			for(String word:tokens)
			{
				if(!stopWords.StopWords.containsKey(word))
				{
					Double tf1=qd.TermFrequencies.get(word);
					Double idf1=qidf.IDFList.get(word);
					
					Double tf2=d1.TermFrequencies.get(word);
					Double idf2=IDFList.IDFList.get(word);
					
					if(tf2==null)
						tf2=0d;
					if(idf2==null)
						idf2=0d;
					
					
					Double tfidf1=tf1*idf1,tfidf2=tf2*idf2;
					dotProduct+=tfidf1*tfidf2;
					
					querry+=(tfidf1*tfidf1);
					
					document+=(tfidf2*tfidf2);

					System.out.println(word+"\t"+decimalFormat.format(tf1)+"\t"+decimalFormat.format(idf1)+"\t"+decimalFormat.format(tf2)
							+"\t"+decimalFormat.format(idf2)+"\t"+decimalFormat.format(tfidf1)+"\t"+decimalFormat.format(tfidf2));
				}
			}
			querry=Math.sqrt(querry);
			document=Math.sqrt(document);
			
			System.out.println("dotProduct = "+decimalFormat.format(dotProduct)+"\t||Querry|| = "+decimalFormat.format(querry)
					+"\t||Document|| = "+decimalFormat.format(document));
			
			if(querry*document==0d)	
				q1.add(new Docker(d1, 0d));
			else
				q1.add(new Docker(d1, dotProduct/(querry*document)));
		}
		return q1;
	}
	
	public static void displayQuerryOutput()
	{
		System.out.println("Document Id\tDocument Path\tCosineSimilarity(TFIDF)");
		for(Docker d1:QuerryOutput)
			System.out.println(d1.document.DocumentId+"\t"+d1.document.filename+"\t"+decimalFormat.format(d1.TFIDF));
	}
	
	public static void main(String[] Args)
	{
		if(Args.length!=2)
		{
			System.out.println("Implementation : TFIDF <DocumentListFile> <StopWordsFile>");
			return;
		}
		stopWords.setStopFilename(Args[1]);
		stopWords.getStop();
		getDocList(Args[0]);
		IDFList.getIDF(DocList);
		IDFList.displayIDFList(decimalFormat);
		decimalFormat.applyPattern(pattern);
		
		@SuppressWarnings("resource")
		Scanner in=new Scanner(System.in);
		String Command="",Querry="";
		do
		{
			System.out.println("\n\nEnter Querry : ");
			Command=in.next();
			Querry=in.nextLine().trim().toLowerCase();
			if(Command.isEmpty() || Command.contains("$quit") || Querry.isEmpty() || !Command.contains("$search"))
				continue;
			String[] Tokens=Querry.split("[^0-9a-z]+");
			
			DocumentTF qd=new DocumentTF();
			ArrayList<String> nonEssentials=qd.getTermFrequencies(Tokens);
			if(!nonEssentials.isEmpty())
			{
				System.out.print("Querry \""+Querry+"\" contains non Essentials : ");
				DocumentTF.displayNonEssentials(nonEssentials);
			}
			IDF qidf=new IDF();
			qidf.getIDF(Tokens, IDFList.IDFList);
			
			QuerryOutput=getCosineSimilarity(Tokens,qd,qidf);
			Collections.sort(QuerryOutput);
			System.out.println("\nSearch Results for Querry : \""+Querry+"\" are : ");
			displayQuerryOutput();
		}while(!Command.contains("$quit"));		
	}
	
}

	class Docker implements Comparable<Docker>
	{
		DocumentTF document=null;
		double TFIDF=-1;
		
		public Docker(DocumentTF t1,double t2) {
			// TODO Auto-generated constructor stub
			document=t1;
			TFIDF=t2;
		}

		@Override
		public int compareTo(Docker o) {
			// TODO Auto-generated method stub
			return Double.compare(o.TFIDF,TFIDF);
		}
	}
