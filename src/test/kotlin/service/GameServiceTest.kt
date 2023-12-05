package service

import entity.Card
import entity.CardSuit
import entity.CardValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import kotlin.test.*

/**
 *  This is the test class for the GameService class
 */
class GameServiceTest {

    /**
     * It tests the  function startGame which have two
     * string Parameters
     */
    @Test
    fun startGameTest() {

        //initialize test variables
        val testGame = RootService()
        val testRefreshable = TestRefreshable()
        testGame.addRefreshable(testRefreshable)
        val noPlayerName = ""
        //all combination with void string
        assertThrows<IllegalArgumentException> {
            testGame.gameService.startGame(noPlayerName, noPlayerName)
        }
        assertFalse(testRefreshable.refreshAfterStartGameCalled)
        assertThrows<IllegalArgumentException> {
            testGame.gameService.startGame("Player1", noPlayerName)
        }
        assertThrows<IllegalArgumentException> {
            testGame.gameService.startGame(noPlayerName, "Player2")
        }

        //Test of the game
        assertNull(testGame.currentGame)
        testGame.gameService.startGame("Player1", "Player2")
        assertTrue(testRefreshable.refreshAfterStartGameCalled)

        //test the constructor
        //Player
        assertEquals("Player1", testGame.currentGame!!.player1.name)
        assertEquals("Player2", testGame.currentGame!!.player2.name)
        assertEquals(0, testGame.currentGame!!.player2.score)
        assertEquals(0, testGame.currentGame!!.player1.score)

        //reserveStack
        assertEquals(0, testGame.currentGame!!.reserveStack.size)

        //drawStack
        val drawStack = testGame.currentGame!!.drawStack
        assertEquals(24, testGame.currentGame!!.drawStack.size)
        assertEquals(drawStack, testGame.currentGame!!.drawStack)

        //cards
        val cards = testGame.currentGame!!.cards
        assertEquals(52, cards.size)
        //Test of not the equals cards
        for (i in 0..cards.size - 2) {
            for (j in i + 1 until cards.size) {
                assertNotEquals(cards[i], cards[j])
            }
        }
        //pyramid
        val pyramid = testGame.currentGame!!.pyramid.cards
        assertEquals(7, pyramid.size)
        //Test of the Structure
        for (i in 0..6) {
            assertEquals(i + 1, pyramid[i].size)
        }
        testRefreshable.reset()

    }

    /**
     * The test function test flipCards
     */
    @Test
    fun flipCardsTest() {
        //initialize test variables
        val testGame = RootService()
        assertNull(testGame.currentGame)
        assertThrows<IllegalStateException> { (testGame.gameService.flipCards()) }
        assertNull( testGame.currentGame )
        assertNull(testGame.currentGame?.pyramid)
        assertNull(testGame.currentGame?.pyramid?.cards)
       // assertThrows<NullPointerException> { testGame.currentGame?.pyramid }
        testGame.gameService.startGame("Player1", "Player2")

        val pyramid = testGame.currentGame!!.pyramid.cards
        //checking for the revealing cards
        //Test case the edge are revealed
        for (i in 0..6) {
            assertTrue(pyramid[i].first().isRevealed)
            assertTrue(pyramid[i].last().isRevealed)
        }

        /*Test case for the cards in the middle of
        *  the row without first and last card
        * are not revealed
        */
        assertEquals(7, pyramid.size)
        for (i in 0..6) {
            assertEquals(i + 1, pyramid[i].size)
            for (j in 0..i) {
                if (pyramid[i][j] != pyramid[i].first() && pyramid[i][j] != pyramid[i].last()) {
                    assertEquals(false, pyramid[i][j].isRevealed)
                }
            }
        }
        pyramid.add(mutableListOf(Card(CardSuit.CLUBS, CardValue.ACE)))
        assertThrows<IllegalStateException> {
            testGame.gameService.flipCards()
        }
    }

    /**
     *  The test function test the function endGame
     */
    @Test
    fun testEndGame() {
        val testGame = RootService()
        val testRefreshable = TestRefreshable()
        testGame.addRefreshable(testRefreshable)
        assertThrows<IllegalStateException> { testGame.gameService.endGame() }
        assertFalse(testRefreshable.refreshAfterStartGameCalled)
        testGame.gameService.startGame("Player1", "Player2")
        val currentGame = testGame.currentGame
        val player1 = currentGame!!.player1
        //player1 has negative values
        player1.score = -12
        assertThrows<IllegalStateException> { testGame.gameService.endGame() }


        val player2 = currentGame.player2
        //player2 has negative values
        player1.score = 0
        player2.score = -12
        assertThrows<IllegalStateException> { testGame.gameService.endGame() }
        //player2 won
        player2.score = 12
        testGame.gameService.endGame()
        assertTrue(testRefreshable.refreshAfterEndGameCalled)
        testRefreshable.reset()

        //player1 won
        player1.score = 12
        player2.score = 0
        testGame.gameService.endGame()
        assertTrue(testRefreshable.refreshAfterEndGameCalled)
        testRefreshable.reset()

        //tie
        player1.score = 0
        testGame.gameService.endGame()
        assertTrue(testRefreshable.refreshAfterEndGameCalled)
        testRefreshable.reset()
    }

    /**
     *  The function test the method changePlayer.
     *
     *  The Test checks if the property currentPlayer are correct set
     *  and the set property by 1 to 2 and 2 to 1
     */
    @Test
    fun testChangePlayer() {
        val testGame = RootService()
        val testRefreshable = TestRefreshable()
        testGame.addRefreshables(testRefreshable)
        assertThrows<IllegalStateException> {
            testGame.gameService.changePlayer()
        }
        assertFalse(testRefreshable.refreshAfterChangePlayerCalled)
        testGame.gameService.startGame("Player1", "Player2")
        val currentGame = testGame.currentGame
        testGame.gameService.changePlayer()
        assertTrue(testRefreshable.refreshAfterChangePlayerCalled)
        testRefreshable.reset()
        var currentPlayer = currentGame!!.currentPlayer
        assertEquals(2, currentPlayer)

        testGame.gameService.changePlayer()
        assertTrue(testRefreshable.refreshAfterChangePlayerCalled)
        testRefreshable.reset()
        currentPlayer = currentGame.currentPlayer
        assertEquals(1, currentPlayer)

        currentGame.currentPlayer = 3
        assertThrows<IllegalStateException> {
            testGame.gameService.changePlayer()
        }
    }

}