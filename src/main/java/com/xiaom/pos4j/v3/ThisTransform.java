package com.xiaom.pos4j.v3;

import com.xiaom.pos4j.comm.Dict;
import com.xiaom.pos4j.util.ClassUtils;

public class ThisTransform implements Transform {

    private Transform transform;
    public static final String THIS = "this";

    public ThisTransform(Transform transform) {
        this.transform = transform;
    }

    @Override
    public <T> T get(String key, Object env) {
        Dict newEnv = (Dict) env;
        if (key.startsWith(THIS)) {
            Object aThis = newEnv.get(THIS);
            if (key.equals(THIS)) {
                return ClassUtils.cast(aThis);
            } else {
                key = key.replace(THIS + ".", "");
                return transform.get(key, aThis);
            }
        }
        return transform.get(key, env);
    }
}
