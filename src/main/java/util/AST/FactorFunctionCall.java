package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class FactorFunctionCall extends Factor {
	private FunctionCall fc;
	
	public FactorFunctionCall(FunctionCall fc) {
		this.fc = fc;
	}

	public String toString() {
		return "FC[" + this.fc + "]";
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitFactorFunctionCall(this,list);
	}

	public FunctionCall getFc() {
		return fc;
	}
}
