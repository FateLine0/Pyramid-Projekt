package view

import entity.Card
import entity.Pyramid
import service.CardImageLoader
import service.RootService
import tools.aqua.bgw.animation.FlipAnimation
import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.animation.SequentialAnimation
import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.*
import java.awt.Color

/**
 *  This class represented the game table which have the pyramid
 *  and the two stacks
 *
 *  @property rootService The rootService stored have stored the current Game
 */
class GameTableScene(private val rootService: RootService) : BoardGameScene(1920, 1080), Refreshable {
    /**
     * @property card1 Stored temporary first Card for removePair methode
     * @property card2 Stored temporary second Card for removePair methode
     */
    private var card1: Card? = null
    private var card2: Card? = null


    /**
     * Only use for the Pass animation
     */
    private val passLabel = Label(
        width = 200,
        height = 100,
        posX = -300,
        posY = 500,
        text = "PASS",
        font = Font(size = 60, fontStyle = Font.FontStyle.OBLIQUE, color = Color.red),
    )

    /**
     * Label for player 1
     */
    private val player1Label = Label(
        width = 200,
        height = 100,
        posX = 1200,
        posY = 50,
        text = "Player 1:",
        font = Font(size = 40, fontStyle = Font.FontStyle.OBLIQUE)
    )

    /**
     *  label for Player 2
     */
    private val player2Label = Label(
        width = 200,
        height = 100,
        posX = 1600,
        posY = 50,
        text = "Player 2:",
        font = Font(size = 40, fontStyle = Font.FontStyle.OBLIQUE)
    )

    /**
     *  Passbutton to trigger the action pass
     */
    private val passButton = Button(
        width = 200, height = 100,
        posX = 1400, posY = 880,
        text = "Pass",
        font = Font(size = 40, fontStyle = Font.FontStyle.OBLIQUE, color = Color.white)
    ).apply {
        visual = ColorVisual(221, 136, 136)
        onMouseClicked = {
            rootService.currentGame?.let { _ ->
                rootService.playerActionService.pass()
            }
        }
    }

    /**
     *  Visualize form of the reserve stack for the pyramid game
     */
     val reserveStack: CardStack<CardView> = CardStack(
        height = 200,
        width = 130,
        posX = 1250,
        posY = 400,
        visual = CompoundVisual(
            ColorVisual(138, 141, 145),
            TextVisual(
                "ReserveStack",
                Font(color = Color.white)
            )
        )
    )

    /**
     *  Visualize form of the draw stack for the pyramid game
     */
    private val drawStack: CardStack<CardView> = CardStack<CardView>(
        height = 200,
        width = 130,
        posX = 1650,
        posY = 400,
    ).apply {
        visual = CompoundVisual(
            ColorVisual(138, 141, 145),
            TextVisual(
                "DrawStack",
                Font(color = Color.white)
            )
        )
        onMouseClicked = {
            rootService.currentGame?.let { _ ->
                rootService.playerActionService.revealCard()
            }
        }
    }

    /**
     *  Create an area for the pyramid of cards
     */
    private val pyramid: Area<CardView> = Area(
        posX = 50,
        posY = 50,
        width = 960,
        height = 980
    )

    /**
     *  To map the visual cards to the cards in the entity model
     */
    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    /**
     *  For the card in the game to initialize CardView
     *
     *  @param card The card which you are visualize
     *  @param stackView The stack which you are add the cards
     *  @param cardImageLoader the visual the right card suit and card value
     *  from the card
     */
    private fun initializeCardView(
        card: Card,
        stackView: CardStack<CardView>,
        cardImageLoader: CardImageLoader
    ) {
        val cardView = CardView(
            height = 200,
            width = 130,
            front = ImageVisual(cardImageLoader.frontImageFor(card.cardSuit, card.cardValue)),
            back = ImageVisual(cardImageLoader.backImage)
        )
        cardMap.add(card to cardView)
        stackView.push(cardView)
    }

