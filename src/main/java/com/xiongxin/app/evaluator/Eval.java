package com.xiongxin.app.evaluator;

import com.xiongxin.app.ast.*;
import com.xiongxin.app.obj.*;

import java.util.List;
import java.util.Objects;

/**
 * 实现方法
 *  递归解析表达式
 */
public class Eval {

    public Obj eval(Node node) {
        Objects.requireNonNull(node, "node must not null");

        if (node instanceof Program) {
            return evalProgram((Program) node);
        }

        if (node instanceof ExpressionStatement) {
            return eval(((ExpressionStatement) node).expression);
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
            Obj obj = eval(((PrefixExpression) node).right);
            return evalPrefixExpression( ((PrefixExpression) node).operator, obj);
        }

        if (node instanceof InfixExpression) {
            Obj left = eval(((InfixExpression) node).left);
            Obj right = eval(((InfixExpression) node).right);

            return evalInfixExpression(((InfixExpression) node).operator, left, right);
        }

        if (node instanceof IfExpression) {
            return evalIfExpression((IfExpression) node);
        }

        if (node instanceof BlockStatement) {
            return evalBlockStatement((BlockStatement) node);
        }

        if (node instanceof ReturnStatement) {
            Obj obj = eval(((ReturnStatement) node).returnValue);

            return new ReturnObj(obj);
        }

        return null;
    }

    private Obj evalProgram(Program program) {
        Obj obj = null;

        for (Statement statement : program.statements) {
            obj = eval(statement);

            if (obj instanceof ReturnObj) {
                return ((ReturnObj) obj).value; // 如果在Program的statements中存在return，立即返回ReturnObj的值
            }
        }

        return obj;
    }

    private Obj evalBlockStatement(BlockStatement blockStatement) {
        Obj obj = null;
        for (Statement statement : blockStatement.statements) {
            obj = eval(statement);

            if (obj.type().equals(Obj.RETURN_VALUE_OBK)) {
                return obj; // BlockStatemen中碰到Return 语句时，立即返回 ReturnObj
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
                return NullObj.NULL;
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
                    return NullObj.NULL;
            }
        }

        return NullObj.NULL;
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
                return NullObj.NULL;
        }
    }

    private BoolObj nativeBoolToBooleanObject(boolean bool) {
        if (bool)
            return BoolObj.TRUE;
        else
            return BoolObj.FALSE;
    }


    private Obj evalIfExpression(IfExpression ifExpression) {
        Obj obj = eval(ifExpression.condition);

        if (isTruthy(obj)) {
            return eval(ifExpression.consequence);
        } else if (ifExpression.alternative != null) {
            return eval(ifExpression.alternative);
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
}
