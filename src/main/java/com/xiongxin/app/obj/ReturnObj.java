package com.xiongxin.app.obj;

public class ReturnObj implements Obj {

    public Obj value;

    public ReturnObj(Obj value) {
        this.value = value;
    }

    @Override
    public String type() {
        return RETURN_VALUE_OBK;
    }

    @Override
    public String inspect() {
        return value.inspect();
    }
}
