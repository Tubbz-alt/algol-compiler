package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;
import util.Tuple;

public class Expression extends AST {
	private ArithmeticExpression ae;
	private Tuple arithExp;
	private String type;
	
	public Expression(ArithmeticExpression ae1, Tuple arithExp) {
		this.ae = ae1;
		this.arithExp = arithExp;
	}
	
	public String toString() {
		String str = "AE[" + ae.toString() + "]"; 
		if (arithExp != null)
			str += "TupleAE[" + arithExp.toString() + "]"; 			
		return str;
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitExp(this,list);
	}

	public ArithmeticExpression getAe() {
		return ae;
	}

	public Tuple getArithExp() {
		return arithExp;
	}		
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}	
}
