package service

import entity.*

/**
 *  This class is the [GameService] which make all action in the Background
 *
 *  The GameService class is making all background which are all done by
 *  the  System himself. Like to start a Game or énd the currentGame.
 *
 *  @property rootService the rootsService property is calling for the instance of
 *  the rootService which is the connection between the´service layer and the entity layer
 */
class GameService(private val rootService: RootService) : AbstractRefreshingService() {
    /**
     * startGame is a function to Start the game.
     *
     * startGame is a function which start the game. Which have two parameters
     * to set the 2 players name. it creates the  pyramid and the drawStack also
     * shuffel the card
     *
     *  @param player1Name The parameter player1Name is to initialize
     *  the first player name
     *  @param player2Name The parameter player2Name is to initialize
     *  the second player name
     *
     *  @throws IllegalArgumentException If one of the Strings are
     *  empty, then throws an exception
     */
    fun startGame(player1Name: String, player2Name: String) {
        require(player1Name != "" && player2Name != "")
        val cards = distributeCards()
        val newGame = Pyramide(
            player1 = Player(player1Name),
            player2 = Player(player2Name),
            cards = cards,
            reserveStack = CardStack(mutableListOf()),
            drawStack = CardStack(mutableListOf()),
            pyramid = createPyramid()//use a pseudo pyramid as constructor
        )
        rootService.currentGame = newGame
        val currentGame = rootService.currentGame
        //create the real pyramid and drawStack
        currentGame!!.pyramid =createPyramid()
        currentGame.drawStack =createDrawStack()
        flipCards()
        onAllRefreshables { refreshAfterStartGame() }
    }

    /**
     *   A private function which are revealing edges
     *   of the pyramid
     */
    fun flipCards() {
        val currentGame = this.rootService.currentGame
        val pyramid = currentGame?.pyramid?.cards
        checkNotNull(pyramid)
        check(pyramid.size <= 7)
        for (i in 0 until pyramid.size) {
            pyramid[i].first().isRevealed = true
            pyramid[i].last().isRevealed = true

        }
    }

    /**
     * The function end the Game.
     *
     * The Game end when one of two conditions are fulfilled.
     * Condition 1) The pyramid is empty()
     * Condition 2) two times sequentially passed
     *
     * @throws IllegalStateException Throws exception
     * if they are no game
     */
    fun endGame() {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        showResult()
    }

    /**
     *  The function changePlayer() change the player
     *  after a Player action like pass
     */
    fun changePlayer() {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        val currentPlayer = currentGame.currentPlayer
        //player 1 to player 2
        if (currentPlayer == 1) {
            currentGame.currentPlayer = 2
        }
        // player2 to player1
        if (currentPlayer == 2) {
            currentGame.currentPlayer = 1
        }
        //check there is no other currentPlayer index
        check(currentGame.currentPlayer == 1 || currentGame.currentPlayer == 2)
        onAllRefreshables { refreshAfterChangePlayer() }
    }

    /**
     * The private function creates the card deck of the game.
     *
     * The function create the card deck with 52 cards,
     * each card individuell and have combinations of
     * card suit and card value
     *
     *@return The function returns a list of cards,
     * for the game. Each card is individual
     *
     * @throws IllegalArgumentException The function throws an illegal
     * argument exception if the created Cards have not the amount
     * of 52 cards
     */
    private fun distributeCards(): MutableList<Card> {
        val cards = mutableListOf<Card>()
        val cardSuitValues = CardSuit.values()
        val cardValueValues = CardValue.values()
        //create the card set
        for (suitValue in cardSuitValues) {
            for (valueValue in cardValueValues) {
                cards.add(Card(suitValue, valueValue))
            }
        }
        //shuffle all cards
        cards.shuffle()
        //check if they are 52 cards
        check(cards.size == 52)
        return cards
    }

    /**
     * createPyramid is a function which create the Pyramid of the cards
     * for the game
     *
     * The private function create a pyramid with a row of 7.
     * In each row , they are the amount of card as the row
     * index  +1
     *
     * @return create the pyramid for the Start of the Game
     */
    private fun createPyramid(): Pyramid {
        val playingGame = this.rootService.currentGame
        var pyramidCards = playingGame?.cards
        //use first 28 cards of the cards
        pyramidCards = pyramidCards?.subList(0, 28)
        //case if they are no gae to create a pseudo pyramid
        if (pyramidCards.isNullOrEmpty()) {
            pyramidCards = distributeCards()
            pyramidCards = pyramidCards.subList(0, 28)
        }
        check(pyramidCards.size == 28)
        //k index for the pyramidCards
        var k = 0
        val pyramid = mutableListOf<MutableList<Card>>()
        //create the pyramid with two for loops
        for (i in 0..6) {
            pyramid.add(mutableListOf())
            for (j in 0 until i + 1) {
                pyramid[i].add(j, pyramidCards[k])
                k++
            }
        }
        // check for the form of the pyramid
        check(pyramid.size == 7)
        for (i in 0..6) {
            check(pyramid[i].size == i + 1)
        }
        return Pyramid(pyramid)
    }

    /**
     * The private function create the draw stack
     * for the beginning of the game.
     *
     * The DrawStack uses the last 24 cards of the cards
     * from the root service. Which are shuffel and added
     * the drawStack
     *
     * @return The function returns the DrawStack
     * himself as an Object of CardStack
     */
    private fun createDrawStack(): CardStack {
        val playingGame = this.rootService.currentGame
        //use the last 24 cards to create the draw stack
        val drawStack = playingGame?.cards?.subList(28, 52)?.toMutableList()
        checkNotNull(drawStack)
        check(drawStack.size == 24)
        return CardStack(drawStack)
    }

    /**
     * The function show the result in the end of the Game
     *
     * @sample "Winner"
     *         "Player1: 123 "
     *         "Player: 93"
     */
    private fun showResult() {
        var result :String
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        val player1 = currentGame.player1
        val player2 = currentGame.player2
        val player1Score = player1.score
        val player2Score = player2.score
        check(player1Score >= 0)
        check(player2Score >= 0)
        if (player1Score > player2Score) {
            println("Winner")
            println(player1.toString())
            println(player2.toString())
            result= "\uD83C\uDFC6 "+player1.name+" \uD83C\uDFC6"
        } else if (player1Score < player2Score) {
            println("Winner")
            println(player2.toString())
            println(player1.toString())
            result="\uD83C\uDFC6 "+player2.name+" \uD83C\uDFC6"
        } else {
            println("It`s a tie")
            println(player1.toString())
            println(player2.toString())
            result="It´s a tie"
        }
        onAllRefreshables { refreshAfterEndGame(result) }
    }

}