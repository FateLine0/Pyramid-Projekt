package entity


import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

/**
 *  The test class for the pyramid class
 */
class PyramidTest {
    //TestCards
    private val aceOfSpades = Card(CardSuit.SPADES, CardValue.ACE)
    private val jackOfClubs = Card(CardSuit.CLUBS, CardValue.JACK)
    private val queenOfHearts = Card(CardSuit.HEARTS, CardValue.QUEEN)

    //initialize test row for the pyramid
    private val pyramidRow1 = mutableListOf(aceOfSpades)
    private val pyramidRow2 = mutableListOf(jackOfClubs, queenOfHearts)


    //createTheCardSet
    private val cards = TestFunction().createCardSet()

    //pyramid
    private val pyramid = TestFunction().createPyramid(cards)
    private val testPyramidFull = Pyramid(pyramid)

    /**
     * Check if the Constructor are right placed
     */
    @Test
    fun testException() {
        assertThrows<IllegalArgumentException> {
            Pyramid(mutableListOf(pyramidRow1, pyramidRow2))
        }
        assertDoesNotThrow { testPyramidFull }
    }

    /**
     *Check if the String as described as by the documentation
     * in the class pyramid
     */
    @Test
    fun testToString() {
        assertEquals(
            "[♣2]\n" +
                    "[♣3, ♣4]\n" +
                    "[♣5, ♣6, ♣7]\n" +
                    "[♣8, ♣9, ♣10, ♣J]\n" +
                    "[♣Q, ♣K, ♣A, ♠2, ♠3]\n" +
                    "[♠4, ♠5, ♠6, ♠7, ♠8, ♠9]\n" +
                    "[♠10, ♠J, ♠Q, ♠K, ♠A, ♥2, ♥3]",
                    testPyramidFull.toString()
        )
    }


    private val row3 = mutableListOf(aceOfSpades, jackOfClubs, queenOfHearts)
    /**
     * Check if the new row is on the right place
     */
    @Test
    fun testModifyRow() {
        testPyramidFull.cards.add(row3)
        assertEquals(row3, testPyramidFull.cards[7])
        assertEquals(8, testPyramidFull.cards.size)
        assertEquals(row3, testPyramidFull.cards.removeAt(7))
        assertEquals(7, testPyramidFull.cards.size)
    }

    /**
     * Check if the Cards are all correct in the row
     */
    @Test
    fun testCheckModifyCardsInTheRow() {
        assertEquals(Card(CardSuit.CLUBS,CardValue.FOUR), testPyramidFull.cards[1].removeAt(1))
        assertEquals(1, testPyramidFull.cards[1].size)
        testPyramidFull.cards[1].add(1, queenOfHearts)
        assertEquals(queenOfHearts, testPyramidFull.cards[1][1])
        assertEquals(7, testPyramidFull.cards.size)
    }


}