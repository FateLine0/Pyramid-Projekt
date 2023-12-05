package entity

/**
 * The [Player] of the game.
 *
 * The Game has two Player, which have a name and a score.
 *
 * @property name Every Player has a name as String.
 * @property score The Score is at beginning by 0 and can only go up.It save the score of a player.
 */
class Player(val name: String, var score: Int = 0) {
    init {
        require(name!="")
        require(score == 0)
    }
    /**
     * The Player as String
     *
     * @sample "Player1: 0"
     */
    override fun toString(): String = "$name: $score"
}