package com.xiongxin.app.obj;

public class ErrObj implements Obj {
    public String message;

    @Override
    public String type() {
        return ERROR_OBJ;
    }

    @Override
    public String inspect() {
        return "ERROR: " + message;
    }

    public ErrObj(String message) {
        this.message = message;
    }
}
