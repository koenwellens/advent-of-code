package day17;

import common.Parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CityBlockParser implements Parser<Map<String, CityBlock>> {

    private final List<String> textLines;

    public CityBlockParser(final List<String> textLines) {
        this.textLines = textLines;
    }

    @Override
    public Map<String, CityBlock> value() {
        final var result = new HashMap<String, CityBlock>();
        final var maxY = textLines.size();
        for (int y = 0; y < maxY; y++) {
            final var line = textLines.get(y);
            for (int x = 0; x < line.length(); x++) {
                final var heatLoss = line.charAt(x) - '0';
                result.put(STR."\{x};\{y}", new TheCityBlock(heatLoss, x, y, line.length() - 1, maxY - 1));
            }
        }
        return result;
    }
}
