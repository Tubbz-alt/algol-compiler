package encoder;

import checker.Checker;
import parser.Parser;
import util.AST.AST;
import util.AST.Program;

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