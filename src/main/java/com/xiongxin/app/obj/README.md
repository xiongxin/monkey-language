# 3 Evaluation

## Strategies of Evaluation

## A Tree-Walking Interpreter

- a tree-waling evaluator 
- a way to represent Monkey value in our host language Java.


```
function eval(astNode) {
    if (astNode is integerliteral) {
        return astNode.integerValue
    } else if (astNode is booleanLiteral) {
        return astNode.booleanValue
    } else if (astNode is infixExpression) {
        leftEvaluated = eval(astNode.Left)
        rightEvaluated = eval(astNode.Right)
        
        if astNode.Operator =="+" {
            return leftEvaluated + rightEvaluated
        } else if ast.Operator == "-" {
            return leftEvaluated - rightEvaluated
        }
    }
}
```