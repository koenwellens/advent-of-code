package common;

import java.util.List;

public abstract class AbstractObjectBasedOnInput<RESULT> {

    protected final List<String> textLines;

    public AbstractObjectBasedOnInput(final List<String> textLines) {
        this.textLines = textLines;
    }

    public abstract RESULT run();
    public abstract RESULT alternateRun();
}
