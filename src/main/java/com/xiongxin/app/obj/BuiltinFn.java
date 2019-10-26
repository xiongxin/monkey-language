package com.xiongxin.app.obj;

@FunctionalInterface
public interface BuiltinFn {

    Obj apply(Obj ...args);
}
