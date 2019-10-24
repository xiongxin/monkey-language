package com.xiongxin.app.obj;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    public Map<String, Obj> store;
    public Environment outer;

    public Obj get(String name) {
        Obj obj = store.get(name);
        if (obj == null) {
            return outer.get(name);
        }

        return obj;
    }

    public Obj set(String name, Obj value) {
        store.put(name, value);

        return value;
    }

    public Environment(Environment outer) {
        new Environment().outer = new Environment();

        store = new HashMap<>();
    }

    public Environment() {
        store = new HashMap<>();
    }

    public static Environment newEncloseEnvironment(Environment outer) {
        Environment environment = new Environment();
        environment.outer = outer;

        return environment;
    }
}
