import java.util.*;
import java.io.*;


public class Kmeans {
	
	static ArrayList<ArrayList<Float>> D=new ArrayList<ArrayList<Float>>();
	static ArrayList<ArrayList<Float>> input=new ArrayList<ArrayList<Float>>();
	static ArrayList<ArrayList<Boolean>> G=new ArrayList<ArrayList<Boolean>>(),Gprev=new ArrayList<ArrayList<Boolean>>();
	static ArrayList<ArrayList<Float>> centroid=new ArrayList<ArrayList<Float>>();
	static ArrayList<Integer> centroidClusters=new ArrayList<Integer>();
	static int NClusters=0;
	static String filename="";
	
	public static void read_file()
	{		 
		BufferedReader br = null;
		try {
			String str;
			br = new BufferedReader(new FileReader(filename));
 
			System.out.println("Input Values : \n");
			for (int i=0;(str = br.readLine()) != null;i++) {
				// split by ','
				StringTokenizer st2 = new StringTokenizer(str, ",");
		 
				input.add(new ArrayList<Float>());
				D.add(new ArrayList<Float>());
				
				System.out.println("record "+i+" : "+str);
				
				while (st2.hasMoreElements()) {
					input.get(input.size()-1).add(Float.parseFloat(st2.nextElement().toString().trim()));
				}				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void compute_D()
	{
		for(int i=0;i<D.size();i++)
			D.get(i).clear();
		
		for(int i=0;i<NClusters;i++)
		{
			for(int j=0;j<input.size();j++)
			{
				double sum=0;
				for(int k=0;k<input.get(j).size();k++)
				{
					float diff=input.get(j).get(k)-centroid.get(i).get(k);
					sum+=(diff*diff);
				}
				sum=Math.sqrt(sum);
				D.get(j).add((float)sum);
			}
		}
	}

	public static void fill_G(ArrayList<ArrayList<Boolean>> x,int n)
	{
		for(int i=0;i<x.size();i++)
		{
			x.get(i).clear();
			for(int j=0;j<n;j++)
				x.get(i).add(false);
		}
	}
	
	public static void copy_G(ArrayList<ArrayList<Boolean>> x1,ArrayList<ArrayList<Boolean>> x2)
	{
		for(int i=0;i<x1.size();i++)
			for(int j=0;j<x1.get(i).size();j++)
				x1.get(i).set(j, x2.get(i).get(j));
	}
	
	public static boolean compare_G(ArrayList<ArrayList<Boolean>> x1,ArrayList<ArrayList<Boolean>> x2)
	{
		for(int i=0;i<x1.size();i++)
			for(int j=0;j<x1.get(i).size();j++)
				if(x1.get(i).get(j)!=x2.get(i).get(j))
					return false;
		return true;
	}
	
	public static void compute_G()
	{
		for(int i=0;i<D.size();i++)
		{
			int index=D.get(i).indexOf(Collections.min(D.get(i)));
			G.get(index).set(i, true);
		}
	}
	
	public static void compute_centroid()
	{
		for(int i=0;i<NClusters;i++)
		{
			int count=0;
			for(int j=0;j<centroid.get(i).size();j++)
			{
				float sum=0;
				count=0;
				for(int k=0;k<input.size();k++)
				{
					if(Gprev.get(i).get(k))
					{
						sum+=input.get(k).get(j);
						count++;
					}
				}
				centroid.get(i).set(j,sum/(float)count);
			}
			centroidClusters.set(i, count);
		}
	}
	
	public static void main(String[] Args)
	{
		Scanner in=new Scanner(System.in);
		System.out.println("Enter filename : ");
		filename=in.nextLine();
		read_file();
		do
		{
			System.out.println("Enter Number of Clusters (<= Number of Elements {"+input.size()+"}) : ");
			NClusters=in.nextInt();
		}while(NClusters>input.size());
		
		for(int i=0;i<NClusters;i++)
		{
			centroid.add(new ArrayList<Float>());
			for(int j=0;j<input.get(i).size();j++)
				centroid.get(i).add(input.get(i).get(j));
			G.add(new ArrayList<Boolean>());
			Gprev.add(new ArrayList<Boolean>());
			centroidClusters.add(0);
		}
		
		fill_G(G, input.size());
		fill_G(Gprev,input.size());
		
		int count=0;
		for(count=0;;count++)
		{
			compute_D();
			compute_G();
			
			if(compare_G(G, Gprev))
				break;
			
			copy_G(Gprev, G);
			fill_G(G, input.size());
			
			compute_centroid();
			
		}		
		
		System.out.println("\nNumber of Iterations Performed : "+count);
		System.out.println("\nCluster\tN(Elements)\tElements");
		for(int i=0;i<G.size();i++)
		{
			System.out.print("\n"+i+"\t"+centroidClusters.get(i)+" ("+Math.round((centroidClusters.get(i)*100)/(float)input.size())+"%)"+"\t\t");
			for(int j=0;j<G.get(i).size();j++)
				if(G.get(i).get(j))
					System.out.print(j+",");
		}
	}
}