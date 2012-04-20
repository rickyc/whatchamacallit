public class Main
{
	public static DoubleLinkList d;
	
	public static void main(String[] args)
	{
		d = new DoubleLinkList(new Person("a"));
		d.addFirst(new Person("e"));
		d.addFirst(new Person("b"));
		d.addFirst(new Person("z"));
		
		d.addLast(new Person("yy"));
		d.addLast(new Person("zzz"));
		System.out.println(d);
		System.out.println(d.removeFirst());
		System.out.println(d.removeLast());
		System.out.println(d);
		System.out.println(d.backwards());
	}
}