package adventofcode2023.day03;

import java.util.Set;

record GearSymbol(Set<Integer> indexes) {
    boolean isAdjacentTo(EngineSchematicNumber number) {
        return this.indexes().stream().anyMatch(index -> number.indexRange().contains(index));
    }
}
