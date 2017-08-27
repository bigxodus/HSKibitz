package model.action

import model.Card

sealed trait Action {}

sealed trait UserAction extends Action
sealed trait SystemAction extends Action

case class PickCardAction(pickedCard: Card) extends UserAction