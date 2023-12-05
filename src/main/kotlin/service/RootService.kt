package service

import entity.Pyramide
import view.Refreshable

/**
 * The [RootService] describe the class service
 * which are have connection to pyramide and the
 * entity Layer
 */
class RootService {

    /**
     * @property playerActionService The property playerActionService, save and use all the method of the class
     */
    val playerActionService = PlayerActionService(this)
    /**
     * @property gameService The property gameService, save and use all the method of the class
     */
    val gameService = GameService(this)
    /**
     * @property checkService The property checkService , save and use all the method of the class
     */
    val checkService = CheckService(this)

    /**
     *  @property currentGame The property currentGame saves the current
     *  Game ,if its null they are no game
     */
    var currentGame : Pyramide ? = null

    /**
     * Adds the provided [newRefreshable] to all services connected
     * to this root service
     */
    fun addRefreshable(newRefreshable: Refreshable) {
        gameService.addRefreshable(newRefreshable)
        playerActionService.addRefreshable(newRefreshable)
    }

    /**
     * Adds the provided [newRefreshables] to all services connected
     * with all parameters add all refeshables to the method
     */
    fun addRefreshables(vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }

}