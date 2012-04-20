public class Main
{
	public static int[] abc;
	
	public static void main(String[] args)
	{
		abc = new int[1000000];
		for(int i=0;i<abc.length;i++)
		{	abc[i] = (int)(Math.random()*100);	
			System.out.print("[ " + abc[i] + " ]");	
		}
		
		System.out.println("\n-------------");
		MergeSort.mergeSort(abc);
		
		for(int i=0;i<abc.length;i++)
			System.out.print("[ " + abc[i] + " ]");	
	}
}