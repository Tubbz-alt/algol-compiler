package checker;

import parser.Parser;

/**
 * TestChecker class
 * 
 * @version 2016-march-22
 * @course Compiladores
 * @author Pedro H Q Santos
 * @email phqs@ecomp.poli.br
 */

public class TestChecker {
	public static void main(String[] args) {
		Parser parser = new Parser();
		Checker test = new Checker();
		test.check(parser.parseProgram());
	}
}
