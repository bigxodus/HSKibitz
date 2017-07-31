package model

sealed abstract class Property(val score: Card => Double)

object Property {
    case object Attack extends Property(_.attack.getOrElse(0))
    case object Health extends Property(_.health.getOrElse(0))
    case object BattleCry extends Property(card => if (card.mechanics.contains("BATTLECRY")) 1 else 0)
    case object DeathRattle extends Property(card => if (card.mechanics.contains("DEATHRATTLE")) 1 else 0)
}
