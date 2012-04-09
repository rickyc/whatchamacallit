; Ricky Cheng
; Sieve of Erasthosenes Prime Number Generator
; input a number, any character entered that is not a valid number will start the prime number generator sequence
; 3/29/07
			call f_pprompt					; prints out the prompt
			call f_sqrt						; calculates the sqrt
			call f_prime					; prime number generator
			call f_pntline					; new line
			call f_amtprimes				; prints out prime numbers
			call f_pntline					; new line
			call f_printpr					; prints prime numbers
numover:	int 20h							; end of program
; function to read input from keyboard
f_pprompt	proc
			mov si, 0						; sets si index to 0
lp:			mov ah, 0EH   					;  the subfuction of the Video Services ROM Interrupt
			mov bh, 0     					;  set the video page
			mov al, prompt[si]				;  where XX is a constant, register, or memory position of the ASCII char
			int 10h      					;  actually writes the character on the screen and moves the cursor one position
			inc si							; increments si
			cmp al, 0						; check for end of prompt
			jne lp							; if not the end keep looping
; part two of the prompt / input function, this will read the characters and put it into an array
			mov si, 0						; sets 0 to si register
lp2:		mov ah, 0						; sets stuff for the reading from keyboard
			int 16h							; reader a single keystroke in ascii
			mov ah, 0EH						; subfunction for video
			cmp al, 48						; checks if it is in range
			jb convert						; if number is less then 0 then convert number
			cmp al, 57						; if number is greater then 9 then conver 
			ja convert						; jump to convert
			int 10h							; prints to screen whatever is in al
			sub al, '0'						; convert ascii to decimal
			mov n_arry[si], al				; adds to a temporary array
			inc si							; increment si
			jmp lp2							; jmp bac to top of loop
; part three, will take the array and convert it to a decimal
convert:	mov bx, 0						; counter for loop
			dec si							; decrease the si loop by 1
			mov ax, 1						; move 1 to ax, ax is the ones, tens, hundreds digit
silp:		cmp si, 0FFFFh					; si doesnt terminate
			je endlp						; end loop if si is equal to FFFFh
			push ax							; bx is the temporary place holder for the ax register
			mov cl, n_arry[si]				; cl is where the values are getting taken out from
			mul cx							; ax = cl * ax, multiples the value w/ the corresponding digit
			mov dx, 10						; 10 is the multiple
			add bx, ax						; adds ax to num
			pop ax							; resets the ax register
			mul dx							; ax = ax * dx ; ax = ax * 10
			dec si							; decrease si
			jmp silp						; jump back to loop
endlp:		mov num, bx
			ret
f_pprompt	endp
; function to calculate squareroot for num
f_sqrt		proc
			mov ax, num      				;load input data
			mov cl, 0                 		;count up square root   
			mov bx, 1                 	  	;hold odd nums 1, 3 , 5
slp:    	cmp ax, bx                  	;can we sub again??
			jb ex							;if below then jump to exit
			sub ax, bx                  	;do the sub!
			add bx, 2                   	;next odd num
			add cl, 1                   	;up the count of odd nums
			jmp slp                     	;check next num
ex:     	mov sqrt, cl       				;count is now the  square root
			ret
f_sqrt		endp
; function to calculate prime numbers
f_prime		proc
			call f_setcount					; calls the function to set the count
			call f_initptbl					; calls the function to default the values in the prime array
			mov cl, 2						; moves 2 into cl because 2 is the start of the primes
primelp1:	cmp cl, sqrt					; compare 2 to sqrt
			ja primeexit					; if it is over exit prime generator function
			call f_getbit					; this call is to check if the bit is already set or not
			cmp ch, 0						; if it is zero then you will have to set 1s to everything of its multiple
			je setprime						; ends loop and goes to primeexit
			inc cl							; increment cl to get the next number
			jmp primelp1					; go back to top of loop
setprime:	call f_setprimes				; this calls the function to actually set the prime numbers
			inc cl							; increment the counter for the first while loop
			jmp primelp1					; go back to loop
primeexit:	ret
f_prime		endp
; this function does repeated subtraction to divide and gets quotient in ah
f_divquot	proc
			push bx							; saves bx value
			push cx							; saves cx value
			push dx							; saves dx value
			sub ax, 2						; subtract 2 from ax, first prime is 2
			mov bx, 0						; counter
