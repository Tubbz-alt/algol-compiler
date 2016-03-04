package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class FunctionDeclaration extends DeclarationCommand {
	private Identifier id;
	private ParameterList pl;
	private Type type;
	private ArrayList<Command> commands;

	public FunctionDeclaration(Identifier id, ParameterList pl, Type type, ArrayList<Command> commands) {
		this.id = id;
		this.pl = pl;
		this.type = type;
		this.commands = commands;
	}
	
	public String toString() {
		String str = "FD[ID[" + id.toString() + "]";
		if (pl!=null) str = "PL[" + pl.toString() + "]"; 
		str += "Type[" + type.toString() + "]";
		str += "CMD[" + commands.toString() + "]]";
		return str;
	}
	
	public Object visit(Visitor v, ArrayList<AST> list) throws SemanticException {
		return v.visitFuncDec(this,list);
	}

	public Identifier getId() {
		return id;
	}

	public ParameterList getPl() {
		return pl;
	}

	public Type getType() {
		return type;
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}
	
	
}
