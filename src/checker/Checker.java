package checker;

import java.util.ArrayList;

import parser.Parser;
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
import util.symbolsTable.IdentificationTable;

/**
 * Checker class
 * 
 * @version 2016-march-22
 * @course Compiladores
 * @author Pedro H Q Santos
 * @email phqs@ecomp.poli.br
 */

public class Checker implements Visitor {

	IdentificationTable idTable = null;
	Parser parser = null;

	public Checker() {
		this.parser = new Parser();
		this.idTable = new IdentificationTable();
	}

	public AST check(Program p) {
		try {
			return (AST) p.visit(this, new ArrayList<AST>());
		} catch (SemanticException e) {
			System.err.println(e);
		}
		return null;
	}

	public Object visitProgram(Program program, ArrayList<AST> list) throws SemanticException {
		for (DeclarationCommand dCmd : program.getDCmd()) {
			dCmd.visit(this, list);
		}
		// Verifica se na tabela de símbolos tem alguma função chamada "main"
		// (REGRA 9)
		if (idTable.retrieve("main") == null || !(idTable.retrieve("main") instanceof FunctionDeclaration))
			throw new SemanticException("A function \"main\" must be declared.");
		return program;
	}

	public Object visitGlobalVarDec(GlobalVariableDeclaration globalVarDec, ArrayList<AST> list)
			throws SemanticException {
		globalVarDec.getVarDec().visit(this, list);
		return null;
	}

	public Object visitFuncDec(FunctionDeclaration funcDec, ArrayList<AST> list) throws SemanticException {
		list.add(funcDec);
		String id = funcDec.getId().getSpelling();

		// Adiciona a função na tabela de símbolos
		idTable.enter(id, funcDec);

		// Abre o escopo da lista de parâmetros
		idTable.openScope();

		funcDec.getId().visit(this, list);

		if (funcDec.getPl() != null) {
			funcDec.getPl().visit(this, list);
		}

		boolean hasWhileReturn = false;
		boolean hasIfReturn = false;
		boolean hasReturn = false;
		String retType = (String) funcDec.getType().visit(this, list);

		// Abre o escopo das variáveis locais
		idTable.openScope();
		for (Command c : funcDec.getCommands()) {
			c.visit(this, list);
			if (c instanceof ReturnCommand) {
				hasReturn = true;
			}
			if (c instanceof WhileCommand) {
				hasWhileReturn = hasWhileReturn || ((Boolean) c.visit(this, list));
			}
			if (c instanceof IfCommand) {
				hasIfReturn = hasIfReturn || ((Boolean) c.visit(this, list));
			}
		}
		hasReturn = hasReturn || hasWhileReturn || hasIfReturn;

		// Se o retorno da função for diferente de void, deve haver pelo menos
		// um comando de retorno (REGRA 4)
		if (hasReturn == false && !retType.equals("void")) {
			throw new SemanticException("At least one ret command should be defined for function \"" + id + "\".");
		}

		idTable.closeScope();
		idTable.closeScope();
		list.remove(funcDec);
		return null;
	}

	public Object visitPL(ParameterList parList, ArrayList<AST> list) throws SemanticException {
		for (VariableDeclaration vd : parList.getVarDecList()) {
			vd.visit(this, list);
		}
		return null;
	}

	public Object visitVarDec(VariableDeclaration varDec, ArrayList<AST> list) throws SemanticException {
		// Adiciona a variável na tabela de símbolos
		idTable.enter(varDec.getId().getSpelling(), varDec);
		varDec.setScope(idTable.getScope());
		varDec.getType().visit(this, list);
		varDec.getId().visit(this, list);
		return null;
	}

	public Object visitLocalVarDec(LocalVariableDeclaration localVarDec, ArrayList<AST> list) throws SemanticException {
		for (AST ast : list) {
			// Verifica se essa variável local foi declarada dentro de um if ou
			// um while (REGRA 10)
			if (ast instanceof IfCommand || ast instanceof WhileCommand) {
				String block = ast instanceof IfCommand ? "if" : "while";
				String id = localVarDec.getVarDec().getId().getSpelling();
				throw new SemanticException(
						"Cannot declare local variable \"" + id + "\" inside of a " + block + " block.");
			}
		}
		localVarDec.getVarDec().visit(this, list);
		return null;
	}

