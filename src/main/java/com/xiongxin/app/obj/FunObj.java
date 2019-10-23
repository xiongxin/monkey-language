package com.xiongxin.app.obj;

import com.xiongxin.app.ast.BlockStatement;
import com.xiongxin.app.ast.Identifier;

import java.util.List;
import java.util.stream.Collectors;

public class FunObj implements Obj {

    public List<Identifier> parameters;
    public BlockStatement body;
    public Environment environment;


    public FunObj(List<Identifier> parameters, BlockStatement body, Environment environment) {
        this.parameters = parameters;
        this.body = body;
        this.environment = environment;
    }

    @Override
    public String type() {
        return FUNCTION_OBJ;
    }

    @Override
    public String inspect() {

        return "fn (" +
                parameters.stream().map(identifier -> identifier.value).collect(Collectors.joining(",")) +
                ") { \n" +
                body.toString() +
                "}\n";
    }
}
