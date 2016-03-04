package scanner;

/**
 * Token class
 * @version 2010-september-04
 * @discipline Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 */
public class Token {

	// The token kind
	private int kind;
	// The token spelling
	private String spelling;
	// The line and column that the token was found
	private int line, column;
	
	/**
	 * Default constructor
	 * @param kind
	 * @param spelling
	 * @param line
	 * @param column
	 */
	public Token(int kind, String spelling, int line, int column) {
		this.kind = kind;
		this.spelling = spelling;
		this.line = line;
		this.column = column;
	}

	/**
	 * Returns token kind
	 * @return
	 */
	public int getKind() {
		return kind;
	}

	/**
	 * Returns token spelling
	 * @return
	 */
	public String getSpelling() {
		return spelling;
	}

	/**
	 * Returns the line where the token was found
	 * @return
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Returns the column where the token was found
	 * @return
	 */	
	public int getColumn() {
		return column;
	}
	
	public String toString() {
		String type = null;
		switch (getKind()) {
		case 0:
			type = "EOT";
			break;
		case 1:
			type = "INT";
			break;
		case 2:
			type = "BOOL";
			break;
		case 3:
			type = "TRUE";
			break;
		case 4:
			type = "FALSE";
			break;
		case 5:
			type = "NUM";
			break;
		case 6:
			type = "ID";
			break;
		case 7:
			type = "IF";
			break;
		case 8:
			type = "THEN";
			break;
		case 9:
			type = "ELSE";
			break;
		case 10:
			type = "FI";
			break;
		case 11:
			type = "WHILE";
			break;
		case 12:
			type = "DO";
			break;
		case 13:
			type = "OD";
			break;
		case 14:
			type = "PRINT";
			break;
		case 15:
			type = "BEGIN";
			break;
		case 16:
			type = "END";
			break;
		case 17:
			type = "EQ";
			break;
		case 18:
			type = "NE";
			break;
		case 19:
			type = "GT";
			break;
		case 20:
			type = "GE";
			break;
		case 21:
			type = "LT";
			break;
		case 22:
			type = "LE";
			break;
		case 23:
			type = "ASG";
			break;
		case 24:
			type = "ADD";
			break;
		case 25:
			type = "SUB";
			break;
		case 26:
			type = "MUL";
			break;
		case 27:
			type = "DIV";
			break;
		case 28:
			type = "LP";
			break;
		case 29:
			type = "RP";
			break;
		case 30:
			type = "RET";
			break;
		case 31:
			type = "SC";
			break;
		case 32:
			type = "SEP";
			break;
		case 33:
			type = "PROC";
			break;
		case 34:
			type = "VOID";
			break;
		case 35:
			type = "CO";
			break;
		case 36:
			type = "FUNC_EQ";
			break;
		case 37:
			type = "BREAK";
			break;
		case 38:
			type = "CONTINUE";
			break;
		}
		
		return "Token: " + type + ", " + spelling + " (" + getLine() + ":" + getColumn() + ")";
	}
	
}
