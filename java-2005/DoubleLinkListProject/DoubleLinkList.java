public class DoubleLinkList
{
	DoubleListNode first;
	
	public DoubleLinkList()
	{
		first = null;
	}
	
	public DoubleLinkList(Object data)
	{
		first = new DoubleListNode(data,null,null);
	}
	
	public void addFirst(Object o)
	{
		DoubleListNode temp = new DoubleListNode(o,first,null);
		first.setPrev(temp);
		first = temp;
	}
	
	public void addLast(Object o)
	{
		if(first == null)
			addFirst(o);
		else
		{
			DoubleListNode last = first;
		
			while(last.getNext() != null)
				last = last.getNext();
			
			last.setNext(new DoubleListNode(o,null,last));
		}
	}
	
	public Object removeFirst()
	{
		if(first != null)
		{
			Object o = first.getData();
			first = first.getNext();
			first.setPrev(null);
			return o;
		}
		return null;
	}
	
	public Object removeLast()
	{
		if(first != null)
		{
			DoubleListNode curr = first, prev = first;
			
			while(curr.getNext() != null)
			{
				prev = curr;
				curr = curr.getNext();
			}
			Object o = curr.getData();
			prev.setNext(null);
			
			if(first == curr)
				first = null;
			
			return o;
		}
		
		return null;
	}
	
	public String backwards()
	{
		DoubleListNode backwards = first;
		String s = "";
		
		while(backwards.getNext() != null)
			backwards = backwards.getNext();
		
		while(backwards.getPrev() != null)
		{
			s+=backwards.getData() + " <-- ";
			backwards=backwards.getPrev();
		}
		s+=backwards.getData().toString();

		return s;
	}	
	
	public String toString()
	{
		String s = "The list is empty";
		if(first != null)
		{
			DoubleListNode curr = first;
			s = "";
			
			while(curr.getNext() != null)
			{
				s+=curr.getData().toString() + " --> ";
				curr=curr.getNext();
			}
			s+=curr.getData().toString();
		}
		return s;
	}
}