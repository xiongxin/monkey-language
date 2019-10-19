package com.xiongxin.app.obj;

public class BoolObj implements Obj {
    public boolean value;
    public static BoolObj TRUE = new BoolObj(true);
    public static BoolObj FALSE = new BoolObj(false);

    public BoolObj(boolean value) {
        this.value = value;
    }

    @Override
    public String type() {
        return BOOLEAN_OBJ;
    }

    @Override
    public String inspect() {
        return String.valueOf(value);
    }
}
