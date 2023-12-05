package entity


/**
 * The Class has specific function like two create pyramid
 * or create the card set for use for test.
 */
class TestFunction {
    /**
     * The function create the CardSet of the Game
     *
     * @return Returns a list of cards as the cardSet
     */
    fun createCardSet(): MutableList<Card> {
        val card = mutableListOf<Card>()
        val cardSuitValues = CardSuit.values()
        val cardValueValues = CardValue.values()
        for (suitValue in cardSuitValues) {
            for (valueValue in cardValueValues) {
                card.add(Card(suitValue, valueValue))
            }
        }
        return card
    }

    /**
     * private function that create the pyramid
     *
     * @param: A list of cards
     * @returns a 2d List with 7 rows and the size
     * of each are index+1
     */
    fun createPyramid(cards: List<Card>): MutableList<MutableList<Card>> {
        val pyramidCards = cards.subList(0, 28)
        val pyramid = mutableListOf<MutableList<Card>>()
        var k = 0
        for (i in 0..6) {
            pyramid.add(mutableListOf())
            for (j in 0..i) {
                pyramid[i].add(j,pyramidCards[k])
                k++
            }
        }
        return pyramid
    }
}
