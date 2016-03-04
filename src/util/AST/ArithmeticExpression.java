package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;
import util.Tuple;

public class ArithmeticExpression extends AST {	
	private Term t;
	private ArrayList<Tuple> terms;
	
	public ArithmeticExpression(Term t, ArrayList<Tuple> terms) {
		this.t = t;
		this.terms = terms;
	}
	
	public String toString() {
		String str = "Term[" + t.toString() + "]"; 
		for(int i=0; i<terms.size(); i++) 
			str += "TupleTerm[" + terms.get(i).toString() + "]";
		return str;
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitAE(this,list);
	}
	
	public Term getTerm() {
		return t;
	}
	
	public ArrayList<Tuple> getTerms() {
		return terms;
	}
}
