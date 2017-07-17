
object Main {

    def main(args: Array[String]): Unit = {

        val resourcesPath = getClass.getResource("/cards.json")
        val json = io.Source.fromFile(resourcesPath.getPath).mkString
        val cards = CardParser.parseFromJson(json)
    }
}
