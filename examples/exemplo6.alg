begin 

proc isBigger = (int a, int b) bool : 
(
	bool result;
	if(a>b)then result:=true; else result:=false; fi	
	ret result;
);

proc main = () void : # Mudar o nome da funcao para testar REGRA 9
(
	int x;
	x:=10;
	int y;
	y:= 7;
	#y := isBigger(x,y); # Descomentar para testar REGRA 8
	print(y);
);

end