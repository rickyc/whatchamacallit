public class TList
{
	protected ListNode first;
	protected int size;
	
	public TList()
	{
		first = null;
		size = 0;
	}
	
	public TList(Object data)
	{
		first = new ListNode(data,null);
		size=1;
	}
	
	public boolean isEmpty()
	{
		return (first == null);
	}

	public void addFirst(Object data)
	{
		first = new ListNode(data,first);	
		size++;
	}	

	public void addLast(Object data)
	{
		if(size == 0)
			addFirst(data);
		else
		{
			ListNode last = first;
		
			while(last.getNext() != null)
				last = last.getNext();
			
			last.setNext(new ListNode(data,null));
			size++;
		}	
	}
	
	public Object removeFirst()
	{
		Object origFirst = first.getData();
		
		if(!isEmpty())
		{
			first = first.getNext();
			size--;
		}
		
		return origFirst;
	}
	
	public Object removeLast()
	{
		if(!isEmpty())
		{
			ListNode curr=first,prev=first;
			
			while(curr.getNext() != null)
			{
				prev = curr;
				curr = curr.getNext();
			}
			Object o = curr.getData();
			prev.setNext(null);
		
			if(first == curr)
				first = null;
			size--;
			return o;
		}
		
		return null;
	}
	
	public String toString()
	{
		String s = "The list is empty";
		if(!isEmpty())
		{
			ListNode curr = first;
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
	
	public ListNode getFirst(){return first;}
	public int getSize(){return size;};
}