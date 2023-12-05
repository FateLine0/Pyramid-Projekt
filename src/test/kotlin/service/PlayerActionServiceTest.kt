package service

import entity.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * The PlayerActionServiceTest class is a test class for the playerActionService
 */
class PlayerActionServiceTest {

    private val cardSet = TestFunction().createCardSet()
    private val pyramid = TestFunction().createPyramid(cardSet)

    //initialize some testCard
    private val testCard1 = Card(CardSuit.CLUBS, CardValue.FOUR)
    private val testCard2 = Card(CardSuit.CLUBS, CardValue.JACK)
    private val testCard3 = Card(CardSuit.HEARTS, CardValue.ACE)

    /**
     *  The passTest function test the function passed.
     */
    @Test
    fun passTest() {
        val rootService = RootService()
        val testRefreshable = TestRefreshable()
        rootService.addRefreshable(testRefreshable)
        assertThrows<IllegalStateException> {
            rootService.playerActionService.pass()
        }
        rootService.gameService.startGame("Player1", "Player2")
        val testGame = rootService.currentGame
        assertFalse(testRefreshable.refreshAfterPassCalled)
        //first time passed
        rootService.playerActionService.pass()
        assertTrue(testGame!!.opponentPassed)
        assertTrue(testRefreshable.refreshAfterPassCalled)
        assertEquals(2, testGame.currentPlayer)
        //secondTime passed
        rootService.playerActionService.pass()
        testRefreshable.reset()
    }

    /**
     *  the Test function revealCardTest test the revealCard function
     */
    @Test
    fun revealCardTest() {
        val rootService = RootService()
        val testRefreshable = TestRefreshable()
        rootService.addRefreshable(testRefreshable)
        assertThrows<IllegalStateException> {
            rootService.playerActionService.revealCard()
        }
        rootService.gameService.startGame("Player1", "Player2")

        val requiredCard = rootService.currentGame!!.drawStack.cards[0]
        rootService.playerActionService.revealCard()
        val testGame = rootService.currentGame
        assertEquals(requiredCard, testGame!!.reserveStack.cards[0])
        assertNotEquals(requiredCard, testGame.drawStack.cards[0])
        assertTrue(testRefreshable.refreshAfterRevealCardCalled)
        testRefreshable.reset()

        //revealCard without Cards in the draw stack
        testGame.drawStack = CardStack()
        rootService.playerActionService.revealCard()
        assertFalse(testRefreshable.refreshAfterRevealCardCalled)
        testRefreshable.reset()
    }

    /**
     * The function test the function removePair only cards in pyramid
     *
     */
    @Test
    fun removePairTestPyramid() {
        testCard1.isRevealed = true
        testCard2.isRevealed = true
        testCard3.isRevealed = true

        val rootService = RootService()
        val testRefreshable = TestRefreshable()
        rootService.addRefreshable(testRefreshable)
        assertThrows<IllegalStateException> {
            rootService.playerActionService.removePair(testCard1, testCard2)
        }
        rootService.gameService.startGame("Player1", "Rival")

        rootService.currentGame!!.pyramid = Pyramid(pyramid)

        //two card from the pyramid
        rootService.playerActionService.removePair(testCard1, testCard2)
        assertEquals(7, pyramid.size)
        assertEquals(1, pyramid[1].size)
        assertEquals(3, pyramid[3].size)
        assertTrue(testRefreshable.refreshAfterRemovePairCalled)
        testRefreshable.reset()

        //case reserveStack size  not equals 0
        val reserveStack = rootService.currentGame!!.reserveStack.cards
        reserveStack.add(0, testCard3)

        //one card ace is Ace both card are from pyramid
        pyramid[0][0] = testCard1
        pyramid[1][0] = testCard2
        reserveStack.add(0, testCard3)
        rootService.playerActionService.removePair(testCard2, testCard1)
        pyramid[0][0] = testCard3
        pyramid[1][0] = testCard3
        assertNotEquals(testCard1, pyramid[0][0])
        assertNotEquals(testCard2, pyramid[1][0])
        assertFalse(reserveStack.isEmpty())
        assertEquals(5, pyramid.size)
        assertEquals(3, pyramid[0].size)
        assertTrue(testRefreshable.refreshAfterRemovePairCalled)
        testRefreshable.reset()

        reserveStack.removeAt(0)
        //the same card
        rootService.playerActionService.removePair(pyramid[0][0], pyramid[0][0])
        assertFalse(testRefreshable.refreshAfterRemovePairCalled)
        testRefreshable.reset()

        //Last card in the pyramid
        val lastCard = mutableListOf(mutableListOf(testCard1))
        rootService.currentGame!!.pyramid.cards = lastCard
        rootService.playerActionService.removePair(testCard3, testCard1)
        val endPyramid = rootService.currentGame!!.pyramid.cards
        rootService.playerActionService.removePair(testCard3, testCard1)
        assertTrue(reserveStack.isEmpty())
        assertTrue(endPyramid.isEmpty())
        assertTrue(testRefreshable.refreshAfterRemovePairCalled)
        testRefreshable.reset()
    }

    /**
     * The removePairTestReserveStack test all cases
     * that included the reserve Stack
     */
    @Test
    fun removePairTestReserveStack() {
        testCard1.isRevealed = true
        testCard2.isRevealed = true
        testCard3.isRevealed = true

        val rootService = RootService()
        rootService.gameService.startGame("Player1", "Rival")
        val testRefreshable = TestRefreshable()
        rootService.addRefreshable(testRefreshable)

        rootService.currentGame!!.pyramid = Pyramid(pyramid)

        //one Ace card from reserveStack and one of Pyramid
        val reserveStack = rootService.currentGame!!.reserveStack.cards
        pyramid[0][0] = testCard1
        pyramid[1][0] = testCard2
        reserveStack.add(0, testCard3)
        rootService.playerActionService.removePair(testCard1, testCard3)
        assertNotEquals(testCard1, pyramid[0][0])
        assertTrue(reserveStack.isEmpty())
        assertEquals(6, pyramid.size)
        assertEquals(1, pyramid[0].size)
        assertTrue(testRefreshable.refreshAfterRemovePairCalled)
        testRefreshable.reset()

        //one card ace is Ace one card is from pyramid and the other from reserveStack
        pyramid[0][0] = testCard1
        reserveStack.add(0, testCard3)
        rootService.playerActionService.removePair(testCard3, testCard1)
        pyramid[0][0] = testCard2
        assertNotEquals(testCard1, pyramid[0][0])
        assertTrue(reserveStack.isEmpty())
        assertEquals(5, pyramid.size)
        assertEquals(3, pyramid[0].size)
        assertTrue(testRefreshable.refreshAfterRemovePairCalled)
        testRefreshable.reset()
    }
}