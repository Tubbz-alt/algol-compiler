package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class ContinueCommand extends Command {
	public String toString() {
		return "[CONTINUE]";
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitContinue(this,list);
	}
}
