package com.xiongxin.app.obj;

public class NullObj implements Obj {
    @Override
    public String type() {
        return NULL_OBJ;
    }

    @Override
    public String inspect() {
        return "null";
    }
}
