public interface Queue
{
	boolean isEmpty();
	void enqueue(Object o); // Add object from back.
	Object dequeue(); // Remove front.
	Object peekFront(); // Return front.
}