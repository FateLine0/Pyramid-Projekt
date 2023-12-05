package view

import service.RootService
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.visual.ColorVisual

/**
 *  The class [NewGameMenuScene] is a class
 *  which show the start of the application
 */
class NewGameMenuScene(private val rootService: RootService) : MenuScene(400, 600), Refreshable {

    /**
     *  @property headlineLabel The headline for the MenuScene
     */
    private val headlineLabel = Label(
        width = 300, height = 50, posX = 50, posY = 50,
        text = "Start New Game",
        font = Font(size = 22, fontWeight = Font.FontWeight.BOLD, fontStyle = Font.FontStyle.OBLIQUE)
    )

    /**
     * the player 1 Label is to show , which Textinput is for player 1
     */
    private val player1Label = Label(
        width = 100, height = 35,
        posX = 25, posY = 150,
        text = "Player 1:",
        font = Font(fontStyle = Font.FontStyle.OBLIQUE)
    )


    /**
     * @property player2Label the player 2 Label is to show , which Textinput is for player 1
     */
    private val player2Label = Label(
        width = 100, height = 35,
        posX = 25, posY = 240,
        text = "Player 2:",
        font = Font(fontStyle = Font.FontStyle.OBLIQUE)
    )

    /**
     *@property player1Input  The player1Input is to save the input for player and is
     *  one of the parameter for start Game
     */
    private val player1Input: TextField = TextField(
        width = 300, height = 35,
        posX = 50, posY = 195,
        text = "",
        font = Font(fontStyle = Font.FontStyle.OBLIQUE)
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = player2Input.text.isBlank() || this.text.isBlank()
        }
    }

    /**
     *  @property player2Input The player2Input is to save the input for player and is
     *  one of the parameter for start Game
     */
    private val player2Input: TextField = TextField(
        width = 300, height = 35,
        posX = 50, posY = 285,
        text = "",
        font = Font (fontStyle = Font.FontStyle.OBLIQUE)
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = player1Input.text.isBlank() || this.text.isBlank()
        }
    }

    /**
     * @property startButton The startButton is for to initialize the Game,
     * only can start both text has an input
     */
    private val startButton = Button(
        width = 140, height = 35,
        posX = 210, posY = 405,
        text = "Start",
        font = Font(fontStyle = Font.FontStyle.OBLIQUE)
    ).apply {
        isDisabled = true
        visual = ColorVisual(136, 221, 136)
        onMouseClicked = {
            rootService.gameService.startGame(
                player1Input.text.trim(),
                player2Input.text.trim()
            )
        }
    }

    /**
     * @property quitButton The quitButton quit the complete Application
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
            player1Label, player1Input,
            player2Label, player2Input,
            startButton, quitButton
        )
    }
}