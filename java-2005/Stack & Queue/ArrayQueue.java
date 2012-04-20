import java.util.ArrayList;
public class ArrayQueue implements Queue
{
	private ArrayList aQueue;
	public ArrayQueue()
	{
		aQueue=new ArrayList();
	}
	public boolean isEmpty()
	{
		return (aQueue.size()==0);
	}
	public void enqueue(Object o)
	{
		aQueue.add(aQueue.size()-1,o);
	}
	public Object dequeue()
	{
		return aQueue.remove(0);
	}
	public Object peekFront()
	{
		return aQueue.get(0);
	}
}