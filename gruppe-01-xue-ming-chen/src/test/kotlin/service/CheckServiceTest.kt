package service

import entity.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * The check Service is for testting the function in the test service
 *
 */
class CheckServiceTest {
    private val testCard1 = Card(CardSuit.HEARTS, CardValue.FOUR)
    private val testCard2 = Card(CardSuit.HEARTS, CardValue.JACK)
    private val testCard3 = Card(CardSuit.HEARTS, CardValue.QUEEN)
    private val cardSet = TestFunction().createCardSet()
    private val pyramid = TestFunction().createPyramid(cardSet)

    /**
     *  This test function test the function checkCardChoice
     *  with all cases with pyramid cards
     */
    @Test
    fun testCheckCardChoicePyramid() {
        val testGame = RootService()

        testGame.gameService.startGame("Player1", "Rival")
        testGame.currentGame!!.pyramid = Pyramid(pyramid)
        val testPlayGame = testGame.checkService

        testCard1.isRevealed = true
        testCard2.isRevealed = true
        testCard3.isRevealed = true
        //two card from the pyramid
        val pyramid = testGame.currentGame?.pyramid!!.cards
        pyramid[0][0] = testCard2
        pyramid[1][0] = testCard1
        var twoCardsPyramid = testPlayGame.checkCardChoice(testCard1, testCard2)
        assertTrue(twoCardsPyramid)
        pyramid[1][0] = testCard3
        twoCardsPyramid = testPlayGame.checkCardChoice(testCard3, testCard2)
        assertFalse(twoCardsPyramid)

        //two unrevealed cards
        val card1Unrevealed = pyramid[2][1]
        val card2Unrevealed = pyramid[3][1]
        val twoUnrevealedCards = testPlayGame.checkCardChoice(card1Unrevealed, card2Unrevealed)
        assertFalse(twoUnrevealedCards)

        //oneCard is revealed
        var oneUnrevealedCards = testPlayGame.checkCardChoice(card1Unrevealed, testCard2)
        assertFalse(oneUnrevealedCards)
        oneUnrevealedCards = testPlayGame.checkCardChoice(testCard2, card1Unrevealed)
        assertFalse(oneUnrevealedCards)

        //test same card
        val testSameCard = testPlayGame.checkCardChoice(testCard3, testCard3)
        assertFalse(testSameCard)

        //two cards from the pyramid one is from the middle
        var pyramidOneMiddleCard = testPlayGame.checkCardChoice(card1Unrevealed, testCard2)
        assertFalse(pyramidOneMiddleCard)
        pyramidOneMiddleCard = testPlayGame.checkCardChoice(testCard2, card1Unrevealed)
        assertFalse(pyramidOneMiddleCard)

        //two cards from middle of the pyramid
        card1Unrevealed.isRevealed = true
        pyramid[3][1].isRevealed = true
        val pyramidTwoMiddleCard = testPlayGame.checkCardChoice(card1Unrevealed, card2Unrevealed)
        assertFalse(pyramidTwoMiddleCard)
        card1Unrevealed.isRevealed = false
        pyramid[3][1].isRevealed = false

        // test by aces
        val testAce1 = Card(CardSuit.CLUBS, CardValue.ACE)
        testAce1.isRevealed = true
        pyramid[0][0] = testAce1

        val testAce2 = Card(CardSuit.HEARTS, CardValue.ACE)
        testAce2.isRevealed = true
        pyramid[1][1] = testAce2
        var twoAces = testPlayGame.checkCardChoice(testAce1, testAce1)
        assertFalse(twoAces)
        pyramid[0][0] = testCard1
        pyramid[2][2] = testAce1
        twoAces = testPlayGame.checkCardChoice(testAce1, testAce2)
        assertFalse(twoAces)
        twoAces = testPlayGame.checkCardChoice(testAce2, testAce1)
        assertFalse(twoAces)
        val nonAceCard = Card(CardSuit.HEARTS, CardValue.FOUR)
        nonAceCard.isRevealed = true
        pyramid[0][0] = nonAceCard
        var oneAce = testPlayGame.checkCardChoice(nonAceCard, testAce2)
        assertTrue(oneAce)
        oneAce = testPlayGame.checkCardChoice(testAce2, nonAceCard)
        assertTrue(oneAce)

    }

    /**
     *  This test function test the function checkCardChoice
     *  with all cases with reserve Stack
     */
    @Test
    fun testCheckCardChoiceReserveStack() {
        val testGame = RootService()
        assertThrows<IllegalStateException> {
            testGame.checkService.checkCardChoice(testCard1, testCard2)
        }
        testGame.gameService.startGame("Player1", "Rival")
        testGame.currentGame!!.pyramid = Pyramid(pyramid)
        val testPlayGame = testGame.checkService

        testCard1.isRevealed = true
        testCard2.isRevealed = true
        testCard3.isRevealed = true

        val pyramid = testGame.currentGame?.pyramid!!.cards
        pyramid[0][0] = testCard2
        pyramid[1][0] = testCard1

        //one card from reserveStack and one Card form the Pyramid
        val reserveStack = testGame.currentGame!!.reserveStack.cards
        reserveStack.add(0, testCard1)
        var reserveStackCard = reserveStack[0]
        var oneCardReserveStack = testPlayGame.checkCardChoice(reserveStackCard, testCard2)
        assertTrue(oneCardReserveStack)
        oneCardReserveStack = testPlayGame.checkCardChoice(testCard2, reserveStackCard)
        assertTrue(oneCardReserveStack)
        reserveStack.add(0, testCard3)
        reserveStackCard = reserveStack[0]
        oneCardReserveStack = testPlayGame.checkCardChoice(reserveStackCard, testCard2)
        assertFalse(oneCardReserveStack)
        oneCardReserveStack = testPlayGame.checkCardChoice(testCard2, reserveStackCard)
        assertFalse(oneCardReserveStack)

        // one card is from stack and the other card is from the middle of the pyramid
        val card1Unrevealed = pyramid[2][1]
        card1Unrevealed.isRevealed = true
        pyramid[2][1].isRevealed = true
        var middleCardPyramidTest = testPlayGame.checkCardChoice(card1Unrevealed, reserveStackCard)
        assertFalse(middleCardPyramidTest)
        middleCardPyramidTest = testPlayGame.checkCardChoice(reserveStackCard, card1Unrevealed)
        assertFalse(middleCardPyramidTest)

        //test same card
        val testSameCard = testPlayGame.checkCardChoice(reserveStackCard, reserveStackCard)
        assertFalse(testSameCard)

        // test by aces
        val testAce1 = Card(CardSuit.CLUBS, CardValue.ACE)
        testAce1.isRevealed = true
        pyramid[0][0] = testAce1

        val testAce2 = Card(CardSuit.HEARTS, CardValue.ACE)
        testAce2.isRevealed = true
        pyramid[1][1] = testAce2
        val twoAces = testPlayGame.checkCardChoice(testAce1, testAce1)
        assertFalse(twoAces)
        pyramid[0][0] = testCard1
        pyramid[2][2] = testAce1
        val nonAceCard = Card(CardSuit.HEARTS, CardValue.FOUR)
        nonAceCard.isRevealed = true
        pyramid[0][0] = nonAceCard

        //two cards from reserveStack
        var twoCardsFromReverseStack = testPlayGame.checkCardChoice(reserveStack[1], reserveStackCard)
        assertFalse(twoCardsFromReverseStack)
        twoCardsFromReverseStack = testPlayGame.checkCardChoice(reserveStackCard, reserveStack[1])
        assertFalse(twoCardsFromReverseStack)

    }

}