package parser;

import java.util.ArrayList;

import scanner.Scanner;
import scanner.Token;
import util.Tuple;
import util.AST.AST;
import util.AST.ArgumentList;
import util.AST.ArithmeticExpression;
import util.AST.AssignCommand;
import util.AST.BreakCommand;
import util.AST.Command;
import util.AST.ContinueCommand;
import util.AST.DeclarationCommand;
import util.AST.Expression;
import util.AST.Factor;
import util.AST.FactorExpression;
import util.AST.FactorFunctionCall;
import util.AST.FactorIdentifier;
import util.AST.FactorLiteral;
import util.AST.FunctionCall;
import util.AST.FunctionDeclaration;
import util.AST.GlobalVariableDeclaration;
import util.AST.Identifier;
import util.AST.IfCommand;
import util.AST.LocalVariableDeclaration;
import util.AST.Logic;
import util.AST.Number;
import util.AST.Operator;
import util.AST.ParameterList;
import util.AST.PrintCommand;
import util.AST.Program;
import util.AST.ReturnCommand;
import util.AST.Term;
import util.AST.Type;
import util.AST.TypeBool;
import util.AST.TypeInt;
import util.AST.TypeVoid;
import util.AST.VariableDeclaration;
import util.AST.WhileCommand;

/**
 * Parser class
 * 
 * @version 2010-august-29
 * @discipline Projeto de Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 */
public class Parser {

	// The current token
	private Token currentToken = null;
	// The scanner
	private Scanner scanner = null;

	/**
	 * Parser constructor
	 */
	public Parser() {
		// Initializes the scanner object
		this.scanner = new Scanner();
		this.currentToken = scanner.getNextToken();
	}

	/**
	 * Verifies if the current token kind is the expected one
	 */
	private void accept(int kind) throws SyntacticException {
		if (currentToken.getKind() == kind) {
			acceptIt();
		} else {
			throw new SyntacticException("Token not Expected.", currentToken);
		}
	}

	/**
	 * Gets next token
	 */
	private void acceptIt() {
		//System.out.println(currentToken);
		currentToken = scanner.getNextToken();
	}

