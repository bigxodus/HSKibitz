package model

sealed abstract class ScoreKey(jsonKey : String)

object ScoreKey {
    case object Attack extends ScoreKey("")
    case object Health extends ScoreKey("")
}
