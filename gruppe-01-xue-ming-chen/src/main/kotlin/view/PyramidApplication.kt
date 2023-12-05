package view

import service.RootService
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.DialogType

/**
 *  The class PyramidApplication is to start and initialize the complete game
 *
 * The class has all scene and is to run the Application.
 *
 */
class PyramidApplication : BoardGameApplication(windowTitle = "Pyramid"), Refreshable {
    /**
     *  @property rootService To have the use of the rootService for all scenes
     */
    private val rootService = RootService()

    /**
     *  @property gameScene THe GameTable is the GameScene which you play the Game
     */
    private val gameScene = GameTableScene(rootService).apply {
        val notValid = Dialog(
            DialogType.INFORMATION,
            title = "INFORMATION",
            header = "Reserve Stack is Empty",
            message = "They are no cards in the reserve stack. \uD83D\uDE2D \n" +
                    " Chose another Action\uD83D\uDE09",
        )
        reserveStack.apply {
            onMouseClicked={
                if(isEmpty()){
                    showDialog(notValid)
                }
            }
        }

    }

    /**
     *@property newGameMenuScene have the StartNewGameScene
     */
    private val newGameMenuScene = NewGameMenuScene(rootService).apply {
        //close the program by mouse click
        quitButton.onMouseClicked = {
            exit()
        }
    }

    /**
     * @property endGameMenuScene The variable make quitButton to make it quit the application and
     *  newGame button to start a new Game
     */

    private val endGameMenuScene = EndGameMenuScene(rootService).apply {
        //close the program
        quitButton.onMouseClicked = {
            exit()
        }
        //Starting new newGameMenuScne
        newGameButton.onMouseClicked = {
            this@PyramidApplication.showMenuScene(newGameMenuScene)
        }
    }


    init {
        rootService.addRefreshables(
            this,
            gameScene,
            newGameMenuScene,
            endGameMenuScene,
        )

        rootService.gameService.startGame("Player1", "Player2")
        this.showGameScene(gameScene)
        this.showMenuScene(newGameMenuScene)
        this.show()
    }

    /**
     *  The function hide the Menu scene.
     */
    override fun refreshAfterStartGame() {
        this.hideMenuScene()
    }

    /**
     * The function show a dialog if the valid is refresh after start
     *
     * @param isValid if the card are correct
     */
    override fun refreshAfterRemovePair(isValid: Boolean) {
        if (!isValid) {
            val notValid = Dialog(
                DialogType.INFORMATION,
                title = "INFORMATION",
                header = "Not the right sum",
                message = "The Cards, you have chosen, have not the sum 15 \n" +
                        "Choose Again \uD83D\uDE2D",
            )
            showDialog(notValid)
        }
    }

    /**
     * The function shows a dialog if they are no cards in the draw stack
     */
    override fun refreshAfterRevealCard(isValid: Boolean) {
        if (!isValid) {
            val notValid = Dialog(
                DialogType.INFORMATION,
                title = "INFORMATION",
                header = "Draw Stack is Empty",
                message = "They are no cards in the draw stack. \uD83D\uDE2D \n" +
                        " Chose another Action\uD83D\uDE09",
            )
            showDialog(notValid)
        }
    }

    /**
     *  The function show the endgame menu scene
     */
    override fun refreshAfterEndGame(result: String) {
        this.showMenuScene(endGameMenuScene)
    }

}