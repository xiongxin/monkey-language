package com.xiongxin.app.obj;

public class BuiltinObj implements Obj {

    public BuiltinFn fn;

    public BuiltinObj(BuiltinFn fn) {
        this.fn = fn;
    }

    @Override
    public String type() {
        return BUILTIN_OBJ;
    }

    @Override
    public String inspect() {
        return "builtin function";
    }
}
