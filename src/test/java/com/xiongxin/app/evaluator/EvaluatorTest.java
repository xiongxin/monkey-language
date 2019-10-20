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
                new EvalInteger("10", 10),
                new EvalInteger("-10", -10),
                new EvalInteger("-5", -5),
                new EvalInteger("5 + 5 + 5 + 5 - 10", 10),
                new EvalInteger("5 + 2 + 10", 17),
                new EvalInteger("2 * 2 * 2 * 2 * 2", 32),
                new EvalInteger("(5 + 10 * 2 + 15 / 3) * 2 + -10", 50),
                new EvalInteger("3 * (3 * 3) + 10", 37),
                new EvalInteger("50 / 2 * 2 + 10", 60)
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

    @Test
    public void testBangOperator() {
        List<EvalBoolean> evalIntegers = Arrays.asList(
                new EvalBoolean("!true", false),
                new EvalBoolean("!false", true),
                new EvalBoolean("!5", false),
                new EvalBoolean("!!true", true),
                new EvalBoolean("!!false", false),
                new EvalBoolean("!!5", true),
                new EvalBoolean("1 > 2", false),
                new EvalBoolean("1 < 2", true),
                new EvalBoolean("1 > 1", false),
                new EvalBoolean("1 < 1", false),
                new EvalBoolean("1 == 1", true),
                new EvalBoolean("1 != 1", false),
                new EvalBoolean("true == true", true),
                new EvalBoolean("false == false", true),
                new EvalBoolean("true == false", false),
                new EvalBoolean("false != true", true),
                new EvalBoolean("true != false", true),
                new EvalBoolean("(1 < 2) == true", true),
                new EvalBoolean("(1 < 2) == false", false),
                new EvalBoolean("(1 > 2) == true", false),
                new EvalBoolean("(1 > 2) == false", true)

        );

        evalIntegers.forEach( evalBoolean -> {
            Obj obj = testEval(evalBoolean.input);
            testBoolObj(obj, evalBoolean.expected);
        });
    }
}
