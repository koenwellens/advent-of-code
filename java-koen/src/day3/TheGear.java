package day3;

import java.util.Collection;

public final class TheGear implements Gear {

    private final Collection<PartNumber> partNumbers;

    public TheGear(final Collection<PartNumber> partNumbers) {
        this.partNumbers = partNumbers;
    }

    @Override
    public int gearRatio() {
        return partNumbers.stream().mapToInt(PartNumber::value).reduce(1, (l, r) -> l * r);
    }
}
