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
    public static final String RETURN_VALUE_OBK = "RETURN_VALUE";
    public static final String ERROR_OBJ = "ERROR";
    public static final String FUNCTION_OBJ = "FUNCTION";
    public static final String STRING_OBJ = "STRING";
}
