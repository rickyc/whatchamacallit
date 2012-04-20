import java.util.ArrayList;
public class ArrayStack implements Stack
{
	private ArrayList aStack;
	public ArrayStack()
	{
		aStack=new ArrayList();
	}
	public boolean isEmpty()
	{
		return (aStack.size()==0);
	}
	public void push(Object o) // Put something on top.
	{
		aStack.add(o);
	}
	public Object pop() // Remove top.
	{
		return aStack.remove(aStack.size()-1);
	}
	public Object peekTop() // Return top.
	{
		return aStack.get(aStack.size()-1);
	}
}