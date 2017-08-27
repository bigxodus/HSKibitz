package model.gamestate

import model.Card

sealed trait GameState {

}

case class WaitForUserPickCard() extends GameState
case class WaitForUserPlayCard(pickedCard: Card) extends GameState