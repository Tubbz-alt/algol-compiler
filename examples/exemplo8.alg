begin

proc div = (int a, int b) int : (ret a / b;);
int a;
proc main = () int : 
(
a := 4;
#b := 2; # Descomentar para testar REGRA 1

if (a == 3) then
    #int y; # Descomentar para testar REGRA 10
    a:=2*a;
fi
ret div (a, 2);
);

end