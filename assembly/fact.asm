; Ricky Cheng
; Input - 1000h, Output - 1001h, Error Code - 1002h
; Error Code: 0F - Correct, F0 - Error

		mov al, byte ptr 1000h			; grabs the input from 1000h, variable n
		mov bl, al						; variable: addCounter
										; other variables used are cl: tempCount and dl: tempSum
		cmp al, 0						; checks if there is a zero inputed
		jz zero
		cmp al, 1						; checks if there is only one variable
		jz one							; if it is one then jump and finish program
lp:		cmp al, 0						; while n  != 0
		mov cl, al						; sets tempCount to n 
		sub cl, 1						; subtracts one from the tempCount because it is n * (n-1)
		jz done							; if the counter becomes 0 then the loop is finished
		mov dl, 0						; sets tempSum to 0
lp2:	cmp cl, 0						; while tempCount != 0
		jz fin							; if cl is equal to zero then jump to label: fin
		add dl, bl						; tempSum = tempSum + addCounter
		jc err							; if the sum is greater then 255 then jump to label:err
		sub cl, 1						; tempCount = tempCount - 1
		jmp lp2							; continue this loop, jump to label:lp2
zero:	mov byte ptr 1001h, 00h			; if n was equal to 0!
		mov byte ptr 1001h, 0Fh			; set error code to correct
		int 20h
one:	mov byte ptr 1001h, 01h			; if n was equal to 1!
		mov byte ptr 1002h, 0Fh			; sets the error code to correct
		int 20h
fin:	mov bl, dl						; addCounter is set to tempSum
		sub al, 1						; subtract one from the counter n
		jmp lp							; jump back into the loop
err:	mov byte ptr 1002h, 0F0h		; if there was an error set code F0h
		int 20h
done:	mov byte ptr 1001h, dl			; if it was successful then put the product into byte ptr 1001h
		mov byte ptr 1002h, 0Fh			; set the error code to correct
		int 20h
		end
		