package day1;

public final class TextOrDigitCalibratedText implements CalibratedText {

    private final DigitValue firstDigit;
    private final DigitValue lastDigit;

    public TextOrDigitCalibratedText(final String text) {
        this(new FirstNumber(text), new LastNumber(text));
    }

    private TextOrDigitCalibratedText(final FirstNumber firstDigit, final LastNumber lastDigit) {
        this.firstDigit = firstDigit;
        this.lastDigit = lastDigit;
    }

    public CalibrationValue calibrationValue() {
        return new CalibrationValue(firstDigit, lastDigit);
    }
}