subdiv:		cmp ax, 8						; while (ax > 8)
			jb divremext					; {
			sub ax, 8						; ax = ax - 8
			inc bx							; bx++
			jmp subdiv						; }
divremext:	mov ax, bx						; moves the left over number into ah
			pop dx							; restore values dx
			pop cx							; restore values cx
			pop bx							; restore values bx
			ret
f_divquot	endp
; this function does repeated subtraction to divide and gets remainder in ah
f_divrem	proc
			push bx							; saves bx value
			push cx							; saves cx value
			push dx							; saves dx value
			sub ax, 2						; subtract 2 from ax, first prime is 2
subdiv2:	cmp ax, 8						; while (ax > dx)
			jb divremext2					; {
			sub ax, 8						; ax = ax -8
			jmp subdiv2						; }
divremext2:	mov ah, al						; moves the left over number into ah
			pop dx							; restore values dx
			pop cx							; restore values cx
			pop bx							; restore values bx
			ret
f_divrem	endp
; this function divides and gets the position, AX - number to be divded
f_divide	proc
			push bx							; saves bx value
			push cx							; saves cx value
			push dx							; saves dx value
			sub ax, 2						; subtract 2 from ax, first prime is 2
			mov dx, 8						; moves 8 into dx because it will divide into ax
			div dl							; does division, ax = ax / dl
			pop dx							; restore values dx
			pop cx							; restore values cx
			pop bx							; restore values bx
			ret
f_divide	endp
; sets the prime 
f_setprimes	proc
			push ax							; saves ax
			push bx							; saves bx
			push dx							; saves dx
			mov bl, cl						; dl will be the start location
			push cx							; saves cx
			mov ch, bl						; this will tell u how many spacs you have to move
setprimelp:	push ax							; saves ax register
			mov ah, 0						; blanks out ah side
			mov al, ch						; moves ch which tells u how many spaces there are to move
			add bx, ax						; adds ch to bx
			pop ax							; restores ax register			
			cmp bx, num						; checks bx register against number
			ja setprmpop					; if it goes over then exit loop
			mov ax, bx						; sets next number to be divded
			push ax							; saves ax
			call f_divrem					; this will figure out which array location / how many roates are necessary
			mov cl, ah						; amount of rotates needed
			mov dl, 10000000b 				; mask
			ror dl, cl						; does the rotate
			push bx							; keep the bx value
			mov bh, 0						; clears the bh register
			pop ax							; restores ax
			call f_divquot					; calls function to get quotient
			mov bx, ax						; moves the array position into bl
			or primes[bx], dl				; sets the spot to 1
			pop bx							; returns the bx value
			mov cl, 0						; clears off cl, originally the amount to rotate
			jmp setprimelp					; goes back to loop
setprmpop:	pop cx							; restore value cx
			pop dx							; restore value dx
			pop bx							; restore value bx
			pop ax							; restore value ax
f_setprimes	endp
; gets the bit to see if it is set or not, this is for the first loop
f_getbit	proc
			push ax							; stores ax
			mov al, cl						; moves cl into al for division
			sub al, 2						; first number starts as 2
			mov dl, 8						; divide by 8 to get position
			div dl							; ah is remainder, al is the quotient
			mov bl, 10000000b 				; mask
rotater:	cmp ah, 0						; compares remainder to 0
			jz getbitexit					; if no remainder exit because no need to rotate
			ror bl, 1						; rotate by 1
			dec ah							; decrease ah	
			jmp rotater						; jmp back to rotater, i can simplify this by putting ah into cl and calling ror
getbitexit: push bx							; saves bx value
			mov bl, al						;  bl = al, bx will represent the position, for primes[bx]
			mov dl, primes[bx]				;  moves the byte out into dl
			pop bx							; returns the original bx content
			pop ax							; returns the original ax content
			and dl, bl						; ANDs it together to single out the bit
			jnz nxtpnum 					; if not zero then go to nxtpnum
			mov ch, 0						; ch = 0, means it is a zero / even
			ret								; return
nxtpnum:	mov ch, 1						; ch = 1, odd / it is a one
			ret
