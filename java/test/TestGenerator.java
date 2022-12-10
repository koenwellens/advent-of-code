import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DynamicTest;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class TestGenerator<S extends DaySolution<R>, R> {

    private final Stream.Builder<TestConfig> testStreamBuilder = Stream.builder();
    private final S testInstance;

    public TestGenerator(Class<S> classToTest) {
        try {
            this.testInstance = classToTest.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <S extends DaySolution<R>, R> TestGenerator<S, R> generateTestsForClass(Class<S> classToTest) {
        return new TestGenerator<>(classToTest);
    }

    public ExecutionConfig forExample() {
        return forExample(null);
    }

    public ExecutionConfig forExample(Integer exampleNr) {
        return forInputFile("example", exampleNr);
    }

    public ExecutionConfig forInput() {
        return forInputFile("input", null);
    }

    private ExecutionConfig forInputFile(String type, Integer index) {
        TestConfig testConfig = new TestConfig();
        testConfig.inputLabel = type +
                Optional.ofNullable(index).map(nr -> " " + nr).orElse("");
        testConfig.inputFilePath = Path.of("input/day%02d_%s%s".formatted(
                testInstance.getDayNr(),
                type,
                Optional.ofNullable(index).map(nr -> "_" + nr).orElse("")));
        return new ExecutionConfig(testConfig);
    }

    public Stream<DynamicTest> generateTests() {
        return testStreamBuilder.build()
                .map(testConfig -> dynamicTest(
                        testConfig.testLabel(),
                        null,
                        () -> assertEquals(testConfig.expectation, testConfig.execution.apply(testConfig.inputFilePath))));
    }

    public class ExecutionConfig {
        private final TestConfig testConfig;

        public ExecutionConfig(TestConfig testConfig) {
            this.testConfig = testConfig;
        }

        public ExpectationConfig<R> part1() {
            return part1(DaySolution::part1);
        }

        public ExpectationConfig<R> part2() {
            return part2(DaySolution::part2);
        }

        public <R2> ExpectationConfig<R2> part1(BiFunction<S, Path, R2> partSolutionMethod) {
            return part(1, partSolutionMethod);
        }

        public <R2> ExpectationConfig<R2> part2(BiFunction<S, Path, R2> partSolutionMethod) {
            return part(2, partSolutionMethod);
        }

        @NotNull
        private <R2> ExpectationConfig<R2> part(int partNr, BiFunction<S, Path, R2> daySolutionMethod) {
            testConfig.execution = path -> daySolutionMethod.apply(testInstance, path);
            testConfig.executionLabel = "part " + partNr;
            return new ExpectationConfig<>(testConfig);
        }
    }

    public class ExpectationConfig<E> {

        private final TestConfig testConfig;

        public ExpectationConfig(TestConfig testConfig) {
            this.testConfig = testConfig;
        }

        public TestGenerator<S, R> shouldBe(E expectation) {
            testConfig.expectation = expectation;
            testStreamBuilder.add(testConfig);
            return TestGenerator.this;
        }
    }

    private static class TestConfig {
        Path inputFilePath;
        Function<Path, Object> execution;
        Object expectation;
        String executionLabel;
        String inputLabel;

        String testLabel() {
            return executionLabel + " " + inputLabel + " should be " + expectation;
        }
    }
}
