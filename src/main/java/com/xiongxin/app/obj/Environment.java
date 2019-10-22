package com.xiongxin.app.obj;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    public Map<String, Obj> store = new HashMap<>();

    public Obj get(String name) {
        return store.get(name);
    }

    public Obj set(String name, Obj value) {
        store.put(name, value);

        return value;
    }
}
