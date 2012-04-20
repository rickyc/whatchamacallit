public class ToolBelt extends TList implements ListIteratorTest
{
	public ToolBelt()
	{
//		for(int i=0;i<5;i++)
//			addLast(Tool.getTool(i));
	}
	
	public void add(Tool t)
	{
		if(size == 5)
		{
			removeFirst();
			addFirst(t);
		}
		else
			addFirst(t);
	}
	
	public boolean hasNext()
	{
		return (first.getNext() != null);
	}
	
	public Object next()
	{
		Object o = first.getData();
		addLast(first.getData());
		removeFirst();

		return o;
	}
	
	public Object remove()
	{
		Object o = first.getData();
		removeFirst();
		
		return o;
	}
}