with Ada.Text_IO, Ada.Integer_Text_IO;
use Ada.Text_IO, Ada.Integer_Text_IO;
with Ada.Strings.Unbounded;
use Ada.Strings.Unbounded;

procedure LinkedList is
	type Node;
	type Node_Pointer is access Node;

	type Node is record
		Data: Unbounded_String;
		Next: Node_Pointer;
	end record;

	Head: Node_Pointer;
	
	procedure append(DataStr: Unbounded_String) is
		Temp: Node_Pointer;
		begin
			Temp := new Node;
			Temp.Data := DataStr;

			if Head = null then
				Head := Temp;
			else
				Temp.Next := Head;
				Head := Temp;
			end if;
	end append;
	
	function get(Index: Integer) return Node_Pointer is
		Temp: Node_Pointer;
		begin
		Temp := Head;
		for I in 1 .. Index loop
			Temp := Temp.Next;
			if Temp = null then
				return Temp;
			end if;
		end loop;
		return Temp;
	end get;

	procedure remove(Index: Integer) is
		Prev: Node_Pointer;
		Next: Node_pointer;		
				
		begin
		Prev := Head;
		if Index = 0 then
			Head := Head.next;
		else
			for I in 1 .. Index loop
				if I+1 /= Index then
					Prev := Prev.Next;
				end if;
				Next := Prev.Next;
			end loop;
			Prev.Next := Next.Next;
		end if;
	end remove;

	Foo: Node_pointer;
begin
	append(To_Unbounded_String("first input"));
	append(To_Unbounded_String("second input"));

	Foo := get(0);
	Put_Line(To_String(Foo.Data));

	Foo := get(1);
	Put_Line(To_String(Foo.Data));
	
	remove(0);
	Put_Line(To_String(Foo.Data));
end LinkedList;
