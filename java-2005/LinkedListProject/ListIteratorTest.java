public interface ListIteratorTest
{
	public boolean hasNext();
	// returns true if curr has next thats not null
	public Object next();
	// returns the data from curr // makes the next node current
	public Object remove();
	// deletes current, makes hte next node curr, returns curr data
}
//  -------->29
// 24
// |       top circular link list
// |       6 shafts (3x4) TTT
//\/ 