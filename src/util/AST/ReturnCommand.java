package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class ReturnCommand extends Command{
	private Expression exp;
	
	public ReturnCommand(Expression exp) {
		this.exp = exp;
	}

	public String toString() {
		String str = "RET[EXP[" + exp.toString() + "]]";
		return str;
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitReturn(this,list);
	}
	
	public Expression getExp() {
		return exp;
	}
}
