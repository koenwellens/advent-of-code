package adventofcode2023.common.runner;

import java.util.ArrayList;
import java.util.List;

class RunnerConfig {
    private final Class<?> solutionClass;
    private final List<RunConfig<?>> runConfigs = new ArrayList<>();

    RunnerConfig(Class<?> solutionClass) {
        this.solutionClass = solutionClass;
    }

    void add(RunConfig<?> runConfig) {
        this.runConfigs.add(runConfig);
    }

    List<RunConfig<?>> getRunConfigs() {
        return List.copyOf(runConfigs);
    }

    String getPuzzleName() {
        return solutionClass.getSimpleName();
    }
}
