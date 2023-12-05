package view

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/**
 *  The class EndGameMenuScene visualize with
 *  the winner and all Player score, also if it's a tie with a tie.
 *  You can make a new game or quit application
 *
 *  @property rootService The property rootService is for to
 *  get the current game information
 */
class EndGameMenuScene(private val rootService: RootService) : MenuScene(400, 600), Refreshable {
    /**
     * @property headlineLabel The headlineLabel is to show the Winner , if not then
     *  It's a tie
     */
    private val headlineLabel = Label(
        width = 300,
        height = 50,
        posX = 50,
        posY = 50,
        text = "Winner",
        font = Font(size = 22, fontWeight = Font.FontWeight.BOLD, fontStyle = Font.FontStyle.OBLIQUE)
    )

    /**
     * @property player1Name The label show the player 1 name
     */
    private val player1Name = Label(
        width = 100, height = 35,
        posX = 75, posY = 175,
        text = "Player 1:",
        font = Font(size = 16, fontStyle = Font.FontStyle.OBLIQUE)
    )

    /**
     *@property player2Name The label show the player 2 name
     */
    private val player2Name = Label(
        width = 100, height = 35,
        posX = 225, posY = 175,
        text = "Player 2:",
        font = Font(size = 16, fontStyle = Font.FontStyle.ITALIC)
    )

    /**
     * @property player1Score The label shows the score from player 1
     */
    private val player1Score = Label(
        width = 100, height = 35,
        posX = 75, posY = 240,
        text = "35",
        font = Font(fontStyle = Font.FontStyle.ITALIC)
    )

    /**
     * @property player2Score The label shows the score from player 2
     */
    private val player2Score = Label(
        width = 100, height = 35,
        posX = 225, posY = 240,
        text = "3",
        font = Font(fontStyle = Font.FontStyle.OBLIQUE)
    )

    /**
     * @property newGameButton The newGameButton is for to start a new game
     */
    val newGameButton = Button(
        width = 140, height = 35,
        posX = 210, posY = 405,
        text = "New",
        font = Font(fontStyle = Font.FontStyle.OBLIQUE)
    ).apply {
        visual = ColorVisual(136, 221, 136)
    }

    /**
     *  @property quitButton The button is for quit the game
     */
    val quitButton = Button(
        width = 140, height = 35,
        posX = 50, posY = 405,
        text = "Quit",
        font = Font(fontStyle = Font.FontStyle.OBLIQUE)
    ).apply {
        visual = ColorVisual(221, 136, 136)
    }

    init {
        opacity = .5
        addComponents(
            headlineLabel,
            player1Name, player2Name,
            player1Score, player2Score,
            newGameButton, quitButton
        )
    }

    /**
     * The refresh After Endgame show in the menuScene the winner
     * and the score of the players, if it is a tie show it's a tie and
     * the players scores
     *
     * @param result The result have store the winner or if there is not a winner
     * store "It's a tie"
     */
    override fun refreshAfterEndGame(result: String) {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame) { "No Game found" }
        val player1 = currentGame.player1
        val player2 = currentGame.player2
        headlineLabel.text = result
        player1Name.text = player1.name
        player1Score.text = player1.score.toString()
        player2Name.text = player2.name
        player2Score.text = player2.score.toString()
    }
}
