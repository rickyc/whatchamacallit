public class ListNode
{
	private Object data;
	private ListNode next;
	
	public ListNode()
	{
		data = next = null;	
	}
	
	public ListNode(Object data, ListNode next)
	{
		this.data = data;
		this.next = next;
	}
	
	public void setData(Object data)
	{
		this.data = data;
	}
	
	public void setNext(ListNode next)
	{
		this.next = next;
	}
	
	public Object getData(){return data;}
	public ListNode getNext(){return next;}
}