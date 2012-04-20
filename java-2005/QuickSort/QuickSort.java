public class QuickSort
{
	public static int[] qSort(int[] a, int start, int end)
	{
		int pivot, pivotIndex, left = start+1, right = end;
		
		if(start < end)
		{
			pivot = a[start];
			do
			{
				if(a[left] >= pivot && a[right] < pivot)
				{
					int temp = a[left];
					a[left] = a[right];
					a[right] = temp;
				}
				if(a[left] < pivot)
					left++;
				if(a[right] >= pivot)
					right--;
			}while(left<right);
			
			pivotIndex = left;
			if(left > end || a[left] >= pivot)
				pivotIndex--;
			
			a[start] = a[pivotIndex];
			a[pivotIndex] = pivot;
			
			qSort(a,start,pivotIndex-1);
			qSort(a,pivotIndex+1,end);
		}	
		
		return a;
	}
	
	public static int[] quickSort(int[] a, int left, int right)
	{
		System.out.println("LEFT: " + left + " RIGHT: " + right);
		if(left<right)
		{
			int pivot = a[left], lctr = left+1, rctr = right;
			
			while(lctr < rctr)
			{
				if(a[lctr] > pivot && a[rctr] <= pivot)
				{
					int temp = a[lctr];
					a[lctr] = a[rctr];
					a[rctr] = temp;
				}
				if(a[lctr] < pivot)
					lctr++;
				if(a[rctr] >= pivot)
					rctr--;
				print(a,"A ");
			}
			System.out.println("LCTR: " + left + " RCTR: " + right);
			if(a[lctr] < a[lctr-1])
				pivot = lctr;
			else
				pivot = lctr-1;
		
			int temp = a[pivot];
			a[pivot] = a[0];
			a[0] = temp;
			print(a,"A ");
			
			System.out.println("- - - - - - -");
			quickSort(a,left,pivot);
			quickSort(a,pivot+1,right);
		}
		return a;
	}

	public static void print(int[] a, String str)
	{
		System.out.print(str);
		for(int i=0;i<a.length;i++)
			System.out.print("[ " + a[i] + " ]");
		System.out.println();
	}
}