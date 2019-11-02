package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class ArgumentList extends AST {
	private ArrayList<Expression> exps;
	
	public ArgumentList(ArrayList<Expression> exps) {
		this.exps = exps;
	}

	public String toString() {
		String str = "";
		for(int i=0; i<exps.size(); i++) 
			str += "EXP[" + exps.get(i).toString() + "]";
		
		return str;
	}

	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitAL(this,list);
	}
	
	public ArrayList<Expression> getExps() {
		return exps;
	}
}
