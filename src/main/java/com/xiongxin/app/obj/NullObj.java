package com.xiongxin.app.obj;

public class NullObj implements Obj {

    public static NullObj NULL = new NullObj();

    @Override
    public String type() {
        return NULL_OBJ;
    }

    @Override
    public String inspect() {
        return "null";
    }
}
