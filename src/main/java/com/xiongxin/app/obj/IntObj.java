package com.xiongxin.app.obj;

public class IntObj implements Obj {
    public int value;

    @Override
    public String type() {
        return INTEGER_OBJ;
    }

    @Override
    public String inspect() {
        return String.valueOf(value);
    }

}
