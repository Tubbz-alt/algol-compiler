package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;
import util.Tuple;

public class Term extends AST {
	
	private Factor f;
	private ArrayList<Tuple> factors;
	
	public Term(Factor f, ArrayList<Tuple> factors) {
		this.f = f;
		this.factors = factors;
	}

	public String toString() {
		String str = "F[" + f.toString() + "]"; 
		for(int i=0; i<factors.size(); i++) 
			str += "TupleF[" + factors.get(i).toString() + "]";	
		return str;
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitTerm(this,list);
	}

	public Factor getFactor() {
		return f;
	}

	public ArrayList<Tuple> getFactors() {
		return factors;
	}
}
