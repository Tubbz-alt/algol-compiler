package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class FactorLiteral extends Factor {
	private Literal l;
	
	public FactorLiteral(Literal l) {
		this.l = l;
	}

	public String toString() {
			return "L[" + this.l + "]";
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitFactorLiteral(this,list);
	}

	public Literal getL() {
		return l;
	}
}
