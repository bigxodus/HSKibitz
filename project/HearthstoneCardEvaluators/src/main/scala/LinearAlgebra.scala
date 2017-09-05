import scala.collection.immutable.HashMap

object LinearAlgebra {

    private def epsilon = 0.0001

    def derivative[T](weights: HashMap[T, Double])(f: (HashMap[T, Double] => Double)): HashMap[T, Double] = {
        weights.map{case (k, v) => (k, (f(weights.updated(k, v + epsilon)) - f(weights)) / epsilon)}
    }
}
