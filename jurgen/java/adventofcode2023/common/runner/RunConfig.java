package adventofcode2023.common.runner;

import adventofcode2023.common.Input;

import java.util.function.Function;

record RunConfig<R>(
        String name,
        Function<Input, R> solution,
        Input input,
        R expectedResult) {

}
