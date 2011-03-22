package org.everpeace.test.junit4.annotation;

import java.lang.annotation.*;


/**
 * marker annotaion for injecting a mock object.
 *
 * @author everpeace _at_ gmail _dot_ com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface EasyMockObject {
    /**
     * indicate a type of EasyMock
     *
     * @return EasyMockType
     */
    EasyMockType value() default EasyMockType.DEFAULT;

    public enum EasyMockType {
        DEFAULT,
        STRICT,
        NICE
    }
}