f_getbit	endp
; function to calculate the amount of registers necessary
f_setcount	proc
			push bx							; stores bx
			mov ax, num						; move the number into the ax register
			push ax							; stores ax
			call f_divquot					; calls function to get quotient
			mov bx, ax						; bx = ax
			pop ax							; restores ax
			call f_divrem					; calls function to get remainder
			cmp ah, 0						; checks if there is a remainder
			je skipadd						; if there isnt skip the next step
			inc bx							; else add one to the count
skipadd:	mov ah, 0						; clear out ah
			mov count, bx					; set count (number of registers used for the prime table)
			pop bx							; restores bx
			ret
f_setcount	endp
; function to init primes table to all zero
f_initptbl	proc
			mov si, 0						; sets si = 0
intptblp:	cmp	si, count					; compare si to count
			ja intpexit						; if it is equal exit
			mov	primes[si], 0				; defaults prime
			inc si							; increment si
			jmp intptblp					; jmp bac to top
intpexit:	ret
f_initptbl 	endp
; function to print a new line
f_pntline	proc
			push ax							; saves ax
			push bx							; saves bx
			mov bh, 0						; video page
			mov ah, 0eh						; video 
			mov al, 10						; return cursor to the front of line
			int 10h							; print ascii
			mov al, 13						; carriage return
			int 10h							; print ascii
			pop bx							; restores bx
			pop ax							; restores ax
			ret
f_pntline	endp
; function to print a specific digit
f_pntdigit 	proc
			push ax							; saves ax
			mov ah, 0eh						; video
			mov al, bl						; moves al = bl, gets ready to print digit
			add al, '0'						; converts it to an ascii digit
			int 10h							; prints out ascii
			pop ax							; restores ax
			ret
f_pntdigit	endp
; function to print out up a 5 digit number, AX is the location of the number
f_printax	proc
			push dx							; saves dx
			push cx							; saves cx
			push bx							; saves bx
			mov cl, 0						; if cl is turned to a 1 then print out zeros
			mov dx, 10000					; move dx, 10000, subtract by 10000
			mov bx, 0						; clears out bx register
			cmp ax, dx						; this compare is check if it is necessary to skip the first loop
			jb thousandx					; if(ax < dx) jump to thousandx, skip print
tenthd:		cmp ax, dx						; while(ax<10000) {
			jb thousand						; if it is lower then break loop
			inc bl 							; print counter
			sub ax, dx						; subtract ax = ax - 10000
			jmp tenthd						; }
thousand:	call f_pntdigit					; prints digit
			mov cl, 1
thousandx:	mov dx, 1000					; jumps to this spot and skips the print
			mov bl, 0						; move bl = 0
			cmp cl, 1
			je pthds
			cmp ax, dx						; this compare is to check if it is necessary to skip the next loop
			jb hundredx						; if(ax < dx) jump to hundredx, skip print
pthds:		cmp ax, dx						; while(ax<1000)
			jb hundred						; { if it is lower jump out of loop
			inc bl							; increment counter
			sub ax, dx						; ax = ax - 1000;
			jmp pthds						; }
hundred:	call f_pntdigit					; prints digit
			mov cl, 1
hundredx:	mov dx, 100						; dx = 100
			mov bl, 0						; clears bl
			cmp cl, 1
			je phds
			cmp ax, dx						; this compare is to check if it is necessary to skip the next loop
			jb tensx						; if(ax<dx) jumps to tensx, skip print
phds:		cmp ax, dx						; compare ax to dx
			jb tens							; if ax < 100 then jump to tens 
			inc bl							; bl = hundreds places
			sub ax, dx						; ax = ax - 100
			jmp phds						; jump back to hundreds loop
tens:		call f_pntdigit					; prints digit
			mov cl, 1
tensx:		mov dx, 10 						; tens digit divide
			cmp ax, dx						; if(ax < dx) check if there is tens digit
			jb onesx						; if there is no tens place then jump to print only ones digit
			div dl							; ax = ax / dx (10)
			mov bl, al						; moves bl = al
			call f_pntdigit					; print tens
			jmp one							; if there is a tens digit skip the next step
onesx:		cmp cl, 1
			jne onskp
			mov bl, 0
			call f_pntdigit
