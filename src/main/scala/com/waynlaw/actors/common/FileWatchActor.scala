package com.waynlaw.actors.common

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
    log.info(s"log filePath : ${filePath}")
    readContinuously(filePath, "UTF-8").map(_.toString).runForeach(msg => self ! msg)
  }

  override def receive: Receive = {
    case line : String =>
      val endPattern = "TAG_CHANGE Entity=[a-zA-z0-9가-힣]+ tag=PLAYSTATE value=[a-zA-z0-9가-힣]+".r

      val startPattern = "CREATE_GAME".r

      val status = line match {
        // 게임 시작
        case matchedString if startPattern.findFirstIn(line).isDefined =>
          "Start"
        // 게임 종료
        case matchedString if endPattern.findFirstIn(line).isDefined =>
          "End"
        case _ =>
          ""
      }

      Console println status + "/" + line
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
