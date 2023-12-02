package day1;

import common.AbstractObjectBasedOnInput;

import java.util.List;

public final class CalibrationDocument extends AbstractObjectBasedOnInput<Integer> {

    public CalibrationDocument(final List<String> textLines) {
        super(textLines);
    }

    @Override
    public Integer run() {
        return this.textLines.stream()
                .map(TwoDigitCalibratedText::new)
                .map(CalibratedText::calibrationValue)
                .mapToInt(CalibrationValue::calibration)
                .sum();
    }

    @Override
    public Integer alternateRun() {
        return this.textLines.stream()
                .map(TextOrDigitCalibratedText::new)
                .map(CalibratedText::calibrationValue)
                .mapToInt(CalibrationValue::calibration)
                .sum();
    }
}
