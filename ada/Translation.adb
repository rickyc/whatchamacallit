with Ada.Text_IO, Ada.Integer_Text_IO;
use Ada.Text_IO, Ada.Integer_Text_IO;

procedure translation is
    setTable:array(1..26) of Integer; -- H => 1(A)
    subTable:array(1..26) of Integer; -- A => H

    input:String(1..500);

    encodeStr:String(1..10000); 
    encodeStrLen:Integer := 1;

    quit: String := "quit";
    count: Integer:= 0;
    len:Integer := 0;
    
    ASCII:Integer := 64; -- 65 is A, 64 represents the first array position
    counter:Integer := 1; -- counter for letters
    index: Integer; -- temp variable for key
begin
    -- set default values for table
    for i in 1 .. setTable'length loop
	setTable(i) := 0;
	subTable(i) := 0;
    end loop;
    
    Put_Line("Please type the string to encode");
    Get_Line(input,len); -- grabs the key
    for i in 1 .. len loop
	index := Character'Pos(input(i)); -- index of encode table
	if (index >= 65 and index <= 90) or (index >= 97 and index <= 122) then
	    -- converts letters to caps
	    if index >= 97 then
		index := index-32;
	    end if;
	    
	    index := index - ASCII; -- position

	    if setTable(index) = 0 then
		setTable(index) := counter+ASCII;
		subTable(counter) := index+ASCII;
		counter := counter + 1; -- increment counter
	    end if;
	end if;
    end loop;
    
    -- fills in the rest of the table
    for i in reverse 1 .. setTable'length loop
	if setTable(i) = 0 then
	    setTable(i) := counter+ASCII;
	    subTable(counter) := i+ASCII;
	    counter := counter + 1;
	end if;
    end loop;
	
    -- gets string to encode
    Put_Line("Please type in some text, quit to exit");
    loop
	Get_Line(input,len);
    exit when input(1..4) = quit and len = 4;
	encodeStr(encodeStrLen .. encodeStrLen+len-1) := input(1..len);
	encodeStrLen := encodeStrLen + len;
    end loop;
    
    -- prints out encoded string
    for i in 1 .. encodeStrLen loop
	index := Character'pos(encodeStr(i));
	--- ******
	if (index >= 65 and index <= 90) or (index >= 97 and index <= 122) then
	    -- converts letters to caps
	    if index >= 97 then
		index := index-32;
	    end if;
	    
	    index := index - ASCII; -- position
	    Put(Character'val(subTable(index)));

	    count := count + 1;
	    if count = 8 then
		count := 0;
		New_Line;
	    end if;
	end if;
    end loop;
    New_Line;

    -- testing prints out table
--    for i in 1 .. subTable'length loop
--	Put(Character'val(i+ASCII) & " => " & Character'val(subTable(i)));
--	New_Line;
--    end loop;
end Translation;
