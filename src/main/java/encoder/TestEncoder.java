package encoder;

import checker.Checker;
import parser.Parser;
import util.AST.AST;
import util.AST.Program;

/**
 * TestEncoder class
 * 
 * @version 2016-march-22
 * @course Compiladores
 * @author Pedro H Q Santos
 * @email phqs@ecomp.poli.br
 */

public class TestEncoder {
	public static void main(String[] args) {
		Parser parser = new Parser();
		AST ast = parser.parseProgram();
		Checker checker = new Checker();
		AST decoredAST = checker.check((Program) ast);
		Encoder encoder = new Encoder();
		encoder.encode((Program) decoredAST);
	}
}
