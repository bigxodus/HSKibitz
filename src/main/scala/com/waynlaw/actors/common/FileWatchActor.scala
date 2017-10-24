package com.waynlaw.actors.common

import java.nio.file.Paths

import akka.actor.{Actor, ActorLogging, Props}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.Source
import com.waynlaw.Main.getClass
import com.waynlaw.model.Card
import org.apache.commons.io.input.{Tailer, TailerListenerAdapter}
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods.parse

import scala.collection.mutable.ArrayBuffer
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

  var deck = ArrayBuffer.empty[String]

  override def preStart(): Unit = {
    log.info(s"log filePath : ${filePath}")
    readContinuously(filePath, "UTF-8").map(_.toString).runForeach(msg => self ! msg)
  }

  implicit val formats = DefaultFormats

  val resourcesPath = getClass.getResource("/cards.json")
  val json = io.Source.fromFile(resourcesPath.getPath).mkString
  val cards = parse(json).extract[List[Card]]

  val cardsMap = cards.map(x => (x.id, x.name)).toMap

  override def receive: Receive = {
    case line: String =>

      // GAME
      val GAME_STATE_RE = "GameState".r
      val METHOD_RE = "(?<=GameState.).*(?=\\(\\))".r

      // DebugPrintPower
      val SHOW_ENTITY = "SHOW_ENTITY".r

      // BlockType=PLAY
      val BLOCK_START = "BLOCK_START BlockType=PLAY".r

      val TAG_CHANGE_RE = "TAG_CHANGE".r

      // 일단 value=1 까지 살펴보면 Play 데이터를 얻을 수 있을 것 같다.
      val JUST_PLAYED_RE = "JUST_PLAYED value=1".r

      val CARD_ID_RE = "(?<=[cC]ardI[dD]=)[^\\s]+".r
      val PLAYER1_RE = "player=1".r
      val PLAYER2_RE = "player=2".r

      GAME_STATE_RE.findFirstIn(line).isDefined match {
        case true =>
          METHOD_RE.findFirstMatchIn(line).get.toString match {
            case "DebugPrintPower" =>
              // SHOW_ENTITY를 보면 상대편은 카드를 낼 때, 나는 카드가 들어올 때를 알 수 있다.
              SHOW_ENTITY.findFirstMatchIn(line) match {
                case Some(_) =>
                  if (CARD_ID_RE.findFirstMatchIn(line).isDefined && PLAYER1_RE.findFirstMatchIn(line).isDefined) {
                    // 상대 플레이 카드
                    val cardId: String = CARD_ID_RE.findFirstMatchIn(line).get.toString
                    Console println s"Opponent's play: ${getName(cardId)}"
                  } else if (CARD_ID_RE.findFirstMatchIn(line).isDefined && PLAYER2_RE.findFirstMatchIn(line).isDefined) {
                    // 내 드로우 카드
                    val cardId: String = CARD_ID_RE.findFirstMatchIn(line).get.toString
                    Console println s"My draw: ${getName(cardId)}"
                  } else {
                    Console println line
                  }
                case None =>
                }


              BLOCK_START.findFirstMatchIn(line) match {
                case Some(_) =>
                  if (CARD_ID_RE.findFirstMatchIn(line).isDefined && PLAYER2_RE.findFirstMatchIn(line).isDefined) {
                    // 내 플레이 카드 아닌듯
                    val cardId: String = CARD_ID_RE.findFirstMatchIn(line).get.toString
//                    Console println s"My play: ${getName(cardId)} : ${line}"
                  }
                case None => {
                }
              }

              TAG_CHANGE_RE.findFirstMatchIn(line) match {
                case Some(_) =>
                  if (JUST_PLAYED_RE.findFirstMatchIn(line).isDefined && PLAYER2_RE.findFirstMatchIn(line).isDefined) {
                    // 내 플레이 카드
                    val cardId: String = CARD_ID_RE.findFirstMatchIn(line).get.toString
                    Console println s"My play: ${getName(cardId)} : ${line}"
                  }
                case None => {
                }
              }
                case "DebugPrintPowerList" =>
            case "DebugPrintOptions" =>
            case "SendChoices" =>
              if (CARD_ID_RE.findFirstMatchIn(line).isDefined) {
                val cardId: String = CARD_ID_RE.findFirstMatchIn(line).get.toString
                Console println s"Mulligan of mine: ${getName(cardId)} : ${line}"
              }
            case "DebugPrintEntitiesChosen" =>
//              Console println line
            case "DebugPrintEntityChoices" =>
//              Console println line
            case _ =>

          }
        case _ =>
      }
  }

  def powerHandler(line: String) = {

  }

  def getName(id: String): String = {
    s"${cardsMap.getOrElse(id, None).getOrElse("None")} (;${id})"
  }

  /*
    override def receive: Receive = {
      case line: String =>

        val startPattern = "GameState.DebugPrintPower\\(\\) - CREATE_GAME".r

        val winnerPattern = "GameState.DebugPrintPower\\(\\) - TAG_CHANGE Entity=[a-zA-z0-9가-힣]+ tag=PLAYSTATE value=WON".r
        val loserPattern = "GameState.DebugPrintPower\\(\\) - TAG_CHANGE Entity=[a-zA-z0-9가-힣]+ tag=PLAYSTATE value=LOST".r

        val endPattern = "GameState.DebugPrintPower\\(\\) - TAG_CHANGE Entity=GameEntity tag=STATE value=COMPLETE".r

        // 카드 Draw
        val showEntityPlayer1 = "GameState.DebugPrintPower\\(\\) -     SHOW_ENTITY - .*player=1.*".r
        val showEntityPlayer2 = "GameState.DebugPrintPower\\(\\) -     SHOW_ENTITY - .*player=2.*".r

        // 현재 플레이 사용자
        val currentPlayer = "GameState.DebugPrintPower\\(\\) -     TAG_CHANGE Entity=[a-zA-z0-9가-힣]+ tag=CURRENT_PLAYER value=1".r

        // 카드 Play
        val playCardPlayer1 = "GameState.DebugPrintPower\\(\\) - BLOCK_START BlockType=PLAY .*player=1.*".r
        val playCardPlayer2 = "GameState.DebugPrintPower\\(\\) - BLOCK_START BlockType=PLAY .*player=2.*".r

        val entityPattern = "(?<=Entity=)[^\\s]+".r
        val statePattern = "(?<=value=).+$".r
        val cardIdPattern = "(?<=CardID=).+$".r

        val cardidPattern = "(?<=cardId=)[^\\s]+".r

        val status = line match {
          // 게임 시작
          case matchedString if startPattern.findFirstIn(line).isDefined =>
            Console println "Game Start"
          // 게임 종료
          case matchedString if endPattern.findFirstIn(line).isDefined =>
            Console println "Game End"
            deck.clear()
          // 승자
          case matchedString if winnerPattern.findFirstIn(line).isDefined =>
            Console println "승자 : " + entityPattern.findFirstIn(matchedString).get
          // 패자
          case matchedString if loserPattern.findFirstIn(line).isDefined =>
            Console println "패자 : " + entityPattern.findFirstIn(matchedString).get
          //        case matchedString if cardId.findFirstIn(line).isDefined =>
          //          Console println "카드 Id : " + matchedString


          case matchedString if showEntityPlayer1.findFirstIn(line).isDefined =>
            Console println "Show Entity Player1: " + cardIdPattern.findFirstMatchIn(matchedString).get
          // Card 찾음
          //          deck += cardIdPattern.findFirstMatchIn(matchedString).get.toString()
          case matchedString if showEntityPlayer2.findFirstIn(line).isDefined =>
            Console println "Show Entity Player2: " + cardIdPattern.findFirstMatchIn(matchedString).get.toString()
            deck += cardIdPattern.findFirstMatchIn(matchedString).get.toString()
          case matchedString if currentPlayer.findFirstIn(line).isDefined =>
            Console println "현재 플레이어: " + entityPattern.findFirstIn(matchedString).get
            if (entityPattern.findFirstIn(matchedString).get == "현명") {
              Console println ""
              Console println "현재 보유 덱 : " + deck.mkString(", ")
              Console println ""
            }
          case matchedString if playCardPlayer2.findFirstMatchIn(line).isDefined =>
            Console println "플레이 카드 : " + cardidPattern.findFirstIn(matchedString).get
            deck - cardidPattern.findFirstIn(matchedString).get
          case _ =>
            ""
        }

      //      Console println status + "/" + line
    }
  */


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
