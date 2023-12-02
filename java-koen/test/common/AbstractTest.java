package common;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractTest<T extends AbstractObjectBasedOnInput<EXPECTED>, EXPECTED> {

    protected abstract Function<List<String>,T> constructor();

    private AdventOfCodeTest myAoCAnnotation() {
        return getClass().getAnnotation(AdventOfCodeTest.class);
    }

    @Test
    void runExample1() {
        runTestCode("example1", AbstractObjectBasedOnInput::run, AdventOfCodeTest::text);
    }

    @Test
    void runInput1() {
        runTestCode("input1", AbstractObjectBasedOnInput::run, AdventOfCodeTest::text);
    }
    @Test
    void runExample2() {
        runTestCode("example2", AbstractObjectBasedOnInput::alternateRun, AdventOfCodeTest::alternateText);
    }

    @Test
    void runInput2() {
        runTestCode("input2", AbstractObjectBasedOnInput::alternateRun, AdventOfCodeTest::alternateText);
    }

    private void runTestCode(final String name, final Function<T, EXPECTED> run, Function<AdventOfCodeTest, String> text) {
        final var myAnnotation = myAoCAnnotation();
        final var sut = new ActualInput<>(myAnnotation.day(), name, constructor()).get();
        final var result = run.apply(sut);
        final var expected = expectedValue(myAnnotation, name);

        assertEquals(expected, result);
        System.out.println(text.apply(myAnnotation) + result);
    }

    private EXPECTED expectedValue(AdventOfCodeTest annotation, String name) {
        String expectedValue;
        try {
            expectedValue = (String) annotation.getClass().getMethod(name).invoke(annotation);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Something went wrong with looking for method " + name + " on the AdventOfCode annotation!", e);
        }

        if (annotation.valueType().equals(Integer.class)) {
            return (EXPECTED) Integer.valueOf(expectedValue);
        }
        return (EXPECTED) expectedValue;
    }
}