	public Object visitAsgn(AssignCommand asgn, ArrayList<AST> list) throws SemanticException {
		String type = (String) asgn.getId().visit(this, list);
		String id = asgn.getId().getSpelling();

		// Se o id não foi uma declaração de variável (REGRA 8)
		if (!(idTable.retrieve(id) instanceof VariableDeclaration)) {
			throw new SemanticException("\"" + id + "\" must be a variable.");
		}

		// Se o tipo da variável for diferente do tipo da expressão (REGRA 8)
		if (!type.equals(asgn.getExp().visit(this, list))) {
			throw new SemanticException("Assign error: the type of \"" + id + "\" is " + type + ".");
		}

		return null;
	}

	public Object visitPrint(PrintCommand print, ArrayList<AST> list) throws SemanticException {
		print.getExp().visit(this, list);
		return null;
	}

	public Object visitIfCmd(IfCommand cmdIf, ArrayList<AST> list) throws SemanticException {
		list.add(cmdIf);
		cmdIf.getExp().visit(this, list);
		
		String type = (String) cmdIf.getExp().visit(this, list);

		boolean validReturn, hasIfReturn, hasElse, hasElseReturn;
		validReturn = hasIfReturn = hasElse = hasElseReturn = false;
		
		if ((type.equals("bool")) && cmdIf.getExp().getArithExp() == null) {
			if (cmdIf.getExp().getAe().getTerm().getFactor().toString().contains("true")) {
				validReturn = true;
			}
		}

		for (Command c : cmdIf.getCommands()) {
			c.visit(this, list);
			if (c instanceof ReturnCommand) {
				hasIfReturn = true;
			} else if (c instanceof IfCommand || c instanceof WhileCommand) {
				hasIfReturn = hasIfReturn || (Boolean) c.visit(this, list);
			}
		}
		for (Command c : cmdIf.getElseCommands()) {
			hasElse = true;
			c.visit(this, list);
			if (c instanceof ReturnCommand) {
				hasElseReturn = true;
			} else if (c instanceof IfCommand || c instanceof WhileCommand) {
				hasElseReturn = hasElseReturn || (Boolean) c.visit(this, list);
			}
		}
		list.remove(cmdIf);
		
		// Se não houver else, no if deve haver um retorno e sua condição tem que ser sempre atendida.
		// Se houver um else, deve haver um retorno nele e no if
		return (!hasElse && hasIfReturn && validReturn) || (hasElse && hasIfReturn && hasElseReturn); 
	}

	public Object visitWhile(WhileCommand cmdWhile, ArrayList<AST> list) throws SemanticException {
		list.add(cmdWhile);

		String type = (String) cmdWhile.getExp().visit(this, list);
		boolean validReturn, hasWhileReturn;

		validReturn = hasWhileReturn = false;
		if ((type.equals("bool")) && cmdWhile.getExp().getArithExp() == null) {
			if (cmdWhile.getExp().getAe().getTerm().getFactor().toString().contains("true")) {
				validReturn = true;
			}
		}

		for (Command c : cmdWhile.getCommands()) {
			c.visit(this, list);
			if (c instanceof ReturnCommand) {
				hasWhileReturn = true;
			} else if (c instanceof IfCommand || c instanceof WhileCommand) {
				hasWhileReturn = hasWhileReturn || (Boolean) c.visit(this, list);
			}
		}

		list.remove(cmdWhile);

		return hasWhileReturn && validReturn;
	}

	public Object visitBreak(BreakCommand tBreak, ArrayList<AST> list) throws SemanticException {
		// Verifica se esse break foi declarado dentro de um while (REGRA 6)
		for (Object o : list) {
			if (o instanceof WhileCommand) {
				return null;
			}
		}
		throw new SemanticException("Command Break should be inside a loop.");
	}

	public Object visitContinue(ContinueCommand tContinue, ArrayList<AST> list) throws SemanticException {
		// Verifica se esse continue foi declarado dentro de um while (REGRA 6)
		for (Object o : list) {
			if (o instanceof WhileCommand) {
				return null;
			}
		}
		throw new SemanticException("Command Continue should be inside a loop.");
	}

