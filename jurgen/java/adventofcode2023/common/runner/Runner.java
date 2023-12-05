package adventofcode2023.common.runner;

import adventofcode2023.common.Input;

import java.util.function.Function;

public class Runner {
    public static Runner.Builder runnerFor(Class<?> solutionClass) {
        return new Builder(new RunnerConfig(solutionClass));
    }

    private final RunnerConfig runnerConfig;

    private Runner(RunnerConfig runnerConfig) {
        this.runnerConfig = runnerConfig;
    }

    public void run() {
        System.out.println("Running solution for " + runnerConfig.getPuzzleName());
        runnerConfig.getRunConfigs()
                .forEach(runConfig -> {
                    Object result = runConfig.solution().apply(runConfig.input());
                    boolean success = result != null && result.equals(runConfig.expectedResult());
                    System.out.println(
                            success
                                    ? "\t✅ Running " + runnerConfig.getPuzzleName() + " " + runConfig.name() + " with [" + runConfig.input().getName() + "] yielded expected result [" + result + "]"
                                    : "\t❌ Running " + runnerConfig.getPuzzleName() + " " + runConfig.name() + " with [" + runConfig.input().getName() + "] yielded wrong result [" + result + "]. Expected [" + runConfig.expectedResult() + "]");
                });
    }

    public static class Builder {
        private final RunnerConfig runnerConfig;

        private Builder(RunnerConfig runnerConfig) {
            this.runnerConfig = runnerConfig;
        }

        public <R> SolutionConfig<R> execute(String name, Function<Input, R> solution) {
            return new SolutionConfig<>(runnerConfig, solution, name);
        }

        public Runner build() {
            if (runnerConfig.getRunConfigs().isEmpty())
                throw new IllegalArgumentException("No runs configured");
            return new Runner(runnerConfig);
        }
    }

    public static class SolutionConfig<R> {
        private final Function<Input, R> solution;
        private final RunnerConfig runnerConfig;
        private final String name;

        private SolutionConfig(RunnerConfig runnerConfig, Function<Input, R> solution, String name) {
            this.runnerConfig = runnerConfig;
            this.solution = solution;
            this.name = name;
        }

        public ExpectationConfig<R> withInput(Input input) {
            return new ExpectationConfig<>(runnerConfig, solution, name, input);
        }

    }

    public static class ExpectationConfig<R> {
        private final RunnerConfig runnerConfig;
        private final Function<Input, R> solution;
        private final String name;
        private final Input input;

        private ExpectationConfig(RunnerConfig runnerConfig, Function<Input, R> solution, String name, Input input) {
            this.runnerConfig = runnerConfig;
            this.solution = solution;
            this.name = name;
            this.input = input;
        }

        public Builder expect(R result) {
            RunConfig<R> runConfig = new RunConfig<>(name, solution, input, result);
            runnerConfig.add(runConfig);
            return new Builder(runnerConfig);
        }

    }
}