    /**
     *  The function initializeRevealCards shows the front of the revealed Card.
     *
     *  The function shows the front of the revealing Card, also it has the feature
     *  if the Mouse entered the card it makes the blue layer on the card and
     *  by clicked the Card it mark it red
     *
     *  @param cardView The cardView parameter is give the Function the revealing Card
     */
    private fun initializeRevealCards(cardView: CardView) {
        val chooseCard = cardMap.backward(cardView)
        val cardVisual = CardImageLoader().frontImageFor(chooseCard.cardSuit, chooseCard.cardValue)
        cardView.apply {
            showFront()
            onMouseEntered = {
                //To make if entered make its blue
                if (chooseCard != card1) {
                    frontVisual = CompoundVisual(
                        ImageVisual(cardVisual),
                        ColorVisual.BLUE.apply {
                            transparency = 0.2
                        }
                    )
                }
            }
            onMouseExited = {
                //Undo the entered featured
                if (chooseCard != card1 && chooseCard != card2)
                    frontVisual = ImageVisual(cardVisual)
            }
            onMouseClicked = {
                //mark the Card with a red layer
                frontVisual = CompoundVisual(
                    ImageVisual(cardVisual),
                    ColorVisual.RED.apply {
                        transparency = 0.2
                    })
                //runs the function removePair you choose two cards
                card1?.let { card ->
                    card2 = chooseCard
                    rootService.currentGame?.let {
                        rootService.playerActionService.removePair(card, chooseCard)
                    }
                    //store the first Card
                } ?: run {
                    card1 = chooseCard
                }
            }
        }
    }

    /**
     *  For the card in the game to initialize the Pyramid
     *
     *  The Pyramid view of the game.
     *
     *  @param cards The cards which you are visualize
     *  @param pyramidView The area which are the pyramid of cards
     *  @param cardImageLoader the visual the right card suit and card value
     *  from the card
     */
    private fun initializePyramidView(
        cards: Pyramid,
        pyramidView: Area<CardView>,
        cardImageLoader: CardImageLoader
    ) {
        var posX = 480
        var posY = -75
        val pyramidCards = cards.cards
        for (i in pyramidCards.indices) {
            posY += 125
            posX -= i * 90 + 45
            for (j in pyramidCards[i].indices) {
                val cardView = CardView(
                    height = 100,
                    width = 65,
                    posY = posY,
                    posX = posX,
                    front = ImageVisual(
                        cardImageLoader.frontImageFor(
                            pyramidCards[i][j].cardSuit,
                            pyramidCards[i][j].cardValue
                        )
                    ),
                    back = ImageVisual(cardImageLoader.backImage)
                )
                cardMap.add(cards.cards[i][j] to cardView)
                if (j == pyramidCards[i].size - 1 || j == 0) {
                    initializeRevealCards(cardView)
                }
                pyramidView.add(cardView)
                posX += 90
            }
        }
    }

    init {
        //color Magic Mint
        background = ColorVisual(156, 232, 194)
        addComponents(
            player2Label,
            player1Label,
            passButton,
            reserveStack,
            drawStack,
            pyramid,
            passLabel,
        )
    }

