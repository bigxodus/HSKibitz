package com.waynlaw.actors

import com.waynlaw.actors.common.FileWatchActor
import akka.actor.{Actor, ActorLogging, Props}
import com.waynlaw.model.properties.LogProperties

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
}
