package entity

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

/**
 * The class is for test for the Object Card from the entity layer
 */
class CardStackTest {

    //card attribute
    private val aceOfSpades = Card(CardSuit.SPADES, CardValue.ACE)
    private val jackOfClubs = Card(CardSuit.CLUBS, CardValue.JACK)
    private val queenOfHearts = Card(CardSuit.HEARTS, CardValue.QUEEN)
    private val jackOfDiamonds = Card(CardSuit.DIAMONDS, CardValue.JACK)

    //example card stack for the test
    private var cardStack = CardStack(mutableListOf(aceOfSpades, jackOfClubs, queenOfHearts))

    //create the complete card set
    private val cardSet = TestFunction().createCardSet()

    /**
     * The Test tests, if the CardStack is Correct implemented.
     */
    @Test
    fun testAttributes() {
        assertEquals(mutableListOf(aceOfSpades, jackOfClubs, queenOfHearts), cardStack.cards)
    }

    //Require after added Card to CardStack
    private val testCardStackAddRequire =
        CardStack(mutableListOf(jackOfDiamonds, aceOfSpades, jackOfClubs, queenOfHearts))

    /**
     * The Test is to check if you can modify the List
     * and if the size change automatically
     */
    @Test
    fun testModifyCardStack() {
        cardStack.cards.add(0, jackOfDiamonds)
        assertEquals(testCardStackAddRequire.cards, cardStack.cards)
        assertEquals(4, cardStack.size)
        assertEquals(jackOfDiamonds, cardStack.cards.removeAt(0))
        assertEquals(3, cardStack.size)
    }

    /**
     * check if the default constructor are right
     */
    @Test
    fun testDefault() {
        val stack = CardStack()
        assertEquals(0, stack.size)
    }

    /**
     *  test the toString if the method work correct
     */
    @Test
    fun testToString() {
        assertEquals("[♠A, ♣J, ♥Q]", cardStack.toString())
    }

    /**
     *  Thew test if the exception is calling we have an oversize
     */
    @Test
    fun testSizeOfCardStack() {
        assertThrows<IllegalArgumentException> {
            CardStack(cardSet)
        }
        assertDoesNotThrow{
            CardStack(cardSet.subList(28,52))
        }
    }
}