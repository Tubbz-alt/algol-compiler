package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class Operator extends Terminal {
	public Operator(String spelling) {
		this.spelling = spelling;
	}

	public String toString() {
		return spelling;
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitOp(this,list);
	}
}
