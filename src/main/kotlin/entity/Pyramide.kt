package entity

/**
 *The class represented the [Pyramid] game
 *
 * The class represented the [Pyramid] game,which have two Player , a draw stack , a reservestack,
 * a flag,  where the opponent passed, and Player index as currentPlayer, cards of the game
 * and the pyramid as a card pyramid.
 *
 * @property player1  the first player of game
 * @property player2  The second player of game
 * @property cards  The List of the cards in the game (amount: 52)
 * @property drawStack  The Stack which you can draw from and put it on the top of reverse stack
 * @property reserveStack The Stack which you can choose the top of the Card and you use to sum
 *                         with one of the card from the Pyramid
 * @property pyramid The card Pyramid with row(1 to 7) and with exact numbers of card in the row as index+1.
 * @property currentPlayer A flag which tell which Player is active
 *                          (currentPlayer == 1 for player1 and currentPlayer == 2 for player2 )
 */
data class Pyramide(
    val player1: Player,
    val player2: Player,
    var cards: List<Card>,
    var drawStack: CardStack,
    var reserveStack: CardStack,
    var pyramid: Pyramid,
    var currentPlayer: Int = 1,
) {
    /**
     *  @property opponentPassed The Property opponentPassed gives
     *  you the actual state, if the opponent hs Passed.
     */
    var opponentPassed: Boolean = false

    init {
        require(cards.size == 52)
        require(
            currentPlayer == 1 ||
                    currentPlayer == 2
        )
    }
}

