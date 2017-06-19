package actors.common

import java.nio.file.Paths

import akka.actor.{Actor, ActorLogging, Props}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.Source
import org.apache.commons.io.input.{Tailer, TailerListenerAdapter}

import scala.concurrent.duration._

object FileWatchActor {
  def props(filePath: String) = {
    Props(classOf[FileWatchActor], filePath)
  }

  sealed trait FileWatchActorCommands

  case object Tick extends FileWatchActorCommands

}

class FileWatchActor(filePath: String) extends Actor with ActorLogging {

  import context.dispatcher
  import FileWatchActor._
  implicit val system = context.system
  implicit val materializer = ActorMaterializer()

  override def preStart(): Unit = {
    readContinuously(filePath, "UTF-8").map(_.toString).runForeach(println)
    log.info(s"log filePath : ${filePath}")
  }

  override def receive: Receive = {
    case msg =>
  }

  private def readContinuously[T](path: String, encoding: String): Source[String, _] =
    Source.queue[String](bufferSize = 1000, OverflowStrategy.fail).
      mapMaterializedValue { queue =>
        Tailer.create(Paths.get(path).toFile, new TailerListenerAdapter {
          override def handle(line: String): Unit = {
            queue.offer(line)
          }
        })
      }
}
