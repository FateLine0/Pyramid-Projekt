package entity

/** the data class [Card] for the element card
 *
 *
 * @property cardSuit CardSuit Describe the Suit of the Card with the four suit
 * @property cardValue CardValue represented the Value of card (2-10, Ace ,Jack ,Queen , King)
 */

data class Card(val cardSuit: CardSuit, val cardValue: CardValue) {
    /**
     *  @property isRevealed  A flag for the card, if the card is revealed or not.
     *  All cards are in the beginning are not revealed
     */
    var isRevealed = false

    /**
     *  The Function put the card as a string out.
     */
    override fun toString(): String {
        return "$cardSuit$cardValue"
    }

    /**
     * The function to compare two cards according to the enum value
     * @param other Other is the parameter for the card to compare
     */
    operator fun compareTo(other : Card) = this.cardValue.ordinal - other.cardValue.ordinal

}