onskp:		div dl							; this line is only called if there isn't a tens digit, it moves ah = al
one:		mov bl, ah						; bl = ah, bl = the item being printed
			call f_pntdigit					; print ones
			pop bx							; restore values bx
			pop cx							; restore values cx
			pop dx							; restore values dx
			ret
f_printax	endp
; calculate amount of primes
f_amtprimes proc
			mov si, 0						; sets si index to 0
amtlp1:		mov ah, 0EH   					;  the subfuction of the Video Services ROM Interrupt
			mov bh, 0     					;  set the video page
			mov al, msg[si]					;  where XX is a constant, register, or memory position of the ASCII char
			int 10h      					;  actually writes the character on the screen and moves the cursor one position
			inc si							; increments si
			cmp al, 0						; check for end of prompt
			jne amtlp1						; if not the end keep looping
; print msg
			mov si, 2						; move si = 2
			mov ax, 0						; clear ax
			mov bx, 0						; clear bx
			mov dl, 1000000b				; mask
amtprimelp: cmp si, num						; while(si < num)
			ja amtprimeet					;{ if si > num exit loop
			mov cl, primes[bx]				; cl = primes[bx]
			and cl, dl						; single out the bit
			jnz amtprimein					; if != 0 skip increment
			inc ax							; if 0 increment
amtprimein:	ror dl, 1						; rotate mask
			jnc amtprimesk					; if it is not going to return to original position then skip inc
			inc bx							; increment bx
amtprimesk:	inc si							; increment si counter
			jmp amtprimelp					; jump back to top
amtprimeet: call f_printax					; print out loop
; print msg2	
			mov si, 0						; si = 0
amtlp2:		mov ah, 0EH   					;  the subfuction of the Video Services ROM Interrupt
			mov bh, 0     					;  set the video page
			mov al, msg2[si]				;  where XX is a constant, register, or memory position of the ASCII char
			int 10h      					;  actually writes the character on the screen and moves the cursor one position
			inc si							; increments si
			cmp al, 0						; check for end of prompt
			jne amtlp2						; if not the end keep looping
; print number
			mov ax, num						; move ax = num
			call f_printax					; print number
			ret
f_amtprimes	endp
; function to print out prime numbers
f_printpr	proc
			mov si, 0						; si = 0
prlp1:		mov ah, 0EH   					;  the subfuction of the Video Services ROM Interrupt
			mov bh, 0     					;  set the video page
			mov al, msg3[si]				;  where XX is a constant, register, or memory position of the ASCII char
			int 10h      					;  actually writes the character on the screen and moves the cursor one position
			inc si							; increments si
			cmp al, 0						; check for end of prompt
			jne prlp1						; if not the end keep looping
			call f_pntline
; primes start now
			mov si, 2						; sets si = 2, first prime number
			mov bx, 0						; moves the array position into bl
			mov dl, 10000000b 				; mask
prttlpp:	cmp si, num						; while(si < num)
			ja prtprexit					; if it goes over the num then exit joop
			mov cl, primes[bx]				; moves the prime[bx] (byte) into cl
			and cl, dl						; sets the spot to 1
			jnz prttnum						; if it is not a zero then jump to prttnum
			mov ax, si						; ax = si
			call f_printax					; print the value in ax
			call f_pntline					; call print line function
prttnum:	inc si							; incremnet si counter
			ror dl, 1						; rotate the mask by one to the right
			jnc lppret						; if there is no jc then skip the incremnet
			inc bx							; if there is a carrier then increment the position in primes array
lppret:		jmp prttlpp						; jump back to the print loop
prtprexit:	ret
f_printpr	endp
; rest of the program
prompt		db 'Input your number:', 0		; var for Input your number
msg			db 'There are', 0				; msg 1
msg2		db ' prime numbers less than or equal to', 0
msg3		db 'They are: ', 0				; msg 3
sqrt		db ?							; var for square root
count		dw ?							; var for count = n-2, number of primes table
num 		dw 0							; the number inputted by the user
n_arry		db 5 DUP(?)						; temporary array for the keyboard input, reserving 5 spots because the number can not be higher then 255*255 = 65025
primes		db ?							; primes table
; ends program
			end
