package com.waynlaw.model

case class Card(
    id: String,
    mechanics: List[String],
    attack: Option[Double],
    cardClass: Option[String],
    cost: Option[Double],
    dbfId: Option[Double],
    health: Option[Double],
    name: Option[String],
    playerClass: Option[String],
    playRequirements: Option[Map[String, Int]],
    set: Option[String],
    text: Option[String],
    `type`: Option[String]
)