import model.Card
import org.json4s._
import org.json4s.native.JsonMethods._

object Main {
    def main(args:Array[String]): Unit = {
        implicit val formats = DefaultFormats

        val resourcesPath = getClass.getResource("/cards.json")
        val json = io.Source.fromFile(resourcesPath.getPath).mkString
        val cards = parse(json).extract[List[Card]]
    }
}
