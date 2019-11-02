# Algol68 Compiler
Final Project for the course of Compilers at University of Pernambuco / Brazil


#### Syntax Analysis (Context-free Grammar)

`Program` ::= ***begin***(`VariableDeclaration`**;** | `FunctionDeclaration`**;**)**end*
 
`Command` ::= `VariableDeclaration`; </br>
| **if** `Expression` **then** `Command`* (**else** `Command`*)? **fi** </br>
| **while** `Expression` **do** (`Command`) * **od** </br>
| **print** **(**`Expression`**)**; </br>
| **break** **;** | **continue** **;** </br>
| **Identifier** (**(**`ArgumentList`?**)** | (:= `Expression`))**;** </br>
| **ret** `Expression`?**;**

`FunctionDeclaration` ::= **proc** **Identifier** **=** **(**`ParameterList`?**)** (**Type** | **void**) **:** **(**`Command`***)** </br>
`Expression` ::= `ArithmeticExpression` (( **==** | **!=** | **>** | **>=** | **<** | **<=** ) `ArithmeticExpression`) ? </br>
`ArithmeticExpression` ::= `Term` (( **+** | **-** ) `Term`) * </br>
`Term` ::= `Factor` (( * | **/** ) `Factor`) * </br>
`Factor` ::= **Identifier** ((`ArgumentList`?))? | **Number** | **Logic** | (`ArithmeticExpression`) </br>
`ArgumentList` ::= (`Expression`) **(** **,** `Expression` **)** *  </br>
`ParameterList` ::= (**Type** **Identifier**)(**,** **Type** **Identifier**) * </br>
`VariableDeclaration` ::= **Type** **Identifier**



#### Lexical Analysis (Regular Expressions)

Token -> **Type** | **Logic** | **Number** | **Identifier** | **EOT** </br>
| **print** | **begin** | **end** | **if** | **then** | **else** | **fi** </br>
| **while** | **do** | **od** | **break** | **continue** | **ret** </br>
| **==** | **!=** | **>** | **>=** | **<** | **<=** | **:=** </br>
| **+** | **-** | * | **/** |  **(**  |  **)**  | **;** | **,** 

**Type** -> int | bool </br>
**Identifier** -> Letter (Letter | Digit) * </br>
Letter -> [a – zA – Z] </br>
Digit -> [0 – 9] </br>
**Logic** -> true | false </br>
**Number** -> Digit + </br>

###### Note: </br>
Line comments are defined by the symbol "#".

#### Semantic Analysis (Informal rules)

##### Rules implemented in this project:

1. Every identifier (function or variable) must be declared before being used.
2. A variable cannot be redeclared in the same scope.
3. In a function call, the number of arguments must be equals to the number of this function's parameters.
4. If the return of a function is different of null, the function must have a return expression.
5. The type of return of a function must be equals to the type declared at the function.
6. A break or a continue command must be declared inside a loop.
7. Every operator must be applied to operands of the same type. </br>`+`, `-`, `*`, `/` must be applied between `int` and return a `int`. `>`, `<`, `>=` and `<=` must be applied between `int`. </br>`==` and `!=` must be applied between `int` or between `bool`. </br> `>`, `<`, `>=`, `<=`, `==` and `!=` return a `bool`.
8. When assigning a variable `(A := B)`, `A` must be a variable and `B` must be the same type of `B`.
9. It must exist one, and only one, main function named `main`.
10. A variable cannot be declared inside a loop or a selection.

## Project Organization

### Scanner

It is the lexical analysis. This part of the project receives the source code as input and returns tokens as output, based on the Regular Expressions. If anything goes wrong, it throws Lexical Exception.

### Parser

It is the syntactic analysis. It receives the tokens processed in the Scanner and assures it is on the right order and it respect the Language Syntax, based on the Context-free grammar. It has an abstract tree as output. If anything goes wrong, it throws Syntactic Exception.


### Checker

It is the semantic analysis. It receives the abstract tree from the parser and checks if all the rules are respected. It returns a decorated abstract tree as output. If anything goes wrong, it throws Semantic Exception.

### Encoder

It receives the decorated abstract tree as input, and it returns object code as output. In this case, it returns x86 Assembly.

