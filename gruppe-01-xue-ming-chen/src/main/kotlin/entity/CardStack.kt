package entity

/**
 *   The Class as the Object of a [CardStack].
 *
 *   The Object cardstack can be the drawstack and the reserve stack
 *   (amount of cards up to 24)
 *
 *   @property cards : The Stack as a List of the cards
 */
class CardStack( var cards: MutableList<Card> = mutableListOf()) {
    init {
        require(cards.size <= 24)
    }
    /**
     * variable for the amount of cards in the stack
     */

    val size: Int get() = cards.size

    /**
     * variable to check for empty stack
     */

    val empty: Boolean get() = cards.isEmpty()

    /**
     * Override the toSting method to show the Cards
     * in the Card card Stack .
     */

    override fun toString(): String {
        return cards.toString()
    }

}

