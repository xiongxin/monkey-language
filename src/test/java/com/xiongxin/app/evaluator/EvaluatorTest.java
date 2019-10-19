package com.xiongxin.app.evaluator;

import com.xiongxin.app.ast.Program;
import com.xiongxin.app.lexer.Lexer;
import com.xiongxin.app.obj.BoolObj;
import com.xiongxin.app.obj.IntObj;
import com.xiongxin.app.obj.Obj;
import com.xiongxin.app.parser.Parser;
import org.junit.Test;


import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class EvaluatorTest {

    private static class EvalInteger {
        public String input;
        public int expected;

        public EvalInteger(String input, int expected) {
            this.input = input;
            this.expected = expected;
        }
    }


    private Obj testEval(String input) {
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Program program = parser.parseProgram();
        Eval eval = new Eval();
        return eval.eval(program);
    }

    private void testIntObj(Obj obj, int excepted) {
        assertNotNull("Obj is null ", obj);

        IntObj intObj = (IntObj) obj;

        assertEquals("obj's int", excepted, intObj.value);
    }

    @Test
    public void testEvalIntegerExpression() {

        List<EvalInteger> evalIntegers = Arrays.asList(
                new EvalInteger("5", 5),
                new EvalInteger("10", 10)
        );

        evalIntegers.forEach( evalInteger -> {
            Obj obj = testEval(evalInteger.input);
            testIntObj(obj, evalInteger.expected);
        } );
    }

    private static class EvalBoolean {
        public String input;
        public boolean expected;

        public EvalBoolean(String input, boolean expected) {
            this.input = input;
            this.expected = expected;
        }
    }

    private void testBoolObj(Obj obj, boolean expected) {
        assertNotNull("obj in not null", obj);

        BoolObj boolObj = (BoolObj) obj;

        assertEquals("bool obj equal", expected, boolObj.value);
    }

    @Test
    public void testEvalBooleanExpression() {

        List<EvalBoolean> evalIntegers = Arrays.asList(
                new EvalBoolean("true", true),
                new EvalBoolean("false", false)
        );

        evalIntegers.forEach( evalBoolean -> {
            Obj obj = testEval(evalBoolean.input);
            testBoolObj(obj, evalBoolean.expected);
        } );
    }
}
