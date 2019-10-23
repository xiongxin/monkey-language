package com.xiongxin.app.evaluator;

import com.xiongxin.app.ast.*;
import com.xiongxin.app.obj.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 实现方法
 *  递归解析表达式
 */
public class Eval {

    public Obj eval(Node node, Environment environment) {
        Objects.requireNonNull(node, "node must not null");

        // 动态触发，运行时才能知道具体的类，调用特定的方法
        if (node instanceof Program) {
            return evalProgram((Program) node, environment);
        }

        if (node instanceof ExpressionStatement) {
            return eval(((ExpressionStatement) node).expression, environment);
        }

        if (node instanceof IntegerLiteral) {
            return new IntObj(((IntegerLiteral) node).value);
        }

        if (node instanceof BooleanExpression) {
            Boolean value = ((BooleanExpression) node).value;
            if (value)
                return BoolObj.TRUE;
            return BoolObj.FALSE;
        }

        if (node instanceof PrefixExpression) {
            Obj obj = eval(((PrefixExpression) node).right, environment);
            if (isError(obj)) return obj;
            return evalPrefixExpression( ((PrefixExpression) node).operator, obj);
        }

        if (node instanceof InfixExpression) {
            Obj left = eval(((InfixExpression) node).left, environment);
            if (isError(left)) return left;

            Obj right = eval(((InfixExpression) node).right, environment);
            if (isError(right)) return right;

            return evalInfixExpression(((InfixExpression) node).operator, left, right);
        }

        if (node instanceof IfExpression) {
            return evalIfExpression((IfExpression) node, environment);
        }

        if (node instanceof BlockStatement) {
            return evalBlockStatement((BlockStatement) node, environment);
        }

        if (node instanceof ReturnStatement) {
            Obj obj = eval(((ReturnStatement) node).returnValue, environment);
            if (isError(obj)) return obj;
            return new ReturnObj(obj);
        }

        if (node instanceof LetStatement) {
            Obj obj = eval(((LetStatement) node).value, environment);
            if (isError(obj)) return obj;

            environment.set(((LetStatement) node).name.value, obj);
        }

        if (node instanceof Identifier) {
            return evalIdentifier(((Identifier) node), environment);
        }

        if (node instanceof FunctionLiteral) {
            FunctionLiteral functionLiteral = (FunctionLiteral) node;
            return new FunObj(functionLiteral.parameters, functionLiteral.body, environment);
        }

        if (node instanceof CallExpression) {
            Obj obj = eval(((CallExpression) node).function, environment);
            if (isError(obj)) return obj;


        }

        return null;
    }

    private List<Obj> evalExpressions(List<Expression> expressions, Environment environment) {
        List<Obj> objs = new LinkedList<>();


    }

    private Obj evalProgram(Program program, Environment environment) {
        Obj obj = null;

        for (Statement statement : program.statements) {
            obj = eval(statement, environment);

            if (obj instanceof ReturnObj) {
                // 如果在Program的statements中存在return，立即返回ReturnObj的值
                // blockStatement中的return语句会在这里被处理
                return ((ReturnObj) obj).value;
            }

            if (obj instanceof ErrObj) {
                return obj;
            }
        }

        return obj;
    }

    private Obj evalBlockStatement(BlockStatement blockStatement, Environment environment) {
        Obj obj = null;
        for (Statement statement : blockStatement.statements) {
            obj = eval(statement, environment);

            // BlockStatement中碰到Return 语句时，立即返回 ReturnObj
            // 解析出错时也立即返回
            if (obj.type().equals(Obj.RETURN_VALUE_OBK) || obj.type().equals(Obj.ERROR_OBJ)) {
                return obj;
            }
        }

        return obj;
    }

    private Obj evalPrefixExpression(String operator, Obj right) {
        switch (operator) {
            case "!":
                return evalBangOperatorExpression(right);
            case "-":
                return evalMinusPrefixExpression(right);
            default:
                return new ErrObj(String.format("unknown operator: %s%s", operator, right.type()));
        }
    }

    private Obj evalBangOperatorExpression(Obj right) {
        if (right == BoolObj.TRUE) {
            return BoolObj.FALSE;
        }

        if (right == BoolObj.FALSE) {
            return BoolObj.TRUE;
        }

        if (right == NullObj.NULL) {
            return BoolObj.TRUE;
        }

        return BoolObj.FALSE;
    }

    private Obj evalMinusPrefixExpression(Obj right) {
        if (right instanceof IntObj) {
            return new IntObj(-((IntObj) right).value);
        }

        return NullObj.NULL;
    }

    private Obj evalInfixExpression(String operator, Obj left, Obj right) {
        if (!left.type().equals(right.type())) {
            return new ErrObj(String.format("unknown operator: %s %s %s",left.type(), operator, right.type()));
        }

        if (left instanceof IntObj && right instanceof IntObj) {
            return evalIntegerInfixExpression(operator, (IntObj) left, (IntObj) right);
        }

        if (left instanceof BoolObj && right instanceof BoolObj) {
            switch (operator) {
                case "==":
                    return nativeBoolToBooleanObject(((BoolObj) left).value == ((BoolObj) right).value);
                case "!=":
                    return nativeBoolToBooleanObject(((BoolObj) left).value != ((BoolObj) right).value);
                default:
                    return new ErrObj(String.format("unknown operator: %s %s %s",left.type(), operator, right.type()));
            }
        }

        return new ErrObj(String.format("unknown operator: %s %s %s",left.type(), operator, right.type()));
    }


    private Obj evalIntegerInfixExpression(String operator, IntObj left, IntObj right) {
        int lvalue = left.value;
        int rvalue = right.value;

        switch (operator) {
            case "+":
                return new IntObj(lvalue + rvalue);
            case "-":
                return new IntObj(lvalue - rvalue);
            case "*":
                return new IntObj(lvalue * rvalue);
            case "/":
                return new IntObj(lvalue / rvalue);
            case "<":
                return nativeBoolToBooleanObject(lvalue < rvalue);
            case ">":
                return nativeBoolToBooleanObject(lvalue > rvalue);
            case "==":
                return nativeBoolToBooleanObject(lvalue == rvalue);
            case "!=":
                return nativeBoolToBooleanObject(lvalue != rvalue);
            default:
                return new ErrObj(String.format("unknown operator: %s %s %s",left.type(), operator, right.type()));
        }
    }

    private BoolObj nativeBoolToBooleanObject(boolean bool) {
        if (bool)
            return BoolObj.TRUE;
        else
            return BoolObj.FALSE;
    }


    private Obj evalIfExpression(IfExpression ifExpression, Environment environment) {
        Obj obj = eval(ifExpression.condition, environment);

        if (isError(obj)) return obj;

        if (isTruthy(obj)) {
            return eval(ifExpression.consequence, environment);
        } else if (ifExpression.alternative != null) {
            return eval(ifExpression.alternative, environment);
        } else {
            return null;
        }
    }


    private boolean isTruthy(Obj obj) {
        if (obj == NullObj.NULL) {
            return false;
        } else if (obj == BoolObj.FALSE) {
            return false;
        } else if (obj == BoolObj.TRUE) {
            return true;
        }

        return true;
    }

    private boolean isError(Obj obj) {
        if (obj != null) {
            return obj.type().equals(Obj.ERROR_OBJ);
        }
        return false;
    }

    private Obj evalIdentifier(Identifier identifier, Environment environment) {
        Obj obj = environment.get(identifier.value);

        if (obj == null) {
            return new ErrObj("identifier not found: " + identifier.value);
        }

        return obj;
    }
}
