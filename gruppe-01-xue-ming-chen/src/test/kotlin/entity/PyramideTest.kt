package entity

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals


/**
 *  The test Class for the class Pyramide ,to check all the tests
 */
class PyramideTest {
    //initialize Player1 and Player2
    private var player1 = Player("Xue")
    private var player2 = Player("Player")

    //Initialize list of cards
    private val aceOfSpades = Card(CardSuit.SPADES, CardValue.ACE)
    private val jackOfClubs = Card(CardSuit.CLUBS, CardValue.JACK)
    private val queenOfHearts = Card(CardSuit.HEARTS, CardValue.QUEEN)
    private val jackOfDiamonds = Card(CardSuit.DIAMONDS, CardValue.JACK)

    private val listCard = listOf(aceOfSpades, jackOfClubs, queenOfHearts, jackOfDiamonds)

    // Initialize both stack
    private val drawStack = CardStack()
    private val reserveStack = CardStack()

    //create CardSet
    private val cards = TestFunction().createCardSet()

    // Initialize pyramid
    private val pyramid = Pyramid(TestFunction().createPyramid(cards))


    //Initialize two test Pyramide variables
    private val pyramideWithDefault = Pyramide(player1, player2, cards, drawStack, reserveStack, pyramid)
    private val pyramideWithOutDefault = Pyramide(player1, player2, cards, drawStack, reserveStack, pyramid, 2)


    /**
     * Check if the attributes are all correct set.
     */
    @Test
    fun testOfAttributes() {
        //test for pyramideWithDefault
        assertEquals(player1, pyramideWithDefault.player1)
        assertEquals(player2, pyramideWithDefault.player2)
        assertEquals(cards, pyramideWithDefault.cards)
        assertEquals(drawStack, pyramideWithDefault.drawStack)
        assertEquals(reserveStack, pyramideWithDefault.reserveStack)
        assertEquals(pyramid, pyramideWithDefault.pyramid)
        //test for pyramidWithOutDefault
        assertEquals(player1, pyramideWithOutDefault.player1)
        assertEquals(player2, pyramideWithOutDefault.player2)
        assertEquals(cards, pyramideWithOutDefault.cards)
        assertEquals(drawStack, pyramideWithOutDefault.drawStack)
        assertEquals(reserveStack, pyramideWithOutDefault.reserveStack)
        assertEquals(pyramid, pyramideWithOutDefault.pyramid)
        assertEquals(2, pyramideWithOutDefault.currentPlayer)
    }

    /**
     * check if the default value or correct set
     */
    @Test
    fun testDefault() {
        assertEquals(false, pyramideWithDefault.opponentPassed)
        assertEquals(1, pyramideWithDefault.currentPlayer)
    }

    /**
     *  The test check if the amount of cards is correct
     */
    @Test
    fun testException() {
        assertDoesNotThrow { pyramideWithDefault }
        assertDoesNotThrow { pyramideWithOutDefault }
        assertThrows<IllegalArgumentException> {
            Pyramide(
                player1,
                player2,
                listCard,
                drawStack,
                reserveStack,
                pyramid
            )
        }
        assertThrows<IllegalArgumentException> {
            Pyramide(
                player1,
                player1,
                cards,
                drawStack,
                reserveStack,
                pyramid,
                3
            )
        }
    }
}