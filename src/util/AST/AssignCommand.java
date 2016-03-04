package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class AssignCommand extends Command {
	private Identifier id;
	private Expression exp;

	public AssignCommand(Identifier id, Expression exp) {
		this.id = id;
		this.exp = exp;
	}
	
	public String toString() {
		return "ASG[" + id.toString() + "]EXP[" + exp.toString() + "]";
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitAsgn(this,list);
	}
	
	public Identifier getId() {
		return id;
	}
	
	public Expression getExp() {
		return exp;
	}
}
