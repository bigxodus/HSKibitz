
import model.Card
import org.json4s.DefaultFormats
import org.json4s._
import org.json4s.native.JsonMethods._

object CardParser {

    def parseFromJson(json: String): List[Card] = {
        implicit val formats = DefaultFormats

        parse(json).extract[List[Card]]
    }
}
