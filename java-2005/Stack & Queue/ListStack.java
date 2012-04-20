import java.util.LinkedList;
public class ListStack implements Stack
{
	private LinkedList lStack;
	public ListStack()
	{
		lStack=new LinkedList();
	}
	public boolean isEmpty()
	{
		return (lStack.size()==0);
	}
	public void push(Object o) // Put something on top.
	{
		lStack.addFirst(o);
	}
	public Object pop() // Remove top.
	{
		return lStack.removeFirst();
	}
	public Object peekTop() // Return top.
	{
		return lStack.getFirst();
	}
}