package com.xiongxin.app.evaluator;

import com.xiongxin.app.ast.*;
import com.xiongxin.app.obj.*;

import java.util.*;
import java.util.stream.IntStream;

/**
 * 将 ast 转换成 Obj 类型
 * 实现方法
 *  根据ast类型，递归解析表达式
 */
public class Eval {

    private static Map<String, BuiltinObj> builtins = new HashMap<String, BuiltinObj>(){{
        put("len", new BuiltinObj(args -> {
            if (args.length != 1) {
                return new ErrObj(String.format("wrong number of arguments. got=%d, want=1", args.length));
            }

            switch (args[0].type()) {
                case Obj.STRING_OBJ:
                    return new IntObj(((StrObj) args[0]).value.length());
                default:
                    return new ErrObj(String.format(""));
            }
        }));
    }};

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

        if (node instanceof StringLiteral) {
            return new StrObj(((StringLiteral) node).value);
        }

        /**
         *
         * 需要支持的语法格式
         * {"let identity = fn(x) { x; }; identity(5);", 5},
         * {"let identity = fn(x) { return x; }; identity(5);", 5},
         * {"let double = fn(x) { x * 2; }; double(5);", 10},
         * {"let add = fn(x, y) { x + y; }; add(5, 5);", 10},
         * {"let add = fn(x, y) { x + y; }; add(5 + 5, add(5, 5));", 20},
         * {"fn(x) { x; }(5)", 5},
         */
        if (node instanceof CallExpression) {
            // function is an Identifier or FunctionLiteral
            CallExpression callExpression = (CallExpression) node;
            Obj obj = eval(callExpression.function, environment);
            if (isError(obj)) return obj;

            List<Obj> args = evalExpressions(callExpression.arguments, environment);

            if (obj instanceof BuiltinObj) {
                return ((BuiltinObj) obj).fn.apply(args.toArray(new Obj[0]));
            }

            if (obj instanceof FunObj) {
                FunObj funObj = (FunObj) obj;
                if (args.size() == 1 && isError(args.get(0)))
                    return args.get(0);

                return applyFunction(funObj, args);
            }
        }

        return null;
    }

    // 执行函数
    private Obj applyFunction(FunObj fn, List<Obj> args) {
        Environment extendedEnv = extendFunctionEnv(fn, args);

        Obj obj = eval(fn.body, extendedEnv);

        return unwrapReturnValue(obj);
    }

    private Environment extendFunctionEnv(FunObj obj, List<Obj> args) {
        Environment environment = Environment.newEncloseEnvironment(obj.environment);

        // 将参数列表装进当前函数运行时环境
        IntStream.range(0, obj.parameters.size())
                .forEach(idx -> {
                    environment.set(obj.parameters.get(idx).value, args.get(idx));
                });

        return environment;
    }

    private Obj unwrapReturnValue(Obj obj) {
        if (obj instanceof ReturnObj) {
            return ((ReturnObj) obj).value;
        }

        return obj;
    }

    private List<Obj> evalExpressions(List<Expression> expressions, Environment environment) {
        List<Obj> objs = new LinkedList<>();

        for (Expression expression : expressions) {
            Obj obj = eval(expression, environment);
            if (isError(obj)) {
                objs = new LinkedList<>();
                objs.add(obj);

                return objs;
            }

            objs.add(obj);
        }

        return objs;
    }

    /**
     * 解析程序
     * 一个程序包含 很多 Statement
     * @param program
     * @param environment
     * @return
     */
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

        if (left instanceof StrObj && right instanceof StrObj) {
            String lvalue = ((StrObj) left).value;
            String rvalue = ((StrObj) right).value;

            return new StrObj(lvalue + rvalue);
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
            obj = builtins.get(identifier.value);

            if (obj != null) {
                return obj;
            }
            return new ErrObj("identifier not found: " + identifier.value);
        }

        return obj;
    }
}
