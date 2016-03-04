package parser;

/**
 * This class contains codes for each grammar terminal
 * 
 * @version 2010-september-04
 * @discipline Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 */
public class GrammarSymbols {

	// Language terminals (starts from 0)
	public static final int EOT = 0, INT = 1, BOOL = 2, TRUE = 3, FALSE = 4, 
			NUM = 5, ID = 6, IF = 7, THEN = 8, ELSE = 9, FI = 10, WHILE = 11, 
			DO = 12, OD = 13, PRINT = 14, BEGIN = 15, END = 16, EQ = 17, 
			NE = 18, GT = 19, GE = 20, LT = 21, LE = 22, ASG = 23, ADD = 24, 
			SUB = 25, MUL = 26, DIV = 27, LP = 28, RP = 29,
			RET = 30, SC = 31, SEP = 32, PROC = 33, VOID = 34, CO = 35, FUNC_EQ = 36,
			BREAK = 37, CONTINUE = 38;

}
