package parser;

/**
 * TestParser class
 * 
 * @version 2016-march-22
 * @course Compiladores
 * @author Pedro H Q Santos
 * @email phqs@ecomp.poli.br
 */
 
public class TestParser {
	public static void main(String[] args) {
		Parser test = new Parser();
		System.out.println(test.parseProgram());
	}
}
