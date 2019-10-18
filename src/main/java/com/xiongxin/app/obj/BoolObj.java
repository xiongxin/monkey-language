package com.xiongxin.app.obj;

public class BoolObj implements Obj {
    public boolean value;

    @Override
    public String type() {
        return BOOLEAN_OBJ;
    }

    @Override
    public String inspect() {
        return String.valueOf(value);
    }
}
