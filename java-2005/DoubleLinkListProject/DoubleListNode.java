public class DoubleListNode
{
	private DoubleListNode next, prev;
	private Object data;
	
	public DoubleListNode()
	{
		data = next = prev = null;
	}
	
	public DoubleListNode(Object data, DoubleListNode next, DoubleListNode prev)
	{
		this.data = data;
		this.next = next;
		this.prev = prev;
	}
	
	public void setData(Object data)
	{
		this.data = data;
	}
	
	public void setPrev(DoubleListNode prev)
	{
		this.prev = prev;
	}

	public void setNext(DoubleListNode next)
	{
		this.next = next;
	}
	
	public Object getData(){return data;}
	public DoubleListNode getNext(){return next;}
	public DoubleListNode getPrev(){return prev;}
}