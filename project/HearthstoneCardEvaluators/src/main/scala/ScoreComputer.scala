import model.Card
import model.ScoreKey

import scala.collection.immutable.HashMap

object ScoreComputer {

    def score(weightMap: HashMap[ScoreKey, Double])(card: Card): Double = {
        val attackScore = weightMap(ScoreKey.Attack) * card.attack.getOrElse(0.0)
        val healthScore = weightMap(ScoreKey.Health) * card.health.getOrElse(0.0)

        attackScore + healthScore
    }
}
