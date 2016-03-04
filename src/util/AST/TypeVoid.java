package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class TypeVoid extends Type {

	public TypeVoid(String spelling) {
		super(spelling);
	}

	public String toString() {
		return spelling;
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitTypeVoid(this,list);
	}
}
