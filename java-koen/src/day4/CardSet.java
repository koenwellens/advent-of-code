package day4;

public interface CardSet {


    Card aCardToHandle();

    boolean hasCardsToHandle();

    void handleCard(Card card);

    int numberOfScratchCardsHandled();
}
