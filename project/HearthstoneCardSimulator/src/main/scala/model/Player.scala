package model

case class Player(
    cardsInHand: Array[Card] = Array(),
    cardsInField: Array[Card] = Array(),
    cardsPlayed: Array[Card] = Array(),
    cardsInGrave: Array[Card] = Array(),
    mana: Int = 0,
    maxMana: Int = 0,
    OverloadedMana: Int = 0
)
