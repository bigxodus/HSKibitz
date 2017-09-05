import model._
import model.action.PickCardAction
import model.gamestate.WaitForUserPickCard
import org.scalatest._

class BasicTest extends FlatSpec {

    "A Card in hand" should "be selectable" in {
        val board = Board(
            Player(cardsInHand = Array(
                Card(cardType = 1),
                Card(cardType = 2),
            )),
            Player(),
            Turn.TURN_PLAYER_1,
            WaitForUserPickCard()
        )
        val availActions = Simulator.availActions(board)
        assert(availActions === Array(
            PickCardAction(Card(cardType = 1)),
            PickCardAction(Card(cardType = 2))
        ))
    }
}
