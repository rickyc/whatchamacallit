// ------------------------------------------------
// | Circular Link List Class |
// ----------------------------
// This class is the circular link list
// ------------------------------------------------

public class CircularLinkList
{
	protected ListNode current;
	
	// default constructor
	public CircularLinkList()
	{
		current = null;
	}
	
	// another constructor
	public CircularLinkList(Object data)
	{
		setFirst(data);
	}
	
	// checks if the list is empty
	public boolean isEmpty()
	{
		return (current == null);
	}
	
	// set the first piece to the listnode
	public void setFirst(Object data)
	{
		current = new ListNode(data,current);
		current.setNext(current);
	}
	
	// adds a node to the circular link list
	public void addNode(Object data)
	{
		current.setNext(new ListNode(data,current.getNext()));
	}
	
	// removes a node from the circular link list
	public Object removeNode()
	{
		ListNode removed = current;
		current = current.getNext();
		ListNode tempNewCurrent = current;
		
		while(tempNewCurrent.getNext() != removed)
			tempNewCurrent = tempNewCurrent.getNext();
		
		tempNewCurrent.setNext(current);
		
		return removed.getData();	
	}
	
	// prints out the circular link list
	public String toString()
	{
		String s = "";
		if(!isEmpty())
		{
			ListNode curr = current;
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
}