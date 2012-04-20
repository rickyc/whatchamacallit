public interface Stack
{
	boolean isEmpty();
	void push(Object o); // put something on top
	Object pop(); // remove top
	Object peekTop(); // return top
}