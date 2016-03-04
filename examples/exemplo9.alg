begin

proc mul = (int a, int b) int : (
	ret a * b; # Descomentar para testar REGRA 4
);

proc main = () void : 
(
int n;
n:= 10;
int fat;
fat:=1;
while (n>0) do
    fat:= mul(fat,n);     #FATORIAL = FATORIAL*N
    n:=n-1;
    print(fat);
od
#ret fat;# Descomentar para testar REGRA 5
);
end