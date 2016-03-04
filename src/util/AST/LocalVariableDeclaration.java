package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class LocalVariableDeclaration extends Command {
	private VariableDeclaration vd;
	
	public LocalVariableDeclaration(Type type, Identifier id) {
		vd = new VariableDeclaration(type,id);
	}

	public String toString() {
		String str = "LVD[" + vd.toString()+ "]";
		return str;
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitLocalVarDec(this,list);
	}
	
	public VariableDeclaration getVarDec() {
		return this.vd;
	}
}
