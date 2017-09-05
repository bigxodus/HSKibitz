import java.util.UUID
import scala.collection.mutable

case class Card(id: UUID, title: String, completed: Boolean, order: Int)

object Card {
    private[this] val db: mutable.Map[UUID, Card] = mutable.Map.empty[UUID, Card]

    def get(id: UUID): Option[Card] = synchronized { db.get(id) }
    def list(): List[Card] = synchronized { db.values.toList }
    def save(t: Card): Unit = synchronized { db += (t.id -> t) }
    def delete(id: UUID): Unit = synchronized { db -= id }
}

case class CardNotFound(id: UUID) extends Exception {
    override def getMessage: String = s"Card (${id.toString}) not found."
}