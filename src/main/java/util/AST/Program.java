package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class Program extends AST {
	private ArrayList<DeclarationCommand> dcmd;
	
	public Program(ArrayList<DeclarationCommand> dcmd) {
		this.dcmd = dcmd;
	}

	public String toString() {
		String str = "P[";
		for(int i=0; i<dcmd.size(); i++)
			str += "DCMD[" + dcmd.get(i).toString() + "]\n";
		
		return str += "]";
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitProgram(this,list);
	}
	
	public ArrayList<DeclarationCommand> getDCmd() {
		return this.dcmd;
	}
}
