package model

import model.action.{Action, PickCardAction}
import model.gamestate.{WaitForUserPickCard, WaitForUserPlayCard}

object Simulator {
    def availActions(board: Board): Array[Action] = {
        board.state match {
            case WaitForUserPickCard() =>
                val player = board.currentPlayer
                val cards = player.cardsInHand
                cards.filter(_.cost <= player.mana).map(PickCardAction)
            case WaitForUserPlayCard(pickedCard: Card) =>
                Array()
        }
    }
}