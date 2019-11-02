package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class Number extends Literal {

	public Number(String spelling) {
		super(spelling);
	}

	public String toString() {
		return spelling;
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitNumber(this,list);
	}
}
