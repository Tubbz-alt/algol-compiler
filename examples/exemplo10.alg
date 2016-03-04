begin

proc greater = (int a, int b) bool : (ret a > b;);

proc main = () void : 
(
#int greater; # Descomentar para testar REGRA 2
#greater := 79; # Descomentar para testar REGRA 8
int a;
int b;
if greater ( a, b) then
	print (a);
else
	print(b);
fi
);

end