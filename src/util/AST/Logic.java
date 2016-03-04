package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class Logic extends Literal {

	public Logic(String spelling) {
		super(spelling);
	}

	public String toString() {
		return spelling;
	}

	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitLogic(this,list);
	}
}
