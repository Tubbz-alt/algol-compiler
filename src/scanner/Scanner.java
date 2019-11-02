package scanner;

import compiler.Properties;
import parser.GrammarSymbols;
import util.File;

/**
 * Scanner class
 * 
 * @version 2016-march-22
 * @course Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 * @author Pedro H Q Santos
 * @email phqs@ecomp.poli.br
 */

public class Scanner {

	// The file object that will be used to read the source code
	private File file;
	// The last char read from the source code
	private char currentChar;
	// The kind of the current token
	private int currentKind;
	// Buffer to append characters read from file
	private StringBuffer currentSpelling;
	// Current line and column in the source file
	private int line, column;

	/**
	 * Default constructor
	 */
	public Scanner() {
		this.file = new File(Properties.sourceCodeLocation);
		this.line = 0;
		this.column = 0;
		this.currentChar = this.file.readChar();
	}
	
	/**
	 * Returns the next token
	 */
	public Token getNextToken() {
 		currentSpelling = new StringBuffer("");
		try {
			while (isSeparator(currentChar)) {
				scanSeparator();
			}
			currentSpelling = new StringBuffer("");
			currentKind = scanToken();
		} catch (LexicalException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		return new Token(currentKind, currentSpelling.toString(), line, column);
	}

	/**
	 * Returns if a character is a separator
	 * 
	 * @param c
	 * @return
	 */

	private boolean isSeparator(char c) {
		if (c == '#' || c == ' ' || c == '\n' || c == '\t') {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Reads (and ignores) a separator
	 * 
	 */
	private void scanSeparator() throws LexicalException {
		if (currentChar == '#') {
			getNextChar();
			while (isGraphic(currentChar)) {
				getNextChar();
			}
			if (currentChar == '\n') {
				getNextChar();
			} else {
				throw new LexicalException("Character not expected.", currentChar, line, column);
			}
		} else {
			this.getNextChar();
		}
	}

	/**
	 * Gets the next char
	 */
	private void getNextChar() {
		// Appends the current char to the string buffer
		this.currentSpelling.append(this.currentChar);
		// Reads the next one
		this.currentChar = this.file.readChar();
		// Increments the line and column
		this.incrementLineColumn();
	}

	/**
	 * Increments line and column
	 */
	private void incrementLineColumn() {
		// If the char read is a '\n', increments the line variable and assigns
		// 0 to the column
		if (this.currentChar == '\n') {
			this.line++;
			this.column = 0;
			// If the char read is not a '\n'
		} else {
			// If it is a '\t', increments the column by 4
			if (this.currentChar == '\t') {
				this.column = this.column + 4;
				// If it is not a '\t', increments the column by 1
			} else {
				this.column++;
			}
		}
	}

	/**
	 * Returns if a char is a digit (between 0 and 9)
	 * 
	 * @param c
	 * @return
	 */
	private boolean isDigit(char c) {
		if (c >= '0' && c <= '9') {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns if a char is a letter (between a and z or between A and Z)
	 * 
	 * @param c
	 * @return
	 */
	private boolean isLetter(char c) {
		if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns if a char is a graphic (any ASCII visible character)
	 * 
	 * @param c
	 * @return
	 */
	private boolean isGraphic(char c) {
		if (c >= ' ' && c <= '~') {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Scans the next token Simulates the DFA that recognizes the language
	 * described by the lexical grammar
	 */
	private int scanToken() throws LexicalException {
		int state = 0;
		while (true) {
			switch (state) {
			case 0:
				if (isDigit(currentChar)) {
					state = 1;
					getNextChar();
				} else if (isLetter(currentChar)) {
					state = 2;
					getNextChar();
				} else if (currentChar == '=') {
					state = 3;
					getNextChar();
				} else if (currentChar == '!') {
					state = 5;
					getNextChar();
				} else if (currentChar == '>') {
					state = 7;
					getNextChar();
				} else if (currentChar == '<') {
					state = 9;
					getNextChar();
				} else if (currentChar == ':') {
					state = 11;
					getNextChar();
				} else if (currentChar == '+') {
					state = 13;
					getNextChar();
				} else if (currentChar == '-') {
					state = 14;
					getNextChar();
				} else if (currentChar == '*') {
					state = 15;
					getNextChar();
				} else if (currentChar == '/') {
					state = 16;
					getNextChar();
				} else if (currentChar == '(') {
					state = 17;
					getNextChar();
				} else if (currentChar == ')') {
					state = 18;
					getNextChar();
				} else if (currentChar == ',') {
					state = 19;
					getNextChar();
				} else if (currentChar == ';') {
					state = 20;
					getNextChar();
				} else if (currentChar == '\000') {
					state = 21;
				} else {
					state = 22;
				}
				break;
			case 1:
				while (isDigit(currentChar)) {
					getNextChar();
				}
				return GrammarSymbols.NUM;
			case 2:
				while (isDigit(currentChar) || isLetter(currentChar)) {
					getNextChar();
				}
				String spelling = currentSpelling.toString();

				if (spelling.equals("false")) {
					return GrammarSymbols.FALSE;
				} else if (spelling.equals("true")) {
					return GrammarSymbols.TRUE;
				} else if (spelling.equals("int")) {
					return GrammarSymbols.INT;
				} else if (spelling.equals("bool")) {
					return GrammarSymbols.BOOL;
				} else if (spelling.equals("if")) {
					return GrammarSymbols.IF;
				} else if (spelling.equals("then")) {
					return GrammarSymbols.THEN;
				} else if (spelling.equals("else")) {
					return GrammarSymbols.ELSE;
				} else if (spelling.equals("fi")) {
					return GrammarSymbols.FI;
				} else if (spelling.equals("while")) {
					return GrammarSymbols.WHILE;
				} else if (spelling.equals("do")) {
					return GrammarSymbols.DO;
				} else if (spelling.equals("od")) {
					return GrammarSymbols.OD;
				} else if (spelling.equals("print")) {
					return GrammarSymbols.PRINT;
				} else if (spelling.equals("begin")) {
					return GrammarSymbols.BEGIN;
				} else if (spelling.equals("end")) {
					return GrammarSymbols.END;
				} else if (spelling.equals("ret")) {
					return GrammarSymbols.RET;
				} else if (spelling.equals("proc")) {
					return GrammarSymbols.PROC;
				} else if (spelling.equals("void")) {
					return GrammarSymbols.VOID;
				} else if (spelling.equals("break")) {
					return GrammarSymbols.BREAK;
				} else if (spelling.equals("continue")) {
					return GrammarSymbols.CONTINUE;
				}
				else {
					return GrammarSymbols.ID;
				}
			case 3:				
				if (currentChar == '=') {
					state = 4;
					getNextChar();
				} else {
					return GrammarSymbols.FUNC_EQ;
				}
				break;
			case 4:
				return GrammarSymbols.EQ;
			case 5:
				if (currentChar == '=') {
					state = 6;
					getNextChar();
				} else {
					state = 22;
				}
				break;
			case 6:
				return GrammarSymbols.NE;
			case 7:
				if (currentChar == '=') {
					state = 8;
					getNextChar();
				} else {
					return GrammarSymbols.GT;
				}
				break;
			case 8:
				return GrammarSymbols.GE;
			case 9:
				if (currentChar == '=') {
					state = 10;
					getNextChar();
				} else {
					return GrammarSymbols.LT;
				}
				break;
			case 10:
				return GrammarSymbols.LE;
			case 11:
				if (currentChar == '=') {
					state = 12;
					getNextChar();
				} else {
					return GrammarSymbols.CO;
				}
				break;
			case 12:
				return GrammarSymbols.ASG;
			case 13:
				return GrammarSymbols.ADD;
			case 14:
				return GrammarSymbols.SUB;
			case 15:
				return GrammarSymbols.MUL;
			case 16:
				return GrammarSymbols.DIV;
			case 17:
				return GrammarSymbols.LP;
			case 18:
				return GrammarSymbols.RP;
			case 19:
				return GrammarSymbols.SEP;
			case 20:
				return GrammarSymbols.SC;
			case 21:
				return GrammarSymbols.EOT;
			case 22:
				throw new LexicalException("Character not expected.", currentChar, line, column);
			}
		}
}}
