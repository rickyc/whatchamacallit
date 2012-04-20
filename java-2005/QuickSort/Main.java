public class Main
{
	private static int[] abc;
	private static long timer = System.currentTimeMillis();
	
	public static void main(String[] args)
	{
		abc = new int[500000];
		
		for(int i=0;i<abc.length;i++)
			abc[i] = (int)(Math.random()*100);	
			
		QuickSort.qSort(abc,0,abc.length-1);
		
		System.out.println("TIMER: " + (System.currentTimeMillis() - timer));
		for(int i=0;i<abc.length;i++)
			System.out.print("[ " + abc[i] + " ]");
		System.out.println();	
	}
}
