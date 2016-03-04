package scanner;

import parser.GrammarSymbols;

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
