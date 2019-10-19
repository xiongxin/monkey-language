package com.xiongxin.app.evaluator;

import com.xiongxin.app.ast.*;
import com.xiongxin.app.obj.BoolObj;
import com.xiongxin.app.obj.IntObj;
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
            IntObj intObj = new IntObj();
            intObj.value = ((IntegerLiteral) node).value;

            return intObj;
        }

        if (node instanceof BooleanExpression) {
            Boolean value = ((BooleanExpression) node).value;
            if (value)
                return BoolObj.TRUE;
            return BoolObj.FALSE;
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
}