    /**
     *  The function build the game visually with all component ,
     *  the two player labels, draw stack , reserve stack, a button for
     *  pass and the pyramid of cards
     */
    override fun refreshAfterStartGame() {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame) { "No Game found" }
        val player1 = currentGame.player1
        val player2 = currentGame.player2
        player1Label.text = player1.name
        player2Label.text = player2.name
        player1Label.font = Font(size = 40, fontWeight = Font.FontWeight.BOLD, fontStyle = Font.FontStyle.OBLIQUE)
        player2Label.font = Font(size = 40, fontWeight = Font.FontWeight.NORMAL, fontStyle = Font.FontStyle.OBLIQUE)
        val cardImageLoader = CardImageLoader()
        cardMap.clear()
        reserveStack.clear()
        drawStack.clear()
        val entityDrawStack = currentGame.drawStack.cards
        for (i in entityDrawStack.indices) {
            initializeCardView(entityDrawStack[i], drawStack, cardImageLoader)
        }
        pyramid.clear()
        val entityPyramid = currentGame.pyramid
        initializePyramidView(entityPyramid, pyramid, cardImageLoader)
    }

    /**
     *  The function change the font weight of the current player
     *  to BOLD and the other font weight is normal
     */
    override fun refreshAfterChangePlayer() {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame) { "No Game found" }
        val currentPlayer = currentGame.currentPlayer
        if (currentPlayer == 1) {
            player1Label.font = Font(
                size = 40,
                fontWeight = Font.FontWeight.BOLD,
                fontStyle = Font.FontStyle.OBLIQUE
            )
            player2Label.font = Font(
                size = 40,
                fontWeight = Font.FontWeight.NORMAL,
                fontStyle = Font.FontStyle.OBLIQUE
            )
        }
        if (currentPlayer == 2) {
            player2Label.font = Font(size = 40, fontWeight = Font.FontWeight.BOLD, fontStyle = Font.FontStyle.OBLIQUE)
            player1Label.font = Font(size = 40, fontWeight = Font.FontWeight.NORMAL, fontStyle = Font.FontStyle.OBLIQUE)
        }
        //Special case if someone choose a card but want to pass or reveal a card
        if (card1 != null && card2 == null) {
            card1?.let {
                val card1View = cardMap.forward(card1!!)
                val cardImage = ImageVisual(CardImageLoader().frontImageFor(card1!!.cardSuit, card1!!.cardValue))
                card1View.frontVisual = cardImage
            }
            card1 = null
        }
    }

    /**
     *  The function visually flips the cards and move it from the draw
     *  stack to the reserve stack
     *
     *  @param isValid If the parameter is true the action is called
     */
    override fun refreshAfterRevealCard(isValid: Boolean) {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        if (isValid) {
            val drawnCard = cardMap.forward(currentGame.reserveStack.cards.first())
            playAnimation(
                SequentialAnimation(
                    FlipAnimation(
                        drawnCard,
                        fromVisual = drawnCard.backVisual,
                        toVisual = drawnCard.frontVisual,
                        duration = 1000
                    ).apply {
                        onFinished = {
                            drawnCard.showFront()
                        }
                    },
                    //to movew it to reverseStack
                    MovementAnimation.toComponentView(
                        componentView = drawnCard,
                        toComponentViewPosition = reserveStack,
                        scene = this,
                        duration = 750
                    ).apply {
                        onFinished = {
                            /*to add the card to the reserve stack in view and
                            remove it from draw stack in the view
                             */
                            drawnCard.removeFromParent()
                            reserveStack.push(drawnCard)
                            if (reserveStack.isNotEmpty()) {
                                initializeRevealCards(reserveStack.peek())
                            }
                        }
                    }
                )
            )
        }
    }

    /**
     *  The function makes a short pass animation
     */
    override fun refreshAfterPass() {
        playAnimation(
            MovementAnimation(
                componentView = passLabel,
                toX = 2280,
                duration = 3000
            )
        )
    }

    /**
     *  The function remove a pair from the View if is valid
     *
     *  @param isValid The parameter isValid is use if the card choice are valid
     */
    override fun refreshAfterRemovePair(isValid: Boolean) {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        if (isValid) {
            removeCardAnimation(card1)
            removeCardAnimation(card2)
            val pyramid = currentGame.pyramid.cards
            for (i in pyramid.indices) {
                val firstCardPyramid = cardMap.forward(pyramid[i].first())
                val lastCardPyramid = cardMap.forward(pyramid[i].last())
                if (firstCardPyramid.currentSide != CardView.CardSide.FRONT) {
                    lock()
                    playAnimation(
                        FlipAnimation(
                            firstCardPyramid,
                            duration = 1000,
                            fromVisual = firstCardPyramid.backVisual,
                            toVisual = firstCardPyramid.frontVisual
                        ).apply {
                            onFinished = {
                                initializeRevealCards(firstCardPyramid)
                                unlock()
                            }
                        }
                    )
                }
                if (lastCardPyramid.currentSide != CardView.CardSide.FRONT) {
                    lock()
                    playAnimation(
                        FlipAnimation(
                            lastCardPyramid,
                            duration = 1000,
                            fromVisual = lastCardPyramid.backVisual,
                            toVisual = lastCardPyramid.frontVisual
                        ).apply {
                            onFinished = {
                                unlock()
                                initializeRevealCards(lastCardPyramid)
                            }
                        }
                    )
                }
            }
        }
        //reset the cardView
        val card1View = card1?.let { cardMap.forward(it) }
        val card2View = card2?.let { cardMap.forward(it) }
        val card1Visual = card1?.let { ImageVisual(CardImageLoader().frontImageFor(it.cardSuit, it.cardValue)) }
        val card2Visual = card2?.let { ImageVisual(CardImageLoader().frontImageFor(it.cardSuit, it.cardValue)) }
        if (card1Visual != null) {
            card1View?.frontVisual = card1Visual
        }
        if (card2Visual != null) {
            card2View?.frontVisual = card2Visual
        }
        //make the temporary card1 and card2 vars null
        card1 = null
        card2 = null
    }

    /**
     *  The function remove the Card the Cards from the view
     *  as an Animation to make it go up above the y-Axis.At the end it removes it.
     *
     *  @param card The card parameter is to show which card
     *  the animation are make
     */
    private fun removeCardAnimation(card: Card?) {
        card?.let { card1 ->
            val chooseCard = cardMap.forward(card1)
            // make the card disappear
            playAnimation(
                MovementAnimation(
                    chooseCard,
                    duration = 1000,
                    toY = -800
                ).apply {
                    onFinished = {
                        chooseCard.removeFromParent()
                    }
                }
            )
        }
    }
}
