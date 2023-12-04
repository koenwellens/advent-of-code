package day4;

import java.util.Collection;

public class CopyOfColorfulCard extends AbstractColorfulCard implements CopyCard {
    public CopyOfColorfulCard(final int id,
                              final Collection<WinningNumber> winningNumbers,
                              final Collection<Number> numbers) {
        super(id, winningNumbers, numbers);
    }
}
