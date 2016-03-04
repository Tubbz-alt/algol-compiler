begin

proc fat = (int x) int : 
(
	int fatorial;
	if (x>0) then
		fatorial:= x*fat(x-1);
		else
			fatorial:= 1;
	fi
	ret fatorial;
);
	
proc main= () void : 
(
    int x;

    #print(fat(x,1)); # Descomentar para testar REGRA 3
);

end