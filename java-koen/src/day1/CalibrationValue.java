package day1;

public final class CalibrationValue {

    private final int value;

    public CalibrationValue(final DigitValue firstDigit, final DigitValue secondDigit) {
        this(firstDigit.value() + secondDigit.value());
    }

    public CalibrationValue(final String value) {
        this(Integer.parseInt(value));
    }

    public CalibrationValue(final int value) {
        this.value = value;
    }

    public int calibration() {
        return this.value;
    };
}
