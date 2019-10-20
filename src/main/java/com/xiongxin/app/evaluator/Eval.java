package com.xiongxin.app.evaluator;

import com.xiongxin.app.ast.*;
import com.xiongxin.app.obj.BoolObj;
import com.xiongxin.app.obj.IntObj;
import com.xiongxin.app.obj.NullObj;
import com.xiongxin.app.obj.Obj;

import java.util.List;
import java.util.Objects;

public class Eval {

    public Obj eval(Node node) {
        Objects.requireNonNull(node, "node must not null");

        if (node instanceof Program) {
            return evalStatements(((Program) node).statements);
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

        return null;
    }


    private Obj evalStatements(List<Statement> statements) {
        Objects.requireNonNull(statements, "语句不能为空");
        Obj obj = null;
        for (Statement statement : statements) {
            obj = eval(statement);
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
}
