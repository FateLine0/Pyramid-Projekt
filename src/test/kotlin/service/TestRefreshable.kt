package service

import view.Refreshable

/**
 *  The Class Test refreshables are used that the refreshables
 *  all can be tested
 *
 *  If the specific Refreshable is called the test function set
 *  the called value from false to true
 *
 *  @property refreshAfterPassCalled
 *  @property refreshAfterRevealCardCalled
 *  @property refreshAfterRemovePairCalled
 *  @property refreshAfterChangePlayerCalled
 *  @property refreshAfterStartGameCalled
 *  @property refreshAfterEndGameCalled
 */
class TestRefreshable : Refreshable {
    var refreshAfterPassCalled = false
        private set

    var refreshAfterRemovePairCalled = false
        private set

    var refreshAfterRevealCardCalled = false
        private set

    var refreshAfterChangePlayerCalled = false
        private set

    var refreshAfterStartGameCalled = false
        private set

    var refreshAfterEndGameCalled = false
        private set

    /**
     *  The function reset all the called valued to
     *  false
     */
    fun reset() {
        refreshAfterPassCalled = false
        refreshAfterRevealCardCalled = false
        refreshAfterRemovePairCalled = false
        refreshAfterChangePlayerCalled = false
        refreshAfterStartGameCalled = false
        refreshAfterEndGameCalled = false
    }

    /**
     *  The function [refreshAfterPass] is set
     *  the called value to true. Only for the tests
     */
    override fun refreshAfterPass() {
        refreshAfterPassCalled = true
    }

    /**
     *  The function [refreshAfterRevealCardCalled] is set
     *  the called value to true. Only for the tests
     */
    override fun refreshAfterRemovePair(isValid: Boolean) {
        if(isValid)
        refreshAfterRemovePairCalled = true
    }

    /**
     *  The function [refreshAfterRemovePairCalled] is set
     *  the called value to true. Only for the tests
     */
    override fun refreshAfterRevealCard(isValid: Boolean) {
        if(isValid)
        refreshAfterRevealCardCalled = true
    }

    /**
     *  The function [refreshAfterChangePlayer] is set
     *  the called value to true. Only for the tests
     */
    override fun refreshAfterChangePlayer() {
        refreshAfterChangePlayerCalled = true
    }

    /**
     *  The function [refreshAfterStartGame] is set
     *  the called value to true. Only for the tests
     */
    override fun refreshAfterStartGame() {
        refreshAfterStartGameCalled = true
    }

    /**
     *  The function [refreshAfterEndGame] is set
     *  the called value to true. Only for the tests
     */
    override fun refreshAfterEndGame(result: String) {
        refreshAfterEndGameCalled = true
    }

}