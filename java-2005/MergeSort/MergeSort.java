public class MergeSort
{
	public static int[] mergeSort(int[] a)
	{
		if(a.length > 1)
		{
			int[] left, right;
			
			if(a.length%2 == 0)
			{
				left = new int[(int)(a.length/2)];
				right = new int[(int)(a.length/2)];
//				for(int i=0;i<left.length;i++)
//				{
//					left[i] = a[i];
//					right[i] = a[i+(int)(a.length/2)];
//				}
			}
			else
			{  
				left = new int[(int)(a.length/2)+1];
				right = new int[(int)(a.length/2)];
//				for(int i=0;i<left.length;i++)
//					left[i] = a[i];
//				for(int i=0;i<right.length;i++)
//					right[i] = a[i+(int)(a.length/2)+1];
				
			}
			for(int i=0;i<left.length;i++)
				left[i] = a[i];
			for(int i=0;i<right.length;i++)
				right[i] = a[i+left.length];
			
			mergeSort(left);
			mergeSort(right);
			
			int lctr = 0, rctr = 0;
			
			for(int i=0;i<a.length;i++)
			{
				if(lctr >= left.length)
				{
					a[i] = right[rctr];
					rctr++;
				}
				else
				{
					if(rctr >= right.length)
					{
						a[i] = left[lctr];
						lctr++;
					}
					else
					{
						if(left[lctr] < right[rctr])
						{
							a[i] = left[lctr];
							lctr++;
						}
						else
						{
							a[i] = right[rctr];
							rctr++;
						}
					}
				}
			}
		}

		return a;
	
	}
	
	public static void print(int[] array)
	{
		for(int i=0;i<array.length;i++)
			System.out.print("[ " + array[i] + " ]");
		System.out.println();
	}
}