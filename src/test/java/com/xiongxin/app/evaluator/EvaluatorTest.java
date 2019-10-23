package com.xiongxin.app.evaluator;

import com.xiongxin.app.ast.BlockStatement;
import com.xiongxin.app.ast.Identifier;
import com.xiongxin.app.ast.Program;
import com.xiongxin.app.lexer.Lexer;
import com.xiongxin.app.lexer.Token;
import com.xiongxin.app.obj.*;
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
        Environment environment = new Environment();
        return eval.eval(program, environment);
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

    private static class EvalIf {
        public String input;
        public Object expected;

        public EvalIf(String input, Object expected) {
            this.input = input;
            this.expected = expected;
        }
    }

    @Test
    public void testIfExpression() {
        List<EvalIf> tests = Arrays.asList(
                new EvalIf("if (true) { 10 }", 10),
                new EvalIf("if (true) { 10 }", 10),
                new EvalIf("if (10 > 1) { return 1; }", 1)
        );

        tests.forEach(evalIf -> {
            Obj obj = testEval(evalIf.input);
            if (evalIf.expected instanceof Integer) {
                testIntObj(obj, (Integer) evalIf.expected);
            }
        });
    }


    @Test
    public void testReturnExpression() {

        List<EvalInteger> evalIntegers = Arrays.asList(
                new EvalInteger("return 5;", 5),
                new EvalInteger("return 10;9", 10),
                new EvalInteger("return -10;", -10),
                new EvalInteger("9;return -5;777;", -5)
        );

        evalIntegers.forEach( evalInteger -> {
            Obj obj = testEval(evalInteger.input);
            testIntObj(obj, evalInteger.expected);
        } );
    }

    private static class ErrorMessage {
        public String input;
        public String message;

        public ErrorMessage(String input, String message) {
            this.input = input;
            this.message = message;
        }
    }

    @Test
    public void testErrorHanding() {
        List<ErrorMessage> evalIntegers = Arrays.asList(
                new ErrorMessage("foobar", "identifier not found: foobar")
        );

        evalIntegers.forEach( evalInteger -> {
            ErrObj obj = (ErrObj) testEval(evalInteger.input);
            assertEquals("message equal:", evalInteger.message, obj.message);
        } );
    }

    @Test
    public void testLetExpression() {

        List<EvalInteger> evalIntegers = Arrays.asList(
                new EvalInteger("let a = 5; a;", 5),
                new EvalInteger("let a = 5 * 5; a;", 25)
        );

        evalIntegers.forEach( evalInteger -> {
            Obj obj = testEval(evalInteger.input);
            testIntObj(obj, evalInteger.expected);
        } );
    }


//    @Test
//    public void testFunctionObj() {
//        FunObj obj = new FunObj();
//        obj.parameters = Arrays.asList(
//                new Identifier(new Token(Token.IDENT, "a"), "a"),
//                new Identifier(new Token(Token.IDENT, "b"), "b"),
//                new Identifier(new Token(Token.IDENT, "c"), "c")
//        );
//
//        obj.body = new BlockStatement();
//
//        System.out.println(obj.inspect());
//    }

    @Test
    public void TestFunctioObject() {
        String input = "fn(x){ x + 2 };";

        Obj eval = testEval(input);
        assertTrue("is fun obj", eval instanceof FunObj);
        FunObj funObj = (FunObj) eval;
        assertEquals("parameter length", 1, funObj.parameters.size());
        assertEquals("parameter name", "x", funObj.parameters.get(0).value);
        assertEquals("fun body", "(x + 2)", funObj.body.toString());
    }


    @Test
    public void testFunctionApp() {

        List<EvalInteger> evalIntegers = Arrays.asList(
                new EvalInteger("let identity = fn(x) { x; }; identity(5);", 5),
                new EvalInteger("let identity = fn(x) { return x; }; identity(5);", 5),
                new EvalInteger("let double = fn(x) { x * 2; }; double(5);", 10),
                new EvalInteger("let add = fn(x, y) { x + y; }; add(5, 5);", 10),
                new EvalInteger("let add = fn(x, y) { x + y; }; add(5, 5);", 10),
                new EvalInteger("let add = fn(x, y) { x + y; }; add(5 + 5, add(5, 5));",10),
                new EvalInteger("fn(x) { x; }(5)", 5)
        );

        evalIntegers.forEach( evalInteger -> {
            Obj obj = testEval(evalInteger.input);
            testIntObj(obj, evalInteger.expected);
        } );
    }
}
