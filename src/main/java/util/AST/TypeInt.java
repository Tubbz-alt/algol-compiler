package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class TypeInt extends Type {
	public TypeInt(String spelling) {
		super(spelling);
	}

	public String toString() {
		return spelling;
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitTypeInt(this,list);
	}

}
