package entity


import org.junit.jupiter.api.Test
import kotlin.test.*

/**
 * The test class for the class Cards from the entity layer
 *
 */
class CardTest {
    private val aceOfSpades = Card(CardSuit.SPADES, CardValue.ACE)
    private val jackOfClubs = Card(CardSuit.CLUBS, CardValue.JACK)
    private val queenOfHearts = Card(CardSuit.HEARTS, CardValue.QUEEN)
    private val jackOfDiamonds = Card(CardSuit.DIAMONDS, CardValue.JACK)


    /**
     * The test tests if the attribute suit is correct implemented and initialize
     */
    @Test
    fun testAttributeCardSuit() {
        assertEquals(CardSuit.SPADES, aceOfSpades.cardSuit)
        assertEquals(CardSuit.CLUBS, jackOfClubs.cardSuit)
        assertEquals(CardSuit.HEARTS, queenOfHearts.cardSuit)
        assertEquals(CardSuit.DIAMONDS, jackOfDiamonds.cardSuit)
    }

    /**
     * The test tests if the attribute value is correct implemented and initialize
     */

    @Test
    fun testAttributeCardValue() {
        assertEquals(CardValue.ACE, aceOfSpades.cardValue)
        assertEquals(CardValue.JACK, jackOfClubs.cardValue)
        assertEquals(CardValue.QUEEN, queenOfHearts.cardValue)
        assertEquals(CardValue.JACK, jackOfDiamonds.cardValue)
    }

    /**
     * Check for the Case of isReveal
     */
    @Test
    fun testDefault() {
        assertEquals(false, aceOfSpades.isRevealed)
    }

    /**
     * Check for the if isRevealed is setable
     */
    @Test
    fun testSetisReveald(){
        aceOfSpades.isRevealed = true
        assertTrue(aceOfSpades.isRevealed)
    }
    /**
     * The test check for the toString method if the method are correct
     */
    @Test
    fun testToString() {
        assertEquals("♠A", aceOfSpades.toString())
        assertEquals("♣J", jackOfClubs.toString())
        assertEquals("♥Q", queenOfHearts.toString())
        assertEquals("♦J", jackOfDiamonds.toString())
    }

    /**
     * check if card value are in the order in the Pyramid are correct
     */
    @Test
    fun testCompareTo() {
        assertTrue(jackOfDiamonds < queenOfHearts)
        assertFalse(jackOfClubs < jackOfDiamonds)
        assertTrue(jackOfClubs <= jackOfDiamonds)
    }

    /**
     *  check if the card are the same Card or have the both cards are equals,
     *  because they have the same CardSuit/CardValue combination.
     * == is for Equal and === is for same
     */
    @Test
    fun testEquals(){
         val aceOfSpades2 = Card(CardSuit.SPADES,CardValue.ACE)
        assertEquals(aceOfSpades2,aceOfSpades)
        assertNotSame(aceOfSpades,aceOfSpades2)
    }

}