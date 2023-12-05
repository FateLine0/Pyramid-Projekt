package view

/**
 * The interface refreshable is for use
 * to call from the Service Layer the refreshes
 * for the vie Layer
 */
interface Refreshable {
    /**
     *  The function is called after Pass and
     *  refresh the Game
     */
    fun refreshAfterPass() {}

    /**
     *  The function is called after removePair and
     *  refresh the Game only if isValid is true
     *
     *  @param isValid Is the parameter true it refresh the
     *  scenes, else nothing happens
     */
    fun refreshAfterRemovePair(isValid: Boolean) {}

    /**
     *  The function is called after revealCard and
     *  refresh the Game only if isValid is true
     *
     *  @param isValid Is the parameter true it refresh the
     *  scenes, else nothing happens
     */
    fun refreshAfterRevealCard(isValid: Boolean) {}

    /**
     *  The function is called after changPlayer and
     *  refresh the Game
     */
    fun refreshAfterChangePlayer() {}

    /**
     *  The function is called after startGame and
     *  refresh the Game
     */
    fun refreshAfterStartGame() {}

    /**
     *  The function is called after endGame and
     *  refresh the Game
     *
     *  @param result the param result have the
     *  name of the winner
     */
    fun refreshAfterEndGame(result: String) {}

}