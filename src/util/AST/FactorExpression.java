package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class FactorExpression extends Factor {
	private Expression exp;
	
	public FactorExpression(Expression exp) {
		this.exp = exp;
	}

	public String toString() {
		return "EXP[" + this.exp + "]";
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitFactorExpression(this,list);
	}

	public Expression getExp() {
		return exp;
	}
}
