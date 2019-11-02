package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class PrintCommand extends Command {
	private Expression exp;
	
	public PrintCommand(Expression exp) {
		this.exp = exp;
	}

	public String toString() {
		String str = "PRINT[EXP[" + exp.toString() + "]]";
		return str;
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitPrint(this,list);
	}

	public Expression getExp() {
		return exp;
	}
}
