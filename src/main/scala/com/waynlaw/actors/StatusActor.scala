package com.waynlaw.actors

import akka.actor.{Actor, ActorLogging, Props}
import com.waynlaw.model.Status

/**
  *
  * @author: Lawrence
  * @since: 2017. 7. 10.
  * @note:
  */
object StatusActor {
  def props: Props = {
    Props(classOf[StatusActor])
  }
}

class StatusActor extends Actor with ActorLogging{

  var myDeck: List[Status] = List.empty

  override def receive: Receive = {
    case status: Status =>
      Console println status
  }
}
