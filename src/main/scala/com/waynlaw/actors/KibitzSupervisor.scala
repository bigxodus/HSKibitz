package com.waynlaw.actors

import akka.actor.SupervisorStrategy.Resume
import com.waynlaw.actors.common.FileWatchActor
import akka.actor.{Actor, ActorLogging, OneForOneStrategy, Props}
import com.waynlaw.model.properties.LogProperties

import scala.concurrent.duration._

object KibitzSupervisor {
  def props = Props(classOf[KibitzSupervisor])
}

class KibitzSupervisor extends Actor with ActorLogging{

  private val logFilePath = LogProperties.filePath
  private val logWatcher = context.actorOf(FileWatchActor.props(logFilePath), "log-watcher")

  override def preStart(): Unit = {
    log.info("I'm arrived")
  }

  override def receive: Receive = {
    case msg =>
      log.info(s"${msg}")
  }

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 1, withinTimeRange = 1 minute) {
      case _ => Resume
    }
}
