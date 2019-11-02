package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class BreakCommand extends Command {
	public String toString() {
		return "[BREAK]";
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitBreak(this,list);
	}
}
