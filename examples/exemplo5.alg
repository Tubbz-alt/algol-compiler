begin

proc sum = (int a, int b) int : (ret a + b;);
proc sub = (int a, int b) int : (ret a - b;);
proc mul = (int a, int b) int : (ret a * b;);
proc div = (int a, int b) int : (ret a / b;);
proc equal = (int a, int b) bool : (ret a == b;);
proc diff = (int a, int b) bool : (ret a != b;);
proc greater = (int a, int b) bool : (ret a > b;);
proc greaterEqual = (int a, int b) bool : (ret a >= b;);
proc lesser = (int a, int b) bool : (ret a < b;);
proc lesserEqual = (int a, int b) bool : (ret a <= b;);

int x;
int t;
bool resposta;

proc main = () void : 
(
#resposta:=10; # Descomentar para testar REGRA 8
x := 4;
if true then
	while (t < x) do 
		x:= div(x,t); 
	od
	# break; # Descomentar para testar REGRA 6
else x:=mul(x,t);
	# continue; # Descomentar para testar REGRA 6
	
fi

resposta := true;

);


end 