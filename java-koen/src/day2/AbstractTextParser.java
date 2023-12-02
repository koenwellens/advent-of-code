package day2;

public abstract class AbstractTextParser<T> implements TextParser<T> {

    protected final String text;

    protected AbstractTextParser(final String text) {
        this.text = text;
    }
}
