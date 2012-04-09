; Ricky Cheng
; Input: 1000h - single byte, Output: 1001h - output 1, 1002h - output 2, 1003 - reversed, note i refer 1st digit as the tens digit and the 2nd digit as the ones digit

		mov al, byte ptr 1000h		; gets the input from 1000h and puts it into register al
		mov bl, 0					; clears the bl register
lp1:	cmp al, 16					; compares the al register to 16, while(al > 16)
		jb exit16					; if al is lower then 16 then jump out of loop, if(al < 16) go to exit 16
		sub al, 16					; subtracts 16 off al and then adds one to the counter
		add bl, 1					; adds one to the counter for digit one
		jmp lp1						; repeat the loop
exit16: cmp bl, 10					; compares the first digit
		jb d1bel					; if (bl < 10) then jump down to dlbel else
		sub bl, 10					; subtract then from the number so if it is 10 - 10 = 0 then 
		add bl, 'A'					; adds the character 'A' to it to get the hex place
		jmp nxt						; jump and do the 2nd digit
d1bel:	add bl, '0'					; adds ASCII '0' or 30
nxt:	cmp al, 10					; compares 2nd digit to 10, if(al < 10)
		jb d2bel					; jump to d2bel
		sub al, 10					; else if(a > 10) subtract 10 from it
		add al, 'A'					; add the ascii letter 'A'
		jmp part2					; jump to part 2
d2bel:	add al, '0'					; add ascii '0' to digit 2 if it was less then 10
part2:	mov byte ptr 1001h, bl		; moves the 1st digit into 1001h
		mov byte ptr 1002h, al		; moves the 2nd digit into 1002h
									; part 2 of project, reverse order
		cmp bl, 'A'					; if(bl < 'a') or if (first digit is less then 41)
		jb d1low					; if it is lower meaning if it is from 0-9 then jump
		add bl, 10					; else add ten because it is greater then 9
		sub bl, 'A'					; then you subtract by ascii A to get the number of the 1st digit
		jmp d1mul					; jump to the step digit 1 multiple, what the jump will do is 16*(1st digit) so it will return its hex value
d1low:	sub bl, '0'					; subtract by ascii 0		
d1mul:	add bl, bl					; multiply bl by 2
		add bl, bl					; bl x 4
		add bl, bl					; bl x 8
		add bl, bl					; bl x 16
		mov cl, bl					; move bl to cl
		cmp al, 'A'					; if(bl < 'a') or if (2nd digit is less then 41)
		jb d2low					; if it is lower meaning if it is from 0-9 then jump
		add al, 10					; else add ten because it is greater then 9
		sub al, 'A'					; then you subtract by ascii A to get the number of the 1st digit
		jmp d2fin					; jumps to finish, what that does is add the two together
d2low:	sub al, '0'					; subtract by ascii 0
d2fin:	add cl, al					; add bl to cl
		mov byte ptr 1003h, cl		; moves the converted number into 1003h
		mov ah, 0EH   				;  the subfuction of the Video Services ROM Interrupt
		mov bh, 0					;  set the video page
		mov cl, byte ptr 1001h		; moves it back into cl to print
		mov al, cl					;  where cl is a constant, register, or memory position of the ASCII char
		int 10h      				;  actually writes the character on the screen and moves the cursor one position
		mov cl, byte ptr 1002h		; movesi t back to cl to print
		mov al, cl					; moves cl into register al to be printer
		int 10h						; prints
		int 20h						; halts
		end
		