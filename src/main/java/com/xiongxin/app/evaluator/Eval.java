package com.xiongxin.app.evaluator;

import com.xiongxin.app.ast.IntegerLiteral;
import com.xiongxin.app.ast.Node;
import com.xiongxin.app.obj.IntObj;
import com.xiongxin.app.obj.Obj;

public class Eval {

    public static Obj eval(Node node) {

        if (node instanceof IntegerLiteral) {
            IntObj intObj = new IntObj();
            intObj.value = ((IntegerLiteral) node).value;

            return intObj;
        }

        return null;
    }
}
