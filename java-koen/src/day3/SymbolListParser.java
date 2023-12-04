package day3;

import java.util.List;

public final class SymbolListParser extends AbstractListParser<Symbol, SymbolParser> {

    public SymbolListParser(final List<String> textLines) {
        super(textLines);
    }

    @Override
    protected SymbolParser parser(final String line, final int row) {
        return new SymbolParser(line, row);
    }
}
