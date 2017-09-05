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

      val startPattern = "GameState.DebugPrintPower\\(\\) - CREATE_GAME".r

      val winnerPattern = "GameState.DebugPrintPower\\(\\) - TAG_CHANGE Entity=[a-zA-z0-9가-힣]+ tag=PLAYSTATE value=WON".r
      val loserPattern = "GameState.DebugPrintPower\\(\\) - TAG_CHANGE Entity=[a-zA-z0-9가-힣]+ tag=PLAYSTATE value=LOST".r

      val endPattern = "GameState.DebugPrintPower\\(\\) - TAG_CHANGE Entity=GameEntity tag=STATE value=COMPLETE".r

//      val cardId = "CardID=".r

      // 카드 Draw
      val showEntityPlayer1 = "GameState.DebugPrintPower\\(\\) -     SHOW_ENTITY - .*player=1.*".r
      val showEntityPlayer2 = "GameState.DebugPrintPower\\(\\) -     SHOW_ENTITY - .*player=2.*".r

      // 현재 플레이 사용자
      val currentPlayer = "GameState.DebugPrintPower\\(\\) -     TAG_CHANGE Entity=[a-zA-z0-9가-힣]+ tag=CURRENT_PLAYER value=1".r

      val userPattern = "(?<=Entity=)[^\\s]+".r
      val statePattern = "(?<=value=).+$".r

      val status = line match {
        // 게임 시작
        case matchedString if startPattern.findFirstIn(line).isDefined =>
          Console println "Game Start"
        // 게임 종료
        case matchedString if endPattern.findFirstIn(line).isDefined =>
          Console println "Game End"
        case matchedString if winnerPattern.findFirstIn(line).isDefined =>
          Console println "승자 : " + userPattern.findFirstIn(matchedString).get
        case matchedString if loserPattern.findFirstIn(line).isDefined =>
          Console println "패자 : " + userPattern.findFirstIn(matchedString).get
//        case matchedString if cardId.findFirstIn(line).isDefined =>
//          Console println "카드 Id : " + matchedString
        case matchedString if showEntityPlayer1.findFirstIn(line).isDefined =>
          Console println "Show Entity : " + matchedString
        case matchedString if showEntityPlayer2.findFirstIn(line).isDefined =>
//          Console println "Show Entity : " + matchedString
        case matchedString if currentPlayer.findFirstIn(line).isDefined =>
          Console println "현재 플레이어: " + userPattern.findFirstIn(matchedString).get
        case _ =>
          ""
      }

//      Console println status + "/" + line
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
