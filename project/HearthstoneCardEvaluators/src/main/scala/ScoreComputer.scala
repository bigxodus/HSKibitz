import model.Card
import model.Property

import scala.collection.immutable.HashMap

object ScoreComputer {

    def score(weightMap: HashMap[Property, Double])(card: Card): Double = {
        val totalValue: Double = weightMap.map{ case (k, weight) => k.score(card) * weight }.sum

        totalValue / card.cost.getOrElse(0.0)
    }

    def evaluateWeightUsingCard(cards: List[Card])(x: HashMap[Property, Double]): Double = {
        val avgScore = cards.map(card => ScoreComputer.score(x)(card)).sum / cards.size
        Math.abs(1.0 - avgScore)
    }
}
