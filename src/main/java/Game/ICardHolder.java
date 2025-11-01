package Game;

/**
 * Interface for classes that can hold cards
 * Used as a custom type instead of using Object
 */
public interface ICardHolder {
    void placeCard(Card card);

    Card pickUpCard();

    boolean isEmpty();

    Card peek();
}
