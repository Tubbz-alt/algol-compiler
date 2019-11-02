package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class ParameterList extends AST {
	private ArrayList<VariableDeclaration> varDecList;

	public ParameterList(ArrayList<VariableDeclaration> varDecList) {
		this.varDecList = varDecList;
	}

	public String toString() {
		String str = "";
		for(int i=0; i < varDecList.size(); i++) 
			str += "Par[" + varDecList.get(i).toString() + "]";
		return str;
	}
	
	public Object visit (Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitPL(this, list);
	}

	public ArrayList<VariableDeclaration> getVarDecList() {
		return varDecList;
	}
}
