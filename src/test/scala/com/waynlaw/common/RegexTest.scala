package com.waynlaw.common

import org.scalatest.FunSuite

class RegexTest extends FunSuite{

  test("Hello world") {
    val pattern = "TAG_CHANGE Entity=[a-zA-z0-9가-힣]+ tag=PLAYSTATE value=[a-zA-z0-9가-힣]+".r
    val str = "TAG_CHANGE Entity=현명 tag=PLAYSTATE value=LOST"

    str match {
      case matchedString if ((pattern.findPrefixMatchOf(str)).isDefined) =>
        Console println "isDefined"
      case _ =>
        Console println "empty"
    }
  }
}
