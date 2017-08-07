package com.waynlaw.model.properties

import com.typesafe.config.ConfigFactory

/**
  *
  * @author: Lawrence
  * @since: 2017. 6. 19.
  * @note:
  */
object LogProperties {

  private val config = ConfigFactory.load()
  val filePath = config.getString("log.file-path")

}
