package org.apache.flink.streaming.runtime.operators.windowing;

import org.apache.flink.streaming.api.operators.OneInputStreamOperator;
import org.apache.flink.streaming.api.transformations.OneInputTransformation;
import org.apache.flink.util.OutputTag;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class WindowOperatorHelper {

    public static Object getField(Object obj, Class declaringClazz, String field) {
        try {
            Field f = declaringClazz.getDeclaredField(field);
            f.setAccessible(true);
            return f.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Couldn't get " + field + " from " + obj, e);
        }
    }

    public static void replaceOperator(OneInputTransformation transformation,
                                       OneInputStreamOperator operator) {
        try {
            Field operatorField = OneInputTransformation.class.getDeclaredField("operator");

            // circumvent private modifier of OneInputTransformation.operator
            operatorField.setAccessible(true);
            // circumvent final modifier of OneInputTransformation.operator
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(operatorField, operatorField.getModifiers() & ~Modifier.FINAL);

            operatorField.set(transformation, operator);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Couldn't replace operator of " + transformation, e);
        }
    }

    public static void enrichWindowOperator(OneInputTransformation transformation,
                                            OutputTag mappedEventOutputTag) {
        replaceOperator(
            transformation,
            MapWithStateWindowOperator.from(
                (WindowOperator) transformation.getOperator(),
                mappedEventOutputTag
            )
        );
    }
}
