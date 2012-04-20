public class DoubleCircularLinkList
{
	protected DoubleListNode current;
	
	public DoubleCircularLinkList()
	{
		current = null;	
	}
	
	public DoubleCircularLinkList(Object data)
	{
		current = new DoubleListNode(data,null,null);
		current.setNext(current);
		current.setPrev(current);
	}
	
	public void addNode(Object data)
	{
		current.setNext(new DoubleListNode(data,current.getNext(),current));	
		DoubleListNode temp = current;
		
		while(temp.getNext() != current)
			temp = temp.getNext();
			
		current.setPrev(temp);
		current.getNext().getNext().setPrev(current.getNext());
	}
	
	public Object removeNode()
	{
		if(current.getNext() != current)
		{
			DoubleListNode removed = current;
			current = current.getNext();
			DoubleListNode tempNewCurrent = current;
			
			while(tempNewCurrent.getNext() != removed)
				tempNewCurrent = tempNewCurrent.getNext();
			
			tempNewCurrent.setNext(current);
			current.setPrev(tempNewCurrent);
			
			return removed.getData();	
		}
		else
			current = null;
		
		return null;
	}
	
	public String toString()
	{
		String s = "";
		if(current != null)
		{
			DoubleListNode curr = current;
			s = "";
			
			while(curr.getNext() != null)
			{
				s+=curr.getData().toString() + " --> ";
				curr=curr.getNext();
				System.out.println(s);
			}
			s+=curr.getData().toString();
		}
		return s;
	}
	
	public String backwards()
	{
		DoubleListNode backwards = current;
		String s = "";
		
		while(backwards.getNext() != current)
			backwards = backwards.getNext();
		
		while(backwards.getPrev() != null)
		{
			s+=backwards.getData() + " <-- ";
			backwards=backwards.getPrev();
			System.out.println(s);
		}
		s+=backwards.getData().toString();

		return s;
		
	}	
}