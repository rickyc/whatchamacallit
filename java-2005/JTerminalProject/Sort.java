public class Sort
{
	public static void bubbleSort(HighScore []list)
	{
		for(int i=0;i<list.length;i++)
		{
			for(int w=0;w<list.length-1;w++)
			{
				if(list[w].getScore() < list[w+1].getScore())
				{
					HighScore temp = list[w];
					list[w] = list[w+1];
					list[w+1] = temp;
				}	
			}
		}	
	}
}