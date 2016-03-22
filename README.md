# Algol68-Compiler
Final Project for the course of Compilers at University of Pernambuco / Brazil

#### Systax Analysis (Context-free Grammar)

 `Program` ::= ***begin***(`VariableDeclaration`**;** | `FunctionDeclaration`**;**)**end*
 
`Command` ::= `VariableDeclaration`; </br>
| **if** `Expression` **then** `Command`* (**else** `Command`*)? *fi* </br>
| *while* `Expression` *do* (`Command`) * *od* </br>
| **print** **(**`Expression`**)**; </br>
| **break** **;** | **continue** **;** </br>
| **Identifier** (**(**ArgumentList?**)** | (:= Expression))**;** </br>
| **ret** `Expression`?**;**

`FunctionDeclaration` ::= **proc** **Identifier** **=** **(**`ParameterList`?**)** (**Type** | **void**) **:** **(**`Command`***)** </br>
`Expression` ::= `ArithmeticExpression` (( **==** | **!=** | **>** | **>=** | **<** | **<=** ) `ArithmeticExpression`)? </br>
`ArithmeticExpression` ::= `Term` (( **+** | **-** ) `Term`)* </br>
`Term` ::= `Factor` (( * | **/** ) `Factor`) * </br>
`Factor` ::= **Identifier** ((`ArgumentList`?))? | **Number** | **Logic** | (`ArithmeticExpression`) </br>
`ArgumentList` ::= (`Expression`) **(** **,** `Expression` **)** *  </br>
`ParameterList` ::= (**Type** **Identifier**)(**,** **Type** **Identifier**) * </br>
`VariableDeclaration` ::= **Type** **Identifier**

#### Lexical Analysis (Regular Expressions)

Token -> **Type** | **Logic** | **Number** | **Identifier** | **EOT** </br>
| **print** | **begin** | **end** | **if** | **then** | **else** | **fi** </br>
| **while** | **do** | **od** | **break** | **continue** | **ret** </br>
| **==** | **!= **| **>** | **>=** | **<** | **<=** | **:=** </br>
| **+** | **-** | * | **/** | **(** | **)** | **;** | **,** 

**Type** -> int | bool </br>
**Identifier** -> Letter(Letter|Digit)* </br>
Letter -> [a – zA – Z] </br>
Digit -> [0 – 9] </br>
**Logic** -> true | false </br>
**Number** -> Digit+ </br>

###### Note: </br>
Line comments are defined by the symbol "#".
