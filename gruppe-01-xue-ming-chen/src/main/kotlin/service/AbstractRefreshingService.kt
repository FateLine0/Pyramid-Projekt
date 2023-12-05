package service

import view.Refreshable

/**
 * The class abstract [AbstractRefreshingService] is a class that
 *  is used to refresh the game after an Action
 */
abstract class AbstractRefreshingService {
    /**
     * @property refreshables list all the refreshbles
     */
    private val refreshables = mutableListOf<Refreshable>()

    /**
     *  The function add all refreshable to the list
     */
    fun addRefreshable (newRefreshable : Refreshable) {
        refreshables += newRefreshable
    }

    /**
     * The function is use the refresh the game after an Action
     */
    fun onAllRefreshables(method: Refreshable.() -> Unit) =
        refreshables.forEach { it.method() }

}