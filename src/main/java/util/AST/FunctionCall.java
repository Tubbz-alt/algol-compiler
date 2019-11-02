package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class FunctionCall extends Command {
	private Identifier id;
	private ArgumentList al;
	

	public FunctionCall(Identifier id, ArgumentList al) {
		this.id = id;
		this.al = al;
	}

	public String toString() {
		String str = "ID[" + id.toString() + "]";
		if (al!=null) str = "AL[" + al.toString() + "]";
		return str;
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitFuncCall(this,list);
	}

	public Identifier getId() {
		return id;
	}

	public ArgumentList getAl() {
		return al;
	}
}
