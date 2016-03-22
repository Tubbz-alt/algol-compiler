package encoder;

import java.util.ArrayList;
import java.util.Hashtable;

import checker.SemanticException;
import checker.Visitor;
import util.Tuple;
import util.AST.AST;
import util.AST.ArgumentList;
import util.AST.ArithmeticExpression;
import util.AST.AssignCommand;
import util.AST.BreakCommand;
import util.AST.Command;
import util.AST.ContinueCommand;
import util.AST.DeclarationCommand;
import util.AST.Expression;
import util.AST.FactorExpression;
import util.AST.FactorFunctionCall;
import util.AST.FactorIdentifier;
import util.AST.FactorLiteral;
import util.AST.FunctionCall;
import util.AST.FunctionDeclaration;
import util.AST.GlobalVariableDeclaration;
import util.AST.Identifier;
import util.AST.IfCommand;
import util.AST.LocalVariableDeclaration;
import util.AST.Logic;
import util.AST.Number;
import util.AST.Operator;
import util.AST.ParameterList;
import util.AST.PrintCommand;
import util.AST.Program;
import util.AST.ReturnCommand;
import util.AST.Term;
import util.AST.TypeBool;
import util.AST.TypeInt;
import util.AST.TypeVoid;
import util.AST.VariableDeclaration;
import util.AST.WhileCommand;

/**
 * Encoder class
 * 
 * @version 2016-march-22
 * @course Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 * @author Pedro H Q Santos
 * @email phqs@ecomp.poli.br
 */
public class Encoder implements Visitor {

	private ObjectCode objectCode;
	private Hashtable<String, Integer> vars;
	private int localVarPosition;
	private int countIf;
	private int countWhile;
	private int countCmp;

	public Encoder() {
		objectCode = new ObjectCode();
	}

	public void emit(String section, String instruction) {
		objectCode.addInstruction(section, new Instruction(instruction));
	}

	public void emit(String instruction) {
		emit("text", instruction);
	}

	public void encode(Program p) {
		try {
			p.visit(this, new ArrayList<AST>());
			objectCode.generateCode();
		} catch (SemanticException e) {
			System.err.println(e);
		}
	}

	public Object visitProgram(Program program, ArrayList<AST> list) throws SemanticException {
		emit("extern", "extern _printf");
		for (DeclarationCommand dc : program.getDCmd()) {
			dc.visit(this, list);
		}
		emit("data", "intFormat: db \"%d\", 10, 0");
		return null;
	}

	public Object visitGlobalVarDec(GlobalVariableDeclaration globalVarDec, ArrayList<AST> list)
			throws SemanticException {
		emit("data", globalVarDec.getVarDec().getId().getSpelling() + ": dd 0");
		return null;
	}

	public Object visitFuncDec(FunctionDeclaration funcDec, ArrayList<AST> list) throws SemanticException {
		vars = new Hashtable<>();
		localVarPosition = 0;
		countIf = countWhile = 1;
		countCmp = 1;
		if (funcDec.getPl() != null) {
			funcDec.getPl().visit(this, list);
		}
		emit("_" + funcDec.getId().getSpelling() + ":");
		emit("push ebp");
		emit("mov ebp, esp");
		list.add(funcDec);
		for (Command c : funcDec.getCommands()) {
			c.visit(this, list);
		}
		list.remove(funcDec);
		emit("mov esp, ebp");
		emit("pop ebp");
		emit("ret");
		emit("");
		return null;
	}

	public Object visitPL(ParameterList parList, ArrayList<AST> list) throws SemanticException {
		int size = parList.getVarDecList().size();
		for (int i = 0; i < size; i++) {
			vars.put(parList.getVarDecList().get(i).getId().getSpelling(), (size - i + 1) * 4);
		}
		return null;
	}

	public Object visitVarDec(VariableDeclaration varDec, ArrayList<AST> list) throws SemanticException {
		return null;
	}

	public Object visitLocalVarDec(LocalVariableDeclaration localVarDec, ArrayList<AST> list) throws SemanticException {
		vars.put(localVarDec.getVarDec().getId().getSpelling(), localVarPosition -= 4);
		emit("sub esp, 4");
		emit("push dword 0");
		emit("pop dword [ebp" + vars.get(localVarDec.getVarDec().getId().getSpelling()) + "]");
		return null;
	}

	public Object visitAsgn(AssignCommand asgn, ArrayList<AST> list) throws SemanticException {
		asgn.getExp().visit(this, list);
		switch (((VariableDeclaration) asgn.getId().getDeclaration()).getScope()) {
		case 0:
			emit("pop dword [" + asgn.getId().getSpelling() + "]");
			break;
		case 1:
			emit("pop dword [ebp+" + vars.get(asgn.getId().getSpelling()) + "]");
			break;
		default:
			emit("pop dword [ebp" + vars.get(asgn.getId().getSpelling()) + "]");
		}
		return null;
	}

