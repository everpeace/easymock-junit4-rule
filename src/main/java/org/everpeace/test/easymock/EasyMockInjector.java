package org.everpeace.test.easymock;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.everpeace.test.junit4.annotation.EasyMockObject;
import org.everpeace.test.junit4.annotation.EasyMockObject.EasyMockType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.*;

/**
 * Inject EasyMock objects to testcase objects.
 *
 * @author everpeace _at_ gmail _dot_ com
 */
public class EasyMockInjector {

    // private constructor.
    private EasyMockInjector() {
        // nop
    }

    /**
     * inject EasyMock objects to all fields annotated by {@link org.everpeace.test.junit4.annotation.EasyMockObject}
     * .
     *
     * @param testInstance test case instance.
     * @return a map from {@link Field} to a mock object injected.
     * @throws IllegalAccessException {@link IllegalAccessException}
     */
    public static Map<Field, Object> injectEasyMockObjectsOn(Object testInstance)
            throws IllegalAccessException {
        Map<Field, Object> map = new HashMap<Field, Object>();
        for (Field f : getAllFieldsWith(testInstance, EasyMockObject.class)) {
            try {
                throwExceptionIfNull(f, testInstance);

                Class<?> rawType = TypeUtils.getRawType(f.getGenericType(),
                        testInstance.getClass());
                if (rawType != null) {
                    EasyMockObject a = rawType
                            .getAnnotation(EasyMockObject.class);
                    EasyMockType type = a != null ? a.value()
                            : EasyMockType.DEFAULT;
                    Object mock = createMockWithType(rawType, type);
                    FieldUtils.writeField(f, testInstance, mock, true);
                    map.put(f, mock);
                } else {
                    throw new IllegalStateException(
                            "cannot resolve field type;" + f.getGenericType());
                }

            } catch (NullPointerException ex) {
                throw new IllegalStateException("cannot resolve field type;"
                        + f.getGenericType());
            }
        }
        return map;
    }

    private static Object createMockWithType(Class<?> rawType, EasyMockType type) {
        switch (type) {
            case STRICT:
                return createStrictMock(rawType);
            case NICE:
                return createNiceMock(rawType);
            default:
                return createMock(rawType);
        }
    }

    private static void throwExceptionIfNull(Field f, Object testInstance)
            throws IllegalAccessException {
        if (FieldUtils.readField(f, testInstance, true) != null) {
            throw new IllegalStateException(
                    "cannot inject a mock object because field " + f.getName()
                            + "is not null");
        }
    }

    /**
     * get all fields on obj annotated by all given annotations.
     *
     * @param obj         an object.
     * @param annotation annotations for search.
     * @return list of fields on obj annotated by all given annotations.
     */
    private static List<Field> getAllFieldsWith(Object obj,
                                                Class<? extends Annotation> annotation) {
        List<Field> fields = new ArrayList<Field>();
        Class<?> clazz = obj.getClass();
        do {
            for (Field f : clazz.getDeclaredFields()) {
                if (annotatedBy(f, annotation)) {
                    fields.add(f);
                }
            }
            clazz = clazz.getSuperclass();
        } while (clazz != Object.class);
        return fields;
    }

    /**
     * check whether f is annotated by a given annotation.
     *
     * @param f a field
     * @param a an annotation to check.
     * @return true if f is annotated bya given annotation, false otherwise.
     */
    private static boolean annotatedBy(Field f, Class<? extends Annotation> a) {
        return f.isAnnotationPresent(a);
    }

}
