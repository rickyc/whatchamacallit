public class Main
{
	public static DoubleCircularLinkList d;
	
	public static void main(String[] args)
	{
		d = new DoubleCircularLinkList(new Person("a"));
		d.addNode(new Person("c"));
		d.addNode(new Person("b"));
		d.addNode(new Person("d"));
		d.removeNode();
		d.removeNode();
		d.removeNode();
	//	d.removeNode();
		//System.out.println(d);
		System.out.println(d.current.getData());
		System.out.println(d.backwards());
	}
}