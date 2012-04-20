import java.util.LinkedList;
public class ListQueue implements Queue
{
	private LinkedList lQueue;
	public ListQueue()
	{
		lQueue=new LinkedList();
	}
	public boolean isEmpty()
	{
		return (lQueue.size()==0);
	}
	public void enqueue(Object o)
	{
		lQueue.addLast(o);
	}
	public Object dequeue()
	{
		return lQueue.removeFirst();
	}
	public Object peekFront()
	{
		return lQueue.getFirst();
	}
}