	public Object visitPrint(PrintCommand print, ArrayList<AST> list) throws SemanticException {
		print.getExp().visit(this, list);
		emit("push dword intFormat");
		emit("call _printf");
		emit("add esp, 8");
		return null;
	}

	public Object visitIfCmd(IfCommand cmdIf, ArrayList<AST> list) throws SemanticException {
		String labelEndIf = null;
		String labelElse = null;
		String functionId = "";
		for (AST ast : list) {
			if (ast instanceof FunctionDeclaration) {
				functionId = ((FunctionDeclaration) ast).getId().getSpelling();
				break;
			}
		}
		labelEndIf = functionId + "_if_" + countIf + "_end";
		labelElse = functionId + "_else_" + countIf;
		cmdIf.getExp().visit(this, list);
		emit("push dword 0");
		emit("pop ebx");
		emit("pop eax");
		emit("cmp eax, ebx");
		countIf++;
		if (cmdIf.getElseCommands().size() != 0) {
			emit("je " + labelElse);
			for (Command c : cmdIf.getCommands()) {
				c.visit(this, list);
			}
			emit("jmp " + labelEndIf);
			emit(labelElse + ":");
			for (Command c : cmdIf.getElseCommands()) {
				c.visit(this, list);
			}
		} else {
			emit("je " + labelEndIf);
			for (Command c : cmdIf.getCommands()) {
				c.visit(this, list);
			}
		}
		emit(labelEndIf + ":");
		return null;
	}

	public Object visitWhile(WhileCommand cmdWhile, ArrayList<AST> list) throws SemanticException {
		String labelWhileBegin = null;
		String labelWhileEnd = null;
		for (AST ast : list) {
			if (ast instanceof FunctionDeclaration) {
				labelWhileBegin = ((FunctionDeclaration) ast).getId().getSpelling();
				labelWhileEnd = labelWhileBegin;
				break;
			}
		}
		labelWhileBegin += "_while_" + countWhile + "_begin";
		labelWhileEnd += "_while_" + countWhile + "_end";		
		countWhile++;
		
		emit(labelWhileBegin + ":");
		cmdWhile.getExp().visit(this, list);
		emit("push 0");
		emit("pop ebx");
		emit("pop eax");
		emit("cmp eax, ebx");
		emit("je " + labelWhileEnd);
		list.add(cmdWhile);
		for (Command c : cmdWhile.getCommands()) {
			c.visit(this, list);
		}
		list.remove(cmdWhile);
		emit("jmp " + labelWhileBegin);
		emit(labelWhileEnd + ":");
		return null;
	}

	public Object visitBreak(BreakCommand tBreak, ArrayList<AST> list) throws SemanticException {
		String labelWhileEnd = null;
		int countWhile = 0;
		
		for (AST ast : list) {
			if (ast instanceof FunctionDeclaration) {
				labelWhileEnd = ((FunctionDeclaration) ast).getId().getSpelling();
			}
			if (ast instanceof WhileCommand) {
				countWhile++;
			}
		}
		labelWhileEnd += "_while_" + countWhile + "_end"; 
		emit("jmp " + labelWhileEnd);
		return null;
	}

	public Object visitContinue(ContinueCommand tContinue, ArrayList<AST> list) throws SemanticException {
		String labelWhileBegin = null;
		int countWhile = 0;
		for (AST ast : list) {
			if (ast instanceof FunctionDeclaration) {
				labelWhileBegin = ((FunctionDeclaration) ast).getId().getSpelling();
			}
			if (ast instanceof WhileCommand) {
				countWhile++;
			}
		}
		labelWhileBegin += "_while_" + countWhile + "_begin";
		emit("jmp " + labelWhileBegin);
		return null;
	}

	public Object visitFuncCall(FunctionCall funcCall, ArrayList<AST> list) throws SemanticException {
		funcCall.getAl().visit(this, list);
		emit("call _" + funcCall.getId().getSpelling());
		emit("add esp, " + funcCall.getAl().getExps().size() * 4);
		if (list.get(list.size() - 1) instanceof FactorFunctionCall) {
			emit("push eax");
		}
		return null;
	}

	public Object visitAL(ArgumentList argList, ArrayList<AST> list) throws SemanticException {
		for (Expression exp : argList.getExps()) {
			exp.visit(this, list);
		}
		return null;
	}

	public Object visitReturn(ReturnCommand cmdReturn, ArrayList<AST> list) throws SemanticException {
		if (cmdReturn.getExp() != null) {
			cmdReturn.getExp().visit(this, list);
			emit("pop eax");
		}
		emit("mov esp, ebp");
		emit("pop ebp");
		emit("ret");
		return null;
	}