	/**
	 * Verifies if the source program is syntactically correct
	 * 
	 * @throws SyntacticException
	 */
	public AST parse() {
		Program p = null;
		try {
			p = parseProgram();
			accept(GrammarSymbols.EOT);
		} catch (SyntacticException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		return p;
	}

	public Program parseProgram() {
		Program p = null;
		try {
			accept(GrammarSymbols.BEGIN);
			ArrayList<DeclarationCommand> declarations = new ArrayList<DeclarationCommand>();
			DeclarationCommand dcmd;
			while (currentToken.getKind() != GrammarSymbols.END) {
				if (currentToken.getKind() == GrammarSymbols.BOOL || currentToken.getKind() == GrammarSymbols.INT) {
					dcmd = parseGlobalVariableDeclaration();
					accept(GrammarSymbols.SC);
				} else {
					dcmd = parseFunctionDeclaration();
					accept(GrammarSymbols.SC);
				}
				declarations.add(dcmd);
			}
			p = new Program(declarations);
			accept(GrammarSymbols.END);
		} catch (SyntacticException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		return p;
	}

	public Command parseCommand() {
		Command cmd = null;
		try {
			if (currentToken.getKind() == GrammarSymbols.BOOL || currentToken.getKind() == GrammarSymbols.INT) {
				cmd = parseLocalVariableDeclaration();
				accept(GrammarSymbols.SC);
			} else if (currentToken.getKind() == GrammarSymbols.IF) {
				acceptIt();
				Expression exp = parseExpression();
				accept(GrammarSymbols.THEN);
				ArrayList<Command> commands = new ArrayList<Command>();
				ArrayList<Command> elseCommands = new ArrayList<Command>();
				while (!(currentToken.getKind() == GrammarSymbols.ELSE
						|| currentToken.getKind() == GrammarSymbols.FI)) {
					Command command = parseCommand();
					commands.add(command);
				}
				if (currentToken.getKind() == GrammarSymbols.FI) {
					acceptIt();
				} else {
					accept(GrammarSymbols.ELSE);
					while (currentToken.getKind() != GrammarSymbols.FI) {
						Command command = parseCommand();
						elseCommands.add(command);
					}
					acceptIt();
				}
				cmd = new IfCommand(exp, commands, elseCommands);
			} else if (currentToken.getKind() == GrammarSymbols.WHILE) {
				acceptIt();
				Expression exp = parseExpression();
				accept(GrammarSymbols.DO);
				ArrayList<Command> commands = new ArrayList<Command>();
				while (currentToken.getKind() != GrammarSymbols.OD) {
					Command command = parseCommand();
					commands.add(command);
				}
				acceptIt();
				cmd = new WhileCommand(exp, commands);
			} else if (currentToken.getKind() == GrammarSymbols.PRINT) {
				acceptIt();
				accept(GrammarSymbols.LP);
				Expression exp = parseExpression();
				accept(GrammarSymbols.RP);
				accept(GrammarSymbols.SC);
				cmd = new PrintCommand(exp);
			} else if (currentToken.getKind() == GrammarSymbols.BREAK) {
				acceptIt();
				accept(GrammarSymbols.SC);
				cmd = new BreakCommand();
			} else if (currentToken.getKind() == GrammarSymbols.CONTINUE) {
				acceptIt();
				accept(GrammarSymbols.SC);
				cmd = new ContinueCommand();
			} else if (currentToken.getKind() == GrammarSymbols.ID) {
				Identifier id = new Identifier(currentToken.getSpelling());
				acceptIt();
				ArgumentList al = null;
				if (currentToken.getKind() == GrammarSymbols.LP) {
					acceptIt();
					if (currentToken.getKind() == GrammarSymbols.RP) {
						acceptIt();
					} else {
						al = parseArgumentList();
						accept(GrammarSymbols.RP);
					}
					cmd = new FunctionCall(id, al);
				} else {
					accept(GrammarSymbols.ASG);
					Expression exp = parseExpression();
					cmd = new AssignCommand(id, exp);
				}
				accept(GrammarSymbols.SC);
			} else {
				accept(GrammarSymbols.RET);
				Expression exp = null;
				if (currentToken.getKind() == GrammarSymbols.SC) {
					acceptIt();
				} else {
					exp = parseExpression();
					accept(GrammarSymbols.SC);
				}
				cmd = new ReturnCommand(exp);
			}
		} catch (SyntacticException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		return cmd;
	}

	public FunctionDeclaration parseFunctionDeclaration() {
		FunctionDeclaration fd = null; 
		try {
			ParameterList pl = null;
			Type type = null;
			
			accept(GrammarSymbols.PROC);
			Identifier id = new Identifier(currentToken.getSpelling());
			accept(GrammarSymbols.ID);
			accept(GrammarSymbols.FUNC_EQ);
			accept(GrammarSymbols.LP);
			if (currentToken.getKind() == GrammarSymbols.RP) {
				acceptIt();
			} else {
				pl = parseParameterList();
				accept(GrammarSymbols.RP);
			}
			if (currentToken.getKind() == GrammarSymbols.BOOL 
					|| currentToken.getKind() == GrammarSymbols.INT
					|| currentToken.getKind() == GrammarSymbols.VOID) {
				if (currentToken.getKind() == GrammarSymbols.BOOL) {
					type = new TypeBool(currentToken.getSpelling());
				} else if (currentToken.getKind() == GrammarSymbols.INT) {
					type = new TypeInt(currentToken.getSpelling());
				} else if (currentToken.getKind() == GrammarSymbols.VOID) {
					type = new TypeVoid(currentToken.getSpelling());
				}
				acceptIt();
			} 
			accept(GrammarSymbols.CO);
			accept(GrammarSymbols.LP);
			ArrayList<Command> commands = new ArrayList<Command>();
			while (currentToken.getKind() != GrammarSymbols.RP) {
				Command command = parseCommand();
				commands.add(command);
			}
			acceptIt();
			fd = new FunctionDeclaration(id, pl, type, commands);
		} catch (SyntacticException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		
		return fd;
	}

	public Expression parseExpression() {
		Expression exp = null;
		ArithmeticExpression ae1 = parseArithmeticExpression();
		Tuple aet = null;
		if (currentToken.getKind() == GrammarSymbols.EQ || currentToken.getKind() == GrammarSymbols.NE
				|| currentToken.getKind() == GrammarSymbols.GT || currentToken.getKind() == GrammarSymbols.GE
				|| currentToken.getKind() == GrammarSymbols.LT || currentToken.getKind() == GrammarSymbols.LE) {
			Operator op = new Operator(currentToken.getSpelling());
			acceptIt();
			ArithmeticExpression a2 = parseArithmeticExpression();
			aet = new Tuple(op,a2);
		}
		exp = new Expression(ae1,aet);
		return exp;
	}

	public ArithmeticExpression parseArithmeticExpression() {
		ArithmeticExpression ae = null;
		Term t1 = parseTerm();
		ArrayList<Tuple> tuples = new ArrayList<Tuple>();
		while (currentToken.getKind() == GrammarSymbols.ADD || currentToken.getKind() == GrammarSymbols.SUB) {
			Operator op = new Operator(currentToken.getSpelling());
			acceptIt();
			Term tn = parseTerm();
			Tuple tt = new Tuple(op,tn);
			tuples.add(tt);	
		}
		ae = new ArithmeticExpression(t1,tuples);
		return ae;
	}

	public Term parseTerm() {
		Term t = null;
		Factor f1 = parseFactor();
		ArrayList<Tuple> tuples = new ArrayList<Tuple>();
		while (currentToken.getKind() == GrammarSymbols.MUL || currentToken.getKind() == GrammarSymbols.DIV) {
			Operator op = new Operator(currentToken.getSpelling());
			acceptIt();
			AST fn = parseFactor();
			Tuple ft = new Tuple(op,fn);
			tuples.add(ft);	
		}
		t = new Term(f1,tuples);
		return t;
	}

	public Factor parseFactor() {
		Factor f = null;
		try {
			if (currentToken.getKind() == GrammarSymbols.ID) {
				Identifier id = new Identifier(currentToken.getSpelling());
				acceptIt();
				f = new FactorIdentifier(id);
				if (currentToken.getKind() == GrammarSymbols.LP) {
					ArgumentList al = null;
					FunctionCall fc = null;
					acceptIt();
					if (currentToken.getKind() == GrammarSymbols.RP) {
						acceptIt();
					} else {
						al = parseArgumentList();
						accept(GrammarSymbols.RP);
					}
					fc = new FunctionCall(id, al);
					f = new FactorFunctionCall(fc);
				}
			} else if (currentToken.getKind() == GrammarSymbols.NUM) {
				Number n = new Number(currentToken.getSpelling());
				acceptIt();
				f = new FactorLiteral(n);
			} else
				if (currentToken.getKind() == GrammarSymbols.TRUE || currentToken.getKind() == GrammarSymbols.FALSE) {
					Logic l = new Logic(currentToken.getSpelling());
					acceptIt();
					f = new FactorLiteral(l);
			} else {
				accept(GrammarSymbols.LP);
				Expression exp = parseExpression();
				accept(GrammarSymbols.RP);
				f = new FactorExpression(exp);
			}
		} catch (SyntacticException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		return f;
	}

	public ArgumentList parseArgumentList() {
		ArgumentList al = null;
		try {
			ArrayList<Expression> exps = new ArrayList<Expression>();
			Expression exp = parseExpression();
			exps.add(exp);
			while (currentToken.getKind() != GrammarSymbols.RP) {
				accept(GrammarSymbols.SEP);
				exp = parseExpression();
				exps.add(exp);
			}
			al = new ArgumentList(exps);
		} catch (SyntacticException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		return al;
	}

	public ParameterList parseParameterList() {
		ParameterList pl = null;
		try {
			ArrayList<VariableDeclaration> varDeclarations = new ArrayList<VariableDeclaration>();
			VariableDeclaration vd = parseVariableDeclaration();
			varDeclarations.add(vd);
			while (currentToken.getKind() != GrammarSymbols.RP) {
				accept(GrammarSymbols.SEP);
				vd = parseVariableDeclaration();
				varDeclarations.add(vd);
			}
			pl = new ParameterList(varDeclarations);
		} catch (SyntacticException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		return pl;
	}

	private VariableDeclaration parseVariableDeclaration() {
		VariableDeclaration lvd = null;
		try {
			if (currentToken.getKind() == GrammarSymbols.BOOL || currentToken.getKind() == GrammarSymbols.INT) {
				Type type = null;
				if (currentToken.getKind() == GrammarSymbols.BOOL) {
					type = new TypeBool(currentToken.getSpelling());
				} else if (currentToken.getKind() == GrammarSymbols.INT) {
					type = new TypeInt(currentToken.getSpelling());
				}
				acceptIt();
				Identifier id = new Identifier(currentToken.getSpelling());
				accept(GrammarSymbols.ID);
				lvd = new VariableDeclaration(type, id);
			}
		} catch (SyntacticException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		return lvd;
	}

	public LocalVariableDeclaration parseLocalVariableDeclaration() {
		LocalVariableDeclaration lvd = null;
		try {
			if (currentToken.getKind() == GrammarSymbols.BOOL || currentToken.getKind() == GrammarSymbols.INT) {
				Type type = null;
				if (currentToken.getKind() == GrammarSymbols.BOOL) {
					type = new TypeBool(currentToken.getSpelling());
				} else if (currentToken.getKind() == GrammarSymbols.INT) {
					type = new TypeInt(currentToken.getSpelling());
				}
				acceptIt();
				Identifier id = new Identifier(currentToken.getSpelling());
				accept(GrammarSymbols.ID);
				lvd = new LocalVariableDeclaration(type, id);
			}
		} catch (SyntacticException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		return lvd;
	}

	public GlobalVariableDeclaration parseGlobalVariableDeclaration() {
		GlobalVariableDeclaration gvd = null;
		try {
			if (currentToken.getKind() == GrammarSymbols.BOOL || currentToken.getKind() == GrammarSymbols.INT) {
				Type type = null;
				if (currentToken.getKind() == GrammarSymbols.BOOL) {
					type = new TypeBool(currentToken.getSpelling());
				} else if (currentToken.getKind() == GrammarSymbols.INT) {
					type = new TypeInt(currentToken.getSpelling());
				}
				acceptIt();
				Identifier id = new Identifier(currentToken.getSpelling());
				accept(GrammarSymbols.ID);
				gvd = new GlobalVariableDeclaration(type, id);
			}
		} catch (SyntacticException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		return gvd;
	}
}
