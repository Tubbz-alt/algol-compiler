package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class WhileCommand extends Command {
	private Expression exp;
	private ArrayList<Command> commands;
	
	public WhileCommand(Expression exp, ArrayList<Command> commands) {
		this.exp = exp;
		this.commands = commands;
	}
	
	public String toString() {
		String str = "WHILE[EXP[" + exp.toString() + "]CMD[" + commands.toString() + "]]";
		return str;
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitWhile(this, list);
	}

	public Expression getExp() {
		return exp;
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}
}
