package entity

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.*

/**
 * Test the class Player
 */
class PlayerTest {

    //testPlayer initialize
    private var player1 = Player("Xue")

    /**
     *  the Test if the Constructor have the right value of arguments
     */
    @Test
    fun testAttribute() {
        //Player1 all Parameters
        assertEquals("Xue", player1.name)

    }

    /**
     * The test if the default value for score is correct;
     */
    @Test
    fun testDefault() {
        assertEquals(0, player1.score)
    }

    /**
     *  The test to test if you can set the score.
     */
    @Test
    fun testSetScore() {
        player1.apply { score += 1 }
        assertEquals(1, player1.score)
    }

    /**
     *  The test if the String as a Player are correct.
     */
    @Test
    fun testToString() {
        assertEquals("Xue: 0", player1.toString())
    }
    /**
     * The function test if the Name of the is not empty
     */
    @Test
    fun testPlayerNameNotEmpty(){
        assertThrows<IllegalArgumentException>{ Player("")}
        assertDoesNotThrow { player1 }
    }

    /**
     * The Test function test if score initialization are equals 0
     *
     */
    @Test
    fun testScoreNotEquals0(){
        assertThrows<IllegalArgumentException> { Player("Player1",-12)}
        assertThrows<IllegalArgumentException>{Player("Player2",12)}
        assertDoesNotThrow { player1 }
        assertDoesNotThrow{Player("Player2",0)}
    }

}