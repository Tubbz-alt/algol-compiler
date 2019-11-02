package checker;

import java.util.ArrayList;

import util.AST.AST;
import util.AST.ArgumentList;
import util.AST.ArithmeticExpression;
import util.AST.AssignCommand;
import util.AST.BreakCommand;
import util.AST.ContinueCommand;
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

public interface Visitor {
	public Object visitAL(ArgumentList argList, ArrayList<AST> list) throws SemanticException;

	public Object visitAE(ArithmeticExpression arithExp, ArrayList<AST> list) throws SemanticException;

	public Object visitAsgn(AssignCommand asgn, ArrayList<AST> list) throws SemanticException;

	public Object visitBreak(BreakCommand tBreak, ArrayList<AST> list) throws SemanticException;

	public Object visitContinue(ContinueCommand tContinue, ArrayList<AST> list) throws SemanticException;

	public Object visitExp(Expression exp, ArrayList<AST> list) throws SemanticException;

	public Object visitFactorExpression(FactorExpression factorExp, ArrayList<AST> list) throws SemanticException;

	public Object visitFactorFunctionCall(FactorFunctionCall factorFuncCall, ArrayList<AST> list)
			throws SemanticException;

	public Object visitFactorIdentifier(FactorIdentifier factorId, ArrayList<AST> list) throws SemanticException;

	public Object visitFactorLiteral(FactorLiteral factorLit, ArrayList<AST> list) throws SemanticException;

	public Object visitFuncCall(FunctionCall funcCall, ArrayList<AST> list) throws SemanticException;

	public Object visitFuncDec(FunctionDeclaration funcDec, ArrayList<AST> list) throws SemanticException;

	public Object visitGlobalVarDec(GlobalVariableDeclaration globalVarDec, ArrayList<AST> list)
			throws SemanticException;

	public Object visitID(Identifier id, ArrayList<AST> list) throws SemanticException;

	public Object visitIfCmd(IfCommand cmdIf, ArrayList<AST> list) throws SemanticException;

	public Object visitLocalVarDec(LocalVariableDeclaration localVarDec, ArrayList<AST> list) throws SemanticException;

	public Object visitLogic(Logic logic, ArrayList<AST> list) throws SemanticException;

	public Object visitNumber(Number number, ArrayList<AST> list) throws SemanticException;

	public Object visitOp(Operator op, ArrayList<AST> list) throws SemanticException;

	public Object visitPL(ParameterList parList, ArrayList<AST> list) throws SemanticException;

	public Object visitPrint(PrintCommand print, ArrayList<AST> list) throws SemanticException;

	public Object visitProgram(Program program, ArrayList<AST> list) throws SemanticException;

	public Object visitReturn(ReturnCommand cmdReturn, ArrayList<AST> list) throws SemanticException;

	public Object visitTerm(Term term, ArrayList<AST> list) throws SemanticException;

	public Object visitTypeInt(TypeInt tType, ArrayList<AST> list) throws SemanticException;

	public Object visitTypeBool(TypeBool tType, ArrayList<AST> list) throws SemanticException;

	public Object visitTypeVoid(TypeVoid tType, ArrayList<AST> list) throws SemanticException;

	public Object visitVarDec(VariableDeclaration varDec, ArrayList<AST> list) throws SemanticException;

	public Object visitWhile(WhileCommand whileCmd, ArrayList<AST> list) throws SemanticException;
}
