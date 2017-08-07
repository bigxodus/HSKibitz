package com.waynlaw

import akka.actor.ActorSystem
import com.waynlaw.actors.KibitzSupervisor
import com.waynlaw.model.Card
import org.json4s._
import org.json4s.native.JsonMethods._

object Main {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem.create("hs-kibitz")

    system.actorOf(KibitzSupervisor.props, "kibitz-supervisor")

    implicit val formats = DefaultFormats

    val resourcesPath = getClass.getResource("/cards.json")
    val json = io.Source.fromFile(resourcesPath.getPath).mkString
    val cards = parse(json).extract[List[Card]]
  }
}
