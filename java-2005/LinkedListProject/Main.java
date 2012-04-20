public class Main
{
	public static void main(String[] args)
	{
		LinkedListTest l = new LinkedListTest(new Person("test"));
		l.addLast(new Person("first"));
		l.addFirst(new Person("abcs"));
		l.addLast(new Person("last"));
		l.addFirst(new Person("Adam"));
		l.addLast(new Person("Zebra"));
		System.out.println(l);
		System.out.println("first removed: " + ((ListNode)(l.removeFirst())).getData());
		System.out.println("last removed: " + ((ListNode)l.removeLast()).getData());
		System.out.println(l);
	}
}