	public Object visitExp(Expression exp, ArrayList<AST> list) throws SemanticException {
		String functionId = "";
		for (AST ast : list) {
			if (ast instanceof FunctionDeclaration) {
				functionId = ((FunctionDeclaration) ast).getId().getSpelling();
				break;
			}
		}
		
		if (exp.getArithExp() == null) {
			exp.getAe().visit(this, list);
		} else {
			exp.getAe().visit(this, list);
			exp.getArithExp().getAst().visit(this, list);
			emit("pop ebx");
			emit("pop eax");
			emit("cmp eax, ebx");
			switch (exp.getArithExp().getOp().getSpelling()) {
			case "==":
				emit(functionId + "jne _false_cmp_" + countCmp);
				break;
			case "!=":
				emit(functionId + "je _false_cmp_" + countCmp);
				break;
			case "<":
				emit(functionId + "jge _false_cmp_" + countCmp);
				break;
			case "<=":
				emit(functionId + "jg _false_cmp_" + countCmp);
				break;
			case ">":
				emit(functionId + "jle _false_cmp_" + countCmp);
				break;
			case ">=":
				emit(functionId + "jl _false_cmp_" + countCmp);
				break;
			}
			emit("push dword 1");
			emit("jmp" + functionId + "_end_cmp_" + countCmp);
			emit(functionId + "_false_cmp_" + countCmp + ":");
			emit("push dword 0");
			emit(functionId + "_end_cmp_" + countCmp + ":");
			countCmp++;
		}
		return null;
	}

	public Object visitAE(ArithmeticExpression arithExp, ArrayList<AST> list) throws SemanticException {
		if (arithExp.getTerms().isEmpty()) {
			arithExp.getTerm().visit(this, list);
		} else {
			int i = 0;
			for (Tuple t : arithExp.getTerms()) {
				if (i == 0) {
					arithExp.getTerm().visit(this, list);
					i++;
				}
				t.getAst().visit(this, list);
				emit("pop ebx");
				emit("pop eax");
				if (t.getOp().getSpelling().equals("+")) {
					emit("add eax, ebx");
				} else {
					emit("sub eax, ebx");
				}
				emit("push eax");
			}
		}
		return null;
	}

	public Object visitTerm(Term term, ArrayList<AST> list) throws SemanticException {
		if (term.getFactors().isEmpty()) {
			term.getFactor().visit(this, list);
		} else {
			int i = 0;
			for (Tuple t : term.getFactors()) {
				if (i == 0) {
					term.getFactor().visit(this, list);
					i++;
				}
				t.getAst().visit(this, list);
				emit("pop ebx");
				emit("pop eax");
				if (t.getOp().getSpelling().equals("*")) {
					emit("imul eax, ebx");
				} else {
					emit("div eax, ebx");
				}
				emit("push eax");
			}
		}
		return null;
	}

	public Object visitFactorExpression(FactorExpression factor, ArrayList<AST> list) throws SemanticException {
		factor.getExp().visit(this, list);
		return null;
	}

	public Object visitFactorIdentifier(FactorIdentifier factor, ArrayList<AST> list) throws SemanticException {
		return factor.getId().visit(this, list);
	}

	public Object visitFactorLiteral(FactorLiteral factor, ArrayList<AST> list) throws SemanticException {
		return factor.getL().visit(this, list);
	}

	public Object visitFactorFunctionCall(FactorFunctionCall factor, ArrayList<AST> list) throws SemanticException {
		list.add(factor);
		factor.getFc().visit(this, list);
		list.remove(factor);
		return null;
	}

	public Object visitID(Identifier tId, ArrayList<AST> list) throws SemanticException {
		switch (((VariableDeclaration) tId.getDeclaration()).getScope()) {
		case 0:
			emit("push dword " + tId.getSpelling());
			break;
		case 1:
			emit("push dword [ebp+" + vars.get(tId.getSpelling()) + "]");
			break;
		default:
			emit("push dword [ebp" + vars.get(tId.getSpelling()) + "]");
		}
		return tId.getDeclaration().visit(this, list);
	}

	public Object visitOp(Operator tOp, ArrayList<AST> list) throws SemanticException {
		return null;
	}

	public Object visitLogic(Logic logic, ArrayList<AST> list) throws SemanticException {
		emit("push dword " + (logic.getSpelling().equals("true") ? "1" : "0"));
		return null;
	}

	public Object visitNumber(Number number, ArrayList<AST> list) throws SemanticException {
		emit("push dword " + number.getSpelling());
		return null;
	}

	public Object visitTypeInt(TypeInt tInt, ArrayList<AST> list) throws SemanticException {
		return null;
	}

	public Object visitTypeBool(TypeBool tBool, ArrayList<AST> list) throws SemanticException {
		return null;
	}

	public Object visitTypeVoid(TypeVoid tVoid, ArrayList<AST> list) throws SemanticException {
		return null;
	}
}
