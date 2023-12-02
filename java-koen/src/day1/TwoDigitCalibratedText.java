package day1;

public final class TwoDigitCalibratedText implements CalibratedText {

    private final DigitValue firstDigit;
    private final DigitValue lastDigit;

    public TwoDigitCalibratedText(final String text) {
        this(new FirstDigit(text), new LastDigit(text));
    }

    private TwoDigitCalibratedText(final FirstDigit firstDigit, final LastDigit lastDigit) {
        this.firstDigit = firstDigit;
        this.lastDigit = lastDigit;
    }

    public CalibrationValue calibrationValue() {
        return new CalibrationValue(firstDigit, lastDigit);
    }
}
