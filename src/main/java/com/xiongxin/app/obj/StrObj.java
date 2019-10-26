package com.xiongxin.app.obj;

public class StrObj implements Obj {

    public String value;

    public StrObj(String value) {
        this.value = value;
    }

    @Override
    public String type() {
        return Obj.STRING_OBJ;
    }

    @Override
    public String inspect() {
        return value;
    }
}