	public Object visitFuncCall(FunctionCall funcCall, ArrayList<AST> list) throws SemanticException {
		list.add(funcCall);
		funcCall.getId().visit(this, list);
		String id = funcCall.getId().getSpelling();

		// Se a função não tiver sido declarada (REGRA 1)
		if (!idTable.containsKey(id)) {
			throw new SemanticException("Function \"" + id + "\" not defined.");
		}

		if (funcCall.getAl() != null) {
			funcCall.getAl().visit(this, list);
		} else {
			FunctionDeclaration funcDec = (FunctionDeclaration) idTable.retrieve(funcCall.getId().getSpelling());
			ArrayList<VariableDeclaration> varDecList = funcDec.getPl().getVarDecList();
			if (varDecList.size() != 0) {
				// O número de argumentos tem que ser igual ao número de parâmetros
				// (REGRA 3)
				throw new SemanticException("Function \"" + id + "\" has wrong argument number.");
			}
		}

		// Recupera a declaração de função da lista de objetos
		String functionId = null;
		FunctionDeclaration funcDec = null;
		for (AST ast : list) {
			if (ast instanceof FunctionCall) {
				functionId = ((FunctionCall) ast).getId().getSpelling();
				break;
			}
		}
		funcDec = (FunctionDeclaration) idTable.retrieve(functionId);

		list.remove(funcCall);

		// Retorna o tipo da chamada de função baseado no tipo declarado na
		// função
		return funcDec.getType().getSpelling();
	}

	public Object visitAL(ArgumentList argList, ArrayList<AST> list) throws SemanticException {
		String functionId = null;
		FunctionDeclaration funcDec = null;

		// Recupera a chamada de função da lista de objetos
		for (AST ast : list) {
			if (ast instanceof FunctionCall) {
				functionId = ((FunctionCall) ast).getId().getSpelling();
				break;
			}
		}
		funcDec = (FunctionDeclaration) idTable.retrieve(functionId);

		ArrayList<VariableDeclaration> varDecList = funcDec.getPl().getVarDecList();
		ArrayList<Expression> expList = argList.getExps();
		String id = funcDec.getId().getSpelling();

		// O número de argumentos tem que ser igual ao número de parâmetros
		// (REGRA 3)
		if (expList.size() != varDecList.size()) {
			throw new SemanticException("Function \"" + id + "\" has wrong argument number.");
		}

		// O tipo dos argumentos tem que ser igual ao tipo dos parâmetros (REGRA
		// 3)
		for (int i = 0; i < expList.size(); i++) {
			if (!varDecList.get(i).getType().getSpelling().equals(expList.get(i).visit(this, list))) {
				throw new SemanticException("Function \"" + id + "\" has wrong argument types.");
			}
		}
		return null;
	}

	public Object visitReturn(ReturnCommand cmdReturn, ArrayList<AST> list) throws SemanticException {
		String functionId = null;
		FunctionDeclaration funcDec = null;

		// Recupera a declaração de função da lista de objetos
		for (AST ast : list) {
			if (ast instanceof FunctionDeclaration) {
				functionId = ((FunctionDeclaration) ast).getId().getSpelling();
				break;
			}
		}
		funcDec = (FunctionDeclaration) idTable.retrieve(functionId);

		String id = (String) funcDec.getId().getSpelling();
		String type = (String) funcDec.getType().getSpelling();
		String exp;

		if (cmdReturn.getExp() != null) {
			exp = (String) cmdReturn.getExp().visit(this, list);
		} else {
			exp = "void";
		}

		// O tipo retornado deve ser igual ao tipo do retorno declarado na
		// função (REGRA 5)
		if (!exp.equals(type)) {
			throw new SemanticException("Function \"" + id + "\" should return a type " + type + ".");
		}
		return exp;
	}

	public Object visitExp(Expression exp, ArrayList<AST> list) throws SemanticException {
		String arithExp1, resultExp;
		arithExp1 = resultExp = (String) exp.getAe().visit(this, list);

		if (exp.getArithExp() != null) {
			String op = (String) exp.getArithExp().getOp().visit(this, list);
			String arithExp2 = (String) exp.getArithExp().getAst().visit(this, list);

			// Se o primeiro operando for diferente do segundo operando (REGRA
			// 7)
			if (!arithExp1.equals(arithExp2)) {
				throw new SemanticException("Cannot operate between different types.");
			}
			// >, <, >= e <= devem ser aplicados a int apenas (REGRA 7)
			if ((op.equals(">") || op.equals("<") || op.equals(">=") || op.equals("<=")) && arithExp1.equals("bool")) {
				throw new SemanticException("Operator not defined for type bool.");
			}
			// Qualquer operação efetuada até este ponto (==, !=, >, <, >= e
			// <=), deve retornar bool (REGRA 7)
			resultExp = "bool";
		}
		// Decora a AST de ID com o tipo
		exp.setType(resultExp);
		return exp.getType();
	}

