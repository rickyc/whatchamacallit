public class LinkedListTest
{
	private ListNode first;
	
	public LinkedListTest()
	{
		first = null;
	}
	
	public LinkedListTest(Object data)
	{
		first = new ListNode(data,null);
	}
	
	public boolean isEmpty()
	{
		return (first == null);
	}
	
	public void addFirst(Object data)
	{
		first = new ListNode(data,first);
	}
	
	public void addLast(Object data)
	{
		ListNode last = first;
		
		while(last.getNext() != null)
			last = last.getNext();
		
		last.setNext(new ListNode(data,null));
	}
	
	public Object removeFirst()
	{
		ListNode tempNode = new ListNode(first.getData(),first.getNext());
		
		if(first.getNext() == null)
			first = null;
		else
		{
			first.setData(first.getNext().getData());
			first.setNext(first.getNext().getNext());
		}
		
		return tempNode;
	}
	
	public Object removeLast()
	{
		ListNode tempNode = first;
		ListNode l = null;
		
		while(tempNode.getNext() != null)
		{
			if(tempNode.getNext() != null)
			{
				tempNode = tempNode.getNext();
				l = tempNode.getNext();
			}
			
			if(tempNode.getNext().getNext() == null)
				tempNode.setNext(null);
		}
		
		return l;	
	}
	
	public String toString()
	{
		String str = ""+first.getData()+" --> ";
		ListNode l = first.getNext();
		
		while(l != null)
		{
			str+=l.getData()+" --> ";
			l = l.getNext();
		}
		str+=null;
		return str;
	}
}