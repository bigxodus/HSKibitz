import java.util.UUID

import com.twitter.app.Flag
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.server.TwitterServer
import com.twitter.util.Await
import io.finch.{Application, Endpoint}
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._

object Main extends TwitterServer {

    val port: Flag[Int] = flag("port", 8081, "TCP port for HTTP server")

    def postedCard: Endpoint[Card] = jsonBody[UUID => Card].map(_(UUID.randomUUID()))

    def getCards: Endpoint[List[Card]] = get("cards") {
        Ok(Card.list())
    }

    def postCards: Endpoint[Card] = post("cards" :: postedCard) { t: Card =>
        Card.save(t)

        Created(t)
    }

    val api: Service[Request, Response] = (
            getCards :+: postCards
        ).handle({
        case e: CardNotFound => NotFound(e)
    }).toServiceAs[Application.Json]

    def main(): Unit = {
        log.info("Serving the Card Evaluation application")

        val server = Http.server
            .withStatsReceiver(statsReceiver)
            .serve(s":${port()}", api)

        onExit { server.close() }

        Await.ready(adminHttpServer)
    }
}
