import model.Property

import scala.collection.immutable.HashMap
import scala.util.Random

object Main {

    def main(args: Array[String]): Unit = {

        val resourcesPath = getClass.getResource("/cards.json")
        val json = io.Source.fromFile(resourcesPath.getPath).mkString
        val cards = CardParser.parseFromJson(json)
                              .filter(card => card.cost.isDefined && 0 < card.cost.get)

        val initWeights: HashMap[Property, Double] = HashMap(
            Property.Attack -> Random.nextDouble(),
            Property.Health -> Random.nextDouble(),
            Property.BattleCry -> Random.nextDouble(),
            Property.DeathRattle -> Random.nextDouble()
        )

        println(GradientDescent.eval(cards)(initWeights))
    }
}
