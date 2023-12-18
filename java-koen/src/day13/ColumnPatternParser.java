package day13;

import common.Parser;

import java.util.ArrayList;
import java.util.List;

public final class ColumnPatternParser implements Parser<List<String>> {

    private final List<String> lines;

    public ColumnPatternParser(final List<String> lines) {
        this.lines = lines;
    }

    @Override
    public List<String> value() {
        var matrix = new char[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            matrix[i] = lines.get(i).toCharArray();
        }

        var transposedMatrix = new char[matrix[0].length][matrix.length];
        for (var i = 0; i < matrix.length; i++) {
            for (var j = 0; j < matrix[0].length; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }

        // Step 3: Join the transposed matrix back into a string
        final var result = new ArrayList<String>();
        for (final var chars : transposedMatrix) {
            final var resultString = new StringBuilder();
            for (int j = 0; j < transposedMatrix[0].length; j++) {
                resultString.append(chars[j]);
            }
            result.add(resultString.toString().trim());
        }

        return result;
    }
}
