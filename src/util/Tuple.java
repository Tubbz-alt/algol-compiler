package util;

import util.AST.AST;
import util.AST.Operator;

public class Tuple {
		public AST ast;
		public Operator op;
		
		public Tuple (Operator op, AST ast) {
			this.op = op;
			this.ast = ast;
		}

		@Override
		public String toString() {
			String str = "OP[" + op.toString() + "],";
			str += "Ts2[" + ast.toString() + "]"; 			
			return str;
		}
		
		public AST getAst() {
			return this.ast;
		}
		
		public Operator getOp() {
			return this.op;
		}
}
