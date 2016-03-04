begin

proc main = () int : 
(	
	int n1;
	int n2;
	int cont;
	int contteste;
	int teste;
	teste :=0;
	bool a;

    print(n1);
    print(n2);
	
	a := 1 > 2;
	cont := n1;
	
	while (cont<=n2) do

        if (cont>2) then
		contteste := cont -1;
			while (contteste >= 2) do
	            if (cont/contteste==0) then
	                teste:=0;
	                break;
	            else
	                if(contteste==2) then
	                    teste:=1;
	                    continue;
	                fi
	            fi
			od
			break;
		contteste := contteste - 1;
		fi
	cont := cont + 1;
    od

    print(n1);

    ret 0; # Comentar para testar REGRA 4
);

end