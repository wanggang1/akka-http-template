package org.gwgs.repository

import java.time.{ Instant, OffsetDateTime, ZoneOffset }
import reactivemongo.bson.{ BSONDateTime, BSONHandler, Macros }

import org.gwgs.models.Employee

object Implicits {

  implicit object BSONDateTimeHandler extends BSONHandler[BSONDateTime, OffsetDateTime] {
    def read(bson: BSONDateTime): OffsetDateTime =
      OffsetDateTime.ofInstant(Instant.ofEpochMilli(bson.value), ZoneOffset.UTC)

    def write(date: OffsetDateTime) = BSONDateTime(date.toInstant.toEpochMilli)
  }

  implicit val fileMetaData = Macros.handler[Employee]
}
