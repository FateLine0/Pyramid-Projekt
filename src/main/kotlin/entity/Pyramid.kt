package entity

/**
 * The [Pyramid] as a card Pyramid
 *
 *  the class Pyramid create the Object pyramid, which are you can choose the cards on it,
 *  and you can remove from it.
 *
 *
 *  @property cards it represented the Pyramid itself, which are as 2d-List initialized
 */

class Pyramid(var cards: MutableList<MutableList<Card>>) {
    init {
        require(cards.size == 7)
        for (i in 0 until cards.size) {
            require(cards[i].size == i + 1)
        }
    }

    /**
     * To visualize the Pyramid as a String;
     *
     *  @return Returns a String which are like a right-angled Triangle
     *
     * @sample : [*]
     *           [*,*]
     *           [*,*,*]
     *           [*,*,*,*]
     * Note: the * are represented as a Card object
     */
    override fun toString(): String {
        var output = ""
        for (i in 0 until cards.size) {
            output += cards[i].toString()
            if (i != cards.size - 1) {
                output += "\n"
            }
        }
        return output
    }

}
