package com.xiongxin.app.obj;

import java.util.List;
import java.util.stream.Collectors;

public class ArrayObj implements Obj {

    public List<Obj> elements;

    @Override
    public String type() {
        return Obj.ARRAY_OBJ;
    }

    @Override
    public String inspect() {
        return "[" +
                elements.stream().map(Obj::inspect).collect(Collectors.joining(",")) +
                "]";
    }
}