	public Object visitAE(ArithmeticExpression arithExp, ArrayList<AST> list) throws SemanticException {
		String term1, resultTerm;
		term1 = resultTerm = (String) arithExp.getTerm().visit(this, list);
		if (!arithExp.getTerms().isEmpty()) {
			// Checa se o termo 1 é booleano (REGRA 7)
			if (!term1.equals("int")) {
				throw new SemanticException("Operator not defined for type bool.");
			}
			String term2 = null;
			for (Tuple t : arithExp.getTerms()) {
				t.getOp().visit(this, list);
				term2 = (String) t.getAst().visit(this, list);
				if (!term2.equals("int")) {
					throw new SemanticException("Operator not defined for type bool.");
				}
			}
			// Qualquer operação efetuada até este ponto (+,-), deve retornar
			// int (REGRA 7)
			resultTerm = "int";
		}
		return resultTerm;
	}

	public Object visitTerm(Term term, ArrayList<AST> list) throws SemanticException {
		String factor1, resultFactor;
		factor1 = resultFactor = (String) term.getFactor().visit(this, list);
		if (!term.getFactors().isEmpty()) {
			// Checa se o fator 1 é booleano (REGRA 7)
			if (!factor1.equals("int")) {
				throw new SemanticException("Operator not defined for type bool.");
			}
			String factor2 = null;
			for (Tuple t : term.getFactors()) {
				t.getOp().visit(this, list);
				factor2 = (String) t.getAst().visit(this, list);
				if (!factor2.equals("int")) {
					throw new SemanticException("Operator not defined for type bool.");
				}
			}
			// Qualquer operação efetuada até este ponto (*,/), deve retornar
			// int (REGRA 7)
			resultFactor = "int";
		}
		return resultFactor;
	}

	public Object visitFactorExpression(FactorExpression factor, ArrayList<AST> list) throws SemanticException {
		return factor.getExp().visit(this, list);
	}

	public Object visitFactorIdentifier(FactorIdentifier factor, ArrayList<AST> list) throws SemanticException {
		return factor.getId().visit(this, list);
	}

	public Object visitFactorLiteral(FactorLiteral factor, ArrayList<AST> list) throws SemanticException {
		return factor.getL().visit(this, list);
	}

	public Object visitFactorFunctionCall(FactorFunctionCall factor, ArrayList<AST> list) throws SemanticException {
		return factor.getFc().visit(this, list);
	}

	public Object visitID(Identifier tId, ArrayList<AST> list) throws SemanticException {
		VariableDeclaration varDec = null;
		String id = tId.getSpelling();

		// Se a variável não tiver sido declarada (REGRA 1)
		if (!idTable.containsKey(id)) {
			throw new SemanticException("\"" + id + "\" must be declared.");
		}

		// Recupera a declaração do id na tabela de símbolos
		// Decora a AST de ID com declaração
		AST declaration = idTable.retrieve(tId.getSpelling());
		tId.setDeclaration(declaration);

		// Checa se o id proveio de uma declaração de variável ou de função
		// Decora a AST de ID com o tipo
		if (declaration instanceof VariableDeclaration) {
			varDec = (VariableDeclaration) idTable.retrieve(tId.getSpelling());
			tId.setType(varDec.getType().getSpelling());
		}
		return tId.getType();
	}

	public Object visitOp(Operator tOp, ArrayList<AST> list) throws SemanticException {
		return tOp.getSpelling();
	}

	public Object visitLogic(Logic logic, ArrayList<AST> list) throws SemanticException {
		return "bool";
	}

	public Object visitNumber(Number number, ArrayList<AST> list) throws SemanticException {
		return "int";
	}

	public Object visitTypeInt(TypeInt tInt, ArrayList<AST> list) throws SemanticException {
		return "int";
	}

	public Object visitTypeBool(TypeBool tBool, ArrayList<AST> list) throws SemanticException {
		return "bool";
	}

	public Object visitTypeVoid(TypeVoid tVoid, ArrayList<AST> list) throws SemanticException {
		return "void";
	}
}
