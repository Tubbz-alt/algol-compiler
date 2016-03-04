begin

int operacao;

proc main = () int :
(	
	operacao := 0;
    int n1;
	int n2 ;
	int n;
	n := n2;
    if(n1 + 1) then
        n:=(n1+n2);
		else
			if(operacao == 2) then
				n:=(n1-n2);
				else
					if (operacao == 3) then
						n:=(n1/n2);
						else
							if(operacao == 4) then
								n:=(n1*n2);
							fi
					fi
			fi
    fi
    ret n;  
);

end