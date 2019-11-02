package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class Identifier extends Terminal {
	private String type = null;
	private AST declaration = null;
	
	public Identifier(String spelling) {
		this.spelling = spelling;
	}

	public String toString() {
		return spelling + "(" + type + ")";
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitID(this,list);
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}	
	
	public void setDeclaration(AST declaration) {
		this.declaration = declaration;
	}
	
	public AST getDeclaration() {
		return declaration;
	}	
}
