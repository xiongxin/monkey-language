package com.xiongxin.app.obj;

/**
 * Monky Object 类型
 */
public interface Obj {
    String type();
    String inspect();


    public static final String INTEGER_OBJ = "INTEGER";
    public static final String BOOLEAN_OBJ = "BOOLEAN";
    public static final String NULL_OBJ = "NULL";
}
