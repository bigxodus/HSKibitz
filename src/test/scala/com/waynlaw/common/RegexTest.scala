package com.waynlaw.common

import org.scalatest.FunSuite

class RegexTest extends FunSuite{

  test("Regex Test") {
    val pattern = "TAG_CHANGE Entity=[a-zA-z0-9가-힣]+ tag=PLAYSTATE value=[a-zA-z0-9가-힣]+".r
    val str = "TAG_CHANGE Entity=현명 tag=PLAYSTATE value=LOST"

    str match {
      case matchedString if ((pattern.findPrefixMatchOf(str)).isDefined) =>
        Console println "isDefined"
      case _ =>
        Console println "empty"
    }
  }

  test("Regex Text") {
    val userPattern = "(?<=Entity=)[^\\s]+".r
    val statePattern = "(?<=value=).+$".r

    val str= "22:26:35.6031010 GameState.DebugPrintPower() - TAG_CHANGE Entity=카리스 tag=PLAYSTATE value=LOST"

    Console println userPattern.findFirstIn(str).get
    Console println statePattern.findFirstIn(str).get
  }
}
