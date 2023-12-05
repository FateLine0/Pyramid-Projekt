package service

import entity.Card
import entity.CardValue

/**
 *  The class [CheckService] are for uses for check something like
 *  to card are from the edges from the pyramid
 */
class CheckService(private val rootService: RootService){
    /**
     * The function checked if both cards are valid
     *
     * The function under followed criterion:
     * 1) Cards are revealed
     * 2)if the sum of them are 15
     * 3)not chosen two Aces
     * 4) both cards are from the pyramid or
     * 5)one card from  the reverseStack and
     * one card from the pyramid
     *
     */
    fun checkCardChoice(card1: Card, card2: Card): Boolean {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        //Both cards are unrevealed false
        if (!(card1.isRevealed && card2.isRevealed)) {
            return false
        }
        //one card not in the reserve stack and one card not the in pyramid
        if (!oneCardIsReserveStack(card1, card2)) {
            //both card not in the card
            if (!checkCardPyramid(card1) || !checkCardPyramid(card2)) {
                return false
            }
        }

        val card1Value = card1.cardValue
        val card2Value = card2.cardValue

        //same cardValue false
        if (card1Value == card2Value) {
            return false
        }
        //check for two aces
        if (card1Value == CardValue.ACE || card2Value == CardValue.ACE) {
            return true
        }
        //because of the enum +2 it starts with two
        val card1IntValue = card1Value.ordinal + 2
        val card2IntValue = card2Value.ordinal + 2
        //both card most the sum of 15
        return card1IntValue + card2IntValue == 15
    }

    /**
     *  help function to check if the card are in the Pyramid
     *
     *  @param card The parameter Card is the card which you
     *  are check if the card are one card of the pyramid
     *  edges
     */
    private fun checkCardPyramid(card: Card): Boolean {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        val pyramid = currentGame.pyramid.cards
        //check all row if the card are first last card in the row
        for (i in 0 until pyramid.size) {
            if (pyramid[i].first() == card || pyramid[i].last() == card) {
                return true
            }
        }
        return false
    }

    /**
     * The private function return true if the second card is
     * from the pyramid edge
     *
     * @param card1  Card 1 is from reserve stack
     * @param card2  Card 2 is from pyramid
     *
     * @return The function returns true if one card
     * is from reserve stack and the other from pyramid edges
     * else false
     */
    private fun oneCardIsReserveStack(card1: Card, card2: Card): Boolean {
        val playingGame = this.rootService.currentGame
        val reserveStack = playingGame?.reserveStack!!.cards
        //no card in the reserve stack
        if (reserveStack.isEmpty()) return false
        //same card
        if (card1 == card2) return false
        //if card2 is from reserve change the order
        if (card2 == reserveStack[0]) {
            return oneCardIsReserveStack(card2, card1)
        }
        //check if card1 from reserve stack and card2 from pyramid
        if (card1 == reserveStack[0] && checkCardPyramid(card2)) {
            return true
        }
        return false
    }
}