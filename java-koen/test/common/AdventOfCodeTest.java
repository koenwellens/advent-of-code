package common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AdventOfCodeTest {

    int day() default 0;
    String example1() default "";
    String example2() default "";
    String input1() default "";
    String input2() default "";
    Class<?> valueType() default Integer.class;
    String text() default "The computed value is ";
}
