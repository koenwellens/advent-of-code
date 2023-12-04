package day4;

import java.util.Collection;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static java.util.stream.LongStream.range;

public abstract class AbstractColorfulCard implements Card {

    private final int id;
    private final Collection<WinningNumber> winningNumbers;
    private final Collection<Number> numbers;

    public AbstractColorfulCard(final String line) {
        this(
                new CardIdParser(line).value(),
                new WinningNumbersParser(line).value(),
                new NumbersParser(line).value()
        );
    }

    public AbstractColorfulCard(final int id,
                                final Collection<WinningNumber> winningNumbers,
                                final Collection<Number> numbers) {
        this.id = id;
        this.winningNumbers = winningNumbers;
        this.numbers = numbers;
    }

    @Override
    public int id() {
        return this.id;
    }

    private long numberOfMatchingNumbers() {
        return this.numbers.stream()
                .filter(number -> number.isOneOf(winningNumbers))
                .count();
    }

    @Override
    public int points() {
        final var numberOfWinningNumbers = numberOfMatchingNumbers();

        if (numberOfWinningNumbers > 0) {
            return (int) Math.pow(2, numberOfWinningNumbers - 1);
        }

        return 0;
    }

    @Override
    public Collection<Card> prizes(final Collection<Card> originalCards) {
        final var numberOfWinningNumbers = numberOfMatchingNumbers();

        if (numberOfWinningNumbers > 0) {
            return range(1, numberOfWinningNumbers + 1)
                    .map(num -> num + id())
                    .mapToObj(copyId -> originalCards.stream().filter(card -> card.id() == copyId).findAny().orElseThrow())
                    .map(Card::createCopy)
                    .collect(Collectors.toUnmodifiableList());
        }

        return emptySet();
    }

    @Override
    public CopyCard createCopy() {
        return new CopyOfColorfulCard(this.id(), this.winningNumbers, this.numbers);
    }
}
