package service

import entity.Card
import entity.*

/**
 *  The class [PlayerActionService] describe the action ,
 *  which the player can do.
 *
 *  The Player can do three Action by the Game, which are :
 *  1)Pass
 *  2)reveal Card
 *  3)removePair
 *
 *  @property rootService The root service property are the
 *  connection between the PlayActionService and RootService
 */
class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     *  The function [pass] describe the pass for player.
     *
     *  The pass function is an action caused by the
     *  player, if the opponent has passed and the current player
     *  pass it end the game. Else it changes the Player.
     */
    fun pass() {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        val opponentPassed = currentGame.opponentPassed
        //case if the opponent has passed
        if (opponentPassed) rootService.gameService.endGame()
        // if the opponent didn't pass
        else {
            currentGame.opponentPassed = true
            rootService.gameService.changePlayer()
            onAllRefreshables { refreshAfterPass() }
        }

    }

    /**
     * The function is a player action which reveal a
     * card from the draw stack to the reserve Stack
     */
    fun revealCard() {
        val currentGame = rootService.currentGame
        val gameService = rootService.gameService
        checkNotNull(currentGame)
        //check if draw has one or more cards
        val isValid = currentGame.drawStack.size > 0
        if (isValid) {
            val topCardReserveStack = currentGame.drawStack.cards.removeAt(0)
            topCardReserveStack.isRevealed = true
            currentGame.reserveStack.cards.add(0, topCardReserveStack)
            currentGame.opponentPassed = false
            gameService.changePlayer()
        }
        onAllRefreshables { refreshAfterRevealCard(isValid) }
    }

    /**
     *  The function removes a Pair.
     *
     *  The function remove a Pair by the following criterion:
     *  1.1 sum of the card 15 or
     *  1.2 one of them are an Ace (only One)
     *  2.1 Two Cards are from the edges or
     *  2.2 One Card are from the edges and the second one is
     *  from the top of the reserveStack
     *  by criterion 1.1 the player get 2 points
     *  by criterion 1.2 the player get 1 point
     */
    fun removePair(card1: Card, card2: Card) {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        val checkService = rootService.checkService
        val gameService = rootService.gameService
        //if both cards are a valid choose
        val isValid = checkService.checkCardChoice(card1, card2)
        val currentPlayer = currentGame.currentPlayer
        //initialize the currentPlayer player1
        var player = currentGame.player1
        val pyramid = currentGame.pyramid.cards
        //change the player to the second player if currentPlayer index is 2
        if (currentPlayer == 2) player = currentGame.player2
        if (isValid) {
            removeCards(card1, card2)
            //sum up the score
            if (card1.cardValue == CardValue.ACE || card2.cardValue == CardValue.ACE) {
                player.score++
            } else {
                player.score += 2
            }
            // opponentPassed false because not 2x in the row passed
            currentGame.opponentPassed = false
            //pyramid is empty ends game
            if (pyramid.isEmpty()) {
                gameService.endGame()
            } else {
                gameService.flipCards()
                gameService.changePlayer()
            }
        }
        onAllRefreshables { refreshAfterRemovePair(isValid) }
    }

    /**
     *  the private function removes the cards from the pyramid
     *  or reserve stack
     */
    private fun removeCards(card1: Card, card2: Card) {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        var reserveStackCard = card1
        val reserveStack = currentGame.reserveStack.cards
        val pyramid = currentGame.pyramid.cards
        //if the are cards in the reserve stack remove card from the pyramid
        if (rootService.checkService.checkCardChoice(card1, card2)) {
            if (reserveStack.size > 0) {
                if (card2 == reserveStack[0]) reserveStackCard = card2
                if (reserveStackCard == reserveStack[0]) {
                    reserveStack.remove(reserveStackCard)
                }
            }

            // remove card from the pyramid
            for (i in pyramid.indices.reversed()) {
                pyramid[i].remove(card2)
                pyramid[i].remove(card1)
                if (pyramid[i].isEmpty()) {
                    pyramid.removeAt(i)
                }
            }
        }
    }
}
