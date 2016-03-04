package compiler;

import checker.Checker;
import encoder.Encoder;
import parser.Parser;
import util.AST.AST;
import util.AST.Program;
import util.symbolsTable.IdentificationTable;

/**
 * Compiler driver
 * @version 2010-september-04
 * @discipline Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 */
public class Compiler {
	
	// Compiler identification table
	public static IdentificationTable identificationTable = null;

	/**
	 * Compiler start point
	 * @param args - none
	 */
	public static void main(String[] args) {
		// Initializes the identification table with the reserved words 
		Compiler.initIdentificationTable();
		
		// Creates the parser object
		Parser p = new Parser();
		
		// Creates the AST object
		AST astRoot = null;
		
		// Parses the source code
		astRoot = p.parse();
		System.out.println("\n-- AST STRUCTURE --");
		if ( astRoot != null ) {
			System.out.println(astRoot.toString());
		}
		
		// Creates the checker object
		Checker checker = new Checker();
		
		// Checks if the code is semantically correct and decorates AST
		AST decoredAST = checker.check((Program) astRoot);
		
		// Creates the encoder object
		Encoder encoder = new Encoder();
		
		// Generates assembly code
		System.out.println("\n-- ASSEMBLY CODE --");
		encoder.encode((Program) decoredAST);
	}
	
	/**
	 * Initializes the identification table with the reserved words
	 */
	private static void initIdentificationTable() {
		// Calls the initializer methods
		Compiler.identificationTable = new IdentificationTable();
	}
	
}
