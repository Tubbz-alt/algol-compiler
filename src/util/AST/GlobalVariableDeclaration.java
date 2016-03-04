package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class GlobalVariableDeclaration extends DeclarationCommand {
	private VariableDeclaration vd;
	
	public GlobalVariableDeclaration(Type type, Identifier id) {
		vd = new VariableDeclaration(type,id);
	}

	public String toString() {
		String str = "GVD[" + vd.toString()+ "]";
		return str;
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitGlobalVarDec(this,list);
	}
	
	public VariableDeclaration getVarDec() {
		return this.vd;
	}
}
