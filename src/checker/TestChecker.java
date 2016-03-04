package checker;

import parser.Parser;

public class TestChecker {
	public static void main(String[] args) {
		Parser parser = new Parser();
		Checker test = new Checker();
		test.check(parser.parseProgram());
	}
}
