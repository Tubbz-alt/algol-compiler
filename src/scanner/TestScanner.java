package scanner;

import parser.GrammarSymbols;

/**
 * TestScanner class
 * 
 * @version 2016-march-22
 * @course Compiladores
 * @author Pedro H Q Santos
 * @email phqs@ecomp.poli.br
 */
 
public class TestScanner {
	
	public static void main(String[] args) {
		Scanner scan = new Scanner();
		Token a;
		do {
			a = scan.getNextToken();
			System.out.println(a);
		} while (a.getKind()!= GrammarSymbols.EOT);

	}

}
