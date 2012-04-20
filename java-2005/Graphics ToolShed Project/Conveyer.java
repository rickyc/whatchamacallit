// ------------------------------------------------
// | Conveyer Class |
// ------------------
// This class is the conveyer where the tools are 
// made and where part of the movement occurs
// ------------------------------------------------

import java.awt.Color;

public class Conveyer extends CircularLinkList implements ListIteratorTest
{
	private ListNode curr;
	
	// default constructor
	public Conveyer()
	{
		for(int i=0;i<58;i++)
		{
			int randNum = (int)(Math.random()*6);
			
			if(i == 0)
				setFirst(Tool.getTool(randNum));
			else
				addNode(Tool.getTool(randNum));
		}
		curr = current;
	}
	
	// shifts the current
	public void shift()
	{
		curr = curr.getNext();	
	}
		
	// checks if theres a next
	public boolean hasNext()
	{
		return (current != null);
	}
	
	// returns the current object and moves the conveyer
	public Object next()
	{	
		Object o = current.getData();
		ListNode tempNode = current;
		
		while(tempNode.getNext() != current)
			tempNode=tempNode.getNext();
			
		current = tempNode;
		
		return o;
	}

	// removes a tool from the conveyer and then replaces it with another one
	public Object remove()
	{
		Object o = curr.getData();
		curr.setData(Tool.getTool((int)(Math.random()*6)));
	
		return o;	
	}
}