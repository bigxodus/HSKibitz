import model.{Card, Property}

import scala.collection.immutable.HashMap

object GradientDescent {
    private def step = -0.001

    private def maximumStep = 1000

    def eval(card: List[Card])(initWeights:HashMap[Property, Double]): HashMap[Property, Double] = {
        (0 until maximumStep).foldLeft(initWeights){ case(weights, _) => {
            val derivation = LinearAlgebra.derivative(weights)(x => ScoreComputer.evaluateWeightUsingCard(card)(x))
            weights.map{case (k, v) => (k, v + derivation(k) * step)}
        }}
    }
}
