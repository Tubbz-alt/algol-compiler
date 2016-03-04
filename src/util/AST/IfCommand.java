package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class IfCommand extends Command {
	private Expression exp;
	private ArrayList<Command> commands;
	private ArrayList<Command> elseCommands;
	
	public IfCommand(Expression exp, ArrayList<Command> commands, ArrayList<Command> elseCommands) {
		this.exp = exp;
		this.commands = commands;
		this.elseCommands = elseCommands;
	}

	public String toString() {
		String str = "IF[EXP[" + exp.toString() + "]";
		
		for(int i=0; i<commands.size(); i++)
			str += "CMD[" + commands.get(i).toString() + "]";
		
		if (elseCommands.size() > 0) {		
			for(int i=0; i<elseCommands.size(); i++)
				str += "ElseCMD[" + elseCommands.get(i).toString() + "]";		
		}
		str += "]";
		return str;
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitIfCmd(this,list);
	}

	public Expression getExp() {
		return exp;
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}

	public ArrayList<Command> getElseCommands() {
		return elseCommands;
	}
}
