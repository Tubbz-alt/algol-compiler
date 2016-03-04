begin

#proc mul = (int a, bool b) int : (ret a * b;); # Descomentar para testar REGRA 1 e REGRA 7
int x;
int y;

proc main = () int : 
(
x := 4;
y := 2; 
ret mul (x, y); #Descomentar a funcao mul para evitar erro da REGRA 1
);

end