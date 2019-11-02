package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class FactorIdentifier extends Factor {
	private Identifier id;
	
	public FactorIdentifier(Identifier id) {
		this.id = id;
	}

	public String toString() {
			return "ID[" + this.id + "]";
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitID(this.id,list);
	}

	public Identifier getId() {
		return id;
	}

}
