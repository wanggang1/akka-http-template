package org.gwgs.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.gwgs.actors.UserRegistryActor.ActionPerformed
import org.gwgs.models._
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val userJsonFormat = jsonFormat3(Employee)
  implicit val usersJsonFormat = jsonFormat1(Employees)

  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)
}
