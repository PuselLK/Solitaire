package model;

import java.util.Objects;

/**
 * Represents a playing card with a suit, rank, and visibility status.
 * It also contains an image path for graphical representation.
 */
public class Card {
    /**
     * Enum for card suits.
     */
    public enum Suit {
        CLUB, SPADE, HEART, DIAMOND
    }

    private final Suit _suit; // The suit of the card.
    private final int _rank; // The rank of the card. 0 for Backside, 1 for Ace, 2-10 for Numbers, 11 Joker, 12 Queen, 13 King.
    private boolean _isVisible; // Visibility status of the card.

    /**
     * Constructs a Card instance with specified suit and rank.
     * Initializes the image path based on the suit and rank.
     *
     * @param suit The suit of the card.
     * @param rank The rank of the card.
     */
    public Card(Suit suit, int rank) {
        _suit = suit;
        _rank = rank;
        _isVisible = false;
    }

    /**
     * Determines if this card and another card have opposite colors.
     *
     * @param otherCard The card to compare with.
     * @return true if the cards have opposite colors, false otherwise.
     */
    public boolean isOppositeColor(Card otherCard) {
        return (isRed() && otherCard.isBlack()) || (isBlack() && otherCard.isRed());
    }

    /**
     * Checks if the card's suit is red.
     *
     * @return true if the suit is heart or diamond, false otherwise.
     */
    public boolean isRed() {
        return getSuit() == Suit.HEART || getSuit() == Suit.DIAMOND;
    }

    /**
     * Checks if the card's suit is black.
     *
     * @return true if the suit is spade or club, false otherwise.
     */
    public boolean isBlack() {
        return getSuit() == Suit.SPADE || getSuit() == Suit.CLUB;
    }

    /**
     * Gets the rank of the card.
     *
     * @return The rank of the card.
     */
    public int getRank() {
        return _rank;
    }

    /**
     * Gets the suit of the card.
     *
     * @return The suit of the card.
     */
    public Suit getSuit() {
        return _suit;
    }

    /**
     * Checks if the card is visible.
     *
     * @return true if the card is visible, false otherwise.
     */
    public boolean isVisible() {
        return _isVisible;
    }

    /**
     * Sets the visibility of the card.
     *
     * @param isVisible The visibility status to set.
     */
    public void setVisibility(boolean isVisible) {
        _isVisible = isVisible;
    }

    /**
     * Compares this card with another card for equality based on suit and rank.
     *
     * @param otherCard The card to compare with.
     * @return true if both cards have the same suit and rank, false otherwise.
     */
    @Override
    public boolean equals(Object otherCard) {
        if (otherCard instanceof Card card) {
            return _rank == card.getRank() && _suit == card.getSuit();
        }
        return false;
    }

    /**
     * Generates a hash code for the card based on suit and rank.
     *
     * @return The hash code of the card.
     */
    @Override
    public int hashCode() {
        return Objects.hash(_suit, _rank);
    }
}