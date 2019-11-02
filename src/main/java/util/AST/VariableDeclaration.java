package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class VariableDeclaration extends AST {
	private Type type;
	private Identifier id;
	private int scope;
	
	public VariableDeclaration(Type type, Identifier id) {
		this.type = type;
		this.id = id;
	}

	@Override
	public String toString() {
		String str = "Type[" + type.toString() + "]";
		str += "ID[" + id.toString() + "]";
		return str;
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitVarDec(this,list);
	}
	
	public Type getType() {
		return type;
	}

	public Identifier getId() {
		return id;
	}

	public int getScope() {
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}
}
