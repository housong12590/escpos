package com.xiaom.pos4j.element.generator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.xiaom.pos4j.comm.Dict;
import com.xiaom.pos4j.element.Block;
import com.xiaom.pos4j.element.Element;
import com.xiaom.pos4j.parser.*;

import java.util.List;
import java.util.Map;

public class BlockGenerator implements Generator<Block> {

    static {
        AviatorEvaluator.addFunction(new IsEmptyFunction());
        AviatorEvaluator.addFunction(new IsNullFunction());
        AviatorEvaluator.addFunction(new LengthFunction());
    }

    @Override
    public Block create(ElementExample example, Transform transform, Object env) {
        Block block = new Block();
        Property testProperty = example.getProperty("test");
        Property listProperty = example.getProperty("list");
        Property itemProperty = example.getProperty("item");
        Map<String, Object> newEnv = Dict.of(env);
        if (testProperty != null) {
            String value = testProperty.getValue();
            // 不满足测试条件, 跳过生成元素
            if (!test(value, newEnv)) {
                return null;
            }
        }
        if (listProperty == null || itemProperty == null) {
            createChildElement(block, example, transform, env);
            return block;
        }
        Object value = transform.get(listProperty.getValue(), env);
        if (!(value instanceof List)) {
            return null;
        }
        List<?> list = (List<?>) value;
        for (Object item : list) {
            newEnv.put(itemProperty.getValue(), item);
            createChildElement(block, example, MapTransform.get(), newEnv);
        }
        return block;
    }

    private void createChildElement(Block block, ElementExample example, Transform transform, Object env) {
        for (ElementExample exampleChild : example.getChildren()) {
            Element element = ElementKit.getElement(exampleChild, transform, env);
            if (element != null) {
                block.addChildren(element);
            }
        }
    }


    private boolean test(String expression, Map<String, Object> env) {
        try {
            Object execute = AviatorEvaluator.execute(expression, env, true);
            if (execute instanceof Boolean) {
                return (boolean) execute;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    static class IsEmptyFunction extends AbstractFunction {

        @Override
        public String getName() {
            return "isEmpty";
        }

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
            boolean result = false;
            Object object = FunctionUtils.getJavaObject(arg1, env);
            if (object == null) {
                result = true;
            } else if (object instanceof String) {
                result = ((String) object).isEmpty();
            } else if (object instanceof List) {
                result = ((List<?>) object).isEmpty();
            } else if (object instanceof Map) {
                result = ((Map<?, ?>) object).isEmpty();
            }
            return FunctionUtils.wrapReturn(result);
        }
    }

    static class IsNullFunction extends AbstractFunction {

        @Override
        public String getName() {
            return "isNull";
        }

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
            boolean result;
            Object object = FunctionUtils.getJavaObject(arg1, env);
            result = object == null;
            return FunctionUtils.wrapReturn(result);
        }
    }

    static class LengthFunction extends AbstractFunction {

        @Override
        public String getName() {
            return "length";
        }

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
            int result = 0;
            Object object = FunctionUtils.getJavaObject(arg1, env);
            if (object instanceof String) {
                result = ((String) object).length();
            } else if (object instanceof List) {
                result = ((List<?>) object).size();
            } else if (object instanceof Map) {
                result = ((Map<?, ?>) object).size();
            }
            return FunctionUtils.wrapReturn(result);
        }
    }
}
