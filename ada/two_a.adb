with Ada.Text_IO, Ada.Integer_Text_IO;
use Ada.Text_IO, Ada.Integer_Text_IO;

procedure two_a is
	Input: String(1..500);
	Quit: String := "quit";
	Count: Integer := 0;
	Word: Boolean := False;
	Len: Integer := 0;
begin
	loop
	Put_Line("Please type 'quit' to end the program.");
	New_Line;
	Put_line("Please type in some text.");
	Get_Line(Input,Len);

	exit when Input(1..4) = Quit AND Len = 4;

	Put("Number of characters in string: ");
	Put(Len);
	--Put(Input'length);
	New_Line;
	
	for I in 1 .. Len loop
		if Input(I) = ' ' then
			Word := False;
		elsif not Word and Input(I) /= ' ' then -- it is a word
			Word:= True;
			Count := Count +1;
		end if;
	end loop;
	
	Put("Number of words in string: ");
	Put_Line(Integer'Image(Count));
	end loop;
end two_a;
